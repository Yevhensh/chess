package chess.main.sample.manage;


import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Position;
import chess.main.sample.figures.instances.Empty;
import chess.main.sample.figures.instances.King;
import chess.main.sample.figures.instances.Pawn;
import chess.main.sample.figures.instances.Queen;
import chess.main.sample.figures.movements.KingMove;
import chess.main.sample.game.GameState;
import chess.main.sample.game.Move;
import chess.main.sample.guimanage.DeckLayoutManager;
import chess.main.sample.storage.ChessPositionsStorage;
import chess.main.sample.utils.ChessUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DeckManager {

    private final ChessPositionsStorage storage;
    private final GameState gameState;
    private DeckLayoutManager layoutManager;

    public DeckManager(ChessPositionsStorage storage, GameState gameState) {
        this.storage = storage;
        this.gameState = gameState;
    }

    public void setLayoutManager(DeckLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }



    public boolean isOppositeFigureOnDeckCell(Map<Integer, Figure> positions, int deckCell, Position position) {
        return ChessUtils.isOpposite(positions, deckCell, position);
    }


    public List<Integer> getAllOppositeSiteAttacks(Map<Integer, Figure> positions, Position oppositePosition) {
        return positions.entrySet().stream()
                .filter(entry -> entry.getValue().getPosition().equals(oppositePosition))
                .flatMap(entry -> getAttacksForFigure(positions, entry).stream())
                .collect(Collectors.toList());
    }

    private List<Integer> getAttacksForFigure(Map<Integer, Figure> positions, Map.Entry<Integer, Figure> entry) {
        Figure figure = entry.getValue();
        return switch (figure) {
            case King king -> new KingMove().getBasicMoves(positions, entry.getKey(), king);
            case Pawn pawn -> getPawnAttacks(entry.getKey(), pawn);
            default -> figure.getAllAvailableMovements(positions, entry.getKey());
        };
    }

    private List<Integer> getPawnAttacks(int deckCell, Pawn pawn) {
        int row = ChessUtils.getRow(deckCell);
        int col = ChessUtils.getCol(deckCell);
        int forwardDir = pawn.getPosition() == Position.WHITE ? -1 : 1;

        return IntStream.of(col - 1, col + 1)
                .filter(nextCol -> ChessUtils.isValid(row + forwardDir, nextCol))
                .map(nextCol -> ChessUtils.getIndex(row + forwardDir, nextCol))
                .boxed()
                .collect(Collectors.toList());
    }

    public boolean isCheck(ChessPositionsStorage positionsStorage, Position position) {
        Position oppositePosition = switch (position) {
            case BLACK -> Position.WHITE;
            case WHITE -> Position.BLACK;
        };
        List<Integer> oppositeAttacks = getAllOppositeSiteAttacks(positionsStorage.getPositionsContainer(), oppositePosition);

        int kingIndex = (position == Position.BLACK) ? positionsStorage.getBlackKingIndex() : positionsStorage.getWhiteKingIndex();

        return oppositeAttacks.contains(kingIndex);
    }

    public boolean isMoveLegal(int fromInd, int toInd, Position side) {
        Map<Integer, Figure> currentPositions = storage.getPositionsContainer();

        Figure movingFigure = currentPositions.get(fromInd);
        Figure capturedFigure = currentPositions.get(toInd);

        int oldWhiteKingIndex = storage.getWhiteKingIndex();
        int oldBlackKingIndex = storage.getBlackKingIndex();

        // Simulate move
        currentPositions.remove(fromInd);
        currentPositions.put(toInd, movingFigure);
        if (movingFigure instanceof King) {
            if (side == Position.WHITE) storage.setWhiteKingIndex(toInd);
            else storage.setBlackKingIndex(toInd);
        }

        boolean isCheck = isCheck(storage, side);

        // Revert move
        currentPositions.put(fromInd, movingFigure);
        if (capturedFigure != null) {
            currentPositions.put(toInd, capturedFigure);
        } else {
            currentPositions.remove(toInd);
        }
        storage.setWhiteKingIndex(oldWhiteKingIndex);
        storage.setBlackKingIndex(oldBlackKingIndex);

        return !isCheck;
    }

    public void makeTurn(int fromInd, int toInd) {
        Map<Integer, Figure> positionsContainer = storage.getPositionsContainer();
        Figure figure = positionsContainer.get(fromInd);
        Figure captured = positionsContainer.get(toInd);
        boolean wasPromoted = false;

        // Check for Pawn Promotion
        if (figure instanceof Pawn) {
            int row = ChessUtils.getRow(toInd);
            if ((figure.getPosition() == Position.WHITE && row == 0) ||
                (figure.getPosition() == Position.BLACK && row == 7)) {
                figure = new Queen(figure.getPosition());
                wasPromoted = true;
            }
        }

        gameState.getHistoryManager().addMove(new Move(fromInd, toInd, positionsContainer.get(fromInd), captured, wasPromoted));

        layoutManager.makeTurn(fromInd, toInd, figure);

        positionsContainer.remove(fromInd);
        positionsContainer.put(toInd, figure);

        if (figure instanceof King) {
            if (figure.getPosition() == Position.WHITE) storage.setWhiteKingIndex(toInd);
            else storage.setBlackKingIndex(toInd);
        }
    }

    public void undoMove() {
        Move move = gameState.getHistoryManager().undo();
        if (move == null) return;

        Map<Integer, Figure> positions = storage.getPositionsContainer();
        positions.put(move.fromIndex(), move.movedFigure());
        if (move.capturedFigure() != null) {
            positions.put(move.toIndex(), move.capturedFigure());
        } else {
            positions.remove(move.toIndex());
        }

        if (move.movedFigure() instanceof King) {
            if (move.movedFigure().getPosition() == Position.WHITE) storage.setWhiteKingIndex(move.fromIndex());
            else storage.setBlackKingIndex(move.fromIndex());
        }

        layoutManager.makeTurn(move.toIndex(), move.fromIndex(), move.movedFigure());
        if (move.capturedFigure() != null) {
            layoutManager.renderCellAtIndex(move.toIndex(), move.capturedFigure());
        } else {
            layoutManager.renderCellAtIndex(move.toIndex(), new Empty());
        }

        gameState.switchTurn();
        layoutManager.updateStatusMessage();
    }

    public void redoMove() {
        Move move = gameState.getHistoryManager().redo();
        if (move == null) return;

        Figure figure = move.movedFigure();
        if (move.wasPromoted()) {
            figure = new Queen(figure.getPosition());
        }

        Map<Integer, Figure> positions = storage.getPositionsContainer();
        positions.remove(move.fromIndex());
        positions.put(move.toIndex(), figure);

        if (figure instanceof King) {
            if (figure.getPosition() == Position.WHITE) storage.setWhiteKingIndex(move.toIndex());
            else storage.setBlackKingIndex(move.toIndex());
        }

        layoutManager.makeTurn(move.fromIndex(), move.toIndex(), figure);

        gameState.switchTurn();
        layoutManager.updateStatusMessage();
    }

    public boolean hasAnyLegalMoves(Position side) {
        Map<Integer, Figure> currentPositions = new HashMap<>(storage.getPositionsContainer());

        return currentPositions.entrySet().stream()
                .filter(entry -> entry.getValue().getPosition() == side)
                .anyMatch(entry -> {
                    int fromInd = entry.getKey();
                    return entry.getValue().getAllAvailableMovements(currentPositions, fromInd).stream()
                            .anyMatch(toInd -> isMoveLegal(fromInd, toInd, side));
                });
    }

    public boolean isCheckmate(Position side) {
        return isCheck(storage, side) && !hasAnyLegalMoves(side);
    }

    public boolean isStalemate(Position side) {
        return !isCheck(storage, side) && !hasAnyLegalMoves(side);
    }
}
