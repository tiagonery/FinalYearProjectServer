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
		
	}
	
	public ServerMessage inviteToEvent(ServerMessage serverReplyMessage, List<String> facebookIDsList, int eventId) {
		EventDAO eventDAO = new EventDAO();
		AppEvent event = eventDAO.getEvent(eventId);
		if(event!=null){
			inviteToEvent(facebookIDsList, event);
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_SUCCES);
		}else{
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
			serverReplyMessage.setErrorMessage("Couldn't delete Event");
		}
	return serverReplyMessage;
	}

	/**
	 * @param facebookID
	 */
	public void inviteToEvent(List<String> facebookIDsList, AppEvent event) {
		UserEventDAO dao= new UserEventDAO();
		UserDAO userDao = new UserDAO();
		FriendshipDAO friendshipDAO = new FriendshipDAO();
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
//		List<String> friendshipIdsList = friendshipDAO.getFriendsIds(getUserRequester().getFacebookId());
//		List<String> listToNotify = removeInvitedUsersFromList(friendshipIdsList, facebookIDsList);
//		addUserEventAsIdle(event, listToNotify);
				
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
	public ServerMessage leaveEvent(ServerMessage serverReplyMessage, int eventID) {
		UserEventDAO userEventDAO = new UserEventDAO();
		if(userEventDAO.updateUserEventStatus(getUserRequester().getFacebookId(), eventID, UserEventState.NOT_GOING)!=null){
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_SUCCES);
		}else{
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
			serverReplyMessage.setErrorMessage("Could not update UserEvent status");
		}
		return serverReplyMessage;
		
	}

	/**
	 * @param serverReplyMessage 
	 * @param eventID
	 */
	public ServerMessage joinEvent(ServerMessage serverReplyMessage, int eventID) {
		UserEventDAO userEventDAO = new UserEventDAO();
		if(userEventDAO.updateUserEventStatus(getUserRequester().getFacebookId(), eventID, UserEventState.GOING)!=null){
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_SUCCES);
		}else{
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
			serverReplyMessage.setErrorMessage("Could not update UserEvent status");
		}
		return serverReplyMessage;
	}

	/**
	 * @param serverReplyMessage
	 * @param eventID
	 */
	public ServerMessage joinWish(ServerMessage serverReplyMessage, int wishId) {
		UserWishDAO userWishDAO = new UserWishDAO();
		if (userWishDAO.getUserWish(wishId, getUserRequester().getFacebookId()) == null) {
			UserWish userWish = userWishDAO.createUserWish(getUserRequester().getFacebookId(), wishId);
			if (userWish != null) {
				serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_SUCCES);
				serverReplyMessage.setUserWish(userWish);
			} else {
				serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
				serverReplyMessage.setErrorMessage("Could not create UserWish");
				System.out.println("Could not create UserWish");
			}
		} else {
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
			serverReplyMessage.setErrorMessage("User already joined the Wish");
			System.out.println("User already joined the Wish");

		}
		return serverReplyMessage;

	}

	/**
	 * @param serverReplyMessage
	 * @param eventID
	 */
	public ServerMessage leaveWish(ServerMessage serverReplyMessage, int wishId) {
		UserWishDAO userWishDAO = new UserWishDAO();
		if (userWishDAO.getUserWish(wishId, getUserRequester().getFacebookId()) != null) {
			if (userWishDAO.deleteUserWish(wishId, getUserRequester().getFacebookId())) {
				serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_SUCCES);
			}
			else{
				serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
				serverReplyMessage.setErrorMessage("Could not delete UserWish");
				System.out.println("Could not delete UserWish");
			}
		} else {
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
			serverReplyMessage.setErrorMessage("There is no UserWish to Update");
			System.out.println("There is no UserWish to Update");
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
		List<Integer> eventsIdsList = new ArrayList<Integer>();
		EventDAO eventDao = new EventDAO();
		UserEventDAO userEventDao = new UserEventDAO();
		eventsIdsList = userEventDao.getAvailableEvents(getUserRequester().getFacebookId());
		if (eventsIdsList != null) {
			for (int id : eventsIdsList) {
				AppEvent event = eventDao.getEvent(id);
				if(event!=null){
					UserEvent userEvent = userEventDao.getUserEvent(getUserRequester().getFacebookId(), event.getEventId());
					List<UserEvent> userEventsList = new ArrayList<UserEvent>();
					if(userEvent!=null){
						userEventsList.add(userEvent);
					}else{
						
					}
					event.setUserEventList(userEventsList);
					eventsList.add(event);
				}else{
					System.out.println("Error Retrieving event with ID: "+id);
					
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
	 * @return
	 * 
	 */
	public ServerMessage getFriendsList(ServerMessage serverReplyMessage) {

		List<User> friendsList = new ArrayList<User>();
		List<Friendship> friendshipList = new ArrayList<Friendship>();
		UserDAO userDao = new UserDAO();
		FriendshipDAO friendshipDAO = new FriendshipDAO();
		friendshipList = friendshipDAO.getAllFriendships(getUserRequester().getFacebookId());
		if (friendshipList != null) {
			for (Friendship friendship : friendshipList) {
				User user = null;
				if (friendship.getUser1Id().equals(getUserRequester().getFacebookId())) {
					user = userDao.getUserByFB(friendship.getUser2Id());
				} else {
					user = userDao.getUserByFB(friendship.getUser1Id());
				}
				if (user != null) {
					if (friendship.getState() != FriendshipState.REQUEST_REFUSED) {
						user = user.getUserWithoutPrivInfo();
						friendsList.add(user);
					}
				}

			}
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_SUCCES);
			serverReplyMessage.setUsersList(friendsList);
			serverReplyMessage.setFriendshipList(friendshipList);

		} else {
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
			serverReplyMessage.setErrorMessage("Error Retrienving List of Friendship");
		}

		return serverReplyMessage;

	}
	/**
	 * @param serverReplyMessage
	 * @return
	 * 
	 */
	public ServerMessage getUsersByName(ServerMessage serverReplyMessage, String userName) {

		List<User> usersList = new ArrayList<User>();
		List<Friendship> friendshipList = new ArrayList<Friendship>();
		UserDAO userDao = new UserDAO();
		FriendshipDAO friendshipDAO = new FriendshipDAO();
		friendshipList = friendshipDAO.getAllFriendships(getUserRequester().getFacebookId());
		if (friendshipList != null) {
//			for (Friendship friendship : friendshipList) {
//				User user = null;
//				if (friendship.getUser1Id().equals(getUserRequester().getFacebookId())) {
//					user = userDao.getUserByFB(friendship.getUser2Id());
//				} else {
//					user = userDao.getUserByFB(friendship.getUser1Id());
//				}
//				if (user != null) {
//					if (friendship.getState() != FriendshipState.REQUEST_REFUSED) {
//						user = user.getUserWithoutPrivInfo();
//						friendsList.add(user);
//					}
//				}

//			}
			usersList = userDao.getUserListByName(userName);
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_SUCCES);
			serverReplyMessage.setUsersList(usersList);
			serverReplyMessage.setFriendshipList(friendshipList);

		} else {
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
			serverReplyMessage.setErrorMessage("Error Retrienving List of Friendship");
		}

		return serverReplyMessage;

	}
	

	
	/**
	 * @param serverReplyMessage
	 * @return
	 * 
	 */
	public ServerMessage getFriendshipIdsList(ServerMessage serverReplyMessage) {

		List<String> friendsIdsList = new ArrayList<String>();
		List<Friendship> friendshipList = new ArrayList<Friendship>();
		UserDAO userDao = new UserDAO();
		FriendshipDAO friendshipDAO = new FriendshipDAO();
		friendshipList = friendshipDAO.getAllFriendships(getUserRequester().getFacebookId());
		if (friendshipList != null) {
			for (Friendship friendship : friendshipList) {
				User user = null;
				if (friendship.getUser1Id().equals(getUserRequester().getFacebookId())) {
					user = userDao.getUserByFB(friendship.getUser2Id());
				} else {
					user = userDao.getUserByFB(friendship.getUser1Id());
				}
				if (user != null) {
					if (friendship.getState() != FriendshipState.REQUEST_REFUSED) {
						friendsIdsList.add(user.getFacebookId());
					}
				}

			}
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_SUCCES);
			serverReplyMessage.setUsersIdsList(friendsIdsList);

		} else {
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
			serverReplyMessage.setErrorMessage("Error Retrienving List of Friendship");
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
//					if(wish.getWishOwner().getFacebookId()!=getUserRequester().getFacebookId()){
					wish = wish.getWishWithStrippedUserWishList(getUserRequester().getFacebookId());
//					}
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
	public ServerMessage acceptFriendship(ServerMessage serverReplyMessage, Friendship friendship) {
		FriendshipDAO friendshipDao = new FriendshipDAO();
		if(friendshipDao.acceptFriendship(friendship)!=null){
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_SUCCES);

			sendFriendshipRequestAcceptedNotification(friendship.getUser1Id(), getUserRequester());
			
		}else{
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
			serverReplyMessage.setErrorMessage("Couldn't accept Friendship");
		}
		
		return serverReplyMessage;
		
	}

	/**
	 * 
	 */
	private void sendFriendshipRequestAcceptedNotification(String to, User from) {
		ServerMessage serverNotificationMessage = new ServerMessage(to, ServerMessageType.NOTIFY_FRIENDSHIP_REQUEST_ACCEPTED);
		serverNotificationMessage.setFriendshipRequestAcceptedFrom(from.getFacebookId());
		sendNotification(serverNotificationMessage);
	}

	/**
	 * 
	 */
	private void sendFriendshipRequestRefusedNotification(String to, User from) {
		ServerMessage serverNotificationMessage = new ServerMessage(to, ServerMessageType.NOTIFY_FRIENDSHIP_REQUEST_REFUSED);
		sendNotification(serverNotificationMessage);
	}

	/**
	 * @param serverReplyMessage
	 * @param facebookID
	 */
	public ServerMessage refuseFriendship(ServerMessage serverReplyMessage, Friendship friendship) {
		FriendshipDAO friendshipDao = new FriendshipDAO();
		if(friendshipDao.refuseFriendship(friendship)!=null){
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_SUCCES);

			sendFriendshipRequestRefusedNotification(friendship.getUser1Id(), getUserRequester());
			
		}else{
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
			serverReplyMessage.setErrorMessage("Couldn't refuse Friendship");
		}
		
		return serverReplyMessage;
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
					sendNewWishAvailableNotification(user.getRegId(), newWish.getWishWithStrippedUserWishList(user.getFacebookId()));
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
	 * @param serverReplyMessage
	 * @param messageId
	 * @param event
	 * @return
	 */
	public ServerMessage deleteWish(ServerMessage serverReplyMessage, int wishId) {
		WishDAO wishDao = new WishDAO();
		FriendshipDAO friendshipDAO = new FriendshipDAO();
		UserDAO userDAO = new UserDAO();
		UserWishDAO userWishDAO = new UserWishDAO();
		if(userWishDAO.deleteUserWishesByWishId(wishId)){
			if(wishDao.deleteWish(wishId)){
				serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_SUCCES);

				List<String> friendshipIdsList = friendshipDAO.getFriendsIds(getUserRequester().getFacebookId());
				for (String id : friendshipIdsList) {
					User user = userDAO.getUserByFB(id);
					if(user!=null){
						sendWishDeletedNotification(user.getRegId());
					}
				}
				
			}else{
				serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
				serverReplyMessage.setErrorMessage("Couldn't delete Wish");
				System.out.println("Couldn't delete UserWish");
			}
		}else{
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
			serverReplyMessage.setErrorMessage("Couldn't delete UserWishes");
			System.out.println("Couldn't delete UserWishes");
		}
		return serverReplyMessage;
	}

	/**
	 * @param serverReplyMessage
	 * @param messageId
	 * @param event
	 * @return
	 */
	public void deleteWish(int wishId) {
		WishDAO wishDao = new WishDAO();
		FriendshipDAO friendshipDAO = new FriendshipDAO();
		UserDAO userDAO = new UserDAO();
		UserWishDAO userWishDAO = new UserWishDAO();
		if(userWishDAO.deleteUserWishesByWishId(wishId)){
			if(wishDao.deleteWish(wishId)){
				List<String> friendshipIdsList = friendshipDAO.getFriendsIds(getUserRequester().getFacebookId());
				for (String id : friendshipIdsList) {
					User user = userDAO.getUserByFB(id);
					if(user!=null){
						sendWishDeletedNotification(user.getRegId());
					}else{
						System.out.println("coul not retrieve user with id: "+id);
					}
				}
				
			}else{
				System.out.println("Couldn't delete UserWish");
			}
		}else{
			System.out.println("Couldn't delete UserWishes");
		}
	}
	
	/**
	 * @param serverReplyMessage
	 * @param messageId
	 * @param event
	 * @return
	 */
	public ServerMessage deleteEvent(ServerMessage serverReplyMessage, int eventId) {
		EventDAO eventDao = new EventDAO();
		UserEventDAO userEventDAO = new UserEventDAO();
		WishDAO wishDAO = new WishDAO();
		UserDAO userDAO = new UserDAO();
		int wishId = wishDAO.getWishIdByEventId(eventId);
		if (wishId != -1) {
			deleteWish(wishId);
		}
		List<UserEvent> userEvents = userEventDAO.getUserEventsFromEvent(eventId);
		if (userEventDAO.deleteUserEventsByEventId(eventId)) {
			if (eventDao.deleteEvent(eventId)) {
				serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_SUCCES);
				for (UserEvent userEvent : userEvents) {
					User user = userDAO.getUserByFB(userEvent.getUserId());
					if (user != null) {
						sendEventDeletedNotification(user.getRegId());
					} else {
						System.out.println("coul not retrieve user with id: " + userEvent.getUserId());
					}
				}
			} else {
				serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
				serverReplyMessage.setErrorMessage("Couldn't delete Event");
				System.out.println("Couldn't delete Event");

			}
		} else {
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
			serverReplyMessage.setErrorMessage("Couldn't delete UserEvents");
			System.out.println("Couldn't delete UserEvents");

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
 * @param regId
 */
private void sendWishDeletedNotification(String regId) {
	ServerMessage serverNotificationMessage = new ServerMessage(regId, ServerMessageType.NOTIFY_WISH_DELETED);
	sendNotification(serverNotificationMessage);
	
}

/**
 * @param regId
 */
private void sendEventDeletedNotification(String regId) {
	ServerMessage serverNotificationMessage = new ServerMessage(regId, ServerMessageType.NOTIFY_NEW_WISH_AVAILABLE);
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

	/**
	 * @param serverReplyMessage
	 * @return
	 */
	public ServerMessage getUsersEventList(ServerMessage serverReplyMessage, int eventId) {
		UserEventDAO userEventDAO = new UserEventDAO();
		EventDAO eventDAO = new EventDAO();
		AppEvent event = eventDAO.getEvent(eventId);

		List<UserEvent> list = userEventDAO.getUserEventsFromEvent(eventId);
		List<User> usersList = new ArrayList<User>();
		if (list != null) {
			list = removePrivateInfoFromUserEventList(list, getUserRequester().getFacebookId());
			if (event.getEventOwner().getFacebookId().equals(getUserRequester().getFacebookId())) {
				FriendshipDAO friendshipDAO = new FriendshipDAO();
				List<String> usersIds = friendshipDAO.getFriendsIds(getUserRequester().getFacebookId());
				if(usersIds!=null){
					for (String useriId : usersIds) {
						UserDAO userDAO = new UserDAO();
						User user = userDAO.getUserByFB(useriId);
						if (user != null) {
							usersList.add(user);
						} else {
							System.out.println("couldnt retrieve user with id: " + useriId);
						}
					}
					
				}else{
					System.out.println("Could not retrieve friends ids");
				}
			} else {
				for (UserEvent userEvent : list) {
					UserDAO userDAO = new UserDAO();
					User user = userDAO.getUserByFB(userEvent.getUserId());
					if (user != null) {
						usersList.add(user);
					} else {
						System.out.println("couldnt retrieve user with id: " + userEvent.getUserId());
					}

				}
			}
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_SUCCES);
			serverReplyMessage.setUsersList(usersList);
			serverReplyMessage.setUsersEventList(list);
		} else {
			serverReplyMessage.setServerMessageType(ServerMessageType.REPLY_ERROR);
			serverReplyMessage.setErrorMessage("Couldn't get list of UserEvent");
			System.out.println("Couldn't get list of UserEvent");
		}
		return serverReplyMessage;
}

/**
 * @param list
 * @param facebookId
 * @return
 */
	private List<UserEvent> removePrivateInfoFromUserEventList(List<UserEvent> list, String facebookId) {
		List<UserEvent> result = new ArrayList<UserEvent>();
		UserEvent currentUserEvent = new UserEvent();
		for (UserEvent userEvent : list) {
			if (userEvent.getUserId().equals(facebookId)) {
				currentUserEvent = userEvent;
			}
		}
		for (UserEvent userEvent : list) {
			if (currentUserEvent.getState() == UserEventState.OWNER|| userEvent.getState() == UserEventState.OWNER || userEvent.getState() == UserEventState.GOING || userEvent.getUserId().equals(facebookId)) {
				result.add(userEvent);
			}
		}
		return result;
	}

}
