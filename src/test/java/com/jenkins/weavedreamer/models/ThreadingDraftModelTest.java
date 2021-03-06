/*
 * ThreadingDraftModelTest.java
 * JUnit based test
 * 
 * Created on January 11, 2005, 10:48 AM
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

import java.awt.Color;
import java.util.Arrays;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.jenkins.weavingsimulator.datatypes.WeftPick;
import com.jenkins.weavingsimulator.datatypes.Treadle;
import com.jenkins.weavingsimulator.datatypes.WarpEnd;
import com.jenkins.weavingsimulator.datatypes.WeavingDraft;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
/**
 *
 * @author ajenkins
 */
public class ThreadingDraftModelTest extends TestCase {
    private WeavingDraft draft;
    private ThreadingDraftModel model;
    private TestTableModelListener listener;
    private EditingSession session;
       
    public ThreadingDraftModelTest(String testName) {
        super(testName);
    }

    protected void setUp() {
        draft = new WeavingDraft("TestDraft");
        draft.setNumHarnesses(2);
        draft.getEnds().add(new WarpEnd(Color.BLUE, 0));
        draft.getEnds().add(new WarpEnd(Color.GREEN, 1));
        session = new EditingSession(draft);

        model = new ThreadingDraftModel(session);
       
        listener = new TestTableModelListener();
        model.addTableModelListener(listener);
    }

    private int gridCol(int col){
        return draft.getEnds().size()-col-1;
    }
    
      private int gridRow(int row){
        return draft.getNumHarnesses()-row-1;
    }
    
    
    public static junit.framework.Test suite() {
        TestSuite suite = new TestSuite(ThreadingDraftModelTest.class);
        
        return suite;
    }

    public void testGetColumnCount() {
        assertEquals(draft.getEnds().size(), model.getColumnCount());
    }

    public void testGetRowCount() {
        assertEquals(draft.getNumHarnesses(), model.getRowCount());
    }

    public void testGetValueAt() {
    	model.showSelection (0,1,2,2);
    	draft.getEnds().get(gridCol(0)).setHarnessId(gridRow(0));
    	draft.getEnds().get(gridCol(1)).setHarnessId(gridRow(1));
        
    	//  * *.
        //  * .* 
        
        
    	assertThat (model.getValueAt(0, 0), equalTo(Color.BLACK));
    	assertThat (model.getValueAt(0, 1), equalTo(Color.LIGHT_GRAY));
    	assertThat (model.getValueAt(1, 0), equalTo(Color.WHITE));
    	assertThat (model.getValueAt(1, 1), equalTo(Color.DARK_GRAY));
    }

    public void testSetValueAt() {
        int testrow=1;
        int testcol=0;
        assertEquals(0, draft.getEnds().get(testcol).getHarnessId());
        //model.setValueAt((Object)true, 1, 0);
        model.setValueAt(true, gridRow(testrow), gridCol(testcol));
        assertEquals(1, draft.getEnds().get(testcol).getHarnessId());
    }

    public void testSetValueAtIsUndoable() {
        int testrow=1;
        int testcol=0;
        assertEquals(0, draft.getEnds().get(0).getHarnessId());
        model.setValueAt(true,  gridRow(testrow), gridCol(testcol));
        assertEquals(1, draft.getEnds().get(0).getHarnessId());
        session.undo();
        assertEquals(0, draft.getEnds().get(0).getHarnessId());    
        session.redo();
        assertEquals(1, draft.getEnds().get(0).getHarnessId());        
        session.undo();
        assertEquals(0, draft.getEnds().get(0).getHarnessId());  
    }

    
    public void testIsCellEditable() {
        for (int harness = 0; harness < model.getRowCount(); harness++) {
            for (int end = 0; end < model.getColumnCount(); end++) {
                assertTrue("Cell "+harness+","+end, model.isCellEditable(harness, end));
            }
        }
    }

    public void testGetColumnClass() {
        for (int col = 0; col < model.getColumnCount(); col++)
            assertEquals("class for column "+col, Color.class, model.getColumnClass(col));
    }
    
    /// Test if tableListener is notified when a setValueAt is called
    public void testNotifyListenerOnSet() {
        model.setValueAt(true, gridRow(1), gridCol(0)); // was 1,0
        TableModelTestUtils.assertTableColumnUpdateEvent(listener.event, model, gridCol(0));
    }
    
