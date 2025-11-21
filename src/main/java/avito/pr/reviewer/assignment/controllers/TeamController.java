package avito.pr.reviewer.assignment.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import avito.pr.reviewer.assignment.dto.requests.team.TeamAddRequestDto;
import avito.pr.reviewer.assignment.dto.responses.team.add.TeamAddResponseDto;
import avito.pr.reviewer.assignment.dto.responses.team.get.TeamGetResponseDto;
import avito.pr.reviewer.assignment.services.team.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @PostMapping("/add")
    public ResponseEntity<TeamAddResponseDto> add(@RequestBody @Valid TeamAddRequestDto dto) {
        TeamAddResponseDto responseDto = teamService.add(dto);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(responseDto);
    }

    @GetMapping("/get")
    public ResponseEntity<TeamGetResponseDto> get(@RequestParam("team_name") String teamName) {
        TeamGetResponseDto responseDto = teamService.get(teamName);
        return ResponseEntity.ok(responseDto);
    }
}
