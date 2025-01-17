package fr.fms.business;

import fr.fms.entities.Category;
import fr.fms.entities.Task;
import fr.fms.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IBusiness {
    /**
     * Méthode qui renvoi la liste des taches en base
     * @return List<Task>
     * @throws Exception
     */
    public List<Task> getTasks() throws Exception;

    /**
     * Méthode qui renvoie la liste des catégories en base
     * @return List<Category>
     * @throws Exception
     */
    public List<Category> getAllCategories() throws Exception;

    /**
     * Méthode qui renvoie une catégorie par son ID
     * @param categoryId l'ID de la catégorie
     * @return Category
     * @throws Exception
     */
    public Category getCategoryById(Long categoryId) throws Exception;

    /**
     * Méthode qui renvoie un utilisateur par son username
     * @param username le nom d'utilisateur
     * @return User
     * @throws Exception
     */
    public User getUserByUsername(String username) throws Exception;

    /**
     * Méthode pour sauvegarder une tâche
     * @param task l'objet Task à sauvegarder
     * @throws Exception
     */
    public void saveTask(Task task) throws Exception;

    /**
     * Méthode qui renvoie la liste des tâches pour un utilisateur donné
     * @param user l'utilisateur pour lequel récupérer les tâches
     * @return List<Task>
     * @throws Exception
     */
    public List<Task> getTasksByUser(User user) throws Exception;

    /**
     * Méthode qui renvoie une tâche par son ID
     * @param id l'ID de la tâche
     * @return Task
     * @throws Exception
     */
    public Task getTaskById(Long id) throws Exception;

    /**
     * Méthode qui permet de supprimer une tâche par son ID
     * @param taskId l'ID de la tâche à supprimer
     * @throws Exception
     */
    public void deleteTask(Long taskId) throws Exception;

    public List<Task> searchTasksByKeyword(User user, String keyword) throws Exception;
}
