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
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 *
 * @author jakes
 */
public class Customer extends Record {
    private String name;
    private int active = 1;
    private Address address;

    public Customer(String name, Address address) {
        setName(name);
        setAddress(address);
    }
    
    public Customer(String name, Address address, User creator) {
        this(name, address);
        setCreatedBy(creator.getUserName());
        setCreateDate(ZonedDateTime.now(ZoneId.of("GMT")));
    }
    
    public Customer(String name, Address address, int active, int id) {
        this(name, address);
        setActive(active);
        setId(id);
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        if (name.length() > 45) throw new IllegalArgumentException();
        this.name = name;
    }

    /**
     * @return the active
     */
    public int getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(int active) {
        this.active = active;
    }

    /**
     * @return the address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(Address address) {
        this.address = address;
    }
    
    /**
     * @return the address id
     */
    public Optional<Integer> getAddressId() {
        return address.getId();
    }
    
    public int getIdAsInt() {
        return getId().orElseThrow(RuntimeException::new);
    }
    
    public String getCity() {
        return getAddress().getCity().getCity();
    }

    public String getCountry() {
        return getAddress().getCity().getCountry().getCountry();
    }
    
    public void pushToDatabase() throws SQLException {
        PreparedStatement s = Schedule.getDbInstance().prepareStatement("insert into "
                + "customer(customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "values(?, ?, ?, ?, ?, ?, ?)");
        
        s.setString(1, getName());
        s.setString(2, "" + getAddressId().orElseThrow(RuntimeException::new));
        s.setString(3, "" + getActive());
        s.setString(4, "" + getCreateDate().toLocalDateTime());
        s.setString(5, getCreatedBy());
        s.setString(6, "" + Timestamp.from(getLastUpdate().toInstant()));
        s.setString(7, getLastUpdateBy());
        s.executeUpdate();
        s.close();
        s = Schedule.getDbInstance().prepareStatement("select customerId from customer where "
                + "customerName = ? COLLATE latin1_general_cs and "
                + "addressId = ? COLLATE latin1_general_cs");
        s.setString(1, getName());
        s.setString(2, "" + getAddressId().orElseThrow(RuntimeException::new));
        ResultSet r = s.executeQuery();
        while (r.next()) {
            setId(r.getInt("customerId"));
        }
        s.close();
        System.out.println(getId().get());
    }
    
    public void update() throws SQLException {
        PreparedStatement s = Schedule.getDbInstance().prepareStatement("update customer "
                + "set customerName = ?, addressId = ?, lastUpdate = ?, lastUpdateBy = ? "
                + "where customerId = ? ");
        s.setInt(5, getIdAsInt());
        s.setString(1, getName());
        s.setInt(2, getAddressId().orElseThrow(RuntimeException::new));
        s.setString(3, "" + ZonedDateTime.now(ZoneId.of("GMT")).toLocalDateTime());
        s.setString(4, Database.getInstance().getLoggedInUser().getUserName());
        
        s.executeUpdate();
    }
    
    @Override
    public String toString() {
        String output = "";
        output += "customerId: " + getId();
        output += "; customerName: " + getName();
        output += "; " + getAddress();
        return output;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Customer)) return false;
        Customer c = (Customer) obj;
        return c.getName().equals(getName()) && c.getAddressId().equals(getAddressId());
    }
    
}
