package chess.main.sample.manage;

import static org.junit.jupiter.api.Assertions.*;

import chess.main.sample.figures.Position;
import chess.main.sample.figures.instances.*;
import chess.main.sample.game.GameState;
import chess.main.sample.storage.ChessPositionsStorage;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DeckManagerTest {
  private ChessPositionsStorage storage;
  private GameState gameState;
  private DeckManager deckManager;

  @BeforeEach
  public void setUp() {
    storage = new ChessPositionsStorage();
    storage.setPositionsContainer(new HashMap<>());
    gameState = new GameState();
    deckManager = new DeckManager(storage, gameState);
  }

  @Test
  public void testIsCheck() {
    storage.getPositionsContainer().put(60, new King(Position.WHITE));
    storage.setWhiteKingIndex(60);
    storage.getPositionsContainer().put(4, new King(Position.BLACK));
    storage.setBlackKingIndex(4);

    // Black Rok attacking White King
    storage.getPositionsContainer().put(56, new Rok(Position.BLACK));

    assertTrue(deckManager.isCheck(storage, Position.WHITE));
    assertFalse(deckManager.isCheck(storage, Position.BLACK));
  }

  @Test
  public void testIsMoveLegal_PinnedPiece() {
    storage.getPositionsContainer().put(60, new King(Position.WHITE));
    storage.setWhiteKingIndex(60);
    storage.getPositionsContainer().put(4, new King(Position.BLACK));
    storage.setBlackKingIndex(4);

    // White Rok at 52, Black Rok at 44 (attacking 60 through 52)
    Rok whiteRok = new Rok(Position.WHITE);
    storage.getPositionsContainer().put(52, whiteRok);
    storage.getPositionsContainer().put(44, new Rok(Position.BLACK));

    // White Rok move to 53 should be illegal as it leaves King in check
    assertFalse(deckManager.isMoveLegal(52, 53, Position.WHITE));

    // White Rok move to 44 should be legal (capturing the pinning piece)
    assertTrue(deckManager.isMoveLegal(52, 44, Position.WHITE));
  }

  @Test
  public void testKingMovingToCheck() {
    storage.getPositionsContainer().put(60, new King(Position.WHITE));
    storage.setWhiteKingIndex(60);

    // Black Rok attacking 61
    storage.getPositionsContainer().put(5, new Rok(Position.BLACK));

    // King moving to 61 should be illegal
    assertFalse(deckManager.isMoveLegal(60, 61, Position.WHITE));
  }

  @Test
  public void testIsCheckmate() {
    // White King at 59 (d1)
    storage.getPositionsContainer().put(59, new King(Position.WHITE));
    storage.setWhiteKingIndex(59);

    // Black Queen at 51 (d2)
    storage.getPositionsContainer().put(51, new Queen(Position.BLACK));
    // Black Bishop at 42 (c3) protecting Queen at 51
    storage.getPositionsContainer().put(42, new Bishop(Position.BLACK));

    // White King at 59 is in check by Queen at 51.
    // Moves for King at 59: 50, 51 (capture), 52, 58, 60.
    // 51 (d2) is protected by Bishop at 42.
    // 50 (c2) is attacked by Queen at 51 (diagonal).
    // 52 (e2) is attacked by Queen at 51 (diagonal).
    // 58 (c1) is attacked by Queen at 51 (horizontal).
    // 60 (e1) is attacked by Queen at 51 (horizontal).

    assertTrue(deckManager.isCheck(storage, Position.WHITE));
    assertTrue(deckManager.isCheckmate(Position.WHITE));
  }

  @Test
  public void testIsStalemate() {
    // Typical stalemate: White King at a8 (0), Black King at c7 (18), Black Queen at b6 (17)
    storage.getPositionsContainer().put(0, new King(Position.WHITE));
    storage.setWhiteKingIndex(0);
    storage.getPositionsContainer().put(18, new King(Position.BLACK));
    storage.setBlackKingIndex(18);
    storage.getPositionsContainer().put(17, new Queen(Position.BLACK));

    assertTrue(deckManager.isStalemate(Position.WHITE));
    assertFalse(deckManager.isCheckmate(Position.WHITE));
  }

  @Test
  public void testMakeTurn_Promotion() {
    Pawn whitePawn = new Pawn(Position.WHITE);
    storage.getPositionsContainer().put(8, whitePawn); // a7

    deckManager.makeTurn(8, 0); // Move to a8

    assertTrue(storage.getPositionsContainer().get(0) instanceof Queen);
    assertEquals(Position.WHITE, storage.getPositionsContainer().get(0).getPosition());
    assertNull(storage.getPositionsContainer().get(8));
  }

  @Test
  public void testUndoRedo_Capture() {
    Pawn whitePawn = new Pawn(Position.WHITE);
    Pawn blackPawn = new Pawn(Position.BLACK);
    storage.getPositionsContainer().put(35, whitePawn); // d4
    storage.getPositionsContainer().put(28, blackPawn); // e5

    deckManager.makeTurn(35, 28); // White pawn captures black pawn
    assertNull(storage.getPositionsContainer().get(35));
    assertEquals(whitePawn, storage.getPositionsContainer().get(28));

    deckManager.undoMove();
    assertEquals(whitePawn, storage.getPositionsContainer().get(35));
    assertEquals(blackPawn, storage.getPositionsContainer().get(28));

    deckManager.redoMove();
    assertNull(storage.getPositionsContainer().get(35));
    assertEquals(whitePawn, storage.getPositionsContainer().get(28));
  }
}
