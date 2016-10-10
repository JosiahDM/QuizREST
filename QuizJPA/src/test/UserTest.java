package test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import entities.Question;
import entities.Quiz;
import entities.User;


public class UserTest {
	public static void main(String[] args) {
		EntityManagerFactory emf;
		EntityManager em;

		emf = Persistence.createEntityManagerFactory("QuizJPA");
	    em = emf.createEntityManager();
	    
	    User u = em.find(User.class, 1);
	    System.out.println(u);
	    System.out.println(u.getScores());
	    
	    Quiz q = em.find(Quiz.class, 1);
	    System.out.println(q.getQuestions());
	    
	    Question question = em.find(Question.class, 1);
	    System.out.println(question.getAnswers());
	    
	    em.getTransaction().begin();
	    Question q2 = new Question();
	    q2.setQuestionText("Blah de blah de blah");
	    q.addQuestion(q2);
	    q2.setQuiz(q);
	    em.persist(q2);
	    em.persist(q);
	    em.getTransaction().commit();
	    System.out.println(q.getQuestions());
	    
		em.close();
		emf.close();
	}
}
