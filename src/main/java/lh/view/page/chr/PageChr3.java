package lh.view.page.chr;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lh.router.annotation.FXRoute;

@FXRoute("/page2/chr2")
public class PageChr3 extends HBox {

    public PageChr3() {
        this.getChildren().add(new Label("Hello Word! "+this.getClass().getSimpleName()));
    }

}
