package chess.main.sample.guimanage;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class LayoutContainer {

    private static Pane layout;
    private static List<Color> layoutColors = new ArrayList<>();

    public static Pane getLayout() {
        return layout;
    }

    public static void setLayout(Pane layout) {
        LayoutContainer.layout = layout;
    }

    public static void addLayoutColor(Color color) {
        layoutColors.add(color);
    }

    public static List<Color> getLayoutColors() {
        return layoutColors;
    }
}
