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
import server.model.UserEvent;
import server.model.Wish;
import server.model.AppEvent.EventType;
import server.model.User;
import server.model.AppEvent.EventVisualizationPrivacy;
import server.model.UserEvent.UserEventState;

/**
 * @author Tiago
 * 
 */
public class WishDAO /** extends AbstractDAO */
{

	private static final String WISH_TABLE = "wish";
	private static final String WISH_ID_COLUMN = "id";
	private static final String WISH_NAME = "name";
	private static final String WISH_CREATOR_FACEBOOK_ID_COLUMN = "owner_id";
	private static final String WISH_DATETIME_COLUMN = "date_time";
	private static final String WISH_ACTIVITY_TYPE_COLUMN = "type";
	private static final String WISH_EVENT_ID_COLUMN = "eventid";
	private Connection connection;
	private Statement statement;


	/**
	 * @param con
	 * @param tableName
	 */
	public WishDAO() {
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

	public Wish getWish(int wishId)  {
		String query = "SELECT * FROM "+WISH_TABLE+" WHERE "+WISH_ID_COLUMN+" = '" + wishId+"'";
		ResultSet rs = null;
		Wish wish = null;
		try {
			connection = DAOManager.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			if (rs.next()) {
				User user = new User();
				user.setFacebookId(rs.getNString(WISH_CREATOR_FACEBOOK_ID_COLUMN));
				wish = new Wish(rs.getInt(WISH_ID_COLUMN), rs.getNString(WISH_NAME),new Date(rs.getTimestamp(WISH_DATETIME_COLUMN).getTime()), EventType.valueOf(rs.getInt(WISH_ACTIVITY_TYPE_COLUMN)), user);

				int eventId = rs.getInt(WISH_EVENT_ID_COLUMN);
				if(!rs.wasNull()){
					AppEvent event = new AppEvent();
					event.setEventId(eventId);
					wish.setLinkedEvent(event);
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
		return wish;
	}

	public int getWishIdByEventId(int eventId)  {
		String query = "SELECT * FROM "+WISH_TABLE+" WHERE "+WISH_EVENT_ID_COLUMN+" = " + eventId+"";
		ResultSet rs = null;
		int result = -1;
		try {
			connection = DAOManager.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			if (rs.next()) {
				result = rs.getInt(WISH_ID_COLUMN);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DbUtil.close(rs);
			DbUtil.close(statement);
			DbUtil.close(connection);
		}
		return result;
	}


	/**
	 * @param surname2
	 * 
	 */
	public Wish createNewWish(String name, Date dateTime, User creator, EventType activity) {
//		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String dateTimeString = sdf.format(dateTime);

		Timestamp timestamp = new Timestamp(dateTime.getTime());
		String query = "INSERT INTO " + WISH_TABLE + " VALUES (DEFAULT, '" + name + "', '"  + activity.getNumber() + "', '" + creator.getFacebookId() + "', " + "NULL" + ", '" + timestamp + "');";
		ResultSet rs; 
		Wish wish = null;
		connection = DAOManager.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			rs = statement.getGeneratedKeys();
			rs.next();
			int wishId = rs.getInt(1);
			wish = new Wish(wishId, name, dateTime, activity, creator);
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DbUtil.close(statement);
			DbUtil.close(connection);
		}
		return wish;

	}

	/**
	 * @param id
	 * @return
	 */
	public boolean deleteWish(int id) {
		String query = "DELETE FROM "+WISH_TABLE+" WHERE "+WISH_ID_COLUMN+" = "+id+";";
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
	 * @param friendship
	 * @return
	 */
	public boolean updateWishEvent(int wishId,int eventId) {
		String query = "UPDATE "+ WISH_TABLE +" SET "+WISH_EVENT_ID_COLUMN+"= "+eventId+" WHERE "+WISH_ID_COLUMN+"= "+wishId+";";
		connection = DAOManager.getConnection();
		boolean worked = false;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
			worked = true;
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DbUtil.close(statement);
			DbUtil.close(connection);
		}
		return worked;
	}




}
