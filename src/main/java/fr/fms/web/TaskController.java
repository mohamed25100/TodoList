package fr.fms.web;

import fr.fms.business.IBusinessImpl;
import fr.fms.entities.Category;
import fr.fms.entities.Task;
import fr.fms.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
public class TaskController {
    @Autowired
    IBusinessImpl businessImpl;

    private final Logger logger = LoggerFactory.getLogger(TaskController.class);
    /**
     * Méthode en GET correspondant à l'url .../index ou page d'accueil de notre application
     * @return la page tasks.html
     */
    @GetMapping("/index")
    public String index(Model model) {
        try {
            // Récupérer l'utilisateur connecté
            String username = getUsernameFromSecurityContext();

            // Récupérer l'utilisateur connecté via la couche business
            User user = businessImpl.getUserByUsername(username);

            // Récupérer uniquement les tâches de l'utilisateur connecté via la couche business
            List<Task> tasks = businessImpl.getTasksByUser(user); // Utilisation de la couche business

            model.addAttribute("listTask", tasks); // Ajouter les tâches à la vue

            model.addAttribute("username", username); // Ajouter le nom d'utilisateur à la vue
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
        }

        return "tasks"; // Retourner la page des tâches (tasks.html)
    }



    /**
     * Méthode en GET correspondant à l'url .../add pour afficher le formulaire d'ajout d'une tâche
     * @param model le modèle pour transmettre les données à la vue
     * @return la page addTask.html
     */
    @GetMapping("/add")
    public String showAddTaskForm(Model model) {
        try {
            // Récupérer la liste des catégories pour afficher dans le formulaire
            List<Category> categories = businessImpl.getAllCategories();
            model.addAttribute("categories", categories);

            // Créer un nouvel objet Task pour le formulaire
            model.addAttribute("task", new Task());
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des catégories: " + e.getMessage());
            return "error"; // Assurez-vous d'avoir une vue d'erreur si nécessaire
        }

        return "addTask"; // Afficher le formulaire d'ajout de tâche
    }

    /**
     * Méthode en POST pour sauvegarder la tâche envoyée depuis le formulaire
     * @param task l'objet Task envoyé par le formulaire
     * @param model le modèle pour transmettre des informations à la vue
     * @return la redirection vers la page des tâches après la sauvegarde
     */
    @PostMapping("/save")
    public String saveTask(@ModelAttribute("task") Task task, Model model) {
        try {
            // Récupérer l'utilisateur connecté
            String username = getUsernameFromSecurityContext();
            User user = businessImpl.getUserByUsername(username);  // Fetch the user

            // Assigner l'utilisateur à la tâche
            task.setUser(user);

            // Assigner la date actuelle à la tâche
            task.setDate(new Date());

            // Sauvegarder la tâche via la couche business
            businessImpl.saveTask(task);

            // Afficher un message de confirmation
            model.addAttribute("message", "La tâche a été ajoutée avec succès!");

        } catch (Exception e) {
            logger.error("Erreur lors de la sauvegarde de la tâche: " + e.getMessage());
            model.addAttribute("errorMessage", "Une erreur est survenue lors de la sauvegarde de la tâche.");
            return "addTask"; // Retourner à la page d'ajout en cas d'erreur
        }

        // Rediriger vers la page d'accueil ou la page des tâches
        return "redirect:/index";
    }

    /**
     * Méthode en GET pour afficher le formulaire d'édition d'une tâche
     * @param id l'ID de la tâche à éditer
     * @param model le modèle pour transmettre les données à la vue
     * @return la page editTask.html
     */
    @GetMapping("/edit")
    public String showEditTaskForm(@RequestParam("id") Long id, Model model) {
        try {
            // Récupérer la tâche par son ID via la couche business
            Task task = businessImpl.getTaskById(id);

            // Récupérer la liste des catégories pour afficher dans le formulaire
            List<Category> categories = businessImpl.getAllCategories();
            model.addAttribute("categories", categories);

            // Ajouter la tâche au modèle pour qu'elle puisse être modifiée dans le formulaire
            model.addAttribute("task", task);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la tâche: " + e.getMessage());
            model.addAttribute("errorMessage", "Une erreur est survenue lors de la récupération de la tâche.");
            return "error"; // Assurez-vous d'avoir une vue d'erreur si nécessaire
        }

        return "editTask"; // Afficher le formulaire d'édition de tâche
    }

    /**
     * Méthode en POST pour sauvegarder les modifications apportées à la tâche
     * @param task l'objet Task envoyé par le formulaire
     * @param model le modèle pour transmettre des informations à la vue
     * @return la redirection vers la page des tâches après la sauvegarde
     */
    @PostMapping("/update")
    public String updateTask(@Valid Task task, BindingResult bindingResult, Model model, RedirectAttributes redirectAttrs) {
        try {
            // Si le bindingResult contient des erreurs, retourner au formulaire d'édition
            if(bindingResult.hasErrors()) {
                model.addAttribute("categories", businessImpl.getAllCategories());
                return "editTask"; // Afficher à nouveau le formulaire d'édition
            }

            // Vérifier si la tâche existe avant de la mettre à jour
            Task existingTask = businessImpl.getTaskById(task.getId());
            if (existingTask == null) {
                model.addAttribute("errorMessage", "Tâche non trouvée");
                return "editTask";
            }

            // Mettre à jour les propriétés de la tâche existante
            existingTask.setName(task.getName());
            existingTask.setDescription(task.getDescription());
            existingTask.setCategory(task.getCategory());
            existingTask.setDate(new Date()); // Optionnellement, mettre à jour la date

            // Sauvegarder la tâche mise à jour
            businessImpl.saveTask(existingTask);

            // Message de confirmation
            redirectAttrs.addFlashAttribute("message", "La tâche a été mise à jour avec succès!");

        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de la tâche: " + e.getMessage());
            model.addAttribute("errorMessage", "Une erreur est survenue lors de la mise à jour de la tâche.");
            return "editTask"; // Retourner au formulaire d'édition en cas d'erreur
        }

        // Rediriger vers la page des tâches après la mise à jour
        return "redirect:/index";
    }

    @GetMapping("/delete")
    public String deleteTask(@RequestParam("id") Long id, RedirectAttributes redirectAttrs) {
        try {
            // Delete the task by ID
            businessImpl.deleteTask(id);

        } catch (Exception e) {
            // Handle any errors and add an error message to the redirect attributes
            redirectAttrs.addFlashAttribute("errorMessage", "Une erreur est survenue lors de la suppression de la tâche.");
        }

        // Redirect back to the index page after deletion
        return "redirect:/index";
    }




    /**
     * Méthode pour obtenir le nom d'utilisateur connecté
     * @return le nom d'utilisateur ou une chaîne vide
     */
    private String getUsernameFromSecurityContext() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername(); // Nom d'utilisateur si authentifié
        } else {
            return principal.toString().contains("anonymous") ? "" : principal.toString();
        }
    }

}
