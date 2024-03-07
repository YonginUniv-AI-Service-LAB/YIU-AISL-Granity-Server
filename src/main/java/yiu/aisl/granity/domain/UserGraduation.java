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
public class UserGraduation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private MajorGraduation major_graduation;

    @Column(nullable = false)
    private Integer status;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String file;

    @Column(nullable = false)
    private String feedback;
}
