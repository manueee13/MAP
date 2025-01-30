package com.manueee.systembreach;

import com.manueee.systembreach.view.MainMenuView;
import javax.swing.SwingUtilities;

/**
 * System Breach
 * Entry point dell'applicazione System Breach.
 * 
 * @version alpha 0.2
 * @author Emanuele "manueee_13" Tamborrino
 * @since 2024
 */
public final class App {
    
    /**
     * Costruttore privato.
     */
    private App() {
    }

    /**
     * Main
     * @param args Command-line arguments (non utilizzati)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainMenuView::new);
    }
}
