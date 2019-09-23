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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author jakes
 */
public class AppointmentController implements Initializable {

    @FXML
    private TextField txtTitle;
    @FXML
    private TextArea txtDescription;
    @FXML
    private TextField txtCustomer;
    @FXML
    private Label lblSelection;
    @FXML
    private Button btnDelete;
    @FXML
    private TextField txtLocation;
    @FXML
    private TextField txtContact;
    @FXML
    private TextField txtType;
    @FXML
    private TextField txtURL;
    @FXML
    private DatePicker dateStart;
    @FXML
    private ChoiceBox hourStart;
    @FXML
    private ChoiceBox minuteStart;
    @FXML
    private ChoiceBox periodStart;
    @FXML
    private DatePicker dateEnd;
    @FXML
    private ChoiceBox hourEnd;
    @FXML
    private ChoiceBox minuteEnd;
    @FXML
    private ChoiceBox periodEnd;
    @FXML
    private TableView tblCustomer;
    
    private boolean newAppointment = true;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        lblSelection.setText("");
        btnDelete.setDisable(isNewAppointment());
        btnDelete.setVisible(!isNewAppointment());
        
        TableColumn<String, Customer> columnId = new TableColumn<>("ID");
        columnId.setCellValueFactory(new PropertyValueFactory<>("idAsInt"));
        
        TableColumn<String, Customer> columnName = new TableColumn<>("Name");
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<String, Customer> columnCity = new TableColumn<>("City");
        columnCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        
        TableColumn<String, Customer> columnCountry = new TableColumn<>("Country");
        columnCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        
        tblCustomer.getColumns().add(columnId);
        tblCustomer.getColumns().add(columnName);
        tblCustomer.getColumns().add(columnCity);
        tblCustomer.getColumns().add(columnCountry);
        
        try {
            System.out.println(Database.getInstance().getCustomers().size());
            tblCustomer.getItems().addAll(Database.getInstance().getCustomers().values());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }    

    @FXML
    private void deleteAppointment(ActionEvent event) {
    }

    @FXML
    private void saveAppointment(ActionEvent event) {
    }

    @FXML
    private void cancelAppointment(ActionEvent event) {
    }

    @FXML
    private void searchCustomer(ActionEvent event) {
    }

    /**
     * @return the newAppointment
     */
    public boolean isNewAppointment() {
        return newAppointment;
    }

    /**
     * @param newAppointment the newAppointment to set
     */
    public void setNewAppointment(boolean newAppointment) {
        this.newAppointment = newAppointment;
    }
    
}
