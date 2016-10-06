package es.udc.pa.pa001.apuestas.web.util;

import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa001.apuestas.model.userprofile.UserProfile;
import es.udc.pa.pa001.apuestas.model.userservice.UserService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;


public class UserSession {

	private Long userProfileId;
	private String firstName;
	private boolean admin=false;

	@Inject
	private UserService userService;
	
	public Long getUserProfileId() {
		return userProfileId;
	}

	public void setUserProfileId(Long userProfileId) {
		this.userProfileId = userProfileId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {		
		this.firstName = firstName;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

}
