import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public abstract class ExportXls {

	private static final Map<String, String> htmlEntities = new HashMap<>();

	static {

		htmlEntities.put("&quot;", "\"");
		htmlEntities.put("&amp;", "&");
		htmlEntities.put("&lt;", "<");
		htmlEntities.put("&gt;", ">");
		htmlEntities.put("&nbsp;", " ");
		htmlEntities.put("&iexcl;", "¡");
		htmlEntities.put("&cent;", "¢");
		htmlEntities.put("&pound;", "£");
		htmlEntities.put("&curren;", "¤");
		htmlEntities.put("&yen;", "¥");
		htmlEntities.put("&brvbar;", "¦");
		htmlEntities.put("&sect;", "§");
		htmlEntities.put("&uml;", "¨");
		htmlEntities.put("&copy;", "©");
		htmlEntities.put("&ordf;", "ª");
		htmlEntities.put("&laquo;", "«");
		htmlEntities.put("&not;", "¬");
		htmlEntities.put("&shy;", "­");
		htmlEntities.put("&reg;", "®");
		htmlEntities.put("&macr;", "¯");
		htmlEntities.put("&deg;", "°");
		htmlEntities.put("&plusmn;", "±");
		htmlEntities.put("&sup2;", "²");
		htmlEntities.put("&sup3;", "³");
		htmlEntities.put("&acute;", "´");
		htmlEntities.put("&micro;", "µ");
		htmlEntities.put("&para;", "¶");
		htmlEntities.put("&middot;", "·");
		htmlEntities.put("&cedil;", "¸");
		htmlEntities.put("&sup1;", "¹");
		htmlEntities.put("&ordm;", "º");
		htmlEntities.put("&raquo;", "»");
		htmlEntities.put("&frac14;", "¼");
		htmlEntities.put("&frac12;", "½");
		htmlEntities.put("&frac34;", "¾");
		htmlEntities.put("&iquest;", "¿");

		// Letras acentuadas y otros caracteres

		htmlEntities.put("&Agrave;", "À");
		htmlEntities.put("&Aacute;", "Á");
		htmlEntities.put("&Acirc;", "Â");
		htmlEntities.put("&Atilde;", "Ã");
		htmlEntities.put("&Auml;", "Ä");
		htmlEntities.put("&Aring;", "Å");
		htmlEntities.put("&AElig;", "Æ");
		htmlEntities.put("&Ccedil;", "Ç");
		htmlEntities.put("&Egrave;", "È");
		htmlEntities.put("&Eacute;", "É");
		htmlEntities.put("&Ecirc;", "Ê");
		htmlEntities.put("&Euml;", "Ë");
		htmlEntities.put("&Igrave;", "Ì");
		htmlEntities.put("&Iacute;", "Í");
		htmlEntities.put("&Icirc;", "Î");
		htmlEntities.put("&Iuml;", "Ï");
		htmlEntities.put("&ETH;", "Ð");
		htmlEntities.put("&Ntilde;", "Ñ");
		htmlEntities.put("&Ograve;", "Ò");
		htmlEntities.put("&Oacute;", "Ó");
		htmlEntities.put("&Ocirc;", "Ô");
		htmlEntities.put("&Otilde;", "Õ");
		htmlEntities.put("&Ouml;", "Ö");
		htmlEntities.put("&times;", "×");
		htmlEntities.put("&Oslash;", "Ø");
		htmlEntities.put("&Ugrave;", "Ù");
		htmlEntities.put("&Uacute;", "Ú");
		htmlEntities.put("&Ucirc;", "Û");
		htmlEntities.put("&Uuml;", "Ü");
		htmlEntities.put("&Yacute;", "Ý");
		htmlEntities.put("&THORN;", "Þ");
		htmlEntities.put("&szlig;", "ß");
		htmlEntities.put("&agrave;", "à");
		htmlEntities.put("&aacute;", "á");
		htmlEntities.put("&acirc;", "â");
		htmlEntities.put("&atilde;", "ã");
		htmlEntities.put("&auml;", "ä");
		htmlEntities.put("&aring;", "å");
		htmlEntities.put("&aelig;", "æ");
		htmlEntities.put("&ccedil;", "ç");
		htmlEntities.put("&egrave;", "è");
		htmlEntities.put("&eacute;", "é");
		htmlEntities.put("&ecirc;", "ê");
		htmlEntities.put("&euml;", "ë");
		htmlEntities.put("&igrave;", "ì");
		htmlEntities.put("&iacute;", "í");
		htmlEntities.put("&icirc;", "î");
		htmlEntities.put("&iuml;", "ï");
		htmlEntities.put("&eth;", "ð");
		htmlEntities.put("&ntilde;", "ñ");
		htmlEntities.put("&ograve;", "ò");
		htmlEntities.put("&oacute;", "ó");
		htmlEntities.put("&ocirc;", "ô");
		htmlEntities.put("&otilde;", "õ");
		htmlEntities.put("&ouml;", "ö");
		htmlEntities.put("&divide;", "÷");
		htmlEntities.put("&oslash;", "ø");
		htmlEntities.put("&ugrave;", "ù");
		htmlEntities.put("&uacute;", "ú");
		htmlEntities.put("&ucirc;", "û");
		htmlEntities.put("&uuml;", "ü");
		htmlEntities.put("&yacute;", "ý");
		htmlEntities.put("&thorn;", "þ");
		htmlEntities.put("&yuml;", "ÿ");

		// Caracteres especiales adicionales
		htmlEntities.put("&OElig;", "Œ");
		htmlEntities.put("&oelig;", "œ");
		htmlEntities.put("&Scaron;", "Š");
		htmlEntities.put("&scaron;", "š");
		htmlEntities.put("&Yuml;", "Ÿ");
		htmlEntities.put("&fnof;", "ƒ");
		htmlEntities.put("&circ;", "ˆ");
		htmlEntities.put("&tilde;", "˜");
		htmlEntities.put("&Alpha;", "Α");
		htmlEntities.put("&Beta;", "Β");
		htmlEntities.put("&Gamma;", "Γ");
		htmlEntities.put("&Delta;", "Δ");
		htmlEntities.put("&Epsilon;", "Ε");
		htmlEntities.put("&Zeta;", "Ζ");
		htmlEntities.put("&Eta;", "Η");
		htmlEntities.put("&Theta;", "Θ");
		htmlEntities.put("&Iota;", "Ι");
		htmlEntities.put("&Kappa;", "Κ");
		htmlEntities.put("&Lambda;", "Λ");
		htmlEntities.put("&Mu;", "Μ");
		htmlEntities.put("&Nu;", "Ν");
		htmlEntities.put("&Xi;", "Ξ");
		htmlEntities.put("&Omicron;", "Ο");
		htmlEntities.put("&Pi;", "Π");
		htmlEntities.put("&Rho;", "Ρ");
		htmlEntities.put("&Sigma;", "Σ");
		htmlEntities.put("&Tau;", "Τ");
		htmlEntities.put("&Upsilon;", "Υ");
		htmlEntities.put("&Phi;", "Φ");
		htmlEntities.put("&Chi;", "Χ");
		htmlEntities.put("&Psi;", "Ψ");
		htmlEntities.put("&Omega;", "Ω");
		htmlEntities.put("&alpha;", "α");
		htmlEntities.put("&beta;", "β");
		htmlEntities.put("&gamma;", "γ");
		htmlEntities.put("&delta;", "δ");
		htmlEntities.put("&epsilon;", "ε");
		htmlEntities.put("&zeta;", "ζ");
		htmlEntities.put("&eta;", "η");
		htmlEntities.put("&theta;", "θ");
		htmlEntities.put("&iota;", "ι");
		htmlEntities.put("&kappa;", "κ");
		htmlEntities.put("&lambda;", "λ");
		htmlEntities.put("&mu;", "μ");
		htmlEntities.put("&nu;", "ν");
		htmlEntities.put("&xi;", "ξ");
		htmlEntities.put("&omicron;", "ο");
		htmlEntities.put("&pi;", "π");
		htmlEntities.put("&rho;", "ρ");
		htmlEntities.put("&sigmaf;", "ς");
		htmlEntities.put("&sigma;", "σ");
		htmlEntities.put("&tau;", "τ");
		htmlEntities.put("&upsilon;", "υ");
		htmlEntities.put("&phi;", "φ");
		htmlEntities.put("&chi;", "χ");
		htmlEntities.put("&psi;", "ψ");
		htmlEntities.put("&omega;", "ω");
		htmlEntities.put("&thetasym;", "ϑ");
		htmlEntities.put("&upsih;", "ϒ");
		htmlEntities.put("&piv;", "ϖ");
		htmlEntities.put("&ensp;", " ");
		htmlEntities.put("&emsp;", " ");
		htmlEntities.put("&thinsp;", " ");
		htmlEntities.put("&zwnj;", "‌");
		htmlEntities.put("&zwj;", "‍");
		htmlEntities.put("&lrm;", "‎");
		htmlEntities.put("&rlm;", "‏");
		htmlEntities.put("&ndash;", "–");
		htmlEntities.put("&mdash;", "—");
		htmlEntities.put("&lsquo;", "‘");
		htmlEntities.put("&rsquo;", "’");
		htmlEntities.put("&sbquo;", "‚");
		htmlEntities.put("&ldquo;", "“");
		htmlEntities.put("&rdquo;", "”");
		htmlEntities.put("&bdquo;", "„");
		htmlEntities.put("&dagger;", "†");
		htmlEntities.put("&Dagger;", "‡");
		htmlEntities.put("&bull;", "•");
		htmlEntities.put("&hellip;", "…");
		htmlEntities.put("&permil;", "‰");
		htmlEntities.put("&prime;", "′");
		htmlEntities.put("&Prime;", "″");
		htmlEntities.put("&lsaquo;", "‹");
		htmlEntities.put("&rsaquo;", "›");
		htmlEntities.put("&oline;", "‾");
		htmlEntities.put("&frasl;", "⁄");
		htmlEntities.put("&euro;", "€");
		htmlEntities.put("&image;", "ℑ");
		htmlEntities.put("&weierp;", "℘");
		htmlEntities.put("&real;", "ℜ");
		htmlEntities.put("&trade;", "™");
		htmlEntities.put("&alefsym;", "ℵ");
		htmlEntities.put("&larr;", "←");
		htmlEntities.put("&uarr;", "↑");
		htmlEntities.put("&rarr;", "→");
		htmlEntities.put("&darr;", "↓");
		htmlEntities.put("&harr;", "↔");
		htmlEntities.put("&crarr;", "↵");
		htmlEntities.put("&lArr;", "⇐");
		htmlEntities.put("&uArr;", "⇑");
		htmlEntities.put("&rArr;", "⇒");
		htmlEntities.put("&dArr;", "⇓");
		htmlEntities.put("&hArr;", "⇔");
		htmlEntities.put("&forall;", "∀");
		htmlEntities.put("&part;", "∂");
		htmlEntities.put("&exist;", "∃");
		htmlEntities.put("&empty;", "∅");
		htmlEntities.put("&nabla;", "∇");
		htmlEntities.put("&isin;", "∈");
		htmlEntities.put("&notin;", "∉");
		htmlEntities.put("&ni;", "∋");
		htmlEntities.put("&prod;", "∏");
		htmlEntities.put("&sum;", "∑");
		htmlEntities.put("&minus;", "−");
		htmlEntities.put("&lowast;", "∗");
		htmlEntities.put("&radic;", "√");
		htmlEntities.put("&prop;", "∝");
		htmlEntities.put("&infin;", "∞");
		htmlEntities.put("&ang;", "∠");
		htmlEntities.put("&and;", "∧");
		htmlEntities.put("&or;", "∨");
		htmlEntities.put("&cap;", "∩");
		htmlEntities.put("&cup;", "∪");
		htmlEntities.put("&int;", "∫");
		htmlEntities.put("&there4;", "∴");
		htmlEntities.put("&sim;", "∼");
		htmlEntities.put("&cong;", "≅");
		htmlEntities.put("&asymp;", "≈");
		htmlEntities.put("&ne;", "≠");
		htmlEntities.put("&equiv;", "≡");
		htmlEntities.put("&le;", "≤");
		htmlEntities.put("&ge;", "≥");
		htmlEntities.put("&sub;", "⊂");
		htmlEntities.put("&sup;", "⊃");
		htmlEntities.put("&nsub;", "⊄");
		htmlEntities.put("&sube;", "⊆");
		htmlEntities.put("&supe;", "⊇");
		htmlEntities.put("&oplus;", "⊕");
		htmlEntities.put("&otimes;", "⊗");
		htmlEntities.put("&perp;", "⊥");
		htmlEntities.put("&sdot;", "⋅");
		htmlEntities.put("&lceil;", "⌈");
		htmlEntities.put("&rceil;", "⌉");
		htmlEntities.put("&lfloor;", "⌊");
		htmlEntities.put("&rfloor;", "⌋");
		htmlEntities.put("&lang;", "⟨");
		htmlEntities.put("&rang;", "⟩");
		htmlEntities.put("&loz;", "◊");
		htmlEntities.put("&spades;", "♠");
		htmlEntities.put("&clubs;", "♣");
		htmlEntities.put("&hearts;", "♥");
		htmlEntities.put("&diams;", "♦");
	}

	public static String decodeHtmlEntities(String html) {

		for (Map.Entry<String, String> entry : htmlEntities.entrySet()) {

			html = html.replace(entry.getKey(), entry.getValue());

		}

		return html;

	}

	public static void exportHtmlTableToExcel(String htmlContent, String excelFilePath, String sheetName)
			throws IOException {

		HSSFWorkbook workbook = new HSSFWorkbook();

		HSSFSheet sheet = workbook.createSheet(sheetName);

		String[] rows = htmlContent.split("</tr>");

		List<String> encabezados = obtenerEncabezados(htmlContent);

		HSSFRow headerRow = sheet.createRow(0);

		for (int i = 0; i < encabezados.size(); i++) {

			String encabezado = encabezados.get(i);

			HSSFCell cell = headerRow.createCell(i);

			cell.setCellValue(encabezado);

		}

		for (int i = 1; i < rows.length; i++) {

			String rowContent = rows[i].trim();

			HSSFRow row = sheet.createRow(i);

			String[] cells = rowContent.split("</td>");

			for (int j = 0; j < cells.length; j++) {

				String cellContent = cells[j].trim();

				HSSFCell cell = row.createCell(j);

				String cellText = cleanHtmlTags(cellContent);

				cell.setCellValue(cellText);

			}

		}

		for (int i = 0; i < encabezados.size(); i++) {

			sheet.autoSizeColumn(i);

		}

		try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {

			workbook.write(outputStream);

		}

		workbook.close();

	}

	public static String cleanHtmlTags(String htmlText) {

		return htmlText.replaceAll("<.*?>", "").trim();

	}

	public static List<String> obtenerEncabezados(String htmlContent) {

		List<String> encabezados = new ArrayList<>();

		int startIndex = htmlContent.indexOf("<thead>");

		int endIndex = htmlContent.indexOf("</thead>");

		if (startIndex != -1 && endIndex != -1) {

			String theadContent = htmlContent.substring(startIndex, endIndex);

			String[] thTags = theadContent.split("</th>");

			int startIndexTh = 0;

			String thContent = "";

			for (String tag : thTags) {

				startIndexTh = tag.indexOf("<th>");

				if (startIndexTh != -1) {

					thContent = tag.substring(startIndexTh + 4).trim();

					encabezados.add(cleanHtmlTags(thContent));

				}

			}

		}

		return encabezados;

	}

	public static void exportHtmlTableToExcel(String htmlContent, String excelFilePath, String sheetName,
			String startCell) throws IOException {

		if (startCell == null || startCell.isEmpty()) {

			startCell = "A1";

		}

		else {

			startCell = startCell.toUpperCase();

		}

		HSSFWorkbook workbook = new HSSFWorkbook();

		HSSFSheet sheet = workbook.createSheet(sheetName);

		int startRow = obtenerFilaDesdeCelda(startCell);

		int startCol = obtenerColumnaDesdeCelda(startCell);

		String[] rows = htmlContent.split("</tr>");

		List<String> encabezados = obtenerEncabezados(htmlContent);

		HSSFRow headerRow = sheet.createRow(startRow);

		String encabezado;

		HSSFCell cell;

		for (int i = 0; i < encabezados.size(); i++) {

			encabezado = encabezados.get(i);

			cell = headerRow.createCell(startCol + i);

			cell.setCellValue(encabezado);

		}

		String rowContent;

		HSSFRow row;

		String[] cells;

		String cellContent;

		String cellText;

		for (int i = 1; i < rows.length; i++) {

			rowContent = rows[i].trim();

			row = sheet.createRow(startRow + i);

			cells = rowContent.split("</td>");

			for (int j = 0; j < cells.length; j++) {

				cellContent = cells[j].trim();

				cell = row.createCell(startCol + j);

				cellText = cleanHtmlTags(cellContent);

				cell.setCellValue(cellText);

			}

		}

		for (int i = 0; i < encabezados.size(); i++) {

			sheet.autoSizeColumn(startCol + i);

		}

		try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {

			workbook.write(outputStream);

		}

		workbook.close();

	}

	public static int obtenerFilaDesdeCelda(String cell) {

		String filaStr = cell.replaceAll("[^0-9]", "");

		return Integer.parseInt(filaStr) - 1;

	}

	public static int obtenerColumnaDesdeCelda(String cell) {

		String columnaStr = cell.replaceAll("[^A-Za-z]", ""); // Extraer solo las letras

		return columnaStr.toUpperCase().chars().reduce(0, (acc, c) -> acc * 26 + (c - 'A' + 1)) - 1;

	}

	public static String headerCellValue(String htmlContent, String startTag, String endTag, int columnIndex) {

		int startIndex = htmlContent.indexOf(startTag, htmlContent.indexOf("<thead>"));

		int endIndex = htmlContent.indexOf(endTag, startIndex);

		String header = htmlContent.substring(startIndex + startTag.length(), endIndex).trim();

		String[] headers = header.split("</th>");

		return cleanHtmlTags(headers[columnIndex]);

	}

	public static String modifyHtml(String html) {

		StringBuilder newHtml = new StringBuilder();

		newHtml.append("<table>\n").append("<thead>\n").append("<tr>\n").append("<th>Partido</th>\n")
				.append("<th>Resultado</th>\n").append("</tr>\n").append("</thead>\n").append("<tbody>\n");

		int currentIndex = 0;

		int startPartido;

		int endPartido;

		String partido;

		int startResultado;

		int endResultado;

		String resultado;

		while (currentIndex < html.length()) {

			startPartido = html.indexOf("title=\"", currentIndex);

			if (startPartido == -1)

				break;

			startPartido += 7;

			endPartido = html.indexOf("\">", startPartido);

			if (endPartido == -1)
				break;

			partido = html.substring(startPartido, endPartido);

			startResultado = html.indexOf("data-mid=\"", endPartido);

			if (startResultado == -1)
				break;

			startResultado = html.indexOf("\">", startResultado) + 2;

			endResultado = html.indexOf("</span>", startResultado);

			if (endResultado == -1)
				break;

			resultado = html.substring(startResultado, endResultado).trim();

			newHtml.append("<tr>\n").append("<td>").append(partido).append("</td>\n").append("<td>").append(resultado)
					.append("</td>\n").append("</tr>\n");

			currentIndex = endResultado + 7;

		}

		newHtml.append("</tbody>\n").append("</table>");

		return newHtml.toString();

	}

}
