package com.api.helper;

import com.api.entity.Product;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Helper {

	public static String[] HEADERS = { "productId", "productName", "productDesc", "productPrice"

	};

	public static String SHEET_NAME = "productList";

	private static Cell createCell;

	public static ByteArrayInputStream dataToExcel(List<Product> list) throws IOException {

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
			for (Product c : list) {

				Row dataRow = sheet.createRow(rowIndex);
				rowIndex++;

				dataRow.createCell(0).setCellValue(c.getProductId());
				dataRow.createCell(1).setCellValue(c.getProductName());
				dataRow.createCell(2).setCellValue(c.getProductDesc());
				dataRow.createCell(3).setCellValue(c.getProductPrice());
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

	public static List<Product> convertExcelToListOfProduct(InputStream is) {
		List<Product> list = new ArrayList<>();

		try {

			XSSFWorkbook workbook = new XSSFWorkbook(is);

			XSSFSheet sheet = workbook.getSheet("productList");

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

				Product p = new Product();

				while (cells.hasNext()) {
					Cell cell = cells.next();

					switch (cid) {
					case 0:
						p.setProductId((int) cell.getNumericCellValue());
						break;
					case 1:
						p.setProductName(cell.getStringCellValue());
						break;
					case 2:
						p.setProductDesc(cell.getStringCellValue());
						break;
					case 3:
						p.setProductPrice(cell.getNumericCellValue());
						break;
					default:
						break;
					}
					cid++;

				}

				list.add(p);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

}
