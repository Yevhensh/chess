package chess.main.sample.figures.movements;

import static org.junit.jupiter.api.Assertions.*;

import chess.main.sample.figures.Position;
import chess.main.sample.figures.instances.Bishop;
import chess.main.sample.figures.instances.Pawn;
import chess.main.sample.storage.ChessPositionsStorage;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BishopMoveTest {
  private ChessPositionsStorage storage;

  @BeforeEach
  public void setUp() {
    storage = new ChessPositionsStorage();
    storage.setPositionsContainer(new HashMap<>());
  }

  @Test
  public void testBishopCenterMoves() {
    Bishop bishop = new Bishop(Position.WHITE);
    int startCell = 36; // e4
    storage.getPositionsContainer().put(startCell, bishop);

    List<Integer> moves =
        bishop.getAllAvailableMovements(storage.getPositionsContainer(), startCell);
    assertEquals(13, moves.size());
    assertTrue(moves.contains(29));
    assertTrue(moves.contains(0));
    assertTrue(moves.contains(63));
    assertTrue(moves.contains(57));
  }

  @Test
  public void testBishopBlockedByAlly() {
    Bishop bishop = new Bishop(Position.WHITE);
    int startCell = 36; // e4
    storage.getPositionsContainer().put(startCell, bishop);

    storage.getPositionsContainer().put(27, new Pawn(Position.WHITE));

    List<Integer> moves =
        bishop.getAllAvailableMovements(storage.getPositionsContainer(), startCell);
    assertFalse(moves.contains(27));
    assertFalse(moves.contains(18));
    assertTrue(moves.contains(29));
  }

  @Test
  public void testBishopCaptureEnemy() {
    Bishop bishop = new Bishop(Position.WHITE);
    int startCell = 36; // e4
    storage.getPositionsContainer().put(startCell, bishop);

    storage.getPositionsContainer().put(27, new Pawn(Position.BLACK));

    List<Integer> moves =
        bishop.getAllAvailableMovements(storage.getPositionsContainer(), startCell);
    assertTrue(moves.contains(27));
    assertFalse(moves.contains(18));
  }

  @Test
  public void testBishopAtEdge() {
    Bishop bishop = new Bishop(Position.WHITE);
    int startCell = 0; // a8
    storage.getPositionsContainer().put(startCell, bishop);

    List<Integer> moves =
        bishop.getAllAvailableMovements(storage.getPositionsContainer(), startCell);
    assertEquals(7, moves.size());
    assertTrue(moves.contains(63));
  }
}
