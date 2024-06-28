package yiu.aisl.granity.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import yiu.aisl.granity.domain.state.RoleCategory;
import yiu.aisl.granity.domain.state.StatusCategory;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @Column(unique = true)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int grade;

    @Column(nullable = false, length = 255)
    private String pwd;

    @Column(nullable = false) // 관리자(0), 학생(1), 조교 및 교수(2)
    @Enumerated(EnumType.ORDINAL)
    private RoleCategory role;

    @Column(nullable = false) // 휴학(0), 재학(1)
    @Enumerated(EnumType.ORDINAL)
    private StatusCategory status;

    @Column(nullable = false)
    @ColumnDefault("1")
    private int push;

    @Column(columnDefinition = "TEXT")
    private String fcmToken;

    @Column(columnDefinition = "TEXT")
    private String refreshToken;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;
}
