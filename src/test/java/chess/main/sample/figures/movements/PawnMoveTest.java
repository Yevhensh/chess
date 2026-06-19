package chess.main.sample.figures.movements;

import static org.junit.jupiter.api.Assertions.*;

import chess.main.sample.figures.Position;
import chess.main.sample.figures.instances.Pawn;
import chess.main.sample.storage.ChessPositionsStorage;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PawnMoveTest {
  private ChessPositionsStorage storage;

  @BeforeEach
  public void setUp() {
    storage = new ChessPositionsStorage();
    storage.setPositionsContainer(new HashMap<>());
  }

  @Test
  public void testPawnInitialMoves() {
    Pawn pawn = new Pawn(Position.WHITE);
    int startCell = 52; // e2
    storage.getPositionsContainer().put(startCell, pawn);

    List<Integer> moves = pawn.getAllAvailableMovements(storage.getPositionsContainer(), startCell);
    assertEquals(2, moves.size());
    assertTrue(moves.contains(44)); // e3
    assertTrue(moves.contains(36)); // e4
  }

  @Test
  public void testPawnCapture() {
    Pawn pawn = new Pawn(Position.WHITE);
    int startCell = 52; // e2
    storage.getPositionsContainer().put(startCell, pawn);

    storage.getPositionsContainer().put(43, new Pawn(Position.BLACK)); // d3

    List<Integer> moves = pawn.getAllAvailableMovements(storage.getPositionsContainer(), startCell);
    assertTrue(moves.contains(43));
  }

  @Test
  public void testPawnBlocked() {
    Pawn pawn = new Pawn(Position.WHITE);
    int startCell = 52; // e2
    storage.getPositionsContainer().put(startCell, pawn);

    storage.getPositionsContainer().put(44, new Pawn(Position.BLACK)); // e3

    List<Integer> moves = pawn.getAllAvailableMovements(storage.getPositionsContainer(), startCell);
    assertTrue(moves.isEmpty());
  }
}
