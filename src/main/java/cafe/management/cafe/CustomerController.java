package cafe.management.cafe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class CustomerController {

    @FXML
    private TextField CustomerConstID;

    @FXML
    private AnchorPane CustomerMainForm;

    @FXML
    private AnchorPane CustomerMenuForm;

    @FXML
    private Label CustomerProfileCat;

    @FXML
    private Label CustomerProfileDept;

    @FXML
    private TextField CustomerProfileEditDept;

    @FXML
    private TextField CustomerProfileEditName;

    @FXML
    private TextField CustomerProfileEditPhone;

    @FXML
    private Label CustomerProfileID;

    @FXML
    private Label CustomerProfileName;

    @FXML
    private AnchorPane CustomerProfilePage;

    @FXML
    private Label CustomerProfilePhone;

    @FXML
    private AnchorPane CustomerProfileUpdatePage;

    @FXML
    private TableColumn<?, ?> menu_col_price;

    @FXML
    private TableColumn<?, ?> menu_col_productName;

    @FXML
    private TableColumn<?, ?> menu_col_quantity;

    @FXML
    private GridPane menu_gridPane;

    @FXML
    private ScrollPane menu_scrollPane;

    @FXML
    private TableView<ProductData> menu_tableView;

    @FXML
    private AnchorPane AboutUs_Page;

    @FXML
    private AnchorPane reviewPage;

    private Connection connection = null;

    private PreparedStatement preparedStatement = null;

    private ResultSet resultSet = null;

    @FXML
    private GridPane CustomerMenu_grid;

    public CustomerController(){connection = DatabaseUsers.getConnection();}

    @FXML
    void CustomerAboutUs(ActionEvent event) {
        AboutUs_Page.setVisible(true);
        CustomerMenuForm.setVisible(false);
        CustomerProfilePage.setVisible(false);
        CustomerProfileUpdatePage.setVisible(false);
        reviewPage.setVisible(false);
    }
    
    private ObservableList<ProductData> cardProduct = FXCollections.observableArrayList();

    public ObservableList<ProductData> menuCard(){
        String sql = "SELECT * FROM productdetails";
        ObservableList<ProductData> temp = FXCollections.observableArrayList();
        try{
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                ProductData prod;
                if ("Available".equalsIgnoreCase(resultSet.getString("Status").trim())) {
                    prod = new ProductData(resultSet.getString("ProductID"),
                            resultSet.getString("ProductName"),
                            resultSet.getDouble("Price"),
                            resultSet.getString("Image"));
                    temp.add(prod);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseConnection();
        }
        return temp;
    }

    public void menuDisplyCard(){
        cardProduct.clear();
        cardProduct.addAll(menuCard());

        CustomerMenu_grid.getChildren().clear();

        int col=0;
        int row=0;
        CustomerMenu_grid.getRowConstraints().clear();
        CustomerMenu_grid.getColumnConstraints().clear();

//        menu_gridPane.setHgap(15);
//        menu_gridPane.setVgap(15);
        for(int i = 0;i < cardProduct.size();i++){
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("CardProduct.fxml"));
                AnchorPane pan = load.load();

                CardProduct controller = load.getController();
                controller.setData(cardProduct.get(i));

                if(col == 3){
                    col = 0;
                    row++;
                }
                CustomerMenu_grid.add(pan,col++,row);
                GridPane.setMargin(pan, new Insets(10, 10, 10, 10));

            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    @FXML
    void CustomerMenu(ActionEvent event) {
        AboutUs_Page.setVisible(false);
        CustomerMenuForm.setVisible(true);
        CustomerProfilePage.setVisible(false);
        CustomerProfileUpdatePage.setVisible(false);
        reviewPage.setVisible(false);
        menuDisplyCard();
        displayorder();
    }

    private String customerName = null;

    void showProfileInformation(){

        String regID = ManagementControl.ID;

        try {
            String sql = "SELECT FullName, RegistrationID, Category, PhoneNumber, Department FROM userdetails WHERE RegistrationID = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, regID);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("FullName");
                String registrationId = resultSet.getString("RegistrationID");
                String category = resultSet.getString("Category");
                String phone = resultSet.getString("PhoneNumber");
                String department = resultSet.getString("Department");

                customerName = name;

                CustomerProfileID.setText(registrationId);
                CustomerProfileName.setText(name);
                CustomerProfileCat.setText(category);
                if(department != null)
                    CustomerProfileDept.setText(department);
                if(phone != null)
                    CustomerProfilePhone.setText(phone);

            } else {
                System.out.println("No data found for regID: " + regID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void CustomerProfile(ActionEvent event) {
        AboutUs_Page.setVisible(false);
        CustomerMenuForm.setVisible(false);
        CustomerProfilePage.setVisible(true);
        CustomerProfileUpdatePage.setVisible(false);

        showProfileInformation();
    }

    void CustomerEditedInfo(){
        String name = CustomerProfileEditName.getText();
        String department = CustomerProfileEditDept.getText();
        String phone = CustomerProfileEditPhone.getText();

        if (name.isEmpty() || department.isEmpty() || phone.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("All fields are required. Please fill out all fields.");
            alert.showAndWait();
            return;
        }

        try {
            String updateQuery = "UPDATE userdetails SET FullName = ?, Department = ?, PhoneNumber = ? WHERE RegistrationID = ?";
            PreparedStatement pstmt = connection.prepareStatement(updateQuery);

            pstmt.setString(1, name);
            pstmt.setString(2, department);
            pstmt.setString(3, phone);
            pstmt.setString(4, CustomerConstID.getText());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Profile updated successfully!");
                alert.showAndWait();
                showProfileInformation();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("No matching record found. Update failed.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while updating the profile.");
            alert.showAndWait();
        } finally {
            CloseConnection();
        }
    }

    @FXML
    void CustomerProfileEditConfirm(ActionEvent event) {
        AboutUs_Page.setVisible(false);
        CustomerMenuForm.setVisible(false);
        CustomerProfilePage.setVisible(true);
        CustomerProfileUpdatePage.setVisible(false);

        CustomerEditedInfo();
    }



    @FXML
    void CustomerProfileUpdate(ActionEvent event) {
        AboutUs_Page.setVisible(false);
        CustomerMenuForm.setVisible(false);
        Alert alrt = new Alert(Alert.AlertType.CONFIRMATION);
        alrt.setTitle("Confirmation Message");
        alrt.setHeaderText(null);
        alrt.setContentText("Are you sure you want to update your information.");

        Optional<ButtonType> opt = alrt.showAndWait();

        if(opt.isPresent() && opt.get() == ButtonType.OK) {
            CustomerProfilePage.setVisible(false);
            CustomerProfileUpdatePage.setVisible(true);
        }

        CustomerConstID.setText(ManagementControl.ID);
        CustomerConstID.setDisable(true);

        if(customerName != null)
        CustomerProfileEditName.setText(customerName);

    }

    @FXML
    void CustomerSignOut(ActionEvent event) {
        Alert alrt = new Alert(Alert.AlertType.CONFIRMATION);
        alrt.setTitle("Confirmation Message");
        alrt.setHeaderText(null);
        alrt.setContentText("Are you sure you want to sign out.");

        Optional<ButtonType> opt = alrt.showAndWait();

        if(opt.isPresent() && opt.get() == ButtonType.OK) {
            try {
                CafeManagement.switchScene("LoginPage.fxml", 800);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Failed to load LoginPage.fxml");
            }
        } else {
            System.out.println("Stay loggedin.");
        }
    }

    @FXML
    void CutomerReview(ActionEvent event) {
        AboutUs_Page.setVisible(false);
        CustomerMenuForm.setVisible(false);
        CustomerProfilePage.setVisible(false);
        CustomerProfileUpdatePage.setVisible(false);
        reviewPage.setVisible(true);

        customercount();
        showCustomerData();
    }

    private double total_price = 0.0;

    public ObservableList<ProductData> menuDisplayOrder(){
        ObservableList<ProductData> listdata =  FXCollections.observableArrayList();
        String sql = "SELECT * FROM customer";

        try {
            // Establishing connection (Make sure your connection logic is correct)
            connection = DatabaseUsers.getConnection();  // assuming DatabaseConnection is a class managing DB connection

            // Prepare the SQL statement
            preparedStatement = connection.prepareStatement(sql);

            // Execute the query and retrieve the result set
            resultSet = preparedStatement.executeQuery();

            // Iterate through the result set and map the data to ProductData objects
            while (resultSet.next()) {
                if(resultSet.getString("type").equals("Paid"))
                    continue;
                String productId = "abc";
                String productName = resultSet.getString("prod_name");
                double price = resultSet.getDouble("Price");
                String image = "abc";
                int quantity = resultSet.getInt("quantity");

                total_price += price;

                // Creating the ProductData object with the constructor
                ProductData product = new ProductData(productId, productName, price, image, quantity);
                listdata.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Handle exceptions appropriately
        } finally {
            // Close resources to avoid memory leaks
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return listdata;
    }

    private  ObservableList<ProductData> menulist;

    @FXML
    private TextField menu_amount;

    @FXML
    private Label menu_change;

    @FXML
    private Label menu_total;

    public void displayorder(){
        menulist = menuDisplayOrder();

        menu_col_price.setCellValueFactory(new PropertyValueFactory<>("Price"));
        menu_col_productName.setCellValueFactory(new PropertyValueFactory<>("ProductName"));
        menu_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        menu_tableView.setItems(menulist);


        String tk = String.valueOf(total_price) + "TK";
        menu_total.setText(tk);
    }


    @FXML
    void payBtn(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Payment Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to proceed with the payment?");

        // Add OK and Cancel buttons
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Wait for user response
        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

        if (result == ButtonType.CANCEL) {
            return; // Cancel payment process if user chooses "Cancel"
        }
        menu_amount.setText("");

        String amountText = menu_amount.getText();
        double given_price = 0.0;

        if (!amountText.isEmpty()) {
            try {
                given_price = Double.parseDouble(amountText);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + e.getMessage());
                return;
            }
        }

        double change = given_price - total_price;
        String cng = String.valueOf(change);

        menu_change.setText("$0.0 TK");
        menu_total.setText("$0.0 TK");
        total_price = 0.0;
        String updateSql = "UPDATE customer SET type = 'Paid' WHERE type = 'Unpaid'";

        try {
            preparedStatement = connection.prepareStatement(updateSql);

            int rowsUpdated = preparedStatement.executeUpdate();
            System.out.println("Updated " + rowsUpdated + " rows to 'Paid'");

        } catch (SQLException e) {
            System.out.println("Error updating 'Paid' status: " + e.getMessage());
        } finally {
            CloseConnection();
            displayorder();
        }

    }

    @FXML
    void Amount(ActionEvent event) {

        String amountText = menu_amount.getText();
        double given_price = 0.0;

        if (!amountText.isEmpty()) {
            try {
                given_price = Double.parseDouble(amountText);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + e.getMessage());
                return;
            }
        }

        if(given_price < total_price){
            Alert insufficientFundsAlert = new Alert(Alert.AlertType.WARNING);
            insufficientFundsAlert.setTitle("Insufficient Funds");
            insufficientFundsAlert.setHeaderText(null);
            insufficientFundsAlert.setContentText("The amount given is less than the total price.\nPlease enter a sufficient amount.");
            insufficientFundsAlert.showAndWait();
            return;
        }
        double change = given_price - total_price;
        String cng = String.valueOf(change) + "TK";

        menu_change.setText(cng);
    }


    private void CloseConnection(){
        try {
            if(resultSet != null) resultSet.close();
            if(preparedStatement != null) preparedStatement.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }

    }

    // In your controller class
    @FXML
    private TableColumn<CustomerOrderHistory, String> cus_col_prod_id;
    @FXML
    private TableColumn<CustomerOrderHistory, String> cus_col_prod_name;
    @FXML
    private TableColumn<CustomerOrderHistory, Integer> cus_col_quantity;
    @FXML
    private TableColumn<CustomerOrderHistory, String> cus_col_date;
    @FXML
    private TableColumn<CustomerOrderHistory, Double> cus_col_price;

    @FXML
    private TableView<CustomerOrderHistory> customer_tableview;

    public ArrayList<CustomerOrderHistory> getCustomerPurchases() {
        ArrayList<CustomerOrderHistory> purchases = new ArrayList<>();
        String customerQuery = "SELECT prod_name, quantity, price, date " +
                "FROM customer " +
                "WHERE customer_id = ?";

        try {
            // First get customer name from userdetails
            String customerName = getCustomerName(ManagementControl.ID);

            // Then get purchase history
            try (PreparedStatement customerStmt = connection.prepareStatement(customerQuery)) {
                customerStmt.setString(1, ManagementControl.ID);

                try (ResultSet customerRs = customerStmt.executeQuery()) {
                    while (customerRs.next()) {
                        String prodName = customerRs.getString("prod_name");
                        int quantity = customerRs.getInt("quantity");
                        double price = customerRs.getDouble("price");
                        String date = customerRs.getString("date");
                        double totalPrice = price * quantity;

                        // Get product ID from productdetails
                        String productId = getProductId(prodName);

                        purchases.add(new CustomerOrderHistory(
                                productId,
                                prodName,
                                ManagementControl.ID,  // customer_id
                                customerName,
                                date,
                                quantity,
                                totalPrice
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching purchases: " + e.getMessage());
        } finally {
            CloseConnection();
        }
        return purchases;
    }

    private String getCustomerName(String customerId) throws SQLException {
        String nameQuery = "SELECT FullName FROM userdetails WHERE RegistrationID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(nameQuery)) {
            stmt.setString(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getString("FullName") : "Unknown Customer";
            }
        }
    }

    private String getProductId(String productName) throws SQLException {
        String productQuery = "SELECT productid FROM productdetails WHERE productname = ?";
        try (PreparedStatement stmt = connection.prepareStatement(productQuery)) {
            stmt.setString(1, productName);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getString("productid") : "N/A";
            }
        }
    }

    private ArrayList<CustomerOrderHistory> alldata;

    public void customercount() {
        alldata = getCustomerPurchases();
    }

    private void showCustomerData() {
        // Set cell value factories
        cus_col_prod_id.setCellValueFactory(new PropertyValueFactory<>("productId"));
        cus_col_prod_name.setCellValueFactory(new PropertyValueFactory<>("productName"));
        cus_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        cus_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        cus_col_price.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        // Create observable list
        ObservableList<CustomerOrderHistory> data = FXCollections.observableArrayList(alldata);
        customer_tableview.setItems(data);

        // Apply alignment
        centerAlignColumn(cus_col_prod_id);
        centerAlignColumn(cus_col_prod_name);
        centerAlignColumn(cus_col_quantity);
        centerAlignColumn(cus_col_date);
        centerAlignColumn(cus_col_price);
    }

    // Updated center alignment method with proper generics
    private <T> void centerAlignColumn(TableColumn<CustomerOrderHistory, T> column) {
        column.setCellFactory(col -> new TableCell<CustomerOrderHistory, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                    setAlignment(Pos.CENTER);
                }
            }
        });
    }


    @FXML
    private TextField rating_bos;

    @FXML
    private TextField id_box;

    @FXML
    void rating_confirm(ActionEvent event) {
        String ratingText = rating_bos.getText();
        String idText = id_box.getText();


        if (ratingText.isEmpty() || idText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in all fields.");
            return;
        }

        try {

            float rating = Float.parseFloat(ratingText);


            if (rating < 1 || rating > 5) {
                showAlert(Alert.AlertType.ERROR, "Invalid Rating", "Please enter a rating between 1 and 5.");
                return;
            }


            String query = "INSERT INTO rating (id, rating) VALUES (?, ?)";

            try (Connection conn = DatabaseUsers.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, idText);
                stmt.setFloat(2, rating);


                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Rating successfully inserted!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to insert the rating.");
                }

            } catch (SQLException e) {

                showAlert(Alert.AlertType.ERROR, "Database Error", "Error inserting rating into database: " + e.getMessage());
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Rating", "Please enter a valid numerical rating.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
