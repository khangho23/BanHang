package com.fpoly.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fpoly.entity.ProductType;
import com.fpoly.entity.User;
import com.fpoly.repository.ProductTypeRepository;
import com.fpoly.repository.UserRepository;
import com.fpoly.service.MailService;
import com.fpoly.service.UserService;
import com.fpoly.utility.CookieUtility;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/home")
public class HomeController {
	@Autowired
	ProductTypeRepository pdtResp;
	@Autowired
	UserRepository userResp;
	@Autowired
	CookieUtility cookie;
	@Autowired
	UserService userService;

	@Autowired
	HttpSession session;

	@Autowired
	MailService mailservice;

	@Autowired
	HttpServletRequest req;

	@GetMapping("/index")
	public String homePage(Model model) {
		System.out.println(cookie.getValue("userId"));
		return "index";
	}

	@GetMapping("/contact")
	public String contact() {
		return "contact";
	}

	@GetMapping("/checkout")
	public String checkout() {
		return "checkout";
	}

	@GetMapping("/edit")
	public String edit(Model model, @CookieValue(name = "userId") Integer userId) {
		User user = userService.edit(userId);
		if (user != null) {
			model.addAttribute("userProfile", user);
			return "edit";
		}
		return "redirect:/home/index";
	}

	@PostMapping("/edit")
	public String edit(User userProfile, @CookieValue(name = "userId") Integer userId) {
		if(userProfile != null) {
			Optional<User> user = userResp.findById(userId);
			user.get().setFullname(userProfile.getFullname());
			user.get().setAddress(userProfile.getAddress());
			user.get().setNumberPhone(userProfile.getNumberPhone());
			userResp.save(user.get());			
		}
		return "redirect:/home/edit";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@PostMapping("/login")
	public String checkLogin(@RequestParam("username") String username, @RequestParam("password") String password) {
		boolean checkLogin = userService.login(username, password);
		if (checkLogin == false) {
			return "login";
		}
		return "redirect:/home/index";
	}

	@GetMapping("/logout")
	public String logout() {
		userService.logout();
		return "redirect:/home/index";
	}

	@GetMapping("/register")
	public String signup(Model model) {
		return "register";
	}

	@PostMapping("/register")
	public String signupBtn(@ModelAttribute User user) throws IOException, MessagingException {
		String code = "";
		for (int i = 0; i < 6; i++) {
			String ranNum = String.valueOf(ThreadLocalRandom.current().nextInt(0, 9));
			code = code + ranNum;
		}
		if (!user.equals(null)) {
			session.setAttribute("numberCode", code);
			session.setAttribute("user", user);
			mailservice.send(user.getEmail(), code);

		}
		return "redirect:/home/register/auth";
	}

	@GetMapping("/register/auth")
	public String auth() {
		return "authentication";
	}

	@PostMapping("/register/auth")
	public String autho(@RequestParam("ma") String ma, Model model) {
		System.out.println(session.getAttribute("numberCode"));
		if (ma.equals(session.getAttribute("numberCode"))) {
			User user = (User) session.getAttribute("user");
			userService.save(user);
			return "redirect:/index";
		}
		model.addAttribute("message", "Mã xác thực chưa chính xác!");
		return "authentication";
	}

	@ModelAttribute("categories")
	public List<ProductType> getCategories() {
		List<ProductType> list = pdtResp.findAll();
		return list;
	}

	@ModelAttribute("userId")
	public String getUserId() {
		return cookie.getValue("userId");
	}
}
