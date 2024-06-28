package yiu.aisl.granity.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {
    @Id
    @Column(unique = true)
    private int id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contents;

    @Column
    private String file;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user_id;

    @Column(nullable = false) // 조교 또는 교수의 승인 여부
    @ColumnDefault("0")
    private int checks;

    @ManyToOne
    @JoinColumn(name = "major_group_id", nullable = false)
    private MajorGroup major_group_id;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int hit;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;
}
