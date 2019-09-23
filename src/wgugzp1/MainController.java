/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wgugzp1;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author jakes
 */
public class MainController implements Initializable {

    @FXML
    private TableView tblCustomers;
    @FXML
    private TableView tblAppointments;
    @FXML
    private Slider sldTimeZone;
    @FXML
    private Label lblOffset;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableColumn<String, Customer> columnId = new TableColumn<>("ID");
        columnId.setCellValueFactory(new PropertyValueFactory<>("idAsInt"));
        
        TableColumn<String, Customer> columnName = new TableColumn<>("Name");
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<String, Customer> columnCity = new TableColumn<>("City");
        columnCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        
        TableColumn<String, Customer> columnCountry = new TableColumn<>("Country");
        columnCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        
        tblCustomers.getColumns().add(columnId);
        tblCustomers.getColumns().add(columnName);
        tblCustomers.getColumns().add(columnCity);
        tblCustomers.getColumns().add(columnCountry);
        
        try {
            System.out.println(Database.getInstance().getCustomers().size());
            tblCustomers.getItems().addAll(Database.getInstance().getCustomers().values());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }    

    @FXML
    private void customerAdd(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Customer.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            CustomerController.setCustomerInProcess(null);
            CustomerController.setNewCustomer(true);
            stage.showAndWait();
            System.out.println(Database.getInstance().getCustomers().size());
            tblCustomers.getItems().clear();
            tblCustomers.getItems().addAll(Database.getInstance().getCustomers().values());
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void customerEdit(ActionEvent event) {
        Customer selection = ((Customer) tblCustomers.getSelectionModel().getSelectedItem());
        if (selection == null) return;
        try {
            CustomerController.setCustomerInProcess(selection);
            CustomerController.setNewCustomer(false);
            Parent root = FXMLLoader.load(getClass().getResource("Customer.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
            System.out.println(Database.getInstance().getCustomers().size());
            tblCustomers.getItems().clear();
            tblCustomers.getItems().addAll(Database.getInstance().getCustomers().values());
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void customerDelete(ActionEvent event) throws SQLException {
        Object obj = tblCustomers.getSelectionModel().getSelectedItem();
        if (obj instanceof Customer) {
            Database.getInstance().deleteCustomer((Customer) obj);
            System.out.println(Database.getInstance().getCustomers().size());
            tblCustomers.getItems().clear();
            tblCustomers.getItems().addAll(Database.getInstance().getCustomers().values());
        }
    }

    @FXML
    private void appointmentAdd(ActionEvent event) {
    }

    @FXML
    private void appointmentEdit(ActionEvent event) {
    }

    @FXML
    private void appointmentDelete(ActionEvent event) {
    }
    
}
