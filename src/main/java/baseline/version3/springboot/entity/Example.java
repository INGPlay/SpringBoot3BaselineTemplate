package baseline.version3.springboot.entity;

import baseline.version3.springboot.entity.base.DateTimeBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "example")
@Getter @Setter
public class Example extends DateTimeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String content;

}
