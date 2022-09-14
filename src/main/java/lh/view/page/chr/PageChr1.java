package lh.view.page.chr;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lh.router.annotation.FXRoute;

@FXRoute("/page1/chr1")
public class PageChr1 extends HBox {

    public PageChr1() {
        this.getChildren().add(new Label("Hello Word! Page1"+this.getClass().getSimpleName()));
    }

}
