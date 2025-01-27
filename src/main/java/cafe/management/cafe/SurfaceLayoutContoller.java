package cafe.management.cafe;

import com.almasb.fxgl.procedural.BiomeMapGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javafx.collections.FXCollections.observableList;

public class SurfaceLayoutContoller {

    @FXML
    private AnchorPane DashboardMainpage;

    @FXML
    private AnchorPane DashboardScroll;

    @FXML
    private AnchorPane DashboardChart;


    @FXML
    private Button cutomer_btn;

    @FXML
    private Button dashboard_btn;

    @FXML
    private Button inventory_addBtn;

    @FXML
    private Button inventory_btn;

    @FXML
    private Button inventory_clearBtn;

    @FXML
    private TableColumn<?, ?> inventory_col_date;

    @FXML
    private TableColumn<?, ?> inventory_col_price;

    @FXML
    private TableColumn<?, ?> inventory_col_productID;

    @FXML
    private TableColumn<?, ?> inventory_col_productName;

    @FXML
    private TableColumn<?, ?> inventory_col_status;

    @FXML
    private TableColumn<?, ?> inventory_col_stock;

    @FXML
    private TableColumn<?, ?> inventory_col_type;

    @FXML
    private Button inventory_deleteBtn;

    @FXML
    private AnchorPane inventory_form;

    @FXML
    private ImageView inventory_imageView;

    @FXML
    private Button inventory_importBtn;

    @FXML
    private TextField inventory_price;

    @FXML
    private TextField inventory_productID;

    @FXML
    private TextField inventory_productName;

    @FXML
    private ComboBox<?> inventory_status;

    @FXML
    private TextField inventory_stock;

    @FXML
    private TableView<ProductData> inventory_tableView;

    @FXML
    private Button inventory_updateBtn;

    @FXML
    private Button logout_btn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private TextField menu_amount;

    @FXML
    private Button menu_btn;

    @FXML
    private Label menu_change;

    @FXML
    private TableColumn<?, ?> menu_col_price;

    @FXML
    private TableColumn<?, ?> menu_col_productName;

    @FXML
    private TableColumn<?, ?> menu_col_quantity;

    @FXML
    private AnchorPane menu_form;

    @FXML
    private GridPane menu_gridPane;

    @FXML
    private Button menu_payBtn;

    @FXML
    private Button menu_receiptBtn;

    @FXML
    private Button menu_removeBtn;

    @FXML
    private ScrollPane menu_scrollPane;

    @FXML
    private TableView<?> menu_tableView;

    @FXML
    private Label menu_total;

    @FXML
    private Label username;

    private Image img;


    @FXML
    private ComboBox<?> Inventory_choiceType;

    @FXML
    private AnchorPane ProfilePage;


    @FXML
    private AnchorPane ConfirmUpdate_page;


    @FXML
    private ComboBox<?> inventory_choiceStatus;

    private String[] type_list = {"Meals","Drinks"};

    private String[] status_list = {"Available","Unavailable"};

    private Connection connection = null;

    private PreparedStatement preparedStatement = null;

    private ResultSet resultSet = null;

    public SurfaceLayoutContoller() {
        connection = DatabaseUsers.getConnection();
    }

    @FXML
    void LeavePage(ActionEvent event) {
        try {
            CafeManagement.switchScene("LoginPage.fxml",800);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load LoginPage.fxml");
        }
    }

    @FXML
    void Customer(ActionEvent event) {
        DashboardMainpage.setVisible(false);
        inventory_form.setVisible(false);
        menu_form.setVisible(false);
        ProfilePage.setVisible(false);
        ConfirmUpdate_page.setVisible(false);
    }

    @FXML
    void Dashboard(ActionEvent event) {
        DashboardMainpage.setVisible(true);
        inventory_form.setVisible(false);
        menu_form.setVisible(false);
        ProfilePage.setVisible(false);
        ConfirmUpdate_page.setVisible(false);
    }

    void inventory_Type_Status() {
        List<String> list = new ArrayList<>();

        for(String s : type_list){
            list.add(s);
        }
        ObservableList observableList = FXCollections.observableArrayList(list);
        Inventory_choiceType.setItems(observableList);

        List<String> statusList = new ArrayList<>();

        for(String s:status_list){
            statusList.add(s);
        }

        ObservableList sList = FXCollections.observableArrayList(statusList);

        inventory_choiceStatus.setItems(sList);
    }

