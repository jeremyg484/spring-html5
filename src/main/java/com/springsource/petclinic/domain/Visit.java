package com.springsource.petclinic.domain;

import javax.persistence.Entity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.roo.addon.entity.RooEntity;
import javax.validation.constraints.Size;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import com.springsource.petclinic.domain.Pet;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import com.springsource.petclinic.domain.Vet;

@Entity
@RooJavaBean
@RooToString
@RooEntity(finders = { "findVisitsByDescriptionAndVisitDate", "findVisitsByVisitDateBetween", "findVisitsByDescriptionLike" })
public class Visit {

    @Size(max = 255)
    private String description;

    @NotNull
    @Past
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Date visitDate;

    @NotNull
    @ManyToOne(targetEntity = Pet.class)
    @JoinColumn
    private Pet pet;

    @ManyToOne(targetEntity = Vet.class)
    @JoinColumn
    private Vet vet;
}
