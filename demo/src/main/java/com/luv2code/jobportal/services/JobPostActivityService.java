package com.luv2code.jobportal.services;

import com.luv2code.jobportal.entity.*;
import com.luv2code.jobportal.repository.JobPostActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class JobPostActivityService {
    private JobPostActivityRepository jobPostActivityRepository;;
    @Autowired
    public JobPostActivityService(JobPostActivityRepository jobPostActivityRepository) {
        this.jobPostActivityRepository = jobPostActivityRepository;
    }

    public JobPostActivity addNew(JobPostActivity jobPostActivity){
        return jobPostActivityRepository.save(jobPostActivity);
    }

    public List<RecruiterJobDto> getJobsByRecruiterId(int recruiter){
        List<IRecruiterJobs> recruiterJobsDtos =jobPostActivityRepository.getRecruiterJobs(recruiter);
        List<RecruiterJobDto> recruiterJobDtosList=new ArrayList<>();
        for(IRecruiterJobs rec:recruiterJobsDtos){
            JobLocation loc=new JobLocation(rec.getJobCompanyId(),rec.getCity(),rec.getState(),rec.getCountry());
            JobCompany comp=new JobCompany(rec.getJobCompanyId(),"",rec.getName());
            recruiterJobDtosList.add(new RecruiterJobDto(rec.getTotalCandidates(),comp,rec.getJobTitle(),loc, rec.getJobPostId()));


        }
        return recruiterJobDtosList;


    }

    public JobPostActivity getOne(int id) {
        return jobPostActivityRepository.findById(id).orElseThrow(()->new RuntimeException("Job Post Activity not found"));
    }

    public List<JobPostActivity> getAll() {
        return jobPostActivityRepository.findAll();
    }

    public List<JobPostActivity> search(String job, String location, List<String> type, List<String> remote, LocalDate searchDate) {
        return Objects.isNull(searchDate)?jobPostActivityRepository.searchWithoutDate(job,location,remote,type):jobPostActivityRepository.search(job,location,remote,type,searchDate);
    }
}
