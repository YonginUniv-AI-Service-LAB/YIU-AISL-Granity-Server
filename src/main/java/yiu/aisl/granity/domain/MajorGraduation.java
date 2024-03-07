package yiu.aisl.granity.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import yiu.aisl.granity.domain.Major;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MajorGraduation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Major major;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String note;

    @Column(nullable = false)
    private Integer target;
}
