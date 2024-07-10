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
public class MajorMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "major_group_code_id",nullable = false)
    private MajorGroupCode majorGroupCode;

    @Column(nullable = false)
    private Integer role;

    @Column(nullable = false)
    private String name;

    @Column
    private String file;

    @Column(nullable = false)
    private String content1; // 커리어, 연구과제, 경력사항 etc

    @Column
    private String content2; // 커리어, 연구과제, 경력사항 etc

    @Column
    private String content3; // 커리어, 연구과제, 경력사항 etc

    @Column(nullable = false)
    private String tel;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String email;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;
}
