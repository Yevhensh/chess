package chess.main.sample.figures.instances;


import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Position;
import chess.main.sample.figures.movements.Line;

import java.util.List;

public class Rok extends Figure {
    public Rok(Position position) {
        super(position);
    }

    @Override
    public String getFileNaming() {
        return getPosition() == Position.BLACK ? "rok" : "rok-white";
    }

    @Override
    public List<Integer> getAllAvailableMovements(int deckCell) {
        Line movement = new Line();
        return movement.determineAvailableMovements(deckCell, this);
    }

    @Override
    public String toString() {
        return "Rok";
    }
}
