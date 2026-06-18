package chess.main.sample.figures;

import chess.main.sample.game.Move;
import java.util.List;
import java.util.Map;

public abstract class Movement {
    protected Move lastMove;

    public void setLastMove(Move lastMove) {
        this.lastMove = lastMove;
    }

    public abstract List<Integer> determineAvailableMovements(Map<Integer, Figure> positions, int deckCell, Figure figure);
}
