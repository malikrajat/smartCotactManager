package com.smart.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepo;

	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home - Smart Contact Manager");
		return "home";
	}

	@RequestMapping("/signup")
	public String singnup(Model model) {
		model.addAttribute("title", "Sign Up - Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}

	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("user") User user, BindingResult validation,
			@RequestParam(value = "term", defaultValue = "false") boolean term, Model model, HttpSession session) {

		try {
			if (!term) {
				throw new Exception("Please accept term and consition.");
			}
			if (validation.hasErrors()) {
				model.addAttribute("user", user);
				return "signup";
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImage("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userRepo.save(user);
			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("You have registred !!", "alert-success"));
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong " + e.getMessage(), "alert-danger"));
		}

		return "signup";
	}

	@GetMapping("/singin")
	public String login(Model model) {
		model.addAttribute("title", "Login Here");
		return "login";
	}
	
	@PostMapping("/loginValidate")
	public String loginValidate() {
		return "login"; 
	}
	
}
