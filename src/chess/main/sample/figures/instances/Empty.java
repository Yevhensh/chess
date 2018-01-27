package chess.main.sample.figures.instances;

import chess.main.sample.figures.Figure;

import java.util.List;

public class Empty extends Figure {
    @Override
    public String toString() {
        return "Empty";
    }

    @Override
    public String getFilenamePath() {
        return "empty";
    }

    @Override
    public String getFileNaming() {
        return "empty";
    }

    @Override
    public List<Integer> getAllAvailableMovements(int deckCell) {
        return null;
    }
}
