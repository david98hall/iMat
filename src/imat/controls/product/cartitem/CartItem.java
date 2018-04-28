package imat.controls.product.cartitem;

import imat.controllers.MainController;
import imat.controls.product.spinner.ProductCountSpinner;
import imat.interfaces.ChangeListener;
import imat.utils.FXMLLoader;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Product;

public class CartItem extends AnchorPane {

    @FXML
    private Label nameLabel;

    @FXML
    private AnchorPane productCountSpinner;

    @FXML
    private Label priceLabel;

    @FXML
    private ImageView removeButton;

    private final MainController mainController;

    private final Product product;

    public CartItem(Product product, MainController mainController) {
        this.product = product;
        this.mainController = mainController;
        FXMLLoader.loadFXMLFromRootPackage("cart_item.fxml", this, this);

        nameLabel.setText(this.product.getName());
        priceLabel.setText(String.valueOf(this.product.getPrice()));
    }

    @FXML
    private void removeButtonOnAction(Event event) {
        mainController.removeCartItem(this);
    }

//    public void addChangeListener(ChangeListener<Integer> listener) {
//        productCountSpinner.addChangeListener(listener);
//    }

}
