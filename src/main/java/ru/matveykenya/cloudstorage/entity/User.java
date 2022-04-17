package ru.matveykenya.cloudstorage.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User {
    @Id
    @Column(name ="username", length = 50, nullable = false)
    private String username;

    @Column(length = 50, nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean enabled;

}
