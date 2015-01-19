package ctrl;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;

import beans.CartBean;
import beans.CustomerBean;
import beans.ItemBean;
import model.Store;
import model.CartProcess;

/**
 * Servlet implementation class Cart
 */
//@WebServlet("/Cart")
public class Cart extends HttpServlet {
	@Override
	public void init() throws ServletException
	{
		super.init();
		try {
			try {
				this.getServletContext().setAttribute("Store", new Store());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//.println("Front-init");
	}
	private static final long serialVersionUID = 1L;
	private static final String HST = "13";
	private static final String SHIPPING = "5";
	
       
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Cart() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		HttpSession session =  request.getSession();
		String addToCart = (String)session.getAttribute("AddToCart");
		String submitOrder = (String)session.getAttribute("submitOrder");
		System.out.println("In cart = "+submitOrder);
		String Searchstring= (String) session.getAttribute("searchstring");
		String UpdateButton = (String) session.getAttribute("update");
		String item_quantity = (String)session.getAttribute("quantity");
		Store store = (Store) this.getServletContext().getAttribute("Store");
		CartBean cart = (CartBean)session.getAttribute("SessionCart");
		CartProcess cartProcess = (CartProcess) session.getAttribute("SessionCartProcess");
		request.setAttribute("CartSize", cart.getItemListSize());
		
		//Get the category that was selected
		int catSelected = -1;
		String catFixed =  null;
		//===============
		/*Add to session
	    session.setAttribute("CATEGORY_ID_LIST", Category_ID_list );
       	session.setAttribute("CATEGORY_NAME_LIST", Category_name_list );
       	session.setAttribute("CATEGORY_DESCRIPTION_LIST", Category_description_list );
       	session.setAttribute("CATEGORY_PICTURE_LIST", Category_picture_list );
       	session.setAttribute("SUCCESS", true);
		*/
		
		List<String> item_number_list = new ArrayList<String>();
		List<String> item_name_list = new ArrayList<String>();
		List<Double> item_price_list = new ArrayList<Double>();
		
		if(request.getParameter("forward") != null){
			try
			{
				//HST, Shipping, Discount Threshold, Discount %
				cart.updateCart(HST, SHIPPING);
				session.setAttribute("SessionCart", cart);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			
		    request.setAttribute("RequestCartItems", cart.getItems());
		    request.setAttribute("RequestTotalCartPrice", cart.getTotal());
		    request.setAttribute("RequestHST", cart.getHST());
		    request.setAttribute("RequestShipping", cart.getShipping());
		    request.setAttribute("RequestGrandTotal", cart.getGrandTotal());

		    request.setAttribute("RequestCart", cart);
		    request.setAttribute("CartSize", cart.getItemListSize());
			request.getRequestDispatcher("/CartCheck.jspx").forward(request, response);
		}
		
		else {
			if(addToCart == null && submitOrder == null && UpdateButton == null)
			{			
			List<ItemBean> itemList = null;
			ArrayList<String> CatNameList = (ArrayList<String>) session.getAttribute("CATEGORY_NAME_LIST");
			
			try {
				if(Searchstring != null){
					itemList = store.getSearchedItems(Searchstring);
					session.setAttribute("ITEM_LIST", itemList);
				}
				
				else if (session.getAttribute("CatOption") != null){
					catSelected = Integer.parseInt(String.valueOf(session.getAttribute("CatOption")));
					catFixed =  CatNameList.get(catSelected - 3);
					itemList = store.getItemsForCategory(catSelected);
				}
				
				for (int m = 0; m < itemList.size() ; m ++)
			    {
			       item_name_list.add(itemList.get(m).getName());
			       item_price_list.add(itemList.get(m).getPrice());
			       item_number_list.add(itemList.get(m).getNumber());  
			    }
			    
			    session.setAttribute("ITEM_NAME_LIST", item_name_list );
			    session.setAttribute("ITEM_PRICE_LIST", item_price_list );
			    session.setAttribute("ITEM_NUMBER_LIST", item_number_list );
			    session.setAttribute("ITEM_LIST", itemList);
			    
			    request.setAttribute("ITEM_NAME_LIST", item_name_list );
			    request.setAttribute("ITEM_PRICE_LIST", item_price_list );
			    request.setAttribute("ITEM_NUMBER_LIST", item_number_list );
			    
			    if(catSelected == -1)
			    	request.setAttribute("ITEM_CAT", "Search ");
			    else
			    	request.setAttribute("ITEM_CAT", catFixed );
				   
			    request.setAttribute("ITEM_COUNT", itemList.size());
			    
			    request.getRequestDispatcher("/Item.jspx").include(request, response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else if((addToCart != null) && (submitOrder == null) && UpdateButton == null)
		{
			List<ItemBean> itemList = (List<ItemBean>) session.getAttribute("ITEM_LIST");
			ItemBean ib = itemList.get(Integer.parseInt(addToCart));
			int quant = Integer.parseInt(item_quantity);
			//ib.setQuantity(Integer.parseInt(item_quantity));
			cart.addToCart(ib, quant);
			cart.updateItemTotalPrice();
			session.setAttribute("AddToCart", null);
			session.setAttribute("itemAdded", "1");
		
			try
			{
				//HST, Shipping, Discount Threshold, Discount %
				cart.updateCart(HST, SHIPPING);
				session.setAttribute("SessionCart", cart);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			
		    request.setAttribute("RequestCartItems", cart.getItems());
		    request.setAttribute("RequestTotalCartPrice", cart.getTotal());
		    request.setAttribute("RequestHST", cart.getHST());
		    request.setAttribute("RequestShipping", cart.getShipping());
		    request.setAttribute("RequestGrandTotal", cart.getGrandTotal());

		    request.setAttribute("RequestCart", cart);
		    request.setAttribute("CartSize", cart.getItemListSize());
		    
		    request.getRequestDispatcher("/CartCheck.jspx").forward(request, response);
		}
		else if (UpdateButton != null) {
			// System.out.println("UPDATE BUTTON WORKIN FROM CART.JAVA");
			// System.out.println(request.getQueryString());
			String query = request.getQueryString();
			// String[] tokens = expression.split("\\=");
			String[] tokens = query.split("&");
			
			for (int i = 0; i < tokens.length - 1; i++) {
				String[] a = tokens[i].split("=");
				//System.out.println("Index in cart is : " + a[0]
				//		+ " and the quantity is : " + a[1]);
				String ItemNumber = a[0];
				int UpdateQuantity = Integer.parseInt(a[1]);
				cart.UpdateCartItems(ItemNumber, UpdateQuantity);
				cart.updateItemTotalPrice();
				//cart.
			}
			
			
			tokens = null ;
			cart.RefactorCart();
			session.setAttribute("update", null);
			session.setAttribute("SessionCart", cart);
			try {
				cart.updateCart(HST, SHIPPING);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			request.setAttribute("RequestCartItems", cart.getItems());
			request.setAttribute("RequestTotalCartPrice", cart.getTotal());
			request.setAttribute("RequestHST", cart.getHST());
			request.setAttribute("RequestShipping", cart.getShipping());
			request.setAttribute("RequestGrandTotal", cart.getGrandTotal());

			request.setAttribute("RequestCart", cart);
			request.setAttribute("CartSize", cart.getItemListSize());

			request.getRequestDispatcher("/CartCheck.jspx").forward(
					request, response);

			// System.out.println(values.length);
			// System.out.println(a[0].toString());
			// System.out.println(a[1].toString());
			// System.out.println(values[1].toString());
		}
		//Once check out is complete, use the checkout button
		//LOGIN USING AUTH SERVER!
		//Create a name for the PO, use the account number
		else if (submitOrder != null)
		{
			System.out.println("Genrating XML...");
			System.out.println(cart.getItemListSize());
			try
			{
				String customerNumber = cart.getCustomer().getNumber().substring(3);
				String path = this.getServletContext().getRealPath("/PO/");
				File folder = new File(path);
				File[] listOfFiles = folder.listFiles();
				
				int orderNum = 1;
				for (int i = 0; i < listOfFiles.length; i++)
				{
					if (listOfFiles[i].isFile() && listOfFiles[i].getName().contains(customerNumber))
						orderNum++;
				}
				cart.setId(orderNum);
				cartProcess.checkout(orderNum, cart, this.getServletContext().getRealPath("/PO/PO"+customerNumber+"_"+orderNum+".xml"));
				session.setAttribute("submitOrder", null);
			    request.setAttribute("RequestCart", cart);
			    session.setAttribute("SessionCart", cart);
			    request.setAttribute("name", cart.getCustomer().getName());
			    request.setAttribute("POpage", "PO"+customerNumber+"_"+orderNum+".xml");

			    request.getRequestDispatcher("/LoginStatus.jspx").forward(request, response);
			} catch (JAXBException e)
			{
				e.printStackTrace();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		    //request.getRequestDispatcher("http://localhost:4413/eFoods/PO/testPO.xml").forward(request, response);
		}
	}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(request, response);
	}

}