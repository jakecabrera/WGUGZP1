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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author jakes
 */
public class CustomerController implements Initializable {
    private boolean newCustomer = true;

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
    @FXML
    private Button btnDelete;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnDelete.setDisable(newCustomer);
        btnDelete.setVisible(!newCustomer);
    }    

    @FXML
    private void deleteCustomer(ActionEvent event) {
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
        
        Database db = Database.getInstance();
        Country inputCountry = new Country(country);
        db.addCountry(inputCountry);
        
        City inputCity = new City(city, inputCountry);
        db.addCity(inputCity);
        
        Address inputAddress = new Address(address1, address2, postalCode, phone, inputCity);
        db.addAddress(inputAddress);
        
        Customer inputCustomer = new Customer(name, inputAddress);
        db.addCustomer(inputCustomer);
    }

    @FXML
    private void cancelCustomer(ActionEvent event) {
    }
    
}
