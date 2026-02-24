/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.airport_reservation_system;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.RoundRectangle2D;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.border.Border;

/**
 *
 * @author ADITYA
 */
public class AppTheme {
    
    public enum TransitionType {
        FADE,
        SLIDE_LEFT,
        SLIDE_RIGHT,
        ZOOM_IN
    }
    
    public static final Color PRIMARY     = new Color(30, 58, 138);
    public static final Color ACCENT      = new Color(59, 130, 246);
    public static final Color INPUT_BG    = new Color(241, 245, 249);
    public static final Color TEXT_DARK   = new Color(15, 23, 42);
    public static final Color WHITE       = Color.WHITE;
    public static final Color DANGER      = new Color(220, 38, 38);
    public static final Color SUCCESS     = new Color(22, 163, 74);
    
    public static final Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD, 32);
    public static final Font FONT_LABEL  = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_INPUT  = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);

    public static Border inputBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT, 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        );
    }
    
    public static Border buttonPadding() {
        return BorderFactory.createEmptyBorder(8, 24, 8, 24);
    }
    
    public static void addCustomTitleBar(JFrame frame) {
    JPanel titleBar = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 3));
    titleBar.setBackground(PRIMARY);
    titleBar.setPreferredSize(new Dimension(0, 35));

    // ── Close button ─────────────────────────
    JButton closeBtn = new JButton("✕");
    closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
    closeBtn.setForeground(WHITE);
    closeBtn.setBackground(DANGER);
    closeBtn.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
    closeBtn.setFocusPainted(false);
    closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    closeBtn.addActionListener(e -> System.exit(0));

    // ── Minimize button ───────────────────────
    JButton minBtn = new JButton("─");
    minBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
    minBtn.setForeground(WHITE);
    minBtn.setBackground(new Color(100, 100, 100));
    minBtn.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
    minBtn.setFocusPainted(false);
    minBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    minBtn.addActionListener(e -> frame.setState(JFrame.ICONIFIED));

    // ── Maximize button ───────────────────────
    JButton maxBtn = new JButton("□");
    maxBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
    maxBtn.setForeground(WHITE);
    maxBtn.setBackground(new Color(100, 100, 100));
    maxBtn.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
    maxBtn.setFocusPainted(false);
    maxBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    maxBtn.addActionListener(e -> {
        if (frame.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
            frame.setExtendedState(JFrame.NORMAL);
        } else {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    });

    titleBar.add(minBtn);
    titleBar.add(maxBtn);
    titleBar.add(closeBtn);

    frame.getContentPane().add(titleBar, BorderLayout.NORTH);
}
    
