package vista;

import control.Joc;
import control.Main;
import control.ResultatAccio;
import control.Verb;
import model.Album;
import model.Connexio;
import model.EstatPartida;
import model.FaseDelDia;
import model.Inventari;
import model.Zona;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// finestra del joc en estil novel.la visual: la imatge a dalt amb l'hora, el text en un requadre fosc, les sortides i el camp d'ordres. els botons escriuen el mateix text i passen pel mateix analitzador
public class VistaGrafica implements Vista {

    // COLORS I MIDES
    private static final Color FONS = new Color(22, 23, 30);
    private static final Color CAIXA = new Color(34, 36, 46);
    private static final Color TEXT = new Color(232, 229, 222);
    private static final Color SUAU = new Color(140, 143, 158);
    private static final Color ACCENT = new Color(232, 164, 86);
    private static final Color ACCENT_SOBRE = new Color(244, 186, 116);
    private static final Color BOTO = new Color(48, 51, 66);
    private static final Color BOTO_SOBRE = new Color(68, 72, 92);
    private static final Color ALERTA = new Color(240, 110, 90);
    private static final String LLETRA = "Segoe UI";

    private static final int AMPLE = 840;
    private static final int ALT_IMATGE = 360;
    private static final int ALT_TEXT = 200;

    private final Joc joc;
    private final GestorImatges gestorImatges = new GestorImatges();

    private JFrame finestra;
    private PanellImatge panellImatge;
    private JTextPane areaText;
    private CampOrdre campOrdre;
    private JPanel panellSortides;
    private JButton[] botonsVerbs;
    private JButton botoEnviar;

    private Style estilNormal;
    private Style estilSuau;
    private Style estilOrdre;
    private Style estilTitol;

    public VistaGrafica(Joc joc) {
        this.joc = joc;
    }

    public void obrir() {
        SwingUtilities.invokeLater(this::construir);
    }

    private void construir() {
        finestra = new JFrame("L'Excursió del Puig de les Bruixes");
        finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel arrel = new JPanel(new BorderLayout());
        arrel.setBackground(FONS);
        finestra.setContentPane(arrel);

        panellImatge = new PanellImatge();
        panellImatge.setPreferredSize(new Dimension(AMPLE, ALT_IMATGE));
        arrel.add(panellImatge, BorderLayout.NORTH);
        arrel.add(construirCaixaText(), BorderLayout.CENTER);
        arrel.add(construirPanellInferior(), BorderLayout.SOUTH);

        finestra.pack();
        finestra.setMinimumSize(finestra.getSize());
        finestra.setLocationRelativeTo(null);
        finestra.setVisible(true);

        mostrarText(joc.textIntroduccio());
        mostrarZona(joc.getJugador().getZonaActual(), joc.getFaseDelDia(), joc.descriureZonaActual());
        campOrdre.requestFocusInWindow();
    }

