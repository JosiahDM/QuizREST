package entities;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Quiz {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String name;
	
	@OneToMany(mappedBy="quiz", fetch = FetchType.EAGER)
//	@JsonManagedReference(value="quiz")
	@JsonIgnore
	private Set<Score> scores;
	
	@OneToMany(mappedBy="quiz", fetch=FetchType.EAGER)
	@JsonManagedReference(value="quizQuestions")
	@OrderBy("id ASC")
	private Set<Question> questions;
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<Score> getScores() {
		return scores;
	}
	public void setScores(Set<Score> scores) {
		this.scores = scores;
	}
	public Set<Question> getQuestions() {
		return questions;
	}
	public void setQuestions(Set<Question> questions) {
		this.questions = questions;
	}
	@Override
	public String toString() {
		return "Quiz [id=" + id + ", name=" + name + "]";
	}
	
	public void addQuestion(Question question) {
		if (questions == null) {
			questions = new TreeSet<>();
		}
		if (!questions.contains(question)) {
			questions.add(question);
			question.setQuiz(this);
		}
	}

	public void removeQuestion(Question question) {
		if (questions != null && questions.contains(question)) {
			questions.remove(question);
			question.setQuiz(null);
		}
	}
	
	
	
}
