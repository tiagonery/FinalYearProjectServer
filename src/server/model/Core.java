/**
 * 
 */
package server.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import server.DAO.EventDAO;
import server.DAO.FriendshipDAO;
import server.DAO.UserDAO;
import server.DAO.UserEventDAO;
import server.DAO.UserWishDAO;
import server.DAO.WishDAO;
import server.gcm.MessageHandler;
import server.gcm.ServerMessage;
import server.gcm.ServerMessage.ServerMessageType;
import server.model.Friendship.FriendshipState;
import server.model.UserEvent.UserEventState;

/**
 * @author Tiago
 *
 */
public class Core {

	private User userRequester;
	/**
	 * @return
	 */
	public boolean validateUser(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @param facebookID
	 * @return 
	 */
	public ServerMessage createNewUser(ServerMessage serverReplyMessage, String regId, User user) {
			UserDAO dao = new UserDAO();
			if(dao.createNewUser(regId, user.getFacebookId(),user.getName(),user.getSurname())!=null){
				serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_SUCCES);
			}else{
				serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
				serverReplyMessage.setErrorMessage("Couldn't create User");
			}return serverReplyMessage;
	}

	
	/**
	 * @return
	 */
	private ServerMessage createServerMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return
	 */
	private boolean isUserRegistered() {

		return true;
	}

