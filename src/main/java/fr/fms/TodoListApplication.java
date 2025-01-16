package fr.fms;

import fr.fms.business.IBusinessImpl;
import fr.fms.dao.CategoryRepository;
import fr.fms.dao.TaskRepository;
import fr.fms.dao.UserRepository;
import fr.fms.entities.Category;
import fr.fms.entities.Task;
import fr.fms.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class TodoListApplication implements CommandLineRunner {
	@Autowired
	TaskRepository taskRepository;
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	IBusinessImpl businessImpl;

	public static void main(String[] args) {
		SpringApplication.run(TodoListApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
	generateTask2();


	}
	private void generateTask(){

		// Créer une nouvelle instance de Category et définir les propriétés via les setters
		Category category = new Category();
		category.setName("Work");

		// Sauvegarder la catégorie en base
		categoryRepository.save(category);

		// Créer une nouvelle tâche et l'associer à la catégorie créée
		Task task = new Task();
		task.setDate(new Date());
		task.setName("Complete Spring Boot Project");
		task.setDescription("Finalize the Spring Boot application for the to-do list project");
		task.setCategory(category);

		// Sauvegarder la tâche en base
		taskRepository.save(task);

		System.out.println("New task added successfully: " + task);
	}
	private void generateTask2() {
		// Exemple de création d'une nouvelle catégorie si nécessaire
		Category category = new Category(null, "TO DO", null);
		categoryRepository.save(category);

		// Exemple de création d'une nouvelle catégorie si nécessaire
		Category category2 = new Category(null, "DOING", null);
		categoryRepository.save(category2);

		// Exemple de création d'une nouvelle catégorie si nécessaire
		Category category3 = new Category(null, "DONE", null);
		categoryRepository.save(category3);

		// Exemple de création user
		User user = new User(null,"mohamed","$2a$10$s3XJbTsSBnJmCT6sXJ1lauyCPzHtHT9HjMhDuNlMvqAcnsfOcjNhW","mohamed.boucherba@fms-ea.com",null);
		userRepository.save(user);

		// Ajout d'une nouvelle tâche avec des détails
		Task task = new Task(null, new Date(), "Complete Spring Boot Project",
				"Finalize the Spring Boot application for the to-do list project", category,user);

		taskRepository.save(task);

		System.out.println("New task added successfully: " + task);
	}
}
