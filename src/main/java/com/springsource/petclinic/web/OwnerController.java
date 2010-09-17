package com.springsource.petclinic.web;

import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import com.springsource.petclinic.domain.Owner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@RooWebScaffold(path = "owners", formBackingObject = Owner.class)
@RequestMapping("/owners")
@Controller
public class OwnerController {
}
