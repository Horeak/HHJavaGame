package Render;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


public abstract class AbstractWindowRender {
	public abstract void render(JFrame frame, Graphics2D g2);
	public abstract boolean canRender(JFrame frame);

	public void keyTyped(KeyEvent e, JFrame frame) {}
	public void keyPressed(KeyEvent e, JFrame frame) {}
	public void keyReleased(KeyEvent e, JFrame frame) {}

	public void mouseClick(MouseEvent e, JFrame frame){}

	public abstract boolean canRenderWithGui();

}
