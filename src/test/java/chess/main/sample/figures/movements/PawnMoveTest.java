package chess.main.sample.figures.movements;

import chess.main.sample.figures.Position;
import chess.main.sample.figures.instances.Pawn;
import chess.main.sample.manage.DeckManager;
import chess.main.sample.storage.ChessPositionsStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PawnMoveTest {
    private ChessPositionsStorage storage;

    @BeforeEach
    public void setUp() {
        storage = new ChessPositionsStorage();
        storage.setPositionsContainer(new HashMap<>());
        // We need to set it as global storage because DeckManager uses global storage
        try {
            java.lang.reflect.Method setGlobal = ChessPositionsStorage.class.getDeclaredMethod("setGlobalStorage", ChessPositionsStorage.class);
            setGlobal.setAccessible(true);
            setGlobal.invoke(null, storage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWhitePawnInitialMove() {
        Pawn pawn = new Pawn(Position.WHITE);
        int startCell = 52; // e2
        storage.getPositionsContainer().put(startCell, pawn);

        List<Integer> moves = pawn.getAllAvailableMovements(startCell);
        assertTrue(moves.contains(44)); // e3
        assertTrue(moves.contains(36)); // e4
        assertEquals(2, moves.size());
    }

    @Test
    public void testWhitePawnCapture() {
        Pawn pawn = new Pawn(Position.WHITE);
        int startCell = 52; // e2
        storage.getPositionsContainer().put(startCell, pawn);
        storage.getPositionsContainer().put(43, new Pawn(Position.BLACK)); // d3
        storage.getPositionsContainer().put(45, new Pawn(Position.BLACK)); // f3

        List<Integer> moves = pawn.getAllAvailableMovements(startCell);
        assertTrue(moves.contains(43));
        assertTrue(moves.contains(45));
        assertTrue(moves.contains(44)); // e3 (forward)
        assertTrue(moves.contains(36)); // e4 (double forward)
        assertEquals(4, moves.size());
    }

    @Test
    public void testPawnNoWrapAround() {
        Pawn pawn = new Pawn(Position.WHITE);
        int startCell = 48; // a2
        storage.getPositionsContainer().put(startCell, pawn);
        // Opponent pawn at index 39 (h3) - should NOT be capturable from a2
        storage.getPositionsContainer().put(39, new Pawn(Position.BLACK));

        List<Integer> moves = pawn.getAllAvailableMovements(startCell);
        assertFalse(moves.contains(39));
    }
}
