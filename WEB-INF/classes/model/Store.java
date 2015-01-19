package model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import beans.CategoryBean;
import beans.ItemBean;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import dao.CategoryDAO;
import dao.ItemDAO;

public class Store
{
	private CategoryDAO categories;
	private ItemDAO	items;

	/*
	 * Create store from database with categories and their items
	 */
	public Store() throws ClassNotFoundException, NamingException, SQLException
	{
		this.categories = new CategoryDAO();
		this.items = new ItemDAO();
	}
	
	/*
	 * Get all categories available for the store
	 */
	public List<CategoryBean> getCategories() throws SQLException
	{
		return categories.retrieveAll();
	}
	
	/*
	 * Get all items for all categories
	 */
	public List<ItemBean> getAllItems() throws SQLException
	{
		return items.retrieveAll();
	}
	
	/*
	 * Get the items as per the search query
	 */
	public List<ItemBean> getSearchedItems(String name) throws SQLException
	{
		return items.retrieveBySearch(name);
	}
	
	/*
	 * Get Items for a specific category that is identified by the category ID
	 */
	public List<ItemBean> getItemsForCategory(int catid) throws SQLException
	{				
		return items.retrieveByCategory(catid);
	}
	
	/*
	 * Method that fetches images for various categories from the database
	 */
	public void getCategoryImages(ServletContext ctx) throws IOException, Base64DecodingException, SQLException
	{
		try {
			List<CategoryBean> currCategories = getCategories();

			for (int i = 0; i < currCategories.size(); i++ )
			{
				// convert byte array back to BufferedImage and write to file
				InputStream in = new ByteArrayInputStream(currCategories.get(i).getPicture());
				BufferedImage bImageFromConvert = ImageIO.read(in);
				ImageIO.write(bImageFromConvert, "gif", new File(ctx.getRealPath("/imgs/" + i + ".gif")));
			}
			
 
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	
	}
}
