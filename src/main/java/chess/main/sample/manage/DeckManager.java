package chess.main.sample.manage;


import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Position;
import chess.main.sample.figures.instances.King;
import chess.main.sample.figures.instances.Pawn;
import chess.main.sample.figures.movements.KingMove;
import chess.main.sample.guimanage.DeckLayoutManager;
import chess.main.sample.storage.ChessPositionsStorage;
import chess.main.sample.utils.ChessUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DeckManager {

    private static DeckManager instance;

    private static ChessPositionsStorage chessPositionsStorage = ChessPositionsStorage.getGlobalStorage();

    public static DeckManager getInstance() {
        if (instance == null) {
            instance = new DeckManager();
        }
        return instance;
    }

    private DeckManager() {
    }

    public boolean isAllyFigureOnDeckCell(Map<Integer, Figure> positions, int deckCell, Position position) {
        return (!isEmptyDeckCell(positions, deckCell) && !isOppositeFigureOnDeckCell(positions, deckCell, position));
    }

    public boolean isEmptyDeckCell(Map<Integer, Figure> positions, int deckCell) {
        return (positions.get(deckCell) == null);
    }

    public boolean isOppositeFigureOnDeckCell(Map<Integer, Figure> positions, int deckCell, Position position) {
        Figure figure = positions.get(deckCell);
        return (figure != null) && (figure.getPosition() != position);
    }

    public List<Integer> getAllAvailableSiteMovements(Map<Integer, Figure> positions, Position position) {
        return positions.entrySet()
                .stream()
                .filter(item -> item.getValue().getPosition().equals(position))
                .flatMap(item -> item.getValue().getAllAvailableMovements(item.getKey()).stream())
                .collect(Collectors.toList());
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
            default -> figure.getAllAvailableMovements(entry.getKey());
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
        ChessPositionsStorage storage = ChessPositionsStorage.getGlobalStorage();
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

    public void makeIndependentTurn(Map<Integer, Figure> positionsContainer, int fromInd, int toInd) {
        Figure figure = positionsContainer.get(fromInd);
        positionsContainer.remove(fromInd);
        positionsContainer.put(toInd, figure);
    }

    public void makeTurn(int fromInd, int toInd) {
        ChessPositionsStorage storage = ChessPositionsStorage.getGlobalStorage();
        Map<Integer, Figure> positionsContainer = storage.getPositionsContainer();
        Figure figure = positionsContainer.get(fromInd);

        DeckLayoutManager.getInstance().makeTurn(fromInd, toInd, figure);

        positionsContainer.remove(fromInd);
        positionsContainer.put(toInd, figure);

        if (figure instanceof King) {
            if (figure.getPosition() == Position.WHITE) storage.setWhiteKingIndex(toInd);
            else storage.setBlackKingIndex(toInd);
        }
    }

    public boolean hasAnyLegalMoves(Position side) {
        ChessPositionsStorage storage = ChessPositionsStorage.getGlobalStorage();
        Map<Integer, Figure> currentPositions = new HashMap<>(storage.getPositionsContainer());

        return currentPositions.entrySet().stream()
                .filter(entry -> entry.getValue().getPosition() == side)
                .anyMatch(entry -> {
                    int fromInd = entry.getKey();
                    return entry.getValue().getAllAvailableMovements(fromInd).stream()
                            .anyMatch(toInd -> isMoveLegal(fromInd, toInd, side));
                });
    }

    public boolean isCheckmate(Position side) {
        return isCheck(ChessPositionsStorage.getGlobalStorage(), side) && !hasAnyLegalMoves(side);
    }

    public boolean isStalemate(Position side) {
        return !isCheck(ChessPositionsStorage.getGlobalStorage(), side) && !hasAnyLegalMoves(side);
    }
}
