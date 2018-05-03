package com.mussum.models.db;

import com.mussum.models.MussumObject;
import java.io.Serializable;
import javax.persistence.Entity;

@Entity
public class Admin_Aviso extends MussumObject implements Serializable {

    private String titulo;

    private String detalhado;

}
