package com.mussum.models.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mussum.models.MussumEntity;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public class Usuario extends MussumEntity {

    @NotNull(message = "O campo nome não pode ser nulo")
    private String username;

    @NotNull(message = "O campo senha não pode ser nulo")
    private String password;

    private String role; // "professor" ou "admin" if its a super user

    public void setDefaultRole() {
	this.role = "professor";
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    @JsonIgnore
    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getRole() {
	return role;
    }

    public void setRole(String roles) {
	this.role = roles;
    }

//    public void addRole(String role) {
//        this.role += "," + role;
//    }
//    public void removeRole(String role) {
//        String[] currentRoles = this.role.split(",");
//        String newRoles = "";
//        int count = 0;
//        for (String rol : currentRoles) {
//            count++;
//            if (!rol.equalsIgnoreCase(role)) {
//                if (count == currentRoles.length) {
//                    newRoles += rol;
//                } else {
//                    newRoles += rol + ",";
//                }
//            }
//        }
//        this.role = newRoles;
//    }
}
