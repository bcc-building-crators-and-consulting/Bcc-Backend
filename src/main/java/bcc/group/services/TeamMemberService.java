package bcc.group.services;


import bcc.group.dto.team.CreateTeamMemberRequest;
import bcc.group.dto.team.TeamMemberResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TeamMemberService {
    TeamMemberResponse create(CreateTeamMemberRequest request, MultipartFile imageFile); // added file param
    List<TeamMemberResponse> getAll();
    List<TeamMemberResponse> getActiveMembers();
    TeamMemberResponse getById(Long id);
    TeamMemberResponse update(Long id, CreateTeamMemberRequest request, MultipartFile imageFile); // added
    TeamMemberResponse update(Long id, CreateTeamMemberRequest request);
    void delete(Long id);
    // Service interface
    TeamMemberResponse create(CreateTeamMemberRequest request);  // no file
    TeamMemberResponse updateImage(Long id, MultipartFile imageFile);  // upload image later
}
