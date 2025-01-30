package com.manueee.systembreach.util.fonts;

import java.awt.*;
import java.io.InputStream;
import java.io.IOException;

/**
 * Utility class per la gestione dei font nell'applicazione.
 * Questa classe fa parte del layer View del pattern MVC, fornendo
 * utility per il caricamento e la gestione dei font custom.
 *
 * @author Manuel
 * @version 1.0
 */
public final class FontUtils {
    
    private static final String DEFAULT_FONT_PATH = "/fonts/BigBlueTermPlusNerdFont-Regular.ttf";
    private static final float DEFAULT_FONT_SIZE = 24f;
    private static final String FALLBACK_FONT_NAME = "Monospaced";
    
    // Costruttore privato per impedire l'istanziazione
    private FontUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Carica il font personalizzato dall'interno del jar.
     * Se il caricamento fallisce, viene utilizzato un font di fallback.
     *
     * @return Font - Il font caricato o un font di fallback
     * @throws SecurityException se non ci sono i permessi per caricare il font
     */
    public static Font loadFont() {
        try {
            InputStream fontStream = FontUtils.class.getResourceAsStream(DEFAULT_FONT_PATH);
            if (fontStream == null) {
                System.err.println("Font non trovato: " + DEFAULT_FONT_PATH);
                return createFallbackFont();
            }
            
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream)
                                .deriveFont(DEFAULT_FONT_SIZE);
            registerFont(customFont);
            return customFont;
            
        } catch (FontFormatException | IOException e) {
            System.err.println("Errore nel caricamento del font: " + e.getMessage());
            return createFallbackFont();
        }
    }

    private static Font createFallbackFont() {
        return new Font(FALLBACK_FONT_NAME, Font.PLAIN, (int)DEFAULT_FONT_SIZE);
    }

    private static void registerFont(Font font) throws SecurityException {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(font);
    }
}
