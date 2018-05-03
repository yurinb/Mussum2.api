package com.mussum.models.db;

import com.mussum.models.MussumObject;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Professor_Aviso extends MussumObject implements Serializable {

    private String titulo;

    private String detalhado;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

}
