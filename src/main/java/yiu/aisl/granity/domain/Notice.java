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
public class Notice {
    @Id
    @Column(unique = true)
    private int id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contents;

    @Column(nullable = false)
    private String file;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user_id;

    @Column(nullable = false)
    private int category; // 뉴스 or 공지

    @Column(nullable = false)
    private int status;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int hit;

    @ManyToOne
    @JoinColumn(name = "major_group_id", nullable = false)
    private MajorGroup major_group_id;

    @Column(nullable = false)
    private int grade1;

    @Column(nullable = false)
    private int grade2;

    @Column(nullable = false)
    private int grade3;

    @Column(nullable = false)
    private int grade4;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;
}
