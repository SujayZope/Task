package com.emp.emp.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.emp.emp.entity.employe;
import com.emp.emp.helper.Helper;
import com.emp.emp.repo.EmpRepo;

@Service
public class EmpService {

	@Autowired
	private EmpRepo empRepo;
	
	public ByteArrayInputStream getActualData() throws IOException {
		List<employe> all = empRepo.findAll();

		ByteArrayInputStream byteArrayInputStream = Helper.dataToExcel(all);
		return byteArrayInputStream;
	}

	public void save(MultipartFile file) {

		try {
			List<employe> emps = Helper.convertExcelToListOfProduct(file.getInputStream());
			this.empRepo.saveAll(emps);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	
}
