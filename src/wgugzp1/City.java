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
public class City extends Record {
    private String city;    
    private Country country;

    public City(String city, Country country) {
        setCity(city);
        setCountry(country);
    }
    
    public City(String city, Country country, int id) {
        this(city, country);
        setId(id);
    }
    
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
    
    public void pushToDatabase() throws SQLException{
        PreparedStatement s = Schedule.getDbInstance().prepareStatement("insert into "
                + "city(city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "values(?, ?, ?, ?, ?, ?)");
        s.setString(1, getCity());
        s.setString(2, "" + getCountryId().orElseThrow(RuntimeException::new));
        s.setString(3, "" + getCreateDate().toLocalDateTime());
        s.setString(4, getCreatedBy());
        s.setString(5, "" + Timestamp.from(getLastUpdate().toInstant()));
        s.setString(6, getLastUpdateBy());
        s.executeUpdate();
        s.close();
        s = Schedule.getDbInstance().prepareStatement("select cityId from city where "
                + "city = ? COLLATE latin1_general_cs and "
                + "countryId = ? COLLATE latin1_general_cs");
        s.setString(1, getCity());
        s.setString(2, "" + getCountryId().orElseThrow(RuntimeException::new));
        ResultSet r = s.executeQuery();
        while (r.next()) {
            setId(r.getInt("cityId"));
        }
        s.close();
        System.out.println(getId().get());
    }
    
    @Override
    public String toString() {
        String output = "";
        output += "cityId: " + getId();
        output += "; cityName: " + getCity();
        output += "; " + getCountry();
        return output;
    }
}
