package procurement;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import order.ItemType;
import order.OrderType;

public class Consolidator
{

	public Consolidator()
	{
	}

	/**
	 * Method unmarshalls a given XML of an order to OrderType. This will be used by SOAP
	 * @param xml_filepath
	 * @return
	 * @throws JAXBException
	 */
	public OrderType unmarshallPOXML(String xml_filepath) throws JAXBException
	{
		JAXBContext xx = JAXBContext.newInstance(OrderType.class);
		Unmarshaller unmarshaller = xx.createUnmarshaller();
		File xml = new File(xml_filepath);
															
		OrderType order = (OrderType) unmarshaller.unmarshal(xml);
		Marshaller marshaller = xx.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		return order;		
	}
	
	/**
	 * Method combines multiples purchase orders into one HashMap of item ID number and their quantity
	 * This is all that is required at this point to put an order to wholesalers
	 * @param filepath
	 * @param map_number_quantity
	 * @throws JAXBException
	 */
	public void combinePO(String filepath, HashMap<String, Integer> order_map) throws JAXBException
	{
		OrderType order = unmarshallPOXML(filepath);
		List<ItemType> orders_list = order.getItems().getItem();
		for(ItemType item : orders_list)
		{
			String itemNum = item.getNumber();
			int new_quantity = item.getQuantity();
			if (order_map.containsKey(""+itemNum))
			{
				int qty = order_map.get(""+itemNum);
				qty += new_quantity;
				order_map.put(""+itemNum, qty);
			}
			else
			{
				order_map.put(""+itemNum, new_quantity);
			}
		}
	}
	
	/**
	 * Fetches the PO XMLs from the web application and puts them in a temp location
	 * Processes them to purchase orders
	 * @param URL
	 * @param procurePath
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Integer> processPO(String URL, String procurePath) throws Exception
	{
		//Create the temp dir in not there
		File dir = new File(procurePath);
		if (!dir.exists()){
			dir.mkdirs();
		}
		
		//get files from web application server
		String wgetFiles = "/usr/bin/wget -P "+procurePath+" -r -nH --cut-dirs=2 --no-parent --reject=\"index.html*\" http://localhost:4413/eFoods/PO/";
	
		try
		{
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(wgetFiles);
			proc.waitFor();
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
		
		//process the XMLs
		File directory = new File(procurePath);
		File[] files = directory.listFiles();
		List<String> list = new ArrayList<String>();
		for(File file : files)
		{
			if (file.getAbsolutePath().contains(".xml"))
			{
				System.out.println(file.getAbsolutePath());
				list.add(file.getAbsolutePath());
			}
		}
		
		HashMap<String, Integer> order_map = new HashMap<String, Integer>();
		for(String filepath : list)
		{
			combinePO(filepath, order_map);
		}
		
		//delete files after
		for(File file : files)
		{
			file.delete();
		}
		
		return order_map;
	}
	
}
