package chess.main.sample.manage;

import static org.junit.jupiter.api.Assertions.*;

import chess.main.sample.figures.Position;
import chess.main.sample.figures.instances.King;
import chess.main.sample.figures.instances.Rok;
import chess.main.sample.game.GameState;
import chess.main.sample.storage.ChessPositionsStorage;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DeckManagerTest {
  private ChessPositionsStorage storage;
  private DeckManager deckManager;

  @BeforeEach
  public void setUp() {
    storage = new ChessPositionsStorage();
    storage.setPositionsContainer(new HashMap<>());
    deckManager = new DeckManager(storage, new GameState());
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
}
