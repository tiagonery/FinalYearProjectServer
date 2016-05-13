/**
 * 
 */
package server.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import server.DAO.FriendshipDAO;
import server.DAO.UserWishDAO;
import server.DAO.WishDAO;
import server.model.Friendship;
import server.model.UserWish;

/**
 * @author Tiago
 *
 */
public class UserWishDAOTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link server.DAO.UserWishDAO#getUserWish(int, java.lang.String)}.
	 */
	@Test
	public final void testGetUserWish() {
		UserWishDAO dao = new UserWishDAO();
		int wishId = 5; 
		String userId = "862636260458733"; 

		UserWish result = dao.getUserWish(wishId, userId);
		assertNotEquals("if user is related to Wish result should not be null", result, null);
	}

	/**
	 * Test method for {@link server.DAO.UserWishDAO#deleteUserWishesByWishId(int)}.
	 */
	@Test
	public final void testDeleteUserWishesByWishId() {
		UserWishDAO dao = new UserWishDAO();
		int id = 5; 

		boolean result = dao.deleteUserWishesByWishId(id);
		assertEquals("if Wish exists result should be true", result, true);
	}

	/**
	 * Test method for {@link server.DAO.UserWishDAO#deleteUserWish(int, java.lang.String)}.
	 */
	@Test
	public final void testDeleteUserWish() {
		UserWishDAO dao = new UserWishDAO();
		int wishId = 5; 
		String userId = "862636260458733"; 

		boolean result = dao.deleteUserWish(wishId, userId);
		assertEquals("if user is related to Wish result should be true", result, true);
	}

}
