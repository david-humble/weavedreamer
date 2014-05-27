/*
 * WeavingDraftWindow.java
 * 
 * Created on April 5, 2003, 4:47 AM
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

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import com.jenkins.weavingsimulator.datatypes.WeavingDraft;
import com.jenkins.weavingsimulator.models.AbstractWeavingDraftModel;
import com.jenkins.weavingsimulator.models.EditingSession;
import com.jenkins.weavingsimulator.models.StatusBarModel;
import com.jenkins.weavingsimulator.models.StepColorModel;
import com.jenkins.weavingsimulator.models.ThreadingDraftModel;
import com.jenkins.weavingsimulator.models.TieUpModel;
import com.jenkins.weavingsimulator.models.TreadlingDraftModel;
import com.jenkins.weavingsimulator.models.WarpEndColorModel;
import com.jenkins.weavingsimulator.models.WeavingPatternModel;

/**
 * 
 * @author ajenkins
 * 
 *         TODO Add a new pallete grid control, which allows a palette to be
 *         created. This will be connected to the endColorGrid and pickColorGrid
 *         via their models. It should work by having the cell editor for the
 *         end and pick colorGrids have a reference.
 */
public class WeavingDraftWindow extends javax.swing.JInternalFrame {

	/** Creates new form WeavingDraftWindow */
	public WeavingDraftWindow(EditingSession session) {
		initComponents();

		jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
		jScrollPane1.getHorizontalScrollBar().setUnitIncrement(16);

		int squareWidth = 15;
		WeavingDraft draft = new WeavingDraft("");
		weavingPatternGrid.setModel(new WeavingPatternModel(draft));
		weavingPatternGrid.setSquareWidth(squareWidth);
		weavingPatternGrid.setIntercellSpacing(new Dimension(0,0));
		weavingPatternGrid.setAllowDrag(false);
		weavingPatternGrid.setShowGrid(false);
		weavingPatternGrid.setName("weavingPatternGrid");

		threadingDraftGrid.setModel(new ThreadingDraftModel(draft));
		threadingDraftGrid.setSquareWidth(squareWidth);
		threadingDraftGrid.setName("threadingDraftGrid");

		warpEndColorGrid.setModel(new WarpEndColorModel(draft));
		warpEndColorGrid.setSquareWidth(squareWidth);
		warpEndColorGrid.setEditValueProvider(new ColorEditProvider());
		warpEndColorGrid.setName("warpEndColorGrid");
		
		tieUpGrid.setModel(new TieUpModel(draft));
		tieUpGrid.setSquareWidth(squareWidth);
		tieUpGrid.setName("tieUpGrid");
		
		treadlingDraftGrid.setModel(new TreadlingDraftModel(draft));
		treadlingDraftGrid.setSquareWidth(squareWidth);
		treadlingDraftGrid.setName("treadlingDraftGrid");
		
		pickColorGrid.setModel(new StepColorModel(draft));
		pickColorGrid.setSquareWidth(squareWidth);
		pickColorGrid.setEditValueProvider(new ColorEditProvider());
		pickColorGrid.setName("pickColorGrid");
		
		treadlingDraftGrid.setToolTipText("Set treadle");
		threadingDraftGrid.setToolTipText("Set shaft");
		warpEndColorGrid.setToolTipText("Set warp colour");
		pickColorGrid.setToolTipText("Set weft colour");
		tieUpGrid.setToolTipText("Set tie-up");
		
		StatusBarModel sbModel = new StatusBarModel();
		sbModel.listen((AbstractWeavingDraftModel)treadlingDraftGrid.getModel());
		sbModel.listen((AbstractWeavingDraftModel)threadingDraftGrid.getModel());
		
		statusBar = new StatusBarControl(sbModel);
		statusBar.setName("statusBar");
		java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		jPanel1.add(statusBar, gridBagConstraints);
		statusBar.setText("0,0");
		
		setName("WeavingDraftWindow");
		
		this.session = session;
		setUpModels();
	}

	private void setUpModels () {
		WeavingDraft draft = session.getDraft();
		((ThreadingDraftModel) threadingDraftGrid.getModel()).setDraft(draft);
		((WarpEndColorModel) warpEndColorGrid.getModel()).setDraft(draft);
		((WeavingPatternModel) weavingPatternGrid.getModel()).setDraft(draft);
		((TieUpModel) tieUpGrid.getModel()).setDraft(draft);
		((TreadlingDraftModel) treadlingDraftGrid.getModel()).setDraft(draft);
		((StepColorModel) pickColorGrid.getModel()).setDraft(draft);
		fileChangedHandler(session.getFile());
		draftModifiedChangedHandler(session.isDraftModified());
		palettePanel.setSession(session);
	}
	
