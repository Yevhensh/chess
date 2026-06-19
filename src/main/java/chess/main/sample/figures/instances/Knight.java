package chess.main.sample.figures.instances;

import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Position;
import chess.main.sample.figures.movements.KnightMove;
import chess.main.sample.game.Move;
import java.util.List;
import java.util.Map;

public class Knight extends Figure {
  public Knight(Position position) {
    super(position);
  }

  @Override
  public String getFileNaming() {
    return getPosition() == Position.BLACK ? "knight" : "knight-white";
  }

  @Override
  public List<Integer> getAllAvailableMovements(
      Map<Integer, Figure> positions, int deckCell, Move lastMove) {
    KnightMove movement = new KnightMove();
    movement.setLastMove(lastMove);
    return movement.determineAvailableMovements(positions, deckCell, this);
  }

  @Override
  public String toString() {
    return "Knight";
  }
}
