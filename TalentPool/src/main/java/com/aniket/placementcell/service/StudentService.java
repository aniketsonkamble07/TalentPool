package com.aniket.placementcell.service;

import com.aniket.placementcell.dto.AppliedDTO;
import com.aniket.placementcell.dto.JobPostingResponseDTO;
import com.aniket.placementcell.dto.StudentResponseDTO;
import com.aniket.placementcell.entity.JobPosting;
import com.aniket.placementcell.entity.Student;
import com.aniket.placementcell.enums.JobStatus;
import com.aniket.placementcell.exceptions.JobIdNotFoundException;
import com.aniket.placementcell.repository.JobPostRepository;
import com.aniket.placementcell.repository.StudentRepository;
import com.aniket.placementcell.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository student;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobPostRepository jobPostRepository;

    public StudentResponseDTO sendProfile(String name) {
        StudentResponseDTO dto = new StudentResponseDTO();

        System.out.println("Fetching profile for username: " + name);

        if (userRepository.existsByUsername(name)) {
            Student s = student.findByEmail(name)
                    .orElseThrow(() -> new UsernameNotFoundException(name + " Not found!!"));

            System.out.println("Student entity found: " + s);

            dto.setName(s.getName());
            dto.setCrnNumber(s.getCrnNumber());
            dto.setEmail(s.getEmail());
            dto.setBranch(s.getBranch());
            dto.setStudentYear(s.getYear());
            dto.setActiveBacklog(s.getActiveBacklog());
            dto.setMobileNumber(s.getMobileNumber());
            dto.setGender(s.getGender());

            dto.setCgpa(s.getCgpa());
            dto.setMark10th(s.getMark10th());
            dto.setMark12th(s.getMark12th());
            dto.setDiplomaMark(s.getDiplomaMarks());

            dto.setPlacementStatus(s.getPlacementStatus());
            dto.setCompanyName(s.getCompanyName());

            dto.setAppliedDTOList(
                    s.getAppliedList().stream()
                            .map(applied -> {
                                JobPosting job = applied.getJobPosting();
                                AppliedDTO appliedDTO = new AppliedDTO();
                                appliedDTO.setJobId(job.getId());
                                appliedDTO.setTitle(job.getTitle());
                                appliedDTO.setCompanyName(job.getCompanyName());
                                appliedDTO.setLocation(job.getLocation());
                                appliedDTO.setStatus(job.getStatus());
                                appliedDTO.setAppliedDate(applied.getAppliedDate());
                                appliedDTO.setApplicationStatus(applied.getStatus().toString());

                                System.out.println("Mapped applied job: " + appliedDTO);

                                return appliedDTO;
                            })
                            .toList()
            );

            System.out.println("Final StudentResponseDTO: " + dto);
        } else {
            System.out.println("Username not found in user repository: " + name);
        }

        return dto;
    }


    public JobPostingResponseDTO getJobPosting(String jobId) {
        try {
            // Convert String jobId to Long (assuming jobId is the entity ID)
            Long id = Long.parseLong(jobId);
            JobPosting job = jobPostRepository.findById(id)
                    .orElseThrow(() -> new JobIdNotFoundException("Job with ID " + jobId + " not found!!"));

            return mapToJobPostingResponseDTO(job);

        } catch (NumberFormatException e) {
            throw new JobIdNotFoundException("Invalid job ID format: " + jobId);
        }
    }

    public List<JobPostingResponseDTO> getAllJobPosting() {
        List<JobPosting> jobPostings = jobPostRepository.findByStatusOrderByCreatedAtDesc(JobStatus.PUBLISHED);

        return jobPostings.stream()
                .map(this::mapToJobPostingResponseDTO)
                .toList();
    }



    private JobPostingResponseDTO mapToJobPostingResponseDTO(JobPosting job) {
        JobPostingResponseDTO dto = new JobPostingResponseDTO();

        // Use job.getId() for jobId since that's what you're using in getAllJobPosting
        dto.setJobId(job.getId().toString());
        dto.setTitle(job.getTitle());
        dto.setDescription(job.getDescription());
        dto.setJobType(job.getJobType());
        dto.setEmploymentType(job.getEmploymentType());
        dto.setLocation(job.getLocation());
        dto.setMinSalary(job.getMinSalary());
        dto.setMaxSalary(job.getMaxSalary());
        dto.setSalaryType(job.getSalaryType());
        dto.setRequiredCGPA(job.getRequiredCGPA());
        dto.setRequiredSkills(job.getRequiredSkills());
        dto.setRequiredBranches(job.getRequiredBranches());
        dto.setApplicationDeadline(job.getApplicationDeadline());
        dto.setDriveDate(job.getDriveDate());
        dto.setDriveTime(job.getDriveTime());
        dto.setDriveVenue(job.getDriveVenue());
        dto.setNumberOfVacancies(job.getNumberOfVacancies());
        dto.setStatus(job.getStatus());
        dto.setCompanyName(job.getCompanyName());

        // Map placement officer info safely
        if (job.getPostedBy() != null) {
            dto.setPostedByName(job.getPostedBy().getName());
            dto.setPostedByEmail(job.getPostedBy().getEmail());
        }

        dto.setViewsCount(job.getViewsCount());
        dto.setApplicationsCount(job.getApplicationsCount());
        dto.setCreatedAt(job.getCreatedAt());
        dto.setUpdatedAt(job.getUpdatedAt());
        dto.setPublishedAt(job.getPublishedAt());

        return dto;
    }
}