    void ClearFields() {
        inventory_imageView.setImage(null);
        img = null;
        inventory_productName.requestFocus();
        inventory_productID.requestFocus();
        inventory_stock.requestFocus();
        inventory_price.requestFocus();
        inventory_choiceStatus.requestFocus();
        Inventory_choiceType.requestFocus();
        inventory_productName.setText("");
        inventory_productID.setText("");
        inventory_stock.setText("");
        inventory_price.setText("");
        inventory_choiceStatus.setValue(null);
        Inventory_choiceType.setValue(null);
    }

    ObservableList<ProductData> Inventory_ProductDatalist(){
        ObservableList<ProductData> list = FXCollections.observableArrayList();

        String query = "SELECT * FROM ProductDetails";

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            ProductData data = null;

            while(resultSet.next()){
                data = new ProductData(resultSet.getString("ProductID"),
                        resultSet.getString("ProductName"),
                        resultSet.getString("Type"),
                        resultSet.getInt("Stock"),
                        resultSet.getDouble("Price"),
                        resultSet.getString("Status"),
                        resultSet.getDate("DateAdded"),
                        resultSet.getString("Image")
                );
                list.add(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseConnection();
            ClearFields();
        }
        return list;
    }

    ObservableList<ProductData> InventoryListData;

    void InventoryShowData() {
        InventoryListData = Inventory_ProductDatalist();

        inventory_col_productID.setCellValueFactory(new PropertyValueFactory<>("ProductID"));
        inventory_col_productName.setCellValueFactory(new PropertyValueFactory<>("ProductName"));
        inventory_col_type.setCellValueFactory(new PropertyValueFactory<>("Type"));
        inventory_col_stock.setCellValueFactory(new PropertyValueFactory<>("Stock"));
        inventory_col_price.setCellValueFactory(new PropertyValueFactory<>("Price"));
        inventory_col_status.setCellValueFactory(new PropertyValueFactory<>("Status"));
        inventory_col_date.setCellValueFactory(new PropertyValueFactory<>("DateAdded"));
        inventory_tableView.setItems(InventoryListData);
    }

    @FXML
    void InventoryAddBtn(ActionEvent event) {
        if(inventory_productName.getText().isEmpty() ||
           inventory_productID.getText().isEmpty() ||
           inventory_stock.getText().isEmpty() ||
           inventory_price.getText().isEmpty() ||
           inventory_choiceStatus.getSelectionModel().getSelectedItem() == null ||
                Inventory_choiceType.getSelectionModel().getSelectedItem() == null||
            inventory_imageView.getImage() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the fields.");
            alert.showAndWait();
        } else {
            String CheckProductID = "SELECT ProductID FROM ProductDetails WHERE ProductID = '" + inventory_productID.getText() +"'";
            try {
                Statement Statement = connection.createStatement();
                ResultSet rs = Statement.executeQuery(CheckProductID);
                if(rs.next()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Product ID already exists.");
                    alert.showAndWait();
                } else {
                    String query = "INSERT INTO ProductDetails (ProductID, ProductName, Type, Stock, Price, Status, DateAdded, Image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, inventory_productID.getText());
                    preparedStatement.setString(2, inventory_productName.getText());
                    preparedStatement.setString(3, Inventory_choiceType.getSelectionModel().getSelectedItem().toString());
                    preparedStatement.setInt(4, Integer.parseInt(inventory_stock.getText()));
                    preparedStatement.setDouble(5, Double.parseDouble(inventory_price.getText()));
                    preparedStatement.setString(6, inventory_choiceStatus.getSelectionModel().getSelectedItem().toString());
                    preparedStatement.setDate(7, new Date(System.currentTimeMillis()));
                    preparedStatement.setString(8, img.getUrl());
                    int rowsInserted = preparedStatement.executeUpdate();
                    if(rowsInserted > 0){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Product added successfully.");
                        alert.showAndWait();

                        inventory_productName.requestFocus();
                        inventory_productID.requestFocus();
                        inventory_stock.requestFocus();
                        inventory_price.requestFocus();
                        inventory_choiceStatus.requestFocus();
                        Inventory_choiceType.requestFocus();
                        inventory_productName.setText("");
                        inventory_productID.setText("");
                        inventory_stock.setText("");

                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Failed to add product.");
                    }
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            finally {
                CloseConnection();
                InventoryShowData();
            }
        }
    }

    @FXML
    void InventoryClearBtn(ActionEvent event) {
        ClearFields();
        InventoryShowData();
    }

    @FXML
    void InventoryDeleteBtn(ActionEvent event) {
        if (inventory_productID.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill the Product ID field.");
            alert.showAndWait();
        } else {
            String CheckProductID = "SELECT ProductID FROM ProductDetails WHERE ProductID = '" + inventory_productID.getText() + "'";

            try {
                connection = DatabaseUsers.getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(CheckProductID);

                if (!rs.next()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Product ID does not exist.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure you want to delete this Product ID: " + inventory_productID.getText() + "?");

                    Optional<ButtonType> opt = alert.showAndWait();

                    if (opt.isPresent() && opt.get() == ButtonType.OK) {
                        String query = "DELETE FROM ProductDetails WHERE ProductID = ?";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, inventory_productID.getText());

                        int rowsDeleted = preparedStatement.executeUpdate();

                        if (rowsDeleted > 0) {
                            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                            infoAlert.setTitle("Information Message");
                            infoAlert.setHeaderText(null);
                            infoAlert.setContentText("Product deleted successfully.");
                            infoAlert.showAndWait();
                        } else {
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                            errorAlert.setTitle("Error Message");
                            errorAlert.setHeaderText(null);
                            errorAlert.setContentText("Failed to delete product.");
                            errorAlert.showAndWait();
                        }
                    }
                }

                InventoryShowData();

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                CloseConnection();
                ClearFields();
            }
        }

    }

    @FXML
    <optional>
    void InventoryUpdateBtn(ActionEvent event) {
        if (inventory_productID.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill the Product ID field.");
            alert.showAndWait();
        } else {
            String CheckProductID = "SELECT ProductID FROM ProductDetails WHERE ProductID = '" + inventory_productID.getText() + "'";

            try {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(CheckProductID);

                if (!rs.next()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Product ID does not exist.");
                    alert.showAndWait();
                } else {

                    String pdType = null;
                    int pdStock = 0;
                    double pdPrice = 0.0;
                    String pdStatus = null;
                    String pdImage = null;
                    String find = "SELECT ProductName, Type, Stock, Price, Status, Image FROM ProductDetails WHERE ProductID = ?";
                    String pdName = null;
                    try {
                        preparedStatement = connection.prepareStatement(find);
                        preparedStatement.setString(1, inventory_productID.getText());

                        ResultSet result = preparedStatement.executeQuery();

                        if (result.next()) {
                            pdName = result.getString("ProductName");
                            pdType = result.getString("Type");
                            pdStock = result.getInt("Stock");
                            pdPrice = result.getDouble("Price");
                            pdStatus = result.getString("Status");
                            pdImage = result.getString("Image");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    pdName = (inventory_productName.getText().isEmpty()) ? pdName : inventory_productName.getText();
                    pdType = (Inventory_choiceType.getSelectionModel().getSelectedItem() == null) ? pdType : Inventory_choiceType.getSelectionModel().getSelectedItem().toString();
                    pdStock = (inventory_stock.getText().isEmpty()) ? pdStock : Integer.parseInt(inventory_stock.getText());
                    pdPrice = (inventory_price.getText().isEmpty()) ? pdPrice : Double.parseDouble(inventory_price.getText());
                    pdStatus = (inventory_choiceStatus.getSelectionModel().getSelectedItem() == null) ? pdStatus : inventory_choiceStatus.getSelectionModel().getSelectedItem().toString();
                    pdImage = (img == null) ? pdImage : img.getUrl();

                    String query = "UPDATE ProductDetails SET ProductName = ?, Type = ?, Stock = ?, Price = ?, Status = ?, DateAdded = ?, Image = ? WHERE ProductID = ?";

                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, pdName);
                    preparedStatement.setString(2, pdType);
                    preparedStatement.setInt(3, pdStock);
                    preparedStatement.setDouble(4, pdPrice);
                    preparedStatement.setString(5, pdStatus);
                    preparedStatement.setDate(6, new Date(System.currentTimeMillis()));
                    preparedStatement.setString(7, pdImage);
                    preparedStatement.setString(8, inventory_productID.getText());

                    Alert alrt = new Alert(Alert.AlertType.CONFIRMATION);
                    alrt.setTitle("Confirmation Message");
                    alrt.setHeaderText(null);
                    alrt.setContentText("Are you sure you want to update this Product ID: " + inventory_productID.getText() + "?");

                    Optional<ButtonType> opt = alrt.showAndWait();

                    if (opt.isPresent() && opt.get() == ButtonType.OK) {

                        int rowsUpdated = preparedStatement.executeUpdate();

                        if (rowsUpdated > 0) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Information Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Product updated successfully.");
                            alert.showAndWait();

                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Failed to update product.");
                            alert.showAndWait();
                        }
                    }

                    InventoryShowData();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                CloseConnection();
                ClearFields();
            }
        }
    }

    @FXML
    void InventoryImortBtn(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Open Image File", "*.png", "*.jpg")
        );

        File file = fileChooser.showOpenDialog(DashboardMainpage.getScene().getWindow());

        if(file != null){
            img = new Image(file.toURI().toString(),168,144,false,true);
            inventory_imageView.setImage(img);
        }
    }

    @FXML
    void Inventory(ActionEvent event) {
        DashboardMainpage.setVisible(false);
        inventory_form.setVisible(true);
        ProfilePage.setVisible(false);
        menu_form.setVisible(false);
        ConfirmUpdate_page.setVisible(false);
        inventory_Type_Status();
        InventoryShowData();
    }
    @FXML
    void Menu(ActionEvent event) {
        DashboardMainpage.setVisible(false);
        inventory_form.setVisible(false);
        ProfilePage.setVisible(false);
        menu_form.setVisible(true);
        ConfirmUpdate_page.setVisible(false);
    }

    @FXML
    private Label ProfileCat;

    @FXML
    private Label ProfileDept;

    @FXML
    private Label ProfileID;

    @FXML
    private Label ProfileName;

    private String registration_ID;

    private String fullName;

    @FXML
    private Label updatePhone;

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

                fullName = name;
                registration_ID = registrationId;

                ProfileID.setText(registrationId);
                ProfileName.setText(name);
                ProfileCat.setText(category);
                if(department != null)
                ProfileDept.setText(department);
                if(phone != null)
                updatePhone.setText(phone);

            } else {
                System.out.println("No data found for regID: " + regID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Profile(ActionEvent event) {
        DashboardMainpage.setVisible(false);
        inventory_form.setVisible(false);
        ProfilePage.setVisible(true);
        menu_form.setVisible(false);
        ConfirmUpdate_page.setVisible(false);

        showProfileInformation();
    }

    @FXML
    private TextField const_ID;

    @FXML
    private TextField updateName;

    @FXML
    private TextField update_Department;

    @FXML
    private TextField update_phone;




    @FXML
    void ProfileUpdate(ActionEvent event) {
        Alert alrt = new Alert(Alert.AlertType.CONFIRMATION);
        alrt.setTitle("Confirmation Message");
        alrt.setHeaderText(null);
        alrt.setContentText("Are you sure you want to update your information.");

        Optional<ButtonType> opt = alrt.showAndWait();

        if(opt.isPresent() && opt.get() == ButtonType.OK){
            ProfilePage.setVisible(false);
            ConfirmUpdate_page.setVisible(true);
            const_ID.setText(registration_ID);
            const_ID.setDisable(true);
            updateName.setText(fullName);
            updateName.requestFocus();
        }

    }



    @FXML
    void ConfirmUpdate(ActionEvent event) {
        Alert alrt = new Alert(Alert.AlertType.CONFIRMATION);
        alrt.setTitle("Confirmation Message");
        alrt.setHeaderText(null);
        alrt.setContentText("Are you sure you want to update your information.");

        Optional<ButtonType> opt = alrt.showAndWait();

        if(opt.isPresent() && opt.get() == ButtonType.OK){
            ProfilePage.setVisible(true);
            ConfirmUpdate_page.setVisible(false);
            confirm_update();
        }
    }



    void confirm_update(){
        String name = updateName.getText();
        String department = update_Department.getText();
        String phone = update_phone.getText();

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
            pstmt.setString(4, const_ID.getText());

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


   private void CloseConnection(){
        try {
            if(resultSet != null) resultSet.close();
            if(preparedStatement != null) preparedStatement.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }

   }
}

