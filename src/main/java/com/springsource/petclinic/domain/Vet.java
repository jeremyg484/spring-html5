package com.springsource.petclinic.domain;

import com.springsource.petclinic.domain.AbstractPerson;
import javax.persistence.Entity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.roo.addon.entity.RooEntity;
import java.util.Calendar;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import com.springsource.petclinic.reference.Specialty;
import javax.persistence.Enumerated;

@Entity
@RooJavaBean
@RooToString
@RooEntity
public class Vet extends AbstractPerson {

    @NotNull
    @Past
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Calendar employedSince;

    @Enumerated
    private Specialty specialty;
}
