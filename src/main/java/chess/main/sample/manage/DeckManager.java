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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DeckManager {

    private static DeckManager instance;

    public static DeckManager getInstance() {
        if (instance == null) {
            instance = new DeckManager();
        }
        return instance;
    }

    private DeckManager() {
    }

    public boolean isAllyFigureOnDeckCell(int deckCell, Position position) {
        Figure figure = getFigureAt(deckCell);
        return figure != null && figure.getPosition() == position;
    }

    public boolean isEmptyDeckCell(int deckCell) {
        return getFigureAt(deckCell) == null;
    }

    public boolean isOppositeFigureOnDeckCell(int deckCell, Position position) {
        Figure figure = getFigureAt(deckCell);
        return figure != null && figure.getPosition() != position;
    }

    private Figure getFigureAt(int deckCell) {
        return ChessPositionsStorage.getGlobalStorage().getFigureByDeckCell(deckCell);
    }

    public List<Integer> getAllOppositeSiteAttacks(Position oppositePosition) {
        Map<Integer, Figure> positions = ChessPositionsStorage.getGlobalStorage().getPositionsContainer();
        List<Integer> attacks = new ArrayList<>();
        for (Map.Entry<Integer, Figure> entry : positions.entrySet()) {
            Figure figure = entry.getValue();
            if (figure.getPosition().equals(oppositePosition)) {
                attacks.addAll(getFigureAttacks(entry.getKey(), figure));
            }
        }
        return attacks;
    }

    private List<Integer> getFigureAttacks(int index, Figure figure) {
        if (figure instanceof King) {
            return new KingMove().getBasicMoves(index, figure);
        } else if (figure instanceof Pawn) {
            return getPawnAttacks(index, figure);
        } else {
            return figure.getAllAvailableMovements(index);
        }
    }

    private List<Integer> getPawnAttacks(int index, Figure figure) {
        List<Integer> attacks = new ArrayList<>();
        int row = ChessUtils.getRow(index);
        int col = ChessUtils.getCol(index);
        int forwardDir = (figure.getPosition() == Position.WHITE) ? -1 : 1;
        int nextRow = row + forwardDir;
        if (nextRow >= 0 && nextRow < 8) {
            for (int dc : new int[]{-1, 1}) {
                int nextCol = col + dc;
                if (nextCol >= 0 && nextCol < 8) {
                    attacks.add(ChessUtils.getIndex(nextRow, nextCol));
                }
            }
        }
        return attacks;
    }

    public boolean isCheck(ChessPositionsStorage positionsStorage, Position position) {
        Position oppositePosition = (position == Position.BLACK) ? Position.WHITE : Position.BLACK;
        List<Integer> oppositeAttacks = getAllOppositeSiteAttacks(oppositePosition);
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
        updateKingIndex(storage, movingFigure, toInd);

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
        ChessPositionsStorage storage = ChessPositionsStorage.getGlobalStorage();
        Map<Integer, Figure> positionsContainer = storage.getPositionsContainer();
        Figure figure = positionsContainer.get(fromInd);

        DeckLayoutManager.getInstance().makeTurn(fromInd, toInd, figure);

        positionsContainer.remove(fromInd);
        positionsContainer.put(toInd, figure);
        updateKingIndex(storage, figure, toInd);
    }

    private void updateKingIndex(ChessPositionsStorage storage, Figure figure, int index) {
        if (figure instanceof King) {
            if (figure.getPosition() == Position.WHITE) {
                storage.setWhiteKingIndex(index);
            } else {
                storage.setBlackKingIndex(index);
            }
        }
    }
}
