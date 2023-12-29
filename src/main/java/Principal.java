
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
import javax.swing.LayoutStyle.ComponentPlacement;
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

	public Principal() throws IOException {

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

	public static String limpiarEspacios(String texto) {

		String textoSinEspacios = texto.trim();

		textoSinEspacios = textoSinEspacios.replaceAll("\\s+", " ");

		Document document = Jsoup.parse(textoSinEspacios);

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

		JMthos.crearFichero("hola.txt", textoSinEspacios);

		return textoSinEspacios;

	}

	private void buscarEquipo(String url) {

		try {

			panel_1.setEnabled(false);

			System.setProperty("webdriver.gecko.driver",
					new File(".").getCanonicalPath() + "\\geckodriver\\geckodriver.exe");

			WebDriver driver = new FirefoxDriver();

			driver.get(url);

			WebElement selectElement = driver.findElement(By.id("selectMatchCount1"));

			Select select = new Select(selectElement);

			select.selectByValue("20");

			selectElement = driver.findElement(By.id("selectMatchCount2"));

			select = new Select(selectElement);

			select.selectByValue("20");

			String pageSource = driver.getPageSource();

			pageSource = pageSource.substring(pageSource.indexOf("<table id=\"table_v1\""),
					pageSource.indexOf("Data Comparison"));

			pageSource = limpiarEspacios(pageSource);

			driver.quit();

			LinkedList<String> lista = new LinkedList<>();

			lista.add("Puntuaciones anteriores Local");

			lista.add("Puntuaciones anteriores Visitante");

			lista.add("Apuestas");

			JMthos.crearFichero("prueba.html", pageSource);

			String fecha = JMthos.saberFechaYHoraActual(true);

			fecha = fecha.replace("/", "_");

			fecha = fecha.replace(" ", "_");

			fecha = fecha.replace(":", "_");

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

	public static void convertHTMLtoExcel(String htmlContent, String outputPath, List<String> lista)
			throws IOException {

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

		for (int tableIndex = 0; tableIndex < tables.size(); tableIndex++) {

			table = tables.get(tableIndex);

			if (lista == null) {

				dato = "Sheet " + tableIndex;

			}

			else {

				dato = lista.get(tableIndex);

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

	public void initComponents() throws IOException {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

		setResizable(false);

		TextFieldShadow panel = new TextFieldShadow();

		panel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {

					buscarEquipo(panel.getText());

				}

			}

		});

		panel.setFont(new Font("Dialog", Font.PLAIN, 20));

		panel_1 = new NButton("Exportar");

		panel_1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				buscarEquipo(panel.getText());

			}

		});

		JLabel lblNewLabel = new JLabel("URL");

		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);

		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));

		ResizedButton btnNewButton_1 = new ResizedButton(true, "");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_1.setBackground(Color.WHITE);

		btnNewButton_1.setIcon(new ImageIcon(Principal.class.getResource("/imgs/folder.png")));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup()
				.addGap(49)
				.addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(lblNewLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
								GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addComponent(panel_1, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 341,
								GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
				.addContainerGap(129, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.TRAILING).addGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup()
						.addGap(24)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addGap(18).addComponent(panel, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
						.addGap(28).addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE))
						.addGroup(layout.createSequentialGroup().addGap(163).addComponent(btnNewButton_1,
								GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap(32, Short.MAX_VALUE)));

		getContentPane().setLayout(layout);

		setSize(new Dimension(500, 300));

		setLocationRelativeTo(null);

	}

	public void actionPerformed(ActionEvent arg0) {

	}

	public void stateChanged(ChangeEvent e) {

	}

}
