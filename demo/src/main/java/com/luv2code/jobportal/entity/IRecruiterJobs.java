package com.luv2code.jobportal.entity;

public interface IRecruiterJobs {
    Long getTotalCandidates();
    int getJobPostId();
    String getJobTitle();
    int getJobLocationId();
    int getJobCompanyId();
    String getName();
    String getCity();
    String getState();
    String getCountry();
}
