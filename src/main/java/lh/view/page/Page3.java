package lh.view.page;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lh.router.annotation.FXRoute;

@FXRoute("/page3/ui")
public class Page3 extends HBox {

    public Page3() {
        this.getChildren().add(new Label("Hello Word! "+this.getClass().getSimpleName()));
    }

}
