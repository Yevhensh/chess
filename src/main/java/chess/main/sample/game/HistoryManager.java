package chess.main.sample.game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryManager {
  private record HistoryEntry(Move move, String snapshot, int halfMoveClock) {}

  private final List<HistoryEntry> history = new ArrayList<>();
  private int currentIndex = -1;

  public void addMove(Move move, String boardSnapshot, int halfMoveClock) {
    // Clear redo history
    if (currentIndex < history.size() - 1) {
      history.subList(currentIndex + 1, history.size()).clear();
    }
    history.add(new HistoryEntry(move, boardSnapshot, halfMoveClock));
    currentIndex++;
  }

  public Move undo() {
    if (canUndo()) {
      return history.get(currentIndex--).move();
    }
    return null;
  }

  public int getPreviousHalfMoveClock() {
    if (currentIndex >= 0) {
      return history.get(currentIndex).halfMoveClock();
    }
    return 0;
  }

  public Move redo() {
    if (canRedo()) {
      currentIndex++;
      return history.get(currentIndex).move();
    }
    return null;
  }

  public boolean canUndo() {
    return currentIndex >= 0;
  }

  public boolean canRedo() {
    return currentIndex < history.size() - 1;
  }

  public Move getLastMove() {
    if (currentIndex >= 0) {
      return history.get(currentIndex).move();
    }
    return null;
  }

  public List<Move> getMoves() {
    return history.subList(0, currentIndex + 1).stream()
        .map(HistoryEntry::move)
        .collect(Collectors.toList());
  }

  public int getRepetitionCount(String currentSnapshot) {
    int count = 1;
    for (int i = 0; i <= currentIndex; i++) {
      if (history.get(i).snapshot().equals(currentSnapshot)) {
        count++;
      }
    }
    return count;
  }
}
