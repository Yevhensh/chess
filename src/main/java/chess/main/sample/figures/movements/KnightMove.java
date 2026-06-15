package chess.main.sample.figures.movements;


import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Movement;
import chess.main.sample.manage.DeckManager;
import chess.main.sample.utils.ChessUtils;

import java.util.ArrayList;
import java.util.List;

public class KnightMove extends Movement {

    @Override
    public List<Integer> determineAvailableMovements(java.util.Map<Integer, Figure> positions, int deckCell, Figure figure) {
        List<Integer> availableMoves = new ArrayList<>();
        DeckManager deckManager = DeckManager.getInstance();
        int row = ChessUtils.getRow(deckCell);
        int col = ChessUtils.getCol(deckCell);

        int[][] moves = {
            {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
            {1, -2}, {1, 2}, {2, -1}, {2, 1}
        };

        for (int[] move : moves) {
            int nextRow = row + move[0];
            int nextCol = col + move[1];
            if (ChessUtils.isValid(nextRow, nextCol)) {
                int nextIndex = ChessUtils.getIndex(nextRow, nextCol);
                if (deckManager.isEmptyDeckCell(positions, nextIndex) || deckManager.isOppositeFigureOnDeckCell(positions, nextIndex, figure.getPosition())) {
                    availableMoves.add(nextIndex);
                }
            }
        }

        return availableMoves;
    }
}
