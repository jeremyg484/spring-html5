// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.springsource.petclinic.domain;

import com.springsource.petclinic.domain.Pet;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

privileged aspect PetDataOnDemand_Roo_DataOnDemand {
    
    declare @type: PetDataOnDemand: @Component;
    
    private Random PetDataOnDemand.rnd = new java.security.SecureRandom();
    
    private List<Pet> PetDataOnDemand.data;
    
    public Pet PetDataOnDemand.getNewTransientPet(int index) {
        com.springsource.petclinic.domain.Pet obj = new com.springsource.petclinic.domain.Pet();
        obj.setName("name_" + index);
        obj.setOwner(null);
        obj.setSendReminders(true);
        obj.setType(com.springsource.petclinic.reference.PetType.class.getEnumConstants()[0]);
        java.lang.Float weight = new Integer(index).floatValue();
        if (weight < 0) {
            weight = 0F;
        }
        obj.setWeight(weight);
        return obj;
    }
    
    public Pet PetDataOnDemand.getSpecificPet(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        Pet obj = data.get(index);
        return Pet.findPet(obj.getId());
    }
    
    public Pet PetDataOnDemand.getRandomPet() {
        init();
        Pet obj = data.get(rnd.nextInt(data.size()));
        return Pet.findPet(obj.getId());
    }
    
    public boolean PetDataOnDemand.modifyPet(Pet obj) {
        return false;
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void PetDataOnDemand.init() {
        if (data != null && !data.isEmpty()) {
            return;
        }
        
        data = com.springsource.petclinic.domain.Pet.findPetEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'Pet' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }
        
        data = new java.util.ArrayList<com.springsource.petclinic.domain.Pet>();
        for (int i = 0; i < 10; i++) {
            com.springsource.petclinic.domain.Pet obj = getNewTransientPet(i);
            obj.persist();
            data.add(obj);
        }
    }
    
}
