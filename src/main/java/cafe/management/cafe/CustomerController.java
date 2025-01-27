package cafe.management.cafe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

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
    private TableView<?> menu_tableView;

    @FXML
    private AnchorPane AboutUs_Page;

    @FXML
    void CustomerAboutUs(ActionEvent event) {
        AboutUs_Page.setVisible(true);
        CustomerMenuForm.setVisible(false);
        CustomerProfilePage.setVisible(false);
        CustomerProfileUpdatePage.setVisible(false);
    }

    @FXML
    void CustomerMenu(ActionEvent event) {
        AboutUs_Page.setVisible(false);
        CustomerMenuForm.setVisible(true);
        CustomerProfilePage.setVisible(false);
        CustomerProfileUpdatePage.setVisible(false);
    }

    @FXML
    void CustomerProfile(ActionEvent event) {
        AboutUs_Page.setVisible(false);
        CustomerMenuForm.setVisible(false);
        CustomerProfilePage.setVisible(true);
        CustomerProfileUpdatePage.setVisible(false);
    }

    @FXML
    void CustomerProfileEditConfirm(ActionEvent event) {
        AboutUs_Page.setVisible(false);
        CustomerMenuForm.setVisible(false);
        CustomerProfilePage.setVisible(true);
        CustomerProfileUpdatePage.setVisible(false);
    }

    @FXML
    void CustomerProfileUpdate(ActionEvent event) {
        AboutUs_Page.setVisible(false);
        CustomerMenuForm.setVisible(false);
        CustomerProfilePage.setVisible(false);
        CustomerProfileUpdatePage.setVisible(true);
    }

    @FXML
    void CustomerSignOut(ActionEvent event) {

    }

    @FXML
    void CutomerReview(ActionEvent event) {
        AboutUs_Page.setVisible(false);
        CustomerMenuForm.setVisible(false);
        CustomerProfilePage.setVisible(false);
        CustomerProfileUpdatePage.setVisible(false);
    }

}
