package Render;

public abstract class AbstractWindowRender {
	public abstract void render( org.newdawn.slick.Graphics graphics );

	public abstract boolean canRender();

	public void keyPressed( int key, char c ) {
	}

	public void keyReleased( int key, char c ) {
	}

	public void mouseClick( int button, int x, int y ) {
	}

	public abstract boolean canRenderWithGui();

}
