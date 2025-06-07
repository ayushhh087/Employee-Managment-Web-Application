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
import com.ayu.Service.EmailService;
import com.ayu.Service.EmployeeService;

@Controller
public class LoginController {
	
	@Autowired
	UserRepository repo;
	@Autowired
	EmployeeRepository empRepo;
	@Autowired
	EmployeeService service;
	@Autowired
	EmailService mail;
//	@GetMapping("/logging")
//	public String userLogin()
//	{
//		return "Login";
//	}
	@GetMapping("/login")
	public String showLoginPage() {
		return "Login";  // ye Login.jsp ya Login.html page ka naam hai
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
	
		@GetMapping("/logout")
		public String logOutPage()
		{
			return "Login";
		}
		
		@GetMapping("/forget-password")
		public String goToForgetPasswordPage()
		{
			return "Forget-Password";
		}
		
		@PostMapping("/reset-password")
		public String resetPassword(@RequestParam String email,RedirectAttributes redirect)
		{
			try {
				Employee emp = service.findByEmailID(email.toLowerCase());
				System.out.println(emp.toString());
				if(emp !=null && emp.getUser() !=null)
				{
					String subject = "Your Account Credentials - HRMS System";

					String body = "<div style='font-family: Arial, sans-serif; font-size: 14px; color: #333;'>"
					        + "<p>Dear <strong>" + emp.getFirstName() + "</strong>,</p>"
					        + "<p>We received a request to retrieve your account credentials.</p>"
					        + "<p><strong>Username:</strong> " + emp.getUser().getUsername() + "<br>"
					        + "<strong>Password:</strong> " + emp.getUser().getPassword() + "</p>"
					        + "<p style='color: #c0392b;'><em>For security reasons, please change your password after logging in.</em></p>"
					        + "<p>If you did not request this, please contact the administrator immediately.</p>"
					        + "<br><p>Regards,<br><strong>HRMS Support Team</strong></p>"
					        + "</div>";

					if(mail.sendMail(email, subject, body))
					{
						redirect.addFlashAttribute("msg", "Mail Sent Successfully!!");				
					}else {
						redirect.addFlashAttribute("msg", "Email does not exist");
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				redirect.addFlashAttribute("msg", "Something went wrong!");
			}
			return "redirect:/forget-password";
		}
}
