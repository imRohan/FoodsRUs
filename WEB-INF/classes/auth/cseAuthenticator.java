package auth;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class cseAuthenticator extends Authenticator 
{    
    private String username;    
    private String password;
    
    public cseAuthenticator(String username, String password) 
    {    
        super();    
        
        this.username = username;    
        this.password = password;    
    }    
    
    public PasswordAuthentication getPasswordAuthentication() 
    {       
        return new PasswordAuthentication(username, password.toCharArray());    
    } 
}