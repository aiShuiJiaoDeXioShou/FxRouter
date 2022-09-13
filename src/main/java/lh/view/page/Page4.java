package lh.view.page;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lh.router.annotation.FXRoute;

@FXRoute("/page4/ui/good/")
public class Page4 extends HBox {

    public Page4() {
        this.getChildren().add(new Label("Hello Word! "+this.getClass().getSimpleName()));
    }

}
