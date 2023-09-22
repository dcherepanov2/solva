package kz.solva.tz.expense.tracker.api.data;


import jakarta.persistence.*;
import lombok.Data;


import java.util.List;
import java.util.TimeZone;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;


    @OneToMany(cascade = CascadeType.ALL)
    private List<Account> accounts;

    @Column(name = "timezone")
    private TimeZone timezone;
}






