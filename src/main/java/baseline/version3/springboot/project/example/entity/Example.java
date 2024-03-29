package baseline.version3.springboot.project.example.entity;

import baseline.version3.springboot.common.entity.base.DateTimeBase;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "example")
@Getter @Setter
public class Example extends DateTimeBase {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String content;

}
