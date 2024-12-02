package com.manueee.systembreach;

import com.manueee.systembreach.view.MainMenuView;
import javax.swing.SwingUtilities;

public final class App {
    private App() {
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainMenuView::new);
    }
}
