package avito.pr.reviewer.assignment.dto.responses;

import avito.pr.reviewer.assignment.exceptions.CodeError;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {
    private String message;
    private CodeError code;
}
