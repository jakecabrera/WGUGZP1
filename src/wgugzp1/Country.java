/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wgugzp1;

/**
 *
 * @author jakes
 */
public class Country extends Record{
    private String country;
    
    public Country(String country) {
        setCountry(country);
    }
    
    public Country(String country, int id) {
        this(country);
        setId(id);
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        if (country.length() > 50) throw new IllegalArgumentException();
        this.country = country;
    }
}
