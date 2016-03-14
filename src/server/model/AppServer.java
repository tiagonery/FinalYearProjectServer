package server.model;

import java.sql.SQLException;

import org.jivesoftware.smack.XMPPException;

import server.DAO.DAOManager;
import server.DAO.DAOManager.Table;
import server.DAO.UserDAO;
import server.gcm.GCMManager;


public class AppServer 
{
	private static final String PROJECT_ID = "956122336085";
	private static final String API_KEY = "AIzaSyBNbkKWCt1eOtI2W9aFv_95XKLYZTbGdjg"; 
	private static GCMManager gcmManager;
	
    public static void main( String[] args ) throws SQLException
    {
    	gcmManager = new GCMManager(PROJECT_ID, API_KEY, true);
    	try {
			gcmManager.connect();
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// crude loop to keep connection open for receiving messages
		while (true) {
			;
		}
		
    	
    }

	public static GCMManager getGcmManager() {
		return gcmManager;
	}
}