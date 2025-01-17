package fr.fms.dao;

import fr.fms.entities.Task;
import fr.fms.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user); // Méthode pour rechercher des tâches par utilisateur
    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND (t.name LIKE :keyword OR t.description LIKE :keyword)")
    List<Task> findByUserAndKeyword(@Param("userId") Long userId, @Param("keyword") String keyword);
}
