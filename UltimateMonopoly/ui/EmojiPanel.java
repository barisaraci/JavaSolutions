package monopoly.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class EmojiPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private int posX, width;
	private boolean isAnimActive;
	
	JLabel label;
    
    public EmojiPanel() {
    	Timer timer = new Timer((int) 2, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isAnimActive) {
					if (posX > 350) {
						posX = 0;
						width = 0;
						isAnimActive = false;
						repaint();
					} else {
						posX += 3;
						width = 10;
						repaint();
					}
				}
			}
		});
		timer.setRepeats(true);
		timer.start();
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        
        g.setColor(Color.RED);
        g.fillRect(posX, 0, width, 50);
    }

	public JLabel getLabel() {
		return label;
	}

	public void setLabel(JLabel label) {
		this.label = label;
	}
    
	public void setAnimActive() {
		isAnimActive = true;
	}

}
