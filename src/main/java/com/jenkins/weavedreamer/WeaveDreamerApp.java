/*
 * WeavingSimulatorApp.java
 *
 * Created on March 31, 2003, 10:31 PM
 *
 * Copyright 2003 Adam P. Jenkins
 *
 * This file is part of WeavingSimulator
 *
 * WeavingSimulator is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * WeavingSimulator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WeavingSimulator; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.jenkins.weavedreamer;

import com.jenkins.weavedreamer.datatypes.WIFIO;
import com.jenkins.weavedreamer.models.EditingSession;
import com.jenkins.weavingsimulator.datatypes.Palette;
import com.jenkins.weavingsimulator.datatypes.WeavingDraft;
import com.jenkins.wifio.WIFException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

/**
 * @author ajenkins
 */
public class WeaveDreamerApp extends javax.swing.JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final String WIF_EXTENSION = ".wif";
    private static final String DRAFT_EXTENSION = ".wsml";

    /**
     * Creates new form WeaveDreamerApp
     */
    public WeaveDreamerApp() {
        helpFile = new File("help.html");
        initComponents();
        setName("Weaving Simulator");
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        setBounds(prefs.getInt("x", getX()),
                prefs.getInt("y", getY()),
                prefs.getInt("w", getWidth()),
                prefs.getInt("h", getHeight()));

        if (prefs.getBoolean("showGettingStarted", true)) {
            gettingStartedWindow = new GettingStartedWindow();
            mainDesktop.add(gettingStartedWindow);
            gettingStartedWindow.show();
        }

        fileChooser = new JFileChooser(prefs.get("last_browse", ""));
        fileChooser.setAcceptAllFileFilterUsed(false);

        fileChooser.addChoosableFileFilter(new DraftFileFilter());
        fileChooser.addChoosableFileFilter(new WifFileFilter());

        InputStream imgStream = getClass().getResourceAsStream("icon.png");
        try {
            setIconImage(ImageIO.read(imgStream));
        } catch (IOException e) {
        }
    }

    private void initComponents() {
        mainDesktop = new javax.swing.JDesktopPane();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        printMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        propertiesMenuItem = new javax.swing.JMenuItem();
        tiledViewMenuItem = new javax.swing.JMenuItem();
        windowMenu = new javax.swing.JMenu();
        helpMenu = new javax.swing.JMenu();
        contentsMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        setTitle("Weave Dreamer");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        getContentPane().add(mainDesktop, java.awt.BorderLayout.CENTER);

        fileMenu.setMnemonic('f');
        fileMenu.setText("File");
        newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        newMenuItem.setMnemonic('n');
        newMenuItem.setText("New");
        newMenuItem.addActionListener(this::newMenuItemActionPerformed);

        fileMenu.add(newMenuItem);

        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        openMenuItem.setMnemonic('o');
        openMenuItem.setText("Open");
        openMenuItem.addActionListener(this::openMenuItemActionPerformed);

        fileMenu.add(openMenuItem);

        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        saveMenuItem.setMnemonic('s');
        saveMenuItem.setText("Save");
        saveMenuItem.addActionListener(this::saveMenuItemActionPerformed);

        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setMnemonic('a');
        saveAsMenuItem.setText("Save As ...");
        saveAsMenuItem.addActionListener(this::saveAsMenuItemActionPerformed);

        fileMenu.add(saveAsMenuItem);

        printMenuItem.setMnemonic('p');
        printMenuItem.setText("Print");
        printMenuItem.addActionListener(this::printMenuItemActionPerformed);

        fileMenu.add(printMenuItem);

        exitMenuItem.setMnemonic('x');
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(this::exitMenuItemActionPerformed);

        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setMnemonic('e');
        editMenu.setText("Edit");
        propertiesMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        propertiesMenuItem.setMnemonic('p');
        propertiesMenuItem.setText("Edit Properties");
        propertiesMenuItem.addActionListener(this::propertiesMenuItemActionPerformed);

        javax.swing.JMenuItem undoMenuItem = new javax.swing.JMenuItem();
        undoMenuItem.setText("Undo");
        undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Z, ActionEvent.CTRL_MASK));

        undoMenuItem.addActionListener(this::undoItemActionPerformed);
        editMenu.add(undoMenuItem);

        javax.swing.JMenuItem redoMenuItem = new javax.swing.JMenuItem();
        redoMenuItem.setText("Redo");
        redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Y, ActionEvent.CTRL_MASK));

        redoMenuItem.addActionListener(this::redoItemActionPerformed);
        editMenu.add(redoMenuItem);

        editMenu.add(propertiesMenuItem);

        savePaletteMenuItem = new javax.swing.JMenuItem();
        savePaletteMenuItem.setMnemonic('p');
        savePaletteMenuItem.setText("Save Palette ...");
        savePaletteMenuItem.addActionListener(this::savePaletteItemActionPerformed);
        editMenu.add(savePaletteMenuItem);
        menuBar.add(editMenu);

        javax.swing.JMenu viewMenu = new javax.swing.JMenu();
        viewMenu.setMnemonic('v');
        viewMenu.setText("View");

        javax.swing.JMenuItem zoomInMenuItem = new javax.swing.JMenuItem();
        zoomInMenuItem.setText("Zoom In");
        zoomInMenuItem.addActionListener(this::zoomInItemActionPerformed);
        viewMenu.add(zoomInMenuItem);

        javax.swing.JMenuItem zoomOutMenuItem = new javax.swing.JMenuItem();
        zoomOutMenuItem.setText("Zoom Out");
        zoomOutMenuItem.addActionListener(this::zoomOutItemActionPerformed);
        viewMenu.add(zoomOutMenuItem);

        viewMenu.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                boolean enabled = mainDesktop.getSelectedFrame() instanceof WeavingDraftWindow;
                zoomInMenuItem.setEnabled(enabled);
                zoomOutMenuItem.setEnabled(enabled);
            }

            public void menuDeselected(MenuEvent e) {
            }

            public void menuCanceled(MenuEvent e) {
            }
        });

        tiledViewMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        tiledViewMenuItem.setMnemonic('t');
        tiledViewMenuItem.setText("Tiled View");
        tiledViewMenuItem.addActionListener(this::tiledViewMenuItemActionPerformed);
        viewMenu.add(tiledViewMenuItem);
        menuBar.add(viewMenu);

        windowMenu.setText("Windows");
        windowMenu.setMnemonic('w');
        windowMenu.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                windowMenuSelected(e);
            }

            public void menuDeselected(MenuEvent e) {
            }

            public void menuCanceled(MenuEvent e) {
            }
        });
        menuBar.add(windowMenu);

        helpMenu.setMnemonic('h');
        helpMenu.setText("Help");

        if (helpFile.canRead()) {
            contentsMenuItem.setText("Contents");
            contentsMenuItem.addActionListener(this::helpContentsActionPerformed);
            helpMenu.add(contentsMenuItem);
        }

        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(evt -> helpAboutMenuItemActionPerformed());
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setSize(new java.awt.Dimension(400, 400));
        setLocation((screenSize.width - 400) / 2, (screenSize.height - 400) / 2);
    }

    protected void windowMenuSelected(MenuEvent e) {
        windowMenu.removeAll();
        // Prevent the task bar's view of the window causing a duplicate entry.
        for (JInternalFrame w : new HashSet<>(asList(mainDesktop.getAllFrames()))) {
            JMenuItem item = new JMenuItem(w.getTitle());
            item.addActionListener(evt -> {
                try {
                    w.setSelected(true);
                } catch (PropertyVetoException ex) {
                }
                w.setVisible(true);
            });
            windowMenu.add(item);
        }
    }

    protected void savePaletteItemActionPerformed(ActionEvent evt) {
        String name = javax.swing.JOptionPane.showInputDialog("Name your palette", this);
        if (name != null) {
            savePalette(name, getCurrentSession().getPalette());
        }
    }

    protected void zoomInItemActionPerformed(ActionEvent evt) {
        WeavingDraftWindow curFrame
                = (WeavingDraftWindow) mainDesktop.getSelectedFrame();
        if (curFrame == null) {
            return;
        }
        curFrame.zoomIn();
    }

    protected void zoomOutItemActionPerformed(ActionEvent evt) {
        WeavingDraftWindow curFrame
                = (WeavingDraftWindow) mainDesktop.getSelectedFrame();
        if (curFrame == null) {
            return;
        }
        curFrame.zoomOut();
    }

    private void undoItemActionPerformed(ActionEvent evt) {
        getCurrentSession().undo();
    }

    private void redoItemActionPerformed(ActionEvent evt) {
        getCurrentSession().redo();
    }

    private void tiledViewMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tiledViewMenuItemActionPerformed
        EditingSessionWindow curFrame
                = (EditingSessionWindow) mainDesktop.getSelectedFrame();
        if (curFrame == null) {
            return;
        }
        curFrame.displayTiledView();
    }//GEN-LAST:event_tiledViewMenuItemActionPerformed

    private void helpAboutMenuItemActionPerformed() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("about.txt")));
            String about = reader.lines().collect(Collectors.joining("\n"));
            JOptionPane.showMessageDialog(this, about, "About", JOptionPane.INFORMATION_MESSAGE);
        } catch (HeadlessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void helpContentsActionPerformed(ActionEvent evt) {
        try {
            Desktop.getDesktop().browse(helpFile.toURI());
        } catch (IOException ex) {
        }
    }

    private void propertiesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_propertiesMenuItemActionPerformed
        EditingSession session = getCurrentSession();
        if (session == null) {
            return;
        }
        WeavingDraftPropertiesDialog dlg
                = new WeavingDraftPropertiesDialog(this, true);
        dlg.editProperties(session, loadPalettes());

    }//GEN-LAST:event_propertiesMenuItemActionPerformed

    private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuItemActionPerformed
        EditingSession session = getCurrentSession();
        if (session != null) {
            saveAsWeavingDraft(session);
        }
    }//GEN-LAST:event_saveAsMenuItemActionPerformed

    private void printMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuItemActionPerformed
        WeaveDreamerPrintSelect dlg = new WeaveDreamerPrintSelect(this, true);
        WeavingDraftWindow curFrame = null;

        EditingSession session = getCurrentSession();
        try {
            curFrame = (WeavingDraftWindow) mainDesktop.getSelectedFrame();
        } catch (ClassCastException e) {
            JOptionPane.showMessageDialog(this,
                    "Cannot Print Network Information Directly", "Print Error", JOptionPane.ERROR_MESSAGE);
        }

        if (session != null && curFrame != null) {
            dlg.PrintWeavingDraft(session, curFrame);

            //printWeavingDraft(session);
        }
    }

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        EditingSession session = getCurrentSession();
        if (session != null) {
            saveWeavingToFile(session);
        }
    }//GEN-LAST:event_saveMenuItemActionPerformed

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
        try {
            openWeavingDraft(null);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not open file: "
                    + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(this, "Failed to read file");
        }
    }//GEN-LAST:event_openMenuItemActionPerformed

    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItemActionPerformed
        WeavingDraft draft = new WeavingDraft("*New Draft " + newFileNum++);
        WeavingDraftPropertiesDialog dlg
                = new WeavingDraftPropertiesDialog(this, true);

        EditingSession session = new EditingSession(draft);
        if (!dlg.editProperties(session, loadPalettes())) {
            return;
        }
        dlg.saveDefaults();
        //getCurrentSession().setPalette(dlg.getSelectedPalette());
        openWeavingDraftWindow(session, null);
    }//GEN-LAST:event_newMenuItemActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        if (closeAllFrames()) {
            System.exit(0);
        }
    }//GEN-LAST:event_exitMenuItemActionPerformed

    /**
     * Exit the Application
     */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        if (fileChooser.getSelectedFile() != null && fileChooser.getSelectedFile().getParent() != null) {
            prefs.put("last_browse", fileChooser.getSelectedFile().getParent());
        }
        prefs.putInt("x", getX());
        prefs.putInt("y", getY());
        prefs.putInt("w", getWidth());
        prefs.putInt("h", getHeight());
        if (gettingStartedWindow != null) {
            prefs.putBoolean("showGettingStarted", gettingStartedWindow.getShow());
        }
        if (closeAllFrames()) {
            System.exit(0);
        }
    }//GEN-LAST:event_exitForm

    private EditingSession getCurrentSession() {
        EditingSessionWindow curFrame
                = (EditingSessionWindow) mainDesktop.getSelectedFrame();
        if (curFrame == null) {
            return null;
        } else {
            return curFrame.getSession();
        }
    }

    private boolean closeAllFrames() {
        /* close all internal frames, so that close event will be called
         * on them. Returns false if the close was cancelled.*/
        JInternalFrame[] wins = mainDesktop.getAllFrames();
        for (int i = 0; i < wins.length; i++) {
            try {
                wins[i].doDefaultCloseAction();
                //wins[i].setClosed(true);
            } catch (Exception e) {

            }
        }
        return mainDesktop.getAllFrames().length == 0;
    }

    private List<Palette> loadPalettes() {
        List<Palette> palettes = null;
        try {
            palettes = Palette.loadPalettes(Preferences.userNodeForPackage(this.getClass()).node("Palettes"));
        } catch (IOException e) {
        } catch (BackingStoreException e) {
        }
        if (palettes == null || palettes.size() == 0) {
            palettes = Palette.getDefaultPalettes();
        }
        return palettes;
    }

    private void savePalette(String name, Palette palette) {
        Preferences prefs = Preferences.userNodeForPackage(this.getClass()).node("Palettes");
        List<Palette> palettes = loadPalettes();
        // Save a clone, since the most likely case is we are saving 
        // a palette from an existing draft which may have no name or
        // a different name.
        palettes.add(new Palette(palette.getColors(), name));

        try {
            Palette.savePalettes(palettes, prefs);
        } catch (BackingStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }

        WeaveDreamerApp app = new WeaveDreamerApp();
        if (args.length > 0) {
            File draftFile = new File(args[0]);
            try {
                app.openWeavingDraft(draftFile);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(app, "Could not open file: "
                        + e.getMessage());
            } catch (ArrayIndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(app, "Failed to read file");
            }
        }
        app.setVisible(true);
    }

    private void saveWeavingToFile(EditingSession session) {
        File file = session.getFile();
        if (file == null) { // hasn't been saved yet
            saveAsWeavingDraft(session);
            return;
        }
        WeavingDraft draft = session.getDraft();
        OutputStream outs;
        try {
            outs = new java.io.FileOutputStream(file);
            if (session.getSaveasDraft()) {
                writeWeavingDraft(draft, outs);
            } else {
                writeWIF(draft, outs);
            }
            outs.close();
            session.setDraftModified(false);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save " + file + ": " + e.getMessage(),
                    "Save Error", JOptionPane.ERROR_MESSAGE);

        }
    }

    private void saveAsWeavingDraft(EditingSession session) {
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setSelectedFile(new File(""));
        int res = fileChooser.showSaveDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            if (fileChooser.getFileFilter().getClass() == WifFileFilter.class) {
                session.setSaveasDraft(false);

            } else if (fileChooser.getFileFilter().getClass() == DraftFileFilter.class) {
                session.setSaveasDraft(true);
            } else {
                System.out.println("borked");
            }

            File file = fileChooser.getSelectedFile();
            session.setFile(file);
            saveWeavingToFile(session);
        }
    }

    private void openWeavingDraft(File file) throws IOException {

        if (file == null) {
            fileChooser.setAcceptAllFileFilterUsed(true);
            int res = fileChooser.showOpenDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
            } else {
                return;
            }
        }

        WeavingDraft draft = null;
        try {
            InputStream ins = new java.io.FileInputStream(file);
            if (file.getName().toLowerCase().endsWith(WIF_EXTENSION)) {
                WIFIO io = new WIFIO();
                try {
                    draft = io.readWeavingDraft(ins);
                    draft.setName(file.getName());

                } catch (WIFException e) {
                    reportWifFailure(file);
                    return;
                } catch (NullPointerException e) {
                    reportWifFailure(file);
                    return;
                }
            } else {
                draft = readWeavingDraft(ins);
            }
            ins.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to open " + file
                    + ": " + e.getMessage(), "Open Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        openWeavingDraftWindow(new EditingSession(draft, !file.getName().toLowerCase().endsWith(WIF_EXTENSION)), file);
    }

    private void reportWifFailure(File file) {
        JOptionPane.showMessageDialog(this,
                "The file could not be opened.\n"
                        + "This may be because it uses features of WIF that are not yet\n"
                        + "supported. Please send a copy of the file to \n"
                        + "peterhammond@users.sf.net so we can investigate the problem.",
                "Save Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Opens a new WeavingDraftWindow displaying draft. If file is not null,
     * uses the filename as the window title.
     */
    private void openWeavingDraftWindow(EditingSession session, File file) {

        if (file != null) {
            session.setFile(file);
        }
        session.setDraftModified(false);

        EditingSessionWindow win = new WeavingDraftWindow(session);
        mainDesktop.add(win);
        try {
            win.setMaximum(true);
        } catch (java.beans.PropertyVetoException e) {
        }

        win.addInternalFrameListener(new InternalFrameAdapter() {
                                         @Override
                                         public void internalFrameClosing(InternalFrameEvent e) {
                                             EditingSessionWindow w = (EditingSessionWindow) e.getInternalFrame();
                                             EditingSession session = w.getSession();
                                             boolean canclose = false;
                                             String name = w.getTitle();
                                             if (session.getFile() != null) {
                                                 name = session.getFile().getName();
                                             }

                                             if (session.isDraftModified()) {
                                                 int res = JOptionPane.showConfirmDialog(WeaveDreamerApp.this,
                                                         "Save " + name + "?", "Save Draft",
                                                         JOptionPane.YES_NO_OPTION,
                                                         JOptionPane.QUESTION_MESSAGE);
                                                 if (res == JOptionPane.YES_OPTION) {
                                                     canclose = true;
                                                     saveWeavingToFile(session);
                                                     if (session.isDraftModified()) {
                                                         canclose = false;
                                                     }
                                                 } else {
                                                     canclose = true;
                                                 }
                                             } else {
                                                 canclose = true;
                                             }


                                             for (EditingSession.View v : session.getViews()) {
                                                 if (v != e.getSource()) {
                                                     v.closeView();
                                                 }
                                             }
                                             if (canclose) {
                                                 try {
                                                     w.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                                                     w.dispose();
                                                 } catch (Exception f) {
                                                 }


                                                 //throw new PropertyVetoException("File Not Saved", new PropertyChangeEvent(null,"",null,null));
                                             }
                                         }
                                     }

        );

        win.show();

        if (session.getDraft()
                .getNetwork() != null) {
            NetworkWindow nwWin = new NetworkWindow(session);

            mainDesktop.add(nwWin);
            nwWin.show();
        }
    }

    private void writeWeavingDraft(WeavingDraft draft, OutputStream outs)
            throws IOException {
        XMLEncoder enc = new XMLEncoder(outs);
        enc.writeObject(draft);
        enc.close();
    }

    private void writeWIF(WeavingDraft draft, OutputStream outs)
            throws IOException {
        WIFIO WifWrite = new WIFIO();
        WifWrite.writeWeavingDraft(draft, outs);
    }

    private WeavingDraft readWeavingDraft(InputStream ins) throws IOException {
        java.beans.XMLDecoder dec = new java.beans.XMLDecoder(ins);
        WeavingDraft draft = (WeavingDraft) dec.readObject();
        dec.close();
        if (draft == null) {
            throw new IOException("Failed to read Weaving Draft");
        }
        draft.validate();
        return draft;


    }


    private class DraftFileFilter extends javax.swing.filechooser.FileFilter {

        /**
         * Whether the given file is accepted by this filter.
         */
        public boolean accept(File f) {
            String name = f.getName().toLowerCase();
            return f.isDirectory() || name.endsWith(DRAFT_EXTENSION);
        }

        /**
         * The description of this filter. For example: "JPG and GIF Images"
         *
         * @see FileView#getName
         */
        public String getDescription() {
            return "Weaving Draft Files";
        }
    }

    private class WifFileFilter extends javax.swing.filechooser.FileFilter {

        /**
         * Whether the given file is accepted by this filter.
         */
        public boolean accept(File f) {
            String name = f.getName().toLowerCase();
            return f.isDirectory() || name.endsWith(WIF_EXTENSION);
        }

        /**
         * The description of this filter. For example: "JPG and GIF Images"
         *
         * @see FileView#getName
         */
        public String getDescription() {
            return "Weaving Interchange Files";
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JMenuItem tiledViewMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JDesktopPane mainDesktop;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem printMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenuItem propertiesMenuItem;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem contentsMenuItem;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenu windowMenu;

    // End of variables declaration//GEN-END:variables
    private javax.swing.JMenuItem savePaletteMenuItem;

    private GettingStartedWindow gettingStartedWindow;

    private WifFileFilter wifFilter;
    DraftFileFilter draftFilter;
    private int newFileNum = 0;
    private JFileChooser fileChooser;

    private File helpFile;
}
