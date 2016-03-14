/**
 * 
 */
package server.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.InitialContext;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * @author Tiago
 *
 */
public class DAOManager {  
	
	//Private
    private MysqlDataSource src;
    private Connection con;
    
    //static reference to itself
	private static DAOManager instance = new DAOManager();
    public static final String URL = "jdbc:mysql://localhost/finalyearapp";
    public static final String USER = "root";
    public static final String PASSWORD = "";
    public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver"; 
    
    
    public enum Table { USER, EVENT, USER_EVENT }
    
    private DAOManager(){
       try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//            try {
//            	MysqlDataSource dataSource = new MysqlDataSource();
//                dataSource.setUser("host");
//                dataSource.setPassword("");
//                dataSource.setServerName("localhost");
//                dataSource.setPort(3306);
//                dataSource.setDatabaseName("finalyearapp");
//                this.src = dataSource;
//
//              } catch (Exception e) {
//                System.out.println("SetupJNDIDataSource err: " + e.getMessage());
//                e.printStackTrace();
//              }
//        catch(Exception e) { throw e; }
    }
    private Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("ERROR: Unable to Connect to Database.");
        }
        return connection;
    }  
    
	public static Connection getConnection() {
		return instance.createConnection();
		}  

	
	
    public void open() throws SQLException {
        try
        {
            if(this.con==null || this.con.isClosed())
                this.con = src.getConnection();
        }
        catch(SQLException e) { throw e; }
    }

    public void close() throws SQLException {
        try
        {
            if(this.con!=null && !this.con.isClosed())
                this.con.close();
        }
        catch(SQLException e) { throw e; }
    }

//    private static class DAOManagerSingleton {
//
//        public static ThreadLocal<DAOManager> INSTANCE;
//        static
//        {
//            ThreadLocal<DAOManager> dm;
//            try
//            {
//                dm = new ThreadLocal<DAOManager>(){
//                    @Override
//                    protected DAOManager initialValue() {
//                        try
//                        {
//                            return new DAOManager();
//                        }
//                        catch(Exception e)
//                        {
//                            return null;
//                        }
//                    }
//                };
//            }
//            catch(Exception e){
//                dm = null;
//            INSTANCE = dm;
//            }
//        }        
//
//    }
    
    public AbstractDAO getDAO(Table t) throws SQLException 
    {

        try
        {
            if(this.con == null || this.con.isClosed()) //Let's ensure our connection is open   
                this.open();
        }
        catch(SQLException e){ throw e; }

        switch(t)
        {
        case USER:
//            return new UserDAO(this.con);
//        case EVENT:
//            return new SecondDAO(this.con);
//        case USER_EVENT:
//            return new SecondDAO(this.con);
        default:
            throw new SQLException("Trying to link to an unexistant table.");
        }

    }
}
