package dao;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.tomcat.jdbc.pool.DataSource;

import beans.CategoryBean;

public class CategoryDAO {
	
	//Change schema here
	private static final String SCHEMA_NAME = "roumani";
	private static final String GET_ALL_CATEGORIES = "select * from category";
	
	private static Connection conn = null;
	private static Statement stmt = null;
	private DataSource ds;
	
	public CategoryDAO() throws NamingException, SQLException, ClassNotFoundException {
		 ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/EECS");				
	}
	
	//Get a connection from the pool
	private void createConnection() throws SQLException
	{
		 conn = this.ds.getConnection();
	     conn.createStatement().executeUpdate("set schema "+SCHEMA_NAME);
	     stmt = conn.createStatement();
	}
	
	//Return connection to the pool
	private void closeConnection() throws SQLException
	{
    	conn.close();
	}
	
	//Convert the result set from SQL query into a List bean
	private List<CategoryBean> prepareList(ResultSet r) throws SQLException
	{
    	List<CategoryBean> resultList = new ArrayList<CategoryBean>();
    	
	    while(r.next()){
			 int id = r.getInt("ID");
			 String name = r.getString("NAME");
			 String description = r.getString("DESCRIPTION");
			 Blob img = r.getBlob("PICTURE");
			 byte[] picture = img.getBytes(1, (int)img.length());

			 CategoryBean temp = new CategoryBean(id,name,description,picture);
			 resultList.add(temp);
		 }
		return resultList;
	}
	public List<CategoryBean> retrieveAll() throws SQLException{
		createConnection();
		ResultSet r = stmt.executeQuery(GET_ALL_CATEGORIES);
		closeConnection();
		return prepareList(r);
	}

}
