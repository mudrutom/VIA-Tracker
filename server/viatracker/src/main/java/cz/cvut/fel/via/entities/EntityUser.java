/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.via.entities;

/**
 *
 * @author Vasek
 */
public class EntityUser {
    
    private static int maxID = 0;
    private int idUser;
    private String firstName;
    private String lastName;
    private String email;
    private String pw;
    
    public EntityUser(String fisrtName, String lastName, String email, String pw){
        this.idUser = ++maxID;
        this.firstName = fisrtName;
        this.lastName = lastName;
        this.email = email;
        this.pw = pw;
        
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }
    
    
}