	private class ColorEditProvider implements GridControl.EditedValueProvider {
		public Object getValue() {
			int selection = session.getPalette().getSelection();
			if (selection != -1) {
				return session.getPalette().getColor(selection);
			} else {
				return null;
			}
		}
	};

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		jScrollPane1 = new javax.swing.JScrollPane();
		jPanel1 = new javax.swing.JPanel();
		warpEndColorGrid = new com.jenkins.weavingsimulator.GridControl();
		threadingDraftGrid = new com.jenkins.weavingsimulator.GridControl();
		tieUpGrid = new com.jenkins.weavingsimulator.GridControl();
		weavingPatternGrid = new com.jenkins.weavingsimulator.GridControl();
		treadlingDraftGrid = new com.jenkins.weavingsimulator.GridControl();
		pickColorGrid = new com.jenkins.weavingsimulator.GridControl();
		palettePanel = new com.jenkins.weavingsimulator.PalettePanel();

		setClosable(true);
		setMaximizable(true);
		setResizable(true);

		jPanel1.setLayout(new java.awt.GridBagLayout());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
		jPanel1.add(warpEndColorGrid, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		jPanel1.add(threadingDraftGrid, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		jPanel1.add(tieUpGrid, gridBagConstraints);

		weavingPatternGrid.setRowSelectionAllowed(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		jPanel1.add(weavingPatternGrid, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		jPanel1.add(treadlingDraftGrid, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		jPanel1.add(pickColorGrid, gridBagConstraints);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridheight = 2;
		jPanel1.add(palettePanel, gridBagConstraints);

		jScrollPane1.setViewportView(jPanel1);

		getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void fileChangedHandler(File file) {
		if (file != null)
			setTitle(file.getName());
	}

	public void displayTiledView() {
		if (tiledViewFrame == null) {
			wpanel = new WeavingPatternPanel();
			wpanel.setName("draftPanel");
			wpanel.setDraft(session.getDraft());
			tiledViewFrame = new javax.swing.JFrame(getTitle());
			tiledViewFrame.getContentPane().add(wpanel);
			tiledViewFrame.pack();
			tiledViewFrame.setSize(400, 400);
			addPropertyChangeListener(
					javax.swing.JInternalFrame.TITLE_PROPERTY,
					new java.beans.PropertyChangeListener() {
						public void propertyChange(
								java.beans.PropertyChangeEvent e) {
							tiledViewFrame.setTitle((String) e.getNewValue());
						}
					});
		}
		tiledViewFrame.setVisible(true);
	}

	public void hideTiledView() {
		if (tiledViewFrame != null)
			tiledViewFrame.setVisible(false);
	}

	private void draftModifiedChangedHandler(boolean draftModified) {
		if (draftModified) {
			setTitle(getTitle() + " *");
		} else {
			String oldTitle = getTitle();
			if (oldTitle.endsWith(" *"))
				setTitle(oldTitle.substring(0, oldTitle.length() - 2));
		}
	}

	/**
	 * Getter for property session.
	 * 
	 * @return Value of property session.
	 */
	public EditingSession getSession() {
		return this.session;
	}

	public void zoomIn() {
		int square = warpEndColorGrid.getSquareWidth() * 2;
		warpEndColorGrid.setSquareWidth(square);
		threadingDraftGrid.setSquareWidth(square);
		tieUpGrid.setSquareWidth(square);
		weavingPatternGrid.setSquareWidth(square);
		treadlingDraftGrid.setSquareWidth(square);
		pickColorGrid.setSquareWidth(square);
	}

	public void zoomOut() {
		int square = warpEndColorGrid.getSquareWidth() / 2;
		warpEndColorGrid.setSquareWidth(square);
		threadingDraftGrid.setSquareWidth(square);
		tieUpGrid.setSquareWidth(square);
		weavingPatternGrid.setSquareWidth(square);
		treadlingDraftGrid.setSquareWidth(square);
		pickColorGrid.setSquareWidth(square);
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel jPanel1;
	private javax.swing.JScrollPane jScrollPane1;
	private com.jenkins.weavingsimulator.PalettePanel palettePanel;
	private com.jenkins.weavingsimulator.GridControl pickColorGrid;
	private com.jenkins.weavingsimulator.GridControl threadingDraftGrid;
	private com.jenkins.weavingsimulator.GridControl tieUpGrid;
	private com.jenkins.weavingsimulator.GridControl treadlingDraftGrid;
	private com.jenkins.weavingsimulator.GridControl warpEndColorGrid;
	private com.jenkins.weavingsimulator.GridControl weavingPatternGrid;
	// End of variables declaration//GEN-END:variables
	private StatusBarControl statusBar;

	private javax.swing.JFrame tiledViewFrame = null;
	private WeavingPatternPanel wpanel = null;
	private EditingSession session;
}
