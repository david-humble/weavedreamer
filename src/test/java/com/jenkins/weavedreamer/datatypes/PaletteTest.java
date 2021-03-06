/*
 * PaletteTest.java
 * JUnit based test
 * 
 * Created on January 13, 2005, 1:03 AM
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


package com.jenkins.weavedreamer.datatypes;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import com.jenkins.weavingsimulator.datatypes.Palette;
import junit.framework.*;

/**
 *
 * @author ajenkins
 */
public class PaletteTest extends TestCase {
    private Palette palette;
    private TestPropertyChangeListener listener;
    
    public PaletteTest(String testName) {
        super(testName);
    }

    protected void setUp() {
        palette = new Palette(10);
        listener = new TestPropertyChangeListener();
        palette.addPropertyChangeListener(listener);
    }

    public static junit.framework.Test suite() {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(PaletteTest.class);
        
        return suite;
    }
    
    public void testGetNumColors() {
        assertEquals(10, palette.getNumColors());
    }
    
    public void testSetNumColors() {
        palette.setColors(Arrays.asList(Color.BLUE, Color.GREEN, Color.RED));
        assertEquals(3, palette.getNumColors());
        
        // first try setting it larger
        palette.setNumColors(4);
        assertEquals(4, palette.getNumColors());
        assertEquals(Color.BLUE, palette.getColor(0));
        assertEquals(Color.GREEN, palette.getColor(1));
        assertEquals(Color.RED, palette.getColor(2));
        BeanTestUtils.assertPropertyChangeFired(listener.events, "numColors",
                3, 4, -1);
        
        // now try setting it smaller
        palette.setNumColors(2);
        assertEquals(2, palette.getNumColors());
        // make sure initial colors are still the same
        assertEquals(Color.BLUE, palette.getColor(0));
        assertEquals(Color.GREEN, palette.getColor(1));
        BeanTestUtils.assertPropertyChangeFired(listener.events, "numColors",
                4, 2, -1);
    }
    
    public void testGetSetColor() {
        palette.setColor(0, Color.BLUE);
        assertEquals(Color.BLUE, palette.getColor(0));
        BeanTestUtils.assertPropertyChangeFired(listener.events, "color",
                Palette.DEFAULT_COLOR, Color.BLUE, 0);
    }
    
    public void testSetColors() {
        List<Color> oldColors = palette.getColors();
        palette.setColors(Arrays.asList(Color.BLUE, Color.GREEN, Color.RED));
        assertEquals(3, palette.getNumColors());
        assertEquals(Color.BLUE, palette.getColor(0));
        assertEquals(Color.GREEN, palette.getColor(1));
        assertEquals(Color.RED, palette.getColor(2));
        BeanTestUtils.assertPropertyChangeFired(listener.events, "colors",
                oldColors, palette.getColors(), -1);
    }
    
    public void testGetSetSelection() {
        // it should initially be -1
        assertEquals(-1, palette.getSelection());
        
        // make sure it doesn't allow setting out of bounds
        boolean exceptionThrown = false;
        try {
            palette.setSelection(palette.getNumColors());
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        
        exceptionThrown = false;
        try {
            palette.setSelection(-2);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        
        palette.setSelection(4);
        assertEquals(4, palette.getSelection());
        BeanTestUtils.assertPropertyChangeFired(listener.events, "selection",
                -1, 4, -1);
        
        // make sure resizing bigger doesn't affect selection
        palette.setNumColors(palette.getNumColors() + 1);
        assertEquals(4, palette.getSelection());
        
        // make sure if resize eliminates selected color, selection is invalidated
        palette.setNumColors(4);
        assertEquals(-1, palette.getSelection());
        BeanTestUtils.assertPropertyChangeFired(listener.events, "selection",
                4, -1, -1);
    }
    
    public void testSerialisation() throws IOException, ClassNotFoundException {
    	// More of a test of my learning the library.
    	ByteArrayOutputStream os = new ByteArrayOutputStream();
    	ObjectOutputStream oos = new ObjectOutputStream(os);
    	oos.writeObject(palette);
    	
    	ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
    	ObjectInputStream ois = new ObjectInputStream(is);
    	Palette p = (Palette)(ois.readObject());
    	
    	assertThat(p.getColors(), equalTo(palette.getColors()));
    }
    
    public void testPersistence () throws IOException, BackingStoreException {
    	Preferences prefs = Preferences.userRoot().node("test/palette/preferences");
    	// One palette can be saved and restored.
    	List<Palette> palettes = new ArrayList<Palette>();
    	palettes.add(new Palette(new ArrayList<Color>(), "One"));
    	Palette.savePalettes(palettes, prefs);
    	
    	// Default palettes can be loaded
    	List<Palette> restored = Palette.loadPalettes(prefs);
    	assertThat(restored, equalTo(palettes));
    	
    	// save replaces existing saved palettes. Palettes are restored in order.
    	List<Palette> defaults = Palette.getDefaultPalettes();
    	Palette.savePalettes(defaults, prefs);    	
    	assertThat(Palette.loadPalettes(prefs), equalTo(defaults));
    }
}
