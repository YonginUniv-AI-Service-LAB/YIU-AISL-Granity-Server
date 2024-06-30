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
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "major_group_id", nullable = false)
    private MajorGroup major_group_id;

    @Column(nullable = false)
    private String subject; // 과목명

    @Column(nullable = false)
    private Integer classification; // 기초전공 여부

    @Column(nullable = false)
    private Integer grade; // 학년

    @Column(nullable = false)
    private Integer semester; // 학기

    @Column(nullable = false)
    private Integer code; // 학수번호

    @Column(nullable = false)
    private Integer credit; // 학점

    @Column(nullable = false)
    private Integer theory; // 이론 몇 실기 몇

    @Column(nullable = false)
    private Integer practice; // 전공 및 실기 여부

    @Column(nullable = false)
    private Integer hidden; // 홈페이지 공개여부

    @Column(nullable = false)
    private Integer required; // 필수 이수 과목 여부

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;
}
