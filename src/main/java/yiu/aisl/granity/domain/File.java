package yiu.aisl.granity.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Integer id;

    @Column(nullable = false)
    private Integer type;

    @Column(nullable = false)
    private Integer typeId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String originName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String saveName;

    @Column(nullable = false)
    private Long size;

    // 파일 삭제 여부
    @Column(nullable = false)
    private Integer deleteOrNot;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;
}
