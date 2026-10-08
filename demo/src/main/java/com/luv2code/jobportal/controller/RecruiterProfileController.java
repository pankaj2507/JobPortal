package com.luv2code.jobportal.controller;

import com.luv2code.jobportal.entity.RecuiterProfile;
import com.luv2code.jobportal.entity.Users;
import com.luv2code.jobportal.repository.RecuiterProfileRepository;
import com.luv2code.jobportal.repository.UsersRepository;
import com.luv2code.jobportal.services.RecruiterProfileService;
import com.luv2code.jobportal.util.FileUploadUtil;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {

    private final UsersRepository usersRepository;
    private final RecruiterProfileService recruiterProfileService;


    public RecruiterProfileController(UsersRepository usersRepository,RecruiterProfileService recruiterProfileService) {
        this.usersRepository = usersRepository;
        this.recruiterProfileService=recruiterProfileService;
    }

    @GetMapping("/")
    public String recuiterProfile(Model model){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUserName=authentication.getName();
            Users users=usersRepository.findByEmail(currentUserName).orElseThrow(()->new UsernameNotFoundException("Could not" + "found user"));
            Optional<RecuiterProfile> recuiterProfile=recruiterProfileService.getOne(users.getUserId());
            if(recuiterProfile.isEmpty()){
                model.addAttribute("Profile"+recuiterProfile.get());
            }
        }
        return "recuiterProfile";
    }
    @PostMapping("/addNew")
    public String addNew(RecuiterProfile recuiterProfile, @RequestParam("image") MultipartFile file,Model model){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUserName=authentication.getName();
            Users users=usersRepository.findByEmail(currentUserName).orElseThrow(()->new UsernameNotFoundException("Could not" + "found user"));
            recuiterProfile.setUserId(users);
            recuiterProfile.setUserAccountId(users.getUserId());
        }
        model.addAttribute("profile",recuiterProfile);
        String fileName="";
        if(!file.getOriginalFilename().equals("")){
            fileName= StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            recuiterProfile.setProfilePhoto(fileName);
        }
        RecuiterProfile savedUser=recruiterProfileService.addNew(recuiterProfile);
        String uploadDir="photo/recruiter/"+savedUser.getUserAccountId();
        try{
            FileUploadUtil.saveFile(uploadDir,fileName,file);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/dashboard/";
    }
}
