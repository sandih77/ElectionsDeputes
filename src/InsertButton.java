package button;

import javax.swing.JButton;

public class InsertButton extends JButton {

    JButton insertButton;

    public InsertButton(String label) {
        super(label);
    }

    public JButton getInsertButton() {
        return this.insertButton;
    }
}
