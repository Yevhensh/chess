package chess.main.sample.game;

import chess.main.sample.figures.Position;

public class TurnSwitcher {
    private static Position position = Position.WHITE;

    public static void switchPosition() {
        if (position == Position.WHITE) {
            position = Position.BLACK;
        } else {
            position = Position.WHITE;
        }
    }

    public static Position getPosition() {
        return position;
    }
}
