/*
 * WeavingDraftPropertiesDialog.java
 * 
 * Created on April 21, 2003, 3:11 PM
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


package com.jenkins.weavingsimulator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.List;
import java.util.Vector;
import java.util.prefs.Preferences;

import javax.swing.JDialog;

import com.jenkins.weavingsimulator.datatypes.Palette;
import com.jenkins.weavingsimulator.models.PalettePreviewModel;

/** A Dialog to edit the properties of a WeavingDraft.  To use this class,
 * create an instance, and then call editProperties(weavingDraft).
 *
 * @author  ajenkins
 */
public class WeavingDraftPropertiesDialog extends javax.swing.JDialog {
    
    /** Creates new form WeavingDraftPropertiesDialog */
    public WeavingDraftPropertiesDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
  	
        initComponents();
        palettes_combo.addActionListener(new ActionListener (){
			public void actionPerformed(ActionEvent arg0) {
				paletteGrid.setModel(new PalettePreviewModel((Palette)palettes_combo.getSelectedItem()));
			}
        });
        
        java.awt.Dimension dim = getPreferredSize();
        dim.setSize(dim.width + 20, dim.height + 20);
        setSize(dim);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        numHarnessesField = new javax.swing.JFormattedTextField(formatter);
        numHarnessesField.setName("numHarnessesField");
        javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        numTreadlesField = new javax.swing.JFormattedTextField(formatter);
        numTreadlesField.setName("numTreadlesField");
        javax.swing.JLabel jLabel3 = new javax.swing.JLabel();
        numWarpEndsField = new javax.swing.JFormattedTextField(formatter);
        numWarpEndsField.setName("numWarpEndsField");
        javax.swing.JLabel jLabel4 = new javax.swing.JLabel();
        numWeftPicksField = new javax.swing.JFormattedTextField(formatter);
        numWeftPicksField.setName("numWeftPicksField");
        javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        
        palettes_combo = new javax.swing.JComboBox(palettes);

        setTitle("Weaving Draft Properties");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Num Harnesses: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(jLabel1, gridBagConstraints);

