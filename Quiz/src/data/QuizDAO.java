package data;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import entities.Question;
import entities.Quiz;
import entities.Score;

@Transactional
public class QuizDAO {

	@PersistenceContext
	private EntityManager em;
	
	public List<Quiz> index() {
		String query = "select q from Quiz q";
		return em.createQuery(query, Quiz.class).getResultList();
	}
	
	public Quiz show(int id) {
		return em.find(Quiz.class, id);
	}
	
	public Set<Score> showScores(int id) {
		Quiz q = em.find(Quiz.class, id);
		Set<Score> scores = q.getScores();
		return scores;
	}
	
	// Create score for given quiz id
	public void createScore(Score score, int id) {
		Quiz q = em.find(Quiz.class, id);
		score.setQuiz(q);
		em.persist(score);
		em.flush();
	};
	
	public void create(Quiz quiz) {
		em.persist(quiz);
		em.flush();
	}
	
	public void update(Quiz quiz, int id) {
		Quiz q = em.find(Quiz.class, id);
		q.setName(quiz.getName());
		q.setScores(q.getScores());
		em.persist(q);
		em.flush();
	}
	
	public void delete(int id) {
		Quiz q = em.find(Quiz.class, id);
		em.remove(q);
		em.flush();
	}
	public void deleteQuestion(int id, int qId) {
		Quiz q = em.find(Quiz.class, id);
		Question que = em.find(Question.class, qId);
		q.removeQuestion(que);
		em.remove(que);
		em.persist(q);
		em.flush();
	}
	public void createQuestion(Question question, int id) {
		Quiz q = em.find(Quiz.class, id);
		q.addQuestion(question);
		em.persist(question);
		em.persist(q);
		em.flush();
	}
	
}