    /// Test that listener is notifed when number of harnesses changes
    public void testNotifyListenerOnHarnessesChanged() {
        draft.setNumHarnesses(4);
        
        TableModelTestUtils.assertAllTableCellsUpdateEvent(listener.event, model);
    }
    
    /// Test that listener is notifed when number of warp ends changes
    public void testNotifyListenerOnWarpChanged() {
        draft.getEnds().add(new WarpEnd(Color.RED, 1));
        
        TableModelTestUtils.assertAllTableCellsUpdateEvent(listener.event, model);
    }
    
    /// Test that listener is not notified unnecessarily
    public void testNotifyOnlyForHarnessesOrWarp() {
        // change some other properties of draft
        draft.setName("Foo");
        draft.getTreadles().add(new Treadle());
        draft.getPicks().add(new WeftPick(Color.BLUE, 1, 0));
        
        assertNull(listener.event);
    }

    public void testSelectionisPastedFromSession() {
        draft.setNumHarnesses(4);
        draft.setTreadles(Arrays.asList(new Treadle(), new Treadle(), 
    			new Treadle(), new Treadle()));
    	draft.setEnds(Arrays.asList(
                new WarpEnd(Color.BLUE, 2),
                new WarpEnd(Color.BLACK, 3), 
                new WarpEnd(Color.WHITE, 2),
                new WarpEnd(Color.WHITE, 3),
                new WarpEnd(Color.WHITE, 2),
                new WarpEnd(Color.WHITE, 3)
                ));

        Color T;
    	/* Start with display Grid  
    	 *  *.*.*.
    	 *  .*.*.* 
         *  ......
         *  ......
        */
        
        // display 
        assertThat(model.getValueAt(0,0), is(Color.BLACK));
        assertThat(model.getValueAt(1,0), is(Color.WHITE));

        assertThat(model.getValueAt(0,1), is(Color.WHITE));
        assertThat(model.getValueAt(1,1), is(Color.BLACK));
        
        assertThat(model.getValueAt(0,2), is(Color.BLACK));
        assertThat(model.getValueAt(1,2), is(Color.WHITE));
        
        assertThat(model.getValueAt(0,3), is(Color.WHITE));
        assertThat(model.getValueAt(1,3), is(Color.BLACK));
              
        
    	 /* 
        *  copy from r,c 0, 0 .. 2, 2, paste to 0, 1, should get
    	 *  **..*.
    	 *  ..**.*
         *  ......
         *  ......
    	 */	
    	session.setSelectedCells(new PasteGrid(model, new GridSelection(0, 0, 2, 2)));
    	
    	model.pasteSelection(0, 1, CellSelectionTransforms.Null());
       
        assertThat(model.getValueAt(0,0), is(Color.BLACK));
        assertThat(model.getValueAt(1,0), is(Color.WHITE));

        assertThat(model.getValueAt(0,1), is(Color.BLACK));
        assertThat(model.getValueAt(1,1), is(Color.WHITE));
        
        assertThat(model.getValueAt(0,2), is(Color.WHITE));
        assertThat(model.getValueAt(1,2), is(Color.BLACK));
        
        assertThat(model.getValueAt(0,3), is(Color.WHITE));
        assertThat(model.getValueAt(1,3), is(Color.BLACK));
        

    }

    public void testSelectionLeftUpIsPastedFromSession() {
        draft.setNumHarnesses(4);
        draft.setTreadles(Arrays.asList(new Treadle(), new Treadle(),
                new Treadle(), new Treadle()));
        draft.setEnds(Arrays.asList(
                new WarpEnd(Color.BLUE, 2),
                new WarpEnd(Color.BLACK, 3),
                new WarpEnd(Color.WHITE, 2),
                new WarpEnd(Color.WHITE, 3),
                new WarpEnd(Color.WHITE, 2),
                new WarpEnd(Color.WHITE, 3)
                ));

        session.setSelectedCells(new PasteGrid(model, new GridSelection(1, 1, -1, -1)));

        model.pasteSelection(0, 1, CellSelectionTransforms.Null());
        assertThat(model.getValueAt(0, 0), is(Color.BLACK));
        assertThat(model.getValueAt(1, 0), is(Color.WHITE));

        assertThat(model.getValueAt(0, 1), is(Color.BLACK));
        assertThat(model.getValueAt(1, 1), is(Color.WHITE));

        assertThat(model.getValueAt(0, 2), is(Color.WHITE));
        assertThat(model.getValueAt(1, 2), is(Color.BLACK));

        assertThat(model.getValueAt(0, 3), is(Color.WHITE));
        assertThat(model.getValueAt(1, 3), is(Color.BLACK));
    }

