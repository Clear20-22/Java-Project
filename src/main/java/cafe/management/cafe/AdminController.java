package cafe.management.cafe;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.*;
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
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static javafx.collections.FXCollections.observableList;

public class AdminController {

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
    private TableView<ProductData> menu_tableView;

    @FXML
    private Label menu_total;

    @FXML
    private Label username;

    private Image img;

    @FXML
    private AnchorPane EmployeePage;


    @FXML
    private ComboBox<?> Inventory_choiceType;

    @FXML
    private AnchorPane ProfilePage;


    @FXML
    private AnchorPane ConfirmUpdate_page;


    @FXML
    private ComboBox<?> inventory_choiceStatus;

    @FXML
    private Label Today_Income;

    @FXML
    private Label Total_Sold;

    @FXML
    private Label Total_customer;

    @FXML
    private Label Total_income;

    private String[] type_list = {"Meals","Drinks"};

    private String[] status_list = {"Available","Unavailable"};

    private Connection connection = null;

    private PreparedStatement preparedStatement = null;

    private ResultSet resultSet = null;

    @FXML
    private AnchorPane customer_show;

    public AdminController() {
        connection = DatabaseUsers.getConnection();
    }

    @FXML
    void LeavePage(ActionEvent event) {
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
    void Customer(ActionEvent event) {
        DashboardMainpage.setVisible(false);
        inventory_form.setVisible(false);
        menu_form.setVisible(false);
        ProfilePage.setVisible(false);
        ConfirmUpdate_page.setVisible(false);
        EmployeePage.setVisible(false);
        customer_show.setVisible(true);

        customercount();
        showCustomerData();
    }

    private HashMap<String, Double> track = new HashMap<>();
    private double tot = 0.0;
    private int totsold = 0;
    private int totcustomer = 0;

    @FXML
    void Dashboard(ActionEvent event) {
        DashboardMainpage.setVisible(true);
        inventory_form.setVisible(false);
        menu_form.setVisible(false);
        ProfilePage.setVisible(false);
        ConfirmUpdate_page.setVisible(false);
        EmployeePage.setVisible(false);
        customer_show.setVisible(false);

        loadCustomerChartData();
        initializeCustomerChart();
        initializeIncomeChart();

        tot = 0.0;
        totsold = 0;
        track.clear();

        ArrayList<CustomerSummary> summaries = getCustomerSummaries();
        totcustomer = summaries.size();

        for (CustomerSummary summary : summaries) {
            tot += summary.getTotalPrice();
            totsold += summary.getTotalQuantity();
            String date = summary.getDate();
            track.put(date, track.getOrDefault(date, 0.0) + summary.getTotalPrice());
        }

        String formattedDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        double todayIncomeValue = track.getOrDefault(formattedDate, 0.0);

        String todayIncome = String.format("%.2f", todayIncomeValue) + " TK";
        String totalIncome =String.format("%.2f", tot) + " TK";
        String totalCustomer =String.valueOf(totsold);
        String totalSold =String.valueOf(totcustomer);

        Today_Income.setText(todayIncome);
        Total_income.setText(totalIncome);
        Total_Sold.setText(totalCustomer);
        Total_customer.setText(totalSold);
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
        EmployeePage.setVisible(false);
        customer_show.setVisible(false);
        inventory_Type_Status();
        InventoryShowData();
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

        menu_gridPane.getChildren().clear();

        int col=0;
        int row=0;
        menu_gridPane.getRowConstraints().clear();
        menu_gridPane.getColumnConstraints().clear();

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
                menu_gridPane.add(pan,col++,row);
                GridPane.setMargin(pan, new Insets(10, 10, 10, 10));

            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @FXML
    void Menu(ActionEvent event) {
        DashboardMainpage.setVisible(false);
        inventory_form.setVisible(false);
        ProfilePage.setVisible(false);
        menu_form.setVisible(true);
        ConfirmUpdate_page.setVisible(false);
        EmployeePage.setVisible(false);
        customer_show.setVisible(false);

        menuDisplyCard();
        displayorder();
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
        } finally {
            CloseConnection();
        }

    }

    @FXML
    void Profile(ActionEvent event) {
        DashboardMainpage.setVisible(false);
        inventory_form.setVisible(false);
        ProfilePage.setVisible(true);
        menu_form.setVisible(false);
        ConfirmUpdate_page.setVisible(false);
        EmployeePage.setVisible(false);
        customer_show.setVisible(false);

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


    @FXML
    void Employee(ActionEvent event) {
        DashboardMainpage.setVisible(false);
        inventory_form.setVisible(false);
        ProfilePage.setVisible(false);
        menu_form.setVisible(false);
        ConfirmUpdate_page.setVisible(false);
        EmployeePage.setVisible(true);
        customer_show.setVisible(false);

        employee_Category_Status();
        EmployeeShowData();
    }



    private void CloseConnection(){
        try {
            if(resultSet != null) resultSet.close();
            if(preparedStatement != null) preparedStatement.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }

    }


    @FXML
    private TableColumn<EmployeeData, String> Employee_Col_Category;

    @FXML
    private TableColumn<EmployeeData, Date> Employee_Col_Date;

    @FXML
    private TableColumn<EmployeeData, String> Employee_Col_ID;

    @FXML
    private TableColumn<EmployeeData, String> Employee_Col_Name;

    @FXML
    private TableColumn<EmployeeData, String> Employee_Col_Phone;

    @FXML
    private TableColumn<EmployeeData, String> Employee_Col_Status;

    @FXML
    private ComboBox<String> Employee_category;

    @FXML
    private ComboBox<String> Employee_Status;

    @FXML
    private TableView<EmployeeData> Employee_tableView;

    ObservableList<EmployeeData> EmployeeListData;

    ObservableList<EmployeeData> EmployeeDataList() {
        ObservableList<EmployeeData> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM userdetails";

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                if (resultSet.getString("Category").equals("Employee")) {
                    EmployeeData data = new EmployeeData(
                            resultSet.getString("RegistrationID"),
                            resultSet.getString("FullName"),
                            resultSet.getString("PhoneNumber"),
                            resultSet.getString("Category"),
                            resultSet.getDouble("Salery"),
                            resultSet.getDate("Date"),
                            resultSet.getString("Status")
                    );
                    list.add(data);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseConnection();
        }
        return list;
    }

    private <T> void centerAlignColumn(TableColumn<EmployeeData, T> column) {
        column.setCellFactory(col -> new TableCell<>() {
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

    void EmployeeShowData() {
        EmployeeListData = EmployeeDataList();

        Employee_Col_ID.setCellValueFactory(new PropertyValueFactory<>("RegistrationID"));
        Employee_Col_Name.setCellValueFactory(new PropertyValueFactory<>("FullName"));
        Employee_Col_Phone.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
        Employee_Col_Category.setCellValueFactory(new PropertyValueFactory<>("Category"));
        Employee_Col_Date.setCellValueFactory(new PropertyValueFactory<>("Date"));
        Employee_Col_Status.setCellValueFactory(new PropertyValueFactory<>("Status"));

        centerAlignColumn(Employee_Col_ID);
        centerAlignColumn(Employee_Col_Name);
        centerAlignColumn(Employee_Col_Phone);
        centerAlignColumn(Employee_Col_Category);
        centerAlignColumn(Employee_Col_Status);

        Employee_tableView.setItems(EmployeeListData);
    }

    void employee_Category_Status() {
        List<String> categoryList = List.of("Employee","Student","Teacher");
        ObservableList<String> categoryObservableList = FXCollections.observableArrayList(categoryList);
        Employee_category.setItems(categoryObservableList);

        List<String> statusList = List.of("Active", "Inactive", "On Leave");
        ObservableList<String> statusObservableList = FXCollections.observableArrayList(statusList);
        Employee_Status.setItems(statusObservableList);
    }

    @FXML
    private TextField employeenamefield;

    @FXML
    private TextField employeephonefield;

    @FXML
    private TextField employeesaleryfield;

    @FXML
    private TextField employidfield;

    @FXML
    void Employee_Delete(ActionEvent event) {
        if (employidfield.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill the Employee ID field.");
            alert.showAndWait();
        } else {
            String query = "DELETE FROM userdetails WHERE RegistrationID = ?";
            try {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, employidfield.getText());
                int rowsDeleted = preparedStatement.executeUpdate();
                if (rowsDeleted > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Employee deleted successfully.");
                    alert.showAndWait();
                    EmployeeShowData();
                    Employee_clear(event);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Employee ID not found.");
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                CloseConnection();
            }
        }
    }

    @FXML
    void Employee_Update(ActionEvent event) {
        if (employidfield.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill the Employee ID field.");
            alert.showAndWait();
        } else {
            String query = "UPDATE userdetails SET FullName = ?, PhoneNumber = ?, Salery = ? WHERE RegistrationID = ?";
            try {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, employeenamefield.getText());
                preparedStatement.setString(2, employeephonefield.getText());
                preparedStatement.setDouble(3, Double.parseDouble(employeesaleryfield.getText()));
                preparedStatement.setString(4, employidfield.getText());

                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Employee updated successfully.");
                    alert.showAndWait();
                    EmployeeShowData();
                    Employee_clear(event);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Employee ID not found.");
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                CloseConnection();
            }
        }
    }

    @FXML
    void Employee_add(ActionEvent event) {
        if (employeenamefield.getText().isEmpty() ||
                employeephonefield.getText().isEmpty() ||
                employeesaleryfield.getText().isEmpty() ||
                employidfield.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the fields.");
            alert.showAndWait();
        } else {
            String query = "INSERT INTO userdetails (RegistrationID, FullName, PhoneNumber, Salery, Date, Status, Category) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, employidfield.getText());
                preparedStatement.setString(2, employeenamefield.getText());
                preparedStatement.setString(3, employeephonefield.getText());
                preparedStatement.setDouble(4, Double.parseDouble(employeesaleryfield.getText()));
                preparedStatement.setDate(5, new Date(System.currentTimeMillis()));
                preparedStatement.setString(6, Employee_Status.getSelectionModel().getSelectedItem().toString());
                preparedStatement.setString(7, Employee_category.getSelectionModel().getSelectedItem().toString());

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Employee added successfully.");
                    alert.showAndWait();
                    EmployeeShowData();
                    Employee_clear(event);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                CloseConnection();
            }
        }
    }

    @FXML
    void Employee_clear(ActionEvent event) {
        employeenamefield.setText("");
        employeephonefield.setText("");
        employeesaleryfield.setText("");
        employidfield.setText("");
        employeenamefield.requestFocus();
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

        menu_total.setText("$0.0 TK");
        total_price = 0.0;
        String updateSql = "UPDATE customer SET type = 'Paid' WHERE type = 'Unpaid'";

        try {
            preparedStatement = connection.prepareStatement(updateSql);

            int rowsUpdated = preparedStatement.executeUpdate();
            System.out.println("Updated " + rowsUpdated + " rows to 'Paid'");

        } catch (SQLException e) {
            System.out.println("Error updating 'Paid' status: " + e.getMessage());
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

    @FXML
    private TableView<CustomerSummary> customer_tableview;

    @FXML
    private TableColumn<CustomerSummary, String> pars_col_customerid;

    @FXML
    private TableColumn<CustomerSummary, String> pars_col_customername;

    @FXML
    private TableColumn<CustomerSummary, String> pars_col_date;

    @FXML
    private TableColumn<CustomerSummary, Integer> pars_col_quantity;

    @FXML
    private TableColumn<CustomerSummary, Double> pars_col_price;





    public ArrayList<CustomerSummary> getCustomerSummaries() {
        ArrayList<CustomerSummary> summaries = new ArrayList<>();
        String sql = "SELECT c.customer_id, u.FullName AS customer_name, c.date, " +
                "SUM(c.quantity) AS total_quantity, " +
                "SUM(c.price * c.quantity) AS total_price " +
                "FROM customer c " +
                "JOIN userdetails u ON c.customer_id = u.RegistrationID " +
                "GROUP BY c.customer_id, c.date " +
                "ORDER BY c.customer_id, c.date";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String customerId = resultSet.getString("customer_id");
                String name = resultSet.getString("customer_name"); // Directly get the name
                String date = resultSet.getString("date");
                int totalQuantity = resultSet.getInt("total_quantity");
                double totalPrice = resultSet.getDouble("total_price");

                summaries.add(new CustomerSummary(customerId, date, totalQuantity, totalPrice, name));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching customer summaries: " + e.getMessage());
        }

        return summaries;
    }

        private ArrayList<CustomerSummary> alldata;

    public void customercount(){
        alldata = getCustomerSummaries();
    }

    private void showCustomerData() {
        // Link TableColumns to CustomerSummary properties
        ObservableList<CustomerSummary> data = FXCollections.observableArrayList(alldata);

        pars_col_customerid.setCellValueFactory(new PropertyValueFactory<>("CustomerId"));
        pars_col_customername.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
        pars_col_date.setCellValueFactory(new PropertyValueFactory<>("Date"));
        pars_col_quantity.setCellValueFactory(new PropertyValueFactory<>("TotalQuantity"));
        pars_col_price.setCellValueFactory(new PropertyValueFactory<>("TotalPrice"));


        customer_tableview.setItems(data);

        centerAlignColumn2(pars_col_customerid);
        centerAlignColumn2(pars_col_customername);
        centerAlignColumn2(pars_col_price);
        centerAlignColumn2(pars_col_quantity);
        centerAlignColumn2(pars_col_date);

    }

    private <T> void centerAlignColumn2(TableColumn<CustomerSummary, T> column) {
        column.setCellFactory(col -> new TableCell<>() {
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
    private BarChart<String, Number> Dash_customerChart; // Corrected generics
    @FXML
    private AreaChart<String, Number> Dash_incomeChart; // Corrected generics

    // Initialize Income Chart
    private void initializeIncomeChart() {
        // Set up axes
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Month");
        yAxis.setLabel("Revenue ($)");

        Dash_incomeChart.setTitle("Monthly Income Overview");
        Dash_incomeChart.setLegendVisible(false);

        // Sample data (replace with actual database data)
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Jan", 15000));
        series.getData().add(new XYChart.Data<>("Feb", 23000));
        series.getData().add(new XYChart.Data<>("Mar", 18500));
        series.getData().add(new XYChart.Data<>("Apr", 21000));

        Dash_incomeChart.getData().add(series);

        // Styling the AreaChart
        Platform.runLater(() -> {
            for (XYChart.Data<String, Number> data : series.getData()) {
                Node bar = data.getNode();
                if (bar != null) {
                    // Make the bars fatter
                    bar.setStyle("-fx-bar-width: 40px;");

                    // Change the color of the bars with a fancy gradient
                    bar.setStyle("-fx-bar-fill: linear-gradient(to top, #4caf50, #81c784);");

                    // Optional: Add a border to the bars
                    bar.setStyle("-fx-border-color: #388e3c; -fx-border-width: 2px;");
                }
            }
        });
    }

    // Initialize Customer Chart
    private void initializeCustomerChart() {
        // Set up axes
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Month");
        yAxis.setLabel("Number of Customers");

        Dash_customerChart.setTitle("Monthly Customer Activity");
        Dash_customerChart.setLegendVisible(false);

        // Sample data (replace with database data)
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Jan", 150));
        series.getData().add(new XYChart.Data<>("Feb", 230));
        series.getData().add(new XYChart.Data<>("Mar", 185));
        series.getData().add(new XYChart.Data<>("Apr", 210));

        Dash_customerChart.getData().add(series);

        // Use Platform.runLater to ensure the chart is fully rendered before applying styling
        Platform.runLater(() -> {
            for (XYChart.Data<String, Number> data : series.getData()) {
                Node bar = data.getNode();

                // Ensure the node is not null before styling
                if (bar != null) {
                    // Make the bars fatter
                    bar.setStyle("-fx-bar-width: 40px;");

                    // Change the color of the bars with a fancy gradient
                    bar.setStyle("-fx-bar-fill: linear-gradient(to top, #ff9800, #ff5722);");

                    // Optional: Add a border to the bars
                    bar.setStyle("-fx-border-color: #ff5722; -fx-border-width: 2px;");
                }
            }

            // Optional: Add a glow effect to the bars
            for (XYChart.Data<String, Number> data : series.getData()) {
                Node bar = data.getNode();
                if (bar != null) {
                    bar.setStyle("-fx-effect: dropshadow(gaussian, #ff5722, 10, 0, 0, 2);");
                }
            }
        });
    }


    // Load Customer Chart Data from the database
    private void loadCustomerChartData() {
        // Clear any existing data before adding new data
        Dash_customerChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        String sql = "SELECT MONTH(date) AS month, COUNT(DISTINCT customer_id) AS count "
                + "FROM customer GROUP BY MONTH(date)";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String month = getMonthName(rs.getInt("month"));
                int count = rs.getInt("count");
                series.getData().add(new XYChart.Data<>(month, count));
            }

            Dash_customerChart.getData().add(series); // Add data to the chart
        } catch (SQLException e) {
            System.out.println("Error loading chart data: " + e.getMessage());
        }
    }

    // Utility method to get the month name
    private String getMonthName(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }



}

