package chess.main.sample.game;

import static org.junit.jupiter.api.Assertions.*;

import chess.main.sample.figures.Position;
import chess.main.sample.figures.instances.Pawn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HistoryManagerTest {
  private HistoryManager historyManager;

  @BeforeEach
  public void setUp() {
    historyManager = new HistoryManager();
  }

  @Test
  public void testAddMoveAndGetLastMove() {
    Move move = new Move(48, 40, new Pawn(Position.WHITE), null, false);
    historyManager.addMove(move, "snapshot1", 0);

    assertEquals(move, historyManager.getLastMove());
    assertTrue(historyManager.canUndo());
    assertFalse(historyManager.canRedo());
  }

  @Test
  public void testUndoRedo() {
    Move move1 = new Move(48, 40, new Pawn(Position.WHITE), null, false);
    Move move2 = new Move(8, 16, new Pawn(Position.BLACK), null, false);

    historyManager.addMove(move1, "snapshot1", 0);
    historyManager.addMove(move2, "snapshot2", 0);

    assertEquals(move2, historyManager.undo());
    assertEquals(move1, historyManager.getLastMove());
    assertTrue(historyManager.canRedo());

    assertEquals(move2, historyManager.redo());
    assertEquals(move2, historyManager.getLastMove());
  }

  @Test
  public void testClearRedoHistory() {
    Move move1 = new Move(48, 40, new Pawn(Position.WHITE), null, false);
    Move move2 = new Move(8, 16, new Pawn(Position.BLACK), null, false);
    Move move3 = new Move(40, 32, new Pawn(Position.WHITE), null, false);

    historyManager.addMove(move1, "snapshot1", 0);
    historyManager.addMove(move2, "snapshot2", 0);

    historyManager.undo(); // back to after move1

    historyManager.addMove(move3, "snapshot3", 0); // should clear move2 from redo history

    assertFalse(historyManager.canRedo());
    assertEquals(move3, historyManager.getLastMove());

    historyManager.undo();
    assertEquals(move1, historyManager.getLastMove());
  }

  @Test
  public void testRepetitionCount() {
    String snapshot = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w";
    Move move1 = new Move(48, 40, new Pawn(Position.WHITE), null, false);
    Move move2 = new Move(40, 48, new Pawn(Position.WHITE), null, false);

    historyManager.addMove(move1, snapshot, 0);
    assertEquals(2, historyManager.getRepetitionCount(snapshot)); // initial 1 + 1 added

    historyManager.addMove(move2, "other", 0);
    historyManager.addMove(move1, snapshot, 0);
    assertEquals(3, historyManager.getRepetitionCount(snapshot));
  }

  @Test
  public void testHalfMoveClockHistory() {
    Move move1 = new Move(48, 40, new Pawn(Position.WHITE), null, false);
    historyManager.addMove(move1, "s1", 1);
    historyManager.addMove(move1, "s2", 2);

    assertEquals(2, historyManager.getPreviousHalfMoveClock());
    historyManager.undo();
    assertEquals(1, historyManager.getPreviousHalfMoveClock());
    historyManager.undo();
    assertEquals(0, historyManager.getPreviousHalfMoveClock());
  }
}
