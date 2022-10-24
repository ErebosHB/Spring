package de.encoway.backend.model;
import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "user_table")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
}
