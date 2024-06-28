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
public class Message {
    @Id
    @Column(unique = true)
    private int id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contents;

    @ManyToOne
    @JoinColumn(name = "from_user_id", nullable = false)
    private User from_user_id;

    @ManyToOne
    @JoinColumn(name = "to_user_id", nullable = false)
    private User to_user_id;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;
}
