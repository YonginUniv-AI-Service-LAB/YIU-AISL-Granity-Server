package yiu.aisl.granity.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yiu.aisl.granity.domain.MajorHistory;

import java.util.*;
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

// 각 연도의 이벤트를 month 기준으로 최신 순서대로 정렬
        return groupedByYear.entrySet().stream()
                .map(entry -> {
                    List<MajorHistoryResponseDto> sortedList = entry.getValue().stream()
                            .sorted(Comparator.comparing(MajorHistoryResponseDto::getMonth).reversed()) // month 기준으로 내림차순 정렬
                            .collect(Collectors.toList());
                    return YearlyEvents.fromMapEntry(new AbstractMap.SimpleEntry<>(entry.getKey(), sortedList));
                })
                .collect(Collectors.toList());
    }
}
