package chess.main.sample.figures.movements;

import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Movement;
import chess.main.sample.utils.ChessUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Line extends Movement {

  @Override
  public List<Integer> determineAvailableMovements(
      Map<Integer, Figure> positions, int deckCell, Figure figure) {
    List<Integer> allExistingMoves = new ArrayList<>();
    allExistingMoves.addAll(getVerticalMoves(positions, deckCell, figure));
    allExistingMoves.addAll(getHorizontalMoves(positions, deckCell, figure));
    return allExistingMoves;
  }

  private List<Integer> getVerticalMoves(
      Map<Integer, Figure> positions, int deckCell, Figure figure) {
    int row = ChessUtils.getRow(deckCell);
    int col = ChessUtils.getCol(deckCell);

    List<Integer> moves = new ArrayList<>();
    moves.addAll(
        collectSlidingMoves(
            positions, figure, IntStream.range(row + 1, 8), r -> ChessUtils.getIndex(r, col)));
    moves.addAll(
        collectSlidingMoves(
            positions,
            figure,
            IntStream.iterate(row - 1, r -> r >= 0, r -> r - 1),
            r -> ChessUtils.getIndex(r, col)));
    return moves;
  }

  private List<Integer> getHorizontalMoves(
      Map<Integer, Figure> positions, int deckCell, Figure figure) {
    int row = ChessUtils.getRow(deckCell);
    int col = ChessUtils.getCol(deckCell);

    List<Integer> moves = new ArrayList<>();
    moves.addAll(
        collectSlidingMoves(
            positions, figure, IntStream.range(col + 1, 8), c -> ChessUtils.getIndex(row, c)));
    moves.addAll(
        collectSlidingMoves(
            positions,
            figure,
            IntStream.iterate(col - 1, c -> c >= 0, c -> c - 1),
            c -> ChessUtils.getIndex(row, c)));
    return moves;
  }

  private List<Integer> collectSlidingMoves(
      Map<Integer, Figure> positions,
      Figure figure,
      IntStream coordinates,
      IntUnaryOperator toDeckIndex) {
    List<Integer> indices =
        coordinates.mapToObj(toDeckIndex::applyAsInt).collect(Collectors.toList());

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
