package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Answer {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Column(name="answer_text")
	private String answerText;
	@Column(name="is_correct")
	private boolean isCorrect;
	
	
	public int getId() {
		return id;
	}
	public String getAnswerText() {
		return answerText;
	}
	public boolean isCorrect() {
		return isCorrect;
	}
	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}
	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}
	@Override
	public String toString() {
		return "Answer [id=" + id + ", answerText=" + answerText + ", isCorrect=" + isCorrect + "]";
	}
	
}
