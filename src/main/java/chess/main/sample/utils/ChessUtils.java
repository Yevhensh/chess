package chess.main.sample.utils;

public class ChessUtils {
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
