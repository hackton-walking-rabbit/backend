package ddg.walking_rabbit.message.entity;

import ddg.walking_rabbit.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class ConversationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long conversationId;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity user;

    @OneToOne
    private MissionEntity mission;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

}
