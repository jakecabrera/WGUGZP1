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
public class User extends Record {
    private String userName;
    private String password;
    private byte active;

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        if (userName.length() > 50) throw new IllegalArgumentException();
        this.userName = userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        if (password.length() > 50) throw new IllegalArgumentException();
        this.password = password;
    }

    /**
     * @return the active
     */
    public byte getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(byte active) {
        this.active = active;
    }
}
