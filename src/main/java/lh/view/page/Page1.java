package lh.view.page;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lh.router.annotation.FXRoute;

@FXRoute("/page1")
public class Page1 extends HBox {

    public Page1() {
        this.getChildren().add(new Label("Hello Word! Page1"));
    }

}
