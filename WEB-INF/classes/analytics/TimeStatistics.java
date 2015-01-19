package analytics;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionIdListener;
import javax.servlet.http.HttpSessionListener;

/**
 * Application Lifecycle Listener implementation class TimeStatistics
 *
 */
@WebListener
public class TimeStatistics implements ServletContextListener, ServletContextAttributeListener, HttpSessionListener, HttpSessionAttributeListener, HttpSessionActivationListener, HttpSessionBindingListener, HttpSessionIdListener, ServletRequestListener, ServletRequestAttributeListener, AsyncListener {

	private int totalClients;
	private long totalTime;
	private long itemTotalTime;
	private List<Long> timeList = new ArrayList<Long>();
	private List<Long> itemTimeList = new ArrayList<Long>();
	ServletContext ctx;
    /**
     * Default constructor. 
     */
    public TimeStatistics() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see AsyncListener#onComplete(AsyncEvent)
     */
    public void onComplete(AsyncEvent arg0) throws java.io.IOException { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletRequestListener#requestDestroyed(ServletRequestEvent)
     */
    public void requestDestroyed(ServletRequestEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see HttpSessionAttributeListener#attributeAdded(HttpSessionBindingEvent)
     */
    public void attributeAdded(HttpSessionBindingEvent arg0)  { 
    	HttpSession session = arg0.getSession();   	
    	if (((String) session.getAttribute("submitOrder") != null) && (totalClients > timeList.size() ))
    	{
    		long clientCheckoutTime = System.currentTimeMillis()/1000;
    		long clientEntryTime = (long) session.getAttribute("clientEntryTime");
    		long timeTaken = clientCheckoutTime - clientEntryTime;
    		System.out.println("Entry = " + clientEntryTime);
    		System.out.println("Exit = "+ clientCheckoutTime);
    		System.out.println("Time Taken = "+ timeTaken);
    		timeList.add(timeTaken);
    		totalTime += timeTaken;
    		ctx.setAttribute("timeList", timeList);
    		ctx.setAttribute("totalTime", totalTime);
    		System.out.println("Time List size = " + timeList.size());
    	}
    	
    	if (((String) session.getAttribute("itemAdded") != null) && ((String) session.getAttribute("submitOrder") == null))
    	{
    		long itemPickedTime = System.currentTimeMillis()/1000;
    		long clientEntryTime = (long) session.getAttribute("clientEntryTime");
    		long itemPicked = itemPickedTime - clientEntryTime;
    		System.out.println("Entry = " + clientEntryTime);
    		System.out.println("Item Picked = "+ itemPickedTime);
    		System.out.println("Time Taken = "+ itemPicked);
    		itemTimeList.add(itemPicked);
    		itemTotalTime += itemPicked;
    		ctx.setAttribute("itemTotalTime", itemTotalTime);
    		ctx.setAttribute("itemTimeList", itemTimeList);
    		System.out.println("Item Time List size = " + itemTimeList.size());
   		}
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
    	totalClients = 0;
    	arg0.getServletContext().setAttribute("totalClients", totalClients);
    	arg0.getServletContext().setAttribute("timeList", timeList);
    	arg0.getServletContext().setAttribute("itemTimeList", itemTimeList);
    	arg0.getServletContext().setAttribute("totalTime", totalTime);
    	arg0.getServletContext().setAttribute("itemTotalTime", itemTotalTime);

    	ctx = arg0.getServletContext();
    }

	/**
     * @see HttpSessionActivationListener#sessionDidActivate(HttpSessionEvent)
     */
    public void sessionDidActivate(HttpSessionEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see HttpSessionBindingListener#valueBound(HttpSessionBindingEvent)
     */
    public void valueBound(HttpSessionBindingEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextAttributeListener#attributeAdded(ServletContextAttributeEvent)
     */
    public void attributeAdded(ServletContextAttributeEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see AsyncListener#onTimeout(AsyncEvent)
     */
    public void onTimeout(AsyncEvent arg0) throws java.io.IOException { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextAttributeListener#attributeRemoved(ServletContextAttributeEvent)
     */
    public void attributeRemoved(ServletContextAttributeEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see HttpSessionAttributeListener#attributeRemoved(HttpSessionBindingEvent)
     */
    public void attributeRemoved(HttpSessionBindingEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see AsyncListener#onStartAsync(AsyncEvent)
     */
    public void onStartAsync(AsyncEvent arg0) throws java.io.IOException { 
         // TODO Auto-generated method stub
    }

	/**
     * @see AsyncListener#onError(AsyncEvent)
     */
    public void onError(AsyncEvent arg0) throws java.io.IOException { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletRequestAttributeListener#attributeAdded(ServletRequestAttributeEvent)
     */
    public void attributeAdded(ServletRequestAttributeEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see HttpSessionIdListener#sessionIdChanged(HttpSessionEvent, String)
     */
    public void sessionIdChanged(HttpSessionEvent arg0, String arg1)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see HttpSessionBindingListener#valueUnbound(HttpSessionBindingEvent)
     */
    public void valueUnbound(HttpSessionBindingEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see HttpSessionActivationListener#sessionWillPassivate(HttpSessionEvent)
     */
    public void sessionWillPassivate(HttpSessionEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent arg0)  { 
         totalClients++;
         System.out.println("Total Clients = " + totalClients);
         HttpSession session = arg0.getSession();
         long now = System.currentTimeMillis()/1000;
         session.setAttribute("clientEntryTime", now);
    }

	/**
     * @see HttpSessionAttributeListener#attributeReplaced(HttpSessionBindingEvent)
     */
    public void attributeReplaced(HttpSessionBindingEvent arg0)  {
    /*	HttpSession session = arg0.getSession();   	
    	if (((String) session.getAttribute("itemAdded") != null))
    	{
    		System.out.println("Item picked again");
    		long itemPickedTime = System.currentTimeMillis()/1000;
    		long clientEntryTime = (long) session.getAttribute("clientEntryTime");
    		long itemPicked = itemPickedTime - clientEntryTime;
    		System.out.println("Entry = " + clientEntryTime);
    		System.out.println("Item Picked = "+ itemPickedTime);
    		System.out.println("Time Taken = "+ itemPicked);
    		itemTimeList.add(itemPicked);
    		ctx.setAttribute("itemTimeList", itemTimeList);
    		System.out.println("Item Time List size = " + itemTimeList.size());
		}
	*/
    }

	/**
     * @see ServletContextAttributeListener#attributeReplaced(ServletContextAttributeEvent)
     */
    public void attributeReplaced(ServletContextAttributeEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletRequestAttributeListener#attributeRemoved(ServletRequestAttributeEvent)
     */
    public void attributeRemoved(ServletRequestAttributeEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletRequestAttributeListener#attributeReplaced(ServletRequestAttributeEvent)
     */
    public void attributeReplaced(ServletRequestAttributeEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletRequestListener#requestInitialized(ServletRequestEvent)
     */
    public void requestInitialized(ServletRequestEvent arg0)  { 
         // TODO Auto-generated method stub
    }
	
}
