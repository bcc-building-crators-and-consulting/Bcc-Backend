package bcc.group.repository;

import bcc.group.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    List<TeamMember> findByIsActiveTrueOrderByDisplayOrderAsc();
    List<TeamMember> findByDepartment(String department);
}
