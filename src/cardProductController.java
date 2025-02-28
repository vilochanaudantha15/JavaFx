
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class cardProductController implements Initializable {

    @FXML
    private AnchorPane card_form;

    @FXML
    private Button prod_addBtn;

    @FXML
    private ImageView prod_imageView;

    @FXML
    private Label prod_name;

    @FXML
    private Label prod_price;

    @FXML
    private Spinner<Integer> prod_spinner;

    private productData prodData;
    private Image image;
    private String prodID;

    private SpinnerValueFactory<Integer> spin;
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Alert alert;
    private String type;
    private String prod_image;
    private String prod_date;

    private int qty;
    private double totalp;
    private double pr;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setQuantity();
    }

    public void setData(productData prodData) {
        this.prodData = prodData;
        this.prodID = prodData.getProductId();
        this.prod_name.setText(prodData.getProductName());
        this.prod_price.setText("Rs: " + prodData.getPrice());

        try {
            String path = "file:" + prodData.getImage(); // Ensure the correct format for file paths
            image = new Image(path, 210, 76, false, true);
            prod_imageView.setImage(image);
            this.pr = prodData.getPrice();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception (e.g., show a default image or an error message)
        }

        this.type = prodData.getType();
        this.prod_image = prodData.getImage();
        this.prod_date = String.valueOf(prodData.getDate());
    }

    public void setQuantity() {
        spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        prod_spinner.setValueFactory(spin);
    }

    public void addBtn() {
        mainFormController mForm = new mainFormController();
        mForm.customerID();

        qty = prod_spinner.getValue();

        String checkAvailable = "SELECT status FROM product WHERE prod_id = ?";
        connect = Database.connectDB();

        try {
              int checkStock = 0;
                String checkStockQuery = "SELECT stock FROM product WHERE prod_id = ?";
                prepare = connect.prepareStatement(checkStockQuery);
                prepare.setString(1, prodID);
                result = prepare.executeQuery();

                if (result.next()) {
                    checkStock = result.getInt("stock");
                }
                
                if(checkStock == 0) {
                    String updateStock = "UPDATE PRODUCT SET";
                }
            
            prepare = connect.prepareStatement(checkAvailable);
            prepare.setString(1, prodID);
            result = prepare.executeQuery();

            String check = "";
            if (result.next()) {
                check = result.getString("status");
            }

            if (!check.equals("Available") || qty == 0) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Something went wrong :(");
                alert.showAndWait();
            } else {
              

                if (checkStock < qty) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid, this product is out of stock");
                    alert.showAndWait();
                } else {
                    
                    prod_image = prod_image.replace("\\", "\\\\");
                    String insertData = "INSERT INTO customer "
                            + "(customer_id, prod_id, prod_name, type, quantity, price, date, image, em_username) "
                            + "VALUES (?,?, ?, ?, ?, ?, ?, ?,?)";
                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, String.valueOf(data.cID));
                    prepare.setString(2, prodID);
                    prepare.setString(3, prod_name.getText());
                    prepare.setString(4, type);
                    prepare.setString(5, String.valueOf(qty));

                    totalp = qty * pr;
                    prepare.setString(6, String.valueOf(totalp));

                    Date date = new Date(System.currentTimeMillis());
                    prepare.setDate(7, date);
                    prepare.setString(8, prod_image);
                    prepare.setString(9, data.username);

                    prepare.executeUpdate();

                    int upStock = checkStock - qty;

                    String updateStock = "UPDATE product SET stock = ?, date = ? WHERE prod_id = ?";
                    prepare = connect.prepareStatement(updateStock);
                    prepare.setInt(1, upStock);
                    prepare.setString(2, prod_date);
                    prepare.setString(3, prodID);

                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added");
                    alert.showAndWait();
                    
                    mainFormController mForm1 = new mainFormController();
                    mForm.menuGetTotal();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
                if (prepare != null) {
                    prepare.close();
                }
                if (connect != null) {
                    connect.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
