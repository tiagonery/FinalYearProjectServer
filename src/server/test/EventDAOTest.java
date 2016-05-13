/**
 * 
 */
package server.test;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import server.DAO.EventDAO;
import server.DAO.UserDAO;
import server.model.AppEvent;
import server.model.AppEvent.EventType;
import server.model.User;

/**
 * @author Tiago
 *
 */
public class EventDAOTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link server.DAO.EventDAO#EventDAO()}.
	 */
	@Test
	public final void testEventDAO() {
		EventDAO dao = new EventDAO();
		
		assertNotEquals("number was inserted on numbersList", dao, null);
	}

	/**
	 * Test method for {@link server.DAO.EventDAO#getEvent(int)}.
	 */
	@Test
	public final void testGetEvent() {
		EventDAO dao = new EventDAO();
		int eventId = 10; 
		int fakeEventId = -1; 

		AppEvent existingResult = dao.getEvent(eventId);
		AppEvent fakeResult = dao.getEvent(fakeEventId);
		assertEquals("fake id should result null", fakeResult, null);
		assertNotEquals("real id should not be null, if that user is on database", existingResult, null);
	}

	/**
	 * Test method for {@link server.DAO.EventDAO#createNewEvent(java.lang.String, java.util.Date, java.lang.String, server.model.User, server.model.AppEvent.EventType)}.
	 */
	@Test
	public final void testCreateNewEvent() {
		EventDAO dao = new EventDAO();
		String name = "Event";
		Date dateTime = new Date();
		String location = "Location";
		User creator = new User();
		EventType activity = EventType.EXERCISE;

		AppEvent newEvent = dao.createNewEvent(name, dateTime, location, creator, activity);
		assertNotEquals("newEvent should not be null", newEvent, null);
	}

	/**
	 * Test method for {@link server.DAO.EventDAO#deleteEvent(int)}.
	 */
	@Test
	public final void testDeleteEvent() {
		EventDAO dao = new EventDAO();
		int id = 5; 

		boolean result = dao.deleteEvent(id);
		assertNotEquals("if id is existent, result should be true", result, true);
	}

}
