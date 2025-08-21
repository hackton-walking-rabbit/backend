package ddg.walking_rabbit.global.domain.entity;

import ddg.walking_rabbit.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChatRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRecordId;

    @OneToOne
    @JoinColumn(nullable = false)
    private ConversationEntity conversation;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity user;

    private String title;
    private String coverPhoto;
    private Integer explorerOrder;
    private LocalDateTime createdAt;
}
