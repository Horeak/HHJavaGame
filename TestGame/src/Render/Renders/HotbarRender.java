package Render.Renders;

import Main.MainFile;
import Render.AbstractWindowRender;
import Utils.ConfigValues;
import Utils.RenderUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class HotbarRender extends AbstractWindowRender {

	public static int slotSelected = 1;


	int startX = BlockRendering.START_X_POS ;
	int startY = BlockRendering.START_Y_POS + (ConfigValues.renderYSize * ConfigValues.size) + (BlockRendering.START_Y_POS / 2);

	@Override
	public void render(JFrame frame, Graphics2D g2) {
		Color temp = g2.getColor();
		Shape c = g2.getClip();

		//TODO Remove debug feature
		String[] blocks = new String[]{"Grass", "Dirt", "Stone"};

		Rectangle rect = new Rectangle(startX - 3, startY - 1, ConfigValues.renderXSize * ConfigValues.size + 5, ConfigValues.hotbarRenderSize + 6);
		g2.setClip(rect);

		g2.setColor(Color.GRAY);
		g2.fill(rect);

		g2.setColor(Color.black);
		g2.draw(new Rectangle(startX - 3, startY - 1, ConfigValues.renderXSize * ConfigValues.size + 4, ConfigValues.hotbarRenderSize + 5));

		int size = 79;

		for(int i = 0; i < 10; i++){
			boolean hover = false;

	      try {
		      if (frame != null && frame.getMousePosition() != null && frame.isFocused()) {
			      Point p = frame.getMousePosition();
			      if(p != null) {
				      int mouseX = p.x;
				      int mouseY = p.y;

				      int tempX = startX, tempY = startY;

				      tempX -= 1;
				      tempY += 1 + (ConfigValues.hotbarRenderSize) - 7;

				      if (mouseX > tempX && mouseY > tempY) {
					      if (mouseX < (tempX + (ConfigValues.renderXSize * ConfigValues.size)) && mouseY < (tempY + ConfigValues.hotbarRenderSize)) {
						      mouseX -= tempX;

						      int g = mouseX / 80;
						      if (i == g)
							      hover = true;
					      }
				      }
			      }
		      }
	      }catch (Exception e){
		      e.printStackTrace();
	      }

			boolean selected = (i + 1) == slotSelected;

			g2.setColor(Color.DARK_GRAY);

			if(selected)
				g2.setColor(Color.GRAY);

			if(hover)
				g2.setColor(Color.LIGHT_GRAY);

			Rectangle rectt = new Rectangle(startX - 1 + (i * size ) + (i * 1), startY + 1, size, ConfigValues.hotbarRenderSize - 1);
			g2.fill(rectt);

			g2.setColor(Color.ORANGE);

			if(hover)
				g2.setColor(Color.RED);

			g2.draw(rectt);

			g2.setColor(Color.WHITE);
			RenderUtil.resizeFont(g2, 14);

			if(selected)
			RenderUtil.changeFontStyle(g2, Font.BOLD);

			g2.drawString((i+1) + "", rectt.x + 5, rectt.y + 14);
			RenderUtil.resetFont(g2);

			//TODO Remove debug feature
			if(MainFile.currentGui == null)
			if(blocks.length > i && blocks[i] != null){
				g2.drawString(blocks[i], rectt.x + 5, rectt.y + 34);
			}

		}

		g2.setColor(temp);
		g2.setClip(c);
	}

	public void mouseClick(MouseEvent e, JFrame frame){
		try {
			if (frame != null && frame.getMousePosition() != null) {
				int mouseX = frame.getMousePosition().x;
				int mouseY = frame.getMousePosition().y;

				int tempX = startX, tempY = startY;

				tempX -= 1;
				tempY += 1 + (ConfigValues.hotbarRenderSize) - 7;

				if (mouseX > tempX && mouseY > tempY) {
					if (mouseX < (tempX + (ConfigValues.renderXSize * ConfigValues.size)) && mouseY < (tempY + ConfigValues.hotbarRenderSize)) {
						mouseX -= tempX;

						slotSelected = (mouseX / 80) + 1;
					}
				}
			}
		}catch (Exception ee){
			ee.printStackTrace();
		}
	}

	@Override
	public boolean canRender(JFrame frame) {
		return ConfigValues.RENDER_HOTBAR;
	}


	@Override
	public boolean canRenderWithGui() {
		return true;
	}
}
