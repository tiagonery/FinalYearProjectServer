/**
 * 
 */
package server.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import server.model.Friendship;
import server.model.Friendship.FriendshipState;
import server.model.Wish;
import server.model.User;
import server.model.UserWish;
import server.model.UserEvent.UserEventState;

/**
 * @author Tiago
 *
 */
public class UserWishDAO {

	private static final String USERWISH_TABE = "user_wish";
	private static final String USERWISH_USER_ID_COLUMN = "user_id";
	private static final String USERWISH_WISH_ID_COLUMN = "wish_id";
	private Connection connection;
	private Statement statement;

	
	
	public UserWish createUserWish(String userId,int wishId)  {
		String query = "INSERT INTO " + USERWISH_TABE + " VALUES ('" + userId + "','" + wishId + "');";
		UserWish userWish = null;
		connection = DAOManager.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
			userWish = new UserWish(userId, wishId);
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DbUtil.close(statement);
			DbUtil.close(connection);
		}
		return userWish;

	}

	/**
	 * @param facebookId
	 */
	public List<Integer> getAvailableWishes(String facebookId) {
		List<Integer> wishesIdsList = new ArrayList<Integer>();
		String query = "SELECT * FROM "+ USERWISH_TABE +" WHERE "+USERWISH_USER_ID_COLUMN+" = '" + facebookId+"'";
		ResultSet rs = null;
		Wish wish = null;
		try {
			connection = DAOManager.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				wishesIdsList.add((rs.getInt(USERWISH_WISH_ID_COLUMN)));
			}
		} catch (SQLException e) {
			wishesIdsList = null;
			e.printStackTrace();
		} finally {
			DbUtil.close(rs);
			DbUtil.close(statement);
			DbUtil.close(connection);
		}
		return wishesIdsList;
		
	}

	/**
	 * @param wishId
	 * @return
	 */
	public List<UserWish> getUserWishesFromWish(int wishId) {
		String query = "SELECT * FROM "+USERWISH_TABE+" WHERE "+USERWISH_WISH_ID_COLUMN+" = '" + wishId+"'";
		ResultSet rs = null;
		List<UserWish> list = new ArrayList<UserWish>();
		try {
			connection = DAOManager.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				UserWish userWish = new UserWish(rs.getNString(USERWISH_USER_ID_COLUMN), rs.getInt(USERWISH_WISH_ID_COLUMN));
				list.add(userWish);

			}
		} catch (SQLException e) {
			list = null;
			e.printStackTrace();
		} finally {
			DbUtil.close(rs);
			DbUtil.close(statement);
			DbUtil.close(connection);
		}
		return list;
	}

	/**
	 * @param id
	 * @return
	 */
	public boolean deleteUserWishesByWishId(int id) {
		String query = "DELETE FROM "+USERWISH_TABE+" WHERE "+USERWISH_WISH_ID_COLUMN+" = "+id+";";
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
}
