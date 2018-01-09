package game;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener {
	
	/** 
     * Assigns the newly created InputHandler to a Component 
     * @param c Component to get input from 
     */ 
	private boolean[] keys = new boolean [256];
	public int mouseX, mouseY, mouseButton;
	
    public InputHandler(Component c) 
    { 
            c.addKeyListener(this); 
            c.addMouseListener(this);
            c.addMouseMotionListener(this);
    } 
    
    /** 
     * Checks whether a specific key is down 
     * @param keyCode The key to check 
     * @return Whether the key is pressed or not 
     */ 
    public boolean isKeyDown(int keyCode) 
    { 
            if (keyCode > 0 && keyCode < 256) 
            { 
                    return keys[keyCode]; 
            } 
            
            return false; 
    } 
    
    /** 
     * Called when a key is pressed while the component is focused 
     * @param e KeyEvent sent by the component 
     */ 
    public void keyPressed(KeyEvent e) 
    { 
            if (e.getKeyCode() > 0 && e.getKeyCode() < 256) 
            { 
                    keys[e.getKeyCode()] = true; 
            } 
    } 

    /** 
     * Called when a key is released while the component is focused 
     * @param e KeyEvent sent by the component 
     */ 
    public void keyReleased(KeyEvent e) 
    { 
            if (e.getKeyCode() > 0 && e.getKeyCode() < 256) 
            { 
                    keys[e.getKeyCode()] = false; 
            } 
    } 

    /** 
     * Not used 
     */ 
    public void keyTyped(KeyEvent e){}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseX = e.getX();
		mouseY = e.getY();
		mouseButton = e.getButton();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseButton = e.getButton();
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseButton = 0;
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseButton = e.getButton();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseButton = e.getButton();
	}
	
	public boolean isMouseButtonDown(int mouseKey) {
		if(mouseButton == mouseKey) {
			return true;
		} else {
			return false;
		}
	}
	
	public int getX() {
		return mouseX;
	}

	public void setMouseX(int mouseX) {
		this.mouseX = mouseX;
	}

	public int getY() {
		return mouseY;
	}

	public void setMouseY(int mouseY) {
		this.mouseY = mouseY;
	}

	public int getMouseButton() {
		return mouseButton;
	}

	public void setMouseButton(int mouseButton) {
		this.mouseButton = mouseButton;
	}
	
}