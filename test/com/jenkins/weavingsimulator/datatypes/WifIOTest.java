package com.jenkins.weavingsimulator.datatypes;

import junit.framework.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import com.jenkins.wifio.WIFException;


import java.awt.Color;
import java.io.*;

public class WifIOTest extends TestCase {
	// Most of these test cases are reductions of problems encountered in 
	// testing the app against the most popular 50 drafts from www.Handweaving.net.
	// As such, practical concerns override standards adherence.
	
	String minimal = "[WIF]\n" +
	    "Version=1.1\n"+                 
	    "Date=April 20, 1997\n"+         
	    "Developers=wif@mhsoft.com\n"+   
	    "Source Program=WeavingSImulator test\n"+
	    "[CONTENTS]\n";

    // A minimal WIF file as specified should not raise an exception.
    public void testMinimalWif () throws IOException {
    	WIFIO io = new WIFIO();
    	WeavingDraft draft = io.readWeavingDraft(new StringReader(minimal));
    };
    
    // A minimal usable WIF file can be read. Specifically,
    // the standard does not require WARP COLORS, WEFT COLORS or COLOR PALETE
    // entries.
    public void testMinimalTwillWif() throws IOException {
    	String twill = minimal + 
    			"Weaving=yes\n" +
    			"Tieup=yes\n"+
    			"Threading=yes\n"+
    			"Treadling=yes\n"+
    			"Warp=yes\n"+
    			"Weft=yes\n"+
    			"[WEAVING]\n"+
    			"Shafts=2\n"+
    			"Treadles=2\n"+
    			"[WARP]\n"+
    			"Threads=2\n"+
    			"[WEFT]\n"+
    			"Threads=2\n"+
    			"[THREADING]\n"+
    			"1=1\n"+
    			"2=2\n"+
    			"[TREADLING]\n"+
    			"1=1\n"+
    			"2=2\n"+
    			"[TIEUP]\n"+
    			"1=2\n"+
    			"2=1\n";
    	WIFIO io = new WIFIO();
    	WeavingDraft draft = io.readWeavingDraft(new StringReader(twill));
    	assertEquals(draft.getPicks().size(), 2);
    	assertEquals(draft.getPicks().get(0).getColor(), Color.white);
    	assertEquals(draft.getEnds().get(0).getColor(), Color.black);
    }
    
    public void testColorCanBeReadFromWarpAndWeft () throws IOException {
    	String twill = minimal + 
    			"Color palette=yes\n"+
    			"Color Table=yes\n"+
    			"Weaving=yes\n" +
    			"Tieup=yes\n"+
    			"Threading=yes\n"+
    			"Treadling=yes\n"+
    			"Warp=yes\n"+
    			"Weft=yes\n"+
    			"[COLOR PALETTE]\n"+
    			"Range=0,255\n"+
    			"Entries=2\n"+
    			"[COLOR TABLE]\n"+
    			"1=255,0,0\n"+
    			"2=0,255,0\n"+
    			"[WEAVING]\n"+
    			"Shafts=2\n"+
    			"Treadles=2\n"+
    			"[WARP]\n"+
    			"Threads=2\n"+
    			"Color=1\n"+
    			"[WEFT]\n"+
    			"Threads=2\n"+
    			"Color=2\n"+
    			"[THREADING]\n"+
    			"1=1\n"+
    			"2=2\n"+
    			"[TREADLING]\n"+
    			"1=1\n"+
    			"2=2\n"+
    			"[TIEUP]\n"+
    			"1=2\n"+
    			"2=1\n";
    	WIFIO io = new WIFIO();
    	WeavingDraft draft = io.readWeavingDraft(new StringReader(twill));
    	assertEquals(2, draft.getPicks().size());
    	assertEquals(Color.green, draft.getPicks().get(0).getColor());
    	assertEquals(Color.red, draft.getEnds().get(0).getColor());
    }
    
