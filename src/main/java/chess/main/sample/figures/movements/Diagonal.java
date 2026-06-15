package chess.main.sample.figures.movements;

import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Movement;
import chess.main.sample.manage.DeckManager;
import chess.main.sample.utils.ChessUtils;

import java.util.ArrayList;
import java.util.List;

public class Diagonal extends Movement {

    @Override
    public List<Integer> determineAvailableMovements(int deckCell, Figure figure) {
        List<Integer> allAvailableMoves = new ArrayList<>();
        DeckManager deckManager = DeckManager.getInstance();
        int row = ChessUtils.getRow(deckCell);
        int col = ChessUtils.getCol(deckCell);

        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        for (int[] dir : directions) {
            int nextRow = row + dir[0];
            int nextCol = col + dir[1];
            while (ChessUtils.isValid(nextRow, nextCol)) {
                int nextIndex = ChessUtils.getIndex(nextRow, nextCol);
                if (!deckManager.isEmptyDeckCell(nextIndex)) {
                    if (deckManager.isOppositeFigureOnDeckCell(nextIndex, figure.getPosition())) {
                        allAvailableMoves.add(nextIndex);
                    }
                    break;
                }
                allAvailableMoves.add(nextIndex);
                nextRow += dir[0];
                nextCol += dir[1];
            }
        }
        return allAvailableMoves;
    }
}
