package avito.pr.reviewer.assignment.services.team;

import org.springframework.stereotype.Service;

import avito.pr.reviewer.assignment.bd.entities.team.TeamWithMembers;
import avito.pr.reviewer.assignment.dto.requests.team.TeamAddRequestDto;
import avito.pr.reviewer.assignment.dto.responses.team.add.TeamAddResponseDto;
import avito.pr.reviewer.assignment.dto.responses.team.get.TeamGetResponseDto;
import avito.pr.reviewer.assignment.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;

    public TeamAddResponseDto add(TeamAddRequestDto requestDto) {
        TeamWithMembers teamWithMembers = teamRepository.add(requestDto);
        return TeamMapper.fromTeamToTeamAddResponseDto(teamWithMembers);
    }

    public TeamGetResponseDto get(String teamName) {
        TeamWithMembers teamWithMembers = teamRepository.get(teamName);
        return TeamMapper.fromTeamToTeamGetResponseDto(teamWithMembers);
    }
}