    public void testColorRangeIsHonoured () throws IOException {
    	String twill = minimal + 
    			"Color palette=yes\n"+
    			"Color Table=yes\n"+
    			"Weaving=yes\n" +
    			"Tieup=yes\n"+
    			"Threading=yes\n"+
    			"Treadling=yes\n"+
    			"Warp=yes\n"+
    			"Weft=yes\n"+
    			"[COLOR PALETTE]\n"+
    			"Range=0,1000\n"+
    			"Entries=2\n"+
    			"[COLOR TABLE]\n"+
    			"1=1000,0,0\n"+
    			"2=0,1000,0\n"+
    			"[WEAVING]\n"+
    			"Shafts=2\n"+
    			"Treadles=2\n"+
    			"[WARP]\n"+
    			"Threads=2\n"+
    			"Color=1\n"+
    			"[WEFT]\n"+
    			"Threads=2\n"+
    			"Color=2\n"+
    			"[THREADING]\n"+
    			"1=1\n"+
    			"2=2\n"+
    			"[TREADLING]\n"+
    			"1=1\n"+
    			"2=2\n"+
    			"[TIEUP]\n"+
    			"1=2\n"+
    			"2=1\n";
    	WIFIO io = new WIFIO();
    	WeavingDraft draft = io.readWeavingDraft(new StringReader(twill));
    	assertEquals(2, draft.getPicks().size());
    	assertEquals(Color.green, draft.getPicks().get(0).getColor());
    	assertEquals(Color.red, draft.getEnds().get(0).getColor());
    }
    
    public void testActualNumberOfThreadsTakesPriority () throws IOException {
    	// A WIF might claim a certain number of threads and actually contains fewer.
    	// Personally I'd throw that out as non-compliant but handweaving.net 
    	// can cope so I suppose we should.
    	String twill = minimal + 
    			"Color palette=yes\n"+
    			"Color Table=yes\n"+
    			"Weaving=yes\n" +
    			"Tieup=yes\n"+
    			"Threading=yes\n"+
    			"Treadling=yes\n"+
    			"Warp=yes\n"+
    			"Weft=yes\n"+
    			"[COLOR PALETTE]\n"+
    			"Range=0,1000\n"+
    			"Entries=2\n"+
    			"[COLOR TABLE]\n"+
    			"1=1000,0,0\n"+
    			"2=0,1000,0\n"+
    			"[WEAVING]\n"+
    			"Shafts=2\n"+
    			"Treadles=2\n"+
    			"[WARP]\n"+
    			"Threads=3\n"+  // <- Here's the test
    			"Color=1\n"+
    			"[WEFT]\n"+
    			"Threads=3\n"+
    			"Color=2\n"+
    			"[THREADING]\n"+
    			"1=1\n"+
    			"2=2\n"+
    			"[TREADLING]\n"+
    			"1=1\n"+
    			"2=2\n"+
    			"[TIEUP]\n"+
    			"1=2\n"+
    			"2=1\n";
    	WIFIO io = new WIFIO();
    	WeavingDraft draft = io.readWeavingDraft(new StringReader(twill));
    	assertEquals(2, draft.getPicks().size());
    	assertEquals(2, draft.getEnds().size());
    }
    
