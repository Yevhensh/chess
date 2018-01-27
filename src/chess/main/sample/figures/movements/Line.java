package chess.main.sample.figures.movements;


import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Movement;
import chess.main.sample.manage.DeckManager;

import java.util.ArrayList;
import java.util.List;

public class Line extends Movement {

    @Override
    public List<Integer> determineAvailableMovements(int deckCell, Figure figure) {
        List<Integer> allExistingMoves = new ArrayList<>();
        allExistingMoves.addAll(getHorizontalMoves(deckCell, figure));
        allExistingMoves.addAll(getVerticalMoves(deckCell, figure));
        return allExistingMoves;
    }

    private List<Integer> getVerticalMoves(int deckCell, Figure figure) {
        DeckManager deckManager = DeckManager.getInstance();
        List<Integer> list = new ArrayList<>();
        int upperIterator = deckCell + 8;
        while (upperIterator < 64) {
            if (!deckManager.isEmptyDeckCell(upperIterator)) {
                if (deckManager.isOppositeFigureOnDeckCell(upperIterator, figure.getPosition())) {
                    list.add(upperIterator);
                }
                break;
            }
            list.add(upperIterator);
            upperIterator += 8;
        }
        int downIterator = deckCell - 8;
        while (downIterator >= 0) {
            if (!deckManager.isEmptyDeckCell(downIterator)) {
                if (deckManager.isOppositeFigureOnDeckCell(downIterator, figure.getPosition())) {
                    list.add(downIterator);
                }
                break;
            }
            list.add(downIterator);
            downIterator -= 8;
        }
        return list;
    }

    private List<Integer> getHorizontalMoves(int deckCell, Figure figure) {
        DeckManager deckManager = DeckManager.getInstance();
        List<Integer> list = new ArrayList<>();
        int rightIterator = deckCell + 1;
        while (rightIterator % 8 != 0 && rightIterator < 64) {
            if (!deckManager.isEmptyDeckCell(rightIterator)) {
                if (deckManager.isOppositeFigureOnDeckCell(rightIterator, figure.getPosition())) {
                    list.add(rightIterator);
                }
                break;
            }
            list.add(rightIterator++);
        }
        int leftIterator = deckCell - 1;
        while (leftIterator % 8 != 0 && leftIterator >= 0) {
            if (!deckManager.isEmptyDeckCell(leftIterator)) {
                if (deckManager.isOppositeFigureOnDeckCell(leftIterator, figure.getPosition())) {
                    list.add(leftIterator);
                }
                break;
            }
            list.add(leftIterator--);
        }
        return list;
    }
}
