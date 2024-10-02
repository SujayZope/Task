package com.emp.emp.service;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.emp.emp.entity.Employee;
import com.emp.emp.repo.EmpRepo;

@Service
public class EmpService {

	@Autowired
	private EmpRepo empRepo;

	public void saveDataFromExcelFile(MultipartFile file) throws Exception {
		try (InputStream inputStream = file.getInputStream()) {
			Workbook workbook = WorkbookFactory.create(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.rowIterator();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // For the date
			SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a"); // For the 12-hour time with AM/PM

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (row.getRowNum() == 0) {
					continue; // Skip the header row
				}

				Employee entity = new Employee();
				entity.setId(getNumericCellValue(row.getCell(0))); // ID
				entity.setName(getStringCellValue(row.getCell(1))); // Name
				entity.setAge(getNumericCellValue(row.getCell(2))); // Age
				entity.setAddr1(getStringCellValue(row.getCell(3))); // Addr1
				entity.setAddr2(getStringCellValue(row.getCell(4))); // Addr2
				entity.setCity(getStringCellValue(row.getCell(5))); // City
				entity.setState(getStringCellValue(row.getCell(6))); // State
				entity.setCountry(getStringCellValue(row.getCell(7))); // Country
				entity.setPincode(getNumericCellValue(row.getCell(8))); // Pincode
				entity.setProject(getStringCellValue(row.getCell(9))); // Project
				entity.setManagerId(getNumericCellValue(row.getCell(10))); // Manager ID

				// Handle date parsing
				Cell dateCell = row.getCell(11); // Date
				String formattedDate = null;
				if (dateCell != null && dateCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(dateCell)) {
					Date date = dateCell.getDateCellValue();
					formattedDate = dateFormat.format(date);
				} else if (dateCell != null) {
					String dateString = getStringCellValue(dateCell);
					try {
						formattedDate = dateFormat.format(dateFormat.parse(dateString));
					} catch (ParseException e) {
						throw new RuntimeException("Invalid date format: " + dateString);
					}
				}
				entity.setDate(formattedDate); // Set date

				// Handle login time parsing
				String loginTimeString = getTimeString(row.getCell(12)); // Login Time
				entity.setLoginTime(loginTimeString != null ? loginTimeString : null); // Set login time

				// Handle logout time parsing
				String logoutTimeString = getTimeString(row.getCell(13)); // Logout Time
				entity.setLogoutTime(logoutTimeString != null ? logoutTimeString : null); // Set logout time

				entity.setStatus("Pending");
				empRepo.save(entity);
			}
		}
	}

	// Helper method to get numeric cell value or null
	private Integer getNumericCellValue(Cell cell) {
		if (cell == null) {
			return null; // Return null if cell is null
		}
		if (cell.getCellType() == CellType.NUMERIC) {
			return (int) cell.getNumericCellValue(); // Cast numeric value to Integer
		}
		return null; // Return null for non-numeric cells
	}

	// Helper method to safely get a string value from a cell
	private String getStringCellValue(Cell cell) {
		if (cell == null) {
			return null; // Return null if cell is null
		}
		if (cell.getCellType() == CellType.STRING) {
			return cell.getStringCellValue();
		}
		return null; // Return null for non-string cells
	}

	// Helper method to safely get a time string value from a cell
	private String getTimeString(Cell cell) {
		if (cell == null) {
			return null; // Return null if cell is null
		}
		if (cell.getCellType() == CellType.STRING) {
			return cell.getStringCellValue();
		} else if (cell.getCellType() == CellType.NUMERIC) {
			if (DateUtil.isCellDateFormatted(cell)) {
				SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
				return timeFormat.format(cell.getDateCellValue());
			} else {
				return String.valueOf((int) cell.getNumericCellValue());
			}
		}
		return null; // Return null for other types
	}

	public List<Employee> getAllEmployees() {
		return empRepo.findAll();
	}
	
	public Optional<Employee> FindEmpById(int Id) {
		return empRepo.findById(Id);
	}

	public Employee getEmpById(int id) {
		return empRepo.findById(id).get();
	}
	
	public List<Employee> getApprovedEmp(){
		return empRepo.findByStatus("Approved");
	}
	
	public void updateEmp(int id, String status, String comment) {
		Employee emp=empRepo.findById(id).orElse(null);
		if (emp != null) {
			emp.setStatus(status);
			emp.setComments(comment); // Assuming there's a comment field in the Employee entity
			empRepo.save(emp);
		}
	}
	
}
