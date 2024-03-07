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
public class MajorMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Major major;

    @Column(nullable = false)
    private Integer role;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String content1;

    @Column(nullable = false)
    private String content2;

    @Column(nullable = false)
    private String content3;

    @Column(nullable = false)
    private String tel;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String email;
}
