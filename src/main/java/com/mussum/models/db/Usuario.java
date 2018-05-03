package com.mussum.models.db;

import com.mussum.models.MussumObject;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Usuario extends MussumObject {

    //@NotNull(message = "O campo nome não pode ser nulo")
    private String username;

    //@NotNull(message = "O campo senha não pode ser nulo")
    private String password;

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

}
