package logic;

import java.util.ArrayList;
import java.util.Date;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Logic 
{
	public static ArrayList<Measurement> getDataFromDB()
	{
		ArrayList<Measurement> values = new ArrayList<Measurement>();
		
		ConectionDDBB conector = new ConectionDDBB();
		Connection con = null;
		try
		{
			con = conector.obtainConnection(true);
			LCon.log.info("Database Connected");
			
			PreparedStatement ps = ConectionDDBB.GetDataBD(con);
			LCon.log.info("Query=>" + ps.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				Measurement measure = new Measurement();
				measure.setValue(rs.getInt("VALUE"));
				measure.setDate(rs.getTimestamp("DATE"));
				values.add(measure);
			}	
		} catch (SQLException e)
		{
			LCon.log.error("Error: " + e);
			values = new ArrayList<Measurement>();
		} catch (NullPointerException e)
		{
			LCon.log.error("Error: " + e);
			values = new ArrayList<Measurement>();
		} catch (Exception e)
		{
			LCon.log.error("Error:" + e);
			values = new ArrayList<Measurement>();
		}
		conector.closeConnection(con);
		return values;
	}

	public static ArrayList<Measurement> setDataToDB(int value)
	{
		ArrayList<Measurement> values = new ArrayList<Measurement>();
		
		ConectionDDBB conector = new ConectionDDBB();
		Connection con = null;
		try
		{
			con = conector.obtainConnection(true);
			LCon.log.info("Database Connected");

			PreparedStatement ps = ConectionDDBB.SetDataBD(con);
			ps.setInt(1, value);
			ps.setTimestamp(2, new Timestamp((new Date()).getTime()));
			LCon.log.info("Query=>" + ps.toString());
			ps.executeUpdate();
		} catch (SQLException e)
		{
			LCon.log.error("Error: " + e);
			values = new ArrayList<Measurement>();
		} catch (NullPointerException e)
		{
			LCon.log.error("Error: " + e);
			values = new ArrayList<Measurement>();
		} catch (Exception e)
		{
			LCon.log.error("Error:" + e);
			values = new ArrayList<Measurement>();
		}
		conector.closeConnection(con);
		return values;
	}
	
	
}
