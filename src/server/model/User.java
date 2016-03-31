/**
 * 
 */
package server.model;

import java.io.Serializable;
import java.sql.Blob;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * @author Tiago
 *
 */
public class User implements Serializable{


	private String username;
	private String name;
	private String surname;
	private String id;
	private String facebookId;
	private UserStatus userStatus;
	private int age;
	


	public enum UserStatus{
		ON(StringConstants.ON),
		OFF(StringConstants.OFF);
		
		private UserStatus(String message) {
			this.message = message;
		}

		private String message;

		public String getMessage() {
			return message;
		}
	
	}


	public User(String id) {
		this.id = id;
	}

	public User() {
	}

	/**
	 * @param id
	 * @param facebookId
	 * @param name
	 * @param surname
	 */
	public User(String id, String facebookId, String name, String surname) {
		this(id);
		setFacebookId(facebookId);
		setName(name);
		setSurname(surname);
	}

	public String generateFullName() {
		return getName()+" "+getSurname();
	}
	public String getUserName() {
		return username;
	}


	public void setUserName(String userName) {
		this.username = userName;
	}


	public String getName() {
		if(name!=null) {
			return name;
		}else{
			return "";
		}
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getRegId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public UserStatus getUserStatus() {
		return userStatus;
	}


	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSurname() {
		if(surname!=null) {
			return surname;
		}else{
			return "";
		}
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}


    @JsonIgnore
	public User getUserWithoutPrivInfo() {
		User result = this;
		result.setId(null);
		return result;
	}

	
}
