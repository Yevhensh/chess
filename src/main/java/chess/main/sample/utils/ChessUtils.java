package chess.main.sample.utils;

import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Position;
import java.util.Map;

public class ChessUtils {
  public static boolean isEmpty(Map<Integer, Figure> positions, int index) {
    return positions.get(index) == null;
  }

  public static boolean isAlly(Map<Integer, Figure> positions, int index, Position side) {
    Figure figure = positions.get(index);
    return figure != null && figure.getPosition() == side;
  }

  public static boolean isOpposite(Map<Integer, Figure> positions, int index, Position side) {
    Figure figure = positions.get(index);
    return figure != null && figure.getPosition() != side;
  }

  public static int getRow(int index) {
    return index / 8;
  }

  public static int getCol(int index) {
    return index % 8;
  }

  public static boolean isValid(int row, int col) {
    return row >= 0 && row < 8 && col >= 0 && col < 8;
  }

  public static boolean isValid(int index) {
    return index >= 0 && index < 64;
  }

  public static int getIndex(int row, int col) {
    return row * 8 + col;
  }
}
