package chess.main.sample.figures.instances;

import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Position;
import chess.main.sample.figures.movements.Diagonal;
import chess.main.sample.game.Move;
import java.util.List;
import java.util.Map;

public class Bishop extends Figure {
    public Bishop(Position position) {
        super(position);
    }

    @Override
    public String getFileNaming() {
        return getPosition() == Position.BLACK ? "bishop" : "bishop-white";
    }

    @Override
    public List<Integer> getAllAvailableMovements(Map<Integer, Figure> positions, int deckCell, Move lastMove) {
        Diagonal movement = new Diagonal();
        movement.setLastMove(lastMove);
        return movement.determineAvailableMovements(positions, deckCell, this);
    }

    @Override
    public String toString() {
        return "Bishop";
    }
}
