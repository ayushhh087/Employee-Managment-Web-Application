package com.ayu.Controller;

import java.awt.print.Pageable;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ayu.DTO.EmployeeUserDTO;
import com.ayu.Model.Employee;
import com.ayu.Repository.EmployeeRepository;
import com.ayu.Repository.UserRepository;
import com.ayu.Service.EmailService;
import com.ayu.Service.EmployeeService;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

	
	@Autowired
	EmployeeService service;

	@Autowired
	EmailService mailSender;

	@Autowired
	private EmployeeRepository empRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@PostMapping("/addUser")
	public String addEmployee(@ModelAttribute("EmployeeUserDTO")EmployeeUserDTO dto,
							  @RequestParam("idProofFile")MultipartFile file,
							  RedirectAttributes redirect)throws IOException {
		if(file != null && !file.isEmpty() && file.getSize() > 0)
		{
			dto.getEmployee().setIdProofFileName(file.getOriginalFilename());
			dto.getEmployee().setIdProofFile(file.getBytes());
			System.out.println("Yha tk toh aya h aage ka dekhte h...");
		}
		try {
			Employee emp = service.saveEmployeeWithUser(dto);
			if(emp.getDepartment().equals("HR"))
			{
				String mail = emp.getEmail();
				String subject = "Welcome to the Organization - Your Credentials";

				String body = "<div style='font-family: Arial, sans-serif; background-color: #1e1e2f; color: #f5f5f5; padding: 20px; border-radius: 10px;'>"
				           + "<h2 style='color: #00c6ff;'>Welcome " + emp.getFirstName() + "!</h2>"
				           + "<p>Thank you for registering with our organization.</p>"
				           + "<p><strong>Username:</strong> " + dto.getUser().getUsername() + "<br>"
				           + "<strong>Password:</strong> " + dto.getUser().getPassword() + "</p>"
				           + "<p style='font-size: 14px; color: #bbbbbb;'>Please keep this information confidential.</p>"
				           + "<hr style='border: 0; border-top: 1px solid #444;' />"
				           + "<p style='color: #aaaaaa;'>Regards,<br>HR Team</p>"
				           + "</div>";
				if(mailSender.sendMail(mail, subject, body)) {
				    System.out.println("✅ Mail Sent Successfully to: " + mail);
				} else {
				    System.out.println("❌ Failed to send mail to: " + mail);
				}

			}
			redirect.addFlashAttribute("successMessage", "Employee " + emp.getFirstName() + " saved successfully!");
		} catch (Exception e) {
			redirect.addFlashAttribute("errorMessage", "Something went wrong. Please try again.");
		}
		return "redirect:/employee/addEmployee";
	}
	
	@GetMapping("/addEmployee")
	public String addUserPage()
	{
		return "newUser";
	}
	
	
	//Dashboard page pe data dikhane k liy
	@GetMapping("/details")
	public String getAllEmployees(Model model) {
	    List<Employee> employees = empRepo.findAll();

	    // Prepare base64 encoded PDFs for each employee
	    Map<Long, String> pdfBase64Map = new HashMap<>();
	    for (Employee emp : employees) {
	        if (emp.getIdProofFile() != null && emp.getIdProofFile().length > 0) {
	            String base64 = Base64.getEncoder().encodeToString(emp.getIdProofFile());
	            pdfBase64Map.put(emp.getId(), base64);
	        }
	    }

	    model.addAttribute("employees", employees);
	    model.addAttribute("pdfBase64Map", pdfBase64Map);

	    return "employeeDetails";
	}
	
	//Method for displaying the doc of emp
	@GetMapping("/view-id-proof/{id}")
	public String getDoc(@PathVariable long id,Model model)
	{
		Employee emp = empRepo.findById(id).orElseThrow(()->new RuntimeException("File Not Found"));
		model.addAttribute("emp",emp);
		if(emp.getIdProofFile() != null && emp.getIdProofFile().length > 0)
		{
			String base64 = Base64.getEncoder().encodeToString(emp.getIdProofFile());
			model.addAttribute("file",base64);
		}
		
		return "employeeDetails";
	}
	//Dashboard pe redirect krn k liy
	@GetMapping("/showDashboard")
	public String getToDashboard(Model model,
								@RequestParam(defaultValue = "0") int pageNo,
								@RequestParam(defaultValue = "5") int size,
								@RequestParam("sortBy")String sortBy)
	{
		if(sortBy.equalsIgnoreCase("name"))
		{
			List<Employee> emp = empRepo.findAllByOrderByFirstNameDesc();
			model.addAttribute("emp",emp);			
		}else if(sortBy.equalsIgnoreCase("salary"))
		{
			List<Employee> emp = empRepo.findAllByOrderBySalaryDesc();
			model.addAttribute("emp",emp);			
		}
		
		
//		Pageable pageable = PageRequest.of(pageNo, size);
		
		
		return "Dashboard";
	}
	//id ke help se input data delete krn k liy
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable Long id,RedirectAttributes redirect)
	{
		try {	
			empRepo.deleteById(id);
//			userRepo.deleteById(id);
			redirect.addFlashAttribute("msg", "Deleted Succesfully");
		} catch (Exception e) {
			redirect.addFlashAttribute("msg", "Something went wrong");
			// TODO: handle exception
		}
		
		return "redirect:/employee/showDashboard";
	}
	
	//Sorting krn k liy
//	@

	
}
