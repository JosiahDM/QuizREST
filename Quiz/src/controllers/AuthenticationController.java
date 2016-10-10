package controllers;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import data.UserDAO;
import entities.User;

@RestController
public class AuthenticationController {
	
	@Autowired
	UserDAO userDao;
	
	@RequestMapping(path="/auth", method = RequestMethod.POST)
	public User login(@RequestBody String userJSON, HttpServletResponse response) {
		ObjectMapper mapper = new ObjectMapper();
		User user = null;
		String username = "";
		try {
			user = mapper.readValue(userJSON, User.class);
			username = user.getUsername();
			user = userDao.login(user);
			if (user == null) {
				throw new IOException();
			}
			// Don't want to send the hashed password back to the client
			user.setPassword("");
			response.setStatus(200);
		} catch (IOException e) {
			response.setStatus(401);
			System.err.println("[" + LocalDateTime.now() + "] Auth failed for " + username);
		}
		return user;
	}
	
}
