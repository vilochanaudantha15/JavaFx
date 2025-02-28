
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import javafx.scene.chart.XYChart;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author vilochana udantha
 */
public class mainFormController implements Initializable {

    @FXML
    private Button customers_Btn;

    @FXML
    private Button dashboard_Btn;

    @FXML
    private Button inventory_Btn;

    @FXML
    private Button inventory_addBtn;

    @FXML
    private Button inventory_clearBtn;

    @FXML
    private TableColumn<productData, String> inventory_col_Date;

    @FXML
    private TableColumn<productData, String> inventory_col_Price;

    @FXML
    private TableColumn<productData, String> inventory_col_ProductName;

    @FXML
    private TableColumn<productData, String> inventory_col_Status;

    @FXML
    private TableColumn<productData, String> inventory_col_Stock;

    @FXML
    private TableColumn<productData, String> inventory_col_Type;

    @FXML
    private TableColumn<productData, String> inventory_col_idProduct;

    @FXML
    private Button inventory_deleteBtn;

    @FXML
    private AnchorPane inventory_form;

    @FXML
    private ImageView inventory_imageView;

    @FXML
    private Button inventory_importBtn;

    @FXML
    private TableView<productData> inventory_tableView;

    @FXML
    private Button inventory_updateBtn;

    @FXML
    private ImageView logout_Btn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button menu_Btn;

    @FXML
    private TextField Inventory_Price;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private TextField Inventory_ProductID;

    @FXML
    private TextField Inventory_ProductName;

    @FXML
    private ComboBox<?> Inventory_Status;

    @FXML
    private TextField Inventory_Stock;

    @FXML
    private ComboBox<?> Inventory_Type;

    @FXML
    private Label username;

    @FXML
    private TextField menu_amount;

    @FXML
    private Label menu_change;

    @FXML
    private TableColumn<productData, String> menu_col_price;

    @FXML
    private TableColumn<productData, String> menu_col_productName;

    @FXML
    private TableColumn<productData, String> menu_col_quantity;

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
    private TableView<productData> menu_tableView;

    @FXML
    private Label menu_total;
    
        @FXML
    private AreaChart<?, ?> dashboard_incomeChart; 
    
   @FXML
    private BarChart<?, ?> dashboard_CustomerChart;

    @FXML
    private Label dashboard_NC;

    @FXML
    private Label dashboard_NSP;

    @FXML
    private Label dashboard_TI;

    @FXML
    private Label dashboard_TotalI;


    private Alert alert;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    private Image image;

    private ObservableList<productData> cardListData = FXCollections.observableArrayList();

