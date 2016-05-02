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
import server.model.AppEvent.EventType;
import server.model.AppEvent.EventVisualizationPrivacy;
import server.model.Friendship.FriendshipState;
import server.model.AppEvent;
import server.model.User;
import server.model.UserEvent;
import server.model.UserEvent.UserEventState;

/**
 * @author Tiago
 *
 */
public class UserEventDAO {

	private static final String USEREVENT_TABLE = "user_event";
	private static final String USEREVENT_USER_ID_COLUMN = "userid";
	private static final String USEREVENT_EVENT_ID_COLUMN = "eventid";
	private static final String USEREVENT_STATE_COLUMN = "state";
	private Connection connection;
	private Statement statement;

	
	
	public UserEvent createUserEvent(String userId,int eventId, UserEventState state )  {
		String query = "INSERT INTO " + USEREVENT_TABLE + " VALUES ('" + userId + "','" + eventId + "','" + state.getNumber() +"');";
		UserEvent userEvent = null;
		connection = DAOManager.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
			userEvent = new UserEvent(userId, eventId, state);
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DbUtil.close(statement);
			DbUtil.close(connection);
		}
		return userEvent;

	}

	/**
	 * @param facebookId
	 */
	public List<Integer> getAvailableEvents(String facebookId) {
		List<Integer> eventsIdsList = new ArrayList<Integer>();
		String query = "SELECT * FROM "+ USEREVENT_TABLE +" WHERE "+USEREVENT_USER_ID_COLUMN+" = '" + facebookId+"' AND "+USEREVENT_STATE_COLUMN+" IN ("+UserEventState.GOING.getNumber()+","+UserEventState.OWNER.getNumber()+","+UserEventState.INVITED.getNumber()+") ORDER BY "+USEREVENT_STATE_COLUMN+";";
		ResultSet rs = null;
		AppEvent event = null;
		try {
			connection = DAOManager.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				eventsIdsList.add((rs.getInt(USEREVENT_EVENT_ID_COLUMN)));
			}
		} catch (SQLException e) {
			eventsIdsList = null;
			e.printStackTrace();
		} finally {
			DbUtil.close(rs);
			DbUtil.close(statement);
			DbUtil.close(connection);
		}
		return eventsIdsList;
		
	}

	/**
	 * @param friendship
	 * @return
	 */
	public UserEvent updateUserEventStatus(String userId,int eventId, UserEventState state) {
		String query = "UPDATE "+ USEREVENT_TABLE +" SET "+USEREVENT_STATE_COLUMN+"= "+state.getNumber()+" WHERE "+USEREVENT_USER_ID_COLUMN+"= '"+userId+"' AND "+USEREVENT_EVENT_ID_COLUMN+" = "+eventId+";";
		connection = DAOManager.getConnection();
		UserEvent userEvent = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
			userEvent = new UserEvent(userId, eventId, state);
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DbUtil.close(statement);
			DbUtil.close(connection);
		}
		return userEvent;
	}

	/**
	 * @param eventId
	 * @return
	 */
	public List<UserEvent> getUserEventsFromEvent(int eventId) {
		String query = "SELECT * FROM "+USEREVENT_TABLE+" WHERE "+USEREVENT_EVENT_ID_COLUMN+" = '" + eventId+"'";
		ResultSet rs = null;
		List<UserEvent> list = new ArrayList<UserEvent>();
		try {
			connection = DAOManager.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				UserEvent userEvent = new UserEvent(rs.getNString(USEREVENT_USER_ID_COLUMN), rs.getInt(USEREVENT_EVENT_ID_COLUMN), UserEventState.valueOf(rs.getInt(USEREVENT_STATE_COLUMN)));
				list.add(userEvent);

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
 * @param eventId
 * @return
 */
	public UserEvent getUserEvent(String userId, int eventId) {
		String query = "SELECT * FROM " + USEREVENT_TABLE + " WHERE " + USEREVENT_EVENT_ID_COLUMN + " = " + eventId + " AND " + USEREVENT_USER_ID_COLUMN
				+ " = '" + userId + "';";
		ResultSet rs = null;
		UserEvent userEvent = null;
		try {
			connection = DAOManager.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			if (rs.next()) {
				userEvent = new UserEvent(rs.getNString(USEREVENT_USER_ID_COLUMN), rs.getInt(USEREVENT_EVENT_ID_COLUMN), UserEventState.valueOf(rs
						.getInt(USEREVENT_STATE_COLUMN)));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.close(rs);
			DbUtil.close(statement);
			DbUtil.close(connection);
		}
		return userEvent;
	}
	


	/**
	 * @param id
	 * @return
	 */
	public boolean deleteUserEventsByEventId(int id) {
		String query = "DELETE FROM "+USEREVENT_TABLE+" WHERE "+USEREVENT_EVENT_ID_COLUMN+" = "+id;
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
