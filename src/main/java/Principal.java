
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.LinkedList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import com.buttons.round.NButton;
import com.buttons.simple.ResizedButton;
import com.message.alerts.PopupAlerts;
import com.message.alerts.PopupAlerts.AlertType;
import com.textField.text.TextFieldShadow;

import mthos.JMthos;

@SuppressWarnings("all")

public class Principal extends javax.swing.JFrame {

	private NButton panel_1;

	private String carpeta;

	private LinkedList<String> lista;

	private TextFieldShadow panel;

	private String urlApuesta;

	public Principal() throws IOException {

		System.setProperty("webdriver.gecko.driver",
				new File(".").getCanonicalPath() + "\\geckodriver\\geckodriver.exe");

		carpeta = JMthos.directorioActual() + "exportaciones";

		if (!new File(carpeta).exists()) {

			JMthos.crearCarpeta(carpeta);

		}

		setIconImage(Toolkit.getDefaultToolkit().getImage(Principal.class.getResource("/imgs/icon.png")));

		getContentPane().setBackground(Color.WHITE);

		setTitle("JLigas");

		initComponents();

		setVisible(true);

	}

	public static void main(String[] args) {

		try {

			new Principal().setVisible(true);

		}

		catch (Exception e) {

		}

	}

	public String limpiarEspacios(String texto) {

		String textoSinEspacios = texto.trim();

		Document document = Jsoup.parse(textoSinEspacios);

		Element menu3Li = document.select("#menu3").first();

		urlApuesta = menu3Li.select("a").attr("href");

		texto = texto.substring(texto.indexOf("<table id=\"table_v1\""), texto.indexOf("Data Comparison"));

		Element primeraFila = document.select("table.team-table-home tbody tr").first();

		String valorPrimeraFila = primeraFila.text();

		valorPrimeraFila = valorPrimeraFila.trim();

		lista.add(valorPrimeraFila);

		Element firstTr = document.select("tr").first();

		if (firstTr != null) {

			firstTr.remove();

		}

		Element table = document.select("#table_v2").first();

		if (table != null) {

			Element td = table.select("tbody tr td").first();

			if (td != null) {

				Element anchor = td.select("a").first();

				if (anchor != null) {

					String hrefValue = anchor.attr("href");

					String textValue = anchor.text();

					lista.add(textValue);

				}

			}

		}

		Elements tdElements = document.select("td[id=td_stat1]");

		tdElements.remove();

		tdElements = document.select("td[id=td_stat2]");

		tdElements.remove();

		Elements selectElements = document.select("select");

		for (Element select : selectElements) {

			Elements optionsToRemove = select.select("option:contains(Last)");

			optionsToRemove.remove();

		}

		Elements scriptTags = document.select("script");

		scriptTags.remove();

		textoSinEspacios = document.outerHtml();

		lista.add("Apuestas");

		return textoSinEspacios;

	}

	private static String eliminarPrimerTR(String html, String tablaId) {

		Document document = Jsoup.parse(html);

		Element table = document.select(tablaId).first();

		if (table != null) {

			Element tbody = table.select("tbody").first();

			if (tbody != null) {

				Elements trs = tbody.select("tr");

				if (!trs.isEmpty()) {

					Element primerTR = trs.first();

					primerTR.remove();

				}

			}

		}

		return document.outerHtml();

	}

