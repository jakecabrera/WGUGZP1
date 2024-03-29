/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wgugzp1;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

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
    
    private static boolean newAppointment = true;
    private Database db;
    private static Appointment appointmentInProcess;

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
        
        List<String> hours = new ArrayList<>();
        List<String> minutes = new ArrayList<>();
        for (int i = 1; i <= 12; i++) hours.add((("" + i).length() == 1)? "0" + i: "" + i);
        for (int i = 0; i <= 59; i++) minutes.add((("" + i).length() == 1)? "0" + i: "" + i);
        hourStart.setItems(FXCollections.observableArrayList(hours));
        hourEnd.setItems(FXCollections.observableArrayList(hours));
        minuteStart.setItems(FXCollections.observableArrayList(minutes));
        minuteEnd.setItems(FXCollections.observableArrayList(minutes));
        periodStart.setItems(FXCollections.observableArrayList(Arrays.asList("AM", "PM")));
        periodEnd.setItems(FXCollections.observableArrayList(Arrays.asList("AM", "PM")));
        
        lblSelection.setText("");
        
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
        
        System.out.println(db.getCustomers().size());
        tblCustomer.getItems().addAll(db.getCustomers().values());
        
        if (!isNewAppointment()) {
            Appointment a = getAppointmentInProcess();
            txtTitle.setText(a.getTitle());
            txtDescription.setText(a.getDescription());
            txtContact.setText(a.getContact());
            txtLocation.setText(a.getLocation());
            txtType.setText(a.getType());
            txtURL.setText(a.getURL());
            tblCustomer.getSelectionModel().select(a.getCustomer());
            
            dateStart.setValue(a.getStart().withZoneSameInstant(ZoneId.systemDefault()).toLocalDate());
            LocalTime t = a.getStart().withZoneSameInstant(ZoneId.systemDefault()).toLocalTime();
            int hour = t.getHour() % 12;
            if (hour == 0) hour = 12;
            int minute = t.getMinute() % 60;
            String period = (t.getHour() - 12 >= 0)? "PM": "AM";
            String h = "" + hour;
            String m = "" + minute;
            if (h.length() == 1) h = "0" + h;
            if (m.length() == 1) m = "0" + m;
            hourStart.setValue(h);
            minuteStart.setValue(m);
            periodStart.setValue(period);
            
            dateEnd.setValue(a.getEnd().withZoneSameInstant(ZoneId.systemDefault()).toLocalDate());
            t = a.getEnd().withZoneSameInstant(ZoneId.systemDefault()).toLocalTime();
            hour = t.getHour() % 12;
            if (hour == 0) hour = 12;
            minute = t.getMinute() % 60;
            period = (t.getHour() - 12 >= 0)? "PM": "AM";
            h = "" + hour;
            m = "" + minute;
            if (h.length() == 1) h = "0" + h;
            if (m.length() == 1) m = "0" + m;
            hourEnd.setValue(h);
            minuteEnd.setValue(m);
            periodEnd.setValue(period);
        }
    }    

    @FXML
    private void saveAppointment(ActionEvent event) throws SQLException{
        String title = txtTitle.getText();
        String description = txtDescription.getText();
        Customer customer = (Customer) tblCustomer.getSelectionModel().getSelectedItem();
        String location = txtLocation.getText();
        String contact = txtContact.getText();
        String type = txtType.getText();
        String url = txtURL.getText();
        
        // set up start and end ZonedDateTime's
        int hour = Integer.parseInt((String) hourStart.getValue());
        LocalDate s = dateStart.getValue();
        if (((String) periodStart.getValue()).equals("PM") && hour != 12) {
            hour += 12;
            if (hour >= 24) {
                hour -= 24;
                s.plusDays(1);
            }
        }
        int minute = Integer.parseInt((String) minuteStart.getValue());
        LocalTime time = LocalTime.of(hour, minute);
        LocalDateTime localDateTime = LocalDateTime.of(s, time);
        ZonedDateTime start = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        
        hour = Integer.parseInt((String) hourEnd.getValue());
        LocalDate e = dateEnd.getValue();
        if (((String) periodEnd.getValue()).equals("PM") && hour != 12) {
            hour += 12;
            if (hour >= 24) {
                hour -= 24;
                e.plusDays(1);
            }
        }
        minute = Integer.parseInt((String) minuteEnd.getValue());
        time = LocalTime.of(hour, minute);
        LocalDateTime localDateTime2 = LocalDateTime.of(e, time);
        ZonedDateTime end = ZonedDateTime.of(localDateTime2, ZoneId.systemDefault());
        
        // Set up contact
        if (contact.isEmpty()) contact = db.getLoggedInUser().getUserName();
        
        // Exception handling
        
        // Check if this appointment overlaps with existing appointments
        List<Appointment> list = new ArrayList<>(db.getAppointments().values());
        list.removeIf(x -> x == getAppointmentInProcess()); // This lambda is so much easier than writing it out without one
        if (db.overlapExists(start, end, list)) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Appointment Overlap");
            alert.setHeaderText(null);
            alert.setContentText("The time and date specified for this appointment "
                    + "overlaps another appointment that already exists.");
            alert.showAndWait();
            return;
        }
        
        // Check if this appointment is within business hours
        int openHour = 9;
        int closeHour = 17;
        boolean startsDuringBusiness = start.getHour() < closeHour && start.getHour() >= openHour;
        boolean endsDuringBusiness = (end.getHour() < closeHour || (end.getHour() == closeHour 
                && end.getMinute() == 0)) && end.getHour() >= openHour; 
        if (!(startsDuringBusiness && endsDuringBusiness)) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Outside of Business Hours");
            alert.setHeaderText(null);
            alert.setContentText("The time specified for this appointment "
                    + "is outside of business hours. Please schedule the appointment "
                    + "between 9am and 5pm local time.");
            alert.showAndWait();
            return;
        }
        
        // start and end need to be in GMT for saving
        end = end.withZoneSameInstant(ZoneId.of("GMT"));
        start = start.withZoneSameInstant(ZoneId.of("GMT"));
        
        // Update or save appointment
        if (isNewAppointment()) {
            appointmentInProcess = new Appointment(customer, db.getLoggedInUser(), title, description, location, contact, type, url, start, end);
            db.addAppointment(appointmentInProcess);
        } else {
            Appointment a = getAppointmentInProcess();
            a.setCustomer(customer);
            a.setTitle(title);
            a.setDescription(description);
            a.setLocation(location);
            a.setContact(contact);
            a.setType(type);
            a.setURL(url);
            a.setStart(start);
            a.setEnd(end);
            a.update();
        }
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Appointment Saved");
        alert.setHeaderText(null);
        alert.setContentText("The appointment you input has been saved.");
        alert.showAndWait();
        ((Stage) txtLocation.getScene().getWindow()).close();
    }

    @FXML
    private void cancelAppointment(ActionEvent event) {
        ((Stage) txtLocation.getScene().getWindow()).close();
    }

    @FXML
    private void searchCustomer(ActionEvent event) {
        List<Customer> list = new ArrayList<>(db.getCustomers().values());
        list.removeIf(x -> !x.getName().contains(txtCustomer.getText())); // Using a lambda with List.removeIf is way convenient compared to how much I'd have to write if I didn't use a lambda
        tblCustomer.getItems().clear();
        tblCustomer.getItems().addAll(list);
    }

    /**
     * @return the newAppointment
     */
    public static boolean isNewAppointment() {
        return newAppointment;
    }

    /**
     * @param newAppointment the newAppointment to set
     */
    public static void setNewAppointment(boolean aNewAppointment) {
        newAppointment = aNewAppointment;
    }

    /**
     * @return the appointmentInProcess
     */
    public static Appointment getAppointmentInProcess() {
        return appointmentInProcess;
    }

    /**
     * @param aAppointmentInProcess the appointmentInProcess to set
     */
    public static void setAppointmentInProcess(Appointment aAppointmentInProcess) {
        appointmentInProcess = aAppointmentInProcess;
    }
}
