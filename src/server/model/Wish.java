/**
 * 
 */
package server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import server.model.UserEvent.UserEventState;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Tiago
 *
 */
public class Wish implements Serializable{


	private int wishId;
	private String name;
	private Date wishDateTime;
	private User wishOwner;
	private List<UserWish> userWishList;
	private AppEvent.EventType eventType;	
	private AppEvent linkedEvent;


	public Wish(int wishId, String name, Date wishDateTime, AppEvent.EventType eventType, User wishOwner, List<UserWish> userWishList) {
		this.wishId = wishId;
		this.name = name;
		this.wishDateTime = wishDateTime;
		this.eventType = eventType;
		this.wishOwner = wishOwner;
		this.userWishList = userWishList;

	}
	public Wish(int wishId, String name, Date wishDateTime, AppEvent.EventType eventType, User wishOwner) {
		this.wishId = wishId;
		this.name = name;
		this.wishDateTime = wishDateTime;
		this.eventType = eventType;
		this.wishOwner = wishOwner;

	}
	public Wish() {
	}

//	public UserWish.UserWishState getCurrentUserWishState(String id) {
//		UserWish.UserWishState state = null;
//		for (UserWish userWish : getUserWishList()) {
//			if((userWish.getUserId()).equals(id)){
//				state = userWish.getState();
//			}
//		}
//		return state;
//	}


	public int getWishId() {
		return wishId;
	}


	public void setWishId(int WishId) {
		this.wishId = WishId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Date getWishDateTime() {
		return wishDateTime;
	}


	public void setWishDateTime(Date WishDateTime) {
		this.wishDateTime = WishDateTime;
	}



	public User getWishOwner() {
		return wishOwner;
	}


	public void setWishOwner(User WishOwner) {
		this.wishOwner = WishOwner;
	}


	public List<UserWish> getUserWishList() {
		if(userWishList==null){
			userWishList = new ArrayList<UserWish>();
		}
		return userWishList;
	}


	public void setUserWishList(List<UserWish> userWishList) {
		this.userWishList = userWishList;
	}



	public AppEvent.EventType getEventType() {
		return eventType;
	}

	public void setEvent(AppEvent.EventType eventType) {
		this.eventType = eventType;
	}
	
	public AppEvent getLinkedEvent() {
		return linkedEvent;
	}
	public void setLinkedEvent(AppEvent linkedEvent) {
		this.linkedEvent = linkedEvent;
	}

	
    @JsonIgnore
	public Wish getWishWithStrippedUserWishList(String currentUserId) {
    	Wish result = this;
        for(Iterator<UserWish> iterator = getUserWishList().iterator(); iterator.hasNext();) {
			if(!iterator.next().getUserId().equals(currentUserId)){
				iterator.remove();
			}
		}
    	return result;
	}
   

}
