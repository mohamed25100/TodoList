package fr.fms.dao;

import fr.fms.entities.Task;
import fr.fms.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user); // Méthode pour rechercher des tâches par utilisateur
}
