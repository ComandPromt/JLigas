import java.io.FileOutputStream;
import java.io.OutputStream;

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

	public static void createXlsFromHtmlTables(String htmlContent, String filePath) throws Exception {

		Workbook workbook = new HSSFWorkbook();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = factory.newDocumentBuilder();

		Document document = builder.parse(new InputSource(new java.io.StringReader(makeHtmlWellFormed(htmlContent))));

		NodeList tableList = document.getElementsByTagName("table");

		for (int i = 0; i < tableList.getLength(); i++) {

			Node tableNode = tableList.item(i);

			if (tableNode.getNodeType() == Node.ELEMENT_NODE) {

				Element tableElement = (Element) tableNode;

				Sheet sheet = workbook.createSheet("JORNADA " + (i + 1));

				fillSheetWithHtmlTable(sheet, tableElement);

			}

		}

		try (OutputStream fileOut = new FileOutputStream(filePath)) {

			workbook.write(fileOut);

		}

		workbook.close();

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

	private static String makeHtmlWellFormed(String htmlContent) {

		htmlContent = htmlContent.replaceAll("<br>", "<br/>");

		htmlContent = htmlContent.replaceAll("&nbsp;", "&#160;");

		htmlContent = htmlContent.replaceAll("&(?!#?[a-zA-Z0-9]+;)", "&amp;");

		return "<!DOCTYPE html><html>" + htmlContent + "</html>";

	}

}
