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
public class City extends Record {
    private String city;    
    private Country country;

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        if (city.length() > 50) throw new IllegalArgumentException();
        this.city = city;
    }

    /**
     * @return the country
     */
    public Country getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(Country country) {
        this.country = country;
    }
    
    /**
     * @return the country id
     */
    public Optional<Integer> getCountryId() {
        return country.getId();
    }
}
