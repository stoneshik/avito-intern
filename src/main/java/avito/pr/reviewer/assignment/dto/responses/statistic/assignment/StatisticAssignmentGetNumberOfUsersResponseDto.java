package avito.pr.reviewer.assignment.dto.responses.statistic.assignment;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticAssignmentGetNumberOfUsersResponseDto {
    @JsonProperty("number_of_assigned_users")
    private Long numberOfAssignedUsers;
}
