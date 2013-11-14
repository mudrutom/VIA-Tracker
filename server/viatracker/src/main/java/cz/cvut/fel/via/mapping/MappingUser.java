/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.via.mapping;

import cz.cvut.fel.via.entities.EntityUser;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Vasek
 */

//DB -> XML(JSON) adapter
@XmlRootElement(name="destination")
public class MappingUser {
    
    private int idUser;
    private String firstName;
    private String lastName;
    private String email;
    private String pw;
    
    public MappingUser() {}
    
    public MappingUser(EntityUser eu) {
        if(eu != null){
            this.idUser = eu.getIdUser();
            this.firstName = eu.getFirstName();
            this.lastName = eu.getLastName();
            this.email = eu.getEmail();
            this.pw = eu.getPw();
        }
    }

    @XmlElement(required=true)
    public String getFirstName() {
        return firstName;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
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
