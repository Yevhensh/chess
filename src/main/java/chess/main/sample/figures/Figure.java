package chess.main.sample.figures;

import chess.main.sample.game.Move;
import java.util.List;
import java.util.Map;

public abstract class Figure {

  private Position position;

  public Figure() {}

  public Figure(Position position) {
    this.position = position;
  }

  public String getFilenamePath() {
    return String.format("pictures/%s.png", getFileNaming());
  }

  public abstract String getFileNaming();

  public List<Integer> getAllAvailableMovements(Map<Integer, Figure> positions, int deckCell) {
    return getAllAvailableMovements(positions, deckCell, null);
  }

  public abstract List<Integer> getAllAvailableMovements(
      Map<Integer, Figure> positions, int deckCell, Move lastMove);

  public boolean isWhite() {
    return this.position == Position.WHITE;
  }

  public Position getPosition() {
    return position;
  }
}
