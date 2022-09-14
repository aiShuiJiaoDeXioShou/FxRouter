package lh.router;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * 这个类里面只能允许有一个根Node
 */
public class RouterView extends Pane {

    /**
     * 切换视图
     */
    public void switchView(Node node) {
        getChildren().set(0, node);
    }

}
