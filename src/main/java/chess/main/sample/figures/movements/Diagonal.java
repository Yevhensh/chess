package chess.main.sample.figures.movements;

import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Movement;
import chess.main.sample.utils.ChessUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Diagonal extends Movement {

  private static final int[][] DIRECTIONS = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

  @Override
  public List<Integer> determineAvailableMovements(
      Map<Integer, Figure> positions, int deckCell, Figure figure) {
    int row = ChessUtils.getRow(deckCell);
    int col = ChessUtils.getCol(deckCell);

    return Arrays.stream(DIRECTIONS)
        .flatMap(direction -> collectDiagonalMoves(positions, figure, row, col, direction).stream())
        .collect(Collectors.toList());
  }

  private List<Integer> collectDiagonalMoves(
      Map<Integer, Figure> positions, Figure figure, int row, int col, int[] direction) {
    List<Integer> indices =
        IntStream.iterate(1, step -> step + 1)
            .mapToObj(step -> new int[] {row + direction[0] * step, col + direction[1] * step})
            .takeWhile(coords -> ChessUtils.isValid(coords[0], coords[1]))
            .map(coords -> ChessUtils.getIndex(coords[0], coords[1]))
            .collect(Collectors.toList());

    long emptySquareCount =
        indices.stream().takeWhile(nextIndex -> ChessUtils.isEmpty(positions, nextIndex)).count();

    List<Integer> moves =
        indices.stream().limit(emptySquareCount).collect(Collectors.toCollection(ArrayList::new));

    if (emptySquareCount < indices.size()) {
      int captureIndex = indices.get((int) emptySquareCount);
      if (ChessUtils.isOpposite(positions, captureIndex, figure.getPosition())) {
        moves.add(captureIndex);
      }
    }

    return moves;
  }
}
