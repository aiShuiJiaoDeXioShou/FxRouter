package lh.view.page;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lh.router.annotation.FXRoute;

@FXRoute("/page2/ui1")
public class Page2 extends HBox {

    public Page2() {
        this.getChildren().add(new Label("Hello Word! Page2"));
    }

}
