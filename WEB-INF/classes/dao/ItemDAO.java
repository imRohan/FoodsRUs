package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.tomcat.jdbc.pool.DataSource;

import beans.ItemBean;

public class ItemDAO {

	private static final String SCHEMA_NAME = "roumani";
	private static final String GET_ALL_ITEMS = "select * from item";
	private static final String GET_ALL_FOR_CATEGORY = "select * from item where catid=?";
	private static final String GET_FOR_SEARCH = "select * from item where lower(name) LIKE '%";

	private static Connection conn = null;
	private static Statement stmt = null;
	private DataSource ds;
	
	public ItemDAO() throws NamingException, SQLException, ClassNotFoundException {
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
	private List<ItemBean> prepareList(ResultSet r) throws SQLException
	{
    	List<ItemBean> resultList = new ArrayList<ItemBean>();
    	
    	while(r.next()){
			 int catid = r.getInt("CATID");
			 double price = r.getDouble("PRICE");
			 String name = r.getString("NAME");
			 String number = r.getString("NUMBER");
			 
			 ItemBean temp = new ItemBean(price, name, number,  catid);
			 resultList.add(temp);
		 }
		return resultList;
	}
	public List<ItemBean> retrieveAll() throws SQLException{		
	    createConnection();
		ResultSet r = stmt.executeQuery(GET_ALL_ITEMS);
		closeConnection();
		return prepareList(r);
	}
	
	public List<ItemBean> retrieveByCategory(int cid) throws SQLException{		
		createConnection();
		PreparedStatement prepStmt = conn.prepareStatement(GET_ALL_FOR_CATEGORY);
		prepStmt.setInt(1, cid);
		ResultSet r = prepStmt.executeQuery();
		closeConnection();
		return prepareList(r);
	}
	
	public List<ItemBean> retrieveBySearch(String name) throws SQLException{		
		createConnection();
		System.out.println(createSQL(name));
		PreparedStatement prepStmt = conn.prepareStatement(createSQL(name.toLowerCase()));
		ResultSet r = prepStmt.executeQuery();
		closeConnection();
		return prepareList(r);
	}
	
	private static String createSQL(String search)
	{
		String queryString = null;
		if (!search.contains(" "))
		{
			System.out.println("No space found");
			queryString = GET_FOR_SEARCH + search.toLowerCase().concat("%") + "'";
		}
		
		else
		{
			String [] searchTokens = search.toLowerCase().split(" ");
			queryString = GET_FOR_SEARCH + searchTokens[0].concat("%") + "'";
			for (int i = 1; i < searchTokens.length; i++)
			{
				queryString = queryString.concat(" and ");
				queryString = queryString.concat(" lower(name) like '%" + searchTokens[i] + "%'");
			}
		}
			
		return queryString;
		
	}
}
