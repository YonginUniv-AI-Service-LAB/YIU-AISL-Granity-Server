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
public class MajorGroup {
    @Id
    @Column(unique = true)
    private int id;

    @Column(nullable = false)
    private String majorGroup;

    @ManyToOne
    @JoinColumn(name = "major_id", nullable = false)
    private Major major_id;

    @Column(nullable = false)
    private int status;

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

    @Column(nullable = false)
    private String color;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;
}
