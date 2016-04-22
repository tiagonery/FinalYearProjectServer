/**
 * 
 */
package server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.model.UserEvent.UserEventState;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Tiago
 *
 */
public class AppEvent implements Serializable{


	private int eventId;
	private String name;
	private Date eventDateTimeStart;
//	private Timestamp eventDateTimeEnd;
//	private Address address;
	private User eventOwner;
	private String location;
	private List<UserEvent> userEventList;
	private EventVisualizationPrivacy eventVisualizationPrivacy;
	private EventMatchingPrivacy eventMatchingPrivacy;
//	private Venue venue;
	private EventType eventType;

	public AppEvent(String name, EventType eventType , UserEvent.UserEventState state) {
		this.name = name;
		this.eventType = eventType;
	}
	public AppEvent(int id, String name, Date dateTime, String location, User creatorFbId, EventVisualizationPrivacy visualizationPrivacy, EventMatchingPrivacy matchingPrivacy, EventType eventType) {
		this.eventId = id;
		this.name = name;
		this.eventDateTimeStart = dateTime;
		this.location = location;
		this.eventOwner = creatorFbId;
		this.eventVisualizationPrivacy = visualizationPrivacy;
		this.eventMatchingPrivacy = matchingPrivacy;
		this.eventType = eventType;
	}
	public AppEvent() {
	}

	public enum EventVisualizationPrivacy {
		INVTED_FRIENDS(1),
		ALL_FRIENDS(2);


	    private final int num;
	    
	    private static Map<Integer, EventVisualizationPrivacy> map = new HashMap<Integer, EventVisualizationPrivacy>();

	    static {
	        for (EventVisualizationPrivacy privacy : EventVisualizationPrivacy.values()) {
	            map.put(privacy.num, privacy);
	        }
	    }

	    private EventVisualizationPrivacy(final int num) { 
	    		this.num = num; 
	    	}

	    public static EventVisualizationPrivacy valueOf(int num) {
	        return map.get(num);
	    }

	    public int getNumber()
	    {
	        return num;
	    }
	}

	public enum EventMatchingPrivacy {
		DISABLED(1),
		ENABLED_FOR_FRIENDS(2),
		ENABLE_PUBLIC(3);
		
	    private final int num;
	    private static Map<Integer, EventMatchingPrivacy> map = new HashMap<Integer, EventMatchingPrivacy>();

	    static {
	        for (EventMatchingPrivacy privacy : EventMatchingPrivacy.values()) {
	            map.put(privacy.num, privacy);
	        }
	    }

	    public static EventMatchingPrivacy valueOf(int num) {
	        return map.get(num);
	    }
	    
	    private EventMatchingPrivacy(int num)
	    {
	        this.num = num;
	    }

	    public int getNumber()
	    {
	        return num;
	    }
	}

	public enum EventType{
		DRINKS(1),
		FOOD(2),
		SPORTS(3),
		BUSINESS(4),
		FILM(5),
		CLUB(6),
		OTHER(7);
		
	    private final int num;
	    
	    private static Map<Integer, EventType> map = new HashMap<Integer, EventType>();

	    static {
	        for (EventType eventType : EventType.values()) {
	            map.put(eventType.num, eventType);
	        }
	    }

	    public static EventType valueOf(int num) {
	        return map.get(num);
	    }
	    
	    private EventType(int num)
	    {
	        this.num = num;
	    }

	    public int getNumber()
	    {
	        return num;
	    }

		public static String[] names() {
			EventType[] states = values();
			String[] names = new String[states.length];

			for (int i = 0; i < states.length; i++) {
				names[i] = states[i].name();
			}

			return names;
		}
	}


	public int getEventId() {
		return eventId;
	}


	public void setEventId(int eventId) {
		this.eventId = eventId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Date getEventDateTimeStart() {
		return eventDateTimeStart;
	}


	public void setEventDateTimeStart(Date eventDateTimeStart) {
		this.eventDateTimeStart = eventDateTimeStart;
	}


//	public Timestamp getEventDateTimeEnd() {
//		return eventDateTimeEnd;
//	}
//
//
//	public void setEventDateTimeEnd(Timestamp eventDateTimeEnd) {
//		this.eventDateTimeEnd = eventDateTimeEnd;
//	}
//
//
//	public Address getAddress() {
//		return address;
//	}
//
//
//	public void setAddress(Address address) {
//		this.address = address;
//	}


	public User getEventOwner() {
		return eventOwner;
	}


	public void setEventOwner(User eventOwner) {
		this.eventOwner = eventOwner;
	}



	public EventVisualizationPrivacy getEventVisualizationPrivacy() {
		return eventVisualizationPrivacy;
	}


	public void setEventVisualizationPrivacy(EventVisualizationPrivacy eventVisualizationPrivacy) {
		this.eventVisualizationPrivacy = eventVisualizationPrivacy;
	}


//	public Venue getVenue() {
//		return venue;
//	}
//
//
//	public void setVenue(Venue venue) {
//		this.venue = venue;
//	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public EventMatchingPrivacy getEventMatchingPrivacy() {
		return eventMatchingPrivacy;
	}

	public void setEventMatchingPrivacy(EventMatchingPrivacy eventMatchingPrivacy) {
		this.eventMatchingPrivacy = eventMatchingPrivacy;
	}


	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	public List<UserEvent> getUserEventList() {
		if(userEventList==null){
			userEventList = new ArrayList<UserEvent>();
		}
		return userEventList;
	}
	public void setUserEventList(List<UserEvent> userEventList) {
		this.userEventList = userEventList;
	}
	
    @JsonIgnore
	public AppEvent getEventWithoutPrivateInfo(String currentUserId) {
    	AppEvent result = this;
    	List<UserEvent> newList = new ArrayList<UserEvent>();
    	for (UserEvent userEvent : getUserEventList()) {
			if(userEvent.getState()==UserEventState.OWNER ||userEvent.getState()==UserEventState.GOING || userEvent.getUserId().equals(currentUserId)){
				newList.add(userEvent);
			}
		}
    	result.setUserEventList(newList);
		return result;
	}
}
