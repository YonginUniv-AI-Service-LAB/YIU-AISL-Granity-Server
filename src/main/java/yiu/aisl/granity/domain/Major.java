package yiu.aisl.granity.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Major {
    @Id
    private Integer id;

    @Column(nullable = false)
    private String major;

    @Column(nullable = false)
    private String greetings;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String tel;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String fax;
}
