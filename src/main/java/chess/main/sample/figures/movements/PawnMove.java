package chess.main.sample.figures.movements;

import chess.main.sample.constants.SceneConstants;
import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Movement;
import chess.main.sample.manage.DeckManager;
import chess.main.sample.utils.ChessUtils;

import java.util.ArrayList;
import java.util.List;

public class PawnMove extends Movement {
    @Override
    public List<Integer> determineAvailableMovements(int deckCell, Figure figure) {
        List<Integer> availableMovesList = new ArrayList<>();
        int row = ChessUtils.getRow(deckCell);
        int col = ChessUtils.getCol(deckCell);

        int forwardDir = figure.isWhite() ? -1 : 1;

        // Front moves
        int nextRow = row + forwardDir;
        if (ChessUtils.isValid(nextRow, col)) {
            int upMove = ChessUtils.getIndex(nextRow, col);
            if (DeckManager.getInstance().isEmptyDeckCell(upMove)) {
                availableMovesList.add(upMove);

                // Double move from start
                if (isOnStartRow(row, figure)) {
                    int upUpRow = row + 2 * forwardDir;
                    int upUpMove = ChessUtils.getIndex(upUpRow, col);
                    if (DeckManager.getInstance().isEmptyDeckCell(upUpMove)) {
                        availableMovesList.add(upUpMove);
                    }
                }
            }
        }

        // Capture moves
        int[] captureCols = {col - 1, col + 1};
        for (int nextCol : captureCols) {
            if (nextCol >= 0 && nextCol < 8) {
                int captureRow = row + forwardDir;
                if (captureRow >= 0 && captureRow < 8) {
                    int captureIndex = ChessUtils.getIndex(captureRow, nextCol);
                    if (DeckManager.getInstance().isOppositeFigureOnDeckCell(captureIndex, figure.getPosition())) {
                        availableMovesList.add(captureIndex);
                    }
                }
            }
        }

        return availableMovesList;
    }

    private boolean isOnStartRow(int row, Figure figure) {
        if (figure.isWhite()) {
            return row == SceneConstants.WHITE_PAWN_START_ROW;
        } else {
            return row == SceneConstants.BLACK_PAWN_START_ROW;
        }
    }
}
