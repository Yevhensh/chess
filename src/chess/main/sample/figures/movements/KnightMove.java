package chess.main.sample.figures.movements;


import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Movement;
import chess.main.sample.manage.DeckManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KnightMove extends Movement {

    @Override
    public List<Integer> determineAvailableMovements(int deckCell, Figure figure) {
        DeckManager deckManager = DeckManager.getInstance();
        Map<String, Integer> availableMovesMap = new HashMap<>();
        String topLeftUpMove = "TopLeftUpMove",
                topLeftDownMove = "TopLeftDownMove",
                topRightUpMove = "TopRightUpMove",
                topRightDownMove = "TopRightDownMove";
        int topLeftUpMoveVal = deckCell - 17,
                topLeftDownMoveVal = deckCell - 10,
                topRightUpMoveVal = deckCell - 15,
                topRightDownMoveVal = deckCell - 6;
        availableMovesMap.put(topLeftUpMove, topLeftUpMoveVal);
        availableMovesMap.put(topLeftDownMove, topLeftDownMoveVal);
        availableMovesMap.put(topRightUpMove, topRightUpMoveVal);
        availableMovesMap.put(topRightDownMove, topRightDownMoveVal);

        String bottomLeftDownMove = "BottomLeftDownMove",
                bottomLeftUpMove = "BottomLeftUpMove",
                bottomRightDownMove = "BottomRightDownMove",
                bottomRightUpMove = "BottomRightUpMove";
        int bottomLeftDownMoveVal = deckCell + 17,
                bottomLeftUpMoveVal = deckCell + 10,
                bottomRightDownMoveVal = deckCell + 15,
                bottomRightUpMoveVal = deckCell + 6;
        availableMovesMap.put(bottomLeftDownMove, bottomLeftDownMoveVal);
        availableMovesMap.put(bottomLeftUpMove, bottomLeftUpMoveVal);
        availableMovesMap.put(bottomRightDownMove, bottomRightDownMoveVal);
        availableMovesMap.put(bottomRightUpMove, bottomRightUpMoveVal);

        if ((deckCell % 8 == 0) || deckCell < 16 ||
                deckManager.isAllyFigureOnDeckCell(topLeftUpMoveVal, figure.getPosition())) {
            availableMovesMap.remove(topLeftUpMove);
        }
        if ((deckCell % 8 == 0) || deckCell < 8 ||
                deckManager.isAllyFigureOnDeckCell(topLeftDownMoveVal, figure.getPosition())) {
            availableMovesMap.remove(topLeftDownMove);
        }
        if ((deckCell % 7 == 0) || deckCell < 16 ||
                deckManager.isAllyFigureOnDeckCell(topRightUpMoveVal, figure.getPosition())) {
            availableMovesMap.remove(topRightUpMove);
        }
        if ((deckCell % 7 == 0) || deckCell < 8 ||
                deckManager.isAllyFigureOnDeckCell(topRightDownMoveVal, figure.getPosition())) {
            availableMovesMap.remove(topRightDownMove);
        }
        if ((deckCell % 8 == 0) || deckCell >= 48 ||
                deckManager.isAllyFigureOnDeckCell(bottomLeftDownMoveVal, figure.getPosition())) {
            availableMovesMap.remove(bottomLeftDownMove);
        }
        if ((deckCell % 8 == 0) || deckCell >= 56 ||
                deckManager.isAllyFigureOnDeckCell(bottomLeftUpMoveVal, figure.getPosition())) {
            availableMovesMap.remove(bottomLeftUpMove);
        }
        if ((deckCell % 7 == 0) || deckCell >= 48 ||
                deckManager.isAllyFigureOnDeckCell(bottomRightDownMoveVal, figure.getPosition())) {
            availableMovesMap.remove(bottomRightDownMove);
        }
        if ((deckCell % 7 == 0) || deckCell >= 56 ||
                deckManager.isAllyFigureOnDeckCell(bottomRightUpMoveVal, figure.getPosition())) {
            availableMovesMap.remove(bottomRightUpMove);
        }
        return new ArrayList<>(availableMovesMap.values());
    }
}
