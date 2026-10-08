package com.luv2code.jobportal.services;

import com.luv2code.jobportal.entity.RecuiterProfile;
import com.luv2code.jobportal.entity.Users;
import com.luv2code.jobportal.repository.RecuiterProfileRepository;
import com.luv2code.jobportal.repository.UsersRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecruiterProfileService {
    private final RecuiterProfileRepository recruiterProfileRepository;
    private final UsersRepository usersRepository;

    public RecruiterProfileService(RecuiterProfileRepository recruiterProfileRepository,UsersRepository usersRepository) {
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.usersRepository=usersRepository;
    }

    public Optional<RecuiterProfile>getOne(Integer id){
        return recruiterProfileRepository.findById(id);
    }
    public RecuiterProfile addNew(RecuiterProfile recuiterProfile){
        return recruiterProfileRepository.save(recuiterProfile);
    }

    public RecuiterProfile getCurrentRecruiterProfile() {
        Authentication authentication =SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof AnonymousAuthenticationToken){
            String currentUsername=authentication.getName();
            Users users=usersRepository.findByEmail(currentUsername).orElseThrow(()-> new UsernameNotFoundException("User not found"));
            Optional<RecuiterProfile> recuiterProfile=getOne(users.getUserId());
            return recuiterProfile.orElse((null));
        }
        else return null;
    }
}
