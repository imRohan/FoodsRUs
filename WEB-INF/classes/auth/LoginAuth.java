package auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

public class LoginAuth extends Authenticator 
{    
    private String username;    
    private String password;
    
    public LoginAuth(String username, String password) 
    {    
        super();    
        
        this.username = username;    
        this.password = password;    
    }    
    
    public String authenticate(String username, String password) throws IOException 
	{
		String cse_page = "http://www.cse.yorku.ca/~cse92012/4413/login.cgi";
		BufferedReader br;
		String name = null;
		try {    
	            Authenticator.setDefault(new cseAuthenticator(username, password));    
	            URL url = new URL(cse_page);    
	            int responseCode = 0;
	            HttpURLConnection connection = (HttpURLConnection)url.openConnection(); 
	            connection.setConnectTimeout(2000);
	            
	            try
	            {
	            	long a = System.currentTimeMillis();
	            	connection.connect();
	            	responseCode = connection.getResponseCode();

	            	long b = System.currentTimeMillis();
	            	System.out.println("The time taken is"+(b-a));
	            	System.out.println(responseCode);
	            }
	            catch(Exception e)
	            {
	            	e.printStackTrace();
	            }
	            if(responseCode == 200)
	            {
	            	String line;
	            	br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	            	while ((line = br.readLine()) != null) 
	            	{
	            		if(line.contains("Name:"))
	            			name = line.substring(line.lastIndexOf("Name:")+"Name:".length()).trim();
	            	}
	            	System.out.println(name);
	            	connection.disconnect();
	            	return name;
	            }
	            connection.disconnect();
	            return name;
		 	} catch (Exception e) {    
	            e.printStackTrace();    
	        }    
		 return name;
	}
}