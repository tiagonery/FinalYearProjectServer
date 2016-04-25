package server.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Tiago on 22/03/2016.
 */
public class UserEvent implements Serializable{

    private String userId;
    private int eventId;
    private UserEventState state;

    public enum UserEventState {
    	OWNER(1),
        INVITED(2),
        GOING(3),
        IDLE(4),
        NOT_GOING(5);


        private final int num;

	    private static Map<Integer, UserEventState> map = new HashMap<Integer, UserEventState>();

	    static {
	        for (UserEventState state : UserEventState.values()) {
	            map.put(state.num, state);
	        }
	    }

	    public static UserEventState valueOf(int num) {
	        return map.get(num);
	    }
	    
        private UserEventState(int num)
        {
            this.num = num;
        }

        public int getNumber()
        {
            return num;
        }
    }

    /**
     * @param userId
     * @param eventId
     * @param state
     */
    public UserEvent(String userId, int eventId, UserEventState state) {
        this.userId = userId;
        this.eventId = eventId;
        this.state = state;
    }
    
    public UserEvent() {
    	
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public UserEventState getState() {
        return state;
    }

    public void setState(UserEventState state) {
        this.state = state;
    }


}
