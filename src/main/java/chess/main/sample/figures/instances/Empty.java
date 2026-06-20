package chess.main.sample.figures.instances;

import chess.main.sample.figures.Figure;
import chess.main.sample.game.Move;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
  public List<Integer> getAllAvailableMovements(
      Map<Integer, Figure> positions, int deckCell, Move lastMove) {
    return Collections.emptyList();
  }
}
