package chess.main.sample.storage;


import chess.main.sample.figures.Figure;

import java.util.HashMap;
import java.util.Map;

public class LayoutChessPositionsStorage {

    private static LayoutChessPositionsStorage instance;

    public static LayoutChessPositionsStorage getInstance() {
        if (instance == null) {
            instance = new LayoutChessPositionsStorage();
        }
        return instance;
    }

    public void gameStartPositionsRemind(Map<Integer, Figure> positionsContainer) {
    }
}
