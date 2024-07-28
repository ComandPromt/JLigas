import java.io.FileOutputStream;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import mthos.JMthos;

public abstract class MetodosPrograma {

	/**
	 * Encuentra el contenido de los primeros dos
	 * <tr>
	 * en una tabla con un ID específico.
	 * 
	 * @param html    El código HTML completo que contiene la tabla.
	 * @param tableId El ID de la tabla donde se buscarán los
	 *                <tr>
	 *                .
	 * @return Un arreglo de tamaño 2 con los contenidos de los primeros dos
	 *         <tr>
	 *         si se encuentran, o mensajes de error si no se encuentran.
	 */
	public static String[] encontrarPrimerosDosTr(String html, String tableId) {
		String[] trs = new String[2];

		// Crear un patrón para encontrar la tabla con el id especificado
		String tablePatternString = "<table[^>]*\\bid\\s*=\\s*['\"]" + tableId + "['\"][^>]*>";
		Pattern tablePattern = Pattern.compile(tablePatternString, Pattern.CASE_INSENSITIVE);
		Matcher tableMatcher = tablePattern.matcher(html);

		if (!tableMatcher.find()) {
			trs[0] = "No se encontró la tabla con el id '" + tableId + "'.";
			return trs;
		}

		int tableStartIndex = tableMatcher.start();
		int tableEndIndex = html.indexOf("</table>", tableStartIndex);

		if (tableEndIndex == -1) {
			trs[0] = "No se encontró el final de la tabla con el id '" + tableId + "'.";
			return trs;
		}

		// Extraer el contenido de la tabla
		String tableContent = html.substring(tableStartIndex, tableEndIndex + "</table>".length());

		// Buscar los primeros dos <tr>
		String trPatternString = "<tr[^>]*>";
		Pattern trPattern = Pattern.compile(trPatternString, Pattern.CASE_INSENSITIVE);
		Matcher trMatcher = trPattern.matcher(tableContent);

		for (int i = 0; i < 2; i++) {
			if (!trMatcher.find()) {
				trs[i] = "No se encontró el " + (i == 0 ? "primer" : "segundo") + " <tr> en la tabla.";
				break;
			}

			int trStartIndex = trMatcher.start();
			int trEndIndex = tableContent.indexOf("</tr>", trStartIndex);

			if (trEndIndex == -1) {
				trs[i] = "No se encontró el final del " + (i == 0 ? "primer" : "segundo") + " <tr> en la tabla.";
				break;
			}

			trs[i] = tableContent.substring(trStartIndex, trEndIndex + "</tr>".length());
		}

		return trs;
	}

	/**
	 * Encuentra el contenido de un
	 * <td>con un ID específico dentro de un
	 * <tr>
	 * .
	 * 
	 * @param trContent El contenido del
	 *                  <tr>
	 *                  donde se buscará el
	 *                  <td>.
	 * @param tdId      El ID del
	 *                  <td>que se desea seleccionar.
	 * @return El contenido del
	 *         <td>si se encuentra, o un mensaje de error si no se encuentra.
	 */

	public static String encontrarTdPorId(String trContent, String tdId) {

		String tdPatternString = "<td[^>]*\\bid\\s*=\\s*['\"]" + tdId + "['\"][^>]*>";

		Pattern tdPattern = Pattern.compile(tdPatternString, Pattern.CASE_INSENSITIVE);

		Matcher tdMatcher = tdPattern.matcher(trContent);

		if (!tdMatcher.find()) {

			return "No se encontró el <td> con el id '" + tdId + "' en el <tr>.";

		}

		int tdStartIndex = tdMatcher.end();

		int tdEndIndex = trContent.indexOf("</td>", tdStartIndex);

		if (tdEndIndex == -1) {

			return "No se encontró el final del <td> con el id '" + tdId + "'.";

		}

		return trContent.substring(tdStartIndex, tdEndIndex).trim();

	}

