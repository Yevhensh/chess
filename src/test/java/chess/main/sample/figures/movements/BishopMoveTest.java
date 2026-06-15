package chess.main.sample.figures.movements;

import chess.main.sample.figures.Position;
import chess.main.sample.figures.instances.Bishop;
import chess.main.sample.figures.instances.Pawn;
import chess.main.sample.storage.ChessPositionsStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BishopMoveTest {
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
    public void testBishopCenterMoves() {
        Bishop bishop = new Bishop(Position.WHITE);
        int startCell = 36; // e4
        storage.getPositionsContainer().put(startCell, bishop);

        List<Integer> moves = bishop.getAllAvailableMovements(startCell);
        // Diagonals from e4:
        // Up-Right: 29, 22, 15
        // Up-Left: 27, 18, 9, 0
        // Down-Right: 45, 54, 63
        // Down-Left: 43, 50, 57
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

        // Ally at 27 (d5)
        storage.getPositionsContainer().put(27, new Pawn(Position.WHITE));

        List<Integer> moves = bishop.getAllAvailableMovements(startCell);
        assertFalse(moves.contains(27));
        assertFalse(moves.contains(18)); // Beyond blocked cell
        assertTrue(moves.contains(29)); // Other direction still OK
    }

    @Test
    public void testBishopCaptureEnemy() {
        Bishop bishop = new Bishop(Position.WHITE);
        int startCell = 36; // e4
        storage.getPositionsContainer().put(startCell, bishop);

        // Enemy at 27 (d5)
        storage.getPositionsContainer().put(27, new Pawn(Position.BLACK));

        List<Integer> moves = bishop.getAllAvailableMovements(startCell);
        assertTrue(moves.contains(27)); // Can capture
        assertFalse(moves.contains(18)); // But cannot jump over
    }

    @Test
    public void testBishopAtEdge() {
        Bishop bishop = new Bishop(Position.WHITE);
        int startCell = 0; // a8
        storage.getPositionsContainer().put(startCell, bishop);

        List<Integer> moves = bishop.getAllAvailableMovements(startCell);
        // Only Down-Right: 9, 18, 27, 36, 45, 54, 63
        assertEquals(7, moves.size());
        assertTrue(moves.contains(63));
    }
}
