package com.luv2code.jobportal.services;

import com.luv2code.jobportal.entity.JobSeekerProfile;
import com.luv2code.jobportal.entity.RecuiterProfile;
import com.luv2code.jobportal.entity.Users;
import com.luv2code.jobportal.repository.JobSeekerProfileRepository;
import com.luv2code.jobportal.repository.RecuiterProfileRepository;
import com.luv2code.jobportal.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    private final UsersRepository usersRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final RecuiterProfileRepository recuiterProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(
            UsersRepository usersRepository,
            JobSeekerProfileRepository jobSeekerProfileRepository,
            RecuiterProfileRepository recuiterProfileRepository,
            PasswordEncoder passwordEncoder) {

        this.passwordEncoder = passwordEncoder;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.recuiterProfileRepository = recuiterProfileRepository;
        this.usersRepository = usersRepository;
    }

    public Users addNew(Users users) {
        users.setActive(true);
        users.setRegistrationDate(new Date(System.currentTimeMillis()));
        users.setPassword(passwordEncoder.encode(users.getPassword()));

        Users savedUser = usersRepository.save(users);
        int userTypeId = savedUser.getUsersType().getUsersTypeId();

        if (userTypeId == 1) {
            recuiterProfileRepository.save(new RecuiterProfile(users));
        } else {
            jobSeekerProfileRepository.save(new JobSeekerProfile(savedUser));
        }
        return savedUser;
    }

    public Object getCurrentUserProfile() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {

            String username = authentication.getName();

            Users users = usersRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException(
                            "User not found with email: " + username));

            int userTypeId = users.getUsersType().getUsersTypeId();

            if (authentication.getAuthorities()
                    .contains(new SimpleGrantedAuthority("ROLE_RECRUITER"))) {

                RecuiterProfile recuiterProfile =
                        recuiterProfileRepository
                                .findById(userTypeId)
                                .orElse(new RecuiterProfile());

                return recuiterProfile;
            } else {

                JobSeekerProfile jobSeekerProfile =
                        jobSeekerProfileRepository
                                .findById(userTypeId)
                                .orElse(new JobSeekerProfile());

                return jobSeekerProfile;
            }
        }
        return null;
    }

    public Users getCurrentUser() {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String userName=authentication.getName();
            Users users=usersRepository.findByEmail(userName).orElseThrow(()->new RuntimeException("Could not found user"));
            return users;
        }
        return null;
    }

    public Users findByEmail(String currentUsername) {
        return usersRepository.findByEmail(currentUsername).orElseThrow(()-> new UsernameNotFoundException("User not found with email: " + currentUsername));

    }
}
