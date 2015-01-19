package procurement;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

public class OrderService
{
	private String wsdl_Toronto, wsdl_Vancouver, wsdl_Halifax, key;

	/**
	 * Constructor
	 * @param toronto
	 * @param vancouver
	 * @param halifax
	 * @param key
	 */
	public OrderService(String toronto, String vancouver, String halifax, String key)
	{
		this.wsdl_Toronto = toronto;
		this.wsdl_Vancouver = vancouver;
		this.wsdl_Halifax = halifax;
		this.key = key;
	}
	
	/**
	 * Method places orders to wholesalers
	 * @param order_map
	 * @throws Exception
	 */
	
	public void orderToWholesalers(HashMap<String, Integer> order_map) throws Exception
	{
		String out = "";
		
		out += "<html><body>";
		out += "<h1>Order Details</h1>";
		out += "<table border=\"1\" cellpadding=\"3\">";
		out += "<tbody><tr>";
		out += "<th>"	+	"Order No."			+	"</th>";
		out += "<th>"	+	"Item Name"			+	"</th>";
		out += "<th>"	+	"Item No."			+	"</th>";
		out += "<th>"	+	"Unit Price"			+	"</th>";		
		out += "<th>"	+	"Quantity"		+	"</th>";
		out += "<th>"	+	"Total Price"		+	"</th>";
		out += "<th>"	+	"Wholesaler"	+	"</th>";
		out += "<th>"	+	"Confirmation#"	+	"</th>";
		
		out += "</tr>";
		
		Set<String> map_keys = order_map.keySet();
		Iterator<String> it = map_keys.iterator();
		
		int i = 0;
		while (it.hasNext())
		{
			i++;
			
			String itemNumber = it.next();
			int quantity = order_map.get(""+itemNumber);
			
			//Find lowest price wholesaler
			int bestOption = getLowestPriceWholesaler(itemNumber);
			String itemName = getNameFromWholesaler(itemNumber, bestOption);
			double price = getPriceFromWholesaler(itemNumber, bestOption);
			
			price = (double)Math.round(price * 100) / 100;
			double extended_price = (double)Math.round(price*(double)quantity * 100) / 100;
			NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
			
			//Place the order
			String confirmation = placeOrder(itemNumber, bestOption, quantity);
			String confirmation_number = confirmation.substring(confirmation.indexOf("#")+2);
			
			String wholesaler = "";
			if (bestOption == 1) 
				wholesaler = "Toronto";
			if (bestOption == 2) 
				wholesaler = "Vancouver";
			if (bestOption == 3) 
				wholesaler = "Halifax";
						
			out += "<td>"	+	i					+	"</td>";
			out += "<td>"	+	itemName					+	"</td>";
			out += "<td>"	+	itemNumber					+	"</td>";
			out += "<td>"	+	nf.format(price)			+	"</td>";
			out += "<td>"	+	quantity					+	"</td>";
			out += "<td>"	+	nf.format(extended_price)	+	"</td>";
			out += "<td>"	+	wholesaler					+	"</td>";
			out += "<td>"	+	confirmation_number			+	"</td>";
			
			out += "</tr>";				
			
		}
		
		out += "</tbody></table>";
		out += "</html></body>";
		generateReport(out, "orderDetails.html");
		
	}
	/**
	 * Method generates a report for FoodsRUs to see all orders that have been placed to wholesalers
	 * @param html
	 * @param filepath
	 * @throws Exception
	 */
	
	private void generateReport(String html, String filepath) throws Exception
	{
		File file = new File(filepath);
		FileWriter writer = new FileWriter(file);
		writer.write(html);
		writer.close();
		
		Desktop.getDesktop().open(file);
	}
	

	/**
	 * Method returns the wholesaler who sells the given item for the lowest price
	 * @param itemNumber
	 * @return
	 * @throws Exception
	 */
	private int getLowestPriceWholesaler(String itemNumber) throws Exception
	{
		double priceYYZ = getPriceFromWholesaler(itemNumber, wsdl_Toronto);
		double priceYVR = getPriceFromWholesaler(itemNumber, wsdl_Vancouver);
		double priceYHZ = getPriceFromWholesaler(itemNumber, wsdl_Halifax);
		
		//When price has not been determined, give the highest price in order to get the lowest price
		if (priceYYZ == -1) 
			priceYYZ = Double.MAX_VALUE;
		if (priceYVR == -1) 
			priceYVR = Double.MAX_VALUE;
		if (priceYHZ == -1) 
			priceYHZ = Double.MAX_VALUE;
		
		//If Toronto sells it for the lowest
		if (priceYYZ != -1 && priceYYZ <= priceYVR && priceYYZ <= priceYHZ)
		{
			return 1;
		}
		//If Vancouver sells it for the lowest
		else if (priceYVR != -1 && priceYVR <= priceYYZ && priceYVR <= priceYHZ)
		{
			return 2;
		}
		//If Halifax sells it for the lowers
		else if (priceYHZ != -1 && priceYHZ <= priceYYZ && priceYHZ <= priceYVR)
		{
			return 3;
		}
		return 0;
	}
	
