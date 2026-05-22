package bcc.group.mapper;
import bcc.group.dto.team.CreateTeamMemberRequest;
import bcc.group.dto.team.TeamMemberResponse;
import bcc.group.entity.TeamMember;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TeamMemberMapper {

    TeamMemberResponse toDto(TeamMember entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "profileImageUrl", ignore = true)   // file upload के बाद सेट होगा
    TeamMember toEntity(CreateTeamMemberRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "profileImageUrl", ignore = true)   // अपडेट के समय अलग से हैंडल करें
    void updateEntityFromRequest(CreateTeamMemberRequest request, @MappingTarget TeamMember entity);
}
