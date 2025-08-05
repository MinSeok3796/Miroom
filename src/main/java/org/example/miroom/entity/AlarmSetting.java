package org.example.miroom.entity;


import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.Getter;
import org.example.miroom.enums.AlarmType;

@Entity
@Table(name = "alarm_setting", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "alarm_type"}))
@Getter
public class AlarmSetting extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alarmSettingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "alarm_type", nullable = false)
    private AlarmType alarmType;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    public AlarmSetting() {}

    public AlarmSetting(User user, AlarmType alarmType, boolean enabled) {
        this.user = user;
        this.alarmType = alarmType;
        this.enabled = enabled;
    }

}
