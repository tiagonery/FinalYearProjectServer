/**
 * 
 */
package server.model;

/**
 * @author Tiago
 *
 */
public class Friendship {


	private String user1Id;
	private String user2Id;
	private FriendshipState state;

	public enum FriendshipState {
		REQUEST_ACCEPTED(1),
		REQUEST_UNANSWARED(2),
		REQUEST_REFUSED(3);


	    private final int num;

	    private FriendshipState(int num)
	    {
	        this.num = num;
	    }

	    public int getNumber()
	    {
	        return num;
	    }
	}

	/**
	 * @param userRequesterId
	 * @param userRequestedId
	 * @param number
	 */
	public Friendship(String user1Id, String user2Id, FriendshipState state) {
		this.user1Id = user1Id;
		this.user2Id = user2Id;
		this.state = state;
	}

	public String getUser1Id() {
		return user1Id;
	}

	public void setUser1Id(String user1Id) {
		this.user1Id = user1Id;
	}

	public String getUser2Id() {
		return user2Id;
	}

	public void setUser2Id(String user2Id) {
		this.user2Id = user2Id;
	}

	public FriendshipState getState() {
		return state;
	}

	public void setState(FriendshipState state) {
		this.state = state;
	}
	

}
