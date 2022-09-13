package lh.view.home;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

public class HomeView extends Scene {
    public HomeView(Parent root) {
        super(root);
    }

   public static HomeView create() {
        VBox vBox = new VBox();
        HomeView homeView = new HomeView(vBox);
        return homeView;
    }


}
