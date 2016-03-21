/**
 * 
 */
package server.model;

import java.util.ArrayList;
import java.util.List;

import server.DAO.FriendshipDAO;
import server.DAO.UserDAO;
import server.gcm.MessageHandler;
import server.gcm.ServerMessage;
import server.gcm.ServerMessage.ServerMessageType;
import server.model.Friendship.FriendshipState;
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
	public ServerMessage createNewUser(ServerMessage serverReplyMessage, String clientMessageId, String id, String facebookId, String name, String surname) {
			UserDAO dao = new UserDAO();
			serverReplyMessage.setMessageRepliedId(clientMessageId);
			if(dao.createNewUser(id, facebookId, name, surname)!=null){
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
		if(dao.deleteUser(getUserRequester().getId())){
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
	public ServerMessage requestFriendshipByFacebook(ServerMessage serverReplyMessage,String clientMessageId, List<String> facebookIDsList) {
		FriendshipDAO friendshipDao = new FriendshipDAO();
		serverReplyMessage.setMessageRepliedId(clientMessageId);
		UserDAO userDao = new UserDAO();
		boolean success=true;
		List<String> failedToAddIds = new ArrayList<String>();
		for(String facebookId:facebookIDsList){
			User userRequested = userDao.getUserByFB(facebookId);
			if(userRequested!=null && friendshipDao.requestFriendship(getUserRequester().getFacebookId(), userRequested.getFacebookId())!=null){
				sendFriendshipRequestNotification(userRequested.getId(), getUserRequester().getUserWithoutPrivInfo());
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
		serverNotificationMessage.setFriendshipRequester(from.getFacebookId());
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
	public void inviteToEvent(String facebookID) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param inviteID
	 * @param facebookID
	 */
	public void inviteToEvent(ServerMessage serverReplyMessage, int inviteID, String facebookID) {
		// TODO Auto-generated method stub
		
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
	 * 
	 */
	public void getEvents(ServerMessage serverReplyMessage) {
		// TODO Auto-generated method stub
		
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

}
