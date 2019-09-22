/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wgugzp1;

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
    
    public Address() {
        
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
}
