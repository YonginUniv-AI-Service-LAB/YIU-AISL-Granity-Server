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
public class MajorCurriculum {
    @Id
    @Column(unique = true)
    private int id;

    @ManyToOne
    @JoinColumn(name = "major_group_id", nullable = false)
    private MajorGroup major_group_id;

    @Column(nullable = false)
    private String subject; // 과목명

    @Column(nullable = false)
    private int classification; // 기초전공 여부

    @Column(nullable = false)
    private int grade; // 학년

    @Column(nullable = false)
    private int semester; // 학기

    @Column(nullable = false)
    private int code; // 학수번호

    @Column(nullable = false)
    private int credit; // 학점

    @Column(nullable = false)
    private int theory; // 이론 몇 실기 몇

    @Column(nullable = false)
    private int practice; // 전공 및 실기 여부

    @Column(nullable = false)
    private int hidden; // 홈페이지 공개여부

    @Column(nullable = false)
    private int required; // 필수 이수 과목 여부

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;
}
