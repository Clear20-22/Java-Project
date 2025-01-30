package cafe.management.cafe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CardProduct{

    @FXML
    private AnchorPane Card_form;

    @FXML
    private Button Product_Add;

    @FXML
    private Label Product_Name;

    @FXML
    private Label Product_Price;

    @FXML
    private Spinner<Integer> Product_Spinner;

    @FXML
    private ImageView Product_image;

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    private ProductData productData;
    private Image img;
    private String pname;
    private SpinnerValueFactory<Integer> spin;
    private int qty;

    public void setData(ProductData prodData) {
        this.productData = prodData;
        pname = prodData.getProductName();
        String prc = String.valueOf(productData.getPrice()) + " TK";
        Product_Price.setText(prc);
        Product_Name.setText(productData.getProductName());
        img = new Image(productData.getImage(), 203, 112, false, true);
        Product_image.setImage(img);
        setQuantity(); // Initialize the spinner
    }

    private void setQuantity() {
        spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        Product_Spinner.setValueFactory(spin);
    }

    public static boolean click = false;

    @FXML
    void addBtn(ActionEvent event){
        qty = Product_Spinner.getValue();

        // Validate if at least 1 item is selected
        if (qty <= 0) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select at least 1 item!");
            return;
        }

        // Query to check product availability and stock
        String checkAvailableQuery = "SELECT Status, Stock, price, Type FROM productdetails WHERE ProductName = ?";

        // Query to update stock after purchase
        String updateStockQuery = "UPDATE productdetails SET Stock = Stock - ? WHERE ProductName = ?";

        // Query to insert customer order
        String insertData = "INSERT INTO customer (customer_id, prod_name, type, quantity, price, date, em_username) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            // Establish database connection
            connection = DatabaseUsers.getConnection();

            // Prepare statement to check product availability
            preparedStatement = connection.prepareStatement(checkAvailableQuery);
            preparedStatement.setString(1, Product_Name.getText());

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String status = resultSet.getString("Status");
                int availableQuantity = resultSet.getInt("Stock");
                double price = resultSet.getDouble("price"); // Get product price
                String type = resultSet.getString("Type");

                // Check if product is available
                if (!"Available".equalsIgnoreCase(status)) {
                    showAlert(Alert.AlertType.ERROR, "Product Unavailable", "Sorry, this product is currently unavailable.");
                    return;
                }

                // Check if enough stock is available
                if (availableQuantity < qty) {
                    showAlert(Alert.AlertType.ERROR, "Stock Insufficient", "Only " + availableQuantity + " items are available.");
                    return;
                }

                // Reduce stock after purchase
                PreparedStatement updateStmt = connection.prepareStatement(updateStockQuery);
                updateStmt.setInt(1, qty);
                updateStmt.setString(2, Product_Name.getText());

                int rowsUpdated = updateStmt.executeUpdate();
                type = "Unpaid";

                if (rowsUpdated > 0) {
                    // Insert customer order into the database
                    PreparedStatement insertStmt = connection.prepareStatement(insertData);
                    insertStmt.setString(1, ManagementControl.ID); // Generate a unique customer ID
                    insertStmt.setString(2, Product_Name.getText());
                    insertStmt.setString(3, type);
                    insertStmt.setInt(4, qty);
                    insertStmt.setDouble(5, price * qty);
                    insertStmt.setDate(6, java.sql.Date.valueOf(java.time.LocalDate.now()));
                    insertStmt.setString(7, "admin");

                    int rowsInserted = insertStmt.executeUpdate();
                    if (rowsInserted > 0) {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Product added to cart successfully!");
                        click = true;
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Could not add product to cart.");
                    }

                    insertStmt.close();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Could not update stock.");
                }

                updateStmt.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Product not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeResources() {
        try {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public void addBtn(javafx.event.ActionEvent actionEvent) {
//
//    }
}
