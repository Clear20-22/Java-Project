package cafe.management.cafe;

import com.mysql.cj.exceptions.DataReadException;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.collections.ObservableList;


public class ManagementControl {

    @FXML
    private TextField passclue;

    @FXML
    private AnchorPane ChangePassword;

    @FXML
    private PasswordField ConfirmPassword;

    @FXML
    private TextField ForgateDate;

    @FXML
    private Label ForgateDateError;

    @FXML
    private Hyperlink ForgetPassword;

    @FXML
    private TextField GiveForgetEmail;

    @FXML
    private PasswordField GivePassword;

    @FXML
    private TextField Givefullname;

    @FXML
    private TextField GiveRegiID;

    @FXML
    private Button Login;

    @FXML
    private PasswordField LoginPassword;

    @FXML
    private TextField LoginRegiID;

    @FXML
    private AnchorPane Login_Form;

    @FXML
    private PasswordField NewPassword;

    @FXML
    private Label PasswordError;

    @FXML
    private AnchorPane Proceed_Form;

    @FXML
    private Label Regclueerror;

    @FXML
    private Label RegIDerror;

    @FXML
    private Label RegPasswordError;

    @FXML
    private Label Regnamerror;

    @FXML
    private Button SignUp;

    @FXML
    private Label UsernameError;

    @FXML
    private Label blankemail;

    @FXML
    private Button cngBack;

    @FXML
    private Label confirmpassworderror;

    @FXML
    private Button forgetemailback;

    @FXML
    private Button forgetproceed;

    @FXML
    private Button left;

    @FXML
    private Label need;

    @FXML
    private Label need1;

    @FXML
    private Label newpassworderror;

    @FXML
    private Button right;

    @FXML
    private AnchorPane side_form;


    @FXML
    private Label RegCategoryError;

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    private String [] category = {"Teacher","Student","Employee"};

    @FXML
    private ComboBox<?> Category_Type;

    public ManagementControl() {
        connection = DatabaseUsers.getConnection();
    }

    @FXML
    void Switch(ActionEvent e){
        TranslateTransition slide = new TranslateTransition();

        if(e.getSource() == right){
            slide.setNode(side_form);
            slide.setToX(375);
            slide.setDuration(Duration.seconds(.7));
            slide.setOnFinished(event -> {
                right.setVisible(false);
                left.setVisible(true);
                need.setVisible(false);
                need1.setVisible(true);
                Login_Form.setVisible(true);
                Proceed_Form.setVisible(false);
                ChangePassword.setVisible(false);
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
                Login_Form.setVisible(true);
                Proceed_Form.setVisible(false);
                ChangePassword.setVisible(false);
            });
            slide.play();
        }
       Regnamerror.setText("");
        RegPasswordError.setText("");
        RegIDerror.setText("");
       Regclueerror.setText("");
       UsernameError.setText("");
       PasswordError.setText("");
       RegCategoryError.setText("");
        Givefullname.setText("");
        GivePassword.setText("");
        GiveRegiID.setText("");
        passclue.setText("");
        LoginRegiID.setText("");
        LoginPassword.setText("");

        List<String> list = new ArrayList<>();

        for(String s : category){
            list.add(s);
        }
        ObservableList observableList = FXCollections.observableArrayList(list);
        Category_Type.setItems(observableList);
    }

    protected static String ID = "";

    @FXML
    void LoginPage(ActionEvent event) {
        UsernameError.setText("");
        PasswordError.setText("");

        String username = LoginRegiID.getText();
        String password = LoginPassword.getText();

        boolean loginerror = false;

        if (username.isEmpty()) {
            UsernameError.setText("Registration ID is required.");
            loginerror = true;
        }

        if (password.isEmpty()) {
            PasswordError.setText("Password is required.");
            loginerror = true;
        }

        if(loginerror == true)
            return;

        try {
            String query = "SELECT * FROM userdetails WHERE RegistrationID = ? AND password = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next())  {
                System.out.println("Login successful! Welcome " + username);
                ID = username;
                String sql = "SELECT Category FROM userdetails WHERE RegistrationID = ?";
               try {
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, ID);
                    resultSet = preparedStatement.executeQuery();

                    String category;

                    if (resultSet.next()) {
                        category = resultSet.getString("Category");
                        if (category.equals("Employee")) {
                            CafeManagement.switchScene("SurfaceLayout.fxml", 1100);
                        } else {
                            CafeManagement.switchScene("CurtomerLayout.fxml", 1100);
                        }
                    } else {
                        System.out.println("ID not found.");
                    }
                } catch (SQLException e){
                   e.printStackTrace();
               }
            } else {
                System.out.println("Invalid ID or password!");
                UsernameError.setText("Invalid ID.");
                PasswordError.setText("Invalide Password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Database query failed!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources();
        }
    }