public static void transition(JFrame fromFrame, java.util.function.Supplier<JFrame> toFrameSupplier, TransitionType type) {
    fromFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

    SwingWorker<JFrame, Void> worker = new SwingWorker<>() {
        protected JFrame doInBackground() {
            return toFrameSupplier.get(); // ← builds frame in background
        }

        protected void done() {
            try {
                JFrame toFrame = get();
                fromFrame.setCursor(Cursor.getDefaultCursor());
                switch (type) {
                    case FADE        -> slideTransition(fromFrame, toFrame, -1);
                    case SLIDE_LEFT  -> slideTransition(fromFrame, toFrame, -1);
                    case SLIDE_RIGHT -> slideTransition(fromFrame, toFrame,  1);
                    case ZOOM_IN     -> slideTransition(fromFrame, toFrame, -1);
                    default          -> slideTransition(fromFrame, toFrame, -1);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };
    worker.execute();
}

// ─── FADE ────────────────────────────────────────────────────────────────────

private static void fadeTransition(JFrame fromFrame, JFrame toFrame) {
    boolean supported = GraphicsEnvironment
        .getLocalGraphicsEnvironment()
        .getDefaultScreenDevice()
        .isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT);

    if (!supported) {
        toFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        toFrame.setVisible(true);
        fromFrame.dispose();
        return;
    }

    toFrame.dispose();
    toFrame.setUndecorated(true);
    toFrame.setOpacity(0f);
    toFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    toFrame.setVisible(true);

    // Fade out old frame
    Timer fadeOut = new Timer(12, null);
    float[] opacity = {1.0f};
    fadeOut.addActionListener(e -> {
        opacity[0] -= 0.06f;
        if (opacity[0] <= 0f) {
            fadeOut.stop();
            fromFrame.dispose();

            // Fade in new frame
            Timer fadeIn = new Timer(12, null);
            float[] op2 = {0f};
            fadeIn.addActionListener(e2 -> {
                op2[0] += 0.06f;
                if (op2[0] >= 1f) {
                    toFrame.setOpacity(1f);
                    fadeIn.stop();
                } else {
                    toFrame.setOpacity(op2[0]);
                }
            });
            fadeIn.start();
        } else {
            fromFrame.setOpacity(opacity[0]);
        }
    });
    fadeOut.start();
}

// ─── SLIDE ───────────────────────────────────────────────────────────────────

private static void slideTransition(JFrame fromFrame, JFrame toFrame, int direction) {
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    int screenW = screen.width;
    int screenH = screen.height;

    // Position toFrame just off-screen
    toFrame.setSize(screenW, screenH);
    toFrame.setLocation(direction * screenW, 0);
    toFrame.setVisible(true);

    int[] moved = {0};
    int step = screenW / 15;

    Timer slide = new Timer(12, null);
    slide.addActionListener(e -> {
        moved[0] += step;
        int fromX = fromFrame.getX() - (direction * step);
        int toX   = toFrame.getX()   - (direction * step);

        fromFrame.setLocation(fromX, 0);
        toFrame.setLocation(toX, 0);

        if (moved[0] >= screenW) {
            slide.stop();
            fromFrame.dispose();
            toFrame.setLocation(0, 0);
            toFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    });
    slide.start();
}

// ─── ZOOM IN ─────────────────────────────────────────────────────────────────

private static void zoomTransition(JFrame fromFrame, JFrame toFrame) {
    boolean supported = GraphicsEnvironment
        .getLocalGraphicsEnvironment()
        .getDefaultScreenDevice()
        .isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT);

    if (!supported) {
        toFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        toFrame.setVisible(true);
        fromFrame.dispose();
        return;
    }
    
    toFrame.dispose();
    toFrame.setUndecorated(true);

    fromFrame.setOpacity(0f); // instantly hide source
    toFrame.setOpacity(0f);
    toFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    toFrame.setVisible(true);
    fromFrame.dispose();

    Timer zoom = new Timer(12, null);
    float[] scale = {0f};
    zoom.addActionListener(e -> {
        scale[0] += 0.06f;
        if (scale[0] >= 1f) {
            toFrame.setOpacity(1f);
            zoom.stop();
        } else {
            toFrame.setOpacity(scale[0]);
        }
    });
    zoom.start();
}

public static void styleMenuBar(javax.swing.JMenuBar menuBar) {
    menuBar.setOpaque(false);
    menuBar.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

    for (int i = 0; i < menuBar.getMenuCount(); i++) {
        javax.swing.JMenu menu = menuBar.getMenu(i);
        if (menu == null) continue;
        menu.setForeground(java.awt.Color.WHITE);
        menu.setFont(FONT_BUTTON);
        menu.setOpaque(false);
        menu.setBorderPainted(false);

        for (int j = 0; j < menu.getItemCount(); j++) {
            javax.swing.JMenuItem item = menu.getItem(j);
            if (item == null) continue;
            item.setFont(FONT_INPUT);
            item.setBackground(INPUT_BG);
            item.setForeground(TEXT_DARK);
        }
    }
}
// ─── DESKTOP PANE STYLER ─────────────────────────────────
public static void styleDesktopPane(javax.swing.JDesktopPane desktop) {
    desktop.setOpaque(false);  // ← transparent so background shows
}
    
    public static void styleComboBox(javax.swing.JComboBox<?> box) {
    box.setFont(FONT_INPUT);
    box.setBackground(INPUT_BG);
    box.setForeground(TEXT_DARK);
    box.setFocusable(false);
    box.setCursor(new Cursor(Cursor.HAND_CURSOR));
    box.setBorder(BorderFactory.createLineBorder(ACCENT, 1));
    box.setRenderer(new javax.swing.DefaultListCellRenderer() {
        @Override
        public java.awt.Component getListCellRendererComponent(
                javax.swing.JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            setFont(FONT_INPUT);
            setBackground(isSelected ? ACCENT : INPUT_BG);
            setForeground(isSelected ? WHITE : TEXT_DARK);
            setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            return this;
        }
    });
}
    public static void styleTable(javax.swing.JTable table) {
    // Header
    table.getTableHeader().setFont(FONT_LABEL);
    table.getTableHeader().setBackground(PRIMARY);
    table.getTableHeader().setForeground(WHITE);

    // Rows
    table.setFont(FONT_INPUT);
    table.setBackground(INPUT_BG);
    table.setForeground(TEXT_DARK);
    table.setRowHeight(28);
    table.setShowGrid(false);
    table.setIntercellSpacing(new java.awt.Dimension(0, 0));
    table.setSelectionBackground(ACCENT);
    table.setSelectionForeground(WHITE);
    table.setFillsViewportHeight(true);

    // Alternating row colors via renderer
    table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public java.awt.Component getTableCellRendererComponent(
                javax.swing.JTable t, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
            if (isSelected) {
                setBackground(ACCENT);
                setForeground(WHITE);
            } else {
                setBackground(row % 2 == 0 ? INPUT_BG : WHITE);
                setForeground(TEXT_DARK);
            }
            setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
            return this;
        }
    });
}

    
    public static class GlassPanel extends JPanel {
        private final int alpha;
        private final int radius;

        public GlassPanel(int alpha, int radius) {
            this.alpha = alpha;
            this.radius = radius;
            setOpaque(false);
            setLayout(new BorderLayout());
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 255, 255, alpha));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
//            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
            // ── Draw subtle border ────────────────────────────
        g2.setColor(new Color(255, 255, 255, 180));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

            g2.dispose();
            super.paintComponent(g);
        }
    }
    
    
    
    public static JPanel replaceWithGlassPanel(JFrame frame, JPanel oldPanel,
                                               int alpha, int radius) {
        GlassPanel glassPanel = new GlassPanel(alpha, radius);

        // ── Copy size hints ──────────────────────────────────
//        glassPanel.setBounds(oldPanel.getBounds());
        glassPanel.setPreferredSize(oldPanel.getPreferredSize());
        glassPanel.setMinimumSize(oldPanel.getMinimumSize());
        glassPanel.setMaximumSize(oldPanel.getMaximumSize());
//        glassPanel.setLayout(null);

        oldPanel.setOpaque(false);
        oldPanel.setBackground(new Color(0, 0, 0, 0));
        
        // ── Wrap old panel inside glass panel ────────────────
        glassPanel.setLayout(new BorderLayout());
        
        // ── Move children with bounds preserved ──────────────
//        Component[] kids   = oldPanel.getComponents();
//        Rectangle[] bounds = new Rectangle[kids.length];
        
         // Remove oldPanel from content pane
        Container contentPane = frame.getContentPane();
        LayoutManager layout  = contentPane.getLayout();

        if (layout instanceof GroupLayout groupLayout) {
            groupLayout.replace(oldPanel, glassPanel);
    }

    glassPanel.add(oldPanel, BorderLayout.CENTER);

    frame.revalidate();
    frame.repaint();

    return glassPanel;
    
//        for (int i = 0; i < kids.length; i++) {
//            bounds[i] = kids[i].getBounds();
//        }
//        for (int i = 0; i < kids.length; i++) {
//            glassPanel.add(kids[i]);
//            kids[i].setBounds(bounds[i]);
//        }
//
//        // ── Swap inside GroupLayout ───────────────────────────
//        Container contentPane = frame.getContentPane();
//        LayoutManager layout  = contentPane.getLayout();
//
//        if (layout instanceof GroupLayout) {
//            ((GroupLayout) layout).replace(oldPanel, glassPanel);
//        } else {
//            // Fallback for non-GroupLayout frames
//            contentPane.remove(oldPanel);
//            contentPane.add(glassPanel);
//        }
//
//        frame.revalidate();
//        frame.repaint();
//
//        return glassPanel;  // ← assign back to jPanel1 / jPanel2
    }
    
    public static JPanel replaceWithGlassPanel(javax.swing.JInternalFrame frame,
                                           JPanel oldPanel, int alpha, int radius) {
    GlassPanel glassPanel = new GlassPanel(alpha, radius);

    glassPanel.setPreferredSize(oldPanel.getPreferredSize());
    glassPanel.setMinimumSize(oldPanel.getMinimumSize());
    glassPanel.setMaximumSize(oldPanel.getMaximumSize());

    oldPanel.setOpaque(false);
    oldPanel.setBackground(new java.awt.Color(0, 0, 0, 0));

    glassPanel.setLayout(new java.awt.BorderLayout());

    Container contentPane = frame.getContentPane();
    LayoutManager layout  = contentPane.getLayout();

    if (layout instanceof GroupLayout groupLayout) {
            groupLayout.replace(oldPanel, glassPanel);
    }

    glassPanel.add(oldPanel, java.awt.BorderLayout.CENTER);

    frame.revalidate();
    frame.repaint();

    return glassPanel;
}


    
    private static class RoundedButtonUI extends javax.swing.plaf.basic.BasicButtonUI {
        private final Color bgColor;
        private final int radius;

        public RoundedButtonUI(Color bgColor, int radius) {
            this.bgColor = bgColor;
            this.radius = radius;
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(((AbstractButton) c).getModel().isRollover()
                ? bgColor.darker()
                : bgColor);
            g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), radius, radius);
            g2.setColor(WHITE);
            FontMetrics fm = g2.getFontMetrics(c.getFont());
            String text = ((AbstractButton) c).getText();
            int x = (c.getWidth() - fm.stringWidth(text)) / 2;
            int y = (c.getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.setFont(c.getFont());
            g2.drawString(text, x, y);
            g2.dispose();
        }
    }
    
    
    public static void styleTitle(JLabel label) {
        label.setFont(FONT_TITLE);
        label.setForeground(Color.BLACK);       // ← black
    }
    
    public static void styleLabel(JLabel label) {
        label.setFont(FONT_LABEL);
        label.setForeground(Color.BLACK);       // ← black
    }
    
    public static void styleLabels(JLabel... labels) {
        for (JLabel l : labels) styleLabel(l);
    }
    
    public static void styleTextField(JTextField field) {
        field.setFont(FONT_INPUT);
        field.setBackground(INPUT_BG);
        field.setForeground(TEXT_DARK);
        field.setCaretColor(ACCENT);
        field.setBorder(inputBorder());
    }
    
    public static void styleTextFields(JTextField... fields) {
        for (JTextField f : fields) styleTextField(f);
    }
    
    public static void styleTextArea(JTextArea area) {
        area.setFont(FONT_INPUT);
        area.setBackground(INPUT_BG);
        area.setForeground(TEXT_DARK);
        area.setCaretColor(ACCENT);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
    }
    
    public static void styleScrollPane(JScrollPane pane) {
        pane.setBorder(BorderFactory.createLineBorder(ACCENT, 1, true));
    }
    
    public static void stylePasswordField(JPasswordField field) {
        field.setFont(FONT_INPUT);
        field.setBackground(INPUT_BG);
        field.setForeground(TEXT_DARK);
        field.setCaretColor(ACCENT);
        field.setBorder(inputBorder());
    }
    
    public static void styleRadioButton(JRadioButton rb) {
        rb.setFont(FONT_INPUT);
        rb.setForeground(Color.BLACK);          // ← black
        rb.setOpaque(false);
        rb.setFocusPainted(false);
    }
    
    
    public static void stylePrimaryButton(JButton button) {
        button.setFont(FONT_BUTTON);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        button.setUI(new RoundedButtonUI(ACCENT, 30));  // ← rounded corners
    }
    
    public static void styleDangerButton(JButton button) {
        button.setFont(FONT_BUTTON);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        button.setUI(new RoundedButtonUI(DANGER, 30));  // ← rounded corners
    }
    
    public static void styleSuccessButton(JButton button) {
        button.setFont(FONT_BUTTON);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        button.setUI(new RoundedButtonUI(SUCCESS, 30)); // ← rounded corners
    }
    

    public static void styleRadioButtons(JRadioButton... buttons) {
        for (JRadioButton rb : buttons) styleRadioButton(rb);
    }
}
