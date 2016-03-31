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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import server.model.AppEvent;
import server.model.Friendship;
import server.model.User;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Represents a message for CCS based massaging.
 */
public class ClientMessage extends AbstractMessage{

	

	public enum ClientMessageType {
		
		ack,
		nack,
		CREATE_USER, 
		DELETE_USER, 
		EDIT_CONFIG, 
		REQUEST_FRIENDSHIP_USERNAME, 
		REQUEST_FRIENDSHIP_FB, 
		ACCEPT_FRIENDSHIP, 
		REFUSE_FRIENDSHIP, 
		SEARCH_USER, 
		REMOVE_FRIEND, 
		CREATE_EVENT, 
		EDIT_EVENT, 
		INVITE_TO_EVENT, 
		ACCEPT_INVITE, 
		REFUSE_INVITE, 
		JOIN_EVENT, 
		LEAVE_EVENT, 
		REQUEST_EVENTS, 
		WANT_TO_GO_OUT;

	}
	
	public enum ClientContentTypeKey {


		MESSAGE_TYPE,
		REG_ID,
		FB_ID,
		INVITE_ID,
		EVENT,
		FRIENDSHIP,
		USER_CREATED,
		FB_IDS_LIST;
	}
	
    /**
     * Recipient-ID.
     */
    private String from;
    /**
     * Sender app's package.
     */
    private ClientMessageType messageType;

    public ClientMessage(String from, ClientMessageType clientMessageType, String messageId, Map<String, String> content) {
        super (messageId, content);
    	this.from = from;
        this.messageType = clientMessageType;
    }

    public ClientMessage(ClientMessageType clientMessageType) {
        this.messageType = clientMessageType;
    }
//    
//    public String listmap_to_json_string(List<Map<String, Object>> list)
//    {       
//        JSONArray json_arr=new JSONArray();
//        for (Map<String, Object> map : list) {
//            JSONObject json_obj=new JSONObject();
//            for (Map.Entry<String, Object> entry : map.entrySet()) {
//                String key = entry.getKey();
//                Object value = entry.getValue();
//                try {
//                    json_obj.put(key,value);
//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }                           
//            }
//            json_arr.put(json_obj);
//        }
//        return json_arr.toString();
//    }
    
    /**
	 * 
	 */

	public String getFrom() {
        return from;
    }

    public ClientMessageType getMessageType() {
        return messageType;
    }


    /**
	 * @return
	 */
	public String getFacebookID() {
		String facebookId= (String) getContent().get(ClientContentTypeKey.FB_ID.name());
		return facebookId;
	}

    /**
	 * @return
	 */
	public List<String> getFacebookIdsList() {
		String json = (String) getContent().get(ClientContentTypeKey.FB_IDS_LIST.name());
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(MapperFeature.AUTO_DETECT_FIELDS, true);
	    List<String> idsList = null;
		try {
			idsList = mapper.readValue(json, new TypeReference<ArrayList<String>>(){});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return idsList;
	}


	/**
	 * @return
	 */
	public Friendship getFriendshipRequest() {
		String json = (String) getContent().get(ClientContentTypeKey.FRIENDSHIP.name());
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(MapperFeature.AUTO_DETECT_FIELDS, true);
		Friendship friendship = null;
		try {
			friendship = mapper.readValue(json, Friendship.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return friendship;
	}

	public User getUserCreated() {
		String json = (String) getContent().get(ClientContentTypeKey.USER_CREATED.name());
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(MapperFeature.AUTO_DETECT_FIELDS, true);
		User user = null;
			try {
				user = mapper.readValue(json, User.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		return user;
	}

	public AppEvent getEvent() {
		String json = (String) getContent().get(ClientContentTypeKey.EVENT.name());
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(MapperFeature.AUTO_DETECT_FIELDS, true);
		AppEvent event = null;
			try {
				event = mapper.readValue(json, AppEvent.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		return event;
	}

}
