/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wgugzp1;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author jakes
 */
public class CustomerController implements Initializable {
    private static boolean newCustomer = true;
    private static Customer customerInProcess;

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtAddress2;
    @FXML
    private TextField txtAddress1;
    @FXML
    private TextField txtCity;
    @FXML
    private TextField txtCountry;
    @FXML
    private TextField txtPostalCode;
    @FXML
    private TextField txtPhone;
    private Database db;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            db = Database.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(isNewCustomer());
        System.out.println(getCustomerInProcess());
        if (!isNewCustomer()) {
            txtName.setText(getCustomerInProcess().getName());
            txtAddress1.setText(getCustomerInProcess().getAddress().getAddress());
            txtAddress2.setText(getCustomerInProcess().getAddress().getAddress2());
            txtCity.setText(getCustomerInProcess().getAddress().getCity().getCity());
            txtCountry.setText(getCustomerInProcess().getAddress().getCity().getCountry().getCountry());
            txtPhone.setText(getCustomerInProcess().getAddress().getPhone());
            txtPostalCode.setText(getCustomerInProcess().getAddress().getPostalCode());
        }
    }  

    @FXML
    private void saveCustomer(ActionEvent event) throws SQLException {
        // Get all the fields
        String name = txtName.getText();
        String address1 = txtAddress1.getText();
        String address2 = txtAddress2.getText();
        String city = txtCity.getText();
        String country = txtCountry.getText();
        String phone = txtPhone.getText();
        String postalCode = txtPostalCode.getText();
        
        if (name.isEmpty() || address1.isEmpty() || city.isEmpty() || country.isEmpty() || phone.isEmpty() || postalCode.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all fields. Address2 is optional.");
            alert.showAndWait();
            return;
        }
        
        Country inputCountry = new Country(country);
        db.addCountry(inputCountry);
        
        City inputCity = new City(city, inputCountry);
        db.addCity(inputCity);
        
        Address inputAddress = new Address(address1, address2, postalCode, phone, inputCity);
        db.addAddress(inputAddress);
        
        if (isNewCustomer()) {
            setCustomerInProcess(new Customer(name, inputAddress));
            db.addCustomer(getCustomerInProcess());
        } else {
            Customer c = getCustomerInProcess();
            c.setAddress(inputAddress);
            c.setName(name);
            c.update();
        }
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Customer Saved");
        alert.setHeaderText(null);
        alert.setContentText("The customer you input has been saved.");
        alert.showAndWait();
        ((Stage) txtName.getScene().getWindow()).close();
    }

    @FXML
    private void cancelCustomer(ActionEvent event) {
        ((Stage) txtName.getScene().getWindow()).close();
    }

    /**
     * @return the newCustomer
     */
    public static boolean isNewCustomer() {
        return newCustomer;
    }

    /**
     * @param newCustomer the newCustomer to set
     */
    public static void setNewCustomer(boolean aNewCustomer) {
        newCustomer = aNewCustomer;
    }

    /**
     * @return the customerInProcess
     */
    public static Customer getCustomerInProcess() {
        return customerInProcess;
    }

    /**
     * @param aCustomerInProcess the customerInProcess to set
     */
    public static void setCustomerInProcess(Customer aCustomerInProcess) {
        customerInProcess = aCustomerInProcess;
    }
    
}
