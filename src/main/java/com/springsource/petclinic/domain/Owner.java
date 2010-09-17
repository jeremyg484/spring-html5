package com.springsource.petclinic.domain;

import com.springsource.petclinic.domain.AbstractPerson;
import javax.persistence.Entity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.roo.addon.entity.RooEntity;
import java.util.Set;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

@Entity
@RooJavaBean
@RooToString
@RooEntity
public class Owner extends AbstractPerson {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private Set<com.springsource.petclinic.domain.Pet> pets = new java.util.HashSet<com.springsource.petclinic.domain.Pet>();
}