	/**
	 * Encuentra y devuelve el contenido del
	 * <td>con el id 'selectName' en el primer
	 * <tr>
	 * de la tabla con el id 'Table2'. Si no se encuentra en el primer
	 * <tr>
	 * , se busca en el segundo
	 * <tr>
	 * .
	 * 
	 * @param html    El código HTML completo que contiene la tabla.
	 * @param tableId El ID de la tabla donde se buscarán los
	 *                <tr>
	 *                y el
	 *                <td>.
	 * @param tdId    El ID del
	 *                <td>que se desea encontrar.
	 * @return El contenido del
	 *         <td>si se encuentra, o un mensaje de error si no se encuentra.
	 */

	public static String obtenerValorTd(String html, String tableId, String tdId) {

		String[] trs = encontrarPrimerosDosTr(html, tableId);

		if (!trs[0].startsWith("No se encontró")) {

			String tdContent = encontrarTdPorId(trs[0], tdId);

			if (tdContent.startsWith("No se encontró")) {

				if (!trs[1].startsWith("No se encontró")) {

					tdContent = encontrarTdPorId(trs[1], tdId);

				}

			}

			return tdContent;

		}

		else {

			return trs[0];

		}

	}

	static void mirarEnBola(String url, String carpeta, String fecha, WebDriver driver) {

		try {

			Thread.sleep(1500);

		}

		catch (InterruptedException e) {

		}

		String pageSource = driver.getPageSource();

		String nombreArchivo = JMthos.obtenerValorEtiqueta("title", pageSource).trim().replace(" ", "-").replace(",",
				"-");

		String tableId = "Table2";

		String tdId = "selectName";

		int paginas = Integer.parseInt(obtenerValorTd(pageSource, tableId, tdId).trim());

		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

		String datoTablas = "";

		for (int i = 1; i <= paginas; i++) {

			fecha = JMthos.saberFechaYHoraActual(true);

			jsExecutor.executeScript("function hacerClickEnTdPorValor(valorBuscado) {"
					+ "    var tabla = document.querySelector('#Table2');" + "    if (tabla) {"
					+ "        var primerTr = tabla.querySelector('tr');" + "        if (primerTr) {"
					+ "            var celdas = primerTr.querySelectorAll('td');"
					+ "            for (var td of celdas) {"
					+ "                if (td.textContent.trim() == valorBuscado) {" + "                    td.click();"
					+ "                    return true;" + "                }" + "            }" + "        }"
					+ "        var segundoTr = tabla.querySelectorAll('tr')[1];" + "        if (segundoTr) {"
					+ "            var celdas = segundoTr.querySelectorAll('td');"
					+ "            for (var td of celdas) {"
					+ "                if (td.textContent.trim() == valorBuscado) {" + "                    td.click();"
					+ "                    return true;" + "                }" + "            }" + "        }" + "    }"
					+ "    return false;" + "}" + "hacerClickEnTdPorValor('" + i + "');");

			try {

				Thread.sleep(1500);

			}

			catch (Exception e) {

			}

			pageSource = driver.getPageSource();

			pageSource = "<table " + pageSource.substring(pageSource.indexOf("id=\"Table3\">"), pageSource.length());

			pageSource = pageSource.substring(0, pageSource.indexOf("</table>") + 8);

			pageSource = JMthos.eliminarEtiquetaHtml("th", "colspan", "3", pageSource, true);

			pageSource = JMthos.eliminarEtiquetaHtml("td", "colspan", "3", pageSource, true);

			datoTablas += pageSource;

		}

		try {

			fecha = fecha.replace("/", "-");

			fecha = fecha.replace(" ", "_");

			fecha = fecha.replace(":", "-");

			fecha = fecha.replace("-\\", ":\\");

			nombreArchivo = nombreArchivo.replace("&amp;", "");

			String filePath = carpeta + JMthos.saberSeparador() + nombreArchivo + "_" + fecha + ".xls";

			ExportarPoi.createXlsFromHtmlTables(datoTablas, filePath);

		}

		catch (Exception e) {

			e.printStackTrace();

		}

	}

