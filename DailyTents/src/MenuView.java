
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MenuView extends JPanel {
	
	private final String[] DIMENSIONS = {"8", "12", "16", "20"};

	public MenuView(final BoardController controller) {
		setLayout(new GridBagLayout());

		JComboBox<String> dimensionList = new JComboBox<>(DIMENSIONS);
		add(dimensionList);
		
		JCheckBox checkBox = new JCheckBox("show answers");
		add(checkBox);
		
		JButton button = new JButton("start");
		add(button);
		
		button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.create(dimensionList.getSelectedIndex(), checkBox.isSelected());
            }
        });
	}

}
