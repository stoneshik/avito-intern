package avito.pr.reviewer.assignment.services.team;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import avito.pr.reviewer.assignment.bd.entities.team.TeamWithMembers;
import avito.pr.reviewer.assignment.dto.requests.team.TeamCreateRequestDto;
import avito.pr.reviewer.assignment.dto.responses.team.create.TeamCreateResponseDto;
import avito.pr.reviewer.assignment.dto.responses.team.get.TeamGetResponseDto;
import avito.pr.reviewer.assignment.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;

    @Transactional
    public TeamCreateResponseDto create(TeamCreateRequestDto requestDto) {
        TeamWithMembers teamWithMembers = TeamMapper.fromTeamCreateResponseDtoToTeam(requestDto);
        TeamWithMembers savedTeamWithMembers = teamRepository.saveTeamWithUsers(teamWithMembers);
        return TeamMapper.fromTeamToTeamCreateResponseDto(savedTeamWithMembers);
    }

    @Transactional
    public TeamGetResponseDto get(String teamName) {
        TeamWithMembers teamWithMembers = teamRepository.findTeamWithMembers(teamName);
        return TeamMapper.fromTeamToTeamGetResponseDto(teamWithMembers);
    }
}
