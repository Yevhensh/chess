package chess.main.sample.figures.movements;


import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Movement;
import chess.main.sample.manage.DeckManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PawnMove extends Movement {
    @Override
    public List<Integer> determineAvailableMovements(int deckCell, Figure figure) {
        List<Integer> availableMovesList = new ArrayList<>();
        int upMove;
        int upUpMove;
        if (figure.isWhite()) {
            upMove = deckCell - 8;
            upUpMove = deckCell - 16;
        } else {
            upMove = deckCell + 8;
            upUpMove = deckCell + 16;
        }
        availableMovesList.addAll(getFrontMoves(deckCell, upMove, upUpMove, figure));
        Integer[] sideMoves = new Integer[2];
        if (figure.isWhite()) {
            sideMoves[0] = deckCell + 7;
            sideMoves[1] = deckCell + 9;
        } else {
            sideMoves[0] = deckCell - 7;
            sideMoves[1] = deckCell - 9;
        }
        availableMovesList.addAll(Arrays.asList(sideMoves));
        return availableMovesList;
    }

    public static List<Integer> getAvailableSideMoves(int deckCell, int[] sideMoves, Figure figure) {
        DeckManager deckManager = DeckManager.getInstance();
        List<Integer> sideAvailableMoves = new ArrayList<>();
        if ((deckCell % 8 != 0)) {
            if (deckManager.isOppositeFigureOnDeckCell(sideMoves[1], figure.getPosition())) {
                sideAvailableMoves.add(sideMoves[1]);
            }
        } else if ((deckCell + 1) % 8 != 0) {
            if (deckManager.isOppositeFigureOnDeckCell(sideMoves[0], figure.getPosition())) {
                sideAvailableMoves.add(sideMoves[0]);
            }
        } else {
            if (deckManager.isOppositeFigureOnDeckCell(sideMoves[1], figure.getPosition())) {
                sideAvailableMoves.add(sideMoves[1]);
            }
            if (deckManager.isOppositeFigureOnDeckCell(sideMoves[0], figure.getPosition())) {
                sideAvailableMoves.add(sideMoves[0]);
            }
        }
        return sideAvailableMoves;
    }


    private List<Integer> getFrontMoves(int deckCell, int upMove, int upUpMove, Figure figure) {
        List<Integer> frontAvailableMoves = new ArrayList<>();
        DeckManager deckManager = DeckManager.getInstance();
        if (deckManager.isEmptyDeckCell(upMove)) {
            frontAvailableMoves.add(upMove);
            if ((whitePawnOnStart(deckCell, figure) || blackPawnOnStart(deckCell, figure))
                    && deckManager.isEmptyDeckCell(upUpMove)) {
                frontAvailableMoves.add(upUpMove);
            }
        }
        return frontAvailableMoves;
    }

    private boolean whitePawnOnStart(int deckCell, Figure figure) {
        return (deckCell < 56 && deckCell > 48 && figure.isWhite());
    }

    private boolean blackPawnOnStart(int deckCell, Figure figure) {
        return (deckCell < 16 && deckCell > 7 && figure.isBlack());
    }
}
