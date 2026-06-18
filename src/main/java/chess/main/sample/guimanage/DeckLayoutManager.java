package chess.main.sample.guimanage;

import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Position;
import chess.main.sample.figures.instances.Empty;
import chess.main.sample.game.GameState;
import chess.main.sample.game.Selected;
import chess.main.sample.storage.ChessPositionsStorage;
import chess.main.sample.utils.ChessUtils;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static chess.main.sample.constants.SceneConstants.*;

public class DeckLayoutManager {

    private static final int IMAGE_VIEW_OFFSET = 10;

    private final Map<Integer, List<Node>> cellNodes = new HashMap<>();
    private final List<Color> layoutColors = new ArrayList<>();
    private final ChessPositionsStorage chessPositionsStorage;
    private final GameState gameState;
    private MovementHandler movementHandler;
    private AnchorPane pane;
    private Label statusLabel;

    public DeckLayoutManager(ChessPositionsStorage chessPositionsStorage, GameState gameState) {
        this.chessPositionsStorage = chessPositionsStorage;
        this.gameState = gameState;
    }

    public void setMovementHandler(MovementHandler movementHandler) {
        this.movementHandler = movementHandler;
    }

    public AnchorPane getPane() {
        return pane;
    }

    public void initializeBasicGameStart() {
        Map<Integer, Figure> positions = chessPositionsStorage.gameStartPositionRemind();
        pane = new AnchorPane();
        pane.getChildren().add(createDeckBackgroundRectangle());
        renderCoordinates();
        pane.getChildren().add(createStatusLabel());
        pane.getChildren().add(createControlButtons());

        IntStream.range(0, CELLS_COUNT).forEach(cellIndex -> {
            Figure figure = positions.getOrDefault(cellIndex, new Empty());
            initializeCell(cellIndex, figure);
        });
    }

    private HBox createControlButtons() {
        Button undoBtn = new Button("Undo");
        undoBtn.setOnAction(e -> movementHandler.handleUndo());

        Button redoBtn = new Button("Redo");
        redoBtn.setOnAction(e -> movementHandler.handleRedo());

        HBox hbox = new HBox(10, undoBtn, redoBtn);
        hbox.setLayoutX(START_FROM_BORDER_WIDTH);
        hbox.setLayoutY(10);
        return hbox;
    }

    private void renderCoordinates() {
        String[] files = {"a", "b", "c", "d", "e", "f", "g", "h"};
        for (int i = 0; i < 8; i++) {
            Label fileLabel = new Label(files[i]);
            fileLabel.setTextFill(Color.web("#a7a6a4"));
            fileLabel.setFont(Font.font("Segoe UI", 14));
            fileLabel.setLayoutX(START_FROM_BORDER_WIDTH + i * RECTANGLE_DIMENSION + RECTANGLE_DIMENSION / 2.0 - 5);
            fileLabel.setLayoutY(START_FROM_BORDER_WIDTH + ROWS_COUNT * RECTANGLE_DIMENSION + 5);
            pane.getChildren().add(fileLabel);

            Label rankLabel = new Label(String.valueOf(8 - i));
            rankLabel.setTextFill(Color.web("#a7a6a4"));
            rankLabel.setFont(Font.font("Segoe UI", 14));
            rankLabel.setLayoutX(START_FROM_BORDER_WIDTH - 20);
            rankLabel.setLayoutY(START_FROM_BORDER_WIDTH + i * RECTANGLE_DIMENSION + RECTANGLE_DIMENSION / 2.0 - 10);
            pane.getChildren().add(rankLabel);
        }
    }

    public void highlightCell(int index, Color color) {
        Rectangle rectangle = findCellRectangle(index);
        if (rectangle != null) {
            rectangle.setFill(color);
        }
    }

    public void showMoveIndicator(int index, boolean isCapture) {
        List<Node> nodes = cellNodes.get(index);
        if (nodes == null) return;

        int row = ChessUtils.getRow(index);
        int col = ChessUtils.getCol(index);
        int x = getScreenX(col);
        int y = getScreenY(row);

        if (isCapture) {
            Circle ring = new Circle(x + RECTANGLE_DIMENSION / 2.0, y + RECTANGLE_DIMENSION / 2.0, RECTANGLE_DIMENSION / 2.0 - 5);
            ring.setFill(Color.TRANSPARENT);
            ring.setStroke(Color.web("#000000", 0.1));
            ring.setStrokeWidth(5);
            ring.setStrokeType(StrokeType.INSIDE);
            ring.setMouseTransparent(true);
            pane.getChildren().add(ring);
            nodes.add(ring);
        } else {
            Circle dot = new Circle(x + RECTANGLE_DIMENSION / 2.0, y + RECTANGLE_DIMENSION / 2.0, 10);
            dot.setFill(Color.web("#000000", 0.1));
            dot.setMouseTransparent(true);
            pane.getChildren().add(dot);
            nodes.add(dot);
        }
    }

