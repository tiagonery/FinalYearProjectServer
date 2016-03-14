/**
 * 
 */
package server.gcm;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tiago
 *
 */
public abstract class AbstractMessage {

    /**
     * Unique id for this message.
     */
    private String messageId;
    /**
     * Payload data. A String in Json format.
     */
    private Map<String, Object> content = new HashMap<String, Object>();
    

	/**
	 * @param messageId
	 * @param content
	 */
	public AbstractMessage(String messageId, Map<String, Object> content) {
        this.messageId = messageId;
        this.content = content;
	}

	/**
	 * @param messageId
	 */
	public AbstractMessage(String messageId) {
        this.messageId = messageId;
	}
	/**
	 */
	public AbstractMessage() {
	}


	public String getMessageId() {
		return messageId;
	}


	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}


	protected Map<String, Object> getContent() {
		return content;
	}

}
