import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class receiptController {

    @FXML
    private Label receipt_productName;

    @FXML
    private Label receipt_quantity;

    @FXML
    private Label receipt_price;

    @FXML
    private Label receipt_totalPrice;

    @FXML
    private Label receipt_amountPaid;

    @FXML
    private Label receipt_change;

    @FXML
    private Button printReceiptBtn;

    // Method to load the receipt data from the database and display in the labels
     public void loadReceipt(int receiptId) {
        String query = "SELECT * FROM receipt ORDER BY id DESC LIMIT 1";
        Connection connect = null;
        PreparedStatement prepare = null;
        ResultSet result = null;

        try {
            // Use the same database connection method as mainController
            connect = Database.connectDB();
            prepare = connect.prepareStatement(query);

            result = prepare.executeQuery();

            if (result.next()) {
                // Assuming these columns exist in the receipt table
                String productName = result.getString("prod_name");
             
                double price = result.getDouble("price");
                double totalPrice = result.getDouble("total");
                double amountPaid = result.getDouble("amountPaid");
                double change = result.getDouble("change_amount");
                // Set the values in the labels
                receipt_productName.setText("Product Name: " + productName);
               
                receipt_price.setText("Price: Rs. " + price);
                receipt_totalPrice.setText("Total Price: Rs. " + totalPrice);
                receipt_amountPaid.setText("Amount Paid: Rs. " + amountPaid);
                receipt_change.setText("Change: Rs. " + change);
            } else {
                // If no receipt is found
                showAlert("Receipt not found", "No receipt found in the table.");
            }

        } catch (SQLException e) {
            showAlert("Error", "Unable to load receipt data. Please try again.");
            e.printStackTrace();
        } finally {
            // Close resources in the finally block to avoid resource leaks
            try {
                if (result != null) result.close();
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Action method for the "Print Receipt" button
    @FXML
    private void printReceipt() {
        // For simplicity, we will show a confirmation alert. In real scenarios, this would trigger actual printing.
        showAlert("Print", "Receipt is being printed!");
    }

    // Utility method to show an alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null); // No header text
        alert.setContentText(message);
        alert.showAndWait();
    }
}
