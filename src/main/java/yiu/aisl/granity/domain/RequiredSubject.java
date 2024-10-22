package yiu.aisl.granity.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequiredSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Integer id;

    @Column(nullable = false)
    @JoinColumn(name = "majorGroupCode_id", nullable = false)
    private String majorGroupCode;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String common;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String ai;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String bigdata;
}
