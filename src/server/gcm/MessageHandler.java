/**
 * 
 */
package server.gcm;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.json.simple.JSONValue;

import server.gcm.ClientMessage.ClientContentTypeKey;
import server.gcm.ClientMessage.ClientMessageType;
import server.gcm.ServerMessage.ServerMessageType;
import server.model.AppServer;
import server.model.Core;
import server.model.User;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Tiago
 * 
 */
public class MessageHandler {


	public void handleMessage(Map<String, Object> jsonMap) {
		ClientMessage msg = getMessage(jsonMap);
		if (msg.getMessageType() != null) {
			if ("ack".equals(msg.getMessageType().toString())) {
				// Process Ack
				handleAckReceipt(jsonMap);
			} else if ("nack".equals(msg.getMessageType().toString())) {
				// Process Nack
				handleNackReceipt(jsonMap);
			} else {
				// Normal upstream data message
				// try {
				ServerMessage serverMessage = handleIncomingDataMessage(msg);
				if (serverMessage != null) {
					sendMessageToClient(serverMessage);
					// // Send ACK to CCS
					// String ack = createJsonAck(msg.getFrom(), msg.getMessageId());
					// send(ack);
					// }
					// catch (Exception e) {
					// // Send NACK to CCS
					// String nack = createJsonNack(msg.getFrom(), msg.getMessageId());
					// send(nack);
					// }
				}
			}
		} else {
			AppServer.getGcmManager().logger.log(Level.WARNING, "Unrecognized message type (%s)", msg.getMessageType().toString());
		}
	}

        /**
     * Handles an ACK.
     *
     * <p>Logs a INFO message, but subclasses could override it to
     * properly handle ACKs.
     */
    protected void handleAckReceipt(Map<String, Object> jsonObject) {
//        String messageId = (String) jsonObject.get("message_id");
//        String from = (String) jsonObject.get("from");
//        AppServer.getGcmManager().logger.log(Level.INFO, "handleAckReceipt() from: " + from + ",messageId: " + messageId);
    }

    /**
     * Handles a NACK.
     *
     * <p>Logs a INFO message, but subclasses could override it to
     * properly handle NACKs.
     */
    protected void handleNackReceipt(Map<String, Object> jsonObject) {
//        String messageId = (String) jsonObject.get("message_id");
//        String from = (String) jsonObject.get("from");
//        AppServer.getGcmManager().logger.log(Level.INFO, "handleNackReceipt() from: " + from + ",messageId: " + messageId);
    }


	
	public void sendMessageToClient(ServerMessage msg) {
		GCMManager client = AppServer.getGcmManager();
		String msgId = System.currentTimeMillis()+"";
		Map<String, Object> data = new HashMap <String, Object>();
		data.put("data", msg.getContent());
		
		String jsonRequest = GCMManager.createJsonMessage(msg.getFrom(), msgId, data, null, null, false);
//		String jsonRequest = GCMManager.createJsonMessage("APA91bFcJlSaIYQyKM2sTCpIb9PbuVWllZDlNlt-hLcpVgJHgmRkr7JtGtA_XfupFisT_cHzKTtcuxbkMXnx7BSs7LIz1nZb7iPHDftoxm11jv7QxlzEFgg5_mBEVxfQQ9ES5kTnEZJU", "1212121212", new HashMap<String, Object>(), "sample", 10000L, true);
		client.send(jsonRequest);
	}
	
