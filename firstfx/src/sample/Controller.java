package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;

public class Controller {
    public Label hello;
    public void functionsayhello(ActionEvent actionEvent) {
        hello.setText("hello,world!");
    }
}
