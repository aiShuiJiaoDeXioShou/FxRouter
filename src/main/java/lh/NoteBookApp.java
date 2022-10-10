package lh;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lh.router.Router;

public class NoteBookApp extends Application {

    public static Stage primaryStage;

    public static Router router;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Note Book");
        primaryStage.getIcons().add(new Image("static/img/ic3.png"));
        primaryStage.setWidth(1000);
        primaryStage.setHeight(800);
        router = new Router("lh.view", primaryStage);
        router.config().patchCatch(() -> System.out.println("没有该路径哦！"));
        primaryStage.show();
    }
}
