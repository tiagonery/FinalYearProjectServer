/**
 * 
 */
package server.model;


/**
 * @author Tiago
 *
 */
public class Post {

	private User user;
	private AppEvent event;
	private PostMessageType messageType;
	

	public enum PostMessageType{
		
		CHECKED_IN(StringConstants.CHECKED_IN),
		CONFIRMED_PRESENCE(StringConstants.CONFIRMED_PRESENCE),
		CREATED_EVENT(StringConstants.CREATED_EVENT),
		CANCELED_PRESENCE(StringConstants.CANCELED_PRESENCE),
		USER_STATUS_CHANGED(StringConstants.USER_STATUS_CHANGED);
		
		private PostMessageType(String message) {
			this.message = message;
		}

		private String message;

		public String getMessage() {
			return message;
		}
	}


	public User getUser() {
		return user;
	}
	
	public String getPostString() {
		String result = "";
		switch (getMessageType()) {
		case CHECKED_IN:
		case CONFIRMED_PRESENCE:
		case CREATED_EVENT:
		case CANCELED_PRESENCE:
				result = getMessageType().getMessage().replaceFirst("@", getUser().getName());
				result = result.replaceFirst("@", getEvent().getName());

			break;
		case USER_STATUS_CHANGED:
			result = getMessageType().getMessage().replaceFirst("@", getUser().getName());
			result = result.replaceFirst("@", getUser().getUserStatus().getMessage());

		break;
			

		default:
			break;
		}
		return result;
	}
	

	public void setUser(User user) {
		this.user = user;
	}

	public PostMessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(PostMessageType messageType) {
		this.messageType = messageType;
	}

	public AppEvent getEvent() {
		return event;
	}

	public void setEvent(AppEvent event) {
		this.event = event;
	}
	
	
}
