package gui;

import javax.swing.JComboBox;

public class FileDeroulante extends JComboBox {

    public FileDeroulante() {
        super();
    }

    public void remplir(Object[] elements) {
        removeAllItems();
        if (elements != null) {
            for (Object el : elements) {
                addItem(el);
            }
        }
    }

    public void vider() {
        removeAllItems();
    }

    public Object getSelection() {
        return getSelectedItem();
    }
}
