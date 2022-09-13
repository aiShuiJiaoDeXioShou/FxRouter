package lh.view.setting;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lh.router.annotation.FXRoute;

@FXRoute("/setting")
public class Setting extends VBox {

    public Setting() {
        getChildren().add(new Label("这里是设置页面"));
    }

}
