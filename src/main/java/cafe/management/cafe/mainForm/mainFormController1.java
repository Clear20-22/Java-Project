package cafeshopmanagementsystem;

import com.sun.jdi.connect.spi.Connection;
import java.beans.Statement;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class mainFormController1 implements Initializable{
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
    private ComboBox<?> inventory_type;

    @FXML
    private TextField inventory_stock;

    @FXML
    private TableView<?> inventory_tableView;

    @FXML
    private Button inventory_updateBtn;

    @FXML
    private Button logout_btn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button menu_btn;

    @FXML
    private Label username;
    
    //
    
    
  
    
    
    private Alert alert;
    
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    
    
    
    /*
    public void inventoryAddBtn(){
        
        if(inventory_product.ID)
    }
    */
    
    
    
    private String[] typeList={"Meals", "Drinks"};
    
    public void inventoryTypeList(){
        List<String> typeL=new ArrayList<>();
        
        for(String data: typeList){
            typeL.add(data);
        }
        
        ObservableList listData=FXCollections.observableArrayList(typeL);
        inventory_type.setItems(listData);
    }
    
    private String[] statusList={"Available","Unavailable"};
    public void inventoryStatusList(){
        
        List<String> statusL=new ArrayList<>();
        
        for(String data: statusList){
            statusL.add(data);
        }
        
        ObservableList listData= FXCollections.observableArrayList(statusL);
        inventory_status.setItems(listData);
        
    }
    
    public void logout(){
        
        try{
            
            alert=new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option=alert.showAndWait();
            
            if(option.get().equals(ButtonType.OK)){
                
                // TO HIDE MAIN FORM
                logout_btn.getScene().getWindow().hide();
                
                // LINK YOU LOGIN FORM AND SHOW IT
                Parent root=FXMLLoader.load(getClass().getResource("add login page fxml file"));
                
                Stage stage=new Stage();
                Scene scene=new Scene(root);
                
                stage.setTitle("Cafe Shop Management System");
                
                stage.setScene(scene);
                stage.show();
            }
       
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }
    
    public void displayUsername(){
        String user=data.username;
        user=user.substring(0,1).toUpperCase()+user.substring(1);
        username.setText(user);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resources){
        
        displayUsername();
        
        inventoryTypeList();
        
        inventoryStatusList();
    }     
}
