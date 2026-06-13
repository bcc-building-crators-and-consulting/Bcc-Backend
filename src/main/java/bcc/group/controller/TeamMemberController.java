package bcc.group.controller;


import bcc.group.dto.team.CreateTeamMemberRequest;
import bcc.group.dto.team.TeamMemberResponse;
import bcc.group.services.TeamMemberService;
import bcc.group.util.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:5173",
        "http://localhost:5174",
        "http://localhost:5175",
        "http://127.0.0.1:5173",
        "http://127.0.0.1:5174",
        "http://127.0.0.1:5175"
})
@RestController
@RequestMapping("/api/team-members")
@RequiredArgsConstructor
@Tag(name = "TeamMember", description = "TeamMember management APIs")
public class TeamMemberController {

    private final TeamMemberService service;

    // Create with image
    // Create a member with JSON only (no image)
    @PostMapping
    public ResponseEntity<ApiResponse<TeamMemberResponse>> create(@Valid @RequestBody CreateTeamMemberRequest request) {
        TeamMemberResponse created = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Team member created", created));
    }

    // Upload/update profile image for an existing member
    @PutMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<TeamMemberResponse>> uploadImage(
            @PathVariable Long id,
            @RequestPart("image") MultipartFile imageFile) {
        TeamMemberResponse updated = service.updateImage(id, imageFile);
        return ResponseEntity.ok(new ApiResponse<>(true, "Image uploaded", updated));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TeamMemberResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateTeamMemberRequest request) {
        TeamMemberResponse updated = service.update(id, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Team member updated", updated));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TeamMemberResponse>>> getAll() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Team members fetched", service.getAll()));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<TeamMemberResponse>>> getActive() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Active team members", service.getActiveMembers()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeamMemberResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Team member found", service.getById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Team member deleted", null));
    }
}
