package yiu.aisl.granity.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MajorLab {
    @Id
    @Column(unique = true)
    private int id;

    @ManyToOne
    @JoinColumn(name = "major_group_id", nullable = false)
    private MajorGroup major_group_id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description; // 랩실 설명

    @Column
    private String file;

    @Column
    private String link;

    @Column
    private String tel;

    @Column
    private String email;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;
}
