package com.springsource.petclinic.web;

import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import com.springsource.petclinic.domain.Vet;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@RooWebScaffold(path = "vets", formBackingObject = Vet.class)
@RequestMapping("/vets")
@Controller
public class VetController {
}
