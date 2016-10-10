package entities;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Question {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="quiz_id", referencedColumnName="id")
	@JsonBackReference(value="quizQuestions")
	private Quiz quiz;
	
	@Column(name="question_text")
	private String questionText;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="question_id", referencedColumnName="id",  nullable =false)
	@OrderBy("id ASC")
	private Set<Answer> answers;

	public int getId() {
		return id;
	}

	public Quiz getQuiz() {
		return quiz;
	}

	public String getQuestionText() {
		return questionText;
	}

	public Set<Answer> getAnswers() {
		return answers;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public void setAnswers(LinkedHashSet<Answer> answers) {
		this.answers = answers;
	}

	@Override
	public String toString() {
		return "Question [id=" + id + ", quiz=" + quiz + ", questionText=" + questionText
				+ "]";
	}
	
}
