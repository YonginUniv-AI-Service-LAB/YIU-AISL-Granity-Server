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
public class UserMajor {
    @Id
    @Column(unique = true)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user_id;

    @ManyToOne
    @JoinColumn(name = "major_id", nullable = false)
    private Major major_id;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;
}
