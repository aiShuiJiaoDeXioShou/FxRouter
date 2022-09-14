package lh.view.page;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import lh.NoteBookApp;
import lh.router.annotation.FXRoute;
import lh.router.annotation.FxRouter;

@FXRoute("/page1")
public class Page1 extends HBox {

    @FxRouter
    private HBox routerView = new HBox();

    public Page1() {
        Button page = new Button("chr1");
        page.setOnMouseClicked(e-> NoteBookApp.router.toRoute("/page1/chr1"));
        routerView.setPrefWidth(600);
        routerView.setPrefHeight(800);
        routerView.getChildren().add(new Button("安德广厦将"));
        HBox.setMargin(routerView, new Insets(100));
        getChildren().addAll(page,routerView);
    }

}
