package com.smart.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepositiry;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.service.EmailService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ContactRepositiry contactRepositiry;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private EmailService emailService;

	@ModelAttribute
	public void addCommanData(Model model, Principal principal) {
		String name = principal.getName();
		User user = userRepo.getUserByUserName(name);
		model.addAttribute("user", user);
	}

	@RequestMapping("/index")
	public String dashbaord(Model model, Principal principal) {
		model.addAttribute("title", "User Dashboard");
		return "dashboard";
	}

	@RequestMapping("/add-contact")
	public String openAddContactForm(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("conatc", new Contact());
		return "addContact";
	}

	@PostMapping("/saveContact")
	public String saveContact(@ModelAttribute Contact contact, @RequestParam("image1") MultipartFile file, Model model,
			Principal principal, HttpSession session) {
		try {
			model.addAttribute("title", "Add Contact");
			String name = principal.getName();
			User user = userRepo.getUserByUserName(name);
			contact.setUser(user);
			if (file.isEmpty()) {
				contact.setImage("contact.png");
			} else {
				contact.setImage(file.getOriginalFilename());
				File saveFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Image upload success");
				session.setAttribute("message", new Message("Saved Success", "success"));
			}
			user.getContacts().add(contact);
			userRepo.save(user);
		} catch (Exception e) {
			System.out.println("ERROR : " + e.getMessage());
			session.setAttribute("message", new Message("Error", "danger"));
			e.printStackTrace();
		}
		return "addContact";
	}

	@GetMapping("/list/{page}")
	public String showContact(@PathVariable("page") int page, Model model, HttpSession session, Principal principal) {
		model.addAttribute("title", "Show Contact");
		String nameName = principal.getName();
		User user = userRepo.getUserByUserName(nameName);
		Pageable pageRequest = PageRequest.of(page, 5);

		Page<Contact> contacts = contactRepositiry.findContactByUser(user.getId(), pageRequest);
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());

		return "showContact";
	}

	@GetMapping("/{id}/contact")
	public String detail(Model m, @PathVariable("id") int id, Principal principal) {

		String nameName = principal.getName();
		User user = userRepo.getUserByUserName(nameName);

		Optional<Contact> contact = contactRepositiry.findById(id);
		Contact contact2 = contact.get();

		if (contact2.getUser().getId() == user.getId()) {
			m.addAttribute("contact", contact2);
			m.addAttribute("title", contact2.getName());
		}
		return "details";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") int id, Model m, Principal principal, HttpSession session)
			throws IOException {

		Optional<Contact> contact = contactRepositiry.findById(id);
		Contact contact2 = contact.get();

		String nameName = principal.getName();
		User user = userRepo.getUserByUserName(nameName);

		if (contact2.getUser().getId() == user.getId()) {
			// remove img
			File saveFile = new ClassPathResource("static/img").getFile();
			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + contact2.getImage());
			Files.delete(path);
			// u need unlink user from this contact because we have cascade enabled in model
//			contact2.setUser(null);
//			contactRepositiry.delete(contact2);
			user.getContacts().remove(contact2);
			userRepo.save(user);

			session.setAttribute("message", new Message("contact delete", "success"));

		}

		return "redirect:/user/list/0";
	}

	@PostMapping("/edit/{id}")
	public String edit(@PathVariable("id") int id, Model m, Principal principal, HttpSession session) {
		m.addAttribute("title", "Update");
		Contact contact = contactRepositiry.findById(id).get();
		m.addAttribute("contact", contact);
		return "edit";
	}

	@PostMapping("/save")
	public String updateContact(@ModelAttribute Contact contact, Model model, Principal principal,
			@RequestParam("image1") MultipartFile file, HttpSession session) {

		try {
			model.addAttribute("title", "Update");
			String name = principal.getName();
			User user = userRepo.getUserByUserName(name);
			Contact contact2 = contactRepositiry.findById(contact.getId()).get();
			if (file.isEmpty()) {
				contact.setImage(contact2.getImage());
			} else {
				contact.setImage(file.getOriginalFilename());
				File saveFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Path oldImagePath = Paths.get(saveFile.getAbsolutePath() + File.separator + contact2.getImage());
				Files.delete(oldImagePath);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			}
			contact.setUser(user);
			contactRepositiry.save(contact);
			session.setAttribute("message", new Message("Contact updated", "success"));
		} catch (Exception e) {
			System.out.println("ERROR : " + e.getMessage());
			session.setAttribute("message", new Message("Error", "danger"));
			e.printStackTrace();
		}
		return "redirect:/user/" + contact.getId() + "/contact";
	}

	@GetMapping("/profile")
	public String profile(Model m, Principal principa) {
		m.addAttribute("title", "Profile");
		return "profile";
	}

	@GetMapping("/setting")
	public String setting(Model m, Principal principa) {
		m.addAttribute("title", "Setting");
		return "setting";
	}

	@PostMapping("/saveSetting")
	public String saveSetting(@RequestParam("oldpassword") String oldpassword,
			@RequestParam("password") String password, @RequestParam("cpassword") String cpassword, Model m,
			Principal principa, HttpSession session) {
		User user = userRepo.getUserByUserName(principa.getName());
		String password2 = user.getPassword();
		if (password == cpassword) {
			if (this.bCryptPasswordEncoder.matches(oldpassword, password2)) {
				user.setPassword(this.bCryptPasswordEncoder.encode(password));
				session.setAttribute("message", new Message("Password changed !!", "success"));
			} else {
				System.out.println("please enter correct old password");
				session.setAttribute("message", new Message("Please enter correct old password ", "danger"));
				return "redirect:/user/setting";
			}
		} else {
			session.setAttribute("message", new Message("Password do not match with confirm password", "danger"));
			return "redirect:/user/setting";
		}

		return "redirect:/user/index";
	}

	@GetMapping("/forgotPassword")
	public String forgotPassword(Model m, Principal principa, HttpSession session) {

		return "forgotPassword";

	}

	@PostMapping("/sendOtp")
	public String forgotsendOtp(@RequestParam("email") String email, Model m, Principal principa, HttpSession session) {
		Random random = new Random();
		int otp = random.nextInt(999999);
		System.out.println(otp);
		System.out.println(email);
		String message = "<h1>" + otp + "</h1>";
		// Did you switch on less secure app from gmail account
		// https://myaccount.google.com/lesssecureapps
		boolean sendEmail = this.emailService.sendEmail(message, "OTP", email);
		if (sendEmail) {
			session.setAttribute("myOtp", otp);
			session.setAttribute("email", email);
			session.setAttribute("message",
					new Message("OTP is send successfully, Please check email !! .", "success"));
			return "sendOtp";
		} else {
			session.setAttribute("message", new Message("Check for email .", "danger"));
			return "forgotPassword";
		}

	}

	@PostMapping("/verifyOtp")
	public String verifyOtp(@RequestParam("otp") int otp, Model m, Principal principa, HttpSession session) {

		Integer myotp = (int) session.getAttribute("myOtp");
		if (otp == myotp) {
			String myemail = (String) session.getAttribute("email");
			User user = userRepo.getUserByUserName(myemail);
			if(user == null) {
				session.setAttribute("message", new Message("Use dos not exits. Pelase check your email.", "danger"));
				return "forgotPassword";
			} else {
				return "changePassword";
			}
			
		} else {
			session.setAttribute("message", new Message(" You have entered wrong OTP !.", "danger"));
			return "sendOtp";

		}

	}

	@PostMapping("/changePasssword")
	public String changePasssword(@RequestParam("password") String password,
			@RequestParam("cpassword") String cpassword, Model m, Principal principa, HttpSession session) {

		if (password == cpassword) {
			String myemail = (String) session.getAttribute("email");
			User user = userRepo.getUserByUserName(myemail);
			String password2 = user.getPassword();

			user.setPassword(this.bCryptPasswordEncoder.encode(password));
			session.setAttribute("message", new Message("Password changed !!", "success"));
			
			return "redirect:/user/index";
		} else {
			System.out.println("please enter correct old password");
			session.setAttribute("message", new Message("Please enter correct password and confirm password", "danger"));
			return "changePassword";
		}

	}

}
