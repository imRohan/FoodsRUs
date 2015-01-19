package ctrl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import auth.LoginAuth;
import beans.CartBean;
import beans.CategoryBean;
import beans.CustomerBean;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;

import model.CartProcess;
import model.Store;

/**
 * Servlet implementation class Front
 */
@WebServlet({"/Front/*"})
public class Front extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	@Override
	public void init() throws ServletException
	{
		super.init();
		try {
			try {
				this.getServletContext().setAttribute("Store", new Store());
				((Store) this.getServletContext().getAttribute("Store")).getCategoryImages(this.getServletContext());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (Base64DecodingException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Front-init");
	}
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Front() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("===========In Front===========");
		System.out.println("Cat Option value: "+request.getParameter("CatOption"));
		System.out.println("AddToCart value: "+request.getAttribute("AddToCart"));
		System.out.println("URL: "+request.getRequestURI() );
		System.out.println("ServletContext: "+request.getServletContext());
		System.out.println("Servlet Path: "+request.getServletPath());
		System.out.println("=====================================");
		
		
		//Get parameters from the view
		String catOption = request.getParameter("CatOption");
		String UpdateButton = request.getParameter("update");
		String addToCart = request.getParameter("AddToCart");
		String submitOrder = request.getParameter("submitOrder");
		System.out.println("In front = " + submitOrder);
		String Searchstring = request.getParameter("searchstring");
		String Searchbutton = request.getParameter("searchbutton");
		String quantity = request.getParameter("quantity");
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		//Get user session
		HttpSession session =  request.getSession();
		CartBean cart = (CartBean)session.getAttribute("SessionCart");
		
		String jspTarget = null;
		Store store = (Store) this.getServletContext().getAttribute("Store");
		
		
		
		//Import pictures into the database

		
		//Each time something gets updated in the pages, so 
		//Add to cart, increase or decrease in the values, we should be able
		//to send the value back to the cart object, present here. 
		if(session.getAttribute("SessionCart") == null)
		{
			System.out.println("Cart Instantiated");
			cart = new CartBean();
			request.setAttribute("RequestCart", cart);
			session.setAttribute("SessionCart", cart);
			CartProcess cartProcess =  new CartProcess(cart.getItemsList());
			request.setAttribute("RequestCartProcess", cartProcess);
			session.setAttribute("SessionCartProcess", cartProcess);
		}
		
		if(cart != null)  //change
            request.setAttribute("CartSize", cart.getItemListSize()); //changed
		else if(cart == null){ //change
			request.setAttribute("CartSize", 0); //changeddd
		} // changed
		/*
		 * When the user is on Catalog (Home) page
		 * 
		 * 
		 */
		
		
		if (submitOrder != null && submitOrder.equals("1"))
		{
			RequestDispatcher rd;
			if(username == null && password == null)
			{
				rd = request.getRequestDispatcher("/Login.jspx");
				rd.forward(request, response);
			}
			else
			{
				System.out.println("Getting ready to authenticate....");
				LoginAuth newAuthentication = new LoginAuth(username, password);
				String name = newAuthentication.authenticate(username, password);
				if(name != null)
				{
					System.out.println("Logged In");
					cart.setCustomer(new CustomerBean(name, username));
					request.setAttribute("RequestCart", cart);
					session.setAttribute("SessionCart", cart);
					request.setAttribute("login_status", true);
					session.setAttribute("login_status", true);
					System.out.println("Checking out now...");
					session.setAttribute("submitOrder", submitOrder);
					rd = this.getServletContext().getNamedDispatcher("Cart");
					rd.forward(request, response);
				}
				else
				{
					System.out.println("Not Logged In");
					request.setAttribute("login_status", false);
					//Did not login
					rd = request.getRequestDispatcher("/Forward.jspx");
					rd.forward(request, response);
				}
				
			}
			
		}
		else if(UpdateButton != null){
			System.out.println("update button was clicked from front...");
			session.setAttribute("update", UpdateButton);
			RequestDispatcher rd = this.getServletContext().getNamedDispatcher("Cart");
			rd.forward(request, response);
		}
		else if (request.getParameter("forward") == null && catOption == null && addToCart == null && Searchbutton == null)
		{
			//Get all categories in the catalog
			try {
				List<CategoryBean> catList = store.getCategories();
				
				this.getServletContext().setAttribute("catList", catList);
				
				List<Integer> Category_ID_list = new ArrayList<Integer>();
 				List<String> Category_name_list = new ArrayList<String>();//=======
				List<String> Category_description_list = new ArrayList<String>();
				List<byte[]> Category_picture_list = new ArrayList<byte[]>();

			
			    for (int j = 0; j < catList.size() ; j ++)
			    {
			    	Category_ID_list.add(catList.get(j).getId());
			    	Category_name_list.add(catList.get(j).getName());
			    	Category_description_list.add(catList.get(j).getDescription());
			    	Category_picture_list.add(catList.get(j).getPicture());
			    }
			    request.setAttribute("CATEGORY_ID_LIST", Category_ID_list );
		       	request.setAttribute("CATEGORY_NAME_LIST", Category_name_list );
		       	request.setAttribute("CATEGORY_DESCRIPTION_LIST", Category_description_list );
		       	request.setAttribute("CATEGORY_PICTURE_LIST", Category_picture_list );
		       	request.setAttribute("SUCCESS", true);
		       
		       	//Add to session
			    session.setAttribute("CATEGORY_ID_LIST", Category_ID_list );
		       	session.setAttribute("CATEGORY_NAME_LIST", Category_name_list );
		       	session.setAttribute("CATEGORY_DESCRIPTION_LIST", Category_description_list );
		       	session.setAttribute("CATEGORY_PICTURE_LIST", Category_picture_list );
		       	session.setAttribute("SUCCESS", true);
		       
			     jspTarget = "/Catalog.jspx" ;
			     request.getRequestDispatcher(jspTarget).forward(request, response);
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			} 
		}
		/*
		 * When the user has selected a catalog but Cart is empty
		 */
		else if ((catOption != null || Searchbutton != null || request.getParameter("forward") != null) && addToCart == null)
		{
			//Get the Category ID for the option selected by the user
			session.setAttribute("CatOption", catOption);
			session.setAttribute("searchstring",Searchstring);
			request.setAttribute("searchstring",Searchstring);
			session.setAttribute("searchbutton", Searchbutton);
			request.setAttribute("searchbutton", Searchbutton);
			
			RequestDispatcher rd = this.getServletContext().getNamedDispatcher("Cart");
			rd.forward(request, response);
					//request.getRequestDispatcher(jspTarget).forward(request, response);      
		}
		/*
		 * When the user has selected a catalog and there are items in the cart
		 */
		else if(catOption != null && addToCart != null)
		{
			System.out.println("AddToCart is not null....");
			System.out.println("ADD: "+addToCart);
			session.setAttribute("AddToCart", addToCart);
			session.setAttribute("quantity", quantity);
			session.setAttribute("submitOrder", submitOrder);
			RequestDispatcher rd = this.getServletContext().getNamedDispatcher("Cart");
			rd.forward(request, response);
		
		}


		else //Leave this for now....We can add specifics later....
		{
			System.out.println("==============================================================================");

			System.out.println("==============================================================================");
		
			session.setAttribute("AddToCart", addToCart);
			session.setAttribute("quantity", quantity);
			session.setAttribute("submitOrder", submitOrder);
			RequestDispatcher rd = this.getServletContext().getNamedDispatcher("Cart");
			rd.forward(request, response);
			
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
}
