package ddg.walking_rabbit.global.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long missionId;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity user;

    private String content;

    private LocalDate missionDate;

    private MissionType missionType;

    private MissionStatus isSuccess;
}
