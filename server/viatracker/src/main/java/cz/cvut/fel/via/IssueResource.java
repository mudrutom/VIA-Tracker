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
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Vasek
 */
@Path("issue")
public class IssueResource {

    private final String initCtxLookup = "java:comp/env";
    private final String envCtxLookup = "jdbc/ViaTrackerDS";
    
    @javax.ws.rs.core.Context
    private UriInfo context;

    public IssueResource() {
    }

    @GET
    @Produces("application/json")
    public String getAllIssues() { //@HeaderParam("x-State") int state

        JSONArray json = new JSONArray();
        Connection conn = null;

        try {

            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup(initCtxLookup);
            DataSource ds = (DataSource) envCtx.lookup(envCtxLookup);
            System.out.println(ds.toString());
            conn = ds.getConnection();

            ResultSet rs;
//            if (state > 0) {
//                PreparedStatement prepStatement = conn.prepareStatement("SELECT * FROM issues as i, state as s WHERE i.idIssue==s.stateOfIssue AND s.state=?");
//                prepStatement.setInt(1, state);
//
//                rs = prepStatement.executeQuery();
//            } else {
                PreparedStatement prepStatement = conn.prepareStatement("SELECT i.idIssue, i.title, i.description, i.priority, i.createdByUser,"
                        + " s.state, s.timestamp FROM issues as i, (SELECT stateOfIssue, state, timestamp FROM state "
                        + "WHERE timestamp = (SELECT max(timestamp) FROM state as f WHERE f.stateOfIssue = state.stateOfIssue)) as s"
                        + " WHERE i.idIssue=s.stateOfIssue ORDER BY s.state");

                //PreparedStatement prepStatement = conn.prepareStatement("SELECT * FROM issues");
                
                rs = prepStatement.executeQuery();
//            }
            
            ResultSetConverter converter = new ResultSetConverter();

            json = converter.getJSONArray(rs);

            conn.close();
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

        return json.toString();
    }
    
    @GET
    @Path("/{id}/")
    @Produces("application/json")
    public String getIssue(@PathParam("id") int id) {

        JSONArray json = new JSONArray();
        Connection conn = null; 

        try {

            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup(initCtxLookup);
            DataSource ds = (DataSource) envCtx.lookup(envCtxLookup);
            System.out.println(ds.toString());
            conn = ds.getConnection();

            //PreparedStatement prepStatement = conn.prepareStatement("SELECT * FROM issues WHERE idIssue=?");
            PreparedStatement prepStatement = conn.prepareStatement("SELECT i.idIssue, i.title, i.description, i.priority, i.createdByUser,"
                        + " s.state, s.timestamp FROM issues as i, (SELECT stateOfIssue, state, timestamp FROM state "
                        + "WHERE timestamp = (SELECT max(timestamp) FROM state as f WHERE f.stateOfIssue = state.stateOfIssue)) as s"
                        + " WHERE i.idIssue=s.stateOfIssue AND i.idIssue=? ORDER BY s.state");
            prepStatement.setInt(1, id);

            ResultSet rs = prepStatement.executeQuery();

            ResultSetConverter converter = new ResultSetConverter();

            json = converter.getJSONArray(rs);

            conn.close();
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

        return json.toString();
    }

    @PUT
    @Path("/{id}/")
    @Consumes("application/json")
    public void updateIssue(String data, @PathParam("id") int id) {
        
        Connection conn = null;

        try {

            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup(initCtxLookup);
            DataSource ds = (DataSource) envCtx.lookup(envCtxLookup);
            System.out.println(ds.toString());
            conn = ds.getConnection();

            JSONArray json = new JSONArray(data);
            for (int i = 0; i < json.length(); i++) {

                //update issue
                String title = json.getJSONObject(i).getString("title");
                String description = json.getJSONObject(i).getString("description");
                int priority = json.getJSONObject(i).getInt("priority");
                int createdByUser = json.getJSONObject(i).getInt("createdByUser");

                PreparedStatement prepStatement = conn.prepareStatement("UPDATE issues SET title=?, description=?, priority=?, createdByUser=? WHERE idIssue=?");
                prepStatement.setString(1, title);
                prepStatement.setString(2, description);
                prepStatement.setInt(3, priority);
                prepStatement.setInt(4, createdByUser);
                prepStatement.setInt(5, id);
                prepStatement.executeUpdate();

                //update state
                int state = json.getJSONObject(i).getInt("state");
                
                //PreparedStatement prepStatement2 = conn.prepareStatement("UPDATE state SET state=? WHERE idIssue=?");
                
                //vkladam, abych mel historii zmen stavu
                PreparedStatement prepStatement2 = conn.prepareStatement("INSERT INTO state (state, stateOfIssue) VALUES(?,?)");
                prepStatement2.setInt(1, state);
                prepStatement2.setInt(2, id);
                prepStatement2.executeUpdate();
                
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

    }

    @POST
    @Consumes("application/json")
    public void addNewIssue(String data) {

        Connection conn = null;

        try {

            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup(initCtxLookup);
            DataSource ds = (DataSource) envCtx.lookup(envCtxLookup);
            System.out.println(ds.toString());
            conn = ds.getConnection();

            JSONArray json = new JSONArray(data);
            for (int i = 0; i < json.length(); i++) {

                //Issue
                String title = json.getJSONObject(i).getString("title");
                String description = json.getJSONObject(i).getString("description");
                int priority = json.getJSONObject(i).getInt("priority");
                int createdByUser = json.getJSONObject(i).getInt("createdByUser");

                PreparedStatement prepStatement = conn.prepareStatement("INSERT INTO issues (title, description, priority, createdByUser) VALUES(?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
                prepStatement.setString(1, title);
                prepStatement.setString(2, description);
                prepStatement.setInt(3, priority);
                prepStatement.setInt(4, createdByUser);
                prepStatement.executeUpdate();
                
                ResultSet rs = prepStatement.getGeneratedKeys();
                int issueID = -1;
                if(rs.next())
                {
                    issueID = rs.getInt(1);
                }
                
                //State of current Issue
                if(issueID >=0){
                    int state = 1; //created
                    try {
                        state = json.getJSONObject(i).getInt("state");
                    } catch (Exception e) { //state was not specified
                        e.printStackTrace();
                    }

                    PreparedStatement prepStatement2 = conn.prepareStatement("INSERT INTO state (state, stateOfIssue) VALUES(?,?)");
                    prepStatement2.setInt(1, state);
                    prepStatement2.setInt(2, issueID);
                    prepStatement2.executeUpdate();
                    
                }
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
    
    }
    
    @DELETE
    @Path("/{id}/")
    @Consumes("application/json")
    public void deleteIssue(@PathParam("id") int id) {
        
        Connection conn = null;

        try {

            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup(initCtxLookup);
            DataSource ds = (DataSource) envCtx.lookup(envCtxLookup);
            System.out.println(ds.toString());
            conn = ds.getConnection();


            PreparedStatement prepStatement = conn.prepareStatement("DELETE FROM issues WHERE idIssue=?");
            prepStatement.setInt(1, id);

            prepStatement.executeUpdate();

            /*
             *  TO DO
             * 
             *  cascade delete on states/comments (?)
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

    }
}
