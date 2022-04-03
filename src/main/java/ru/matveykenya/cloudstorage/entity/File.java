package ru.matveykenya.cloudstorage.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String description;

    @JoinColumn(name = "id", nullable = false)
    @OneToOne
    private User user;
}
