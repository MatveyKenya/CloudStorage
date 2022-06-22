package ru.matveykenya.cloudstorage.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "files")
public class FileObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String filename;

    @Column
    private long size; //размер в байтах

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private User user;
}
