package avito.pr.reviewer.assignment.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import avito.pr.reviewer.assignment.dto.requests.user.UserSetIsActiveRequestDto;
import avito.pr.reviewer.assignment.dto.responses.user.getreview.UserGetReviewResponseDto;
import avito.pr.reviewer.assignment.dto.responses.user.setisactive.UserSetIsActiveResponseDto;
import avito.pr.reviewer.assignment.services.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/setIsActive")
    public ResponseEntity<UserSetIsActiveResponseDto> setIsActive(@RequestBody @Valid UserSetIsActiveRequestDto dto) {
        UserSetIsActiveResponseDto createdResponseDto = userService.setIsActive(
            dto.getUserId(),
            dto.getIsActive()
        );
        return ResponseEntity.ok(createdResponseDto);
    }

    @GetMapping("/getReview")
    public ResponseEntity<UserGetReviewResponseDto> getReview(@RequestParam("user_id") String userId) {
        UserGetReviewResponseDto responseDto = userService.getReview(userId);
        return ResponseEntity.ok(responseDto);
    }
}
