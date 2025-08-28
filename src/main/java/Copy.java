import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class Copy extends ImageIcon {

	private BufferedImage iconImage;

	private Color fondo = Color.LIGHT_GRAY;
	private Color fondoPagina = Color.WHITE;
	private Color linea = Color.BLACK;
	private int grosor = 1;

	private Color background;

	public void setBackground(Color background) {

		this.background = background;

		regenerar();

	}

	public Copy(int width, int height) {
		generarImagen(width, height);
	}

	private void generarImagen(int width, int height) {
		iconImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = iconImage.createGraphics();
		pintarIcono(g2d, width, height);
		g2d.dispose();
		setImage(iconImage);
	}

	private void pintarIcono(Graphics2D g2d, int width, int height) {
		try {
			g2d.setColor(background);
			g2d.fillRect(0, 0, width, height);
			g2d.setStroke(new BasicStroke(grosor));
			g2d.setColor(fondo);
			g2d.fillRect(0, 0, Math.round(width * 0.625f), Math.round(height * 0.625f));

			g2d.setColor(fondoPagina);
			g2d.fillRect(Math.round(width * 0.375f), Math.round(height * 0.375f), Math.round(width * 0.625f),
					Math.round(height * 0.625f));

			g2d.setColor(linea);
			g2d.drawRect(Math.round(width * 0.375f), Math.round(height * 0.375f), Math.round(width * 0.625f) - 2,
					Math.round(height * 0.625f) - 2);
			int y;

			for (int i = 0; i < 4; i++) {

				y = Math.round(height * (0.5f + i * 0.125f));

				g2d.drawLine(Math.round(width * 0.5f), y, Math.round(width * 0.875f), y);

			}

		} catch (Exception e) {

		}
	}

	public void setGrosor(int grosor) {
		this.grosor = grosor;
		regenerar();
	}

	public void setFondo(Color fondo) {
		this.fondo = fondo;
		regenerar();
	}

	public void setFondoPagina(Color fondoPagina) {
		this.fondoPagina = fondoPagina;
		regenerar();
	}

	public void setLinea(Color linea) {
		this.linea = linea;
		regenerar();
	}

	private void regenerar() {
		if (iconImage != null) {
			generarImagen(iconImage.getWidth(), iconImage.getHeight());
		}
	}
}
