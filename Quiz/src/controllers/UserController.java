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

import data.UserDAO;
import entities.Score;
import entities.User;

@RestController
public class UserController {

	@Autowired
	UserDAO userDao;
	
	@RequestMapping(path="/ping", method=RequestMethod.GET)
	public String ping() {
		return "Pong";
	}
	
	@RequestMapping(path="/user", method=RequestMethod.GET)
	public List<User> index() {
		return userDao.index();
	}
	
	@RequestMapping(path="/user/{id}", method=RequestMethod.GET)
	public User index(@PathVariable int id) {
		return userDao.show(id);
	}
	// get scores by user id
	@RequestMapping(path="/user/{id}/scores", method=RequestMethod.GET)
	public Set<Score> indexScores(@PathVariable int id) {
		return userDao.showScores(id);
	}
	
	// add a score to user id and quiz id
	@RequestMapping(path="/user/{id}/scores/{qId}", method=RequestMethod.POST)
	public void createScore(@RequestBody String userJSON, @PathVariable int id, 
							@PathVariable int qId, HttpServletResponse response) {
		ObjectMapper mapper = new ObjectMapper();
		Score score = null;
		try {
			score = mapper.readValue(userJSON, Score.class);
			response.setStatus(201);
			userDao.createScore(score, id, qId);
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus(400);
		}
	}
	
	@RequestMapping(path="/user", method=RequestMethod.POST)
	public void create(@RequestBody String userJSON, HttpServletResponse response) {
		ObjectMapper mapper = new ObjectMapper();
		User user = null;
		try {
			user = mapper.readValue(userJSON, User.class);
			response.setStatus(201);
			userDao.create(user);
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus(400);
		}
	}
	
	@RequestMapping(path="/user/{id}", method=RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody String userJSON, HttpServletResponse response) {
		ObjectMapper mapper = new ObjectMapper();
		User user = null;
		try {
			user = mapper.readValue(userJSON, User.class);
			response.setStatus(201);
			userDao.update(user, id);
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus(400);
		}
	}
	
	@RequestMapping(path="/user/{id}", method=RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		userDao.delete(id);
	}
	
	
	
	
}
