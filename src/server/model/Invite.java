/**
 * 
 */
package server.model;

import java.sql.Timestamp;

/**
 * @author Tiago
 *
 */
public class Invite {

	private User sender;
	private User receiver;
	private AppEvent event;
	private Timestamp dateOfInvite;

}
