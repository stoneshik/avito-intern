package avito.pr.reviewer.assignment.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import avito.pr.reviewer.assignment.dto.requests.team.TeamCreateRequestDto;
import avito.pr.reviewer.assignment.dto.responses.team.create.TeamCreateResponseDto;
import avito.pr.reviewer.assignment.dto.responses.team.get.TeamGetResponseDto;
import avito.pr.reviewer.assignment.services.team.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<TeamCreateResponseDto> create(@RequestBody @Valid TeamCreateRequestDto dto) {
        TeamCreateResponseDto responseDto = teamService.create(dto);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(responseDto);
    }

    @GetMapping("/{team_name}")
    public ResponseEntity<TeamGetResponseDto> get(@PathVariable("team_name") String teamName) {
        TeamGetResponseDto responseDto = teamService.get(teamName);
        return ResponseEntity.ok(responseDto);
    }
}
