package avito.pr.reviewer.assignment.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import avito.pr.reviewer.assignment.dto.requests.pullrequest.PrCreateRequestDto;
import avito.pr.reviewer.assignment.dto.requests.pullrequest.PrMergeRequestDto;
import avito.pr.reviewer.assignment.dto.requests.pullrequest.PrReassignRequestDto;
import avito.pr.reviewer.assignment.dto.responses.pr.create.PrCreateResponseDto;
import avito.pr.reviewer.assignment.dto.responses.pr.merge.PrMergeResponseDto;
import avito.pr.reviewer.assignment.dto.responses.pr.reassign.PrReassignResponseDto;
import avito.pr.reviewer.assignment.services.pr.PrService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/pull-requests")
@RequiredArgsConstructor
public class PullRequestController {
    private final PrService pullRequestService;

    @PostMapping
    public ResponseEntity<PrCreateResponseDto> create(@RequestBody @Valid PrCreateRequestDto dto) {
        PrCreateResponseDto createdResponseDto = pullRequestService.create(
            dto.getPullRequestId(),
            dto.getPullRequestName(),
            dto.getAuthorId()
        );
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(createdResponseDto);
    }

    @PostMapping("/merge")
    public ResponseEntity<PrMergeResponseDto> merge(@RequestBody @Valid PrMergeRequestDto dto) {
        PrMergeResponseDto responseDto = pullRequestService.merge(dto.getPullRequestId());
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/reassign")
    public ResponseEntity<PrReassignResponseDto> reassign(@RequestBody @Valid PrReassignRequestDto dto) {
        PrReassignResponseDto responseDto = pullRequestService.reassign(
            dto.getPullRequestId(),
            dto.getOldReviewerId()
        );
        return ResponseEntity.ok(responseDto);
    }
}
