/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wgugzp1;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 *
 * @author jakes
 */
public class Login implements Initializable {
    
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private VBox vbox;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // This is so that now text field is automatically focused when loaded
        final BooleanProperty firstTime = new SimpleBooleanProperty(true);
        txtUsername.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue && firstTime.get()) {
                vbox.requestFocus();
                firstTime.setValue(false);                
            }
        });
    }    
    
    public void login(ActionEvent event) throws SQLException {
        // Get all users with the username
        Connection db = Schedule.getDbInstance();
        PreparedStatement ps = db.prepareStatement("select * from user where userName = ? COLLATE latin1_general_cs and active > 0 and password = ? COLLATE latin1_general_cs");
        ps.setString(1, txtUsername.getText());
        ps.setString(2, txtPassword.getText());
        ps.execute();
        ResultSet results = ps.getResultSet();
        
        // Check for any matching users
        if (results.next()) passLogin();
        else failLogin();
        ps.close();
    }
    
    public void failLogin() {
        System.out.println("Fail");
    }
    
    public void passLogin() {
        System.out.println("Pass");
    }
}