    public void testPasteTooBigIsTruncated() {
    	draft.setTreadles(Arrays.asList(new Treadle(), new Treadle(), 
    			new Treadle(), new Treadle()));
    	draft.setEnds(Arrays.asList(
                new WarpEnd(Color.BLACK, 0), 
                new WarpEnd(Color.WHITE, 1),
                new WarpEnd(Color.WHITE, 0),
                new WarpEnd(Color.WHITE, 1),
                new WarpEnd(Color.WHITE, 0),
                new WarpEnd(Color.BLUE, 1)));

    	session.setSelectedCells(new PasteGrid(model, new GridSelection(0, 0, 4, 5)));
    	
    	model.pasteSelection(1, 3, CellSelectionTransforms.Null());    	
    }


    public void testSelectionPasteIsUnoable() {
    	draft.setTreadles(Arrays.asList(new Treadle(), new Treadle(), 
    			new Treadle(), new Treadle()));
    	draft.setEnds(Arrays.asList(
                new WarpEnd(Color.BLACK, 0), 
                new WarpEnd(Color.WHITE, 1),
                new WarpEnd(Color.WHITE, 1),
                new WarpEnd(Color.WHITE, 1),
                new WarpEnd(Color.WHITE, 0),
                new WarpEnd(Color.BLUE, 1)));

        
        
    	/* Start with display 
    	 *  *.***.
    	 *  .*...*
    	 *  Copy/paste one row to get
    	 *  *.....
    	 *  .*****
    	 *  Undo should restore row 0 even though it is not in the pasted buffer.
    	 */	
    	session.setSelectedCells(new PasteGrid(model, new GridSelection(0, 2, 1, 5)));
    	model.pasteSelection(1, 2, CellSelectionTransforms.Null());
    	assertThat(model.getValueAt(0, 0), is(Color.BLACK));
    	assertThat(model.getValueAt(1, 0), is(Color.WHITE));
    	assertThat(model.getValueAt(0, 1), is(Color.WHITE));
    	assertThat(model.getValueAt(1, 1), is(Color.BLACK));
    	assertThat(model.getValueAt(0, 2), is(Color.WHITE));
    	assertThat(model.getValueAt(1, 2), is(Color.BLACK));
    	assertThat(model.getValueAt(0, 3), is(Color.WHITE));
    	assertThat(model.getValueAt(1, 3), is(Color.BLACK));
    	
       
        
    	session.undo();
    	assertThat(session.canUndo(), is(false));
    	assertThat(model.getValueAt(0, 0), is(Color.BLACK));
    	assertThat(model.getValueAt(1, 0), is(Color.WHITE));
    	assertThat(model.getValueAt(0, 1), is(Color.WHITE));
    	assertThat(model.getValueAt(1, 1), is(Color.BLACK));
    	assertThat(model.getValueAt(0, 2), is(Color.BLACK));
    	assertThat(model.getValueAt(1, 2), is(Color.WHITE));
    	assertThat(model.getValueAt(0, 3), is(Color.BLACK));
    	assertThat(model.getValueAt(1, 3), is(Color.WHITE));
    	
    	session.redo();
    	assertThat(model.getValueAt(0, 0), is(Color.BLACK));
    	assertThat(model.getValueAt(1, 0), is(Color.WHITE));
    	assertThat(model.getValueAt(0, 1), is(Color.WHITE));
    	assertThat(model.getValueAt(1, 1), is(Color.BLACK));
    	assertThat(model.getValueAt(0, 2), is(Color.WHITE));
    	assertThat(model.getValueAt(1, 2), is(Color.BLACK));
    	assertThat(model.getValueAt(0, 3), is(Color.WHITE));
    	assertThat(model.getValueAt(1, 3), is(Color.BLACK));
    } 
}
