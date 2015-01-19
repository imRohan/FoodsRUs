package beans;

import java.security.InvalidParameterException;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.sun.xml.internal.txw2.annotation.XmlAttribute;

public class ItemBean {
	
	private double price;
	private String name;
	private String number;
	private int catid;
	private int quantity;
	private double priceTotal;
	//private double check;
	
	/**
	 * @param price
	 * @param name
	 * @param number
	 * @param catid
	 */
	public ItemBean(double price, String name, String number, int catid) {
		super();
		this.price = price;
		this.name = name;
		this.number = number;
		this.catid = catid;
		this.quantity = 0; //The first time it is created, the quantity is 1.
		this.priceTotal = 0.0;
	}
	
	public void setPriceTotal(double priceTotal)
	{
		this.priceTotal = Math.floor(priceTotal * 100) / 100;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		
		return Math.floor(price * 100) / 100;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	/**
	 * @return the name
	 */
	
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	/**
	 * @return the catid
	 */
	public int getCatid() {
		return catid;
	}
	/**
	 * @param catid the catid to set
	 */
	public void setCatid(int catid) {
		this.catid = catid;
	}
	
	/**
	 * 
	 * @return the quantity of this item.
	 */
	public int getQuantity() {
		return this.quantity;
	}
	public void HardSetQuantity(int quantity){
		this.quantity = quantity ;
	}
	/**
	 * Set the quantity of the item ordered
	 */
	public void setQuantity(int quantity){
		if(quantity < 0)
			throw new InvalidParameterException("Please enter quantity greater than 0!");
		this.quantity += quantity;
	}
	
	public double getPriceTotal()
	{
		return priceTotal;
	}

	public double computePriceTotal()
	{
		this.priceTotal = this.getPrice() * this.quantity;
		return this.priceTotal;
	}
	
	public String toString()
	{
		String item = "Price: "+this.getPrice()
					+ " Name: "+this.getName()
					+ " Number: "+this.getNumber()
					+ " CatId: "+this.catid
					+ " Quantity: "+this.getQuantity()
					+ " Total Price: "+this.getPriceTotal();
		return item;
	}

}
