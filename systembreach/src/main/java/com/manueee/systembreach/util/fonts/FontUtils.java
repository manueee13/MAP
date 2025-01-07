package com.manueee.systembreach.util.fonts;

import java.awt.*;
import java.io.InputStream;

public final class FontUtils {
    public static Font loadFont() {
        try {
            InputStream fontStream = FontUtils.class.getResourceAsStream("/fonts/BigBlueTermPlusNerdFont-Regular.ttf");

            if (fontStream != null) {
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(24f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customFont);
                return customFont;
            } else {
                System.err.println("Font not found");
                return new Font("Monospaced", Font.PLAIN, 24);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("Monospaced", Font.PLAIN, 24);
        }
    }
}
