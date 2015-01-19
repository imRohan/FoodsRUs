package model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.stream.StreamResult;

import beans.CartBean;
import beans.ItemBean;
import order.CustomerType;
import order.ItemType;
import order.ItemsListType;
import order.OrderType;

public class CartProcess
{
	List<ItemBean> cart_items_final;
	private final String xslt = "../res/xsl/PO.xsl";
	
	/**
	 * Constructor method to take total price of cart and start the cart process of
	 * calculating the taxes and shipping and generating the PO
	 * @param cart_total_price
	 * @param cart_items_final
	 */

	public CartProcess(List<ItemBean> cart_items_final)
	{
		this.cart_items_final = cart_items_final;
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
/*	
	public CartBean processCart(CartBean basket, CustomerBean client, String taxRate, String shipping, String discountAt, String discountRate) throws Exception {		
		
		double hst = 0; 
		hst = Double.parseDouble(taxRate);
		double shipRate = 0;
		shipRate = Double.parseDouble(shipping);
		double discount = 0;
		discount = Double.parseDouble(discountRate);
		double valDiscount = 0;
		valDiscount = Double.parseDouble(discountAt);

		basket.setCustomer(client);
		basket.setTotal(this.cart_total_price);
		if (this.cart_total_price > valDiscount){
			shipRate = shipRate - discount;
			basket.setDiscountApplied(true);
		}
		basket.setShipping(shipRate);
		basket.setHST(basket.getTotal() * (hst/100));
		basket.setGrandTotal(basket.getTotal() + basket.getHST() + basket.getShipping());
		return basket;
	}
*/	
	/**
	 * Method creates an order for the given cart when the customer wants to check out
	 * @param orderid
	 * @param now
	 * @param cart
	 * @return
	 * @throws Exception
	 */
	
	
	private OrderType createOrder(int orderid, Date now, CartBean cart) throws Exception {
		OrderType lw = new OrderType();
		// attributes of <order>
		lw.setId(new BigInteger("" + orderid));

		GregorianCalendar c = new GregorianCalendar();
		c.setTime(now);
		XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		lw.setSubmitted(date);

		// customer information
		CustomerType customer = new CustomerType();
		customer.setAccount(cart.getCustomer().getNumber() + "");
		customer.setName(cart.getCustomer().getName());
		lw.setCustomer(customer);

		// items
		ItemsListType items = new ItemsListType();
		List<ItemType> listItems = new ArrayList<ItemType>();
		for (ItemBean tmp : cart.getItemsList()) {
			ItemType item = new ItemType();

			item.setNumber(tmp.getNumber());
			item.setName(tmp.getName());
			item.setPrice(new Double("" + tmp.getPrice()));
			item.setQuantity(new Integer("" + tmp.getQuantity()));
			item.setExtended(new Double("" + (tmp.getPrice() * tmp.getQuantity())));

			listItems.add(item);
		}
		items.setItem(listItems);
		lw.setItems(items);

		// remaining elements
		lw.setTotal(new Double("" + cart.getTotal()));
		lw.setShipping(new Double("" + cart.getShipping()));
		lw.setHst(new Double("" + cart.getHST()));
		lw.setGrandTotal(new Double("" + cart.getGrandTotal()));
		return lw;
	}
	
	/**
	 * Method completes the purchase process by generating a purchase order XML
	 * @param orderNum
	 * @param cart
	 * @param PO_filename
	 * @throws Exception
	 */
	
	public void checkout(int orderNum, CartBean cart, String PO_filename) throws Exception {
		Date now = new Date();

		OrderType lw = createOrder(orderNum, now, cart);
		JAXBContext jc = JAXBContext.newInstance(lw.getClass());
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

		  //String writer for writing to file
		  StringWriter sw = new StringWriter();
		  sw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		  sw.write("<?xml-stylesheet type=\"text/xsl\" href=\"" + xslt + "\"?>\n");
		  marshaller.marshal(lw, new StreamResult(sw));
		  
		  System.out.println(sw.toString()); // for debugging
		  FileWriter fw = new FileWriter(PO_filename);
		  fw.write(sw.toString());
		  fw.close();
	}

}
