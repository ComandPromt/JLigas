
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolTip;
import javax.swing.SwingConstants;

import com.toolTip.ToolTipLlamada;

@SuppressWarnings("serial")
public class ButtonCustom extends JButton {

	private Color fondo;

	private Color colorHover;

	private Color colorPressed;

	private boolean mouseOver;

	private String text;

	private Color backgroundColor;

	private int iconWidth;

	private int iconHeight;

	private int iconAlignment = SwingConstants.RIGHT;

	private Color colorTexto;

	private Color border;

	private Font fuente;

	private boolean stretchBackgroundImage;

	private Image backgroundImage;

	private int imageAlignment;

	private float imageOpacity;

	private boolean hideTextIfImage;

	private int leftPadding;

	private boolean scaleIconToComponentHeight;

	private int iconOffsetX;

	private int iconOffsetY;

	private int iconPadding;

	private ImageIcon icon;

	private String texto;

	public void setIconPadding(int padding) {

		this.iconPadding = padding;

		repaint();

	}

	public void setIconAlign(int align) {

		if (align == SwingConstants.LEFT || align == SwingConstants.CENTER || align == SwingConstants.RIGHT) {

			this.iconAlignment = align;

			repaint();

		}

	}

	public void setIconOffsetX(int offsetX) {

		this.iconOffsetX = offsetX;

		repaint();

	}

	public void setIconOffsetY(int offsetY) {

		this.iconOffsetY = offsetY;

		repaint();

	}

	public void setIconOffset(int offsetX, int offsetY) {

		this.iconOffsetX = offsetX;

		this.iconOffsetY = offsetY;

		repaint();

	}

	public void setIconSize(int width) {

		this.iconWidth = width;

		this.scaleIconToComponentHeight = true;

		repaint();

	}

	public void setIconSize(int width, int height) {

		this.iconWidth = width;

		this.iconHeight = height;

		repaint();

	}

	@Override
	public void setText(String text) {

		texto = text;

	}

	public ButtonCustom() {

		this("");

	}

	public ButtonCustom(String text) {

		setFont(getFont().deriveFont(Font.PLAIN, 20f));

		texto = text;

		fondo = new Color(69, 191, 71);

		imageOpacity = 1.0f;

		imageAlignment = SwingConstants.CENTER;

		colorHover = new Color(76, 206, 78);

		colorPressed = new Color(63, 175, 65);

		mouseOver = false;

		setContentAreaFilled(false);

		setFocusPainted(false);

		setBorderPainted(false);

		setOpaque(true);

		init();

	}

	public void setLeft(int left) {

		this.leftPadding = left;

		repaint();

	}

	@Override
	public void setIcon(Icon icon) {

		this.icon = (ImageIcon) icon;

	}

	private void init() {

		setBorder(null);

		setCursor(new Cursor(Cursor.HAND_CURSOR));

		setBackground(fondo);

		setForeground(Color.WHITE);

		addMouseListener(new MouseAdapter() {

			@Override

			public void mouseEntered(MouseEvent e) {

				mouseOver = true;

				ButtonCustom.super.setBackground(getColorHover());

			}

			@Override

			public void mouseExited(MouseEvent e) {

				mouseOver = false;

				ButtonCustom.super.setBackground(fondo);

			}

			@Override
			public void mousePressed(MouseEvent e) {

				ButtonCustom.super.setBackground(getColorPressed());

			}

			@Override
			public void mouseReleased(MouseEvent e) {

				ButtonCustom.super.setBackground(mouseOver ? getColorHover() : fondo);

			}

		});

	}

