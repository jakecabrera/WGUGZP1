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
public class Customer extends Record {
    private String name;
    private int active;
    private Address address;

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
    
}
