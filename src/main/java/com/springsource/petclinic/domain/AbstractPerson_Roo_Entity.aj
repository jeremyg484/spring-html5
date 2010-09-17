// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.springsource.petclinic.domain;

import com.springsource.petclinic.domain.AbstractPerson;
import java.lang.Integer;
import java.lang.Long;
import java.lang.SuppressWarnings;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import org.springframework.transaction.annotation.Transactional;

privileged aspect AbstractPerson_Roo_Entity {
    
    @PersistenceContext
    transient EntityManager AbstractPerson.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long AbstractPerson.id;
    
    @Version
    @Column(name = "version")
    private Integer AbstractPerson.version;
    
    public Long AbstractPerson.getId() {
        return this.id;
    }
    
    public void AbstractPerson.setId(Long id) {
        this.id = id;
    }
    
    public Integer AbstractPerson.getVersion() {
        return this.version;
    }
    
    public void AbstractPerson.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void AbstractPerson.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void AbstractPerson.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            AbstractPerson attached = this.entityManager.find(this.getClass(), this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void AbstractPerson.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public AbstractPerson AbstractPerson.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AbstractPerson merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager AbstractPerson.entityManager() {
        EntityManager em = new AbstractPerson(){
        }.entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long AbstractPerson.countAbstractpeople() {
        return ((Number) entityManager().createQuery("select count(o) from AbstractPerson o").getSingleResult()).longValue();
    }
    
    @SuppressWarnings("unchecked")
    public static List<AbstractPerson> AbstractPerson.findAllAbstractpeople() {
        return entityManager().createQuery("select o from AbstractPerson o").getResultList();
    }
    
    public static AbstractPerson AbstractPerson.findAbstractPerson(Long id) {
        if (id == null) return null;
        return entityManager().find(AbstractPerson.class, id);
    }
    
    @SuppressWarnings("unchecked")
    public static List<AbstractPerson> AbstractPerson.findAbstractPersonEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("select o from AbstractPerson o").setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
