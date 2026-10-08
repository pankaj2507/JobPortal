package com.luv2code.jobportal.controller;

import com.luv2code.jobportal.entity.JobPostActivity;
import com.luv2code.jobportal.entity.JobSeekerProfile;
import com.luv2code.jobportal.entity.JobSeekerSave;
import com.luv2code.jobportal.entity.Users;
import com.luv2code.jobportal.services.JobPostActivityService;
import com.luv2code.jobportal.services.JobSeekerProfileServices;
import com.luv2code.jobportal.services.JobSeekerSaveService;
import com.luv2code.jobportal.services.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JobSeekerSaveController {
    private final UserService userService;
    private final JobSeekerSaveService jobSeekerSaveService;
    private final JobPostActivityService jobPostActivityService;
    private final JobSeekerProfileServices jobSeekerProfileServices;;

    public JobSeekerSaveController(UserService userService, JobSeekerSaveService jobSeekerSaveService, JobPostActivityService jobPostActivityService, JobSeekerProfileServices jobSeekerProfileServices) {
        this.userService = userService;
        this.jobSeekerSaveService = jobSeekerSaveService;
        this.jobPostActivityService = jobPostActivityService;
        this.jobSeekerProfileServices = jobSeekerProfileServices;
    }

    @PostMapping("job-details/save/{id}")
    public String save(@PathVariable("id")int id, JobSeekerSave jobSeekerSave){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername=authentication.getName();
            Users users =userService.findByEmail(currentUsername);
            Optional<JobSeekerProfile> jobSeekerProfile=jobSeekerProfileServices.getOne(users.getUserId());
            JobPostActivity jobPostActivity=jobPostActivityService.getOne(users.getUserId());
            if(jobSeekerProfile.isPresent() || jobPostActivity!=null){
                jobSeekerSave.setJob(jobPostActivity);
                jobSeekerSave.setUserId(jobSeekerProfile.get());
            }
            else{
                throw new RuntimeException("User not found");
            }
            jobSeekerSaveService.addNew(jobSeekerSave);
        }
        return "redirect:/dashboard";
    }

    @GetMapping("saved-jobs/")
    public String savedJob(Model model){
        List<JobPostActivity> jobPost=new ArrayList<>();
        Object curUserProfile=userService.getCurrentUserProfile();
        List<JobSeekerSave> jobSeekerSaves=jobSeekerSaveService.getCandidatesJob((JobSeekerProfile) curUserProfile);
        for(JobSeekerSave jobSeekerSave:jobSeekerSaves){
            jobPost.add(jobSeekerSave.getJob());
        }
        model.addAttribute("jobPost",jobPost);
        model.addAttribute("user",curUserProfile);
        return "saved-jobs";
    }
}
