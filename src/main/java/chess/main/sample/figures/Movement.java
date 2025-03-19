package chess.main.sample.figures;

import java.util.List;

public abstract class Movement {
    public abstract List<Integer> determineAvailableMovements(int deckCell, Figure figure);
}
