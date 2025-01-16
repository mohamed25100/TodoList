package fr.fms.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "tasks")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le nom d'utilisateur est obligatoire.")
    @Size(min=3,max=30)
    private String username;

    @NotNull(message = "Le mot de passe est obligatoire.")
    private String password;

    @Email(message = "Veuillez fournir une adresse e-mail valide.")
    @NotNull(message = "L'adresse e-mail est obligatoire.")
    private String email;

    @OneToMany(mappedBy = "user")
    private Collection<Task> tasks;

}