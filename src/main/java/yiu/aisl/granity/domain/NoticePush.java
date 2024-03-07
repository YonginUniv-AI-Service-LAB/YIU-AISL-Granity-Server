package yiu.aisl.granity.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import yiu.aisl.granity.domain.Major;
import yiu.aisl.granity.domain.Notice;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticePush {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Notice notice;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Major major;
}
