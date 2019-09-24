/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wgugzp1;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import static wgugzp1.Appointment.getOffset;

/**
 *
 * @author jakes
 */
public class Database {
    private final Map<Integer, Customer> customers = new HashMap();
    private final Map<Integer, Address> addresses = new HashMap();
    private final Map<Integer, City> cities = new HashMap();
    private final Map<Integer, Country> countries = new HashMap();
    private final Map<Integer, User> users = new HashMap();
    private Map<Integer, Appointment> appointments = new HashMap();
    private User loggedInUser;
    private static volatile Database database;
    private int offsetMonths = 0;
    
    private Database() throws SQLException{
        setupMap();
    }
    
    public static Database getInstance() throws SQLException{
        if (database == null) {
            synchronized(Database.class) {
                if (database == null) {
                    database = new Database();
                }
            }
        }
        return database;
    }
    
    private void setupMap() throws SQLException {
        Connection c = Schedule.getDbInstance();
        Statement s = c.createStatement();
        
        // Get countries
        getCountries().clear();
        ResultSet r = s.executeQuery("select * from country");
        while (r.next()) {
            String countryName = r.getString("country");
            int id = r.getInt("countryId");
            Country country = new Country(countryName, id);
            setRecord(country, r);
            getCountries().put(id, country);
        }
        r.close();
        
        // Get cities
        getCities().clear();
        r = s.executeQuery("select * from city");
        while (r.next()) {
            String cityName = r.getString("city");
            int id = r.getInt("cityId");
            int countryId = r.getInt("countryId");
            City city = new City(cityName, getCountries().get(countryId), id);
            setRecord(city, r);
            getCities().put(id, city);
        }
        r.close();
        
        // Get addresses
        getAddresses().clear();
        r = s.executeQuery("select * from address");
        while (r.next()) {
            String address = r.getString("address");
            String address2 = r.getString("address2");
            int cityId = r.getInt("cityId");
            int id = r.getInt("addressId");
            String postalCode = r.getString("postalCode");
            String phone = r.getString("phone");
            Address addr = new Address(address, address2, postalCode, phone, getCities().get(cityId), id);
            setRecord(addr, r);
            getAddresses().put(id, addr);
        }
        r.close();
        
        // Get customers
        getCustomers().clear();
        r = s.executeQuery("select * from customer");
        while (r.next()) {
            String name = r.getString("customerName");
            int id = r.getInt("customerId");
            int addressId = r.getInt("addressId");
            int active = r.getInt("active");
            Customer customer = new Customer(name, getAddresses().get(addressId), active, id);
            setRecord(customer, r);
            getCustomers().put(id, customer);
        }
        r.close();
        
        // Get users
        getUsers().clear();
        r = s.executeQuery("select * from user");
        while (r.next()) {
            String name = r.getString("userName");
            int id = r.getInt("userId");
            String password = r.getString("password");
            byte active = (byte) r.getInt("active");
            User user = new User(name, password, active, id);
            setRecord(user, r);
            getUsers().put(id, user);
        }
        r.close();
        
        // Get appointments
        getAppointments().clear();
        r = s.executeQuery("select * from appointment");
        while (r.next()) {
            int id = r.getInt("appointmentId");
            int customerId = r.getInt("customerId");
            int userId = r.getInt("userId");
            String title = r.getString("title");
            String description = r.getString("description");
            String location = r.getString("location");
            String contact = r.getString("contact");
            String type = r.getString("type");
            String url = r.getString("url");
            ZonedDateTime start = ZonedDateTime.ofInstant(r.getTimestamp("start").toInstant(), ZoneId.of("GMT"));
            ZonedDateTime end = ZonedDateTime.ofInstant(r.getTimestamp("end").toInstant(), ZoneId.of("GMT"));
            
            Appointment appointment = new Appointment(
                    getCustomers().get(customerId),
                    getUsers().get(userId),
                    title,
                    description,
                    location,
                    contact,
                    type,
                    url,
                    start,
                    end,
                    id
            );
            setRecord(appointment, r);
            getAppointments().put(id, appointment);
        }
        r.close();
        
        s.close();
    }
    
    private void setRecord(Record record, ResultSet r) throws SQLException{
        record.setCreatedBy(r.getString("createdBy"));
        record.setCreateDate(ZonedDateTime.ofInstant(r.getTimestamp("createDate").toInstant(), ZoneId.of("GMT")));
        record.setLastUpdateBy(r.getString("lastUpdateBy"));
        record.setLastUpdate(ZonedDateTime.ofInstant(r.getTimestamp("lastUpdate").toInstant(), ZoneId.of("GMT")));
    }
    
