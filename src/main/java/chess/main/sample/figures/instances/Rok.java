package chess.main.sample.figures.instances;

import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Position;
import chess.main.sample.figures.movements.Line;
import chess.main.sample.game.Move;
import java.util.List;
import java.util.Map;

public class Rok extends Figure {
  public Rok(Position position) {
    super(position);
  }

  @Override
  public String getFileNaming() {
    return getPosition() == Position.BLACK ? "rok" : "rok-white";
  }

  @Override
  public List<Integer> getAllAvailableMovements(
      Map<Integer, Figure> positions, int deckCell, Move lastMove) {
    Line movement = new Line();
    movement.setLastMove(lastMove);
    return movement.determineAvailableMovements(positions, deckCell, this);
  }

  @Override
  public String toString() {
    return "Rok";
  }
}
