package chess.main.sample.figures.movements;


import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Movement;
import chess.main.sample.manage.DeckManager;
import chess.main.sample.utils.ChessUtils;

import java.util.ArrayList;
import java.util.List;

public class Line extends Movement {

    @Override
    public List<Integer> determineAvailableMovements(java.util.Map<Integer, Figure> positions, int deckCell, Figure figure) {
        List<Integer> allExistingMoves = new ArrayList<>();
        allExistingMoves.addAll(getHorizontalMoves(positions, deckCell, figure));
        allExistingMoves.addAll(getVerticalMoves(positions, deckCell, figure));
        return allExistingMoves;
    }

    private List<Integer> getVerticalMoves(java.util.Map<Integer, Figure> positions, int deckCell, Figure figure) {
        DeckManager deckManager = DeckManager.getInstance();
        List<Integer> list = new ArrayList<>();
        int row = ChessUtils.getRow(deckCell);
        int col = ChessUtils.getCol(deckCell);

        // Down (towards higher row indices)
        for (int r = row + 1; r < 8; r++) {
            int nextIndex = ChessUtils.getIndex(r, col);
            if (!deckManager.isEmptyDeckCell(positions, nextIndex)) {
                if (deckManager.isOppositeFigureOnDeckCell(positions, nextIndex, figure.getPosition())) {
                    list.add(nextIndex);
                }
                break;
            }
            list.add(nextIndex);
        }

        // Up (towards lower row indices)
        for (int r = row - 1; r >= 0; r--) {
            int nextIndex = ChessUtils.getIndex(r, col);
            if (!deckManager.isEmptyDeckCell(positions, nextIndex)) {
                if (deckManager.isOppositeFigureOnDeckCell(positions, nextIndex, figure.getPosition())) {
                    list.add(nextIndex);
                }
                break;
            }
            list.add(nextIndex);
        }
        return list;
    }

    private List<Integer> getHorizontalMoves(java.util.Map<Integer, Figure> positions, int deckCell, Figure figure) {
        DeckManager deckManager = DeckManager.getInstance();
        List<Integer> list = new ArrayList<>();
        int row = ChessUtils.getRow(deckCell);
        int col = ChessUtils.getCol(deckCell);

        // Right
        for (int c = col + 1; c < 8; c++) {
            int nextIndex = ChessUtils.getIndex(row, c);
            if (!deckManager.isEmptyDeckCell(positions, nextIndex)) {
                if (deckManager.isOppositeFigureOnDeckCell(positions, nextIndex, figure.getPosition())) {
                    list.add(nextIndex);
                }
                break;
            }
            list.add(nextIndex);
        }

        // Left
        for (int c = col - 1; c >= 0; c--) {
            int nextIndex = ChessUtils.getIndex(row, c);
            if (!deckManager.isEmptyDeckCell(positions, nextIndex)) {
                if (deckManager.isOppositeFigureOnDeckCell(positions, nextIndex, figure.getPosition())) {
                    list.add(nextIndex);
                }
                break;
            }
            list.add(nextIndex);
        }
        return list;
    }
}
