package com.manueee.systembreach;

import com.manueee.systembreach.view.MainMenuView;
import javax.swing.SwingUtilities;

/**
 * <h1>System Breach</h1>
 * Entry point dell'applicazione.
 * @version alpha 0.1
 * @author Emanuele "manueee_13" Tamborrino
 */
public final class App {

    private App() {
    }

    /**
     * <h2>main</h2>
     * Metodo <b>main</b> per il lancio dell'applicazione.
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainMenuView::new);
    }
}
