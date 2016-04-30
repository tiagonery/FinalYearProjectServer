/**
 * 
 */
package server.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import server.model.AppEvent;
import server.model.AppEvent.EventType;
import server.model.User;
import server.model.AppEvent.EventVisualizationPrivacy;

/**
 * @author Tiago
 * 
 */
public class EventDAO /** extends AbstractDAO */
{

	private static final String EVENT_TABE = "event";
	private static final String EVENT_ID_COLUMN = "id";
	private static final String EVENT_NAME = "name";
	private static final String EVENT_CREATOR_FACEBOOK_ID_COLUMN = "creator_id";
	private static final String EVENT_DATETIME_COLUMN = "date_time";
	private static final String EVENT_LOCATION_COLUMN = "location";
//	private static final String EVENT_VISUALIZATION_PRIVACY_COLUMN = "visualization_privacy";
//	private static final String EVENT_MATCHING_PRIVACY_COLUMN = "matching_privacy";
	private static final String EVENT_ACTIVITY_COLUMN = "activity";
	private Connection connection;
	private Statement statement;


	/**
	 * @param con
	 * @param tableName
	 */
	public EventDAO() {
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

	public AppEvent getEvent(int eventId)  {
		String query = "SELECT * FROM "+EVENT_TABE+" WHERE "+EVENT_ID_COLUMN+" = " + eventId+"";
		ResultSet rs = null;
		AppEvent event = null;
		try {
			connection = DAOManager.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			if (rs.next()) {
				User user = new User();
				user.setFacebookId(rs.getNString(EVENT_CREATOR_FACEBOOK_ID_COLUMN));
				event = new AppEvent(rs.getInt(EVENT_ID_COLUMN), rs.getNString(EVENT_NAME), new Date(rs.getTimestamp(EVENT_DATETIME_COLUMN).getTime()), rs.getNString(EVENT_LOCATION_COLUMN), user, EventType.valueOf(rs.getInt(EVENT_ACTIVITY_COLUMN)));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DbUtil.close(rs);
			DbUtil.close(statement);
			DbUtil.close(connection);
		}
		return event;
	}


	/**
	 * @param surname2
	 * 
	 */
	public AppEvent createNewEvent(String name, Date dateTime, String location, User creator, EventType activity) {
//		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String dateTimeString = sdf.format(dateTime);

		Timestamp timestamp = new Timestamp(dateTime.getTime());
		String query = "INSERT INTO " + EVENT_TABE + " VALUES (DEFAULT, '" + name + "', '" + timestamp + "', '" + location + "', '" + creator.getFacebookId() + "', '" +activity.getNumber() + "');";
		ResultSet rs; 
		AppEvent event = null;
		connection = DAOManager.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			rs = statement.getGeneratedKeys();
			rs.next();
			int eventId = rs.getInt(1);
			event = new AppEvent(eventId, name, dateTime, location, creator, activity);
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DbUtil.close(statement);
			DbUtil.close(connection);
		}
		return event;

	}

	/**
	 * @param id
	 * @return
	 */
	public boolean deleteEvent(String id) {
		String query = "DELETE FROM table"+EVENT_TABE+"WHERE"+EVENT_ID_COLUMN+"='"+id+"';";
		ResultSet rs = null;
		boolean result = true;
		connection = DAOManager.getConnection();
		try {
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		} finally {
			DbUtil.close(rs);
			DbUtil.close(statement);
			DbUtil.close(connection);
		}
		return result;
	}




}
