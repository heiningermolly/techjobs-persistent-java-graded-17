package org.launchcode.techjobs.persistent.controllers;

import jakarta.validation.Valid;
import org.launchcode.techjobs.persistent.models.Employer;
import org.launchcode.techjobs.persistent.models.Job;
import org.launchcode.techjobs.persistent.models.Skill;
import org.launchcode.techjobs.persistent.models.data.EmployerRepository;
import org.launchcode.techjobs.persistent.models.data.JobRepository;
import org.launchcode.techjobs.persistent.models.data.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by LaunchCode
 */
@Controller
public class HomeController {

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private JobRepository jobRepository;

    @RequestMapping("/")
    public String index(Model model) {

        model.addAttribute("title", "MyJobs");
        model.addAttribute("jobs", jobRepository.findAll());


        return "index";
    }

    @GetMapping("add")
    public String displayAddJobForm(Model model) {
	model.addAttribute("title", "Add Job");
        model.addAttribute(new Job());
        model.addAttribute("employers", employerRepository.findAll());
        model.addAttribute("skills", skillRepository.findAll());
        return "add";
    }

    @PostMapping("add")
    public String processAddJobForm(@ModelAttribute @Valid Job newJob,
                                       Errors errors, Model model, @RequestParam int employerId, @RequestParam List<Integer> skills) {
        if (errors.hasErrors()) {
	    model.addAttribute("title", "Add Job");
            return "add";
        }
        Optional<Employer> result = employerRepository.findById(employerId);
        Employer employer = result.get();
        newJob.setEmployer(employer);

        List<Skill> skillObjs = (List<Skill>) skillRepository.findAllById(skills);
        newJob.setSkills(skillObjs);

        jobRepository.save(newJob);
        return "redirect:";
    }
//
//    @Test
//    public void testProcessAddJobFormHandlesSkillsProperly (
//            @Mocked SkillRepository skillRepository,
//            @Mocked EmployerRepository employerRepository,
//            @Mocked JobRepository jobRepository,
//            @Mocked Job job,
//            @Mocked Errors errors)
//            throws ClassNotFoundException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
//        Class homeControllerClass = getClassByName("controllers.HomeController");
//        Method processAddJobFormMethod = homeControllerClass.getMethod("processAddJobForm", Job.class, Errors.class, Model.class, int.class, List.class);
//
//        new Expectations() {{
//            skillRepository.findAllById((Iterable<Integer>) any);
//            job.setSkills((List<Skill>) any);
//        }};
//
//        Model model = new ExtendedModelMap();
//        HomeController homeController = new HomeController();
//
//        Field skillRepositoryField = homeControllerClass.getDeclaredField("skillRepository");
//        skillRepositoryField.setAccessible(true);
//        skillRepositoryField.set(homeController, skillRepository);
//
//        Field employerRepositoryField = homeControllerClass.getDeclaredField("employerRepository");
//        employerRepositoryField.setAccessible(true);
//        employerRepositoryField.set(homeController, employerRepository);
//
//        Field jobRepositoryField = homeControllerClass.getDeclaredField("jobRepository");
//        jobRepositoryField.setAccessible(true);
//        jobRepositoryField.set(homeController, jobRepository);
//
//        processAddJobFormMethod.invoke(homeController, job, errors, model, 0, new ArrayList<Skill>());
//    }


    @GetMapping("view/{jobId}")
    public String displayViewJob(Model model, @PathVariable int jobId) {

        Optional optJob = jobRepository.findById(jobId);
        if (optJob.isPresent()) {
            Job job = (Job) optJob.get();
            model.addAttribute("job", job);
            return "view";
        } else {
            return "redirect:";
        }
    }

}