   public void dashboardDisplayNC() {
    String sql = "SELECT COUNT(id) AS count FROM receipt";  // Added alias for clarity
    connect = Database.connectDB();
    
    try {
        int nc = 0;
        prepare = connect.prepareStatement(sql);
        result = prepare.executeQuery();
        
        if(result.next()) {
            nc = result.getInt("count");  // Using the alias 'count'
        }
        
        dashboard_NC.setText(String.valueOf(nc));
        
    } catch(Exception e) {
        e.printStackTrace();
    } finally {
        closeResources();
    }
}



public void dashboardDisplayTI() {
    // Get start and end of the current day as Unix timestamps (in seconds)
    long startOfDay = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
    long endOfDay = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toEpochSecond() - 1;
    
    // SQL query to get the sum of total income for today's receipts
    String sql = "SELECT SUM(total) AS total_sum FROM receipt WHERE date >= ? AND date <= ?";
    
    connect = Database.connectDB();
    
    try {
        double ti = 0;
        prepare = connect.prepareStatement(sql);
        prepare.setLong(1, startOfDay);  // Set the start of the current day
        prepare.setLong(2, endOfDay);    // Set the end of the current day

        result = prepare.executeQuery();
        
        if (result.next()) {
            ti = result.getDouble("total_sum");  // Get the sum of today's income
        }
        
        // Display the total income for today
        dashboard_TI.setText("$" + ti);
        
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        closeResources();
    }
}

public void dashboardTotalI() {
    String sql = "SELECT SUM(total) AS total_sum FROM receipt";  // Missing closing quote fixed
    
    connect = Database.connectDB();
    
    try {
        double ti = 0;
        prepare = connect.prepareStatement(sql);
        result = prepare.executeQuery();
        
        if(result.next()) {
            ti = result.getDouble("total_sum");  // Using alias 'total_sum'
        }
        
        dashboard_TotalI.setText("$" + ti);
        
    } catch(Exception e) {
        e.printStackTrace();
    } finally {
        closeResources();
    }
}
 
public void dashboardNSP() {
    dashboard_incomeChart.getData().clear();
    String sql = "SELECT COUNT(id) AS customer_count FROM customer";  // Changed 'quantity' to 'id' (assumed to be correct)
    
    connect = Database.connectDB();
    
    try {
        int q = 0;
        prepare = connect.prepareStatement(sql);
        result = prepare.executeQuery();
        
        if(result.next()) {
            q = result.getInt("customer_count");  // Using alias 'customer_count'
        }
        
        dashboard_NSP.setText(String.valueOf(q));
        
    } catch(Exception e) {
        e.printStackTrace();
    } finally {
        closeResources();
    }
}

// Close resources (if not already implemented)
private void closeResources() {
    try {
        if (result != null) result.close();
        if (prepare != null) prepare.close();
        if (connect != null) connect.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

public void dashboardIncomeChart() {
    
    String sql = "SELECT date, SUM(total) FROM receipt GROUP BY date ORDER BY TIMESTAMP(date)";
    connect = Database.connectDB();
    XYChart.Series chart = new XYChart.Series();
    
    try{
        prepare = connect.prepareStatement(sql);
        result = prepare.executeQuery();
        
        while(result.next()) {
            chart.getData().add(new XYChart.Data<>(result.getString(1), result.getFloat(2)));
        }
        
        dashboard_incomeChart.getData().add(chart);
        
    }catch(Exception e) {e.printStackTrace();}
    
}
        
public void dashboardCustomerChart() {
    
    String sql = "SELECT date, COUNT(total) FROM receipt GROUP BY date ORDER BY TIMESTAMP(date)";
    connect = Database.connectDB();
    XYChart.Series chart = new XYChart.Series();
    
    try{
        prepare = connect.prepareStatement(sql);
        result = prepare.executeQuery();
        
        while(result.next()) {
            chart.getData().add(new XYChart.Data<>(result.getString(1), result.getInt(2)));
        }
        
        dashboard_CustomerChart.getData().add(chart);
        
    }catch(Exception e) {e.printStackTrace();}
    
}
      


    public void inventoryAddBtn() throws SQLException {
        if (Inventory_ProductID.getText().isEmpty()
                || Inventory_ProductName.getText().isEmpty()
                || Inventory_Type.getSelectionModel().getSelectedItem() == null
                || Inventory_Stock.getText().isEmpty()
                || Inventory_Price.getText().isEmpty()
                || Inventory_Status.getSelectionModel().getSelectedItem() == null
                || data.path == null) {

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the blank fields");
            alert.showAndWait();

        } else {

            String checkProdId = "SELECT prod_id FROM product WHERE prod_id = ' " + Inventory_ProductID + " ' ";

            connect = Database.connectDB();

            try {
                statement = connect.createStatement();
                result = statement.executeQuery(checkProdId);

                if (result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(Inventory_ProductID.getText() + "is  already taken");
                    alert.showAndWait();
                } else {

                    String insertData = "INSERT INTO product "
                            + " (prod_id, prod_name, type, stock, price, status, image, date) "
                            + " VALUES (?,?,?,?,?,?,?,?)";

                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, Inventory_ProductID.getText());
                    prepare.setString(2, Inventory_ProductName.getText());
                    prepare.setString(3, (String) Inventory_Type.getSelectionModel().getSelectedItem());
                    prepare.setString(4, Inventory_Stock.getText());
                    prepare.setString(5, Inventory_Price.getText());
                    prepare.setString(6, (String) Inventory_Status.getSelectionModel().getSelectedItem());

                    String path = data.path;
                    path = path.replace("\\", "\\\\");

                    prepare.setString(7, path);

                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                    prepare.setString(8, String.valueOf(sqlDate));

                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Succesfully Added!");
                    alert.showAndWait();

                    inventoryShowData();
                    inventoryClearBtn();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void inventoryUpdateBtn() {

        if (Inventory_ProductID.getText().isEmpty()
                || Inventory_ProductName.getText().isEmpty()
                || Inventory_Type.getSelectionModel().getSelectedItem() == null
                || Inventory_Stock.getText().isEmpty()
                || Inventory_Price.getText().isEmpty()
                || Inventory_Status.getSelectionModel().getSelectedItem() == null
                || data.path == null || data.id == 0) {

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the blank fields");
            alert.showAndWait();

        } else {
            String path = data.path;
            path = path.replace("\\", "\\\\");

            String updateData = "UPDATE product SET "
                    + "prod_id = '" + Inventory_ProductID.getText() + "', "
                    + "prod_name = '" + Inventory_ProductName.getText() + "', "
                    + "type = '" + Inventory_Type.getSelectionModel().getSelectedItem() + "', "
                    + "stock = '" + Inventory_Stock.getText() + "', "
                    + "price = '" + Inventory_Price.getText() + "', "
                    + "status = '" + Inventory_Status.getSelectionModel().getSelectedItem() + "', "
                    + "image = '" + path + "', "
                    + "date = '" + data.date + "' WHERE id = " + data.id;

            connect = Database.connectDB();

            try {

                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE product Id" + Inventory_ProductID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {

                    prepare = connect.prepareStatement(updateData);
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Succesfully Updated!");
                    alert.showAndWait();

                    inventoryShowData();
                    inventoryClearBtn();
                } else {

                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled");
                    alert.showAndWait();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void inventoryDeleteBtn() {
        if (data.path == null || data.id == 0) {

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the blank fields");
            alert.showAndWait();

        } else {
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to DELETE product Id  :  " + Inventory_ProductID.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                String deleteData = "DELETE FROM product WHERE id = " + data.id;
                try {
                    prepare = connect.prepareStatement(deleteData);
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Succesfully Deleted");
                    alert.showAndWait();

                    inventoryShowData();
                    inventoryClearBtn();
                } catch (Exception e) {
                    e.printStackTrace();

                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(" Cancelled");
                    alert.showAndWait();
                }
            }

        }
    }

    public void inventoryClearBtn() {
        Inventory_ProductID.getText();
        Inventory_ProductName.getText();
        Inventory_Type.getSelectionModel().clearSelection();
        Inventory_Stock.getText();
        Inventory_Price.getText();
        Inventory_Status.getSelectionModel().clearSelection();
        data.path = "";
        data.id = 0;
        inventory_imageView.setImage(null);
    }

    public void inventoryImportBtn() {
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new ExtensionFilter("Open Image File", "*.png", "*.jpg", "*.jpeg"));

        File file = openFile.showOpenDialog(main_form.getScene().getWindow());

        if (file != null) {
            data.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 143, 150, false, true);

            inventory_imageView.setImage(image);
        }
    }

    public ObservableList<productData> inventoryDataList() {
        ObservableList<productData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM product";

        connect = Database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            productData prodData;

            while (result.next()) {
                prodData = new productData(result.getInt("id"),
                        result.getString("prod_id"),
                        result.getString("prod_name"),
                        result.getString("type"),
                        result.getInt("stock"),
                        result.getDouble("price"),
                        result.getString("status"),
                        result.getString("image"),
                        result.getDate("date"));

                listData.add(prodData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

    private ObservableList<productData> inventoryListData;

    public void inventoryShowData() {
        inventoryListData = inventoryDataList();

        inventory_col_idProduct.setCellValueFactory(new PropertyValueFactory<>("productId"));
        inventory_col_ProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        inventory_col_Type.setCellValueFactory(new PropertyValueFactory<>("type"));
        inventory_col_Stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        inventory_col_Price.setCellValueFactory(new PropertyValueFactory<>("price"));
        inventory_col_Status.setCellValueFactory(new PropertyValueFactory<>("status"));
        inventory_col_Date.setCellValueFactory(new PropertyValueFactory<>("date"));

        inventory_tableView.setItems(inventoryListData);

    }

    public void inventorySelectData() {
        productData prodData = inventory_tableView.getSelectionModel().getSelectedItem();
        int num = inventory_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        Inventory_ProductID.setText(prodData.getProductId());
        Inventory_ProductName.setText(prodData.getProductName());
        Inventory_Stock.setText(String.valueOf(prodData.getStock()));
        Inventory_Price.setText(String.valueOf(prodData.getPrice()));

        data.path = prodData.getImage();

        String path = "File:" + prodData.getImage();
        data.date = String.valueOf(prodData.getDate());
        data.id = prodData.getId();

        image = new Image(path, 143, 150, false, true);
        inventory_imageView.setImage(image);

    }
    private String[] typeList = {"Meals", "Drinks"};

    public void inventoryTypeList() {

        List<String> typeL = new ArrayList<>();
        for (String data : typeList) {
            typeL.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(typeL);
        Inventory_Type.setItems(listData);
    }

    private String[] statusList = {"Available", "Unavailable"};

    public void inventoryStatusList() {

        List<String> statusL = new ArrayList<>();
        for (String data : statusList) {
            statusL.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(statusL);
        Inventory_Status.setItems(listData);
    }

    public ObservableList<productData> menuGetData() throws SQLException {
        String sql = "SELECT * FROM product";
        ObservableList<productData> listData = FXCollections.observableArrayList();
        connect = Database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            productData prod;

            while (result.next()) {
                prod = new productData(
                        result.getInt("id"), // Corrected here
                        result.getString("prod_id"),
                        result.getString("prod_name"),
                        result.getString("type"),
                        result.getInt("stock"),
                        result.getDouble("price"),
                        result.getString("image"),
                        result.getDate("date")
                );

                listData.add(prod);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources to avoid memory leaks
            if (result != null) {
                result.close();
            }
            if (prepare != null) {
                prepare.close();
            }
            if (connect != null) {
                connect.close();
            }
        }
        return listData;
    }

    public void menuDisplayCard() {
        cardListData.clear();
        try {
            cardListData.addAll(menuGetData());
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        int row = 0;
        int column = 0;

        menu_gridPane.getChildren().clear();
        menu_gridPane.getRowConstraints().clear();
        menu_gridPane.getColumnConstraints().clear();

        for (int q = 0; q < cardListData.size(); q++) {
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("cardProduct.fxml"));
                AnchorPane pane = load.load();
                cardProductController cardC = load.getController();
                cardC.setData(cardListData.get(q));

                if (column == 3) {
                    column = 0;
                    row += 1;
                }

                menu_gridPane.add(pane, column++, row); // increment column after use
                GridPane.setMargin(pane, new Insets(10));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ObservableList<productData> menuGetOrder() {
customerID();
        ObservableList<productData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM customer WHERE customer_id = " + cID;

        connect = Database.connectDB();

        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            productData prod;

            while (result.next()) {
                prod = new productData(result.getInt("id"),
                        result.getString("prod_id"),
                        result.getString("prod_name"),
                        result.getString("type"),
                        result.getInt("quantity"),
                        result.getDouble("price"),
                        result.getString("image"),
                        result.getDate("date"));
                listData.add(prod);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

    private ObservableList<productData> menuOrderListData;

public void menuShowOrderData() {
    // Retrieve the order data
    ObservableList<productData> orderData = menuGetOrder();

    // Set up the table columns and data
    menu_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
    menu_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    menu_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

    // Set the items in the table view to the order data
    menu_tableView.setItems(orderData);
}
    private double totalP;

    public void menuGetTotal() {
        customerID();
        String totalSQL = "SELECT SUM(price) FROM customer WHERE customer_id = ?";
        try (Connection connect = Database.connectDB();
                PreparedStatement prepare = connect.prepareStatement(totalSQL)) {
            prepare.setInt(1, cID);
            try (ResultSet result = prepare.executeQuery()) {
                if (result.next()) {
                    totalP = result.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<productData> menuListData;

    public void menushowData() {
        menuListData = menuGetOrder();

        menu_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        menu_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        menu_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        menu_tableView.setItems(cardListData);
    }

    public void menuDisplayTotal() {
        menuGetTotal();
        menu_total.setText("Rs" + totalP);
    }
   private double amount;
private double change;

public void menuAmount() {
    menuGetTotal();  // Get the total price

    // Check if amount is not empty and if it's a valid number
    if (menu_amount.getText().isEmpty() || totalP == 0) {
        showErrorMessage("Invalid amount or no order selected");
        return;
    }

    try {
        amount = Double.parseDouble(menu_amount.getText());
    } catch (NumberFormatException e) {
        showErrorMessage("Please enter a valid number for the amount");
        return;
    }

    // Validate the entered amount
    if (amount < totalP) {
        showErrorMessage("Amount entered is less than the total price");
        menu_amount.setText("");  // Clear the input if invalid
        return;
    }

    // Calculate the change and display it
    change = amount - totalP;
    menu_change.setText("Rs " + String.format("%.2f", change));
}

private void showErrorMessage(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error Message");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}

private void showConfirmationMessage(String message) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirmation");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}
public void menuPayBtn() {
    // Ensure the total price is calculated
    menuGetTotal();

    if (totalP == 0) {
        showErrorMessage("Please choose your order first.");
        return;
    }

    // Validate the amount before proceeding with payment
    menuAmount();  // Call this to set the 'amount' and handle input validation

    if (amount == 0) {
        return;  // Error handling is already managed in menuAmount(), no need for a message here
    }

    // Confirm the payment action with the user
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Payment Confirmation");
    alert.setHeaderText(null);
    alert.setContentText("Are you sure you want to proceed with the payment?");
    Optional<ButtonType> option = alert.showAndWait();

    if (option.isPresent() && option.get().equals(ButtonType.OK)) {
        try {
            customerID();  // Method to fetch customer ID
            menuGetTotal();  // Calculate total price

            // Define the SQL insert query with updated columns
            String insertPay = "INSERT INTO receipt (customer_id, total, date, em_username, amountPaid, change_amount, prod_name, quantity, price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            // Prepare the statement
            prepare = connect.prepareStatement(insertPay);

            // Set common values
            prepare.setInt(1, cID);  // Set customer ID
            prepare.setDouble(2, totalP);  // Set total price
            long currentTimeMillis = System.currentTimeMillis() / 1000;  // Convert to seconds since epoch
            prepare.setInt(3, (int) currentTimeMillis);  // Set the current date as an integer timestamp
            prepare.setString(4, data.username);  // Assuming data.username holds the employee username
            prepare.setDouble(5, amount);  // Amount paid by the customer
            prepare.setDouble(6, change);  // Change to give back

            // Loop through the items in the order
            for (productData item : cardListData) {
                // Set product details
                prepare.setString(7, item.getProductName());  // Set product name
                prepare.setInt(8, item.getQuantity());  // Set quantity
                prepare.setDouble(9, item.getPrice());  // Set price

                // Execute the insert for each item
                prepare.executeUpdate();
            }

            // Refresh order data
            menuShowOrderData();
            menuRestart();

            // Notify the user of success
            showConfirmationMessage("Payment successful!");

        } catch (SQLException e) {
            e.printStackTrace();
            showErrorMessage("Database error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("An unexpected error occurred: " + e.getMessage());
        }
    } else {
        showErrorMessage("Payment cancelled.");
    }
}

public void menuRestart() {
    totalP = 0;
    change = 0;
    amount = 0;
    
    menu_total.setText("");
            menu_amount.setText("");
            
            menu_change.setText("");
}

 @FXML
private void showReceipt(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("receipt.fxml"));
        Parent root = loader.load();

        receiptController receiptController = loader.getController();

        // Fetch the receipt ID or other necessary data
        int receiptId = 1; // Example ID
        receiptController.loadReceipt(receiptId);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Receipt");
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
        showErrorMessage("Failed to load receipt view.");
    }
}

 

    @FXML
    private void showReceipt(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("receipt.fxml"));
            Parent root = loader.load();

            receiptController receiptController = loader.getController();

            // Fetch the receipt ID or other necessary data
            int receiptId = 1; // Example ID
          receiptController.loadReceipt(receiptId);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Receipt");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Failed to load receipt view.");
        }
    }

  


private int cID;

    public void customerID() {

        String sql = "SELECT MAX(customer_id) FROM customer";
        connect = Database.connectDB();

        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                cID = result.getInt("MAX(customer_id)");

            }
            String checkCID = "SELECT MAX(customer_id) FROM receipt";
            prepare = connect.prepareStatement(checkCID);
            result = prepare.executeQuery();
            int checkID = 0;

            if (result.next()) {
                checkID = result.getInt("MAX(customer_id)");
            }
            if (cID == 0) {
                cID += 1;
            } else if (cID == checkID) {
                cID += 1;
            }

            data.cID = cID;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchForm(ActionEvent event) {
        if (event.getSource() == dashboard_Btn) {
            dashboard_form.setVisible(true);
            inventory_form.setVisible(false);
            menu_form.setVisible(false);
            
                  dashboardDisplayNC(); 
dashboardDisplayTI(); 
dashboardTotalI();
dashboardNSP();
dashboardIncomeChart();
dashboardCustomerChart();
            
        } else if (event.getSource() == inventory_Btn) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(true);
            menu_form.setVisible(false);
            inventoryTypeList();
            inventoryStatusList();
            inventoryShowData();

        } else if (event.getSource() == menu_Btn) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(false);
            menu_form.setVisible(true);

            menuDisplayCard();
             menuGetOrder();
            menuDisplayTotal();
               menuShowOrderData();
        }
    }

    public void logout() {
        try {
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {

                logout_Btn.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setTitle("Cafe Management System");
                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void displayUsername() {
        String user = data.username;
        user = user.substring(0, 1).toUpperCase() + user.substring(1);
        username.setText(user);
    }

    @Override
        public void initialize(URL url, ResourceBundle rb) {

        displayUsername();
        dashboardDisplayNC(); 
dashboardDisplayTI(); 
dashboardTotalI();
dashboardNSP();
dashboardIncomeChart();
dashboardCustomerChart();
        menuDisplayCard();
        menuDisplayTotal();
        menuShowOrderData();
        
        
       

    }

}
