package com.emp.emp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.emp.emp.entity.employe;
import com.emp.emp.service.EmailService;
import com.emp.emp.service.EmpService;

import jakarta.mail.MessagingException;

@Controller
public class controller {

	@Autowired
	private EmpService empService;
	
	@Autowired
	private EmailService emailService;


	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/upload")
	public String upload() {
		return "upload";
	}

	@GetMapping("/mail")
	public String mail() {
		return "sendMail";
	}

	@PostMapping("/upload/file")
	public ResponseEntity<String> uploadExcelFile(@RequestParam("file") MultipartFile file) {
		try {
			empService.saveDataFromExcelFile(file);
			return ResponseEntity.ok("Data saved successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving data: " + e.getMessage());
		}
	}

	@GetMapping("/emplist")
	public String showEmpList(Model model) {
		List<employe> emp = empService.getAllEmployees();
		model.addAttribute("emp", emp);

		return "showEmp";

	}

	@GetMapping("/approve/{id}")
	public String approveEmp(@PathVariable int id) {
		employe existingEmp = empService.getEmpById(id);
		empService.updateEmp(id, "Approved");
		return "redirect:/emplist";
	}

	@GetMapping("/reject/{id}")
	public String rejectEmp(@PathVariable int id) {
		employe existingEmp = empService.getEmpById(id);
		empService.updateEmp(id, "Reject");
		return "redirect:/emplist";
	}

	@PostMapping("/sendAppEmp")
	public String sendApprovedEmployee(@RequestParam("email") String email) throws MessagingException {
		
		List<employe> approvedEmp=empService.getApprovedEmp();
		
		StringBuilder emailBody = new StringBuilder("<h1>Approved Employee</h1>");
		emailBody.append("<table border='1'><tr><th>Id</th><th>Name</th><th>age</th><th>addr1</th><th>addr2</th><th>City</th><th>State</th><th>Country</th><th>Pincode</th><th>Project</th><th>ManagerId</th><th>Date</th><th>LoginTime</th><th>LogoutTime</th><th>Status</th></tr>");
		
		for(employe emp: approvedEmp) {
			emailBody.append("<tr>")
			.append("<td>").append(emp.getId()).append("</td>")
			.append("<td>").append(emp.getName()).append("</td>")
			.append("<td>").append(emp.getAge()).append("</td>")
			.append("<td>").append(emp.getAddr1()).append("</td>")
			.append("<td>").append(emp.getAddr2()).append("</td>")
			.append("<td>").append(emp.getCity()).append("</td>")
			.append("<td>").append(emp.getState()).append("</td>")
			.append("<td>").append(emp.getCountry()).append("</td>")
			.append("<td>").append(emp.getPincode()).append("</td>")
			.append("<td>").append(emp.getProject()).append("</td>")
			.append("<td>").append(emp.getManagerId()).append("</td>")
			.append("<td>").append(emp.getDate()).append("</td>")
			.append("<td>").append(emp.getLoginTime()).append("</td>")
			.append("<td>").append(emp.getLogoutTime()).append("</td>")
			.append("<td>").append(emp.getApproved()).append("</td>")
			.append("/tr");
			
		}
		emailBody.append("</table>");
		
		emailService.sendApprovedEmpEmail(email, "Approved Employees List", emailBody.toString());
		
		return "redirect:/emplist";
		
	}
}
