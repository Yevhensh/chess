package chess.main.sample.figures.movements;

import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Movement;
import chess.main.sample.manage.DeckManager;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KingMove extends Movement {
    @Override
    public List<Integer> determineAvailableMovements(int deckCell, Figure figure) {
        List<Integer> allMoves = new ArrayList<>();
        DeckManager deckManager = DeckManager.getInstance();
        // upMove
        if (deckCell > 7) {
            int upMove = deckCell - 8;
            if (!deckManager.isEmptyDeckCell(upMove)) {
                if (deckManager.isOppositeFigureOnDeckCell(upMove, figure.getPosition())) {
                    allMoves.add(upMove);
                }
            } else {
                allMoves.add(upMove);
            }
        }
        // downMove
        if (deckCell < 56) {
            int downMove = deckCell + 8;
            if (!deckManager.isEmptyDeckCell(downMove)) {
                if (deckManager.isOppositeFigureOnDeckCell(downMove, figure.getPosition())) {
                    allMoves.add(downMove);
                }
            } else {
                allMoves.add(downMove);
            }
        }
        // leftMove
        if (deckCell % 8 != 0) {
            int leftMove = deckCell - 1;
            if (!deckManager.isEmptyDeckCell(leftMove)) {
                if (deckManager.isOppositeFigureOnDeckCell(leftMove, figure.getPosition())) {
                    allMoves.add(leftMove);
                }
            } else {
                allMoves.add(leftMove);
            }
        }
        // rightMove
        if ((deckCell + 1) % 8 != 0) {
            int rightMove = deckCell + 1;
            if (!deckManager.isEmptyDeckCell(rightMove)) {
                if (deckManager.isOppositeFigureOnDeckCell(rightMove, figure.getPosition())) {
                    allMoves.add(rightMove);
                }
            } else {
                allMoves.add(rightMove);
            }
        }
        return filterAllMovesWithCheckPossible(allMoves, figure);
    }

    private List<Integer> filterAllMovesWithCheckPossible(List<Integer> allMoves, Figure figure) {
        DeckManager deckManager = DeckManager.getInstance();
        List<Integer> oppositeSiteAvailableMovements = deckManager
                .getAllAvailableSiteMovements(figure.getPosition());
        return allMoves.stream()
                .filter(item -> !oppositeSiteAvailableMovements.contains(item))
                .collect(Collectors.toList());
    }
}
