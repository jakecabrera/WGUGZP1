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
import java.util.Optional;

/**
 *
 * @author jakes
 */
public class Address extends Record {
    private String address;
    private String address2;
    private String postalCode;
    private String phone;
    private City city;
    
    public Address(String address, String address2, String postalCode, String phone, City city) {
        setAddress(address);
        setAddress2(address2);
        setPostalCode(postalCode);
        setPhone(phone);
        setCity(city);
    }
    
    public Address(String address, String address2, String postalCode, String phone, City city, int id) {
        this(address, address2, postalCode, phone, city);
        setId(id);
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        if (address.length() > 50) throw new IllegalArgumentException();
        this.address = address;
    }

    /**
     * @return the address2
     */
    public String getAddress2() {
        return address2;
    }

    /**
     * @param address2 the address2 to set
     */
    public void setAddress2(String address2) {
        if (address2.length() > 50) throw new IllegalArgumentException();
        this.address2 = address2;
    }

    /**
     * @return the postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode the postalCode to set
     */
    public void setPostalCode(String postalCode) {
        if (postalCode.length() > 10) throw new IllegalArgumentException();
        this.postalCode = postalCode;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        if (phone.length() > 20) throw new IllegalArgumentException();
        this.phone = phone;
    }

    /**
     * @return the city
     */
    public City getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(City city) {
        this.city = city;
    }
    
    /**
     * @return the city id
     */
    public Optional<Integer> getCityId() {
        return city.getId();
    }
    
    public void pushToDatabase() throws SQLException{
        PreparedStatement s = Schedule.getDbInstance().prepareStatement("insert into "
                + "address(address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
        s.setString(1, getAddress());
        s.setString(2, getAddress2());
        s.setString(3, "" + getCityId().orElseThrow(RuntimeException::new));
        s.setString(4, getPostalCode());
        s.setString(5, getPhone());
        s.setString(6, "" + getCreateDate().toLocalDateTime());
        s.setString(7, getCreatedBy());
        s.setString(8, "" + Timestamp.from(getLastUpdate().toInstant()));
        s.setString(9, getLastUpdateBy());
        s.executeUpdate();
        s.close();
        s = Schedule.getDbInstance().prepareStatement("select addressId from address where "
                + "address = ? COLLATE latin1_general_cs and "
                + "address2 = ? COLLATE latin1_general_cs and "
                + "cityId = ? COLLATE latin1_general_cs and "
                + "postalCode = ? COLLATE latin1_general_cs and "
                + "phone = ? COLLATE latin1_general_cs");
        s.setString(1, getAddress());
        s.setString(2, getAddress2());
        s.setString(3, "" + getCityId().orElseThrow(RuntimeException::new));
        s.setString(4, getPostalCode());
        s.setString(5, getPhone());
        ResultSet r = s.executeQuery();
        while (r.next()) {
            setId(r.getInt("addressId"));
        }
        s.close();
        System.out.println(getId().get());
    }
    
    @Override
    public String toString() {
        String output = "";
        output += "addressId: " + getId();
        output += "; address: " + getAddress();
        output += "; address2: " + getAddress2();
        output += "; postalCode: " + getPostalCode();
        output += "; phone: " + getPhone();
        output += "; " + getCity();
        return output;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Address)) return false;
        Address a = (Address) obj;
        boolean address = a.getAddress().equals(getAddress());
        boolean address2 = a.getAddress2().equals(getAddress2());
        boolean postalCode = a.getPostalCode().equals(getPostalCode());
        boolean phone = a.getPhone().equals(getPhone());
        boolean cityId = a.getCityId().orElseThrow(RuntimeException::new).intValue() == getCityId().orElseThrow(RuntimeException::new).intValue();
        return address && address2 && postalCode && phone && cityId;
    }
}