    @FXML
    void SignUp(ActionEvent event) {
       Regnamerror.setText("");
        RegPasswordError.setText("");
        RegIDerror.setText("");
       Regclueerror.setText("");
        RegCategoryError.setText("");


        String username = Givefullname.getText();
        String password = GivePassword.getText();
        String Regid = GiveRegiID.getText();
        String clue= passclue.getText();


        boolean error = false;

        if (username.isEmpty()) {
           Regnamerror.setText("Name is required.");
           error = true;
        }

        if (password.isEmpty()) {
            RegPasswordError.setText("Password is required.");
            error = true;
        }

        if (Regid.isEmpty()) {
            RegIDerror.setText("Registration ID is required.");
            error = true;
        }

        if (clue.isEmpty()) {
           Regclueerror.setText("Password Clue is required.");
            error = true;
        }

        if(Category_Type.getSelectionModel().getSelectedItem() == null){
            RegCategoryError.setText("Please Choose category.");
            error = true;
        }

        if(error == true)
            return;
        try {
            String usernameQuery = "SELECT COUNT(*) FROM userdetails WHERE FullName = ?";
            preparedStatement = connection.prepareStatement(usernameQuery);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            boolean check = false;
            if (resultSet.next() && resultSet.getInt(1) > 0) {
               Regnamerror.setText("Name is already taken.");
               check = true;
            }

            String emailQuery = "SELECT COUNT(*) FROM userdetails WHERE RegistrationID = ?";
            preparedStatement = connection.prepareStatement(emailQuery);
            preparedStatement.setString(1, Regid);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                RegIDerror.setText("Registration ID is already registered.");
                check = true;
            }

            if(check == true)
                return ;

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Registration successfull.");

            Optional<ButtonType> opt = alert.showAndWait();

            if(opt.isPresent() && opt.get() == ButtonType.OK) {
                String insertQuery = "INSERT INTO userdetails (FullName, RegistrationID, PasswordClue, password, Category) VALUES (?, ?, ?, ?, ?)";

                preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, Regid);
                preparedStatement.setString(3, clue);
                preparedStatement.setString(4, password);
                preparedStatement.setString(5, Category_Type.getSelectionModel().getSelectedItem().toString());

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("User registered successfully!");

                        ActionEvent mock = new ActionEvent(left, null);
                        Switch(mock);

                } else {
                    System.err.println("User registration failed!");
                }

            } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error Message");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Please try again.");
                    errorAlert.showAndWait();
                }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("SQL Error: " + e.getMessage());
        } finally {
            closeResources();
        }

    }


    @FXML
    void switchForm(ActionEvent event) {
         if(event.getSource()==ForgetPassword){
            Login_Form.setVisible(false);
            Proceed_Form.setVisible(true);
        }
    }


    @FXML
    void ChangeBack(ActionEvent event) {
            if(event.getSource() == cngBack){
                Proceed_Form.setVisible(true);
                ChangePassword.setVisible(false);
                NewPassword.setText("");
                ConfirmPassword.setText("");
                confirmpassworderror.setText("");
                newpassworderror.setText("");
            }
    }

    @FXML
    void ChangePassword(ActionEvent event) {
        newpassworderror.setText("");
        confirmpassworderror.setText("");

        String nwpassword = NewPassword.getText();
        String confmpassword = ConfirmPassword.getText();
        if(nwpassword.isEmpty()){
            newpassworderror.setText("New password is required.");
            return;
        }

        if(confmpassword.isEmpty()){
            confirmpassworderror.setText("Confirm password is required.");
            return;
        }

        if(!nwpassword.equals(confmpassword)){
            confirmpassworderror.setText("Please give the same password.");
            return;
        }
        if(nwpassword.equals(confmpassword)){
            try{
                String email = GiveForgetEmail.getText();
                String date = ForgateDate.getText();
                date = convertDate(date,"dd-MM-yyyy");
                String query = "UPDATE userdetails SET password = ? WHERE email = ? AND passclue = ?";

                preparedStatement = connection.prepareStatement(query);

                preparedStatement.setString(1,nwpassword);
                preparedStatement.setString(2,email);
                preparedStatement.setString(3, date);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("User details updated successfully.");
                } else {
                    System.out.println("No user found with the specified ID.");
                }
            }catch (SQLException e){

                e.printStackTrace();
                System.out.println(e.getMessage());
            }
            finally {
                closeResources();
            }

        }
    }

    @FXML
    void ForgetEmailBack(@NotNull ActionEvent event) {
        if(event.getSource() == forgetemailback){
            Proceed_Form.setVisible(false);
            Login_Form.setVisible(true);
            ForgateDate.setText("");
            GiveForgetEmail.setText("");
            blankemail.setText("");
            ForgateDateError.setText("");
        }

    }


    @FXML
    void ForgetProceed(ActionEvent event) {

        blankemail.setText("");
        ForgateDateError.setText("");

        String email = GiveForgetEmail.getText();
        String date = ForgateDate.getText();
        if(email.isEmpty()){
            blankemail.setText("Email is required.");
            return;
        }

        if(date.isEmpty()){
            ForgateDateError.setText("Birth Date is required.");
            return;
        }

        try{
            date = convertDate(date,"dd-MM-yyyy");
            String query = "SELECT * FROM userdetails WHERE email = ? AND passclue = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,date);

            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Proceed_Form.setVisible(false);
                ChangePassword.setVisible(true);
                blankemail.setText("");
                ForgateDateError.setText("");
            }else{
                blankemail.setText("Use Valid Email Address.");
                ForgateDateError.setText("Use Valid Birth Date.");
                GiveForgetEmail.setText("");
                ForgateDate.setText("");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            closeResources();
        }
    }

    @NotNull
    private String convertDate(String inputDate, String inputFormat) {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(inputFormat);
            LocalDate date = LocalDate.parse(inputDate, inputFormatter);


            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return date.format(outputFormatter);
        } catch (DateTimeParseException e) {
            return "Invalid date or format: " + e.getMessage();
        }
    }

    private void closeResources() {
        try {
            if (resultSet != null) {
                resultSet.close();
                resultSet = null;
            }
            if (preparedStatement != null) {
                preparedStatement.close();
                preparedStatement = null;
            }
//            if (connection != null) {
//                connection.close();
//                connection = null;
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
