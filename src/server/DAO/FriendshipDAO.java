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
import server.model.UserEvent;
import server.model.Friendship.FriendshipState;
import server.model.UserEvent.UserEventState;
import server.model.User;

/**
 * @author Tiago
 *
 */
public class FriendshipDAO {

	private static final String FRIENDSHIP_TABLE = "friendship";
	private static final String FRIENDSHIP_USER_1_COLUMN = "user1";
	private static final String FRIENDSHIP_USER_2_COLUMN = "user2";
	private static final String FRIENDSHIP_STATE_COLUMN = "state";
	private Connection connection;
	private Statement statement;

	
	
	public Friendship requestFriendship(String userRequesterId,String userRequestedId)  {
		String query = "INSERT INTO " + FRIENDSHIP_TABLE + " VALUES (" + userRequesterId + "," + userRequestedId + "," + FriendshipState.REQUEST_UNANSWARED.getNumber() +");";
		Friendship friendship = null;
		connection = DAOManager.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
			friendship = new Friendship(userRequesterId, userRequestedId, FriendshipState.REQUEST_UNANSWARED);
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DbUtil.close(statement);
			DbUtil.close(connection);
		}
		return friendship;

	}



	/**
	 * @param friendship
	 * @return
	 */
	public Friendship acceptFriendship(Friendship friendship) {
		String query = "UPDATE "+ FRIENDSHIP_TABLE +" SET "+FRIENDSHIP_STATE_COLUMN+"="+FriendshipState.REQUEST_ACCEPTED.getNumber()+" WHERE "+FRIENDSHIP_USER_1_COLUMN+"="+friendship.getUser1Id()+" AND "+FRIENDSHIP_USER_2_COLUMN+"="+friendship.getUser2Id();
		ResultSet rs = null;
		connection = DAOManager.getConnection();
		try {
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			friendship.setState(FriendshipState.REQUEST_ACCEPTED);
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DbUtil.close(rs);
			DbUtil.close(statement);
			DbUtil.close(connection);
		}
		return friendship;
	}



	/**
	 * @param friendship
	 * @return
	 */
	public Friendship refuseFriendship(Friendship friendship) {
		String query = "UPDATE "+ FRIENDSHIP_TABLE +" SET "+FRIENDSHIP_STATE_COLUMN+"="+FriendshipState.REQUEST_REFUSED.getNumber()+" WHERE "+FRIENDSHIP_USER_1_COLUMN+"="+friendship.getUser1Id()+" AND "+FRIENDSHIP_USER_2_COLUMN+"="+friendship.getUser2Id();
		ResultSet rs = null;
		connection = DAOManager.getConnection();
		try {
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			friendship.setState(FriendshipState.REQUEST_REFUSED);
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DbUtil.close(rs);
			DbUtil.close(statement);
			DbUtil.close(connection);
		}
		return friendship;
	}



	/**
	 * @param facebookId
	 * @return
	 */
	public List<Friendship> getAllFriendships(String facebookId) {
		String query = "SELECT * FROM "+FRIENDSHIP_TABLE+" WHERE "+FRIENDSHIP_USER_1_COLUMN+" = '" + facebookId+"' OR "+FRIENDSHIP_USER_2_COLUMN+" = '" + facebookId+"'";
		ResultSet rs = null;
		List<Friendship> friendships = new ArrayList<Friendship>();
		try {
			connection = DAOManager.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			if (rs.next()) {
				Friendship friendship = new Friendship(rs.getNString(FRIENDSHIP_USER_1_COLUMN), rs.getNString(FRIENDSHIP_USER_2_COLUMN), FriendshipState.valueOf(rs.getInt(FRIENDSHIP_STATE_COLUMN)));
				friendships.add(friendship);

			}
		} catch (SQLException e) {
			friendships = null;
			e.printStackTrace();
		} finally {
			DbUtil.close(rs);
			DbUtil.close(statement);
			DbUtil.close(connection);
		}
		return friendships;
	}

	/**
	 * @param facebookId
	 * @return
	 */
	public List<Friendship> getFriendshipsFromFriends(String facebookId) {
		String query = "SELECT * FROM "+FRIENDSHIP_TABLE+" WHERE "+FRIENDSHIP_STATE_COLUMN+" = "+FriendshipState.REQUEST_ACCEPTED.getNumber()+ " AND "+FRIENDSHIP_USER_1_COLUMN+" = '" + facebookId+"' OR "+FRIENDSHIP_USER_2_COLUMN+" = '" + facebookId+"' ";
		ResultSet rs = null;
		List<Friendship> friendships = new ArrayList<Friendship>();
		try {
			connection = DAOManager.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			if (rs.next()) {
				Friendship friendship = new Friendship(rs.getNString(FRIENDSHIP_USER_1_COLUMN), rs.getNString(FRIENDSHIP_USER_2_COLUMN), FriendshipState.valueOf(rs.getInt(FRIENDSHIP_STATE_COLUMN)));
				friendships.add(friendship);

			}
		} catch (SQLException e) {
			friendships = null;
			e.printStackTrace();
		} finally {
			DbUtil.close(rs);
			DbUtil.close(statement);
			DbUtil.close(connection);
		}
		return friendships;
	}



	/**
	 * @param facebookId
	 * @return
	 */
	public List<String> getFriendsIds(String facebookId) {
		List<Friendship> friendships = getFriendshipsFromFriends(facebookId);
		List<String> idsList = new ArrayList<String>();
		for (Friendship friendship : friendships) {
			if(friendship.getUser1Id().equals(facebookId)){
				idsList.add(friendship.getUser2Id());
			}else{
				idsList.add(friendship.getUser1Id());
			}
		}
		return idsList;
		
	}
}
