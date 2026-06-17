package chess.main.sample.figures;

import java.util.List;
import java.util.Map;

public abstract class Figure {

    private Position position;

    public Figure() {
    }

    public Figure(Position position) {
        this.position = position;
    }

    public String getFilenamePath() {
        return String.format("pictures/%s.png", getFileNaming());
    }

    public abstract String getFileNaming();

    public abstract List<Integer> getAllAvailableMovements(Map<Integer, Figure> positions, int deckCell);

    public boolean isWhite() {
        return this.position == Position.WHITE;
    }

    public boolean isBlack() {
        return this.position == Position.BLACK;
    }

    public Position getPosition() {
        return position;
    }
}
