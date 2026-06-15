package chess.main.sample.guimanage;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class LayoutContainer {

    private static Pane layout;
    private static List<Color> layoutColors = new ArrayList<>();
    private static Label statusLabel;

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

    public static Label getStatusLabel() {
        return statusLabel;
    }

    public static void setStatusLabel(Label statusLabel) {
        LayoutContainer.statusLabel = statusLabel;
    }
}
