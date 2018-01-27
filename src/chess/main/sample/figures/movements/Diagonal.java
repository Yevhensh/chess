package chess.main.sample.figures.movements;

import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Movement;
import chess.main.sample.manage.DeckManager;

import java.util.ArrayList;
import java.util.List;

public class Diagonal extends Movement {

    @Override
    public List<Integer> determineAvailableMovements(int deckCell, Figure figure) {
        List<Integer> allAvailableMoves = new ArrayList<>();
        DeckManager deckManager = DeckManager.getInstance();
        int downRightMove = deckCell + 9;
        while (downRightMove < 64 && (downRightMove % 7 != 0)) {
            if (!deckManager.isEmptyDeckCell(downRightMove)) {
                if (deckManager.isOppositeFigureOnDeckCell(downRightMove, figure.getPosition())) {
                    allAvailableMoves.add(downRightMove);
                }
                break;
            }
            allAvailableMoves.add(downRightMove);
            downRightMove += 9;
        }
        int topRightMove = deckCell - 9;
        while (topRightMove >= 0 && (topRightMove % 7 != 0)) {
            if (!deckManager.isEmptyDeckCell(topRightMove)) {
                if (deckManager.isOppositeFigureOnDeckCell(topRightMove, figure.getPosition())) {
                    allAvailableMoves.add(topRightMove);
                }
                break;
            }
            allAvailableMoves.add(topRightMove);
            topRightMove -= 9;
        }
        int downLeftMove = deckCell + 7;
        while (downLeftMove < 64 && (downLeftMove % 8 != 0)) {
            if (!deckManager.isEmptyDeckCell(downLeftMove)) {
                if (deckManager.isOppositeFigureOnDeckCell(downLeftMove, figure.getPosition())) {
                    allAvailableMoves.add(downLeftMove);
                }
                break;
            }
            allAvailableMoves.add(downLeftMove);
            downLeftMove += 7;
        }
        int topLeftMove = deckCell - 7;
        while (topLeftMove >= 0 && (topLeftMove % 8) != 0) {
            if (!deckManager.isEmptyDeckCell(topLeftMove)) {
                if (deckManager.isOppositeFigureOnDeckCell(topLeftMove, figure.getPosition())) {
                    allAvailableMoves.add(topLeftMove);
                }
                break;
            }
            allAvailableMoves.add(topLeftMove);
            topLeftMove -= 7;
        }
        return allAvailableMoves;
    }
}
