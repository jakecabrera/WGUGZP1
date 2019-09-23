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
    
    public void pushToDatabase() throws SQLException {
        PreparedStatement s = Schedule.getDbInstance().prepareStatement("insert into "
                + "country(country, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "values(?, ?, ?, ?, ?)");
        s.setString(1, getCountry());
        s.setString(2, "" + getCreateDate().toLocalDateTime());
        s.setString(3, getCreatedBy());
        s.setString(4, "" + Timestamp.from(getLastUpdate().toInstant()));
        s.setString(5, getLastUpdateBy());
        s.executeUpdate();
        s.close();
        s = Schedule.getDbInstance().prepareStatement("select countryId from country where country = ? COLLATE latin1_general_cs");
        s.setString(1, getCountry());
        ResultSet r = s.executeQuery();
        while (r.next()) {
            setId(r.getInt("countryId"));
        }
        s.close();
        System.out.println(getId().get());
    }
    
    @Override
    public String toString() {
        String output = "";
        output += "countryId: " + getId();
        output += "; countryName: " + country;
        return output;
    }
}
