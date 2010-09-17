package com.springsource.petclinic.domain;

import org.springframework.roo.addon.test.RooIntegrationTest;
import com.springsource.petclinic.domain.Pet;
import org.junit.Test;

@RooIntegrationTest(entity = Pet.class)
public class PetIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }
}
