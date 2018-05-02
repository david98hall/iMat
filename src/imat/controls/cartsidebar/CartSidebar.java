package imat.controls.cartsidebar;

import imat.controls.product.cartitem.CartItem;
import imat.interfaces.RemoveRequestListener;
import imat.interfaces.ShoppingListener;
import imat.utils.FXMLLoader;
import imat.utils.IMatUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.util.ArrayList;
import java.util.List;

public class CartSidebar extends AnchorPane implements ShoppingListener, RemoveRequestListener<CartItem> {

    @FXML
    private VBox cartItemVBox;

    @FXML
    private Label sumLabel;

    @FXML
    private Button toCheckoutButton;

    @FXML
    private Button trashButton;

    private double cartPrice;

    private final List<CartItem> cartItems;

    private boolean isSavingCartAtShutdown;

    private boolean hasLoadedCartItems;

    public CartSidebar() {
        super();
        FXMLLoader.loadFXMLFromRootPackage("cart_sidebar.fxml", this, this);
        cartItems = new ArrayList<>();
        updateSumLabel();
        loadCart();
    }

    public void setCartPrice(double cartPrice) {
        this.cartPrice = cartPrice;
        updateSumLabel();
    }

    public void setSavingCartAtShutdown(boolean savingCartAtShutdown) {
        this.isSavingCartAtShutdown = savingCartAtShutdown;
    }

    private void changeCartPrice(double change) {
        setCartPrice(cartPrice + change);
    }

    private void updateSumLabel() {
        sumLabel.setText(cartPrice + " kr");
    }

    private void loadCart() {
        if (!hasLoadedCartItems) {
            for (ShoppingItem shoppingItem : IMatDataHandler.getInstance().getShoppingCart().getItems()) {
                onAddShoppingItem(shoppingItem);
            }
            hasLoadedCartItems = true;
        }
    }

    @Override
    public void onAddShoppingItem(ShoppingItem shoppingItem) {
        CartItem cartItem = new CartItem(IMatUtils.cloneShoppingItem(shoppingItem));
        cartItem.addRemoveRequestListener(this);
        cartItem.addPriceChangeListener(this::onCartItemPriceChanged);
        cartItemVBox.getChildren().add(cartItem);
        cartItems.add(cartItem);
        changeCartPrice(shoppingItem.getTotal());
        if (isSavingCartAtShutdown && hasLoadedCartItems)
            updateShoppingCart(); // Should be used if the cart SHOULD be saved at shutdown
    }

    @Override
    public void onRemoveRequest(CartItem cartItem) {
        cartItemVBox.getChildren().remove(cartItem);
        cartItems.remove(cartItem);
        changeCartPrice(-cartItem.getShoppingItem().getTotal());
        if (isSavingCartAtShutdown)
            updateShoppingCart(); // Should be used if the cart SHOULD be saved at shutdown
    }

    private void clearCart() {
        cartItemVBox.getChildren().clear();
        cartItems.clear();
        setCartPrice(0);
        IMatDataHandler.getInstance().getShoppingCart().clear();
    }

    @FXML
    private void toCheckoutButtonOnAction(Event event) {
        if (!isSavingCartAtShutdown)
            updateShoppingCart(); // Should be used if the cart should NOT be saved at shutdown
        // TODO Go to the check out view.
        System.out.println("To checkout...");
    }

    @FXML
    private void trashButtonOnAction(Event event) {
        clearCart();
    }

    private void updateShoppingCart() {
        IMatDataHandler.getInstance().getShoppingCart().clear();
        for (CartItem cartItem : cartItems) {
            IMatDataHandler.getInstance().getShoppingCart().addItem(cartItem.getShoppingItem());
        }
    }

    private void onCartItemPriceChanged(double oldPrice, double newPrice) {
        changeCartPrice(newPrice - oldPrice);
        if (isSavingCartAtShutdown)
            updateShoppingCart(); // Should be used if the cart SHOULD be saved at shutdown
    }

}