	/**
	 * 
	 */
	public void deleteUser(ServerMessage serverReplyMessage) {
		UserDAO dao = new UserDAO();
		if(dao.deleteUser(getUserRequester().getRegId())){
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_SUCCES);
		}else{
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
			serverReplyMessage.setErrorMessage("Couldn't delete User");
		}
		
	}

	/**
	 * @param serverReplyMessage 
	 * @param configKey
	 * @param configValue
	 */
	public void editConfig(ServerMessage serverReplyMessage, String configKey, String configValue) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param serverReplyMessage 
	 * @param username2
	 */
	public void requestFriendshipByUsername(ServerMessage serverReplyMessage, String username2) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param serverReplyMessage 
	 * @param facebookID
	 */
	public ServerMessage requestFriendshipByFacebook(ServerMessage serverReplyMessage, List<String> facebookIDsList) {
		FriendshipDAO friendshipDao = new FriendshipDAO();
		UserDAO userDao = new UserDAO();
		boolean success=true;
		List<String> failedToAddIds = new ArrayList<String>();
		for(String facebookId:facebookIDsList){
			User userRequested = userDao.getUserByFB(facebookId);
			if(userRequested!=null && friendshipDao.requestFriendship(getUserRequester().getFacebookId(), userRequested.getFacebookId())!=null){
				sendFriendshipRequestNotification(userRequested.getRegId(), getUserRequester());
				//notify user requested of new friendship request 
				
			}else{
				success = false;
				failedToAddIds.add(facebookId);
			}
		}if(success){
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_SUCCES);
		}else{

			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
			String listOfIds = "";
			for (String string : failedToAddIds) {
				listOfIds = listOfIds+" "+string;
			}
			serverReplyMessage.setErrorMessage("Couldn't add friend with facebookid= "+listOfIds);
		}return serverReplyMessage;
		
	}

	/**
	 * 
	 */
	private void sendFriendshipRequestNotification(String to, User from) {
		ServerMessage serverNotificationMessage = new ServerMessage(to, ServerMessageType.NOTIFY_FRIENDSHIP_REQUEST_RECEIVED);
		serverNotificationMessage.setFriendshipRequester(from);
		sendNotification(serverNotificationMessage);
		
	}

	/**
	 * @param serverNotificationMessage
	 */
	private void sendNotification(ServerMessage serverNotificationMessage) {
		MessageHandler messageHandler = new MessageHandler();
		messageHandler.sendMessageToClient(serverNotificationMessage);
		
	}

	/**
	 * @param serverReplyMessage 
	 * @param facebookID
	 */
	public void removeFriend(ServerMessage serverReplyMessage, String facebookID) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param facebookID
	 */
	public void inviteToEvent(List<String> facebookIDsList, AppEvent event) {
		UserEventDAO dao= new UserEventDAO();
		UserDAO userDao = new UserDAO();
		for (String id : facebookIDsList) {
			UserEvent userEvent = dao.createUserEvent(id, event.getEventId(), UserEventState.INVITED);
			if(userEvent!=null){
				event.getUserEventList().add(userEvent);
			}else{
				System.out.println("could not Invite user with id:"+id+" to event");
			}
			
		}
		for (UserEvent userEvent : event.getUserEventList()) {
			AppEvent eventToSend = event;
			eventToSend.setUserEventList(dao.getUserEventsFromEvent(event.getEventId()));
			User user = userDao.getUserByFB(userEvent.getUserId());
			if(user!=null){
				sendEventInviteReceivedNotification(user.getRegId(), eventToSend.getEventWithoutPrivateInfo(user.getFacebookId()));
			}
		else{
			System.out.println("could not Notify user with id:"+userEvent.getUserId()+" to event");
		}
			
		}
				
	}


	/**
	 * 
	 */
	private void sendEventInviteReceivedNotification(String to, AppEvent event) {
		ServerMessage serverNotificationMessage = new ServerMessage(to, ServerMessageType.NOTIFY_INVITATION_RECEIVED);
		serverNotificationMessage.setEvent(event);
		sendNotification(serverNotificationMessage);
		
	}
	/**
	 * @param serverReplyMessage 
	 * @param inviteID
	 */
	public void acceptInvite(ServerMessage serverReplyMessage, int inviteID) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param serverReplyMessage 
	 * @param inviteID
	 */
	public void refuseInvite(ServerMessage serverReplyMessage, int inviteID) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param serverReplyMessage 
	 * @param eventID
	 */
	public void leaveEvent(ServerMessage serverReplyMessage, int eventID) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param serverReplyMessage 
	 * @param eventID
	 */
	public void joinEvent(ServerMessage serverReplyMessage, int eventID) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param serverReplyMessage 
	 * @param eventID
	 */
	public ServerMessage joinWish(ServerMessage serverReplyMessage, int wishId) {
		UserWishDAO userWishDAO = new UserWishDAO();
		UserWish userWish = userWishDAO.createUserWish(getUserRequester().getFacebookId(), wishId);
		if(userWish!=null){
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_SUCCES);
			serverReplyMessage.setUserWish(userWish);
		}else{
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
			serverReplyMessage.setErrorMessage("Could not create UserWish");
		}
		return serverReplyMessage;
		
	}

	/**
	 * @param serverReplyMessage 
	 * @return 
	 * 
	 */
	public ServerMessage getEvents(ServerMessage serverReplyMessage) {

		List<AppEvent> eventsList = new ArrayList<AppEvent>();
		List<String> eventsIdsList = new ArrayList<String>();
		EventDAO eventDao = new EventDAO();
		UserEventDAO userEventDao = new UserEventDAO();
		eventsIdsList = userEventDao.getAvailableEvents(getUserRequester().getFacebookId());
		if (eventsIdsList != null) {
			for (String id : eventsIdsList) {
				AppEvent event = eventDao.getEvent(id);
				if(event!=null){
					event.setUserEventList(userEventDao.getUserEventsFromEvent(event.getEventId()));
					eventsList.add(event.getEventWithoutPrivateInfo(getUserRequester().getFacebookId()));
				}else{
					serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
					serverReplyMessage.setErrorMessage("Error Retrieving a specific event");
					
				}
			}

			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_SUCCES);
			serverReplyMessage.setEventsList(eventsList);
			
		}else{
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
			serverReplyMessage.setErrorMessage("Error Retrienving List of Events");
		}
		
		return serverReplyMessage;
		
		
	}
	
	public ServerMessage getWishes(ServerMessage serverReplyMessage) {

		List<Wish> wishesList = new ArrayList<Wish>();
		Set<Integer> wishesIdsList = new HashSet<Integer>();
		WishDAO wishDao = new WishDAO();
		UserWishDAO userWishDao = new UserWishDAO();
		UserDAO userDAO = new UserDAO();

		wishesIdsList.addAll(userWishDao.getAvailableWishes(getUserRequester().getFacebookId()));
		for (String id : getFriendsIdsList(getUserRequester().getFacebookId())) {
			wishesIdsList.addAll(userWishDao.getAvailableWishes(id));
		}
		
		if (wishesIdsList != null) {
			for (Integer id : wishesIdsList) {
				Wish wish = wishDao.getWish(id);
				if(wish!=null){
					User user = userDAO.getUserByFB(wish.getWishOwner().getFacebookId());
					if(user!=null){
						wish.setWishOwner(user.getUserWithoutPrivInfo());
					}
					
					List<UserWish> userWishList = userWishDao.getUserWishesFromWish(wish.getWishId());
					wish.setUserWishList(userWishList);
					if(wish.getWishOwner().getFacebookId()!=getUserRequester().getFacebookId()){
						wish = wish.getWishAsNonOwner(getUserRequester().getFacebookId());
					}
					wishesList.add(wish);
				}else{
					serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
					serverReplyMessage.setErrorMessage("Error Retrieving a specific event");
					
				}
			}

			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_SUCCES);
			serverReplyMessage.setWishesList(wishesList);
			
		}else{
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
			serverReplyMessage.setErrorMessage("Error Retrienving List of Wishes");
		}
		
		return serverReplyMessage;
		
		
	}
	

	/**
	 * @param facebookId
	 */
	public ServerMessage getUsersWish(ServerMessage serverReplyMessage, int wishId) {
		List<UserEvent> usersEventList;
		WishDAO wishDAO = new WishDAO();
		Wish wish = wishDAO.getWish(wishId);
		List<User> usersList;
		if (wish != null) {

			UserWishDAO userWishDAO = new UserWishDAO();
			List<UserWish> list = userWishDAO.getUserWishesFromWish(wishId);
			if (list != null) {
				usersList = new ArrayList<User>();
				for (UserWish userWish : list) {
					if (!userWish.getUserId().equals(getUserRequester().getFacebookId())) {
						UserDAO userDAO = new UserDAO();
						User user = userDAO.getUserByFB(userWish.getUserId());
						if (user != null) {
							user = user.getUserWithoutPrivInfo();
							usersList.add(user);
						} else {
							System.out.println("Could not retrieve user with id:" + userWish.getUserId());
						}
					}
				}

				if (wish.getLinkedEvent() != null) {
					UserEventDAO userEventDAO = new UserEventDAO();
					usersEventList = userEventDAO.getUserEventsFromEvent(wish.getLinkedEvent().getEventId());
					serverReplyMessage.setUsersEventList(usersEventList);
				}
				serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_SUCCES);
				serverReplyMessage.setUsersList(usersList);
			} else {
				serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
				serverReplyMessage.setErrorMessage("Error Retrienving UserWishList");

			}
		} else {
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
			serverReplyMessage.setErrorMessage("Error Retrienving Wish");
		}
		return serverReplyMessage;

	}
	/**
	 * @param facebookId
	 */
	private List<String> getFriendsIdsList(String facebookId) {
		FriendshipDAO friendshipDAO = new FriendshipDAO();
		List<String> result = new ArrayList<String>();
		List<String> list = friendshipDAO.getFriendsIds(facebookId);
		if(list!=null){
			result = list;
		}
		return result;
		
	}

	/**
	 * @param serverReplyMessage 
	 * 
	 */
	public void wantToGoOut(ServerMessage serverReplyMessage) {
		// TODO Auto-generated method stub
		
	}



	public User getUserRequester() {
		return userRequester;
	}

	public void createUserRequester(String userId) {
		this.userRequester = userRequester;
	}
	
	public void setUserRequester(User userRequester) {
		this.userRequester = userRequester;
	}

	/**
	 * @param id
	 * @return
	 */
	public User getUserById(String id) {
			UserDAO dao = new UserDAO();
			return dao.getUser(id);
	}

	/**
	 * @param serverReplyMessage
	 * @param facebookID
	 */
	public void acceptFriendship(ServerMessage serverReplyMessage, Friendship friendship) {
		FriendshipDAO friendshipDao = new FriendshipDAO();
		if(friendshipDao.acceptFriendship(friendship).getState()== FriendshipState.REQUEST_ACCEPTED){
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_SUCCES);

			sendFriendshipRequestAcceptedNotification(friendship.getUser1Id(), getUserRequester());
			
		}else{
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
			serverReplyMessage.setErrorMessage("Couldn't delete User");
		}
		
	}

	/**
	 * 
	 */
	private void sendFriendshipRequestAcceptedNotification(String to, User from) {
		ServerMessage serverNotificationMessage = new ServerMessage(to, ServerMessageType.NOTIFY_FRIENDSHIP_REQUEST_RECEIVED);
		serverNotificationMessage.setFriendshipRequestAcceptedFrom(from.getFacebookId());
		sendNotification(serverNotificationMessage);
	}

	/**
	 * @param serverReplyMessage
	 * @param facebookID
	 */
	public void refuseFriendship(ServerMessage serverReplyMessage, String facebookID) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param serverReplyMessage
	 * @param messageId
	 * @param event
	 * @return
	 */
	public ServerMessage createEvent(ServerMessage serverReplyMessage, AppEvent event, List<String> fbIdsList, int wishId) {
		EventDAO dao = new EventDAO();
		FriendshipDAO friendshipDAO = new FriendshipDAO();
		event.setEventOwner(getUserRequester());
		AppEvent newEvent = dao.createNewEvent(event.getName(), event.getEventDateTimeStart(), event.getLocation(), event.getEventOwner(), event.getEventType());
		if (newEvent != null) {
			createOwnerUserEvent(getUserRequester().getFacebookId(), newEvent.getEventId());
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_SUCCES);
			inviteToEvent(fbIdsList, newEvent);
			// if(newEvent.getEventVisualizationPrivacy()==EventVisualizationPrivacy.ALL_FRIENDS){
			List<String> friendshipIdsList = friendshipDAO.getFriendsIds(getUserRequester().getFacebookId());
			List<String> listToNotify = removeInvitedUsersFromList(friendshipIdsList, fbIdsList);
			addUserEventAsIdle(event, listToNotify);
			
			if(wishId!=-1){
				WishDAO wishDAO = new WishDAO();
				if(wishDAO.updateWishEvent(wishId, newEvent.getEventId())){
					
				}else{
					System.out.println("Could not Update Wish with new EventId");
				}
			}

			// }
		} else {
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
			serverReplyMessage.setErrorMessage("Couldn't create User");
		}
		return serverReplyMessage;
	}


	/**
	 * @param serverReplyMessage
	 * @param messageId
	 * @param event
	 * @return
	 */
	public ServerMessage createWish(ServerMessage serverReplyMessage, Wish wish) {
		WishDAO dao = new WishDAO();
		FriendshipDAO friendshipDAO = new FriendshipDAO();
		UserDAO userDAO = new UserDAO();
		wish.setWishOwner(getUserRequester());
		Wish newWish = dao.createNewWish(wish.getName(), wish.getWishDateTime(), wish.getWishOwner(), wish.getEventType());
		if (newWish != null) {
			createOwnerUserWish(getUserRequester().getFacebookId(), newWish.getWishId());
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_SUCCES);
			List<String> friendshipIdsList = friendshipDAO.getFriendsIds(getUserRequester().getFacebookId());
			for (String id : friendshipIdsList) {
				User user = userDAO.getUserByFB(id);
				if(user!=null){
					sendNewWishAvailableNotification(user.getRegId(), newWish.getWishAsNonOwner(user.getFacebookId()));
				}
				
			}

			// }
		} else {
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
			serverReplyMessage.setErrorMessage("Couldn't create Wish");
		}
		return serverReplyMessage;
	}

	/**
	 * @param facebookId
	 * @param eventId
	 */
	private void createOwnerUserEvent(String facebookId, int eventId) {
		UserEventDAO dao= new UserEventDAO();
		UserEvent userEvent = dao.createUserEvent(facebookId, eventId, UserEventState.OWNER);
	}
	
	/**
	 * @param facebookId
	 * @param eventId
	 */
	private void createOwnerUserWish(String facebookId, int wishId) {
		UserWishDAO dao= new UserWishDAO();
		UserWish userWish = dao.createUserWish(facebookId, wishId);
		if(userWish==null){
			System.out.println("Could not create UserWish for Owner");
		}
	}

