/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.via.resources;

import cz.cvut.fel.via.entities.EntityUser;
import cz.cvut.fel.via.mapping.MappingUser;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Vasek
 */

@Path("/user")
public class Users {
    
    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Collection<MappingUser> getUsers(){
        List<MappingUser> users = new ArrayList<MappingUser>();
//        try {
//            Context initCtx = new InitialContext();
//            Context envCtx = (Context) initCtx.lookup("java:comp/env");
//            DataSource ds = (DataSource) envCtx.lookup("jdbc/ViaTrackerDS");
//            System.out.println(ds.toString());
//            Connection conn = ds.getConnection();
//            
//            Statement stmt = conn.createStatement();
//            String query = "select * from users";
//            ResultSet rs = stmt.executeQuery(query);
//            while (rs.next()) {
//                String jmeno = rs.getString("firstName");
//                String prijmeni = rs.getString("lastName");
//                
//                EntityUser eu = new EntityUser(jmeno, prijmeni, "email", "pw");
//                MappingUser mp = new MappingUser(eu);
//                users.add(mp);
//
//            }
//            
//        } catch (Exception e) {
//        }
        
        EntityUser eu = new EntityUser("aaa", "bbb", "email", "pw");
        MappingUser mp = new MappingUser(eu);
        users.add(mp);
 
        return users;
    }
    
}
