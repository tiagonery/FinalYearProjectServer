/**
 * 
 */
package server.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import server.DAO.UserDAO;
import server.model.User;

/**
 * @author Tiago
 *
 */
public class UserDAOTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link server.DAO.UserDAO#UserDAO()}.
	 */
	@Test
	public final void testUserDAO() {
		UserDAO dao = new UserDAO();
		
		assertNotEquals("number was inserted on numbersList", dao, null);
	}

	/**
	 * Test method for {@link server.DAO.UserDAO#getUser(java.lang.String)}.
	 */
	@Test
	public final void testGetUser() {
		UserDAO dao = new UserDAO();
		String existingUserId = "APA91bHUZGyYDHPKkgZftX1FL_RMXGJvD5zHx63ldntPiYOtMKpsbR5oBPMuVeSGwNmP-9J5kPPS8rw7TcklgNY_2EvTDy1G8ZlYP1VQ5goxBJAescCCB3bovggUM3M88Ozoi9UuU-WI"; 
		String fakeUserId = "fakefakefake123"; 

		User existingResult = dao.getUser(existingUserId);
		User fakeResult = dao.getUser(fakeUserId);
		assertEquals("fake id should result null", fakeResult, null);
		assertNotEquals("real id should not be null, if that user is on database", existingResult, null);
	}

	/**
	 * Test method for {@link server.DAO.UserDAO#createNewUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testCreateNewUser() {
		UserDAO dao = new UserDAO();
		String id = "APA91bHUZGyYDHPKkgZftX1FL_RMXGJvD5zHx63ldntPiYOtMKpsbR5oBPMuVeSGwNmP-9J5kPPS8rw7TcklgNY_2EvTDy1G8ZlYP1VQ5goxBJAescCCB3bovggUM3M88Ozoi9UuU-WI"; 
		String facebookId = "123457457365835683568"; 
		String name = "Name";
		String surname = "SurName";

		User newUser = dao.createNewUser(id, facebookId, name, surname);
		assertNotEquals("user should not be null", newUser, null);
	}

	/**
	 * Test method for {@link server.DAO.UserDAO#deleteUser(java.lang.String)}.
	 */
	@Test
	public final void testDeleteUser() {
		UserDAO dao = new UserDAO();
		String facebookId = "8626362604587331"; 

		boolean result = dao.deleteUser(facebookId);
		assertNotEquals("if id is existent, result should be true", result, true);
	}

	/**
	 * Test method for {@link server.DAO.UserDAO#getUserByFB(java.lang.String)}.
	 */
	@Test
	public final void testGetUserByFB() {
		UserDAO dao = new UserDAO();
		String facebookID = "862636260458733"; 
		String fakeFacebookID = "fakefakefake123"; 

		User existingResult = dao.getUserByFB(facebookID);
		User fakeResult = dao.getUserByFB(fakeFacebookID);
		assertEquals("fake id should result null", fakeResult, null);
		assertNotEquals("real id should not be null, if that user is on database", existingResult, null);
	}

	/**
	 * Test method for {@link server.DAO.UserDAO#getUserListByName(java.lang.String)}.
	 */
	@Test
	public final void testGetUserListByName() {
		UserDAO dao = new UserDAO();
		String name = "tiago"; 
		String fakeName = "fakefakefake"; 

		List<User> existingResult = dao.getUserListByName(name);
		List<User>  fakeResult = dao.getUserListByName(fakeName);
		assertEquals("fake name should result null", fakeResult, null);
		assertNotEquals("real name should not be null, if that user is on database", existingResult, null);
	}

}
