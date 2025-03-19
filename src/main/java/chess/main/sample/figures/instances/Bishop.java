package chess.main.sample.figures.instances;


import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Position;
import chess.main.sample.figures.movements.Diagonal;

import java.util.List;

public class Bishop extends Figure {
    public Bishop(Position position) {
        super(position);
    }

    @Override
    public String getFileNaming() {
        return getPosition() == Position.BLACK ? "bishop" : "bishop-white";
    }

    @Override
    public List<Integer> getAllAvailableMovements(int deckCell) {
        Diagonal movement = new Diagonal();
        return movement.determineAvailableMovements(deckCell, this);
    }

    @Override
    public String toString() {
        return "Bishop";
    }
}
