package advert;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import beans.ItemBean;
import model.Store;

/**
 * Servlet Filter implementation class Adverts
 */
@WebFilter("/Front/*")
public class Advert implements Filter {

    /**
     * Default constructor. 
     */
    public Advert() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here

		HttpServletRequest req = (HttpServletRequest)request;
		HttpSession session = req.getSession();
		ArrayList<ItemBean> displayedList;
		String addItem = request.getParameter("AddToCart");
		
		if( addItem != null)
		{
			displayedList = (ArrayList<ItemBean>) session.getAttribute("ITEM_LIST");
			int index = Integer.parseInt(addItem);
			ItemBean item = displayedList.get(index);
			String itemNumber = item.getNumber();
			
			ItemBean suggest = null;
			Store store = (Store) req.getServletContext().getAttribute("Store");
			ArrayList<ItemBean> itemList = null;

			if(itemNumber.equals("1409S413"))
			{
				try 
				{
					itemList = (ArrayList<ItemBean>) store.getAllItems();
					for(int i = 0; i < itemList.size(); i++)
					{
						if(itemList.get(i).getNumber().equals("2002H712"))
						{
							suggest = itemList.get(i);
							break;
						}
						else
						{
							int rand = (int) (Math.random()%itemList.size());
							suggest = itemList.get(rand);
						}
					}
				}
				catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			request.setAttribute("suggested", suggest);	
		}
		chain.doFilter(request, response);
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
