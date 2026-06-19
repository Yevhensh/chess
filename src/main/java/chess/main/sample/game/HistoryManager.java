package chess.main.sample.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryManager {
    private final List<Move> moves = new ArrayList<>();
    private final List<String> boardSnapshots = new ArrayList<>();
    private final List<Integer> halfMoveClocks = new ArrayList<>();
    private int currentIndex = -1;

    public void addMove(Move move, String boardSnapshot, int halfMoveClock) {
        // Clear redo history
        if (currentIndex < moves.size() - 1) {
            moves.subList(currentIndex + 1, moves.size()).clear();
            boardSnapshots.subList(currentIndex + 1, boardSnapshots.size()).clear();
            halfMoveClocks.subList(currentIndex + 1, halfMoveClocks.size()).clear();
        }
        moves.add(move);
        boardSnapshots.add(boardSnapshot);
        halfMoveClocks.add(halfMoveClock);
        currentIndex++;
    }

    public Move undo() {
        if (canUndo()) {
            return moves.get(currentIndex--);
        }
        return null;
    }

    public int getPreviousHalfMoveClock() {
        if (currentIndex >= 0) {
            return halfMoveClocks.get(currentIndex);
        }
        return 0;
    }

    public Move redo() {
        if (canRedo()) {
            currentIndex++;
            return moves.get(currentIndex);
        }
        return null;
    }

    public boolean canUndo() {
        return currentIndex >= 0;
    }

    public boolean canRedo() {
        return currentIndex < moves.size() - 1;
    }

    public Move getLastMove() {
        if (currentIndex >= 0) {
            return moves.get(currentIndex);
        }
        return null;
    }

    public List<Move> getMoves() {
        return moves.subList(0, currentIndex + 1);
    }

    public int getRepetitionCount(String currentSnapshot) {
        int count = 1;
        for (int i = 0; i <= currentIndex; i++) {
            if (boardSnapshots.get(i).equals(currentSnapshot)) {
                count++;
            }
        }
        return count;
    }
}
