package avito.pr.reviewer.assignment.services.team;

import java.util.ArrayList;
import java.util.List;

import avito.pr.reviewer.assignment.bd.entities.team.Member;
import avito.pr.reviewer.assignment.bd.entities.team.TeamWithMembers;
import avito.pr.reviewer.assignment.dto.responses.team.add.MemberInTeamAddResponseDto;
import avito.pr.reviewer.assignment.dto.responses.team.add.TeamAddResponseDto;
import avito.pr.reviewer.assignment.dto.responses.team.get.MemberInTeamGetResponseDto;
import avito.pr.reviewer.assignment.dto.responses.team.get.TeamGetResponseDto;

public class TeamMapper {
    private TeamMapper() {}

    public static TeamAddResponseDto fromTeamToTeamAddResponseDto(TeamWithMembers teamWithMembers) {
        List<MemberInTeamAddResponseDto> members = new ArrayList<>();
        for (Member member: teamWithMembers.getMembers()) {
            members.add(
                MemberInTeamAddResponseDto.builder()
                    .userId(member.getUserId())
                    .username(member.getUsername())
                    .isActive(member.getIsActive())
                    .build()
            );
        }
        return TeamAddResponseDto.builder()
            .teamName(teamWithMembers.getTeamName())
            .members(members)
            .build();
    }

    public static TeamGetResponseDto fromTeamToTeamGetResponseDto(TeamWithMembers teamWithMembers) {
        List<MemberInTeamGetResponseDto> members = new ArrayList<>();
        for (Member member: teamWithMembers.getMembers()) {
            members.add(
                MemberInTeamGetResponseDto.builder()
                    .userId(member.getUserId())
                    .username(member.getUsername())
                    .isActive(member.getIsActive())
                    .build()
            );
        }
        return TeamGetResponseDto.builder()
            .teamName(teamWithMembers.getTeamName())
            .members(members)
            .build();
    }
}
