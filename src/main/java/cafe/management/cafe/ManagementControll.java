package cafe.management.cafe;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;


public class ManagementControll {

    @FXML
    private TextField BirthDate;

    @FXML
    private Label need;

    @FXML
    private Label need1;

    @FXML
    private Hyperlink ForgetPassword;

    @FXML
    private PasswordField GivePassword;

    @FXML
    private TextField GiveUsername;

    @FXML
    private TextField JobTitle;

    @FXML
    private Button Login;

    @FXML
    private PasswordField LoginPassword;

    @FXML
    private TextField LoginUsername;

    @FXML
    private Button SignUp;

    @FXML
    private AnchorPane side_form;

    @FXML
    private Button left;

    @FXML
    private Button right;




    @FXML
    void Switch(ActionEvent e){
        TranslateTransition slide = new TranslateTransition();

        if(e.getSource() == right){
            slide.setNode(side_form);
            slide.setToX(314);
            slide.setDuration(Duration.seconds(1));
            slide.setOnFinished(event -> {
                right.setVisible(false);
                left.setVisible(true);
                need.setVisible(false);
                need1.setVisible(true);
            });
            slide.play();
        }
        else if(e.getSource() == left){
            slide.setNode(side_form);
            slide.setToX(0);
            slide.setDuration(Duration.seconds(1));
            slide.setOnFinished(event -> {
                right.setVisible(true);
                left.setVisible(false);
                need.setVisible(true);
                need1.setVisible(false);
            });
            slide.play();
        }

    }

    @FXML
    void LoginPage(ActionEvent event) {
        String username = LoginUsername.getText();
        String password = LoginPassword.getText();
        if("Sojib".equals(username) && "sojib".equals(password)){
            Login.setText("Logged In.");
        }
        else {
            Login.setText("Wrong.");
        }
    }

    @FXML
    void SignUp(ActionEvent event) {

    }

}
