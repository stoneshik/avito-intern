package avito.pr.reviewer.assignment.dto.responses.pr.reassign;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrReassignResponseDto {
    @JsonProperty("pr")
    private PrReassignPrResponseDto pr;

    @JsonProperty("replaced_by")
    private String replacedBy;
}
