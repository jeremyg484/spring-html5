// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.springsource.petclinic.web;

import com.springsource.petclinic.domain.Owner;
import com.springsource.petclinic.domain.Pet;
import com.springsource.petclinic.reference.PetType;
import java.lang.Float;
import java.lang.Long;
import java.lang.String;
import java.util.Arrays;
import java.util.Collection;
import javax.validation.Valid;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

privileged aspect PetController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST)
    public String PetController.create(@Valid Pet pet, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pet", pet);
            return "pets/create";
        }
        pet.persist();
        return "redirect:/pets/" + pet.getId();
    }
    
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String PetController.createForm(Model model) {
        model.addAttribute("pet", new Pet());
        return "pets/create";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String PetController.show(@PathVariable("id") Long id, Model model) {
        model.addAttribute("pet", Pet.findPet(id));
        model.addAttribute("itemId", id);
        return "pets/show";
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String PetController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            model.addAttribute("pets", Pet.findPetEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) Pet.countPets() / sizeNo;
            model.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            model.addAttribute("pets", Pet.findAllPets());
        }
        return "pets/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public String PetController.update(@Valid Pet pet, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pet", pet);
            return "pets/update";
        }
        pet.merge();
        return "redirect:/pets/" + pet.getId();
    }
    
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String PetController.updateForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("pet", Pet.findPet(id));
        return "pets/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String PetController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
        Pet.findPet(id).remove();
        model.addAttribute("page", (page == null) ? "1" : page.toString());
        model.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/pets?page=" + ((page == null) ? "1" : page.toString()) + "&size=" + ((size == null) ? "10" : size.toString());
    }
    
    @RequestMapping(params = { "find=ByNameAndWeight", "form" }, method = RequestMethod.GET)
    public String PetController.findPetsByNameAndWeightForm(Model model) {
        return "pets/findPetsByNameAndWeight";
    }
    
    @RequestMapping(params = "find=ByNameAndWeight", method = RequestMethod.GET)
    public String PetController.findPetsByNameAndWeight(@RequestParam("name") String name, @RequestParam("weight") Float weight, Model model) {
        model.addAttribute("pets", Pet.findPetsByNameAndWeight(name, weight).getResultList());
        return "pets/list";
    }
    
    @RequestMapping(params = { "find=ByOwner", "form" }, method = RequestMethod.GET)
    public String PetController.findPetsByOwnerForm(Model model) {
        model.addAttribute("owners", Owner.findAllOwners());
        return "pets/findPetsByOwner";
    }
    
    @RequestMapping(params = "find=ByOwner", method = RequestMethod.GET)
    public String PetController.findPetsByOwner(@RequestParam("owner") Owner owner, Model model) {
        model.addAttribute("pets", Pet.findPetsByOwner(owner).getResultList());
        return "pets/list";
    }
    
    @RequestMapping(params = { "find=BySendRemindersAndWeightLessThan", "form" }, method = RequestMethod.GET)
    public String PetController.findPetsBySendRemindersAndWeightLessThanForm(Model model) {
        return "pets/findPetsBySendRemindersAndWeightLessThan";
    }
    
    @RequestMapping(params = "find=BySendRemindersAndWeightLessThan", method = RequestMethod.GET)
    public String PetController.findPetsBySendRemindersAndWeightLessThan(@RequestParam(value = "sendReminders", required = false) boolean sendReminders, @RequestParam("weight") Float weight, Model model) {
        model.addAttribute("pets", Pet.findPetsBySendRemindersAndWeightLessThan(sendReminders, weight).getResultList());
        return "pets/list";
    }
    
    @RequestMapping(params = { "find=ByTypeAndNameLike", "form" }, method = RequestMethod.GET)
    public String PetController.findPetsByTypeAndNameLikeForm(Model model) {
        model.addAttribute("pettypes", java.util.Arrays.asList(PetType.class.getEnumConstants()));
        return "pets/findPetsByTypeAndNameLike";
    }
    
    @RequestMapping(params = "find=ByTypeAndNameLike", method = RequestMethod.GET)
    public String PetController.findPetsByTypeAndNameLike(@RequestParam("type") PetType type, @RequestParam("name") String name, Model model) {
        model.addAttribute("pets", Pet.findPetsByTypeAndNameLike(type, name).getResultList());
        return "pets/list";
    }
    
    @ModelAttribute("owners")
    public Collection<Owner> PetController.populateOwners() {
        return Owner.findAllOwners();
    }
    
    @ModelAttribute("pettypes")
    public Collection<PetType> PetController.populatePetTypes() {
        return Arrays.asList(PetType.class.getEnumConstants());
    }
    
    Converter<Owner, String> PetController.getOwnerConverter() {
        return new Converter<Owner, String>() {
            public String convert(Owner owner) {
                return new StringBuilder().append(owner.getFirstName()).append(" ").append(owner.getLastName()).append(" ").append(owner.getAddress()).toString();
            }
        };
    }
    
    Converter<Pet, String> PetController.getPetConverter() {
        return new Converter<Pet, String>() {
            public String convert(Pet pet) {
                return new StringBuilder().append(pet.getName()).append(" ").append(pet.getWeight()).toString();
            }
        };
    }
    
    @InitBinder
    void PetController.registerConverters(WebDataBinder binder) {
        if (binder.getConversionService() instanceof GenericConversionService) {
            GenericConversionService conversionService = (GenericConversionService) binder.getConversionService();
            conversionService.addConverter(getOwnerConverter());
            conversionService.addConverter(getPetConverter());
        }
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public String PetController.showJson(@PathVariable("id") Long id) {
        return Pet.findPet(id).toJson();
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> PetController.createFromJson(@RequestBody String json) {
        Pet.fromJsonToPet(json).persist();
        return new ResponseEntity<String>("Pet created", HttpStatus.CREATED);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public String PetController.listJson() {
        return Pet.toJsonArray(Pet.findAllPets());
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> PetController.createFromJsonArray(@RequestBody String json) {
        for (Pet pet: Pet.fromJsonArrayToPets(json)) {
            pet.persist();
        }
        return new ResponseEntity<String>("Pet created", HttpStatus.CREATED);
    }
    
}