	/**
     * 
     */
	private ClientMessage getMessage(Map<String, Object> jsonObject) {
		String from = jsonObject.get("from").toString();

		// unique id of this message
		String messageId = (String) jsonObject.get("message_id");

//		ClientMessageType category = ClientMessageType.valueOf((String) jsonObject.get("message_type"));

		@SuppressWarnings("unchecked")
		Map<String, String> data = (Map<String, String>) jsonObject.get("data");
		
		ClientMessageType category = null;
		
		String type = (String) jsonObject.get("message_type");
		if( type!= null){
			category = ClientMessageType.valueOf(type);
		}
		
		
		
		Map<String, String> json;
		ClientMessage msg = null;
		if(data != null){
		try {
			
			
//			 Gson gson = new Gson();
//		     Type type = new TypeToken<Map<String,Object>>() {}.getType();

//	        Map<String,Object> deserealizedData = gson.fromJson((String)data.get("json"), type);
		        
			
		     
			json = (Map<String, String>)  JSONValue.parseWithException(((String)data.get("json")));

//			Map <String, Object> content = new HashMap<>();
//
//				ObjectMapper mapper = new ObjectMapper();
//				mapper.configure(MapperFeature.AUTO_DETECT_FIELDS, true);
//				// Map<String, User> map;
//				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//				Object obj;
//				for (int i = 0; i <= ClientContentTypeKey.values().length; i++) {
//
//					String string = json.get(ClientContentTypeKey.values()[i]);
//					if (string != null) {
//						if (ClientContentTypeKey.values()[i] == ClientContentTypeKey.FB_IDS_LIST) {
//							obj = mapper.readValue(string, new TypeReference<ArrayList<User>>() {});
//							content.put(ClientContentTypeKey.values()[i].name(), obj);
//
//						} else {
//							obj = mapper.readValue(string, ClientContentTypeKey.values()[i].clazz);
//						}
//					}
//				}
			
			// PackageName of the application that sent this message.
			category = ClientMessageType.valueOf((String) json.get(ClientContentTypeKey.MESSAGE_TYPE.name()));
			msg = new ClientMessage(from, category, messageId, json);
		} catch (ParseException | org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
		}else{	
			// PackageName of the application that sent this message.
//			ClientMessageType category = ClientMessageType.valueOf((String) jsonObject.get("message_type"));
//			msg = new ClientMessage(from, category, messageId, null);
//			System.out.println("Client Message with id: "+messageId+" ,is empty");
			msg = new ClientMessage(category);
			
		}

		return msg;
	}

	// / new: customized version of the standard handleIncomingDateMessage
	// method
	/**
	 * Handles an upstream data message from a device application.
	 * @return 
	 */
	public ServerMessage handleIncomingDataMessage(ClientMessage clientMessage) {
		ServerMessage serverReplyMessage = new ServerMessage(clientMessage.getFrom());
		serverReplyMessage.setMessageRepliedId(clientMessage.getMessageId());
		if (clientMessage.getMessageType() != null) {

			Core core = new Core();
			User user = core.getUserById(clientMessage.getFrom());
			if (user!=null) {

				core.setUserRequester(user);
				
				switch (clientMessage.getMessageType()) {
				case DELETE_USER:
					core.deleteUser(serverReplyMessage);
					break; 
				case EDIT_CONFIG:
//					core.editConfig(serverReplyMessage, clientMessage.getConfigKey(), clientMessage.getConfigValue()); //not implemented
					break; 
				case REQUEST_FRIENDSHIP_USERNAME:
//					core.requestFriendshipByUsername(serverReplyMessage, clientMessage.getUsername()); //not implemented
					break; 
				case REQUEST_FRIENDSHIP_FB:
					serverReplyMessage = core.requestFriendshipByFacebook(serverReplyMessage, clientMessage.getFacebookIdsList());
					break; 
				case ACCEPT_FRIENDSHIP:
//					core.acceptFriendship(serverReplyMessage, clientMessage.getFriendshipRequest()); //not implemented
					break; 
				case REFUSE_FRIENDSHIP:
//					core.refuseFriendship(serverReplyMessage, clientMessage.getFacebookID());//not implemented
					break; 
				case SEARCH_USER: 
//					core.searchUser(serverMessage); //not implemented
					break; 
				case REMOVE_FRIEND:
//					core.removeFriend(serverReplyMessage, clientMessage.getFacebookID()); //not implemented
					break; 
				case CREATE_EVENT:
					serverReplyMessage = core.createEvent(serverReplyMessage, clientMessage.getEvent(), clientMessage.getFacebookIdsList(), clientMessage.getWishID());
					break;
				case CREATE_WISH:
					serverReplyMessage = core.createWish(serverReplyMessage, clientMessage.getWish());
					break; 
				case EDIT_EVENT:
//					core.editEvent(serverMessage); //not implemented
					break; 
				case INVITE_TO_EVENT:
//					core.inviteToEvent(serverReplyMessage, clientMessage.getInviteID(), clientMessage.getFacebookID()); //not implemented
					break; 
				case ACCEPT_INVITE:
//					core.acceptInvite(serverReplyMessage, clientMessage.getInviteID()); //not implemented
					break; 
				case REFUSE_INVITE:
//					core.refuseInvite(serverReplyMessage, clientMessage.getInviteID()); //not implemented
					break; 
				case JOIN_EVENT:
//					core.joinEvent(serverReplyMessage, clientMessage.getEventID()); //not implemented
					break; 
				case JOIN_WISH:
					serverReplyMessage = core.joinWish(serverReplyMessage, clientMessage.getWishID());
					break; 
				case LEAVE_EVENT:
//					core.leaveEvent(serverReplyMessage, clientMessage.getEventID()); //not implemented
					break; 
				case REQUEST_EVENTS:
					serverReplyMessage = core.getEvents(serverReplyMessage);
					break; 
				case REQUEST_USERS_NEW_EVENT_LIST:
					serverReplyMessage = core.getUsersForNewEvent(serverReplyMessage);
					break; 
				case REQUEST_WISHES:
					serverReplyMessage = core.getWishes(serverReplyMessage);
					break; 
				case REQUEST_USERS_WISH_LIST:
					serverReplyMessage = core.getUsersWish(serverReplyMessage, clientMessage.getWishID());
					break; 
				case WANT_TO_GO_OUT:
//					core.wantToGoOut(serverReplyMessage); //not implemented
					break; 
				// You can have any number of case statements.
				default: 
					// Statements
				}
				// PayloadProcessor processor =
				// ProcessorFactory.getProcessor(msg.getContent().get("action"));
				// processor.handleMessage(msg);
			} else {

				if (clientMessage.getMessageType() == ClientMessageType.CREATE_USER) {
					serverReplyMessage = core.createNewUser(serverReplyMessage, clientMessage.getFrom(), clientMessage.getUserCreated());
				} else {

				}
			}
		}
		return serverReplyMessage;
	}

}
