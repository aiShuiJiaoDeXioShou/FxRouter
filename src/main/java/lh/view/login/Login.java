package lh.view.login;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import lh.NoteBookApp;
import lh.router.annotation.FXRoute;


@FXRoute("/")
public class Login extends VBox {



    public Login() {
        Button page = new Button("page");
        page.setOnMouseClicked(e->{
            NoteBookApp.router.toRoute("/page1");
        });
        getChildren().add(page);
    }
}
