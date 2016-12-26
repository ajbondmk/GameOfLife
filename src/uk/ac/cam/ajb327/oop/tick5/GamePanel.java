package uk.ac.cam.ajb327.oop.tick5;

import java.awt.Color;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

	private World mWorld = null;

	@Override
	protected void paintComponent(java.awt.Graphics g) {
		// Paint the background white
		g.setColor(java.awt.Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		if (mWorld != null) {

			int squareSize = Math.min(this.getWidth() / mWorld.getWidth(), (this.getHeight() - 30) / mWorld.getHeight());

			for (int row = 0; row < mWorld.getHeight(); row++) {
				for (int col = 0; col < mWorld.getWidth(); col++) {
					if (mWorld.getCell(col, row)) {
						g.setColor(Color.BLACK);
						g.fillRect(col*squareSize, row*squareSize, squareSize, squareSize);
					}
					else {
						g.setColor(Color.LIGHT_GRAY);
						g.drawRect(col*squareSize, row*squareSize, squareSize, squareSize);
					}
				}
			}

			g.setColor(Color.BLACK);
			g.drawString("Generation: " + mWorld.getGenerationCount(), 10, getHeight() - 10);

			// Sample drawing statements
			/*g.setColor(Color.BLACK);
			g.drawRect(200, 200, 30, 30);
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(140, 140, 30, 30);
			g.fillRect(260, 140, 30, 30);
			g.setColor(Color.BLACK);
			g.drawLine(150, 300, 280, 300);
			g.drawString("@@@", 135, 120);
			g.drawString("@@@", 255, 120);*/
		}
	}

	public void display(World w) {
		mWorld = w;
		repaint();
	}
}