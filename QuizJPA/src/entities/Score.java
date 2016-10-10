package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Score {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	@JsonBackReference(value="scores")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="quiz_id")
	@JsonBackReference(value="quiz")
	private Quiz quiz;
	
	private double value;
	
	
	
	public int getId() {
		return id;
	}
	public User getUser() {
		return user;
	}

	public Quiz getQuiz() {
		return quiz;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public String getUsername() {
		return user.getUsername();
	}
	public int getUserId() {
		return user.getId();
	}
	
	
	@Override
	public String toString() {
		return "Score [id=" + id +" ]";
	}
	
	
	
}
