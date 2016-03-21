/**
 * 
 */
package server.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import server.model.Friendship;
import server.model.Friendship.FriendshipState;
import server.model.User;

/**
 * @author Tiago
 *
 */
public class FriendshipDAO {

	private static final String FRIENDSHIP_TABE = "friendship";
	private static final String FRIENDSHIP_USER_1_COLUMN = "user1";
	private static final String FRIENDSHIP_USER_2_COLUMN = "user2";
	private static final String FRIENDSHIP_STATE_COLUMN = "state";
	private Connection connection;
	private Statement statement;

	
	
	public Friendship requestFriendship(String userRequesterId,String userRequestedId)  {
		String query = "INSERT INTO " + FRIENDSHIP_TABE + " VALUES (" + userRequesterId + "," + userRequestedId + "," + FriendshipState.REQUEST_UNANSWARED.getNumber() +");";
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
		String query = "UPDATE "+ FRIENDSHIP_TABE +" SET "+FRIENDSHIP_STATE_COLUMN+"="+FriendshipState.REQUEST_ACCEPTED.getNumber()+" WHERE "+FRIENDSHIP_USER_1_COLUMN+"="+friendship.getUser1Id()+" AND "+FRIENDSHIP_USER_2_COLUMN+"="+friendship.getUser2Id();
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
}
