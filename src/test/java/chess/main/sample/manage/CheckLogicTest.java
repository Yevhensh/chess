package chess.main.sample.manage;

import chess.main.sample.figures.Position;
import chess.main.sample.figures.instances.King;
import chess.main.sample.figures.instances.Queen;
import chess.main.sample.storage.ChessPositionsStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class CheckLogicTest {
    private ChessPositionsStorage storage;
    private DeckManager deckManager;

    @BeforeEach
    public void setUp() {
        storage = new ChessPositionsStorage();
        storage.setPositionsContainer(new HashMap<>());
        deckManager = DeckManager.getInstance();
        try {
            java.lang.reflect.Method setGlobal = ChessPositionsStorage.class.getDeclaredMethod("setGlobalStorage", ChessPositionsStorage.class);
            setGlobal.setAccessible(true);
            setGlobal.invoke(null, storage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsCheck() {
        King whiteKing = new King(Position.WHITE);
        int kingPos = 60; // e1
        storage.getPositionsContainer().put(kingPos, whiteKing);
        storage.setWhiteKingIndex(kingPos);

        Queen blackQueen = new Queen(Position.BLACK);
        int queenPos = 4; // e8
        storage.getPositionsContainer().put(queenPos, blackQueen);

        assertTrue(deckManager.isCheck(storage, Position.WHITE));
    }

    @Test
    public void testMoveLeavesKingInCheckIsIllegal() {
        King whiteKing = new King(Position.WHITE);
        int kingPos = 60; // e1
        storage.getPositionsContainer().put(kingPos, whiteKing);
        storage.setWhiteKingIndex(kingPos);

        Queen blackQueen = new Queen(Position.BLACK);
        int queenPos = 4; // e8
        storage.getPositionsContainer().put(queenPos, blackQueen);

        // White king is in check by Queen at e8.
        assertTrue(deckManager.isCheck(storage, Position.WHITE));

        // Legal move: d1 (59) - moves out of the 'e' file
        assertTrue(deckManager.isMoveLegal(60, 59, Position.WHITE));

        // Illegal move: e2 (52) - still on 'e' file, Queen attacks e file.
        // Wait, King cannot move to e2 if it's too far? No, King moves 1 square.
        // e1 (60) -> e2 (52) is NOT a valid King move.
        // King basic moves are dr, dc in {-1, 0, 1}
        // e1 is (7, 4). e2 is (6, 4). Index = 6*8 + 4 = 52.
        // That is a valid basic move for King.

        assertFalse(deckManager.isMoveLegal(60, 52, Position.WHITE));
    }
}
