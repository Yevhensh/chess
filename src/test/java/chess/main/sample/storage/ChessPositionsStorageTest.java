package chess.main.sample.storage;

import static org.junit.jupiter.api.Assertions.*;

import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Position;
import chess.main.sample.figures.instances.*;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChessPositionsStorageTest {
  private ChessPositionsStorage storage;

  @BeforeEach
  public void setUp() {
    storage = new ChessPositionsStorage();
  }

  @Test
  public void testGameStartPositionRemind() {
    Map<Integer, Figure> positions = storage.gameStartPositionRemind();

    assertNotNull(positions);
    assertEquals(32, positions.size());

    // Check some specific pieces
    assertTrue(positions.get(0) instanceof Rok);
    assertEquals(Position.BLACK, positions.get(0).getPosition());

    assertTrue(positions.get(4) instanceof King);
    assertEquals(Position.BLACK, positions.get(4).getPosition());
    assertEquals(4, storage.getBlackKingIndex());

    assertTrue(positions.get(60) instanceof King);
    assertEquals(Position.WHITE, positions.get(60).getPosition());
    assertEquals(60, storage.getWhiteKingIndex());

    assertTrue(positions.get(56) instanceof Rok);
    assertEquals(Position.WHITE, positions.get(56).getPosition());

    // Check pawns
    for (int i = 8; i < 16; i++) {
      assertTrue(positions.get(i) instanceof Pawn);
      assertEquals(Position.BLACK, positions.get(i).getPosition());
    }
    for (int i = 48; i < 56; i++) {
      assertTrue(positions.get(i) instanceof Pawn);
      assertEquals(Position.WHITE, positions.get(i).getPosition());
    }
  }

  @Test
  public void testGetFigureByDeckCell() {
    storage.gameStartPositionRemind();
    Figure figure = storage.getFigureByDeckCell(60);
    assertTrue(figure instanceof King);
    assertEquals(Position.WHITE, figure.getPosition());

    assertNull(storage.getFigureByDeckCell(32)); // Empty square
  }

  @Test
  public void testKingIndexSetters() {
    storage.setWhiteKingIndex(10);
    assertEquals(10, storage.getWhiteKingIndex());

    storage.setBlackKingIndex(20);
    assertEquals(20, storage.getBlackKingIndex());
  }
}
