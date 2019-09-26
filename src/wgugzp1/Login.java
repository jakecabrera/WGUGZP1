/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wgugzp1;

import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
        txtUsername.focusedProperty().addListener((observable, oldValue, newValue) -> { // Lambda here is slightly shorter than making a new changeListener
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
        if (results.next()) {
            passLogin(results.getInt("userId"));
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        else failLogin();
        ps.close();
    }
    
    public void failLogin() throws SQLException{
        Database.getInstance().recordLogIn(txtUsername.getText(), false);
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Failed Login");
        alert.setHeaderText(null);
        alert.setContentText("You have entered an invalid Username or Password.");
        alert.showAndWait();
        System.out.println("Fail");
    }
    
    public void passLogin(int id) {
        try {
            Database db = Database.getInstance();
            db.recordLogIn(txtUsername.getText(), true);
            User user = db.getUsers().get(id);
            db.setLoggedInUser(user);
            
            System.out.println("Pass");

            Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
