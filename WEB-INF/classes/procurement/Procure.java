package procurement;

import java.util.HashMap;

public class Procure
{
	private static final String KEY = "4413secret";	
	private static final String TORONTO_WHOLESALER = "http://roumani.eecs.yorku.ca:4413/axis/YYZ.jws";
	private static final String VANCOUVER_WHOLESALER = "http://roumani.eecs.yorku.ca:4413/axis/YVR.jws";
	private static final String HALIFAX_WHOLESALER = "http://roumani.eecs.yorku.ca:4413/axis/YHZ.jws";
	
	private static final String PO_URL = "http://localhost:4413/eFoods/PO/";
	private static final String PROCURE_PATH = "/home/user/procurement/";

	public static void main(String[] args) throws Exception
	{
		
		Consolidator consolidate = new Consolidator();
		OrderService newOrder = new OrderService(TORONTO_WHOLESALER, VANCOUVER_WHOLESALER, HALIFAX_WHOLESALER, KEY);
		try
		{
			HashMap<String, Integer> orders = consolidate.processPO(PO_URL, PROCURE_PATH);
			newOrder.orderToWholesalers(orders);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}	
}