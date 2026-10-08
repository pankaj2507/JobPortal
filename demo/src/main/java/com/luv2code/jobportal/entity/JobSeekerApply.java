package com.luv2code.jobportal.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table
public class JobSeekerApply implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="userId",referencedColumnName = "user_account_id")
    private JobSeekerProfile userId;
    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="job",referencedColumnName = "jobPostId")
    private JobPostActivity job;
    @DateTimeFormat(pattern="dd-MM-yyyy")
    private Date applyDate;
    private String coverLetter;

    public JobSeekerApply() {
    }

    public JobSeekerApply(Integer id, String coverLetter, Date applyDate, JobPostActivity job, JobSeekerProfile userId) {
        this.id = id;
        this.coverLetter = coverLetter;
        this.applyDate = applyDate;
        this.job = job;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public JobPostActivity getJob() {
        return job;
    }

    public void setJob(JobPostActivity job) {
        this.job = job;
    }

    public JobSeekerProfile getUserId() {
        return userId;
    }

    public void setUserId(JobSeekerProfile userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "JobSeekerApply{" +
                "id=" + id +
                ", userId=" + userId +
                ", job=" + job +
                ", applyDate=" + applyDate +
                ", coverLetter='" + coverLetter + '\'' +
                '}';
    }
}
