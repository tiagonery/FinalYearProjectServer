/**
 * 
 */
package server.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import server.DAO.UserEventDAO;
import server.DAO.WishDAO;
import server.model.UserEvent;
import server.model.Wish;

/**
 * @author Tiago
 *
 */
public class UserEventDAOTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link server.DAO.UserEventDAO#getAvailableEvents(java.lang.String)}.
	 */
	@Test
	public final void testGetAvailableEvents() {
		UserEventDAO dao = new UserEventDAO();
		String facebookId = "10207958837424873"; 

		List<Integer> result = dao.getAvailableEvents(facebookId);
		assertNotEquals("result should not be null, if that user is on database", result, null);
	}

	/**
	 * Test method for {@link server.DAO.UserEventDAO#getUserEventsFromEvent(int)}.
	 */
	@Test
	public final void testGetUserEventsFromEvent() {
		UserEventDAO dao = new UserEventDAO();
		int eventId = 11; 

		List<UserEvent> result = dao.getUserEventsFromEvent(eventId);
		assertNotEquals("result should not be null, if eventId is on database", result, null);
	}

	/**
	 * Test method for {@link server.DAO.UserEventDAO#deleteUserEventsByEventId(int)}.
	 */
	@Test
	public final void testDeleteUserEventsByEventId() {
		UserEventDAO dao = new UserEventDAO();
		int id = 5; 

		boolean result = dao.deleteUserEventsByEventId(id);
		assertEquals("if id is existent, result should be true", result, true);
	}

}
