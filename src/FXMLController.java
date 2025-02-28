
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FXMLController implements Initializable {

    @FXML
    private TextField SU_Username;

    @FXML
    private TextField SU_answer;

    @FXML
    private PasswordField SU_password;

    @FXML
    private ComboBox<String> SU_question;

    @FXML
    private Button SU_signupBtn;

    @FXML
    private AnchorPane SU_signupForm;

    @FXML
    private TextField Si_Username;

    @FXML
    private Hyperlink Si_forgot;

    @FXML
    private Button Si_loginBtn;

    @FXML
    private AnchorPane Si_loginForm;

    @FXML
    private PasswordField Si_password;

    @FXML
    private Button Side_CreateBtn;

    @FXML
    private AnchorPane Side_form;

    @FXML
    private Button side_alreadyHave;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private Alert alert;

    public String[] questionList = {"What is your favourite color?", "What is your favourite food?", "What is your birth date"};

    public void loginBtn() {
        if (Si_Username.getText().isEmpty() || Si_password.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR Message");
            alert.setHeaderText(null);
            alert.setContentText("Incorrect Username/Password");
            alert.showAndWait();
        } else {
            String selctData = "SELECT username, password FROM employee WHERE username = ? and password = ?";

            connect = Database.connectDB();

            try {
                prepare = connect.prepareStatement(selctData);
                prepare.setString(1, Si_Username.getText());
                prepare.setString(2, Si_password.getText()); // Corrected from prepare.setString(1, ...);

                result = prepare.executeQuery();

                if (result.next()) {
                    
                    data.username = Si_Username.getText();
                    
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Logged In");
                    alert.showAndWait();

                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("manForm.fxml"));
                        Stage stage = new Stage();
                        Scene scene = new Scene(root);
                        stage.setTitle("Cafe Management System");
                        stage.setMinWidth(1100);
                        stage.setMinHeight(600);
                        stage.setScene(scene);
                        stage.show();

                        // Hide the current window (login form)
                        Si_loginBtn.getScene().getWindow().hide();
                    } catch (IOException e) {
                        e.printStackTrace();
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Loading Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Could not load the main form. Please check the file path and try again.");
                        alert.showAndWait();
                    }

                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect username or password");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void regBtn() {
        if (SU_Username.getText().isEmpty() || SU_password.getText().isEmpty() || SU_question.getSelectionModel().getSelectedItem() == null || SU_answer.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            String regData = "INSERT INTO employee (username, password, question, answer, date) VALUES(?,?,?,?,?)";
            connect = Database.connectDB();

            try {
                String checkUsername = "SELECT username FROM employee WHERE username = ?";

                prepare = connect.prepareStatement(checkUsername);
                prepare.setString(1, SU_Username.getText());
                result = prepare.executeQuery();

                if (result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText(SU_Username.getText() + " is already taken");
                    alert.showAndWait();

                } else if (SU_password.getText().length() < 8) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Password should be at least 8 characters");
                    alert.showAndWait();

                } else {
                    prepare = connect.prepareStatement(regData);
                    prepare.setString(1, SU_Username.getText());
                    prepare.setString(2, SU_password.getText());
                    prepare.setString(3, SU_question.getSelectionModel().getSelectedItem());
                    prepare.setString(4, SU_answer.getText());

                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    prepare.setDate(5, sqlDate); // Use setDate instead of setString

                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Registered Account");
                    alert.showAndWait();

                    SU_Username.setText("");
                    SU_password.setText("");
                    SU_question.getSelectionModel().clearSelection();
                    SU_answer.setText("");

                    TranslateTransition slider = new TranslateTransition();
                    slider.setNode(Side_form);
                    slider.setToX(0);
                    slider.setOnFinished((ActionEvent e) -> {
                        side_alreadyHave.setVisible(false);
                        Side_CreateBtn.setVisible(true);
                        Si_loginForm.toFront();
                    });

                    slider.play();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void reLquestionList() {
        if (SU_question != null) {
            List<String> listQ = new ArrayList<>();
            for (String data : questionList) {
                listQ.add(data);
            }
            ObservableList<String> listData = FXCollections.observableArrayList(listQ);
            SU_question.setItems(listData);
        }
    }

    public void switchForm(ActionEvent event) {
        TranslateTransition slider = new TranslateTransition();
        slider.setNode(Side_form);
        slider.setDuration(javafx.util.Duration.seconds(.5));

        if (event.getSource() == Side_CreateBtn) {
            slider.setToX(300);
            slider.setOnFinished((ActionEvent e) -> {
                side_alreadyHave.setVisible(true);
                Side_CreateBtn.setVisible(false);
                SU_signupForm.toFront();
                reLquestionList();
            });
        } else if (event.getSource() == side_alreadyHave) {
            slider.setToX(0);
            slider.setOnFinished((ActionEvent e) -> {
                side_alreadyHave.setVisible(false);
                Side_CreateBtn.setVisible(true);
                Si_loginForm.toFront();
            });
        }

        slider.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize any necessary logic here
    }
}
