package yiu.aisl.granity.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticePush {
    @Id
    @Column(unique = true)
    private int id;

    @Column(nullable = false)
    private int type;

    @ManyToOne
    @JoinColumn(name = "major_group_id", nullable = false)
    private MajorGroup major_group_id;
}
