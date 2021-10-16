package com.smart.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.dao.ContactRepositiry;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;

@RestController
public class SearchController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ContactRepositiry contactRepo;

	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal, Model m) {
		User userName = userRepo.getUserByUserName(principal.getName());
		List<Contact> contacts = contactRepo.findByNameContainingAndUser(query, userName);
		return ResponseEntity.ok(contacts);
	}
}
