package yiu.aisl.granity.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Major major_id1;

    @ManyToOne
    @JoinColumn
    private Major major_id2;

    @ManyToOne
    @JoinColumn
    private Major major_id3;

    @Column(nullable = false)
    private Integer grade;

    @Column(nullable = false)
    private String pwd;

    @Column(nullable = false)
    private Integer role;

    @Column(nullable = false)
    private Integer status;

    @Column
    private String fcm;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updatedAt;

    @Column
    @ColumnDefault("0")
    private Integer push;

    private String refreshToken;
}
