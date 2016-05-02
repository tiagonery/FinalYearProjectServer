/**
 * 
 */
package server.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import server.model.User;

/**
 * @author Tiago
 * 
 */
public class UserDAO /** extends AbstractDAO */
{

	private static final String USER_TABE = "user";
	private static final String USER_ID_COLUMN = "id";
	private static final String USER_FACEBOOK_ID_COLUMN = "facebookid";
	private static final String USER_NAME_COLUMN = "name";
	private static final String USER_SURNAME_COLUMN = "surname";
	private Connection connection;
	private Statement statement;


	/**
	 * @param con
	 * @param tableName
	 */
	public UserDAO() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.DAO.GenericDAO#count()
	 */
	// @Override
	// public int count() throws SQLException {
	// String query = "SELECT COUNT(*) AS count FROM "+this.tableName;
	// PreparedStatement counter;
	// try
	// {
	// counter = this.con.prepareStatement(query);
	// ResultSet res = counter.executeQuery();
	// res.next();
	// return res.getInt("count");
	// }
	// catch(SQLException e){ throw e; }
	//
	// }
	//

	public User getUser(String userRegId)  {
		String query = "SELECT * FROM user WHERE "+USER_ID_COLUMN+" = '" + userRegId+"'";
		ResultSet rs = null;
		User user = null;
		try {
			connection = DAOManager.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				user = new User(userRegId,rs.getNString(USER_FACEBOOK_ID_COLUMN),rs.getNString(USER_NAME_COLUMN),rs.getNString(USER_SURNAME_COLUMN) );
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DbUtil.close(rs);
			DbUtil.close(statement);
			DbUtil.close(connection);
		}
		return user;
	}


	/**
	 * @param surname2
	 * 
	 */
	public User createNewUser(String id, String facebookId, String name, String surname) {
		String query = "INSERT INTO " + USER_TABE + " VALUES ('" + id + "', '" + facebookId + "', '" + name + "', '" + surname + "');";
		int rs; 
		User user = null;
		connection = DAOManager.getConnection();
		try {
			statement = connection.createStatement();
			rs = statement.executeUpdate(query);
			user = new User(id,facebookId,name, surname);
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DbUtil.close(statement);
			DbUtil.close(connection);
		}
		return user;

	}

	/**
	 * @param id
	 * @return
	 */
	public boolean deleteUser(String id) {
		String query = "DELETE FROM "+USER_TABE+" WHERE "+USER_ID_COLUMN+" = '"+id+"';";
		boolean result = true;
		connection = DAOManager.getConnection();
		try {
			statement = connection.createStatement();
			statement.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		} finally {
			DbUtil.close(statement);
			DbUtil.close(connection);
		}
		return result;
	}

	/**
	 * @param facebookID
	 * @return
	 */
	public User getUserByFB(String facebookID) {
		String query = "SELECT * FROM user WHERE "+USER_FACEBOOK_ID_COLUMN+" = " + facebookID;
		ResultSet rs = null;
		User user = null;
		try {
			connection = DAOManager.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				user = new User(rs.getNString(USER_ID_COLUMN),facebookID,rs.getNString(USER_NAME_COLUMN),rs.getNString(USER_SURNAME_COLUMN) );
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DbUtil.close(rs);
			DbUtil.close(statement);
			DbUtil.close(connection);
		}
		return user;
	}

	/**
	 * @param userName
	 * @return
	 */
	public List<User> getUserListByName(String userName) {
		String query = "SELECT * FROM user WHERE UPPER("+USER_NAME_COLUMN+") LIKE UPPER('%" + userName+"%')";
		ResultSet rs = null;
		List<User> usersList = new ArrayList<User>();
		try {
			connection = DAOManager.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				User user = new User(rs.getNString(USER_ID_COLUMN),rs.getNString(USER_FACEBOOK_ID_COLUMN),rs.getNString(USER_NAME_COLUMN),rs.getNString(USER_SURNAME_COLUMN) );
				if(user!=null){
					usersList.add(user);
				}else{
					System.out.println("could not create a User");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DbUtil.close(rs);
			DbUtil.close(statement);
			DbUtil.close(connection);
		}
		return usersList;
	}


}
