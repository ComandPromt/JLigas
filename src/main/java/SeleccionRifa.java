
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.comboBox.comboSuggestion.ComboBoxSuggestion;
import com.label.round.LabelRound;
import com.label.round.RoundLabel;

public class SeleccionRifa extends JPanel {

	public SeleccionRifa() {

		setLayout(new GridLayout(3, 1));

		RoundLabel lblNewLabel = new RoundLabel("Seleccione la rifa");

		add(lblNewLabel);

		ComboBoxSuggestion<String> comboBox = new ComboBoxSuggestion<>();

		add(comboBox);

		JPanel panel = new JPanel();

		add(panel);

		panel.setLayout(new GridLayout(1, 0, 0, 0));

		LabelRound btnNewButton = new LabelRound();

		panel.add(btnNewButton);

		ButtonCustom btnNewButton_1 = new ButtonCustom("aa");
		btnNewButton_1.setText("aaa");
		btnNewButton_1.setLeft(40);
		btnNewButton_1.setHorizontalAlignment(SwingConstants.RIGHT);
		Copy copiar = new Copy(32, 32);

		btnNewButton_1.setIcon(copiar);
		btnNewButton_1.setIconSize(40);
		btnNewButton_1.setIconAlign(SwingConstants.LEFT);
		panel.add(btnNewButton_1);

	}

}
