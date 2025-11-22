package avito.pr.reviewer.assignment.services.team;

import java.util.ArrayList;
import java.util.List;

import avito.pr.reviewer.assignment.bd.entities.team.Member;
import avito.pr.reviewer.assignment.bd.entities.team.TeamWithMembers;
import avito.pr.reviewer.assignment.dto.requests.team.MemberInTeamCreateRequestDto;
import avito.pr.reviewer.assignment.dto.requests.team.TeamCreateRequestDto;
import avito.pr.reviewer.assignment.dto.responses.team.create.MemberInTeamCreateResponseDto;
import avito.pr.reviewer.assignment.dto.responses.team.create.TeamCreateResponseDto;
import avito.pr.reviewer.assignment.dto.responses.team.get.MemberInTeamGetResponseDto;
import avito.pr.reviewer.assignment.dto.responses.team.get.TeamGetResponseDto;

public class TeamMapper {
    private TeamMapper() {}

    public static TeamCreateResponseDto fromTeamToTeamCreateResponseDto(TeamWithMembers teamWithMembers) {
        List<MemberInTeamCreateResponseDto> members = new ArrayList<>();
        for (Member member: teamWithMembers.getMembers()) {
            members.add(
                MemberInTeamCreateResponseDto.builder()
                    .userId(member.getUserId())
                    .username(member.getUsername())
                    .isActive(member.getIsActive())
                    .build()
            );
        }
        return TeamCreateResponseDto.builder()
            .teamName(teamWithMembers.getTeamName())
            .members(members)
            .build();
    }

    public static TeamWithMembers fromTeamCreateResponseDtoToTeam(TeamCreateRequestDto teamAddRequestDto) {
        List<Member> members = new ArrayList<>();
        for (MemberInTeamCreateRequestDto member: teamAddRequestDto.getMembers()) {
            members.add(
                Member.builder()
                    .userId(member.getUserId())
                    .username(member.getUsername())
                    .isActive(member.getIsActive())
                    .build()
            );
        }
        return TeamWithMembers.builder()
            .teamName(teamAddRequestDto.getTeamName())
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
