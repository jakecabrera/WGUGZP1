/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wgugzp1;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author jakes
 */
public class AppointmentController implements Initializable {

    @FXML
    private TextField txtCity;
    @FXML
    private TextField txtCountry;
    @FXML
    private TextField txtPostalCode;
    @FXML
    private TextField txtPhone;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
    
}
