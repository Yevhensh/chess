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
    }

    @Test
    public void testRokCenterMoves() {
        Rok rok = new Rok(Position.WHITE);
        int startCell = 36; // e4
        storage.getPositionsContainer().put(startCell, rok);

        List<Integer> moves = rok.getAllAvailableMovements(storage.getPositionsContainer(), startCell);
        assertEquals(14, moves.size());
        assertTrue(moves.contains(4));  // e8
        assertTrue(moves.contains(60)); // e1
        assertTrue(moves.contains(32)); // a4
        assertTrue(moves.contains(39)); // h4
    }

    @Test
    public void testRokBlockedByAlly() {
        Rok rok = new Rok(Position.WHITE);
        int startCell = 36; // e4
        storage.getPositionsContainer().put(startCell, rok);

        storage.getPositionsContainer().put(44, new Pawn(Position.WHITE)); // e3

        List<Integer> moves = rok.getAllAvailableMovements(storage.getPositionsContainer(), startCell);
        assertFalse(moves.contains(44));
        assertFalse(moves.contains(52));
    }

    @Test
    public void testRokCaptureEnemy() {
        Rok rok = new Rok(Position.WHITE);
        int startCell = 36; // e4
        storage.getPositionsContainer().put(startCell, rok);

        storage.getPositionsContainer().put(44, new Pawn(Position.BLACK)); // e3

        List<Integer> moves = rok.getAllAvailableMovements(storage.getPositionsContainer(), startCell);
        assertTrue(moves.contains(44));
        assertFalse(moves.contains(52));
    }

    @Test
    public void testRokAtEdge() {
        Rok rok = new Rok(Position.WHITE);
        int startCell = 0; // a8
        storage.getPositionsContainer().put(startCell, rok);

        List<Integer> moves = rok.getAllAvailableMovements(storage.getPositionsContainer(), startCell);
        assertEquals(14, moves.size());
    }
}
