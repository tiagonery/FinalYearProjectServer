/**
 * 
 */
package server.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import server.model.UserEvent.UserEventState;

/**
 * @author Tiago
 *
 */
public class Friendship implements Serializable{



	private String user1Id;
	private String user2Id;
	private FriendshipState state;

	public enum FriendshipState {
		REQUEST_ACCEPTED(1),
		REQUEST_UNANSWARED(2),
		REQUEST_REFUSED(3);

		

	    private final int num;
	    
	    private static Map<Integer, FriendshipState> map = new HashMap<Integer, FriendshipState>();

	    static {
	        for (FriendshipState state : FriendshipState.values()) {
	            map.put(state.num, state);
	        }
	    }

	    public static FriendshipState valueOf(int num) {
	        return map.get(num);
	    }

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
	
	public Friendship() {
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
