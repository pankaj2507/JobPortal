package com.luv2code.jobportal.controller;

import com.luv2code.jobportal.entity.*;
import com.luv2code.jobportal.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class JobSeekerApplyController {
    private final JobPostActivityService jobPostActivityService;;
    private final UserService userService;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobSeekerSaveService;
    private final RecruiterProfileService recruiterProfileService;
    private final JobSeekerProfileServices jobSeekerProfileServices;

    @Autowired
    public JobSeekerApplyController(JobPostActivityService jobPostActivityService, UserService userService,
                                    JobSeekerApplyService jobSeekerApplyService,JobSeekerSaveService jobSeekerSaveService,
                                    RecruiterProfileService recruiterProfileService,JobSeekerProfileServices jobSeekerProfileServices) {
        this.jobPostActivityService = jobPostActivityService;
        this.userService = userService;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jobSeekerSaveService = jobSeekerSaveService;
        this.recruiterProfileService = recruiterProfileService;
        this.jobSeekerProfileServices = jobSeekerProfileServices;
    }
    @GetMapping("job-details-apply/{id}")
    public String display(@PathVariable("id") int id,Model model){
        JobPostActivity jobDetails=jobPostActivityService.getOne(id);
        List<JobSeekerApply> jobSeekerApplyList=jobSeekerApplyService.getJobCandidate(jobDetails);
        List<JobSeekerSave> jobSeekerSaveList=jobSeekerSaveService.getJobCandidates(jobDetails);
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority(("Requiter")))){
                RecuiterProfile user=recruiterProfileService.getCurrentRecruiterProfile();
                if(user!=null){
                    model.addAttribute("applyList",jobSeekerApplyList);
                }
            }
            else{
               JobSeekerProfile user= jobSeekerProfileServices.getCurrentSeekerProfile();
               if(user!=null){
                   boolean exists=false;
                   boolean saved=false;;
                   for(JobSeekerApply jobSeekerApply:jobSeekerApplyList){
                       if(jobSeekerApply.getUserId().getUserAccountId()==user.getUserAccountId()){
                            exists=true;
                            break;
                       }
                   }
                   for(JobSeekerSave jobSeekerSave:jobSeekerSaveList){
                       if(jobSeekerSave.getUserId().getUserAccountId()==user.getUserAccountId()){
                           saved=true;
                           break;
                       }
                   }
                   model.addAttribute("alreadyApplied",exists);
                   model.addAttribute("alreadySaved",saved);
               }
            }
        }
        JobSeekerApply jobSeekerApply=new JobSeekerApply();
        model.addAttribute("applyJob",jobSeekerApply);

        model.addAttribute("jobDetails",jobDetails);
        model.addAttribute("user",userService.getCurrentUserProfile());
        return "job-details";
    }
    @PostMapping("job-detail/apply/{id}")
    public String apply(@PathVariable("id") int id,JobSeekerApply jobSeekerApply){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
           String currentUsername=authentication.getName();
              Users users=userService.findByEmail(currentUsername);
            Optional<JobSeekerProfile> jobSeekerProfile=jobSeekerProfileServices.getOne(users.getUserId());
            JobPostActivity jobPostActivity=jobPostActivityService.getOne(id);
            if(jobSeekerProfile.isPresent() && jobPostActivity!=null){
                jobSeekerApply.setUserId(jobSeekerProfile.get());
                jobSeekerApply.setJob(jobPostActivity);
                jobSeekerApply.setApplyDate(new Date());
            }
            else{
                throw  new RuntimeException("Could not found user or job");
            }
            jobSeekerApplyService.addNew(jobSeekerApply);
        }
        return "redirect:/dashboard/";
    }

}
