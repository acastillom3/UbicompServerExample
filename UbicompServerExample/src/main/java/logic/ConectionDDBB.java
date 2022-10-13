package logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class ConectionDDBB
{
	public Connection obtainConnection(boolean autoCommit) throws NullPointerException
    {
        Connection con=null;
        int intentos = 5;
        for (int i = 0; i < intentos; i++) 
        {
        	LCon.log.info("Attempt " + i + " to connect to the database");
        	try
	          {
	            Context ctx = new InitialContext();
	            // Get the connection factory configured in Tomcat
	            DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/ubicomp");
	           
	            // Obtiene una conexion
	            con = ds.getConnection();
				Calendar calendar = Calendar.getInstance();
				java.sql.Date date = new java.sql.Date(calendar.getTime().getTime());
	            LCon.log.debug("Connection creation. Bd connection identifier: " + con.toString() + " obtained in " + date.toString());
	            con.setAutoCommit(autoCommit);
	        	LCon.log.info("Conection obtained in the attempt: " + i);
	            i = intentos;
	          } catch (NamingException ex)
	          {
	            LCon.log.error("Error getting connection while trying: " + i + " = " + ex); 
	          } catch (SQLException ex)
	          {
	            LCon.log.error("ERROR sql getting connection while trying: " + i + " = " + ex.getSQLState() + "\n" + ex.toString());
	            throw (new NullPointerException("SQL connection is null"));
	          }
		}        
        return con;
    }
    
    public void closeTransaction(Connection con)
    {
        try
          {
            con.commit();
            LCon.log.debug("Transaction closed");
          } catch (SQLException ex)
          {
            LCon.log.error("Error closing the transaction: " + ex);
          }
    }
    
    public void cancelTransaction(Connection con)
    {
        try
          {
            con.rollback();
            LCon.log.debug("Transaction canceled");
          } catch (SQLException ex)
          {
            LCon.log.error("ERROR sql when canceling the transation: " + ex.getSQLState() + "\n"  + ex.toString());
          }
    }

    public void closeConnection(Connection con)
    {
        try
          {
        	LCon.log.info("Closing the connection");
            if (null != con)
              {
				Calendar calendar = Calendar.getInstance();
				java.sql.Date date = new java.sql.Date(calendar.getTime().getTime());
	            LCon.log.debug("Connection closed. Bd connection identifier: " + con.toString() + " obtained in " + date.toString());
                con.close();
              }

        	LCon.log.info("The connection has been closed");
          } catch (SQLException e)
          {
        	  LCon.log.error("ERROR sql closing the connection: " + e);
        	  e.printStackTrace();
          }
    }
    
    public static PreparedStatement getStatement(Connection con,String sql)
    {
        PreparedStatement ps = null;
        try
          {
            if (con != null)
              {
                ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

              }
          } catch (SQLException ex)
          {
    	        LCon.log.warn("ERROR sql creating PreparedStatement: " + ex.toString());
          }

        return ps;
    }    

    public static PreparedStatement GetDataBD(Connection con)
    {
    	return getStatement(con,"SELECT * FROM UBICOMP.MEASUREMENT");  	
    }
    
    public static PreparedStatement SetDataBD(Connection con)
    {
    	return getStatement(con,"INSERT INTO UBICOMP.MEASUREMENT VALUES (?,?)");  	
    }
}
