package chess.main.sample.figures.movements;

import chess.main.sample.figures.Position;
import chess.main.sample.figures.instances.Rok;
import chess.main.sample.figures.instances.Pawn;
import chess.main.sample.storage.ChessPositionsStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RokMoveTest {
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
    public void testRokCenterMoves() {
        Rok rok = new Rok(Position.WHITE);
        int startCell = 36; // e4
        storage.getPositionsContainer().put(startCell, rok);

        List<Integer> moves = rok.getAllAvailableMovements(startCell);
        // Cross from e4:
        // Up: 28, 20, 12, 4
        // Down: 44, 52, 60
        // Left: 35, 34, 33, 32
        // Right: 37, 38, 39
        assertEquals(14, moves.size());
        assertTrue(moves.contains(4));
        assertTrue(moves.contains(60));
        assertTrue(moves.contains(32));
        assertTrue(moves.contains(39));
    }

    @Test
    public void testRokBlockedByAlly() {
        Rok rok = new Rok(Position.WHITE);
        int startCell = 36; // e4
        storage.getPositionsContainer().put(startCell, rok);

        // Ally at 28 (e5)
        storage.getPositionsContainer().put(28, new Pawn(Position.WHITE));

        List<Integer> moves = rok.getAllAvailableMovements(startCell);
        assertFalse(moves.contains(28));
        assertFalse(moves.contains(20)); // Beyond blocked cell
        assertTrue(moves.contains(44)); // Other direction still OK
    }

    @Test
    public void testRokCaptureEnemy() {
        Rok rok = new Rok(Position.WHITE);
        int startCell = 36; // e4
        storage.getPositionsContainer().put(startCell, rok);

        // Enemy at 28 (e5)
        storage.getPositionsContainer().put(28, new Pawn(Position.BLACK));

        List<Integer> moves = rok.getAllAvailableMovements(startCell);
        assertTrue(moves.contains(28)); // Can capture
        assertFalse(moves.contains(20)); // But cannot jump over
    }

    @Test
    public void testRokAtEdge() {
        Rok rok = new Rok(Position.WHITE);
        int startCell = 0; // a8
        storage.getPositionsContainer().put(startCell, rok);

        List<Integer> moves = rok.getAllAvailableMovements(startCell);
        // Right: 1, 2, 3, 4, 5, 6, 7
        // Down: 8, 16, 24, 32, 40, 48, 56
        assertEquals(14, moves.size());
        assertTrue(moves.contains(7));
        assertTrue(moves.contains(56));
    }
}