	@Override

	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g.create();

		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		if (backgroundImage != null) {

			int compWidth = getWidth();

			int compHeight = getHeight();

			int imgWidth;

			int imgHeight;

			int x;

			int y;

			if (stretchBackgroundImage) {

				imgWidth = compWidth;

				imgHeight = compHeight;

				x = 0;

				y = 0;

			}

			else {

				imgHeight = compHeight;

				imgWidth = (int) ((double) backgroundImage.getWidth(null) / backgroundImage.getHeight(null)
						* imgHeight);
				y = 0;

				switch (imageAlignment) {

				case SwingConstants.CENTER:

					x = (compWidth - imgWidth) / 2;

					break;

				case SwingConstants.RIGHT:

					x = compWidth - imgWidth;

					break;

				default:

					x = 0;

					break;

				}

			}

			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, imageOpacity));

			g2.drawImage(backgroundImage, x, y, imgWidth, imgHeight, this);

		}

		if (!(hideTextIfImage && backgroundImage != null)) {

			Graphics2D gText = (Graphics2D) g2.create();

			gText.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

			gText.setFont(getFont());

			gText.setColor(getForeground());

			FontMetrics fm = gText.getFontMetrics();

			int textWidth = fm.stringWidth(texto);

			int textHeight = fm.getAscent();

			if (leftPadding == 0 && iconPadding > 0) {

				switch (iconAlignment) {

				case SwingConstants.LEFT:

					leftPadding = iconPadding;

					leftPadding += 10;

					break;

				case SwingConstants.RIGHT:

					leftPadding -= iconPadding;

					leftPadding -= 10;

					break;

				}

			}

			int x;

			switch (getHorizontalAlignment()) {

			case SwingConstants.LEFT:

				x = leftPadding;

				break;

			case SwingConstants.RIGHT:

				x = getWidth() - textWidth - leftPadding;

				break;

			case SwingConstants.CENTER:

			default:

				x = (getWidth() - textWidth) / 2 + leftPadding;

				break;

			}

			int y = (getHeight() + textHeight) / 2 - 2;

			gText.drawString(texto, x, y);

			gText.dispose();

		}

		if (icon != null) {

			int drawWidth = (iconWidth > 0) ? iconWidth : icon.getIconWidth();

			int drawHeight = (iconHeight > 0) ? iconHeight : icon.getIconHeight();

			if (scaleIconToComponentHeight) {

				drawHeight = getHeight();

				drawWidth = (int) ((double) icon.getIconWidth() / icon.getIconHeight() * drawHeight);

			}

			int x;

			switch (iconAlignment) {

			case SwingConstants.LEFT:

				x = iconOffsetX + iconPadding;

				break;

			case SwingConstants.RIGHT:

				x = getWidth() - drawWidth - iconOffsetX - iconPadding;

				break;

			default:

				x = (getWidth() - drawWidth) / 2 + iconOffsetX;

				break;

			}

			int y = (getHeight() - drawHeight) / 2 + iconOffsetY;

			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

			g2.drawImage(icon.getImage(), x, y, drawWidth, drawHeight, this);

		}

		g2.dispose();

	}

	public void setStretchBackgroundImage(boolean stretch) {

		this.stretchBackgroundImage = stretch;

		repaint();

	}

	public boolean isStretchBackgroundImage() {

		return stretchBackgroundImage;

	}

	@Override
	public void setBackground(Color bg) {

		fondo = bg;

		super.setBackground(bg);

	}

	public Color getColorHover() {

		return colorHover;

	}

	public void setColorHover(Color colorHover) {

		this.colorHover = colorHover;

	}

	public Color getColorPressed() {

		return colorPressed;

	}

	public void setColorPressed(Color colorPressed) {

		this.colorPressed = colorPressed;

	}

	public void setToolTip(String text, Color background, Color foreground, Color border, Font font) {

		if (background == null)
			background = new Color(32, 39, 55);

		if (foreground == null)
			foreground = Color.WHITE;

		if (border == null)
			border = new Color(173, 173, 173);

		if (font == null) {
			try {
				font = getFont().deriveFont(20f);
			} catch (Exception e) {
				font = new Font("Dialog", Font.PLAIN, 20);
			}
		}

		this.text = text;

		this.backgroundColor = background;

		this.colorTexto = foreground;

		this.border = border;

		this.fuente = font;

		setToolTipText(text);

	}

	@Override
	public JToolTip createToolTip() {

		if (text == null || backgroundColor == null || colorTexto == null || border == null) {

			return super.createToolTip();

		}

		else {

			ToolTipLlamada tip = new ToolTipLlamada(text, backgroundColor, colorTexto, border, fuente);

			tip.setComponent(this);

			return tip;

		}

	}

	public void setBackgroundImage(Image image) {

		this.backgroundImage = image;

		setOpaque(false);

		repaint();

	}

	public void setBackgroundImage(String path) {

		this.backgroundImage = new ImageIcon(path).getImage();

		setOpaque(false);

		repaint();

	}

	public void setBackgroundImageAlignment(int alignment) {

		if (alignment == SwingConstants.LEFT || alignment == SwingConstants.RIGHT
				|| alignment == SwingConstants.CENTER) {

			this.imageAlignment = alignment;

			repaint();

		}

	}

	public void setImageOpacity(float opacity) {

		this.imageOpacity = Math.max(0f, Math.min(opacity, 1f));

		repaint();

	}

	public void setHideTextIfImage(boolean hide) {

		this.hideTextIfImage = hide;

		repaint();

	}

}
