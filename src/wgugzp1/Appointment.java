/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wgugzp1;

import java.time.ZonedDateTime;

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
    private ZonedDateTime start;
    private ZonedDateTime end;
    
    public Appointment(Customer customer, User user, String title, String description, String type, ZonedDateTime start, ZonedDateTime end) {
        
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
        
        
}
