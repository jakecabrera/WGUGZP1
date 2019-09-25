/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wgugzp1;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author jakes
 */
public class Appointment extends Record {
    private Customer customer;
    private User user;
    private String title;
    private String description;
    private String contact;
    private String URL;
    private String type;
    private String location;
    private ZonedDateTime start;
    private ZonedDateTime end;
    private static int offset = 0;
    private static boolean applyDST = false;
    
    public Appointment(Customer customer, User user, String title, String description, String location, String contact, String type, String url, ZonedDateTime start, ZonedDateTime end) {
        setCustomer(customer);
        setUser(user);
        setTitle(title);
        setDescription(description);
        setLocation(location);
        setContact(contact);
        setType(type);
        setURL(url);
        setStart(start);
        setEnd(end);
    }
    
    public Appointment(Customer customer, User user, String title, String description, String location, String contact, String type, String url, ZonedDateTime start, ZonedDateTime end, int id) {
        this(customer, user, title, description, location, contact, type, url, start, end);
        setId(id);
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the contact
     */
    public String getContact() {
        return contact;
    }

    /**
     * @param contact the contact to set
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * @return the URL
     */
    public String getURL() {
        return URL;
    }

    /**
     * @param URL the URL to set
     */
    public void setURL(String URL) {
        this.URL = URL;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the start
     */
    public ZonedDateTime getStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    /**
     * @return the end
     */
    public ZonedDateTime getEnd() {
        return end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }
        
    public void pushToDatabase() throws SQLException {
        Database db = Database.getInstance();
        
        PreparedStatement s = Schedule.getDbInstance().prepareStatement("insert into "
                + "appointment(customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        
        s.setInt(1, getCustomer().getIdAsInt());
        s.setInt(2, db.getLoggedInUser().getId().orElseThrow(RuntimeException::new));
        s.setString(3, getTitle());
        s.setString(4, getDescription());
        s.setString(5, getLocation());
        s.setString(6, getContact());
        s.setString(7, getType());
        s.setString(8, getURL());
        s.setString(9, "" + getStart().withZoneSameInstant(ZoneId.of("GMT")).toLocalDateTime());
        s.setString(10, "" + getEnd().withZoneSameInstant(ZoneId.of("GMT")).toLocalDateTime());
        s.setString(11, "" + getCreateDate().withZoneSameInstant(ZoneId.of("GMT")).toLocalDateTime());
        s.setString(12, getCreatedBy());
        s.setString(13, "" + Timestamp.from(getLastUpdate().withZoneSameInstant(ZoneId.of("GMT")).toInstant()));
        s.setString(14, getLastUpdateBy());
        s.executeUpdate();
        s.close();
        s = Schedule.getDbInstance().prepareStatement("select appointmentId from appointment where "
                + "title = ? COLLATE latin1_general_cs and "
                + "customerId = ? COLLATE latin1_general_cs and "
                + "userId = ? COLLATE latin1_general_cs "
                + "group by appointmentId");
        s.setString(1, getTitle());
        s.setInt(2, getCustomer().getId().orElseThrow(RuntimeException::new));
        s.setInt(3, db.getLoggedInUser().getId().orElseThrow(RuntimeException::new));
        
        ResultSet r = s.executeQuery();
        while (r.next()) {
            setId(r.getInt("appointmentId"));
        }
        s.close();
        System.out.println(getId().get());
    }
    
    public void update() throws SQLException {
        PreparedStatement s = Schedule.getDbInstance().prepareStatement("update appointment "
                + "set customerId = ?, title = ?, description = ?, location = ?, "
                + "contact = ?, type = ?, url = ?, start = ?, end = ?, lastUpdate = ?, "
                + "lastUpdateBy = ? "
                + "where appointmentId = ? ");
        s.setInt(1, getCustomer().getIdAsInt());
        s.setString(2, getTitle());
        s.setString(3, getDescription());
        s.setString(4, getLocation());
        s.setString(5, getContact());
        s.setString(6, getType());
        s.setString(7, getURL());
        s.setString(8, "" + getStart().withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime());
        System.out.println("End DAte: " + getEnd().withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime());
        s.setString(9, "" + getEnd().withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime());
        s.setString(10, "" + ZonedDateTime.now().withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime());
        s.setString(11, Database.getInstance().getLoggedInUser().getUserName());
        
        s.setInt(12, getId().orElseThrow(RuntimeException::new));
        s.executeUpdate();
    }
    
    public String getCustomerName() {
        return getCustomer().getName();
    }
    
    public String getStartFormatted() {
        String output = "";
        ZoneOffset offset = ZoneOffset.ofTotalSeconds(getOffset());
        ZoneId zone = ZoneId.ofOffset("", offset);
        ZonedDateTime t = getStart().withZoneSameInstant(zone);
        output += t.format(DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm"));
        return output;
    }
    
    @Override
    public String toString() {
        String output = "";
        output += "id: " + getId();
        output += "; title: " + getTitle();
        output += "; contact: " + getContact();
        output += "; Customer[[" + getCustomer();
        return output;
    }
    
    public static void setOffset(int anOffset) {
        System.out.println("Setting offset to " + anOffset);
        offset = anOffset;
    }
    
    public static int getOffset() {
        if (getApplyDST()) return offset + (60 * 60);
        return offset;
    }

    /**
     * @return the applyDST
     */
    public static boolean getApplyDST() {
        return applyDST;
    }

    /**
     * @param aApplyDST the applyDST to set
     */
    public static void setApplyDST(boolean aApplyDST) {
        applyDST = aApplyDST;
    }
    
    public String toConsultantString() {
        String output = "";
        output += "Contact: [" + getContact() + "] ";
        output += "Start: " + getStart().format(DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm"));
        output += " End: " + getEnd().format(DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm"));
        return output;
    }
}
