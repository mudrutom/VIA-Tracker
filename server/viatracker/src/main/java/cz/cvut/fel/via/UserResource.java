/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.via;

//import javax.ws.rs.core.Context;
import cz.cvut.fel.via.utils.ResultSetConverter;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Vasek
 */
@Path("user")
public class UserResource {

    private final String initCtxLookup = "java:comp/env";
    private final String envCtxLookup = "jdbc/ViaTrackerDS";
    @javax.ws.rs.core.Context
    private UriInfo context;

    public UserResource() {
    }

    @GET
    @Produces("application/json")
    public Response getAllUsers() {

        JSONArray json = new JSONArray();
        Connection conn = null;

        try {

            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup(initCtxLookup);
            DataSource ds = (DataSource) envCtx.lookup(envCtxLookup);
            System.out.println(ds.toString());
            conn = ds.getConnection();

            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM users";
            ResultSet rs = stmt.executeQuery(query);
            
            
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
    
    @GET
    @Path("/{id}/")
    @Produces("application/json")
    public Response getUser(@PathParam("id") int id) {

        JSONArray json = new JSONArray();
        Connection conn = null;

        try {

            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup(initCtxLookup);
            DataSource ds = (DataSource) envCtx.lookup(envCtxLookup);
            System.out.println(ds.toString());
            conn = ds.getConnection();

            PreparedStatement prepStatement = conn.prepareStatement("SELECT * FROM users WHERE idUser=?");
            prepStatement.setInt(1, id);

            ResultSet rs = prepStatement.executeQuery();
            
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

    @PUT
    @Path("/{id}/")
    @Consumes("application/json")
    public Response updateUser(String data, @PathParam("id") int id) {
        
        Connection conn = null;

        try {

            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup(initCtxLookup);
            DataSource ds = (DataSource) envCtx.lookup(envCtxLookup);
            System.out.println(ds.toString());
            conn = ds.getConnection();

            JSONArray json = new JSONArray();
            try {
                json = new JSONArray(data);
            } catch (Exception e1) {
                e1.printStackTrace();
                try {
                    json = new JSONArray("["+data+"]");
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            
            for (int i = 0; i < json.length(); i++) {

                String firstName = json.getJSONObject(i).getString("firstName");
                String lastName = json.getJSONObject(i).getString("lastName");
                String email = json.getJSONObject(i).getString("email");
                String pw = json.getJSONObject(i).getString("pw");

                PreparedStatement prepStatement = conn.prepareStatement("UPDATE users SET firstName=?, lastName=?, email=?, pw=? WHERE idUser=?");
                prepStatement.setString(1, firstName);
                prepStatement.setString(2, lastName);
                prepStatement.setString(3, email);
                prepStatement.setString(4, pw);
                prepStatement.setInt(5, id);
                prepStatement.executeUpdate();

            }

            conn.close();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
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

    @POST
    @Consumes("application/json")
    public Response addNewUser(String data) {

        Connection conn = null;

        try {

            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup(initCtxLookup);
            DataSource ds = (DataSource) envCtx.lookup(envCtxLookup);
            System.out.println(ds.toString());
            conn = ds.getConnection();

            JSONArray json = new JSONArray();
            try {
                json = new JSONArray(data);
            } catch (Exception e1) {
                e1.printStackTrace();
                try {
                    json = new JSONArray("["+data+"]");
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            
            for (int i = 0; i < json.length(); i++) {

                String firstName = json.getJSONObject(i).getString("firstName");
                String lastName = json.getJSONObject(i).getString("lastName");
                String email = json.getJSONObject(i).getString("email");
                String pw = json.getJSONObject(i).getString("pw");

                PreparedStatement prepStatement = conn.prepareStatement("INSERT INTO users (firstName, lastName, email, pw) VALUES(?,?,?,?)");
                prepStatement.setString(1, firstName);
                prepStatement.setString(2, lastName);
                prepStatement.setString(3, email);
                prepStatement.setString(4, pw);
                prepStatement.executeUpdate();

            }

            conn.close();
            
            return Response.status(Response.Status.CREATED).build();
            
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
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
    
    @DELETE
    @Path("/{id}/")
    @Consumes("application/json")
    public Response deleteUser(@PathParam("id") int id) {
        
        Connection conn = null;

        try {

            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup(initCtxLookup);
            DataSource ds = (DataSource) envCtx.lookup(envCtxLookup);
            System.out.println(ds.toString());
            conn = ds.getConnection();


            PreparedStatement prepStatement = conn.prepareStatement("DELETE FROM users WHERE idUser=?");
            prepStatement.setInt(1, id);

            prepStatement.executeUpdate();

            /*
             *  TO DO
             * 
             *  cascade delete on issues/comments (?)
             * 
             */
            

            conn.close();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }  finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
