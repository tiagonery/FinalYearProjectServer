/**
 * 
 */
package server.DAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Tiago
 *
 */
public abstract class AbstractDAO {


    protected Connection connection;
    protected Statement statement;
	
    public abstract int count() throws SQLException; 

    //Protected
    protected final DAOManager.Table tableName;
    protected Connection con;

    protected AbstractDAO(Connection con, DAOManager.Table tableName) {
        this.tableName = tableName;
        this.con = con;
    }

}
