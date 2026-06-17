package chess.main.sample.figures.instances;

import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Position;
import chess.main.sample.figures.movements.Diagonal;
import chess.main.sample.figures.movements.Line;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Queen extends Figure {
    public Queen(Position position) {
        super(position);
    }

    @Override
    public String getFileNaming() {
        return getPosition() == Position.BLACK ? "queen" : "queen-white";
    }

    @Override
    public List<Integer> getAllAvailableMovements(Map<Integer, Figure> positions, int deckCell) {
        List<Integer> availableMovements = new ArrayList<>();
        Diagonal diagonalMove = new Diagonal();
        Line lineMove = new Line();
        availableMovements.addAll(diagonalMove.determineAvailableMovements(positions, deckCell, this));
        availableMovements.addAll(lineMove.determineAvailableMovements(positions, deckCell, this));
        return availableMovements;
    }

    @Override
    public String toString() {
        return "Queen";
    }
}
