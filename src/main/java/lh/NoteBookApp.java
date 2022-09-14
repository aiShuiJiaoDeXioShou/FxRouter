package lh;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lh.router.Router;

import java.util.Set;

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
        router.getRoutePaths().forEach(System.out::println);
        primaryStage.show();
    }
}
