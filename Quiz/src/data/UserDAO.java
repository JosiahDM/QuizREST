package data;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import entities.Quiz;
import entities.Score;
import entities.User;

@Transactional
public class UserDAO {

	@PersistenceContext
	EntityManager em;
	
	@Autowired
	BCryptPasswordEncoder pe;
	
	public List<User> index() {
		String query = "select u from User u";
		return em.createQuery(query, User.class).getResultList();
	}

	
	public User show(int id) {
		return em.find(User.class, id);
	}

	public Set<Score> showScores(int id) {
		User u = em.find(User.class, id);
		return u.getScores();
	}
	
	public void createScore(Score score, int id, int qId) {
		User u = em.find(User.class, id);
		Quiz q = em.find(Quiz.class, qId);
		score.setUser(u);
		score.setQuiz(q);
		em.persist(score);
		em.flush();
	};
	
	public User login(User user) {
		String query = "select u from User u where username = ?1";
		User u = null;
		try { 
			u = em.createQuery(query, User.class)
					.setParameter(1, user.getUsername())
					.getSingleResult();		
			if (pe.matches(user.getPassword(), u.getPassword())) {
				return u;
			}
		} catch(Exception e) {
			System.err.println("User " + user.getUsername() + " not found.");
			u = null;
		}
		return null;
	};
	
	public void create(User user) {
		user.setPassword(pe.encode(user.getPassword()));
		em.persist(user);
		em.flush();
	}
	
	public void update(User user, int id) {
		User u = em.find(User.class, id);
		u.setPassword(pe.encode(user.getPassword()));
		u.setUsername(user.getUsername());
		em.persist(u);
		em.flush();
	}
	
	public void delete(int id) {
		User u = em.find(User.class, id);
		em.remove(u);
		em.flush();
	}
}
