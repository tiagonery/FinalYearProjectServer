/**
 * 
 */
package server.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import server.DAO.FriendshipDAO;
import server.DAO.WishDAO;
import server.model.Friendship;
import server.model.Friendship.FriendshipState;
import server.model.Wish;

/**
 * @author Tiago
 *
 */
public class FriendshipDAOTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link server.DAO.FriendshipDAO#requestFriendship(java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testRequestFriendship() {
		FriendshipDAO dao = new FriendshipDAO();
		String userRequesterId = "10207958837424873"; 
		String userRequestedId = "862636260458733"; 

		Friendship result = dao.requestFriendship(userRequesterId, userRequestedId);
		assertNotEquals("if users are friends result should not be null", result, null);
	}

	/**
	 * Test method for {@link server.DAO.FriendshipDAO#acceptFriendship(server.model.Friendship)}.
	 */
	@Test
	public final void testAcceptFriendship() {
		FriendshipDAO dao = new FriendshipDAO();
		Friendship friendship = new Friendship("10207958837424873","862636260458733", FriendshipState.REQUEST_ACCEPTED ); 

		Friendship result = dao.acceptFriendship(friendship);
		assertNotEquals("if users are not friends yet result should not be null", result, null);
	}

	/**
	 * Test method for {@link server.DAO.FriendshipDAO#getFriendsIds(java.lang.String)}.
	 */
	@Test
	public final void testGetFriendsIds() {
		FriendshipDAO dao = new FriendshipDAO();
		String facebookId = "10207958837424873"; 

		List<String> result = dao.getFriendsIds(facebookId);
		assertNotEquals("if users exists result should not be null", result, null);
	}

}
