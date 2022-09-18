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
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "date_modified")
    private LocalDateTime dateModified;

    @Column(name = "date_created")
    private LocalDateTime dateCreated = DataHelper.getCurrentTimeStamp();
    @Enumerated(EnumType.STRING)
    private Role role;

    public boolean isUser() {
        return role.equals(Role.USER);
    }

    public boolean isAdmin() {
        return role.equals(Role.ADMIN);
    }

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

}
