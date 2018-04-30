package imat.controls.history.article;

import imat.utils.FXMLLoader;
import imat.utils.ImageUtils;
import imat.views.history.OrderHistoryController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

public class ArticleHistoryItem extends AnchorPane {

    @FXML
    private ImageView productImageView;

    @FXML
    private Label productNameLabel;

    @FXML
    private Label ecoLabel;

    @FXML
    private Label countLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private VBox articleInfoVBox;

    private final ShoppingItem shoppingItem;

    private final OrderHistoryController orderHistoryController;

    public ArticleHistoryItem(ShoppingItem shoppingItem, OrderHistoryController orderHistoryController) {
        this.shoppingItem = shoppingItem;
        this.orderHistoryController = orderHistoryController;
        FXMLLoader.loadFXMLFromRootPackage("article_history_item.fxml", this, this);

        Product product = shoppingItem.getProduct();

        productImageView.setImage(ImageUtils.getSquareImage(new Image("/imat/resources/images/products/" + product.getImageName())));

        productNameLabel.setText(product.getName());

        if (!product.isEcological()) {
            articleInfoVBox.getChildren().remove(ecoLabel);
        }

        priceLabel.setText(String.valueOf(shoppingItem.getTotal()) + " kr");
        countLabel.setText(String.valueOf((int) shoppingItem.getAmount()) + " st");

    }

    @FXML
    private void addToCartButtonOnAction(Event event) {
        // TODO Add the right number of the relevant product to the current cart
    }

    public ShoppingItem getShoppingItem() {
        return shoppingItem;
    }

}