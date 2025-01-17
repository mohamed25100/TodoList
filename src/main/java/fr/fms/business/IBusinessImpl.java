package fr.fms.business;

import fr.fms.dao.CategoryRepository;
import fr.fms.dao.TaskRepository;
import fr.fms.dao.UserRepository;
import fr.fms.entities.Category;
import fr.fms.entities.Task;
import fr.fms.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IBusinessImpl implements IBusiness{
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<Task> getTasks() throws Exception {
        return taskRepository.findAll();
    }

    @Override
    public List<Category> getAllCategories() throws Exception {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long categoryId) throws Exception {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            return category.get();
        } else {
            throw new Exception("Catégorie introuvable avec ID: " + categoryId);
        }
    }

    @Override
    public User getUserByUsername(String username) throws Exception {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new Exception("Utilisateur introuvable avec username: " + username);
        }
    }

    @Override
    public void saveTask(Task task) throws Exception {
        taskRepository.save(task);
    }

    @Override
    public List<Task> getTasksByUser(User user) throws Exception {
        if (user == null) {
            throw new Exception("Utilisateur introuvable.");
        }
        return taskRepository.findByUser(user);
    }

    @Override
    public Task getTaskById(Long id) throws Exception {
        return taskRepository.getById(id);
    }

    @Override
    public void deleteTask(Long taskId) throws Exception {
        // Supprimez la tâche.
        taskRepository.deleteById(taskId);
    }

    @Override
    public List<Task> searchTasksByKeyword(User user, String keyword) {
            return taskRepository.findByUserAndKeyword(user.getId(), "%" + keyword + "%");
    }
}
