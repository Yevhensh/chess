package chess.main.sample.figures.instances;


import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Position;
import chess.main.sample.figures.movements.KnightMove;

import java.util.List;

public class Knight extends Figure {
    public Knight(Position position) {
        super(position);
    }

    @Override
    public String getFileNaming() {
        return getPosition() == Position.BLACK ? "knight" : "knight-white";
    }

    @Override
    public List<Integer> getAllAvailableMovements(int deckCell) {
        KnightMove movement = new KnightMove();
        return movement.determineAvailableMovements(deckCell, this);
    }

    @Override
    public String toString() {
        return "Knight";
    }
}
