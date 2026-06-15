package chess.main.sample.figures.movements;

import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Movement;
import chess.main.sample.manage.DeckManager;
import chess.main.sample.utils.ChessUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PawnMove extends Movement {
    @Override
    public List<Integer> determineAvailableMovements(Map<Integer, Figure> positions, int deckCell, Figure figure) {
        List<Integer> availableMovesList = new ArrayList<>();
        int row = ChessUtils.getRow(deckCell);
        int col = ChessUtils.getCol(deckCell);

        int forwardDir = figure.isWhite() ? -1 : 1;

        int nextRow = row + forwardDir;
        if (ChessUtils.isValid(nextRow, col)) {
            int upMove = ChessUtils.getIndex(nextRow, col);
            if (DeckManager.getInstance().isEmptyDeckCell(positions, upMove)) {
                availableMovesList.add(upMove);

                if (isOnStartRow(row, figure)) {
                    int upUpRow = row + 2 * forwardDir;
                    int upUpMove = ChessUtils.getIndex(upUpRow, col);
                    if (DeckManager.getInstance().isEmptyDeckCell(positions, upUpMove)) {
                        availableMovesList.add(upUpMove);
                    }
                }
            }
        }

        availableMovesList.addAll(
                IntStream.of(col - 1, col + 1)
                        .filter(nextCol -> ChessUtils.isValid(row + forwardDir, nextCol))
                        .mapToObj(nextCol -> ChessUtils.getIndex(row + forwardDir, nextCol))
                        .filter(captureIndex -> DeckManager.getInstance()
                                .isOppositeFigureOnDeckCell(positions, captureIndex, figure.getPosition()))
                        .collect(Collectors.toList())
        );

        return availableMovesList;
    }

    private boolean isOnStartRow(int row, Figure figure) {
        return figure.isWhite() ? row == 6 : row == 1;
    }
}
