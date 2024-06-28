package yiu.aisl.granity.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MajorHistory {
    @Id
    @Column(unique = true)
    private int id;

    @ManyToOne
    @JoinColumn(name = "major_group_id", nullable = false)
    private MajorGroup major_group_id;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int month;

    @Column(nullable = false)
    private String event;
}