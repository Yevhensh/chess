package chess.main.sample.game;

import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Position;

public class GameState {
    private Position currentTurn = Position.WHITE;
    private Figure selectedPiece;
    private int selectedIndex = -1;
    private final HistoryManager historyManager = new HistoryManager();

    public Position getCurrentTurn() {
        return currentTurn;
    }


    public void switchTurn() {
        currentTurn = (currentTurn == Position.WHITE) ? Position.BLACK : Position.WHITE;
    }

    public Figure getSelectedPiece() {
        return selectedPiece;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelected(Figure piece, int index) {
        this.selectedPiece = piece;
        this.selectedIndex = index;
    }

    public void clearSelection() {
        this.selectedPiece = null;
        this.selectedIndex = -1;
    }

    public boolean hasSelection() {
        return selectedPiece != null;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}
