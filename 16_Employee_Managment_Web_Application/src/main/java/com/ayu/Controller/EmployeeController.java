package com.ayu.Controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.ayu.Service.EmployeeService;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
	
	@Autowired
	EmployeeService service;
	
	@GetMapping("/addPage")
	public String viewPage()
	{
		return "newUser";
	}
	
	@GetMapping("/addEmployee")
	public String addUserPage()
	{
		return "newUser";
	}
	
	@PostMapping("/addUser")
	public String addEmployee(@ModelAttribute("EmployeeUserDTO")EmployeeUserDTO dto,
							  @RequestParam("idProofFile")MultipartFile file,
							  RedirectAttributes redirect)throws IOException {
		if(file != null && !file.isEmpty())
		{
			dto.getEmployee().setIdProofFileName(file.getName());
			dto.getEmployee().setIdProofFile(file.getBytes());
		}
		try {
			Employee emp = service.saveEmployeeWithUser(dto);
			redirect.addFlashAttribute("successMessage", "Employee " + emp.getFirstName() + " saved successfully!");
		} catch (Exception e) {
			redirect.addFlashAttribute("errorMessage", "Something went wrong: " + e.getMessage());
		}
		return "newUser";
	}
	
	@Autowired
    private EmployeeRepository repo;

	@GetMapping("/details")
	public String getAllEmployees(Model model) {
	    List<Employee> employees = repo.findAll();

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
		Employee emp = repo.findById(id).orElseThrow(()->new RuntimeException("File Not Found"));
		model.addAttribute("emp",emp);
		if(emp.getIdProofFile() != null && emp.getIdProofFile().length > 0)
		{
			String base64 = Base64.getEncoder().encodeToString(emp.getIdProofFile());
			model.addAttribute("file",base64);
		}
		
		return "employeeDetails";
	}

	
}