	private void buscarEquipo(String url) {

		try {

			panel_1.setEnabled(false);

			WebDriver driver = new FirefoxDriver();

			driver.get(url);

			ponerValorSelect(driver, "selectMatchCount1");

			ponerValorSelect(driver, "selectMatchCount2");

			String pageSource = driver.getPageSource();

			lista = new LinkedList<>();

			pageSource = limpiarEspacios(pageSource);

			String fecha = JMthos.saberFechaYHoraActual(true);

			fecha = fecha.replace("/", "_");

			fecha = fecha.replace(" ", "_");

			fecha = fecha.replace(":", "_");

			urlApuesta = "https://" + url.substring(8).substring(0, url.substring(8).indexOf("/") + 1) + "1x2-odds"
					+ urlApuesta.substring(urlApuesta.lastIndexOf("/"));

			driver.get(urlApuesta);

			String contenidoHTML = driver.getPageSource();

			contenidoHTML = contenidoHTML.substring(
					contenidoHTML.indexOf("<div id=\"divFooterFload\" class=\"oddfooterDiv\">"),
					contenidoHTML.length());

			contenidoHTML = contenidoHTML.substring(0, contenidoHTML.indexOf("<div id=\"divnotes\""));

			contenidoHTML = contenidoHTML
					.replace("<td width=\"1\" rowspan=\"6\" class=\"gbg lb rb\" style=\"display:none;\"></td>", "");

			contenidoHTML = contenidoHTML
					.replace("<th width=\"1\" rowspan=\"6\" class=\"lb rb\" style=\"display:none;\"></th>", "");

			contenidoHTML += "</body>\r\n" + "</html>";

			driver.quit();

			pageSource = pageSource.substring(pageSource.indexOf("<table id=\"table_v1\""), pageSource.length());

			try {

				pageSource = pageSource.substring(0, pageSource.indexOf("<div id=\"porletAd5\">"));

			}

			catch (Exception e) {

			}

			pageSource = eliminarPrimerTR(pageSource, "#table_v1");

			pageSource = eliminarPrimerTR(pageSource, "#table_v2");

			pageSource += contenidoHTML;

			pageSource = "<html>\r\n" + "<head>\r\n" + "</head>\r\n" + "<body>\r\n" + pageSource;

			convertHTMLtoExcel(pageSource, carpeta + JMthos.saberSeparador() + fecha + ".xls", lista);

			panel_1.setEnabled(true);

			PopupAlerts alerta = new PopupAlerts();

			alerta.setSize(550, 300);

			alerta.mensaje("Archivo generado correctamente", AlertType.SUCCESS, 30, null);

		}

		catch (Exception e1) {

			e1.printStackTrace();

		}

	}

	private void ponerValorSelect(WebDriver driver, String id) {

		try {

			WebElement selectElement = driver.findElement(By.id(id));

			Select select = new Select(selectElement);

			java.util.List<WebElement> options = selectElement.findElements(By.tagName("option"));

			if (!options.isEmpty()) {

				WebElement ultimoOption = options.get(options.size() - 1);

				select.selectByValue(ultimoOption.getText());

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

			for (int tableIndex = 0; tableIndex < 3; tableIndex++) {

				table = tables.get(tableIndex);

				if (lista == null) {

					dato = "Sheet " + tableIndex;

				}

				else {

					dato = lista.get(tableIndex);

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

	public void initComponents() throws IOException {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

		setResizable(false);

		panel = new TextFieldShadow();

		panel.setDistanciaDeSombra(0);

		panel.setDireccionDeSombra(0);

		panel.setBackground(Color.WHITE);

		panel.addKeyListener(new KeyAdapter() {

			@Override

			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {

					buscarEquipo(panel.getText());

				}

			}

		});

		panel.setFont(new Font("Dialog", Font.PLAIN, 25));

		panel_1 = new NButton("Exportar");
		panel_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println("aaaa");
			}
		});

		panel_1.setFont(new Font("Tahoma", Font.PLAIN, 25));

		panel_1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				buscarEquipo(panel.getText());

			}

		});

		JLabel lblNewLabel = new JLabel("URL");

		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);

		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 30));

		ResizedButton btnNewButton_1 = new ResizedButton(true, "");

		btnNewButton_1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				JMthos.abrirCarpeta(carpeta);

			}

		});

		btnNewButton_1.setBackground(Color.WHITE);

		btnNewButton_1.setIcon(new ImageIcon(Principal.class.getResource("/imgs/folder.png")));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup()
				.addGap(24)
				.addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addGroup(Alignment.LEADING,
								layout.createSequentialGroup().addGap(25).addComponent(lblNewLabel))
						.addComponent(panel_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE))
				.addGap(6).addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
				.addContainerGap(129, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.TRAILING).addGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup()
						.addGap(24)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addGap(18).addComponent(panel, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
						.addGap(18).addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE))
						.addGroup(layout.createSequentialGroup().addGap(163).addComponent(btnNewButton_1,
								GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap(42, Short.MAX_VALUE)));

		getContentPane().setLayout(layout);

		setSize(new Dimension(500, 300));

		setLocationRelativeTo(null);

	}

	public void actionPerformed(ActionEvent arg0) {

	}

	public void stateChanged(ChangeEvent e) {

	}

}