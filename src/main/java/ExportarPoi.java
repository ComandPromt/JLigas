import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class ExportarPoi {

	public static void createXlsFromHtmlTables(String htmlContent, String filePath, boolean separarHojas)
			throws Exception {

		Workbook workbook = new HSSFWorkbook();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = factory.newDocumentBuilder();

		Document document = builder.parse(new InputSource(new StringReader(makeHtmlWellFormed(htmlContent))));

		NodeList tableList = document.getElementsByTagName("table");

		if (separarHojas) {

			Node tableNode;

			Element tableElement;

			Sheet sheet;

			for (int i = 0; i < tableList.getLength(); i++) {

				tableNode = tableList.item(i);

				if (tableNode.getNodeType() == Node.ELEMENT_NODE) {

					tableElement = (Element) tableNode;

					sheet = workbook.createSheet("JORNADA " + (i + 1));

					fillSheetWithHtmlTable(sheet, tableElement);

				}

			}

		}

		else {

			Sheet sheet = workbook.createSheet("JORNADAS");

			int currentRow = 0;

			Row separatorRow;

			Cell separatorCell;

			Node tableNode;

			Element tableElement;

			for (int i = 0; i < tableList.getLength(); i++) {

				currentRow += 2;

				separatorRow = sheet.createRow(currentRow++);

				separatorCell = separatorRow.createCell(0);

				separatorCell.setCellValue("JORNADA " + (i + 1));

				sheet.addMergedRegion(
						new org.apache.poi.ss.util.CellRangeAddress(currentRow - 1, currentRow - 1, 0, 10));

				tableNode = tableList.item(i);

				if (tableNode.getNodeType() == Node.ELEMENT_NODE) {

					tableElement = (Element) tableNode;

					currentRow = fillSheetWithHtmlTable(sheet, tableElement, currentRow);

				}

			}

		}

		try (OutputStream fileOut = new FileOutputStream(filePath)) {

			workbook.write(fileOut);

		}

		workbook.close();

	}

	private static String makeHtmlWellFormed(String htmlContent) {

		htmlContent = htmlContent.replaceAll("<br>", "<br/>");

		htmlContent = htmlContent.replaceAll("&nbsp;", "&#160;");

		htmlContent = htmlContent.replaceAll("&(?!#?[a-zA-Z0-9]+;)", "&amp;");

		return "<!DOCTYPE html><html>" + htmlContent + "</html>";

	}

	private static int fillSheetWithHtmlTable(Sheet sheet, Element tableElement, int startRow) {

		NodeList trList = tableElement.getElementsByTagName("tr");

		int currentRow = startRow;

		Node trNode;

		Element trElement;

		Row row;

		NodeList tdList;

		int cellIndex;

		Node tdNode;

		Element tdElement;

		Cell cell;

		for (int i = 0; i < trList.getLength(); i++) {

			trNode = trList.item(i);

			if (trNode.getNodeType() == Node.ELEMENT_NODE) {

				trElement = (Element) trNode;

				row = sheet.createRow(currentRow++);

				tdList = trElement.getElementsByTagName("td");

				cellIndex = 0;

				for (int j = 0; j < tdList.getLength(); j++) {

					tdNode = tdList.item(j);

					if (tdNode.getNodeType() == Node.ELEMENT_NODE) {

						tdElement = (Element) tdNode;

						cell = row.createCell(cellIndex++);

						cell.setCellValue(tdElement.getTextContent());

					}

				}

			}

		}

		return currentRow;

	}

	private static void fillSheetWithHtmlTable(Sheet sheet, Element tableElement) {

		NodeList trList = tableElement.getElementsByTagName("tr");

		int rowIndex = 0;

		Node trNode;

		Element trElement;

		Row row;

		NodeList tdList;

		Node tdNode;

		Element tdElement;

		Cell cell;

		for (int i = 0; i < trList.getLength(); i++) {

			trNode = trList.item(i);

			if (trNode.getNodeType() == Node.ELEMENT_NODE) {

				trElement = (Element) trNode;

				row = sheet.createRow(rowIndex++);

				tdList = trElement.getElementsByTagName("td");

				int cellIndex = 0;

				for (int j = 0; j < tdList.getLength(); j++) {

					tdNode = tdList.item(j);

					if (tdNode.getNodeType() == Node.ELEMENT_NODE) {

						tdElement = (Element) tdNode;

						cell = row.createCell(cellIndex++);

						cell.setCellValue(tdElement.getTextContent());

					}
				}

			}

		}

	}
}
