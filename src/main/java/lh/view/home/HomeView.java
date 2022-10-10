package lh.view.home;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lh.NoteBookApp;
import lh.router.annotation.FXRoute;
import lh.router.annotation.FxRouter;


@FXRoute("/")
public class HomeView extends HBox {

    @FxRouter
    private VBox root = new VBox();

    private VBox sidebar = new VBox();

    /**
     * 主页面主构造函数
     */
    public HomeView() {

        this.getChildren().addAll(Sidebar(sidebar), root);
        HBox.setMargin(root, new Insets(0, 0, 200, 0));
        root.setPrefSize(800, 500);
        root.setStyle("""
                    -fx-background-color: #74c0fc;
                    -fx-border-width: 7px;
                    -fx-border-color: #0000;
                    -fx-border-radius: 5px;
                """);

    }


    /**
     * 自定义的button侧边栏
     */
    public VBox Sidebar(VBox sidebar) {
        Button button = new Button("/login");
        Button button1 = new Button("/page1");
        Button button2 = new Button("/page2/ui1");
        Button button3 = new Button("/page");
        Button button4 = new Button("/re");
        Button button5 = new Button("/pre");
        button.setOnMouseClicked(event -> {
            NoteBookApp.router.to("/login");
        });
        button1.setOnMouseClicked(event -> {
            NoteBookApp.router
                    .sendPackage("你好世界")
                    .to("/page1?index=1&nis=yt");
        });
        button2.setOnMouseClicked(event -> {
            NoteBookApp.router.to("login");
        });
        button3.setOnMouseClicked(event -> {
            NoteBookApp.router.to("/page");
        });
        button4.setOnMouseClicked(event -> {
            NoteBookApp.router.to("/re");
        });
        button5.setOnMouseClicked(event -> {
            NoteBookApp.router.pre();
        });
        sidebar.getChildren()
                .addAll(button, button1, button2, button3, button4, button5);
        return sidebar;
    }

}