/**
	 * @param eventId
	 * @param listToNotify
	 */
	private void addUserEventAsIdle(AppEvent event, List<String> listToNotify) {
		
		UserEventDAO dao= new UserEventDAO();
		UserDAO userDao = new UserDAO();
		for (String id : listToNotify) {
			UserEvent userEvent = dao.createUserEvent(id, event.getEventId(), UserEventState.IDLE);
			if(userEvent!=null){
				event.getUserEventList().add(userEvent);
			}
			
		}
		for (UserEvent userEvent : event.getUserEventList()) {
			AppEvent eventToSend = event;
			eventToSend.setUserEventList(dao.getUserEventsFromEvent(event.getEventId()));
			User user = userDao.getUserByFB(userEvent.getUserId());
			if(user!=null){
				sendNewEventAvailableNotification(user.getRegId(), eventToSend.getEventWithoutPrivateInfo(user.getFacebookId()));
			}
			
		}
			
		
	}

/**
 * @param regId
 * @param eventWithoutPrivateInfo
 */
private void sendNewEventAvailableNotification(String regId, AppEvent event) {
	ServerMessage serverNotificationMessage = new ServerMessage(regId, ServerMessageType.NOTIFY_NEW_EVENTAVAILABLE);
	serverNotificationMessage.setEvent(event);
	sendNotification(serverNotificationMessage);
	
}

