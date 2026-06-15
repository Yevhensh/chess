package chess.main.sample.figures.instances;

import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Position;
import chess.main.sample.figures.movements.KingMove;

import java.util.List;

public class King extends Figure {

    public King(Position position) {
        super(position);
    }

    @Override
    public String getFileNaming() {
        return getPosition() == Position.BLACK ? "king" : "king-white";
    }

    @Override
    public List<Integer> getAllAvailableMovements(int deckCell) {
        return getAllAvailableMovements(chess.main.sample.storage.ChessPositionsStorage.getGlobalStorage().getPositionsContainer(), deckCell);
    }

    @Override
    public List<Integer> getAllAvailableMovements(java.util.Map<Integer, Figure> positions, int deckCell) {
        KingMove movement = new KingMove();
        return movement.determineAvailableMovements(positions, deckCell, this);
    }

    @Override
    public String toString() {
        return "King";
    }
}
