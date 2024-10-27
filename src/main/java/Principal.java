
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.message.alerts.PopupAlerts;
import com.message.alerts.PopupAlerts.AlertType;
import com.textField.text.TextFieldWithPlaceholder;

import mthos.JMthos;

@SuppressWarnings("all")

public class Principal extends javax.swing.JFrame {

	private TextFieldWithPlaceholder textField;

	private String carpeta;

	public Principal() throws IOException {

		System.setProperty("webdriver.gecko.driver",
				new File(".").getCanonicalPath() + "\\geckodriver\\geckodriver.exe");

		carpeta = JMthos.directorioActual() + "exportaciones";

		if (!new File(carpeta).exists()) {

			JMthos.crearCarpeta(carpeta);

		}

		getContentPane().setBackground(Color.WHITE);

		setIconImage(Toolkit.getDefaultToolkit().getImage(Principal.class.getResource("/imgs/icon.png")));

		setTitle("Jligas");

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

	public void initComponents() throws IOException {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

		setResizable(false);

		textField = new TextFieldWithPlaceholder();

		textField.addKeyListener(new KeyAdapter() {

			@Override

			public void keyPressed(KeyEvent e) {

				String url = JMthos.limpiarEspacios(textField.getText(), true);

				ArrayList<String> lista = new ArrayList<>();

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {

					String fecha = JMthos.saberFechaYHoraActual(true);

					WebDriver driver = new FirefoxDriver();

					driver.get(url);

					if (url.contains("league")) {

						MetodosPrograma.mirarEnBola(url, carpeta, fecha, driver);

					}

					else {

						if (url.contains("live-")) {

							url = url.replace("live-", "h2h-");

							driver.get(url);

						}

						MetodosPrograma.mirarEnBolaMatch(carpeta, lista, fecha, driver);

					}

					try {

						Thread.sleep(1500);

					}

					catch (InterruptedException e1) {

					}

					driver.quit();

					PopupAlerts alerta = new PopupAlerts();

					alerta.setSize(550, 300);

					alerta.mensaje("Archivo generado correctamente", AlertType.SUCCESS, 30, null);

				}

			}

		});

		textField.setRound(true);

		textField.setDescripcion("URL");

		JLabel lblNewLabel = new JLabel("");

		lblNewLabel.addMouseListener(new MouseAdapter() {

			@Override

			public void mousePressed(MouseEvent e) {

				JMthos.abrir(carpeta);

			}

		});

		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);

		lblNewLabel.setIcon(new ImageIcon(Principal.class.getResource("/imgs/folder.png")));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(26)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, 280, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(14, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(19)
						.addGroup(layout.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE))
						.addContainerGap(363, Short.MAX_VALUE)));

		getContentPane().setLayout(layout);

		setSize(new Dimension(400, 130));

		setLocationRelativeTo(null);

	}

	public void actionPerformed(ActionEvent arg0) {

	}

	public void stateChanged(ChangeEvent e) {

	}

}
