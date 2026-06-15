package chess.main.sample.figures.movements;

import chess.main.sample.figures.Position;
import chess.main.sample.figures.instances.Knight;
import chess.main.sample.manage.DeckManager;
import chess.main.sample.storage.ChessPositionsStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class KnightMoveTest {
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
    public void testKnightCenterMoves() {
        Knight knight = new Knight(Position.WHITE);
        int startCell = 36; // e4
        storage.getPositionsContainer().put(startCell, knight);

        List<Integer> moves = knight.getAllAvailableMovements(startCell);
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
    public void testKnightEdgeNoWrapAround() {
        Knight knight = new Knight(Position.WHITE);
        int startCell = 40; // a3
        storage.getPositionsContainer().put(startCell, knight);

        List<Integer> moves = knight.getAllAvailableMovements(startCell);
        // From a3 (40):
        // Up 2, Right 1: 40 - 16 + 1 = 25 (b5) - OK
        // Down 2, Right 1: 40 + 16 + 1 = 57 (b1) - OK
        // Up 1, Right 2: 40 - 8 + 2 = 34 (c4) - OK
        // Down 1, Right 2: 40 + 8 + 2 = 50 (c2) - OK
        // Left moves should NOT exist or wrap.
        assertEquals(4, moves.size());
        assertTrue(moves.contains(25));
        assertTrue(moves.contains(57));
        assertTrue(moves.contains(34));
        assertTrue(moves.contains(50));
    }
}
