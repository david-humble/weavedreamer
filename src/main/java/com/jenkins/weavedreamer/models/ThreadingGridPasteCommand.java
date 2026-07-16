package com.jenkins.weavedreamer.models;

public class ThreadingGridPasteCommand implements Command {

    CopyableWeavingGridModel model;
    PasteGrid selection;
    PasteGrid previous;

    /**
     * Creates a new PasteCommand that will paste the selection into the model
     * at row, column, and store the previous state of the model in the
     * undoSelection for undo. undoSelection is needed because the threading
     * model, and the treadling model in non-liftplan, need more than jsut the
     * pasted area to be able to undo.
     *
     * @param model     The model to act on
     * @param selection The selection to paste
     */
    public ThreadingGridPasteCommand(CopyableWeavingGridModel model, PasteGrid selection) {
        this.model = model;
        this.selection = selection;
        previous = model.getUndoSelection(selection);
    }

    public void execute() {
        paste(selection);
    }

    public void undo() {
        paste(previous);
    }

    private void paste(PasteGrid cells) {
    	
    	final int pasteBlockStartCol = Math.max(0, -cells.getStartColumn());
        final int rowcount = Math.min(cells.getRows(), model.getRowCount() - cells.getStartRow());
        // final int colcount = Math.min(cells.getColumns(), model.getColumnCount() - cells.getStartColumn());
        final int colcount = Math.min(cells.getColumns(), model.getColumnCount() - cells.getStartColumn())-pasteBlockStartCol;

        for (int col = 0; col != colcount; col++) {
        	//System.out.println(Integer.toString(pasteBlockStartCol) + " "+ Integer.toString(col) + " " + Integer.toString(cells.getStartColumn()));
            model.setBooleanValueAt(false,
                    -1,
                    col + cells.getStartColumn()+pasteBlockStartCol);
                    //col + pasteBlockStartCol);
            for (int row = 0; row != rowcount; row++) {


                model.setBooleanValueAt(cells.getValue(row, col +pasteBlockStartCol ),
                        row + cells.getStartRow(),
                        col + cells.getStartColumn() +pasteBlockStartCol  );
                       //col + pasteBlockStartCol);
            }
        }
    }

}
