package chess.main.sample.game;

import chess.main.sample.figures.Figure;

public record Selected(Figure selected, int index) {
    private static Figure globalSelected;
    private static int globalIndex;

    public static Figure getGlobalSelected() {
        return globalSelected;
    }

    public static void setGlobalSelected(Selected selected) {
        globalSelected = selected.selected();
        globalIndex = selected.index();
    }

    public static int getGlobalIndex() {
        return globalIndex;
    }

    public static void setGlobalIndex(int globalIndex) {
        Selected.globalIndex = globalIndex;
    }

    public static void emptyGlobalSelected() {
        globalSelected = null;
        globalIndex = 0;
    }

    public static boolean isGlobalSelected() {
        return globalSelected != null;
    }
}
