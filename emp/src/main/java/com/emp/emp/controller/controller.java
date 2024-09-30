package com.emp.emp.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.emp.emp.helper.Helper;
import com.emp.emp.service.EmpService;



@Controller
public class controller {
	
	@Autowired
	private EmpService empService;

	@GetMapping("/")
	public String index() {
		return "index";
	}
	@GetMapping("/upload")
	public String upload() {
		return "upload";
	}
	
	@GetMapping("/excel")
	public ResponseEntity<Resource> download() throws IOException {

		String filename = "empList.xlsx";

		ByteArrayInputStream actualData = empService.getActualData();
		InputStreamResource file = new InputStreamResource(actualData);

		ResponseEntity<Resource> body = ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename= " + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);

		return body;
	}
	
	@PostMapping("/upload/file")
	public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
		if (Helper.checkExcelFormat(file)) {
			// true

			this.empService.save(file);

			return ResponseEntity.ok(Map.of("message", "File is uploaded and data is saved to db"));

		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload excel file ");
	}
	
}
