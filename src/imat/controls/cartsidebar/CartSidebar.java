package imat.controls.cartsidebar;

import imat.FXMLController;
import imat.controls.product.cartitem.CartItem;
import imat.enums.NavigationTarget;
import imat.interfaces.IShoppingListener;
import imat.utils.AnimationHandler;
import imat.utils.DelayedRunnable;
import imat.utils.FXMLLoader;
import imat.utils.MathUtils;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import se.chalmers.cse.dat216.project.Product;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class CartSidebar extends FXMLController implements IShoppingListener {

    @FXML
    private VBox cartItemVBox;

    @FXML
    private Label sumLabel;

    @FXML
    private Button toCheckoutButton;

    @FXML
    private Button trashButton;

    @FXML
    private AnchorPane regretPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button regretButton;

    private final Map<Product, Node> productsInSidebar = new HashMap<>();

    private double cartPrice;

    private boolean shouldTrash;
    private final long millisBeforeTrash = 3000;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        trashButton.setDisable(true);
        model.addShoppingListener(this);
        updateSumLabel();
        loadCart();
        disableCheckoutButtonIfPriceIsZero();
    }

    private void updateSumLabel() {
        sumLabel.setText(MathUtils.round(cartPrice, 2) + " kr");
    }

    private void loadCart() {
        model.getProductsInCart().forEach(this::addCartNode);
    }

    private void addCartNode(Product product) {
        if (productsInSidebar.containsKey(product)) return;
        CartItem cartItemController = new CartItem(product, () -> {
            removeCartNode(product);
        });
        cartItemController.setModel(model);
        Node cartItemNode = FXMLLoader.loadFXMLNodeFromRootPackage("../product/cartitem/cart_item.fxml", this, cartItemController);
        productsInSidebar.put(product, cartItemNode);
        cartItemVBox.getChildren().add(cartItemNode);
    }

    private void removeCartNode(Product product) {
        cartItemVBox.getChildren().remove(productsInSidebar.get(product));
        productsInSidebar.remove(product);
    }

    private void disableCheckoutButtonIfPriceIsZero() {
        toCheckoutButton.setDisable(cartPrice <= 0);
    }

    @FXML
    private void toCheckoutButtonOnAction(Event event) {
        model.navigate(NavigationTarget.PAY);
    }

    @FXML
    private void trashButtonOnAction(Event event) {
        startTrashProcess();
    }

    private void startTrashProcess() {

        shouldTrash = true;

        updateCartInfo(0);

        trashButton.setDisable(true);
        regretButton.setDisable(false);
        regretPane.setOpacity(1);
        switchView(regretPane);

        new DelayedRunnable(() -> {

            if (shouldTrash) {

                Timeline fadeAnimation = AnimationHandler.getAnimation(
                        v -> {

                            cartItemVBox.getChildren().clear();
                            productsInSidebar.keySet().forEach(product -> model.updateShoppingCart(product, 0));
                            productsInSidebar.clear();
                            shouldTrash = false;
                            switchView(scrollPane);
                            regretButton.setDisable(false);

                            Timeline fadeAnimation2 = AnimationHandler.getAnimation(
                                    AnimationHandler.getOpacityChangeKeyFrame(regretPane, 500, 0)
                            );
                            fadeAnimation2.play();
                        },
                        AnimationHandler.getOpacityChangeKeyFrame(regretPane, 250, 0.5)
                );
                fadeAnimation.play();

            }

        }).runLater(millisBeforeTrash);

    }

    @FXML
    private void regretButtonOnAction(Event event) {
        shouldTrash = false;
        trashButton.setDisable(false);
        updateCartInfo(model.getCartPrice());
        switchView(scrollPane);
    }

    private void updateCartInfo(double cartPrice) {
        this.cartPrice = cartPrice;
        updateSumLabel();
        disableCheckoutButtonIfPriceIsZero();
    }

    @Override
    public void onProductAdded(Product product, Double amount) {
        if (!shouldTrash) {
            trashButton.setDisable(false);
        }
        addCartNode(product);
    }

    @Override
    public void onProductRemoved(Product product, Double oldAmount) {

        if (model.getProductsInCart().size() == 0) {
            trashButton.setDisable(true);
        }

        updateCartInfo(model.getCartPrice());
    }

    @Override
    public void onProductUpdate(Product product, Double newAmount) {
        updateCartInfo(model.getCartPrice());
    }

    private void switchView(Node node) {
        node.toFront();
    }

}
