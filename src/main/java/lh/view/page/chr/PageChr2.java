package lh.view.page.chr;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lh.router.annotation.FXRoute;

@FXRoute("/page1/chr2")
public class PageChr2 extends HBox {

    public PageChr2() {
        this.getChildren().add(new Label("Hello Word! "+this.getClass().getSimpleName()));
    }

}
