package Interface;

import Interface.Objects.GuiButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public abstract class Gui {

	public ArrayList<GuiObject> guiObjects = new ArrayList<>();

	public abstract void render(JFrame frame, Graphics2D g2);
	public abstract boolean canRender(JFrame frame);

	public boolean renderOtherWindowsRenders(){return true;}

	public void keyTyped(KeyEvent e, JFrame frame) {}
	public void keyPressed(KeyEvent e, JFrame frame) {}
	public void keyReleased(KeyEvent e, JFrame frame) {}

	public void renderObject(JFrame frame, Graphics2D g2){
		for(GuiObject object : guiObjects){
			object.renderObject(frame, g2, this);
		}
	}

	public void mouseClick(MouseEvent e, JFrame frame)
	{
		for(GuiObject object : guiObjects){
			if(object.isMouseOver(frame.getMousePosition()) && object.enabled){
				object.onClicked(e, frame, this);
			}
		}
	}

	public void buttonPressed(GuiButton button){}
}