    // el requadre del text amb les cantonades arrodonides
    private JPanel construirCaixaText() {
        areaText = new JTextPane();
        areaText.setEditable(false);
        areaText.setOpaque(false);
        areaText.setBorder(BorderFactory.createEmptyBorder());
        crearEstils();

        JScrollPane scroll = new JScrollPane(areaText);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUI(new BarraFina());
        scroll.getVerticalScrollBar().setOpaque(false);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        // sense una alçada propia, pack() li donava zero pixels i el text no es veia
        scroll.setPreferredSize(new Dimension(1, ALT_TEXT));

        PanellArrodonit caixa = new PanellArrodonit(CAIXA, 16);
        caixa.setLayout(new BorderLayout());
        caixa.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 8));
        caixa.add(scroll);

        JPanel marge = new JPanel(new BorderLayout());
        marge.setBackground(FONS);
        marge.setBorder(BorderFactory.createEmptyBorder(4, 14, 12, 14));
        marge.add(caixa);
        return marge;
    }

    private void crearEstils() {
        estilNormal = areaText.addStyle("normal", null);
        StyleConstants.setFontFamily(estilNormal, LLETRA);
        StyleConstants.setFontSize(estilNormal, 14);
        StyleConstants.setForeground(estilNormal, TEXT);

        estilSuau = areaText.addStyle("suau", estilNormal);
        StyleConstants.setForeground(estilSuau, SUAU);

        estilOrdre = areaText.addStyle("ordre", estilNormal);
        StyleConstants.setForeground(estilOrdre, ACCENT);
        StyleConstants.setBold(estilOrdre, true);

        estilTitol = areaText.addStyle("titol", estilNormal);
        StyleConstants.setForeground(estilTitol, ACCENT);
        StyleConstants.setBold(estilTitol, true);
        StyleConstants.setFontSize(estilTitol, 16);

        // una mica d'aire entre linies
        SimpleAttributeSet paragraf = new SimpleAttributeSet();
        StyleConstants.setLineSpacing(paragraf, 0.15f);
        areaText.setParagraphAttributes(paragraf, false);
    }

    private JPanel construirPanellInferior() {
        JPanel inferior = new JPanel();
        inferior.setLayout(new BoxLayout(inferior, BoxLayout.Y_AXIS));
        inferior.setBackground(FONS);
        inferior.setBorder(BorderFactory.createEmptyBorder(0, 14, 14, 14));

        panellSortides = new JPanel();
        panellSortides.setLayout(new BoxLayout(panellSortides, BoxLayout.X_AXIS));
        panellSortides.setOpaque(false);
        panellSortides.setAlignmentX(Component.LEFT_ALIGNMENT);
        inferior.add(panellSortides);
        inferior.add(Box.createVerticalStrut(10));

        JPanel filaOrdre = new JPanel(new BorderLayout(10, 0));
        filaOrdre.setOpaque(false);
        filaOrdre.setAlignmentX(Component.LEFT_ALIGNMENT);
        campOrdre = new CampOrdre("Què fas? Escriu una ordre, per exemple AGAFAR LA CANTIMPLORA");
        campOrdre.addActionListener(e -> enviar(campOrdre.getText()));
        filaOrdre.add(campOrdre, BorderLayout.CENTER);
        filaOrdre.add(crearBotonsVerbs(), BorderLayout.EAST);
        inferior.add(filaOrdre);
        return inferior;
    }

    // pocs botons: nomes les ordres que no porten complement, l'ajuda i enviar
    private JPanel crearBotonsVerbs() {
        JButton mirar = new BotoPla("Mirar", BOTO, BOTO_SOBRE, TEXT);
        mirar.addActionListener(e -> onBotoVerb(Verb.MIRAR));
        JButton motxilla = new BotoPla("Motxilla", BOTO, BOTO_SOBRE, TEXT);
        motxilla.addActionListener(e -> onBotoVerb(Verb.INVENTARI));
        JButton ajuda = new BotoPla("?", BOTO, BOTO_SOBRE, TEXT);
        ajuda.setToolTipText("Ajuda");
        ajuda.addActionListener(e -> mostrarText(Main.AJUDA, estilSuau));
        botoEnviar = new BotoPla("Fes-ho", ACCENT, ACCENT_SOBRE, FONS);
        botoEnviar.addActionListener(e -> enviar(campOrdre.getText()));

        botonsVerbs = new JButton[] {mirar, motxilla, ajuda};

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setOpaque(false);
        p.add(mirar);
        p.add(Box.createHorizontalStrut(6));
        p.add(motxilla);
        p.add(Box.createHorizontalStrut(6));
        p.add(ajuda);
        p.add(Box.createHorizontalStrut(10));
        p.add(botoEnviar);
        return p;
    }

    private void onBotoVerb(Verb v) {
        enviar(v.name());
    }

    private void enviar(String text) {
        if (text == null || text.trim().isEmpty() || joc.getEstat().esFinal()) {
            return;
        }
        String net = text.trim();
        campOrdre.setText("");
        if (net.equalsIgnoreCase("ajuda") || net.equals("?")) {
            mostrarText(Main.AJUDA, estilSuau);
            return;
        }

        Zona abans = joc.getJugador().getZonaActual();
        afegir("> " + net + "\n", estilOrdre);
        ResultatAccio r = joc.processarEntrada(net);
        mostrarText(r.getText());

        if (joc.getEstat().esFinal()) {
            refrescar();
            mostrarFinal(joc.getEstat(), joc.getAlbum(), joc.textFinal());
            return;
        }
        // la descripcio nomes es torna a escriure quan canvies de zona
        Zona ara = joc.getJugador().getZonaActual();
        if (ara != abans) {
            mostrarZona(ara, joc.getFaseDelDia(), joc.descriureZonaActual());
        } else {
            refrescar();
        }
    }

    // torna a pintar la imatge, l'hora i les sortides
    private void refrescar() {
        Zona z = joc.getJugador().getZonaActual();
        FaseDelDia fase = joc.getFaseDelDia();
        boolean fosc = z.esFosca() && !joc.getJugador().teLlumEncesa();
        panellImatge.posar(gestorImatges.carregar(z.getImatge(fase)), z.getNom(),
                joc.getRellotge().getHoraFormatada(), joc.getRellotge().minutsRestants(), fosc);
        refrescarSortides(z);
    }

    // un boto per cada sortida visible, amb una fletxa segons la direccio
    private void refrescarSortides(Zona z) {
        panellSortides.removeAll();
        boolean primera = true;
        for (Connexio c : z.getConnexions()) {
            if (!c.esVisible()) {
                continue;
            }
            if (!primera) {
                panellSortides.add(Box.createHorizontalStrut(8));
            }
            JButton b = new BotoPla(fletxa(c.getDireccio()) + "   " + c.getDesti().getNom(),
                    BOTO, BOTO_SOBRE, TEXT);
            b.addActionListener(e -> enviar("ANAR " + c.getDireccio()));
            panellSortides.add(b);
            primera = false;
        }
        panellSortides.revalidate();
        panellSortides.repaint();
    }

    private String fletxa(String direccio) {
        switch (direccio.toLowerCase()) {
            case "nord": return "↑";
            case "sud":  return "↓";
            case "est":  return "→";
            case "oest": return "←";
            default:     return "•";
        }
    }

    // VISTA

    @Override
    public void mostrarZona(Zona z, FaseDelDia fase, String descripcio) {
        refrescar();
        mostrarText(descripcio != null ? descripcio : joc.descriureZonaActual());
    }

    @Override
    public void mostrarText(String text) {
        mostrarText(text, estilNormal);
    }

    // les linies "== zona ==" surten com a titol i la de sortides en gris
    private void mostrarText(String text, Style estil) {
        if (text == null || text.trim().isEmpty()) {
            return;
        }
        for (String linia : text.replaceAll("\\s+$", "").split("\n")) {
            String l = linia.trim();
            if (l.startsWith("==") && l.endsWith("==") && l.length() > 4) {
                afegir(l.substring(2, l.length() - 2).trim() + "\n", estilTitol);
            } else if (l.startsWith("Sortides:")) {
                afegir(linia + "\n", estilSuau);
            } else {
                afegir(linia + "\n", estil);
            }
        }
        afegir("\n", estil);
    }

    private void afegir(String text, Style estil) {
        StyledDocument doc = areaText.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), text, estil);
        } catch (BadLocationException e) {
            // no passa: sempre s'afegeix al final
        }
        areaText.setCaretPosition(doc.getLength());
    }

    @Override
    public void mostrarInventari(Inventari i) {
        mostrarText(i.llistar());
    }

    // no bloqueja esperant, les ordres arriben pels listeners
    @Override
    public String llegirOrdre() {
        return null;
    }

    @Override
    public void mostrarFinal(EstatPartida estat, Album album, String epileg) {
        afegir("FI DE LA PARTIDA\n", estilTitol);
        mostrarText(epileg);
        mostrarText(album.mostrarGaleria(), estilSuau);
        campOrdre.setEnabled(false);
        botoEnviar.setEnabled(false);
        for (JButton b : botonsVerbs) {
            b.setEnabled(false);
        }
        panellSortides.removeAll();
        JButton altra = new BotoPla("Jugar una altra partida", ACCENT, ACCENT_SOBRE, FONS);
        altra.addActionListener(e -> reiniciar());
        panellSortides.add(altra);
        panellSortides.revalidate();
        panellSortides.repaint();
    }

    private void reiniciar() {
        joc.reiniciar();
        areaText.setText("");
        campOrdre.setEnabled(true);
        botoEnviar.setEnabled(true);
        for (JButton b : botonsVerbs) {
            b.setEnabled(true);
        }
        mostrarText(joc.textIntroduccio());
        mostrarZona(joc.getJugador().getZonaActual(), joc.getFaseDelDia(), joc.descriureZonaActual());
        campOrdre.requestFocusInWindow();
    }

    // COMPONENTS

    // la imatge de la zona. l'escala sense deformar-la, hi posa el nom i l'hora a sobre i la fosqueja si no hi veus
    private static class PanellImatge extends JPanel {

        private ImageIcon imatge;
        private String nomZona = "";
        private String hora = "";
        private int restants;
        private boolean fosc;

        void posar(ImageIcon imatge, String nomZona, String hora, int restants, boolean fosc) {
            this.imatge = imatge;
            this.nomZona = nomZona;
            this.hora = hora;
            this.restants = restants;
            this.fosc = fosc;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            int w = getWidth();
            int h = getHeight();

            if (imatge != null) {
                // omple el panell mantenint la proporcio i retalla el que sobra
                double s = Math.max((double) w / imatge.getIconWidth(), (double) h / imatge.getIconHeight());
                int dw = (int) Math.ceil(imatge.getIconWidth() * s);
                int dh = (int) Math.ceil(imatge.getIconHeight() * s);
                g2.drawImage(imatge.getImage(), (w - dw) / 2, (h - dh) / 2, dw, dh, null);
            } else {
                g2.setColor(new Color(60, 84, 64));
                g2.fillRect(0, 0, w, h);
            }

            if (fosc) {
                g2.setColor(new Color(0, 0, 0, 215));
                g2.fillRect(0, 0, w, h);
            }

            // la part de baix es fon amb el fons de la finestra
            g2.setPaint(new GradientPaint(0, h - 50, new Color(22, 23, 30, 0), 0, h, FONS));
            g2.fillRect(0, h - 50, w, 50);

            String detall = hora + "   ·   queden " + restants + " min";
            Font gran = new Font(LLETRA, Font.BOLD, 16);
            Font petita = new Font(LLETRA, Font.PLAIN, 12);
            FontMetrics mg = g2.getFontMetrics(gran);
            FontMetrics mp = g2.getFontMetrics(petita);
            int ample = Math.max(mg.stringWidth(nomZona), mp.stringWidth(detall)) + 28;

            g2.setColor(new Color(15, 16, 22, 185));
            g2.fillRoundRect(14, 14, ample, 52, 14, 14);
            g2.setFont(gran);
            g2.setColor(TEXT);
            g2.drawString(nomZona, 28, 36);
            g2.setFont(petita);
            // quan queda poc temps l'hora es posa vermella
            g2.setColor(restants <= 20 ? ALERTA : SUAU);
            g2.drawString(detall, 28, 56);
            g2.dispose();
        }
    }

    // panell amb les cantonades arrodonides (el requadre del text)
    private static class PanellArrodonit extends JPanel {

        private final Color color;
        private final int radi;

        PanellArrodonit(Color color, int radi) {
            this.color = color;
            this.radi = radi;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radi, radi);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // boto pla i arrodonit que s'aclareix en passar-hi el ratoli
    private static class BotoPla extends JButton {

        private final Color normal;
        private final Color sobre;
        private boolean ratoliASobre;

        BotoPla(String text, Color normal, Color sobre, Color colorText) {
            super(text);
            this.normal = normal;
            this.sobre = sobre;
            setForeground(colorText);
            setFont(new Font(LLETRA, Font.PLAIN, 13));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    ratoliASobre = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    ratoliASobre = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color c = !isEnabled() ? normal.darker() : (ratoliASobre ? sobre : normal);
            g2.setColor(c);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // camp de text fosc i arrodonit que mostra un exemple mentre esta buit
    private static class CampOrdre extends JTextField {

        private final String exemple;

        CampOrdre(String exemple) {
            this.exemple = exemple;
            setFont(new Font(LLETRA, Font.PLAIN, 14));
            setForeground(TEXT);
            setCaretColor(ACCENT);
            setSelectionColor(new Color(90, 94, 120));
            setSelectedTextColor(TEXT);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(9, 14, 9, 14));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setColor(CAIXA);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            if (getText().isEmpty() && isEnabled()) {
                g2.setColor(SUAU);
                g2.setFont(getFont());
                Insets in = getInsets();
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(exemple, in.left, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // barra de desplaçament prima i fosca, sense fletxes
    private static class BarraFina extends BasicScrollBarUI {

        @Override
        protected void configureScrollBarColors() {
            thumbColor = new Color(80, 84, 104);
            trackColor = CAIXA;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return botoBuit();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return botoBuit();
        }

        private JButton botoBuit() {
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(0, 0));
            b.setMinimumSize(new Dimension(0, 0));
            b.setMaximumSize(new Dimension(0, 0));
            return b;
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            // sense pista, nomes es veu el polze
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            if (r.isEmpty()) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            g2.fillRoundRect(r.x + 1, r.y, r.width - 2, r.height, 6, 6);
            g2.dispose();
        }
    }
}
