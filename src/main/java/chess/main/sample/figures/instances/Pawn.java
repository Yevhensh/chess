package chess.main.sample.figures.instances;

import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Position;
import chess.main.sample.figures.movements.PawnMove;
import chess.main.sample.game.Move;
import java.util.List;
import java.util.Map;

public class Pawn extends Figure {
    public Pawn(Position position) {
        super(position);
    }

    @Override
    public String getFileNaming() {
        return getPosition() == Position.BLACK ? "pawn" : "pawn-white";
    }

    @Override
    public List<Integer> getAllAvailableMovements(Map<Integer, Figure> positions, int deckCell, Move lastMove) {
        PawnMove movement = new PawnMove();
        movement.setLastMove(lastMove);
        return movement.determineAvailableMovements(positions, deckCell, this);
    }

    @Override
    public String toString() {
        return "Pawn";
    }
}
