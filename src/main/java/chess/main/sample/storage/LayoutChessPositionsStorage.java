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

    private static Map<Integer, Integer> layoutPositionsContainer = new HashMap<>();

    public Map<Integer, Integer> getLayoutPositionsContainer() {
        return layoutPositionsContainer;
    }

    public void gameStartPositionsRemind(Map<Integer, Figure> positionsContainer) {
        int j = 1;
        for (int i = 1; i <= 16; i++) {
            layoutPositionsContainer.put(i - 1, j);
            j += 2;
        }
        for (int i = 16; i < 48; i++) {
            layoutPositionsContainer.put(i, j);
            j++;
        }
        for (int i = 48; i < 64; i++) {
            layoutPositionsContainer.put(i, j);
            j += 2;
        }
    }
}
