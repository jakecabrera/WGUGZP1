/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wgugzp1;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
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
    private Database db;
    @FXML
    private RadioButton rdbtnMonth;
    @FXML
    private ToggleGroup window;
    @FXML
    private RadioButton rdBtnWeek;
    @FXML
    private CheckBox chkDst;

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
        
        System.out.println(db.getCustomers().size());
        tblCustomers.getItems().addAll(db.getCustomers().values());
        
        // Set up appointments        
        TableColumn<String, Appointment> columnCustomer = new TableColumn<>("Customer");
        columnCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        
        TableColumn<String, Appointment> columnContact = new TableColumn<>("Contact");
        columnContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        
        TableColumn<String, Appointment> columnTitle = new TableColumn<>("Title");
        columnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        
        TableColumn<String, Appointment> columnStart = new TableColumn<>("Start");
        columnStart.setCellValueFactory(new PropertyValueFactory<>("startFormatted"));
        
        tblAppointments.getColumns().add(columnStart);
        tblAppointments.getColumns().add(columnCustomer);
        tblAppointments.getColumns().add(columnContact);
        tblAppointments.getColumns().add(columnTitle);
        
        updateTblAppointments();
        
        window.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            db.setOffsetMonths(0);
            db.setOffsetWeeks(0);
            updateTblAppointments();
        });
        
        sldTimeZone.valueProperty().addListener((obsVal, oldVal, newVal) -> {
            if (sldTimeZone.valueChangingProperty().getValue()) return;
            int hours = (int) newVal.doubleValue();
            int modifier = (newVal.doubleValue() < 0)? -1: 1;
            hours = hours * modifier;
            String h = (("" + hours).length() == 1)? "0" + hours: "" + hours;
            h = (modifier < 0)? "-" + h: "+" + h;
            int minutes = (int)((newVal.doubleValue() - (hours * modifier)) * 60) * modifier;
            String m = (("" + minutes).length() == 1)? "0" + minutes: "" + minutes;
            lblOffset.setText("" + h + ":" + m);
            Appointment.setOffset((int) (newVal.doubleValue() * 60 * 60));
            updateTblAppointments();
        });
        sldTimeZone.setValue(((double) ZonedDateTime.now().getOffset().getTotalSeconds())/(60.0 * 60.0));
        checkAppointmentIn15();
    }    

    @FXML
    private void customerAdd(ActionEvent event) {
        if (db == null) return;
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Customer.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            CustomerController.setCustomerInProcess(null);
            CustomerController.setNewCustomer(true);
            stage.showAndWait();
            System.out.println(db.getCustomers().size());
            tblCustomers.getItems().clear();
            tblCustomers.getItems().addAll(db.getCustomers().values());
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void customerEdit(ActionEvent event) {
        Customer selection = ((Customer) tblCustomers.getSelectionModel().getSelectedItem());
        if (selection == null || db == null) return;
        try {
            CustomerController.setCustomerInProcess(selection);
            CustomerController.setNewCustomer(false);
            Parent root = FXMLLoader.load(getClass().getResource("Customer.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
            System.out.println(db.getCustomers().size());
            tblCustomers.getItems().clear();
            tblCustomers.getItems().addAll(db.getCustomers().values());
            
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    @FXML
    private void customerDelete(ActionEvent event) throws SQLException {
        if (db == null) return;
        Object obj = tblCustomers.getSelectionModel().getSelectedItem();
        if (obj instanceof Customer) {
            db.deleteCustomer((Customer) obj);
            System.out.println(db.getCustomers().size());
            tblCustomers.getItems().clear();
            tblCustomers.getItems().addAll(db.getCustomers().values());
        }
    }

    @FXML
    private void appointmentAdd(ActionEvent event) {
        if (db == null) return;
        try {
            AppointmentController.setAppointmentInProcess(null);
            AppointmentController.setNewAppointment(true);
            Parent root = FXMLLoader.load(getClass().getResource("Appointment.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
            updateTblAppointments();
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    @FXML
    private void appointmentEdit(ActionEvent event) {
        Appointment selection = (Appointment) tblAppointments.getSelectionModel().getSelectedItem();
        if (selection == null || db == null) return;
        try {
            AppointmentController.setAppointmentInProcess(selection);
            AppointmentController.setNewAppointment(false);
            Parent root = FXMLLoader.load(getClass().getResource("Appointment.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
            updateTblAppointments();
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    @FXML
    private void appointmentDelete(ActionEvent event) throws SQLException{
        if (db == null) return;
        Object obj = tblAppointments.getSelectionModel().getSelectedItem();
        if (obj instanceof Appointment) {
            db.deleteAppointment((Appointment) obj);
            System.out.println(getAppointmentWindow());
            tblAppointments.getItems().clear();
            tblAppointments.getItems().addAll(getAppointmentWindow());
        }
    }
    
    private List<Appointment> getAppointmentWindow() {
        if (rdbtnMonth.selectedProperty().getValue()) {
            return db.getAppointmentsByMonth();
        } else {
            return db.getAppointmentsByWeek();
        }
    }

    @FXML
    private void previousWindow(ActionEvent event) {
        if (rdbtnMonth.selectedProperty().getValue()) {
            db.setOffsetMonths(db.getOffsetMonths() - 1);
        } else {
            db.setOffsetWeeks(db.getOffsetWeeks() - 1);
        }
        tblAppointments.getItems().clear();
        tblAppointments.getItems().addAll(getAppointmentWindow());
    }

    @FXML
    private void nextWindow(ActionEvent event) {
        if (rdbtnMonth.selectedProperty().getValue()) {
            db.setOffsetMonths(db.getOffsetMonths() + 1);
        } else {
            db.setOffsetWeeks(db.getOffsetWeeks() + 1);
        }
        tblAppointments.getItems().clear();
        tblAppointments.getItems().addAll(getAppointmentWindow());
    }

    @FXML
    private void getConsultantSchedule(ActionEvent event) {
    }

    @FXML
    private void getApptsTypesPerMonth(ActionEvent event) {
    }

    @FXML
    private void getUserActivity(ActionEvent event) {
    }
    
    private void checkAppointmentIn15() {
        ZonedDateTime now = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("GMT"));
        long t = now.toEpochSecond();
        long interval = 60 * 15;
        for (Appointment a: db.getAppointmentsByWeek()) {
            long start = a.getStart().toEpochSecond();
            if (t + interval > start && a.getStart().isAfter(now)) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Upcoming Appointment");
                alert.setHeaderText(null);
                alert.setContentText("You have an appointment within 15 minutes!");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void daylightSavingsTime(ActionEvent event) {
        Appointment.setApplyDST(chkDst.selectedProperty().get());
        updateTblAppointments();
    }
    
    private void updateTblAppointments() {
        tblAppointments.getItems().clear();
        tblAppointments.getItems().addAll(getAppointmentWindow());
    }
}
