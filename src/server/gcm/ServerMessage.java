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

import server.gcm.ClientMessage.ClientMessageType;
import server.gcm.ServerMessage.ServerMessageType;
import server.model.User;

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

		ERROR_MESSAGE,
		FRIENDSHIP_REQUEST_FROM,
		FRIENDSHIP_REQUEST_ACCEPTED_FROM;
		
	}


    /**
     * Recipient-ID.
     */
    private String to;
    
    /**
     * MessageRelied-ID.
     */
    private String messageRepliedId;
    
    
    /**
     * Sender app's package.
     */
    private ServerMessageType serverMessageType;

    public ServerMessage(String to, ServerMessageType serverMessageType, String messageId, Map<String, Object> content) {
        super (messageId, content);
    	this.to = to;
        this.serverMessageType = serverMessageType;
    }
    
	/**
	 * @param to2
	 * @param notifyFriendshipRequestReceived
	 * @param currentTimeMillis
	 */
	public ServerMessage(String to2, ServerMessageType notifyFriendshipRequestReceived) {
        this.to = to;	
        this.serverMessageType = serverMessageType;
    
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

	public String getMessageRepliedId() {
		return messageRepliedId;
	}

	public void setMessageRepliedId(String messageRepliedId) {
		this.messageRepliedId = messageRepliedId;
	}

	public void setServerMessageType(ServerMessageType serverMessageType) {
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
	public void setFriendshipRequester(User from) {
		getContent().put(ServerContentTypeKey.FRIENDSHIP_REQUEST_FROM.name(), from);
		
	}

	/**
	 * @param from
	 */
	public void setFriendshipRequestAcceptedFrom(User from) {
		getContent().put(ServerContentTypeKey.FRIENDSHIP_REQUEST_ACCEPTED_FROM.name(), from);
	}



}
