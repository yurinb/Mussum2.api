package com.mussum.models.db;

import com.mussum.models.MussumObject;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Usuario extends MussumObject {

    //@NotNull(message = "O campo nome não pode ser nulo")
    private String username;

    //@NotNull(message = "O campo senha não pode ser nulo")
    private String password;

    private String roles = "user"; // or "user, admin" if its a super user

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getRoles() {
	return roles;
    }

    public void setRoles(String roles) {
	this.roles = roles;
    }

    public void addRole(String role) {
	this.roles += "," + role;
    }

    public void removeRole(String role) {
	String[] currentRoles = this.roles.split(",");
	String newRoles = "";
	int count = 0;
	for (String rol : currentRoles) {
	    count++;
	    if (!rol.equalsIgnoreCase(role)) {
		if (count == currentRoles.length) {
		    newRoles += rol;
		} else {
		    newRoles += rol + ",";
		}
	    }
	}
	this.roles = newRoles;
    }

}
