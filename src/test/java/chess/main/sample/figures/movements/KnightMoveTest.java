package chess.main.sample.figures.movements;

import static org.junit.jupiter.api.Assertions.*;

import chess.main.sample.figures.Position;
import chess.main.sample.figures.instances.Knight;
import chess.main.sample.figures.instances.Pawn;
import chess.main.sample.storage.ChessPositionsStorage;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class KnightMoveTest {
  private ChessPositionsStorage storage;

  @BeforeEach
  public void setUp() {
    storage = new ChessPositionsStorage();
    storage.setPositionsContainer(new HashMap<>());
  }

  @Test
  public void testKnightCenterMoves() {
    Knight knight = new Knight(Position.WHITE);
    int startCell = 36; // e4
    storage.getPositionsContainer().put(startCell, knight);

    List<Integer> moves =
        knight.getAllAvailableMovements(storage.getPositionsContainer(), startCell);
    assertEquals(8, moves.size());
    assertTrue(moves.contains(19));
    assertTrue(moves.contains(21));
    assertTrue(moves.contains(26));
    assertTrue(moves.contains(30));
    assertTrue(moves.contains(42));
    assertTrue(moves.contains(46));
    assertTrue(moves.contains(51));
    assertTrue(moves.contains(53));
  }

  @Test
  public void testKnightCaptureAndBlocked() {
    Knight knight = new Knight(Position.WHITE);
    int startCell = 36; // e4
    storage.getPositionsContainer().put(startCell, knight);

    storage.getPositionsContainer().put(19, new Pawn(Position.WHITE));
    storage.getPositionsContainer().put(21, new Pawn(Position.BLACK));

    List<Integer> moves =
        knight.getAllAvailableMovements(storage.getPositionsContainer(), startCell);
    assertFalse(moves.contains(19));
    assertTrue(moves.contains(21));
  }
}
