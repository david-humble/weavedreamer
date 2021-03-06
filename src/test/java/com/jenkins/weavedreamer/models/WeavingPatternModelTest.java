/*
 * WeavingPatternModelTest.java
 * JUnit based test
 * 
 * Created on January 14, 2005, 2:20 PM
 *  
 * Copyright 2005 Adam P. Jenkins
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


package com.jenkins.weavedreamer.models;

import com.jenkins.weavingsimulator.datatypes.WeftPick;
import com.jenkins.weavingsimulator.datatypes.Treadle;
import com.jenkins.weavingsimulator.datatypes.WarpEnd;
import com.jenkins.weavingsimulator.datatypes.WeavingDraft;
import java.awt.Color;
import java.util.Arrays;
import javax.swing.table.TableModel;
import junit.framework.TestCase;


/**
 *
 * @author ajenkins
 */
public class WeavingPatternModelTest extends TestCase {
    private WeavingDraft draft;
    private TableModel model;
    private TestTableModelListener listener;
    
    public WeavingPatternModelTest(String testName) {
        super(testName);
    }

    protected void setUp() {
        /* Make sure sample draft tests boundary conditions: elements where
         * warp end is not attached to a harness, a WeftPick which doesn't
         * specify any treadle, and both together. */
        draft = new WeavingDraft("TestDraft");
        draft.setNumHarnesses(3);
        draft.setEnds(Arrays.asList(
                new WarpEnd(Color.RED, 0),
                new WarpEnd(Color.GREEN, 1),
                new WarpEnd(Color.BLUE, 2),
                new WarpEnd(Color.YELLOW, 1),
                new WarpEnd(Color.CYAN, 0),
                new WarpEnd(Color.GRAY, -1)));
        draft.setTreadles(Arrays.asList(
                new Treadle(Arrays.asList(0, 2)),
                new Treadle(Arrays.asList(1))));
        draft.setPicks(Arrays.asList(
                new WeftPick(Color.BLACK, 2, 0),
                new WeftPick(Color.WHITE, 2, 1),
                new WeftPick(Color.DARK_GRAY, 2)));
        
        model = new WeavingPatternModel(new EditingSession(draft));
        
        listener = new TestTableModelListener();
        model.addTableModelListener(listener);
    }

    public static junit.framework.Test suite() {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(WeavingPatternModelTest.class);
        
        return suite;
    }

        private int gridCol(int col){
        return draft.getEnds().size()-col-1;
    }
    
          private int gridRow(int row){
        return draft.getNumHarnesses()-row-1;
    }
        
    
    public void testGetColumnCount() {
        assertEquals(draft.getEnds().size(), model.getColumnCount());
    }

    public void testGetRowCount() {
        assertEquals(draft.getPicks().size(), model.getRowCount());
    }

    public void testGetValueAt() {
       for (int r = 0; r < 3; r++) {
           for (int c = 0; c < 6; c++) {
               assertEquals("row="+r+",col="+c, draft.getVisibleColor(c, r), ((WeavingPatternCellModel)model.getValueAt(r, gridCol(c))).color());
           }
       }
    }

    public void testGetColumnClass() {
        for (int c = 0; c < model.getColumnCount(); c++)
            assertEquals("col="+c, WeavingPatternCellModel.class, model.getColumnClass(c));
    }
    
    public void testNotifyOnNumHarnessesChanged() {
        draft.setNumHarnesses(2);
        TableModelTestUtils.assertAllTableCellsUpdateEvent(listener.event, model);
    }
    
    public void testNotifyOnStepsChanged() {
        // try adding a pick
        draft.getPicks().add(new WeftPick(Color.RED, 2, 0));
        TableModelTestUtils.assertTableRowUpdateEvent(listener.event, model, 3);
        
        // try changing a pick
        listener.event = null;
        draft.getPicks().get(0).setColor(Color.GRAY);
        TableModelTestUtils.assertTableRowUpdateEvent(listener.event, model, 0);
        
        // change all picks
        listener.event = null;
        draft.setPicks(Arrays.asList(new WeftPick(Color.BLUE, 2, 0)));
        TableModelTestUtils.assertAllTableCellsUpdateEvent(listener.event, model);
    }
    
    public void testNotifyOnEndsChanged() {
        // try adding an end
        draft.getEnds().add(new WarpEnd(Color.RED, 1));
        TableModelTestUtils.assertTableColumnUpdateEvent(listener.event, model, 6);
        // and the other side sees it too
        draft.getEnds().add(new WarpEnd(Color.RED, 1));
        TableModelTestUtils.assertTableColumnUpdateEvent(listener.event, model, 0);
        
        // try changing an end
        listener.event = null;
        draft.getEnds().get(1).setHarnessId(2);      
        TableModelTestUtils.assertTableColumnsUpdateEvent(listener.event, model, gridCol(1));
        
        // try replacing ends
        listener.event = null;
        draft.setEnds(Arrays.asList(new WarpEnd(Color.BLUE, 1)));
        TableModelTestUtils.assertAllTableCellsUpdateEvent(listener.event, model);
    }
    
    public void testNotifyOnTreadlesChanged() {
        draft.getTreadles().get(1).add(2);
        TableModelTestUtils.assertAllTableCellsUpdateEvent(listener.event, model);
    }
    
}
