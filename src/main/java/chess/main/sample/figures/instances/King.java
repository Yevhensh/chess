package chess.main.sample.figures.instances;

import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Position;
import chess.main.sample.figures.movements.KingMove;
import chess.main.sample.game.Move;
import java.util.List;
import java.util.Map;

public class King extends Figure {
  public King(Position position) {
    super(position);
  }

  @Override
  public String getFileNaming() {
    return getPosition() == Position.BLACK ? "king" : "king-white";
  }

  @Override
  public List<Integer> getAllAvailableMovements(
      Map<Integer, Figure> positions, int deckCell, Move lastMove) {
    KingMove movement = new KingMove();
    movement.setLastMove(lastMove);
    return movement.determineAvailableMovements(positions, deckCell, this);
  }

  @Override
  public String toString() {
    return "King";
  }
}
