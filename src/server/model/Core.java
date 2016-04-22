/**
 * 
 */
package server.model;

import java.util.ArrayList;
import java.util.List;

import server.DAO.EventDAO;
import server.DAO.FriendshipDAO;
import server.DAO.UserDAO;
import server.DAO.UserEventDAO;
import server.gcm.MessageHandler;
import server.gcm.ServerMessage;
import server.gcm.ServerMessage.ServerMessageType;
import server.model.AppEvent.EventVisualizationPrivacy;
import server.model.Friendship.FriendshipState;
import server.model.UserEvent.UserEventState;
import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

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
			}
			
		}
		for (UserEvent userEvent : event.getUserEventList()) {
			AppEvent eventToSend = event;
			eventToSend.setUserEventList(dao.getUserEventsFromEvent(event.getEventId()));
			User user = userDao.getUserByFB(userEvent.getUserId());
			if(user!=null){
				sendEventInviteReceivedNotification(user.getRegId(), eventToSend.getEventWithoutPrivateInfo(user.getFacebookId()));
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
	public ServerMessage createEvent(ServerMessage serverReplyMessage, AppEvent event, List<String> fbIdsList) {
		EventDAO dao = new EventDAO();
		FriendshipDAO friendshipDAO = new FriendshipDAO();
		event.setEventOwner(getUserRequester()); 
		AppEvent newEvent= dao.createNewEvent(event.getName(), event.getEventDateTimeStart(), event.getLocation(), event.getEventOwner(), event.getEventVisualizationPrivacy(), event.getEventMatchingPrivacy(), event.getEventType()); 
		if(newEvent !=null){
			createOwnerUserEvent(getUserRequester().getFacebookId(), newEvent.getEventId());
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_SUCCES);
			inviteToEvent(fbIdsList, newEvent);
			if(newEvent.getEventVisualizationPrivacy()==EventVisualizationPrivacy.ALL_FRIENDS){
				List<String> friendshipIdsList = friendshipDAO.getFriendsIds(getUserRequester().getFacebookId());
				List<String> listToNotify = removeInvitedUsersFromList(friendshipIdsList, fbIdsList);
				addUserEventAsIdle(event, listToNotify);
				
				
			}
		}else{
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
			serverReplyMessage.setErrorMessage("Couldn't create User");
		}return serverReplyMessage;
	}

/**
	 * @param facebookId
	 * @param eventId
	 */
	private void createOwnerUserEvent(String facebookId, int eventId) {
		UserEventDAO dao= new UserEventDAO();
		UserDAO userDao = new UserDAO();
		UserEvent userEvent = dao.createUserEvent(facebookId, eventId, UserEventState.OWNER);
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
	

}
