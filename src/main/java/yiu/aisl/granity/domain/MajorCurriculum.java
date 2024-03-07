package yiu.aisl.granity.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MajorCurriculum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Major major;

    @Column(nullable = false)
    private Integer classification;

    @Column(nullable = false)
    private Integer grade;

    @Column(nullable = false)
    private Integer semester;

    @Column(nullable = false)
    private Integer code;

    @Column(nullable = false)
    private Integer credit;

    @Column(nullable = false)
    private Integer theory;

    @Column(nullable = false)
    private Integer practice;
}
