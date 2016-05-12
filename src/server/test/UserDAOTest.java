/**
 * 
 */
package server.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

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
		fail("Not yet implemented"); // TODO
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
		assertEquals("number was removed from availableBigNumbers", fakeResult, null);
		assertNotEquals("number was inserted on numbersList", existingResult, null);
	}

	/**
	 * Test method for {@link server.DAO.UserDAO#createNewUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testCreateNewUser() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link server.DAO.UserDAO#deleteUser(java.lang.String)}.
	 */
	@Test
	public final void testDeleteUser() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link server.DAO.UserDAO#getUserByFB(java.lang.String)}.
	 */
	@Test
	public final void testGetUserByFB() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link server.DAO.UserDAO#getUserListByName(java.lang.String)}.
	 */
	@Test
	public final void testGetUserListByName() {
		fail("Not yet implemented"); // TODO
	}

}
