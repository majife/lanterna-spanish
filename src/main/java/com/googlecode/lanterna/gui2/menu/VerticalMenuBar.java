package com.googlecode.lanterna.gui2.menu;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;

public class VerticalMenuBar extends MenuBar {

    public VerticalMenuBar() {
        super();
    }

    @Override
    protected ComponentRenderer<MenuBar> createDefaultRenderer() {
        return new DefaultVerticalMenuBarRenderer();
    }

    /**
     * Renderer que coloca cada Menu en vertical, uno debajo del otro.
     */
    public class DefaultVerticalMenuBarRenderer implements ComponentRenderer<MenuBar> {
        @Override
        public TerminalSize getPreferredSize(MenuBar menuBar) {
            int maxWidth = 0;
            int totalHeight = 0;
            for (int i = 0; i < menuBar.getMenuCount(); i++) {
                Menu menu = menuBar.getMenu(i);
                TerminalSize prefSize = menu.getPreferredSize();
                if (prefSize.getColumns() > maxWidth) {
                    maxWidth = prefSize.getColumns();
                }
                // sumamos la altura de cada Menu
                totalHeight += prefSize.getRows();
            }
            return new TerminalSize(maxWidth, totalHeight);
        }

        @Override
        public void drawComponent(TextGUIGraphics graphics, MenuBar menuBar) {
            graphics.applyThemeStyle(getThemeDefinition().getNormal());
            graphics.fill(' ');

            int topPosition = 0;
            int panelHeight = graphics.getSize().getRows();    // altura disponible
            int panelWidth  = graphics.getSize().getColumns(); // ancho disponible

            for (int i = 0; i < menuBar.getMenuCount(); i++) {
                Menu menu = menuBar.getMenu(i);
                TerminalSize menuPrefSize = menu.getPreferredSize();

                // Ajustar la altura si nos pasamos del límite
                int finalHeight = menuPrefSize.getRows();
                if (topPosition + finalHeight > panelHeight) {
                    finalHeight = panelHeight - topPosition; // recortamos si no cabe completo
                }

                // Posicionamos el menú en la columna 0 y la fila topPosition
                menu.setPosition(new TerminalPosition(0, topPosition));

                // El ancho será el máximo disponible; la altura la calculamos arriba
                menu.setSize(new TerminalSize(panelWidth, finalHeight));

                // Dibujamos el menú
                TextGUIGraphics menuGraphics = graphics.newTextGraphics(menu.getPosition(), menu.getSize());
                menu.draw(menuGraphics);

                topPosition += finalHeight;
                if (topPosition >= panelHeight) {
                    break; // no hay más espacio para menús adicionales
                }
            }
        }
    }
}
