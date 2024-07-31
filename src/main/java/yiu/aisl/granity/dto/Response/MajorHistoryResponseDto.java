package yiu.aisl.granity.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yiu.aisl.granity.domain.MajorHistory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MajorHistoryResponseDto {
    private Integer id;
    private Integer year;
    private Integer month;
    private String event;

    public static MajorHistoryResponseDto fromMajorHistory(MajorHistory majorHistory) {
        return new MajorHistoryResponseDto(
                majorHistory.getId(),
                majorHistory.getYear(),
                majorHistory.getMonth(),
                majorHistory.getEvent()
        );
    }

    public static List<YearlyEvents> groupByYear(List<MajorHistory> majorHistories) {
        Map<Integer, List<MajorHistoryResponseDto>> groupedByYear = majorHistories.stream()
                .map(MajorHistoryResponseDto::fromMajorHistory)
                .collect(Collectors.groupingBy(MajorHistoryResponseDto::getYear));

        return groupedByYear.entrySet().stream()
                .map(YearlyEvents::fromMapEntry)
                .collect(Collectors.toList());
    }
}
