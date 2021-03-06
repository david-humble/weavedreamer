Travis CI: [![][Travis CI Img]][Travis CI]

WeaveDreamer Release Notes
========================

WeaveDreamer is a fabric design software, to aid hand weavers in creating textile weaving 
patterns. It supports any number of warp and weft threads, treadles, and harnesses. 
For more details see the [user guide](https://sourceforge.net/p/weavingsim/wiki/User%20Guide/).

This software is a fork of the [FreeWeave Weaving Simulator](https://sourceforge.net/projects/freeweave/) application. 


[Travis CI Img]:https://travis-ci.com/pdhammond42/weavedreamer.svg?branch=master
[Travis CI]:https://travis-ci.com/pdhammond42/weavedreamer

Installing on Windows
---------------------

The application does not need to be specially installed, just unpack the files 
from the zip archive and double-click the WeaveDreamer file. If this does 
not work you probably don't have a suitable Java installed, version 8 or higher is required.
You can get [Java here](http://www.java.com/en/). Registration may be necessary.

Installing on Linux
-------------------
Unpack the zip archive. Either

- from a command line, type 

    `java -jar WeaveDreamer.jar`

- or right click the .jar file and select the option along the lines of 
"Open with Java runtime". Requires Java runtime 8 or above.

Installing on macOS
----
Download the zip file. Open the download, this should expand the archive and open Finder.
Right click (or two finger tap) the WeaveDreamer file, select Open, and Open on the confirm page.

If it fails to run, you may not have a suitable Java installed, version 8 is required. You can get [Java here](https://java.com/en/). 
Registration may be necessary.


Changes in 0.2.9
----------------
Rebreanding/Renaming.

Changes in 0.2.8
----------------
- Add repeat to Paste Special
- Moved palette to staus bar
- Bug fixes.
- Improved tile view
- Getting Started window.

Changes in 0.2.7
---------------

- Network drafts
- Bug fixes

Changes in 0.2.6
----------------

- Undo for all editing actions
- Paste special to scale, transpose and paste between grids.

There is a limitation that it is not possible to Undo a paste 
operation back to a blank pick or end. 

0.2.5.1
-------
Bugfix release, persistence was broken for liftplan.
 
Changes in 0.2.5
----------------
For the user:
- Can now create and edit Liftplan drafts.
- Cursor position shown

Behind the scenes:
- Better testing, in particular automated GUI tests.
- Corrected commit logs, which had been showing the wrong user.

Changes in 0.2.4
----------------
Bug fixes in loading WSML and WIF.

Changes in 0.2.3
----------------
- Select and paste picks and tie-ups.
- More documentation.

Changes in 0.2.2
----------------
- Save and load palettes.
- User Documentation

Changes in 0.2.1
----------------
- More robust WIF import.
- Floats show as continuous in the draft. 
- Consecutive cells can be selected by dragging. 
- Palette can be chosen when creating a new draft. Currently there are only built-in palettes supported. 
This is a beta release. To do:
- Creating and saving custom palettes. 
- Testing!

Changes in 0.2
--------------

- Improved and released support for WIF import.
- Behind the scenes changes to create new repo.
