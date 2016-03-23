/*
 * Copyright 2014 Wolfram Rittmeyer.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server.gcm;

import java.util.Map;

import server.model.User;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * Represents a message for CCS based massaging.
 */
public class ServerMessage extends AbstractMessage{

	

	public enum ServerMessageType {

		REPLY_SUCCES,
		REPLY_ERROR,
		NOTIFY_FRIENDSHIP_REQUEST_RECEIVED,
		NOTIFY_INVITATION_RECEIVED;

	}
	
	public enum ServerContentTypeKey {

		MESSAGE_TYPE, 
		MESSAGE_REPLIED_ID,
		ERROR_MESSAGE,
		FRIENDSHIP_REQUEST_FROM,
		FRIENDSHIP_REQUEST_ACCEPTED_FROM;
		
	}


    /**
     * Recipient-ID.
     */
    private String to;
    
    
    
    /**
     * Sender app's package.
     */
    private ServerMessageType serverMessageType;

    public ServerMessage(String to, ServerMessageType serverMessageType, String messageId, Map<String, String> content) {
        super (messageId, content);
    	this.to = to;
        setServerMessageType(serverMessageType);
    }
    
	/**
	 * @param to2
	 * @param messageType
	 */
	public ServerMessage(String to2, ServerMessageType messageType) {
        this.to = to;	
        setServerMessageType(messageType);
    
	}

	/**
	 * @param from
	 */
	public ServerMessage(String to) {
        this.to = to;	
	}

	/**
	 * 
	 */

	public String getFrom() {
        return to;
    }

    public ServerMessageType getServerMessageType() {
        return serverMessageType;
    }

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

    public String getMessageError() {
        return (String) getContent().get(ServerContentTypeKey.ERROR_MESSAGE.name());
    }

	public void setMessageRepliedId(String messageRepliedId) {
        getContent().put(ServerContentTypeKey.MESSAGE_REPLIED_ID.name(), messageRepliedId);
	}

	public void setServerMessageType(ServerMessageType serverMessageType) {
        getContent().put(ServerContentTypeKey.MESSAGE_TYPE.name(),serverMessageType.name());
		this.serverMessageType = serverMessageType;
	}

	/**
	 * @param string
	 */
	public void setErrorMessage(String string) {
		getContent().put(ServerContentTypeKey.ERROR_MESSAGE.name(), string);
		
	}

	/**
	 * @param from
	 */
	public void setFriendshipRequester(User user) {
		User stripedUser = user.getUserWithoutPrivInfo();
		String string = getJsonValueOf(stripedUser);
		getContent().put(ServerContentTypeKey.FRIENDSHIP_REQUEST_FROM.name(), string);
		
	}

	/**
	 * @param from
	 */
	public void setFriendshipRequestAcceptedFrom(String from) {
		getContent().put(ServerContentTypeKey.FRIENDSHIP_REQUEST_ACCEPTED_FROM.name(), from);
	}



	private String getJsonValueOf(Object object) {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json=null;
		try {
			json = ow.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;

	}

}
