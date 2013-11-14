/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.via;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 *
 * @author Ota
 */
public class TestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            /*         EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("via-test");
             EntityManager em = emf.createEntityManager();
             System.out.println(em.toString());*/

            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/ViaTrackerDS");
            System.out.println(ds.toString());
            Connection conn = ds.getConnection();

            
            PrintWriter out = resp.getWriter();

            out.write("DB values:\n");

//            PreparedStatement updateemp = conn.prepareStatement("insert into test values(?)");
//            updateemp.setString(1, "Roshan");
//            updateemp.executeUpdate();
            
            Statement stmt = conn.createStatement();
            String query = "select * from users";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String jmeno = rs.getString("firstName");
                String prijmeni = rs.getString("lastName");
                out.write("Name> "+jmeno+" "+prijmeni+"\n");

            }




            conn.close();
        } catch (NamingException ex) {
            Logger.getLogger(TestServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TestServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
