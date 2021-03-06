package org.eindopdracht.resource.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    List<Consultation> consultations;

    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    List<Schedule> schedules;

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;

    @NotBlank
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "profileImagePath", nullable = false)
    private String profileImagePath;

    @Column(name = "isApproved")
    private boolean isApproved;

    @Column(name = "role")
    private String role;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;
}