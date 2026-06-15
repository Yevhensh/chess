package chess.main.sample.figures.instances;


import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Position;
import chess.main.sample.figures.movements.PawnMove;

import java.util.List;

public class Pawn extends Figure {
    public Pawn(Position position) {
        super(position);
    }

    @Override
    public String getFileNaming() {
        return getPosition() == Position.BLACK ? "pawn" : "pawn-white";
    }

    @Override
    public List<Integer> getAllAvailableMovements(int deckCell) {
        return getAllAvailableMovements(chess.main.sample.storage.ChessPositionsStorage.getGlobalStorage().getPositionsContainer(), deckCell);
    }

    @Override
    public List<Integer> getAllAvailableMovements(java.util.Map<Integer, Figure> positions, int deckCell) {
        PawnMove movement = new PawnMove();
        return movement.determineAvailableMovements(positions, deckCell, this);
    }

    @Override
    public String toString() {
        return "Pawn";
    }
}
