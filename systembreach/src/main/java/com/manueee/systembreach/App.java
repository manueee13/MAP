package com.manueee.systembreach;

import com.manueee.systembreach.view.MainMenuView;
import javax.swing.SwingUtilities;

/**
 * <h1>System Breach</h1>
 * @version alpha 0.1
 * @author Emanuele "manueee_13" Tamborrino
 */
public final class App {
    private App() {
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainMenuView::new);
    }
}