	/**
	 * Method places the order for a given item number and quantity to the chosen cheapest wholesaler
	 * @param itemNumber
	 * @param bestOption
	 * @param quantity
	 * @return
	 * @throws Exception
	 */
	private String placeOrder(String itemNumber, int bestOption, int quantity) throws Exception
	{
		String wsdl = "";
		if (bestOption == 1) 
			wsdl = wsdl_Toronto;
		if (bestOption == 2) 
			wsdl = wsdl_Vancouver;
		if (bestOption == 3) 
			wsdl = wsdl_Halifax;
		
		SOAPMessage msg = MessageFactory.newInstance().createMessage();
		MimeHeaders mh = msg.getMimeHeaders();
		mh.addHeader("SOAPAction", "");
		SOAPPart soap = msg.getSOAPPart();
		SOAPEnvelope envelope = soap.getEnvelope();
		SOAPBody body = envelope.getBody();	
		
		SOAPElement root = body.addChildElement("order");
		root.addChildElement("itemNumber").addTextNode(itemNumber);
		root.addChildElement("quantity").addTextNode(""+quantity);
		root.addChildElement("key").addTextNode(key);
		
		SOAPConnection sc = SOAPConnectionFactory.newInstance().createConnection();
		SOAPMessage resp = sc.call(msg, new URL(wsdl));
		sc.close();
		
		org.w3c.dom.Node node = resp.getSOAPPart().getEnvelope().getBody().getElementsByTagName("orderReturn").item(0);
		return node.getTextContent();		
	}

	/**
	 * Method returns the item name of procured item from the wholesaler. Remember we do not take this from XML.
	 * @param itemNumber
	 * @param bestOption
	 * @return
	 * @throws Exception
	 */
	private String getNameFromWholesaler(String itemNumber, int bestOption) throws Exception
	{
		String wsdl = "";
		
		if (bestOption == 1) wsdl = wsdl_Toronto;
		if (bestOption == 2) wsdl = wsdl_Vancouver;
		if (bestOption == 3) wsdl = wsdl_Halifax;
		
		SOAPMessage msg = MessageFactory.newInstance().createMessage();
		MimeHeaders header = msg.getMimeHeaders();
		header.addHeader("SOAPAction", "");
		SOAPPart soap = msg.getSOAPPart();
		SOAPEnvelope envelope = soap.getEnvelope();
		SOAPBody body = envelope.getBody();	
		
		SOAPElement root = body.addChildElement("getName");
		root.addChildElement("itemNumber").addTextNode(itemNumber);
		
		SOAPConnection sc = SOAPConnectionFactory.newInstance().createConnection();
		SOAPMessage resp = sc.call(msg, new URL(wsdl));
		sc.close();
		
		org.w3c.dom.Node node = resp.getSOAPPart().getEnvelope().getBody().getElementsByTagName("getNameReturn").item(0);
		return node.getTextContent();		
	}

	/**
	 * Method returns the item price for a given item from a given wholesaler
	 * @param itemNumber
	 * @param bestOption
	 * @return
	 * @throws Exception
	 */
	private double getPriceFromWholesaler(String itemNumber, int bestOption) throws Exception
	{
		String wsdl = "";
		
		if (bestOption == 1) wsdl = wsdl_Toronto;
		if (bestOption == 2) wsdl = wsdl_Vancouver;
		if (bestOption == 3) wsdl = wsdl_Halifax;
		
		SOAPMessage msg = MessageFactory.newInstance().createMessage();
		MimeHeaders header = msg.getMimeHeaders();
		header.addHeader("SOAPAction", "");
		SOAPPart soap = msg.getSOAPPart();
		SOAPEnvelope envelope = soap.getEnvelope();
		SOAPBody body = envelope.getBody();	
		
		SOAPElement root = body.addChildElement("quote");
		root.addChildElement("itemNumber").addTextNode(itemNumber);
		SOAPConnection sc = SOAPConnectionFactory.newInstance().createConnection();
		SOAPMessage resp = sc.call(msg, new URL(wsdl));
		sc.close();
		
		org.w3c.dom.Node node = resp.getSOAPPart().getEnvelope().getBody().getElementsByTagName("quoteReturn").item(0);
		double result = -1;
		try
		{
			result = Double.parseDouble(node.getTextContent());
		} catch (Exception e)
		{
			System.out.println(e);
		}		
		return result;	
	}
	
	/**
	 * Method return the price of the item for a given item number for a given wholesaler name (wsdl)
	 * @param itemNumber
	 * @param wsdl
	 * @return
	 * @throws Exception
	 */
	private double getPriceFromWholesaler(String itemNumber, String wsdl) throws Exception
	{
		SOAPMessage msg = MessageFactory.newInstance().createMessage();
		MimeHeaders header = msg.getMimeHeaders();
		header.addHeader("SOAPAction", "");
		SOAPPart soap = msg.getSOAPPart();
		SOAPEnvelope envelope = soap.getEnvelope();
		SOAPBody body = envelope.getBody();	
		
		SOAPElement root = body.addChildElement("quote");
		root.addChildElement("itemNumber").addTextNode(itemNumber);		
		SOAPConnection sc = SOAPConnectionFactory.newInstance().createConnection();
		SOAPMessage resp = sc.call(msg, new URL(wsdl));
		sc.close();
		
		org.w3c.dom.Node node = resp.getSOAPPart().getEnvelope().getBody().getElementsByTagName("quoteReturn").item(0);
		double result = -1;
		try
		{
			result = Double.parseDouble(node.getTextContent());
		} catch (Exception e)
		{
			System.out.println(e);
		}
		
		return result;		
	}


}
