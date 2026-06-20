package chess.main.sample.game;

import static org.junit.jupiter.api.Assertions.*;

import chess.main.sample.figures.Position;
import chess.main.sample.figures.instances.Pawn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameStateTest {
  private GameState gameState;

  @BeforeEach
  public void setUp() {
    gameState = new GameState();
  }

  @Test
  public void testInitialState() {
    assertEquals(Position.WHITE, gameState.getCurrentTurn());
    assertEquals(0, gameState.getHalfMoveClock());
    assertFalse(gameState.hasSelection());
    assertNotNull(gameState.getHistoryManager());
  }

  @Test
  public void testSwitchTurn() {
    gameState.switchTurn();
    assertEquals(Position.BLACK, gameState.getCurrentTurn());
    gameState.switchTurn();
    assertEquals(Position.WHITE, gameState.getCurrentTurn());
  }

  @Test
  public void testSelection() {
    Pawn pawn = new Pawn(Position.WHITE);
    gameState.setSelected(pawn, 48);
    assertTrue(gameState.hasSelection());
    assertEquals(pawn, gameState.getSelectedPiece());
    assertEquals(48, gameState.getSelectedIndex());

    gameState.clearSelection();
    assertFalse(gameState.hasSelection());
    assertNull(gameState.getSelectedPiece());
    assertEquals(-1, gameState.getSelectedIndex());
  }

  @Test
  public void testHalfMoveClock() {
    gameState.incrementHalfMoveClock();
    assertEquals(1, gameState.getHalfMoveClock());
    gameState.incrementHalfMoveClock();
    assertEquals(2, gameState.getHalfMoveClock());

    gameState.resetHalfMoveClock();
    assertEquals(0, gameState.getHalfMoveClock());

    gameState.setHalfMoveClock(50);
    assertEquals(50, gameState.getHalfMoveClock());
  }
}
