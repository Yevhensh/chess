package chess.main.sample.figures.movements;

import chess.main.sample.figures.Position;
import chess.main.sample.figures.instances.Bishop;
import chess.main.sample.figures.instances.Rok;
import chess.main.sample.manage.DeckManager;
import chess.main.sample.storage.ChessPositionsStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SlidingMovementsTest {
    private ChessPositionsStorage storage;

    @BeforeEach
    public void setUp() {
        storage = new ChessPositionsStorage();
        storage.setPositionsContainer(new HashMap<>());
        try {
            java.lang.reflect.Method setGlobal = ChessPositionsStorage.class.getDeclaredMethod("setGlobalStorage", ChessPositionsStorage.class);
            setGlobal.setAccessible(true);
            setGlobal.invoke(null, storage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRookBlockedByAlly() {
        Rok rook = new Rok(Position.WHITE);
        int startCell = 56; // a1
        storage.getPositionsContainer().put(startCell, rook);
        storage.getPositionsContainer().put(48, new Bishop(Position.WHITE)); // Blocked at a2

        List<Integer> moves = rook.getAllAvailableMovements(startCell);
        // Should only be able to move right on the 1st rank
        assertFalse(moves.contains(48));
        assertTrue(moves.contains(57)); // b1
        assertTrue(moves.contains(63)); // h1
    }

    @Test
    public void testBishopBlockedAndCapture() {
        Bishop bishop = new Bishop(Position.WHITE);
        int startCell = 27; // d4
        storage.getPositionsContainer().put(startCell, bishop);
        storage.getPositionsContainer().put(13, new Bishop(Position.BLACK)); // f6 - Capture
        storage.getPositionsContainer().put(45, new Bishop(Position.WHITE)); // f2 - Blocked

        List<Integer> moves = bishop.getAllAvailableMovements(startCell);
        assertTrue(moves.contains(13)); // Capture f6
        assertFalse(moves.contains(6)); // Cannot go past capture to g7
        assertFalse(moves.contains(45)); // Blocked by f2
        assertFalse(moves.contains(54)); // Cannot go past blocked g1
        assertTrue(moves.contains(18)); // c5
        assertTrue(moves.contains(36)); // e3
    }
}
