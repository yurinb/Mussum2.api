package com.mussum.models.db;

import com.mussum.models.MussumObject;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "admin")
public class Admin extends MussumObject {

    @OneToMany(mappedBy = "id")
    private List<Professor> professores;

    public List<Professor> getProfessores() {
        return professores;
    }

    public void setProfessores(List<Professor> professores) {
        this.professores = professores;
    }

}