        numHarnessesField.setColumns(2);
        numHarnessesField.setValue(new Integer(0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(numHarnessesField, gridBagConstraints);

        jLabel2.setText("Num Treadles: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(jLabel2, gridBagConstraints);

        numTreadlesField.setColumns(2);
        numTreadlesField.setValue(new Integer(0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(numTreadlesField, gridBagConstraints);

        jLabel3.setText("Num Warp Ends: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(jLabel3, gridBagConstraints);

        numWarpEndsField.setColumns(3);
        numWarpEndsField.setValue(new Integer(0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(numWarpEndsField, gridBagConstraints);

        jLabel4.setText("Num Weft Picks: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(jLabel4, gridBagConstraints);

        numWeftPicksField.setColumns(3);
        numWeftPicksField.setValue(new Integer(0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(numWeftPicksField, gridBagConstraints);

        javax.swing.JLabel palette_label = new  javax.swing.JLabel("Palette: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(palette_label, gridBagConstraints);
        
        palettes_combo.setPrototypeDisplayValue("about this wide");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(palettes_combo, gridBagConstraints);
        
        paletteGrid = new GridControl();
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(paletteGrid, gridBagConstraints);
        paletteGrid.setSquareWidth(10);
        
        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        jPanel1.add(okButton);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        jPanel1.add(cancelButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        getContentPane().add(jPanel1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        setVisible(false);
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        if (draft != null) {
            int numHarnesses = ((Number)numHarnessesField.getValue()).intValue();
            int numEnds = ((Number)numWarpEndsField.getValue()).intValue();
            int numPicks = ((Number)numWeftPicksField.getValue()).intValue();
            int numTreadles = ((Number)numTreadlesField.getValue()).intValue();
            
            draft.setNumHarnesses(numHarnesses);
            
            if (numEnds > draft.getEnds().size()) {
                while (numEnds > draft.getEnds().size())
                    draft.getEnds().add(new com.jenkins.weavingsimulator.datatypes.WarpEnd(java.awt.Color.WHITE, -1));
            } else if (numEnds < draft.getEnds().size()) {
                while (numEnds < draft.getEnds().size())
                    draft.getEnds().remove(draft.getEnds().size() - 1);
            }
            
            if (numPicks > draft.getPicks().size()) {
                while (numPicks > draft.getPicks().size())
                    draft.getPicks().add(new com.jenkins.weavingsimulator.datatypes.WeftPick());
            } else if (numPicks < draft.getPicks().size()) {
                while (numPicks < draft.getPicks().size())
                    draft.getPicks().remove(draft.getPicks().size() - 1);
            }
            
            if (numTreadles > draft.getTreadles().size()) {
                while (numTreadles > draft.getTreadles().size())
                    draft.getTreadles().add(new com.jenkins.weavingsimulator.datatypes.Treadle());
            } else if (numTreadles < draft.getTreadles().size()) {
                while (numTreadles < draft.getTreadles().size())
                    draft.getTreadles().remove(draft.getTreadles().size() - 1);
            }
            
    		Palette p = (Palette)palettes_combo.getSelectedItem();
    		if (p != null) {
    			draft.setPalette(p);
    		}
        }
        
        editFinished = true;
        setVisible(false);
        dispose();
    }//GEN-LAST:event_okButtonActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog
    
    /** Call this function to edit the properties of a WeavingDraft.
     * The function displays the dialog, and doesn't return until
     * editing has finished.  The draft is only modified if the
     * user presses OK.
     * @param draft The WeavingDraft whose properties will be edited.
     * @return true if user pressed Ok, false if user pressed Cancel
     * or otherwise closed the dialog.
     */    
    public boolean editProperties(com.jenkins.weavingsimulator.datatypes.WeavingDraft draft,
    		List<Palette> palettes) {
        this.draft = draft;
        this.palettes.clear();
        if (draft.getPalette() != null) {
        	this.palettes.add(draft.getPalette());
        }
        this.palettes.addAll(palettes);
        palettes_combo.setSelectedItem(this.palettes.get(0));

        numWarpEndsField.setValue(value_or_default(draft.getEnds().size(), "ends", 20));
        numWeftPicksField.setValue(value_or_default(draft.getPicks().size(), "picks", 20));
        numHarnessesField.setValue(value_or_default(draft.getNumHarnesses(), "harnesses", 4));
        numTreadlesField.setValue(value_or_default(draft.getTreadles().size(), "treadles", 6));
        editFinished = false;
        
        setVisible(true);
        return editFinished;
    }
    
    public void saveDefaults() {
    	if (draft != null) {
    		Preferences prefs = Preferences.userNodeForPackage(this.getClass());
    		prefs.putInt("harnesses", ((Number)numHarnessesField.getValue()).intValue());
    		prefs.putInt("treadles", ((Number)numTreadlesField.getValue()).intValue());
    		prefs.putInt("ends", ((Number)numWarpEndsField.getValue()).intValue());
    		prefs.putInt("picks", ((Number)numWeftPicksField.getValue()).intValue());
    	}
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        final WeavingDraftPropertiesDialog dlg =
            new WeavingDraftPropertiesDialog(new javax.swing.JFrame(), true);
        dlg.okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String msg = "warp ends = " + dlg.numWarpEndsField.getValue() + "\n" +
                    "weft picks = " + dlg.numWeftPicksField.getValue() + "\n" +
                    "treadles = " + dlg.numTreadlesField.getValue() + "\n" +
                    "harnesses = " + dlg.numHarnessesField.getValue();
                javax.swing.JOptionPane.showMessageDialog(null, msg);
            }
        });
        dlg.setVisible(true);
    }
    
    private Integer value_or_default (int value, String default_key, int deflt) {
    	// Returns  anew Integer with value, unless value is 0, then returns
    	// the preferences value for default_key if available, or deflt.
    	if (value != 0) return new Integer(value);
    	return new Integer (Preferences.userNodeForPackage(this.getClass()).getInt(default_key, deflt));
    }
    
    // formatter for JFormattedTextField which only allows integers >= 0.
    private class NonNegativeIntFormatter 
        extends javax.swing.JFormattedTextField.AbstractFormatter {
        java.text.NumberFormat format = java.text.NumberFormat.getIntegerInstance();
        
        /** Parses <code>text</code> returning an arbitrary Object. Some
         * formatters may return null.
         *
         * @throws ParseException if there is an error in the conversion
         * @param text String to convert
         * @return Object representation of text
         *
         */
        public Object stringToValue(String text) throws ParseException {
            Number val = format.parse(text);
        	if (val.intValue() < 0)
                throw new ParseException("Negative numbers not allowed", 0);
            return val;
        }
        
        /** Returns the string value to display for <code>value</code>.
         *
         * @throws ParseException if there is an error in the conversion
         * @param value Value to convert
         * @return String representation of value
         *
         */
        public String valueToString(Object value) throws ParseException {
            if (value == null)
                return "";
            else
                return format.format(value);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JFormattedTextField numHarnessesField;
    private javax.swing.JFormattedTextField numTreadlesField;
    private javax.swing.JFormattedTextField numWarpEndsField;
    private javax.swing.JFormattedTextField numWeftPicksField;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    
    
    
    private com.jenkins.weavingsimulator.datatypes.WeavingDraft draft = null;
    // used to communicate between button handlers, and editProperties() function
    private boolean editFinished;
    // used by JFormattedTextFields.
    private NonNegativeIntFormatter formatter = new NonNegativeIntFormatter();
    private Vector<Palette> palettes = new Vector<Palette>();
    private javax.swing.JComboBox palettes_combo;
    private GridControl paletteGrid;
}
