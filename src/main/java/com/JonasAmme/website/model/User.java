package com.JonasAmme.website.model;

import com.JonasAmme.website.security.Role;
import com.JonasAmme.website.utils.DataHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "[user]")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="username", nullable = false, unique = true, length = 40)
    private String username;

    @Column(name="password", nullable = false, unique = true, length = 200)
    private String password;
    @Column(name = "date_modified")
    private LocalDateTime dateModified;

    @Column(name = "date_created")
    private LocalDateTime dateCreated = DataHelper.getCurrentTimeStamp();
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

}
