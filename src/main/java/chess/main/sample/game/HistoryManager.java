package chess.main.sample.game;

import java.util.ArrayList;
import java.util.List;

public class HistoryManager {
    private final List<Move> moves = new ArrayList<>();
    private int currentIndex = -1;

    public void addMove(Move move) {
        // Clear redo history
        if (currentIndex < moves.size() - 1) {
            moves.subList(currentIndex + 1, moves.size()).clear();
        }
        moves.add(move);
        currentIndex++;
    }

    public Move undo() {
        if (canUndo()) {
            return moves.get(currentIndex--);
        }
        return null;
    }

    public Move redo() {
        if (canRedo()) {
            return moves.get(++currentIndex);
        }
        return null;
    }

    public boolean canUndo() {
        return currentIndex >= 0;
    }

    public boolean canRedo() {
        return currentIndex < moves.size() - 1;
    }
}
