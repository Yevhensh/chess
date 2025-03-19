package chess.main.sample.game;

import chess.main.sample.figures.Figure;

public class Selected {
    private static Figure globalSelected;
    private static int globalIndex;

    public static Figure getGlobalSelected() {
        return globalSelected;
    }

    public static void setGlobalSelected(Selected selected) {
        globalSelected = selected.getSelected();
        globalIndex = selected.getIndex();
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

    private Figure selected;
    private int index;

    public Selected(Figure selected, int index) {
        this.selected = selected;
        this.index = index;
    }

    public Figure getSelected() {
        return selected;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "Selected{" +
                "selected=" + selected +
                ", index=" + index +
                '}';
    }
}
