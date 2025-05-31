package com.ayu.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ayu.Model.Employee;
import com.ayu.Model.User;
import com.ayu.Repository.EmployeeRepository;
import com.ayu.Repository.UserRepository;

@Controller
public class LoginController {
	
	@Autowired
	UserRepository repo;
	@Autowired
	EmployeeRepository empRepo;
	
	@GetMapping("/logging")
	public String userLogin()
	{
		return "Login";
	}
	
	@PostMapping("/loginPage")
	public String loginCheck(@RequestParam("username")String username,
							@RequestParam("password") String password,
							RedirectAttributes redirect,
							Model model){
		
		User user = repo.getByUserName(username);
		
		if(user == null){
			redirect.addFlashAttribute("error", "User Not Found");
			return "redirect:/login";
		}
		
		
		if(user != null && password.equals(user.getPassword()))
		{
			List<Employee> emp = empRepo.findAll();
			model.addAttribute("emp",emp);
			return "Dashboard";			
		}else {
			redirect.addFlashAttribute("error", "Wrong Password");
			return "redirect:/login";
		}
		}
	
		@GetMapping("/login")
		public String showLoginPage() {
		    return "Login";  // ye Login.jsp ya Login.html page ka naam hai
		}

}
