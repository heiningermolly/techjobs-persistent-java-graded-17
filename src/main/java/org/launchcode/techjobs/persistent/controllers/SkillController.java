package org.launchcode.techjobs.persistent.controllers;

import jakarta.validation.Valid;
import org.launchcode.techjobs.persistent.models.Employer;
import org.launchcode.techjobs.persistent.models.Skill;
import org.launchcode.techjobs.persistent.models.data.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("skills")
public class SkillController {

    @Autowired
    SkillRepository skillRepository;

    @GetMapping("add")
    public String displayAddSkillForm(Model model){
        model.addAttribute(new Skill());
        return "skills/add";
    }

    @PostMapping("add")
    public String processAddSkillForm(@ModelAttribute @Valid Skill newSkill, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return "skills/add";
        }
        skillRepository.save(newSkill);
        return "redirect:";
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "All Skills");
        model.addAttribute("skills", skillRepository.findAll());
        return "skills/index";
    }

//        public String displayAllEvents(@RequestParam(required = false) Integer categoryId,  Model model) {
//        if (categoryId == null) {
//            model.addAttribute("title", "All Events");
//            model.addAttribute("events", eventRepository.findAll());
//        } else {
//            Optional<EventCategory> result = eventCategoryRepository.findById(categoryId);
//            if (result.isEmpty()) {
//                model.addAttribute("title", "Invalid Category ID: " + categoryId);
//            } else {
//                EventCategory category = result.get();
//                model.addAttribute("title", "Events in category: " + category.getName());
//                model.addAttribute("events", category.getEvents());
//            }
//        }
//
//        return "events/index";

    @GetMapping("view/{skillId}")
    public String displayViewSkill(Model model, @PathVariable int skillId) {

        Optional <Skill> result = skillRepository.findById(skillId);
        if (result.isEmpty()) {
            return "redirect:";
        } else {
            Skill skill = result.get();
            model.addAttribute("skill", skill);
        }
            return "skills/view";
        }

    }

