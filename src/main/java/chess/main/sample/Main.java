package chess.main.sample;

import chess.main.sample.constants.SceneConstants;
import chess.main.sample.game.GameState;
import chess.main.sample.guimanage.DeckLayoutManager;
import chess.main.sample.guimanage.MovementHandler;
import chess.main.sample.manage.DeckManager;
import chess.main.sample.storage.ChessPositionsStorage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Chess");

        ChessPositionsStorage storage = new ChessPositionsStorage();
        GameState gameState = new GameState();

        DeckManager deckManager = new DeckManager(storage, gameState);
        DeckLayoutManager layoutManager = new DeckLayoutManager(storage, gameState);
        MovementHandler movementHandler = new MovementHandler(layoutManager, deckManager, storage, gameState);

        deckManager.setLayoutManager(layoutManager);
        layoutManager.setMovementHandler(movementHandler);

        layoutManager.initializeBasicGameStart();

        primaryStage.setScene(new Scene(layoutManager.getPane(), SceneConstants.SCENE_WIDTH,
                SceneConstants.SCENE_HEIGHT));
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
