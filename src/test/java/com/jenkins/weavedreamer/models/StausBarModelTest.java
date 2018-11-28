package com.jenkins.weavedreamer.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.jenkins.weavingsimulator.datatypes.WeavingDraft;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import junit.framework.TestCase;

public class StausBarModelTest extends TestCase {
	
	public void testModelObservesDraftSelection () {
		StatusBarModel sbmodel = new StatusBarModel();
		WeavingDraft draft = new WeavingDraft();
		TreadlingDraftModel tm = new TreadlingDraftModel(new EditingSession(draft));
		
		sbmodel.listen(tm);
		tm.setCurrentCell(3, 2);
		assertThat (sbmodel.getText(), is("3, 4 (0x0)")); // Presented 1-based.

		tm.setCurrentCell(3, 2, 4, 5);
		assertThat (sbmodel.getText(), is("3, 4 (4x2)")); // Presented 1-based.
	}
	
	public void testModelNotifiesChanges () {
		StatusBarModel sbmodel = new StatusBarModel();
		WeavingDraft draft = new WeavingDraft();
		TreadlingDraftModel tm = new TreadlingDraftModel(new EditingSession(draft));
		sbmodel.listen(tm);

		PropertyChangeHandler handler = new PropertyChangeHandler();
		sbmodel.addPropertyChangeListener(handler);
		tm.setCurrentCell(3, 2);
		assertThat (handler.event, is(notNullValue()));
		assertThat ((String)handler.event.getNewValue(), is("3, 4 (0x0)"));
	}

	public void testColumnNumberNotSHownWithOneColumn() {
		StatusBarModel sbmodel = new StatusBarModel();
		WeavingDraft draft = new WeavingDraft();
		StepColorModel tm = new StepColorModel(new EditingSession(draft));
		sbmodel.listen(tm);

		tm.setCurrentCell(3, 0, 5, 0);
		assertThat (sbmodel.getText(), is("4 (3)")); // Presented 1-based.
	}

	public void testRowNumberNotShownWithOneRow() {
		StatusBarModel sbmodel = new StatusBarModel();
		WeavingDraft draft = new WeavingDraft();
		WarpEndColorModel tm = new WarpEndColorModel(new EditingSession(draft));
		sbmodel.listen(tm);

		tm.setCurrentCell(0, 3, 0, 5);
		assertThat (sbmodel.getText(), is("4 (3)")); // Presented 1-based.
	}

	private class PropertyChangeHandler implements PropertyChangeListener {

		public void propertyChange(PropertyChangeEvent evt) {
			event = evt;
		}
		public PropertyChangeEvent event = null;
	}
}
