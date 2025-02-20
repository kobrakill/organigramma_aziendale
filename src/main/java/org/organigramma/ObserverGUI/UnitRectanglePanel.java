package org.organigramma.ObserverGUI;


import org.organigramma.composite.Unit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;


public class UnitRectanglePanel extends JPanel {
    private Unit unit;

    public UnitRectanglePanel(Unit unit, int x, int y, int width, int height) {
        this.unit = unit;
        setBounds(x, y, width, height);
        setBackground(Color.LIGHT_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setLayout(null);
        setToolTipText(unit.getName());

        addMouseListener(new MouseAdapter() {
        });
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Ottengo il font corrente e le metriche
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        String text = unit.getName();

        // Calcolo le coordinate per centrare il testo
        int x = (getWidth() - metrics.stringWidth(text)) / 2;
        int y = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();

        // Disegno il testo centrato
        g.drawString(text, x, y);
    }
}
