package imat.ui.controls.search;

import imat.model.FXMLController;
import imat.model.Model;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class SearchField extends FXMLController {

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchBox;

    private void makeSearch() {
        if (model == null) {
            System.out.println("No imat.model attached to search field!");
        } else {
            System.out.println("Search was successful!");
            model.search(searchBox.getText());
        }
    }

    @FXML
    private void onEnter(Event event) {
        makeSearch();
    }

    @FXML
    private void searchButtonOnAction(Event event) {
        makeSearch();
    }

    @FXML
    private void onSearchBoxUpdate(InputEvent event) {
        // System.out.println(searchBox.getText());
        // TODO Show suggestions depending on the input
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
