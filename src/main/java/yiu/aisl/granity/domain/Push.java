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
public class Push {
    @Id
    @Column(unique = true)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user_id;

    @Column(nullable = false)
    private int type;

    @Column(nullable = false)
    private int type_id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contents;

    @Column(nullable = false)
    private int checks;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;
}