	static void mirarEnBolaMatch(String carpeta, ArrayList<String> lista, String fecha, WebDriver driver) {

		MetodosPrograma.ponerValorSelect(driver, "selectMatchCount1", "20");

		MetodosPrograma.ponerValorSelect(driver, "selectMatchCount2", "20");

		String pageSource = driver.getPageSource();

		pageSource = pageSource.substring(pageSource.indexOf("<title>") + 7, pageSource.length());

		try {

			lista.add(pageSource.substring(0, pageSource.indexOf("VS")).trim());

			lista.add(pageSource.substring(pageSource.indexOf("VS") + 2, pageSource.indexOf("-")).trim());

		}

		catch (Exception e) {

			lista.add(pageSource.substring(0, pageSource.indexOf("vs")).trim());

			lista.add(pageSource.substring(pageSource.indexOf("vs") + 2, pageSource.indexOf("-")).trim());

		}

		pageSource = pageSource.substring(pageSource.indexOf("Previous Scores Statistics") + 26, pageSource.length());

		pageSource = pageSource.replaceAll("</h2>", "");

		pageSource = JMthos.eliminarEtiquetaHtml("tr", "name", "vsBarTr", pageSource, false);

		pageSource = JMthos.eliminarEtiquetaHtml("tr", "name", "vsHeadTr", pageSource, false);

		pageSource = JMthos.eliminarEtiquetaHtml("tr", "class", "team-home", pageSource, false);

		pageSource = pageSource.substring(0, pageSource.lastIndexOf("</table>") + 8);

		pageSource = pageSource.substring(0, pageSource.lastIndexOf("<div id=\"porletAd6\">"));

		pageSource = JMthos.eliminarEtiquetaHtml("tr", "class", "team-guest", pageSource, false);

		pageSource = JMthos.eliminarEtiquetaHtml("tr", "name", "nodataTr", pageSource, false);

		MetodosPrograma.convertHTMLtoExcel(pageSource, carpeta + JMthos.saberSeparador() + "BOLA--" + lista.get(0)
				+ "-VS-" + lista.get(1) + "__" + fecha + ".xls", lista);

	}

	static void ponerValorSelect(WebDriver driver, String selectId, String value) {

		try {

			WebElement selectElement = driver.findElement(By.id(selectId));

			Select select = new Select(selectElement);

			java.util.List<WebElement> options = selectElement.findElements(By.tagName("option"));

			if (!options.isEmpty()) {

				select.selectByValue(value);

			}

		}

		catch (Exception e) {

		}

	}

	public static void convertHTMLtoExcel(String htmlContent, String outputPath, List<String> lista) {

		try {

			Document document = Jsoup.parse(htmlContent);

			Workbook workbook = new HSSFWorkbook();

			Elements tables = document.select("table");

			String dato = "";

			Element table;

			org.apache.poi.ss.usermodel.Sheet sheet;

			Element row;

			Elements cells;

			Row excelRow;

			Element cell;

			String cellValue;

			Cell excelCell;

			Elements rows;

			for (int tableIndex = 0; tableIndex < 2; tableIndex++) {

				table = tables.get(tableIndex);

				if (lista == null) {

					dato = "Sheet " + tableIndex;

				}

				else {

					if (!lista.isEmpty()) {

						if (tableIndex < lista.size()) {

							dato = lista.get(tableIndex);

						}

						else {

							dato = "A";

						}

					}

				}

				dato = dato.trim();

				dato = dato.replace("'", "");

				if (dato.contains("[")) {

					dato = dato.replace("[", "");

					dato = dato.replace("]", "");

					try {

						dato = dato.substring(dato.indexOf(" "));

					}

					catch (Exception e) {

					}

				}

				sheet = workbook.createSheet(dato);

				rows = table.select("tr");

				for (int i = 0; i < rows.size(); i++) {

					row = rows.get(i);

					cells = row.select("td");

					excelRow = sheet.createRow(i);

					for (int j = 0; j < cells.size(); j++) {

						cell = cells.get(j);

						cellValue = cell.text();

						excelCell = excelRow.createCell(j);

						excelCell.setCellValue(cellValue);

					}

				}

			}

			outputPath = outputPath.replace("/", "-");

			outputPath = outputPath.replace(" ", "_");

			outputPath = outputPath.replace(":", "-");

			outputPath = outputPath.replace("-\\", ":\\");

			java.nio.file.Path outputFilePath = FileSystems.getDefault().getPath(outputPath);

			try (FileOutputStream outputStream = new FileOutputStream(outputFilePath.toString())) {

				workbook.write(outputStream);

			}

			workbook.close();

		}

		catch (Exception e) {

			e.printStackTrace();

		}

	}

}
