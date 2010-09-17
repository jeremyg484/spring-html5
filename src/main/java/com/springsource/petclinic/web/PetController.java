package com.springsource.petclinic.web;

import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import com.springsource.petclinic.domain.Pet;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@RooWebScaffold(path = "pets", formBackingObject = Pet.class)
@RequestMapping("/pets")
@Controller
public class PetController {
}
