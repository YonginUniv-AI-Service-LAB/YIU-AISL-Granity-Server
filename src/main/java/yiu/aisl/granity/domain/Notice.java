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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contents;

    @Column
    private String file;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer category; // 뉴스 or 공지

    @Column(nullable = false)
    private Integer status;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer hit;

    @ManyToOne
    @JoinColumn(name = "major_group_code_id",nullable = false)
    private MajorGroupCode majorGroupCode;

    @Column(nullable = false)
    private Integer grade1;

    @Column(nullable = false)
    private Integer grade2;

    @Column(nullable = false)
    private Integer grade3;

    @Column(nullable = false)
    private Integer grade4;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;
}
