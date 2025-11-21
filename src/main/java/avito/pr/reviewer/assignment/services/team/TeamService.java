package avito.pr.reviewer.assignment.services.team;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public TeamAddResponseDto add(TeamAddRequestDto requestDto) {
        TeamWithMembers teamWithMembers = TeamMapper.fromTeamAddResponseDtoToTeam(requestDto);
        TeamWithMembers savedTeamWithMembers = teamRepository.saveTeamWithUsers(teamWithMembers);
        return TeamMapper.fromTeamToTeamAddResponseDto(savedTeamWithMembers);
    }

    @Transactional
    public TeamGetResponseDto get(String teamName) {
        TeamWithMembers teamWithMembers = teamRepository.findTeamWithMembers(teamName);
        return TeamMapper.fromTeamToTeamGetResponseDto(teamWithMembers);
    }
}
