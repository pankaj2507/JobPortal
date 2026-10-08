package com.luv2code.jobportal.entity;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
public class JobPostActivity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "postedById", referencedColumnName = "user_id")
    private Users postedBy;

    @ManyToOne
    @JoinColumn(name = "jobLocationId", referencedColumnName = "Id")
    private JobLocation jbLocation;

    @ManyToOne
    @JoinColumn(name = "jobCompanyId", referencedColumnName = "Id")
    private JobCompany jobCompanyId;

    @Transient
    private boolean isActive;

    @Transient
    private Boolean isSaved;

    @Length(max = 10000)
    private String descriptionOfJob;

    private String jobType;
    private String salary;
    private String remote;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date postedDate;

    private String jobTitle;

    public JobPostActivity() {}

    public JobPostActivity(
            Integer id,
            String jobTitle,
            Date postedDate,
            String remote,
            String salary,
            String jobType,
            Boolean isSaved,
            String descriptionOfJob,
            boolean isActive,
            JobCompany jobCompanyId,
            JobLocation jbLocation,
            Users postedBy
    ) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.postedDate = postedDate;
        this.remote = remote;
        this.salary = salary;
        this.jobType = jobType;
        this.isSaved = isSaved;
        this.descriptionOfJob = descriptionOfJob;
        this.isActive = isActive;
        this.jobCompanyId = jobCompanyId;
        this.jbLocation = jbLocation;
        this.postedBy = postedBy;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Users getPostedBy() { return postedBy; }
    public void setPostedBy(Users postedBy) { this.postedBy = postedBy; }

    public JobLocation getJbLocation() { return jbLocation; }
    public void setJbLocation(JobLocation jbLocation) { this.jbLocation = jbLocation; }

    public JobCompany getJobCompanyId() { return jobCompanyId; }
    public void setJobCompanyId(JobCompany jobCompanyId) { this.jobCompanyId = jobCompanyId; }

    public boolean getIsActive() { return isActive; }
    public void setIsActive(boolean active) { isActive = active; }

    public Boolean getIsSaved() { return isSaved; }
    public void setIsSaved(Boolean saved) { isSaved = saved; }

    public String getDescriptionOfJob() { return descriptionOfJob; }
    public void setDescriptionOfJob(String descriptionOfJob) { this.descriptionOfJob = descriptionOfJob; }

    public String getJobType() { return jobType; }
    public void setJobType(String jobType) { this.jobType = jobType; }

    public String getSalary() { return salary; }
    public void setSalary(String salary) { this.salary = salary; }

    public String getRemote() { return remote; }
    public void setRemote(String remote) { this.remote = remote; }

    public Date getPostedDate() { return postedDate; }
    public void setPostedDate(Date postedDate) { this.postedDate = postedDate; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
}
