package imat.ui.views.browse.centerviews.products;

import imat.interfaces.ICategoryListener;
import imat.interfaces.ISearchListener;
import imat.model.FXMLController;
import imat.model.category.Category;
import imat.ui.controls.product.menu.ProductMenuItem;
import imat.utils.FXMLLoader;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.Product;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Products extends FXMLController implements ICategoryListener, ISearchListener {

    @FXML
    private Label categoryLabel;

    @FXML
    private FlowPane productsFlowPane;

    @FXML
    private CheckBox onlyEcoCheckBox;

    private Category currentCategory;

    private boolean onlyEcologicalProducts;

    private List<Product> currentProducts;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model.addCategoryListener(this);
        model.addSearchListener(this);
    }

    @Override
    public void onCategorySelected(Category category) {
        if (category == currentCategory) return;
        currentCategory = category;
        productsFlowPane.getChildren().removeIf(x -> true);
        categoryLabel.setText(category.getName());
        populateWithProducts(category.getAllProducts(), onlyEcologicalProducts);
    }

    @Override
    public void onSearch(String searchTerm, List<Product> products) {
        String categoryText;
        if (products.size() == 0) {
            categoryText = "Din sökning på \"" + searchTerm + "\" gav inga träffar";
        } else {
            categoryText = "Sökresultat för: \"" + searchTerm + "\"";
        }

        categoryLabel.setText(categoryText);
        productsFlowPane.getChildren().removeIf(x -> true);
        populateWithProducts(products, onlyEcologicalProducts);
    }

    private void populateWithProducts(List<Product> products, boolean onlyEcologicalProducts) {
        currentProducts = products;
        for (Product product : products) {
            if (!onlyEcologicalProducts || product.isEcological()) {
                ProductMenuItem controller = new ProductMenuItem(product);
                controller.setModel(model);
                String fxmlPath = "../../../../controls/product/menu/product_menu_item.fxml";
                Node item = FXMLLoader.loadFXMLNodeFromRootPackage(fxmlPath, this, controller);
                productsFlowPane.getChildren().add(item);
            }
        }
    }

    @FXML
    private void checkBoxOnAction(Event event) {
        onlyEcologicalProducts = onlyEcoCheckBox.isSelected();
        productsFlowPane.getChildren().removeIf(x -> true);
        populateWithProducts(currentProducts, onlyEcologicalProducts);
    }

}
