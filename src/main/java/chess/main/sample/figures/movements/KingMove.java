package chess.main.sample.figures.movements;

import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Movement;
import chess.main.sample.manage.DeckManager;
import chess.main.sample.utils.ChessUtils;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KingMove extends Movement {
    @Override
    public List<Integer> determineAvailableMovements(java.util.Map<Integer, Figure> positions, int deckCell, Figure figure) {
        List<Integer> allMoves = getBasicMoves(positions, deckCell, figure);
        return filterAllMovesWithCheckPossible(positions, allMoves, figure);
    }

    public List<Integer> getBasicMoves(java.util.Map<Integer, Figure> positions, int deckCell, Figure figure) {
        List<Integer> allMoves = new ArrayList<>();
        DeckManager deckManager = DeckManager.getInstance();
        int row = ChessUtils.getRow(deckCell);
        int col = ChessUtils.getCol(deckCell);

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                int nextRow = row + dr;
                int nextCol = col + dc;
                if (ChessUtils.isValid(nextRow, nextCol)) {
                    int nextIndex = ChessUtils.getIndex(nextRow, nextCol);
                    if (!deckManager.isAllyFigureOnDeckCell(positions, nextIndex, figure.getPosition())) {
                        allMoves.add(nextIndex);
                    }
                }
            }
        }
        return allMoves;
    }

    private List<Integer> filterAllMovesWithCheckPossible(java.util.Map<Integer, Figure> positions, List<Integer> allMoves, Figure figure) {
        DeckManager deckManager = DeckManager.getInstance();
        chess.main.sample.figures.Position oppositePosition =
            (figure.getPosition() == chess.main.sample.figures.Position.WHITE) ?
            chess.main.sample.figures.Position.BLACK : chess.main.sample.figures.Position.WHITE;

        List<Integer> oppositeSiteAttacks = deckManager.getAllOppositeSiteAttacks(positions, oppositePosition);

        return allMoves.stream()
                .filter(item -> !oppositeSiteAttacks.contains(item))
                .collect(Collectors.toList());
    }
}
