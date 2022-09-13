package lh.view.login;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lh.NoteBookApp;
import lh.router.annotation.FXRoute;
import javafx.scene.layout.VBox;

import java.util.concurrent.atomic.AtomicInteger;

@FXRoute("/")
public class Login extends VBox {
    public Login() {
        AtomicInteger i = new AtomicInteger();
        Button button = new Button("切换到setting页面");
        getChildren().add(button);
        button.setOnMouseClicked(event -> {
            NoteBookApp.router.toRoute("/setting");
        });
        Button next = new Button("下一页");
        next.setOnMouseClicked(event -> {
            i.getAndIncrement();
            String path = "/page"+i;
            Boolean isroute = NoteBookApp.router.isRoute(path);
            if (!isroute) {
                i.set(1);
            }
            NoteBookApp.router.toRoute(path);
        });
        getChildren().add(next);
        getChildren().add(new Label("你好世界！！！"));
    }
}
