package yiu.aisl.granity.domain;

import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @Column(unique = true)
    private int id;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board_id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user_id;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int checks;

    @ManyToOne
    @JoinColumn(name = "check_user_id")
    private User check_user_id;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;
}
