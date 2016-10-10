package controllers;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import data.QuizDAO;
import entities.Question;
import entities.Quiz;
import entities.Score;

@RestController
public class QuizController {

	@Autowired
	QuizDAO quizDao;
	
	@RequestMapping(path="/quiz", method=RequestMethod.GET)
	public List<Quiz> index() {
		return quizDao.index();
	}
	
	@RequestMapping(path="/quiz/{id}", method=RequestMethod.GET)
	public Quiz index(@PathVariable int id) {
		return quizDao.show(id);
	}
	
	@RequestMapping(path="/quiz", method=RequestMethod.POST)
	public void create(@RequestBody String userJSON, HttpServletResponse response) {
		ObjectMapper mapper = new ObjectMapper();
		Quiz quiz = null;
		try {
			quiz = mapper.readValue(userJSON, Quiz.class);
			response.setStatus(201);
			quizDao.create(quiz);
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus(400);
		}
	}
	
	@RequestMapping(path="/quiz/{id}", method=RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody String userJSON, HttpServletResponse response) {
		ObjectMapper mapper = new ObjectMapper();
		Quiz quiz = null;
		try {
			quiz = mapper.readValue(userJSON, Quiz.class);
			response.setStatus(201);
			quizDao.update(quiz, id);
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus(400);
		}
	}
	
	@RequestMapping(path="/quiz/{id}", method=RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		quizDao.delete(id);
	}

	// Get scores for a specific quiz by quiz id
	@RequestMapping(path="/quiz/{id}/scores", method=RequestMethod.GET)
	public Set<Score> showScores(@PathVariable int id) {
		return quizDao.showScores(id);
	}
	// Add new score to a quiz by quiz id
	@RequestMapping(path="/quiz/{id}/score", method=RequestMethod.POST)
	public void createScore(@RequestBody String userJSON, @PathVariable int id, 
								HttpServletResponse response) {	
		ObjectMapper mapper = new ObjectMapper();
		Score score = null;
		try {
			score = mapper.readValue(userJSON, Score.class);
			response.setStatus(201);
			quizDao.createScore(score, id);
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus(400);
		}
	}
		
	// Create a question for a quiz by quiz id
	@RequestMapping(path="/quiz/{id}/questions", method=RequestMethod.POST)
	public void createQuestion(@RequestBody String userJSON, @PathVariable int id, 
								HttpServletResponse response) {	
		ObjectMapper mapper = new ObjectMapper();
		Question question = null;
		try {
			question = mapper.readValue(userJSON, Question.class);
			response.setStatus(201);
			quizDao.createQuestion(question, id);
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus(400);
		}
	}
	
	// Delete question from quiz with quiz id and question id
	@RequestMapping(path="/quiz/{id}/questions/{q_id}", method=RequestMethod.DELETE)
	public void deleteQuestion(@PathVariable int id, @PathVariable int q_id) {
		quizDao.deleteQuestion(id, q_id);
	}
}