    public void addCountry(Country country) throws SQLException{
        if (country.getId().isPresent()) return;
        for (Country c: getCountries().values()) {
            if (c.getCountry().equals(country.getCountry())) {
                country.setId(c.getId().get());
                System.out.println(country.toString());
                return;
            }
        }
        country.pushToDatabase();     
        System.out.println(country.toString());
        getCountries().put(country.getId().get(), country);
    }
    
    public void addCity(City city) throws SQLException {
        if (city.getId().isPresent()) return;
        for (City c: getCities().values()) {
            if (c.getCity().equals(city.getCity()) && c.getCountry().getCountry().equals(city.getCountry().getCountry())) {
                city.setId(c.getId().get());
                System.out.println(city.toString());
                return;
            }
        }
        city.pushToDatabase();     
        System.out.println(city.toString());
        getCities().put(city.getId().get(), city);
    }
    
    public void addAddress(Address address) throws SQLException {
        if (address.getId().isPresent()) return;
        for (Address a: getAddresses().values()) {
            if (a.equals(address)) {
                address.setId(a.getId().get());
                System.out.println(address.toString());
                return;
            }
        }
        address.pushToDatabase();     
        System.out.println(address.toString());
        getAddresses().put(address.getId().get(), address);
    }
    
    public void addCustomer(Customer customer) throws SQLException {
        if (customer.getId().isPresent()) return;
        for (Customer a: getCustomers().values()) {
            if (a.equals(customer)) {
                customer.setId(a.getId().get());
                System.out.println(customer.toString());
                return;
            }
        }
        customer.pushToDatabase();     
        System.out.println(customer.toString());
        getCustomers().put(customer.getId().get(), customer);
    }
    
    public void addAppointment(Appointment appointment) throws SQLException {
        if (appointment.getId().isPresent()) return;
        for (Appointment a: getAppointments().values()) {
            if (a.equals(appointment)) {
                appointment.setId(a.getId().get());
                System.out.println(appointment.toString());
                return;
            }
        }
        appointment.pushToDatabase();     
        System.out.println(appointment.toString());
        getAppointments().put(appointment.getId().get(), appointment);
    }
    
    public void deleteCustomer(Customer customer) throws SQLException {
        Connection db = Schedule.getDbInstance();
        Statement s = db.createStatement();
        s.executeUpdate("delete from customer where customerId = " + customer.getIdAsInt());
        getCustomers().remove(customer.getId().orElseThrow(RuntimeException::new));
    }
    
    public void deleteAppointment(Appointment appointment) throws SQLException {
        Connection db = Schedule.getDbInstance();
        Statement s = db.createStatement();
        s.executeUpdate("delete from appointment where appointmentId = " + appointment.getIdAsInt());
        getAppointments().remove(appointment.getIdAsInt());
    }

    /**
     * @return the customers
     */
    public Map<Integer, Customer> getCustomers() {
        return customers;
    }

    /**
     * @return the addresses
     */
    public Map<Integer, Address> getAddresses() {
        return addresses;
    }

    /**
     * @return the cities
     */
    public Map<Integer, City> getCities() {
        return cities;
    }

    /**
     * @return the countries
     */
    public Map<Integer, Country> getCountries() {
        return countries;
    }

    /**
     * @return the loggedInUser
     */
    public User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * @return the users
     */
    public Map<Integer, User> getUsers() {
        return users;
    }

    /**
     * @param loggedInUser the loggedInUser to set
     */
    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    /**
     * @return the appointments
     */
    public Map<Integer, Appointment> getAppointments() {
        return appointments;
    }
    
    public List<Appointment> getAppointmentsByMonth() {
        List<Appointment> list;
        Stream<Appointment> stream = getAppointments().values().stream();
        ZoneOffset offset = ZoneOffset.ofTotalSeconds(Appointment.getOffset());
        ZoneId zone = ZoneId.ofOffset("", offset);
        ZonedDateTime t = ZonedDateTime.now().withZoneSameInstant(zone);
        t.plusMonths(getOffsetMonths());
        stream.filter(x -> x.getStart().getYear() == t.getYear() && x.getStart().getMonth() == t.getMonth());
        list = stream.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        
        return list;
    }

    /**
     * @param appointments the appointments to set
     */
    public void setAppointments(Map<Integer, Appointment> appointments) {
        this.appointments = appointments;
    }

    /**
     * @return the offsetMonths
     */
    public int getOffsetMonths() {
        return offsetMonths;
    }

    /**
     * @param offsetMonths the offsetMonths to set
     */
    public void setOffsetMonths(int offsetMonths) {
        this.offsetMonths = offsetMonths;
    }
}