    public void clearIndicators(int index) {
        List<Node> nodes = cellNodes.get(index);
        if (nodes == null || nodes.size() <= 1) return;

        List<Node> indicators = nodes.stream()
                .filter(node -> node instanceof Circle)
                .toList();

        pane.getChildren().removeAll(indicators);
        nodes.removeAll(indicators);
    }

    public void highlightCell(int index) {
        highlightCell(index, Color.web("#f6f669"));
    }

    public void unhighlightCell(int index) {
        Rectangle rectangle = findCellRectangle(index);
        if (rectangle != null) {
            rectangle.setFill(layoutColors.get(index));
        }
    }

    public Selected getSelectedByDeckPosition(int x, int y) {
        int col = (x - START_FROM_BORDER_WIDTH) / RECTANGLE_DIMENSION;
        int row = (y - START_FROM_BORDER_WIDTH) / RECTANGLE_DIMENSION;
        int deckCell = ChessUtils.getIndex(row, col);
        return new Selected(chessPositionsStorage.getFigureByDeckCell(deckCell), deckCell);
    }

    public void makeTurn(int fromIndex, int toIndex, Figure figure) {
        clearCell(fromIndex);
        clearCell(toIndex);
        renderCellAtIndex(fromIndex, new Empty());
        renderCellAtIndex(toIndex, figure);
    }

    private Rectangle createDeckBackgroundRectangle() {
        Rectangle rectangle = new Rectangle(0, 0, SCENE_WIDTH, SCENE_HEIGHT);
        rectangle.setFill(Color.web("#262421"));
        return rectangle;
    }

    private Label createStatusLabel() {
        statusLabel = new Label("White's turn");
        statusLabel.setPrefWidth(SCENE_WIDTH);
        statusLabel.setAlignment(Pos.CENTER);
        statusLabel.setLayoutX(0);
        statusLabel.setLayoutY(START_FROM_BORDER_WIDTH + RECTANGLE_DIMENSION * ROWS_COUNT + 20);
        statusLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        statusLabel.setTextFill(Color.WHITE);
        return statusLabel;
    }

    public void updateStatusMessage() {
        movementHandler.updateStatus();
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    private void initializeCell(int cellIndex, Figure figure) {
        int row = ChessUtils.getRow(cellIndex);
        int col = ChessUtils.getCol(cellIndex);
        Color cellColor = getCellColor(row, col);
        layoutColors.add(cellColor);
        renderCell(cellIndex, cellColor, getScreenX(col), getScreenY(row), figure);
    }

    public void renderCellAtIndex(int cellIndex, Figure figure) {
        int row = ChessUtils.getRow(cellIndex);
        int col = ChessUtils.getCol(cellIndex);
        Color color = layoutColors.get(cellIndex);
        renderCell(cellIndex, color, getScreenX(col), getScreenY(row), figure);
    }

    private void renderCell(int cellIndex, Color color, int x, int y, Figure figure) {
        List<Node> nodes = new ArrayList<>();
        nodes.add(createCellRectangle(x, y, color));

        if (!(figure instanceof Empty)) {
            nodes.add(createPieceImageView(x, y, figure));
        }

        cellNodes.put(cellIndex, nodes);
    }

    private Rectangle createCellRectangle(int x, int y, Color color) {
        Rectangle rectangle = new Rectangle(x, y, RECTANGLE_DIMENSION, RECTANGLE_DIMENSION);
        rectangle.setFill(color);
        rectangle.setOnMouseClicked(movementHandler);
        pane.getChildren().add(rectangle);
        return rectangle;
    }

    private ImageView createPieceImageView(int cellX, int cellY, Figure figure) {
        Image image = new Image(getClass().getClassLoader().getResourceAsStream(figure.getFilenamePath()));
        ImageView imageView = new ImageView(image);
        imageView.setX(cellX + IMAGE_VIEW_OFFSET);
        imageView.setY(cellY + IMAGE_VIEW_OFFSET);
        imageView.setFitWidth(IMAGE_DIMENSION);
        imageView.setFitHeight(IMAGE_DIMENSION);
        imageView.setPreserveRatio(true);
        imageView.setOnMouseClicked(movementHandler);
        pane.getChildren().add(imageView);
        return imageView;
    }

    private void clearCell(int cellIndex) {
        List<Node> nodes = cellNodes.get(cellIndex);
        if (nodes == null) {
            return;
        }
        pane.getChildren().removeAll(nodes);
        cellNodes.remove(cellIndex);
    }

    private Rectangle findCellRectangle(int index) {
        List<Node> nodes = cellNodes.get(index);
        if (nodes == null || nodes.isEmpty()) {
            return null;
        }
        Node firstNode = nodes.get(0);
        return firstNode instanceof Rectangle rectangle ? rectangle : null;
    }

    private Color getCellColor(int row, int col) {
        return (row + col) % 2 != 0 ? Color.web("#769656") : Color.web("#eeeed2");
    }

    private int getScreenX(int col) {
        return START_FROM_BORDER_WIDTH + col * RECTANGLE_DIMENSION;
    }

    private int getScreenY(int row) {
        return START_FROM_BORDER_WIDTH + row * RECTANGLE_DIMENSION;
    }
}
