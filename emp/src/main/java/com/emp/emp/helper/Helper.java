package com.emp.emp.helper;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.emp.emp.entity.employe;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Helper {

	public static String[] HEADERS = { "Id", "name", "age", "addr1","addr2","city","state","country","pincode",
			"project","managerId","date","loginTime","logoutTime","approved"

	};

	public static String SHEET_NAME = "empList";

	private static Cell createCell;

	public static ByteArrayInputStream dataToExcel(List<employe> list) throws IOException {

		// create work book
		Workbook workbook = new XSSFWorkbook();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {

			// create sheet
			Sheet sheet = workbook.createSheet(SHEET_NAME);

			// create row : header row
			Row row = sheet.createRow(0);

			for (int i = 0; i < HEADERS.length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellValue(HEADERS[i]);
			}

			// value row

			int rowIndex = 1;
			for (employe e : list) {

				Row dataRow = sheet.createRow(rowIndex);
				rowIndex++;

				dataRow.createCell(0).setCellValue(e.getId());
				dataRow.createCell(1).setCellValue(e.getName());
				dataRow.createCell(2).setCellValue(e.getAge());
				dataRow.createCell(3).setCellValue(e.getAddr1());
				dataRow.createCell(4).setCellValue(e.getAddr2());
				dataRow.createCell(5).setCellValue(e.getCity());
				dataRow.createCell(6).setCellValue(e.getState());
				dataRow.createCell(7).setCellValue(e.getCountry());
				dataRow.createCell(8).setCellValue(e.getPincode());
				dataRow.createCell(9).setCellValue(e.getProject());
				dataRow.createCell(10).setCellValue(e.getManagerId());
				dataRow.createCell(11).setCellValue(e.getDate());
				dataRow.createCell(12).setCellValue(e.getLoginTime());
				dataRow.createCell(13).setCellValue(e.getLogoutTime());
				dataRow.createCell(14).setCellValue(e.getApproved());
			}

			workbook.write(out);

			return new ByteArrayInputStream(out.toByteArray());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Fail to import data excel");
		}finally {
			workbook.close();
			out.close();
		}
		return null;

	}


	// check that file is of excel type or not
	public static boolean checkExcelFormat(MultipartFile file) {

		String contentType = file.getContentType();

		if (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
			return true;
		} else {
			return false;
		}

	}

	// convert excel to list of products

	public static List<employe> convertExcelToListOfProduct(InputStream is) {
		List<employe> list = new ArrayList<>();

		try {

			XSSFWorkbook workbook = new XSSFWorkbook(is);

			XSSFSheet sheet = workbook.getSheet("empList");

			int rowNumber = 0;
			Iterator<Row> iterator = sheet.iterator();

			while (iterator.hasNext()) {
				Row row = iterator.next();

				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cells = row.iterator();

				int cid = 0;

				employe e = new employe();

				while (cells.hasNext()) {
					Cell cell = cells.next();

					switch (cid) {
					case 0:
						e.setId((int) cell.getNumericCellValue());
						break;
					case 1:
						e.setName(cell.getStringCellValue());
						break;
					case 2:
						e.setAge((int) cell.getNumericCellValue());
						break;
					case 3:
						e.setAddr1(cell.getStringCellValue());
						break;
					case 4:
						e.setAddr2(cell.getStringCellValue());
						break;
					case 5:
						e.setCity(cell.getStringCellValue());
						break;
					case 6:
						e.setCountry(cell.getStringCellValue());
						break;
					case 7:
						e.setPincode((int) cell.getNumericCellValue());
						break;
					case 8:
						e.setProject(cell.getStringCellValue());
						break;
					case 9:
						e.setManagerId((int) cell.getNumericCellValue());
						break;
					case 10:
						e.setDate(cell.getStringCellValue());
						break;
					case 11:
						e.setLoginTime(cell.getStringCellValue());
						break;
					case 12:
						e.setLogoutTime(cell.getStringCellValue());
						break;
					case 13:
						e.setApproved(cell.getStringCellValue());
						break;
						
					default:
						break;
					}
					cid++;

				}

				list.add(e);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

}
