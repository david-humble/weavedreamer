package com.jenkins.uat;

import org.junit.Test;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

public class UndoTest extends WeavingTestCase {
	@Test
	public void testUndo() throws IOException {
		ui.open(new File("testdata/103.wif"));
		
		// Too many undo should be harmless.
		ui.undo();
		
		ui.setPick (0, 1);
		ui.draftIs(0, 0, blue);
		ui.undo();
		ui.draftIs(0, 0, orange);
		ui.redo();
		ui.draftIs(0, 0, blue);
		ui.undo();
		
		ui.setThreading(1, 0);
		ui.draftIs(0, 0, blue);
		ui.undo();
		ui.draftIs(0, 0, orange);

		ui.toggleTieup (0, 1);
		ui.draftIs(1, 0, orange);
		ui.undo();
		ui.draftIs(1, 0, blue);	
		
		ui.selectColour(0);
		ui.setThreadingColour(0);
		ui.draftIs(0, 0, blue);
		ui.undo();
		ui.draftIs(0, 0, orange);	
		
		ui.selectColour(1);
		ui.setPickColour(0);
		ui.draftIs(0, 1, orange);
		ui.undo();
		ui.draftIs(0, 1, blue);	
	}

	@Test
	public void testUndoToStart() {
		ui.newDraft(4, 6, 24, 24, "Monochrome");
		
		ui.setPick (0, 1);
		ui.pickIs(0, 1, Color.black);
		ui.undo();
		ui.pickIs(0,1,Color.white);

		ui.setThreading(1, 0);
		
	
		ui.harnessIs(1, 0, Color.black);
		ui.undo();
		ui.harnessIs(1, 0, Color.white);
	}

	@Test
	public void testUndoPasteToStart() {
		ui.newDraft(4, 6, 24, 24, "Monochrome");	
		
		ui.setPick (0, 0);
		ui.selectPick(0, 0, 3, 3);

		ui.pasteTreadling(4, 0);
            
                
		ui.pickIs(4, 0, Color.black);
		ui.undo();	// this appears not to work	
		 ui.pickIs(4, 0, Color.white);
		
		ui.setThreading(3, 0);
		ui.selectThreading(0, 0, 3, 3);
		ui.pasteThreading(0, 4);
		ui.harnessIs(3, 0, Color.black);
		ui.undo(); // this appears not to work
		ui.endIs(3, 4, Color.white);	
		//ui.endIs(3, 4, Color.blue);	
		
		}
	
	

	@Test
	public void  testUndoPasteLeft() {
		// checks for correct undo when pase block of left edge 
	

			ui.newDraft(4, 6, 20, 20, "Monochrome");
			ui.setThreadingCell(0, 0);
			ui.setThreadingCell(0, 10);
			ui.setThreadingCell (1, 11);
			ui.setThreadingCell (2, 12);
			ui.setThreadingCell (3, 13);
			ui.selectThreading(0, 10, 4, 14);
			
			ui.pasteThreading (0, 9, 1, 1, false, false, false, true);
		
			ui.endIs(0, 6, Color.BLACK);
			ui.endIs(1, 7, Color.BLACK);
			ui.endIs(2, 8, Color.BLACK);
			ui.endIs(3, 9, Color.BLACK);
			ui.undo();
			ui.endIs(0, 6, Color.WHITE);
			ui.endIs(1, 7, Color.WHITE);
			ui.endIs(2, 8, Color.WHITE);
			ui.endIs(3, 9, Color.WHITE);
			
			ui.selectThreading(0, 10, 4, 14);			
			// paste off the edge 
			ui.pasteThreading (0, 1, 1, 1, false, false, false, true);
			ui.endIs(0, 0, Color.WHITE);
			ui.endIs(2, 0, Color.BLACK);
			ui.endIs(3, 1, Color.BLACK);	
			ui.undo();
			ui.endIs(0, 0, Color.BLACK);
			ui.endIs(2, 0, Color.WHITE);
			ui.endIs(3, 1, Color.WHITE);				
	
		
		
		}
}
