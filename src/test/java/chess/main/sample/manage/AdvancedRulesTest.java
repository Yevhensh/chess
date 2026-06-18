package chess.main.sample.manage;

import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Position;
import chess.main.sample.figures.instances.*;
import chess.main.sample.game.GameState;
import chess.main.sample.game.Move;
import chess.main.sample.storage.ChessPositionsStorage;
import chess.main.sample.utils.ChessUtils;
import chess.main.sample.figures.movements.PawnMove;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AdvancedRulesTest {
    private ChessPositionsStorage storage;
    private GameState gameState;
    private DeckManager deckManager;

    @BeforeEach
    public void setUp() {
        storage = new ChessPositionsStorage();
        storage.setPositionsContainer(new HashMap<>());
        gameState = new GameState();
        deckManager = new DeckManager(storage, gameState);
        // Mock DeckLayoutManager to avoid NullPointerException in makeTurn
        deckManager.setLayoutManager(new chess.main.sample.guimanage.DeckLayoutManager(storage, gameState) {
            @Override public void makeTurn(int f, int t, chess.main.sample.figures.Figure fig) {}
            @Override public void renderCellAtIndex(int i, chess.main.sample.figures.Figure fig) {}
            @Override public void updateStatusMessage() {}
        });
    }

    @Test
    public void testEnPassant() {
        Pawn whitePawn = new Pawn(Position.WHITE);
        Pawn blackPawn = new Pawn(Position.BLACK);

        int wIndex = 28; // row 3, col 4
        int bStart = 13; // row 1, col 5
        int bEnd = 29;   // row 3, col 5

        storage.getPositionsContainer().put(wIndex, whitePawn);
        storage.getPositionsContainer().put(bStart, blackPawn);

        // Black moves f7-f5 (13-29)
        deckManager.makeTurn(bStart, bEnd);
        Move lastMove = gameState.getHistoryManager().getLastMove();

        PawnMove pm = new PawnMove();
        pm.setLastMove(lastMove);
        List<Integer> moves = pm.determineAvailableMovements(storage.getPositionsContainer(), wIndex, whitePawn);

        // Target for En Passant is f6 (row 2, col 5) = 21
        int targetIndex = 21;
        assertTrue(moves.contains(targetIndex));

        // Execute En Passant
        gameState.switchTurn(); // Ensure it's White's turn
        deckManager.makeTurn(wIndex, targetIndex);

        // Black Pawn at f5 (29) should be removed
        assertNull(storage.getPositionsContainer().get(bEnd));
        assertEquals(whitePawn, storage.getPositionsContainer().get(targetIndex));
    }

    @Test
    public void testCastlingLegality() {
        // White King at e1 (60), White Rooks at a1 (56) and h1 (63)
        storage.getPositionsContainer().put(60, new King(Position.WHITE));
        storage.setWhiteKingIndex(60);
        storage.getPositionsContainer().put(56, new Rok(Position.WHITE));
        storage.getPositionsContainer().put(63, new Rok(Position.WHITE));

        // Path is clear, no one moved
        assertTrue(deckManager.isCastlingLegal(60, 62, Position.WHITE)); // Kingside
        assertTrue(deckManager.isCastlingLegal(60, 58, Position.WHITE)); // Queenside

        // Black Rok attacking f1 (61)
        storage.getPositionsContainer().put(5, new Rok(Position.BLACK));
        assertFalse(deckManager.isCastlingLegal(60, 62, Position.WHITE)); // Cannot pass through check
    }

    @Test
    public void testInsufficientMaterial() {
        storage.getPositionsContainer().put(60, new King(Position.WHITE));
        storage.getPositionsContainer().put(4, new King(Position.BLACK));
        assertTrue(deckManager.isInsufficientMaterial());

        storage.getPositionsContainer().put(61, new Bishop(Position.WHITE));
        assertTrue(deckManager.isInsufficientMaterial());

        storage.getPositionsContainer().put(5, new Knight(Position.BLACK));
        assertFalse(deckManager.isInsufficientMaterial());
    }

    @Test
    public void testThreefoldRepetition() {
        // Simple back and forth movement
        storage.getPositionsContainer().put(60, new King(Position.WHITE));
        storage.setWhiteKingIndex(60);
        storage.getPositionsContainer().put(4, new King(Position.BLACK));
        storage.setBlackKingIndex(4);

        // Move 1: Ke1-f1, Ke8-f8
        deckManager.makeTurn(60, 61);
        gameState.switchTurn();
        deckManager.makeTurn(4, 5);
        gameState.switchTurn();

        // Move 2: Kf1-e1, Kf8-e8 (Return to start)
        deckManager.makeTurn(61, 60);
        gameState.switchTurn();
        deckManager.makeTurn(5, 4);
        gameState.switchTurn();

        // Move 3: Ke1-f1, Ke8-f8 (Repetition 2)
        deckManager.makeTurn(60, 61);
        gameState.switchTurn();
        deckManager.makeTurn(4, 5);
        gameState.switchTurn();

        // Move 4: Kf1-e1, Kf8-e8 (Repetition 3)
        deckManager.makeTurn(61, 60);
        gameState.switchTurn();
        deckManager.makeTurn(5, 4);
        gameState.switchTurn();

        assertTrue(deckManager.isThreefoldRepetition());
    }

    @Test
    public void testFiftyMoveRule() {
        storage.getPositionsContainer().put(60, new King(Position.WHITE));
        storage.getPositionsContainer().put(4, new King(Position.BLACK));

        for (int i = 0; i < 100; i++) {
            assertFalse(deckManager.isFiftyMoveRule());
            gameState.incrementHalfMoveClock();
        }
        assertTrue(deckManager.isFiftyMoveRule());
    }
}
