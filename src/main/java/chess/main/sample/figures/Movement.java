package chess.main.sample.figures;

import java.util.List;
import java.util.Map;

public abstract class Movement {
    public abstract List<Integer> determineAvailableMovements(Map<Integer, Figure> positions, int deckCell, Figure figure);
}
