package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import beans.ItemBean;

public class CartBean 
{
	private int id;
	private Date submitted;
	private CustomerBean customer;
	private double total;
	private double shipping;
	private double HST;
	private double grandTotal;
	private boolean discountApplied;
	private HashMap<String, ItemBean> items;
	private static final Double DISCOUNT_VALUE = 100.0;
	
	/*
	 * The cart bean is going to contain a list of 
	 * items. The items can be added or taken out of the 
	 * cart.
	 * Just the empty constructor is defined, since all the items
	 * are dynamic and have been provided with functions to calculate
	 * the price, and the items will be added by the user in
	 * each case we don't need the constructor where the price and
	 * items are added. 
	 */
	public CartBean() {
		this.items = new HashMap<String, ItemBean>();
	}

	public CartBean(int id, Date submitted, CustomerBean customer, HashMap<String, ItemBean> items, double total, double shipping, double hST, double grandTotal) {
		this.id = id;
		this.submitted = submitted;
		this.customer = customer;
		this.items = new HashMap<String, ItemBean>();
		this.total = total;
		this.shipping = shipping;
		this.HST = hST;
		this.grandTotal = grandTotal;
		this.discountApplied = false;
	}

	/**
	 * @return the Hash Map itself.
	 */
	public HashMap<String, ItemBean> getItems()
	{
		return this.items;
	}
	
	/**
	 * @return the list of items in the Hash Map
	 */
	
	public List<ItemBean> getItemsList()
	{		
		// Converting HashMap Values into ArrayList
		List<ItemBean> valueList = new ArrayList<ItemBean>(items.values());
		System.out.println("\n==> Size of Value list: " + valueList.size());
		for (ItemBean temp : valueList) {
			System.out.println(temp);
		}
		
		return valueList;
	}
	/**
	 * Might be needed for development for passing the
	 * the cart around.
	 * @param items
	 */
	public void setItems(List<ItemBean> items)
	{
		for(int i = 0; i < items.size(); i++)
		{
			this.addToCart(items.get(i),items.get(i).getQuantity());
		}
	}
	 
	/*
	 * function not preferred, should not be 
	 * able to set without calculating. 
	 * Check the function following the commented section.
	 * 
	protected void setPrice(double price)
	{
		this.price = price;
	}*/
	public void updateItemTotalPrice()
	{
		Iterator<ItemBean> itr= this.items.values().iterator();
		while(itr.hasNext())
		{
			ItemBean currItem = itr.next();
			currItem.setPriceTotal(currItem.computePriceTotal());
			
		}	
	}
	/**
	 * 
	 * Note that both calculate and getPrice return the total
	 * price of the cart, however make sure to use the 
	 * calculatePrice before using getPrice, otherwise the price
	 * will be 0.0; The value is set only after it has been calculated.
	 * 
	 * @return the total price of the cart
	 */
	
	public double getTotalCartPrice()
	{
		double total = 0.0;
		updateItemTotalPrice();
		Iterator<ItemBean> itr= this.items.values().iterator();
		while(itr.hasNext())
		{
			ItemBean item = itr.next();
			total += item.getPriceTotal();
		}
		  
		return total;
	}
	/**
	 * Return the size of item list
	 */
	public int getItemListSize(){
		return this.getItemsList().size();
	}
	
	
	/**
	 * 
	Add the given item to the cart.
	 * @param item  The item to be added to the cart
	 * @param quantity
	 * @return true if the item was successfully adder false otherwise.
	 */
	public boolean addToCart(ItemBean item, int quantity)
	{
		String num = item.getNumber();
		try
		{
				ItemBean found = this.items.get(num);
				if(found != null)
				{
					if (quantity == 0){
					  this.items.remove(num);
					  return false;
					}
					else{
					found.setQuantity(quantity);
					return true;
					}//this.items.get(num).setQuantity(item.getQuantity());
					
				}
				else
				{
					if (quantity == 0){  //not added since quantity is zero
					   return false;
					}
					else{
						item.setQuantity(quantity);
						this.items.put(item.getNumber(), item);
						return true;
					}
					
					
				}			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean removeItem(ItemBean item)
	{
		String num = item.getNumber();
		try
		{
			ItemBean found = this.items.get(num);
			if(found != null)
			{
				if(found.getQuantity() == 0)
				{
					this.items.remove(found.getNumber());
				}
				return true;
			}
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public Date getSubmitted()
	{
		return submitted;
	}

	public void setSubmitted(Date submitted)
	{
		this.submitted = submitted;
	}

	public CustomerBean getCustomer()
	{
		return customer;
	}

	public void setCustomer(CustomerBean customer)
	{
		this.customer = customer;
	}

	public double getTotal()
	{
		return total;
	}

	public void setTotal(double total)
	{
		this.total = total;
	}

	public double getShipping()
	{
		return shipping;
	}

	public void setShipping(double shipping)
	{
		this.shipping = shipping;
	}

	public double getHST()
	{
		return HST;
	}

	public void setHST(double hST)
	{
		HST = hST;
	}

	public double getGrandTotal()
	{
		return grandTotal;
	}

	public void setGrandTotal(double grandTotal)
	{
		this.grandTotal = grandTotal;
	}

	public boolean isDiscountApplied()
	{
		return discountApplied;
	}

	public void setDiscountApplied(boolean discountApplied)
	{
		this.discountApplied = discountApplied;
	}
	
	/**
	 * Method processes the taxes and the shipping and puts these values back in the cart object
	 * @param basket
	 * @param client
	 * @param taxRate
	 * @param shipping
	 * @param discountAt
	 * @param discountRate
	 * @return
	 * @throws Exception
	 */
	
	public void updateCart(String taxRate, String shipping) throws Exception {		
		
		double hst = 0; 
		hst = Double.parseDouble(taxRate);
		double shipRate = 0;
		shipRate = Double.parseDouble(shipping);

		setTotal(Math.floor(getTotalCartPrice() * 100)/100);
		if (getTotalCartPrice() > DISCOUNT_VALUE || getTotalCartPrice() == 0){
			shipRate = 0.0;
			setDiscountApplied(true);
		}
		setShipping(Math.floor(shipRate*100)/100);
		setHST(Math.floor(getTotal() * (hst/100) *100)/100);
		setGrandTotal(Math.floor((getTotal() + getHST() + getShipping())*100)/100);
	}
	
public void UpdateCartItems(String ItemNumber , int quantity){
		
		
		//List<ItemBean> templist = this.getItemsList() ;
		//String key = templist.get(index).getNumber();
		if(this.items.containsKey(ItemNumber)){
			/*if (quantity == 0){
				  this.items.remove(key);
				}
				else{*/
				this.items.get(ItemNumber).HardSetQuantity(quantity);
				//}
		}
		
	}
	
	public void RefactorCart(){
	
		
		CartBean result  = new CartBean() ;
		Iterator<ItemBean> entries =  this.items.values().iterator();
		
		while (entries.hasNext()) {
			ItemBean bean = entries.next();
			String key = bean.getNumber();
			if(bean.getQuantity() == 0){
			    
			}
			else{
				result.getItems().put(bean.getNumber(), bean);
				
			}
			
		}
		
	   this.items = result.items;
	}
	
}