/**
 * @param regId
 * @param eventWithoutPrivateInfo
 */
private void sendNewWishAvailableNotification(String regId, Wish wish) {
	ServerMessage serverNotificationMessage = new ServerMessage(regId, ServerMessageType.NOTIFY_NEW_WISH_AVAILABLE);
	serverNotificationMessage.setWish(wish);
	sendNotification(serverNotificationMessage);
	
}

/**
	 * @param friendshipIdsList
	 * @param fbIdsList
	 * @return
	 */
	private List<String> removeInvitedUsersFromList(List<String> friendshipIdsList, List<String> fbIdsList) {

		List<String> union = new ArrayList<String>(friendshipIdsList);
		union.addAll(fbIdsList);
		// Prepare an intersection
		List<String> intersection = new ArrayList<String>(friendshipIdsList);
		intersection.retainAll(fbIdsList);
		// Subtract the intersection from the union
		union.removeAll(intersection);
		return union;
	}

/**
 * @param serverReplyMessage
 * @return
 */
public ServerMessage getUsersForNewEvent(ServerMessage serverReplyMessage) {
	FriendshipDAO friendshipDAO = new FriendshipDAO();
	List<String> list = friendshipDAO.getFriendsIds(getUserRequester().getFacebookId());
	List<User> usersList = new ArrayList<User>();
	if(list!=null){
		UserDAO userDAO = new UserDAO();
		for (String id : list) {
			User user = userDAO.getUserByFB(id);
			if(user!=null){
				usersList.add(user);
			}else{
				System.out.println("Could not retrieve user with id:"+id);
			}
		}

		serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_SUCCES);
		serverReplyMessage.setUsersList(usersList);
	}else{
		serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
		serverReplyMessage.setErrorMessage("Couldn't get list of friends");
	}
	return serverReplyMessage;
}
	

}
