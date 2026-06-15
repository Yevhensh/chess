package chess.main.sample.manage;


import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Position;
import chess.main.sample.figures.instances.King;
import chess.main.sample.figures.instances.Pawn;
import chess.main.sample.figures.movements.KingMove;
import chess.main.sample.guimanage.DeckLayoutManager;
import chess.main.sample.storage.ChessPositionsStorage;
import chess.main.sample.utils.ChessUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        List<Integer> attacks = new ArrayList<>();
        for (Map.Entry<Integer, Figure> entry : positions.entrySet()) {
            Figure figure = entry.getValue();
            if (figure.getPosition().equals(oppositePosition)) {
                if (figure instanceof King) {
                    attacks.addAll(new KingMove().getBasicMoves(positions, entry.getKey(), figure));
                } else if (figure instanceof Pawn) {
                    int row = ChessUtils.getRow(entry.getKey());
                    int col = ChessUtils.getCol(entry.getKey());
                    int forwardDir = (figure.getPosition() == Position.WHITE) ? -1 : 1;
                    int[] captureCols = {col - 1, col + 1};
                    for (int nextCol : captureCols) {
                        if (nextCol >= 0 && nextCol < 8) {
                            int nextRow = row + forwardDir;
                            if (nextRow >= 0 && nextRow < 8) {
                                attacks.add(ChessUtils.getIndex(nextRow, nextCol));
                            }
                        }
                    }
                } else {
                    attacks.addAll(figure.getAllAvailableMovements(entry.getKey()));
                }
            }
        }
        return attacks;
    }

    public boolean isCheck(ChessPositionsStorage positionsStorage, Position position) {
        Position oppositePosition = (position == Position.BLACK) ? Position.WHITE : Position.BLACK;
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

        for (Map.Entry<Integer, Figure> entry : currentPositions.entrySet()) {
            Figure figure = entry.getValue();
            if (figure.getPosition() == side) {
                int fromInd = entry.getKey();
                List<Integer> possibleMoves = figure.getAllAvailableMovements(fromInd);
                for (int toInd : possibleMoves) {
                    if (isMoveLegal(fromInd, toInd, side)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isCheckmate(Position side) {
        return isCheck(ChessPositionsStorage.getGlobalStorage(), side) && !hasAnyLegalMoves(side);
    }

    public boolean isStalemate(Position side) {
        return !isCheck(ChessPositionsStorage.getGlobalStorage(), side) && !hasAnyLegalMoves(side);
    }
}
