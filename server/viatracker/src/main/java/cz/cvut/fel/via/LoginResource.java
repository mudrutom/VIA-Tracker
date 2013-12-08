/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.via;

import cz.cvut.fel.via.utils.ResultSetConverter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

/**
 *
 * @author Vasek
 */
@Path("login")
public class LoginResource {
    
    private final String initCtxLookup = "java:comp/env";
    private final String envCtxLookup = "jdbc/ViaTrackerDS";
    @javax.ws.rs.core.Context
    private UriInfo context;

    public LoginResource() {
    }
    
    
    @GET
    @Produces("application/json")
    public Response verifyUser(@QueryParam("email") @DefaultValue("-1") String email, 
            @QueryParam("pw") @DefaultValue("-1") String pw) {

        JSONArray json = new JSONArray();
        Connection conn = null;

        try {

            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup(initCtxLookup);
            DataSource ds = (DataSource) envCtx.lookup(envCtxLookup);
            System.out.println(ds.toString());
            conn = ds.getConnection();

            ResultSet rs;

                PreparedStatement prepStatement = conn.prepareStatement("SELECT * FROM users WHERE email=? AND pw=?");
                prepStatement.setString(1, email);
                prepStatement.setString(2, pw);

                rs = prepStatement.executeQuery();

            
            int size = 0;
            try {
                rs.last();
                size = rs.getRow();
                rs.beforeFirst();
            } catch (Exception ex) {
                return Response.status(Response.Status.NO_CONTENT).build();
            }
            if(size==0){
                return Response.status(Response.Status.NO_CONTENT).build();
            }
            

            ResultSetConverter converter = new ResultSetConverter();

            json = converter.getJSONArray(rs);

            conn.close();
            
            if (json.length() == 1) {
                try {
                    //return json.getJSONObject(0).toString();
                    return Response.ok(json.getJSONObject(0).toString(), MediaType.APPLICATION_JSON).build();
                } catch (JSONException e) {
                    e.printStackTrace();
                    return Response.status(Response.Status.NO_CONTENT).build();
                }
            }

            //return json.toString();
            return Response.ok(json.toString(), MediaType.APPLICATION_JSON).build();
            
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return Response.status(Response.Status.NO_CONTENT).build();
    }
    
}
