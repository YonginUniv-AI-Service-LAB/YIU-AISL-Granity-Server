package yiu.aisl.granity.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YearlyEvents {
    private Integer year;
    private List<MajorHistoryResponseDto> events;

    public static YearlyEvents fromMapEntry(Map.Entry<Integer, List<MajorHistoryResponseDto>> entry) {
        return new YearlyEvents(entry.getKey(), entry.getValue());
    }
}