    public void testDobbyLiftplanCanBeRead () throws IOException {
    	// Triggered handweaving.net pattern 56737 - a 296 row dobby pattern.
    	// hw.net interprets it as a really complex tie up and a simple treadle.
    	// Seems sensible enough, the "tie up" is the dobby set up and the
    	// Pick is the dobby bar to use. Although it may prove hard to show.
    	String dobby = minimal + 
    			"Color palette=yes\n"+
    			"Color Table=yes\n"+
    			"Weaving=yes\n"+
    			"Warp=yes\n"+
    			"Weft=yes\n"+
    			"Weft Colors=yes\n"+
    			"Threading=yes\n"+
    			"Liftplan=yes\n"+
    			"[COLOR PALETTE]\n"+
    			"Range=0,255\n"+
    			"Entries=200\n"+ // <- Here's the test
    			"[COLOR TABLE]\n"+
    			"1=255,0,0\n"+
    			"2=0,255,0\n"+
    			"[WEAVING]\n"+
    			"Shafts=4\n"+
    			"Treadles=4\n"+
    			"[WARP]\n"+
    			"Threads=4\n"+ 
    			"[WEFT]\n"+
    			"Threads=6\n"+
    			"[THREADING]\n"+
    			"1=1\n"+
    			"2=2\n"+
    			"3=3\n"+
    			"4=4\n"+
    			"[WEFT COLORS]\n"+
    			"1=1\n"+
    			"2=2\n"+
    			"3=1\n"+	
    			"4=2\n"+
    			"5=2\n"+	
    			"6=1\n"+
    			"[LIFTPLAN]\n"+
    			"1=1,3\n"+
    			"2=2,4\n"+
    			"3=1,3,4\n"+
    			"4=1,2,3\n"+
    			"5=1,3\n"+
    			"6=2,4\n";
    	WIFIO io = new WIFIO();
    	WeavingDraft draft = io.readWeavingDraft(new StringReader(dobby));
    	
    	java.util.List<Treadle> t = draft.getTreadles();
    	assertThat(t, hasSize(6));
    	assertThat(t.get(0), contains(0,2));
    	assertThat(t.get(1), contains(1,3));
    	assertThat(t.get(2), contains(0,2,3));
    	assertThat(t.get(3), contains(0,1,2));
    	assertThat(t.get(4), contains(0,2));
    	assertThat(t.get(5), contains(1,3));
    	
    	java.util.List<WeftPick> p = draft.getPicks();
    	assertThat(p, hasSize(6));
    	assertThat(p.get(0).getTreadleId(), is(0));
    	assertThat(p.get(1).getTreadleId(), is(1));
    	assertThat(p.get(2).getTreadleId(), is(2));
    	assertThat(p.get(3).getTreadleId(), is(3));
    	assertThat(p.get(4).getTreadleId(), is(4));
    	assertThat(p.get(5).getTreadleId(), is(5));
    }
    
    public void testActualNumberOfColoursTakesPriority () throws IOException {
    	// A WIF might claim a certain number of colours and actually contain fewer.
    	// Personally I'd throw that out as non-compliant but handweaving.net 
    	// can cope so I suppose we should.
    	// The more I look at this format the less I like it.
    	String twill = minimal + 
    			"Color palette=yes\n"+
    			"Color Table=yes\n"+
    			"Warp=yes\n"+
    			"Weaving=yes\n"+
    			"Warp Colors=yes\n"+
    			"Threading=yes\n"+
    			"[COLOR PALETTE]\n"+
    			"Range=0,255\n"+
    			"Entries=200\n"+ // <- Here's the test
    			"[COLOR TABLE]\n"+
    			"1=255,0,0\n"+
    			"2=0,255,0\n"+
    			"[WEAVING]\n"+
    			"Shafts=2\n"+
    			"Treadles=2\n"+
    			"[WARP]\n"+
    			"Threads=2\n"+ 
    			"[THREADING]\n"+
    			"1=1\n"+
    			"2=2\n"+
    			"[WARP COLORS]\n"+
    			"1=1\n"+
    			"2=2\n";
    	WIFIO io = new WIFIO();
    	WeavingDraft draft = io.readWeavingDraft(new StringReader(twill));
    	assertEquals(2, draft.getEnds().size());
    	assertEquals(Color.red, draft.getEnds().get(0).getColor());
    }
    
    public void testColorPaletteButNoTableIsError () throws IOException {
    	String twill = minimal + 
    			"Color palette=yes\n"+
    			"Weaving=yes\n" +
    			"Tieup=yes\n"+
    			"Threading=yes\n"+
    			"Treadling=yes\n"+
    			"Warp=yes\n"+
    			"Weft=yes\n"+
    			"[COLOR PALETTE]\n"+
    			"Range=255\n"+
    			"Entries=2\n"+
    			"[COLOR TABLE]\n"+
    			"1=255,0,0\n"+
    			"2=0,255,0\n"+
    			"[WEAVING]\n"+
    			"Shafts=2\n"+
    			"Treadles=2\n"+
    			"[WARP]\n"+
    			"Threads=2\n"+
    			"Color=1\n"+
    			"[WEFT]\n"+
    			"Threads=2\n"+
    			"Color=2\n"+
    			"[THREADING]\n"+
    			"1=1\n"+
    			"2=2\n"+
    			"[TREADLING]\n"+
    			"1=1\n"+
    			"2=2\n"+
    			"[TIEUP]\n"+
    			"1=2\n"+
    			"2=1\n";
    	WIFIO io = new WIFIO();
    	try
    	{
    		io.readWeavingDraft(new StringReader(twill));
    		assertTrue(false);
    	} catch (WIFException e) {
    	
    	}

    }
};