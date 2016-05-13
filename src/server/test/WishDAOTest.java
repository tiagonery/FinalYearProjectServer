/**
 * 
 */
package server.test;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import server.DAO.EventDAO;
import server.DAO.WishDAO;
import server.model.AppEvent;
import server.model.User;
import server.model.Wish;
import server.model.AppEvent.EventType;

/**
 * @author Tiago
 *
 */
public class WishDAOTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link server.DAO.WishDAO#WishDAO()}.
	 */
	@Test
	public final void testWishDAO() {
		WishDAO dao = new WishDAO();
		
		assertNotEquals("number was inserted on numbersList", dao, null);
	}

	/**
	 * Test method for {@link server.DAO.WishDAO#getWish(int)}.
	 */
	@Test
	public final void testGetWish() {
		WishDAO dao = new WishDAO();
		int wishId = 10; 
		int fakeWishId = -1; 

		Wish existingResult = dao.getWish(wishId);
		Wish fakeResult = dao.getWish(fakeWishId);
		assertEquals("fake id should result null", fakeResult, null);
		assertNotEquals("real id should not be null, if that user is on database", existingResult, null);
	}

	/**
	 * Test method for {@link server.DAO.WishDAO#getWishIdByEventId(int)}.
	 */
	@Test
	public final void testGetWishIdByEventId() {
		WishDAO dao = new WishDAO();
		int eventId = 10; 
		int fakeEventId = -1; 

		int existingResult = dao.getWishIdByEventId(eventId);
		int fakeResult = dao.getWishIdByEventId(fakeEventId);
		assertEquals("fake id should result null", fakeResult, -1);
		assertNotEquals("real id should not be null, if that user is on database", existingResult, null);
	}

	/**
	 * Test method for {@link server.DAO.WishDAO#createNewWish(java.lang.String, java.util.Date, server.model.User, server.model.AppEvent.EventType)}.
	 */
	@Test
	public final void testCreateNewWish() {
		WishDAO dao = new WishDAO();
		String name = "Event";
		Date dateTime = new Date();
		User creator = new User();
		EventType activity = EventType.EXERCISE;

		Wish newWish = dao.createNewWish(name, dateTime, creator, activity);
		assertNotEquals("newWish should not be null", newWish, null);
	}

	/**
	 * Test method for {@link server.DAO.WishDAO#deleteWish(int)}.
	 */
	@Test
	public final void testDeleteWish() {
		WishDAO dao = new WishDAO();
		int id = 5; 

		boolean result = dao.deleteWish(id);
		assertEquals("if id is existent, result should be true", result, true);
	}

	/**
	 * Test method for {@link server.DAO.WishDAO#updateWishEvent(int, int)}.
	 */
	@Test
	public final void testUpdateWishEvent() {
		WishDAO dao = new WishDAO();
		int wishId = 5; 
		int eventId = 10; 

		boolean result = dao.updateWishEvent(wishId, eventId);
		assertEquals("if wish id and event id are existent, result should be true", result, true);
	}

}
