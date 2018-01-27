package chess.main.sample;

import chess.main.sample.constants.SceneConstants;
import chess.main.sample.guimanage.DeckLayoutManager;
import chess.main.sample.guimanage.LayoutContainer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Chess");
        DeckLayoutManager.getInstance().initializeBasicGameStart();
        primaryStage.setScene(new Scene(LayoutContainer.getLayout(), SceneConstants.SCENE_WIDTH,
                SceneConstants.SCENE_HEIGHT));
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
