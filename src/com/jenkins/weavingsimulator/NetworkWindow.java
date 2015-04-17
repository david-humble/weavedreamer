package com.jenkins.weavingsimulator;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.jenkins.weavingsimulator.datatypes.NetworkDraft;
import com.jenkins.weavingsimulator.models.BeanPropertyCommand;
import com.jenkins.weavingsimulator.models.EditingSession;
import com.jenkins.weavingsimulator.models.NetworkInitialModel;
import com.jenkins.weavingsimulator.models.NetworkKeyModel;
import com.jenkins.weavingsimulator.models.PatternLineModel;
import com.jenkins.weavingsimulator.models.ThreadingDraftModel;
import com.jenkins.weavingsimulator.models.TieUpModel;

public class NetworkWindow extends EditingSessionWindow {
	private JScrollPane pane;
	private JPanel panel1;
	private JPanel panel2;
	private JFormattedTextField initialRows;
	private JFormattedTextField initialCols;
	private JFormattedTextField patternLineRows;
	private JFormattedTextField patternLineCols;
	private JFormattedTextField ribbonWidth;
	private JComboBox compressionRule;
	private GridControl initialGrid;
	private GridControl patternLineGrid;
	private GridControl key1;
	private GridControl key2;
	
	NetworkDraft network;
	
	private NonNegativeIntFormatter formatter = new NonNegativeIntFormatter();
	
	public NetworkWindow (EditingSession session)
	{
		super(session);
		
		network = session.getDraft().getNetwork();
		
		setClosable(true);
		setMaximizable(true);
		setResizable(true);
		
		initComponents();
		
		initialRows.addFocusListener(new FocusListener() {	
			public void focusLost(FocusEvent e) {
				try {
					initialRows.commitEdit();
				} catch (ParseException e1) {
				}
				getSession().execute(new BeanPropertyCommand<Integer>(network, "initialRows", ((Long) initialRows.getValue()).intValue()));				
			}
			
			public void focusGained(FocusEvent e) {
			}
		});

		initialCols.addFocusListener(new FocusListener() {	
			public void focusLost(FocusEvent e) {			
				try {
					initialCols.commitEdit();
				} catch (ParseException e1) {
				}
				getSession().execute(new BeanPropertyCommand<Integer>(network, "initialCols", 
						((Long) initialCols.getValue()).intValue()));				
			}
			public void focusGained(FocusEvent e) {
			}
        });

		patternLineRows.addFocusListener(new FocusListener() {	
			public void focusLost(FocusEvent e) {			
				try {
					patternLineRows.commitEdit();
				} catch (ParseException e1) {
				}
				getSession().execute(new BeanPropertyCommand<Integer>(network, "patternLineRows", 
						((Long) patternLineRows.getValue()).intValue()));				}
			public void focusGained(FocusEvent e) {
			}
        });
		
		patternLineCols.addFocusListener(new FocusListener() {	
			public void focusLost(FocusEvent e) {			
				try {
					patternLineCols.commitEdit();
				} catch (ParseException e1) {
				}
				getSession().execute(new BeanPropertyCommand<Integer>(network, "patternLineCols", 
						((Long) patternLineCols.getValue()).intValue()));			}
			public void focusGained(FocusEvent e) {
			}
        });
		
		compressionRule.addItemListener(new ItemListener() {	
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					getSession().execute(new BeanPropertyCommand<Boolean>(network, "telescoped", 
							e.getItem().toString() == "Telescope"));			
					}
				}
		});
		
		ribbonWidth.addFocusListener(new FocusListener() {	
			public void focusLost(FocusEvent e) {			
				try {
					ribbonWidth.commitEdit();
				} catch (ParseException e1) {
				}
				getSession().execute(new BeanPropertyCommand<Integer>(network, "ribbonWidth", 
						((Long) ribbonWidth.getValue()).intValue()));	
			}
			public void focusGained(FocusEvent e) {
			}
        });
		
		setTitle ("network");
	}
	
	private void initComponents() {
		pane = new JScrollPane();
		panel1 = new JPanel(new GridBagLayout());
		panel2 = new JPanel(new GridBagLayout());
		getContentPane().add(pane);
		JPanel scrollable = new JPanel();
		scrollable.setLayout(new GridBagLayout());
		scrollable.add(panel1, makeConstraints(0,0));
		scrollable.add(panel2, makeConstraints(0,1));
		pane.setViewportView(scrollable);

		int y = 0;
		addLabel("Initial", y, 0, panel1);
		
		initialRows = new JFormattedTextField(formatter);
		initialRows.setName("initialRows");
		initialRows.setColumns(2);
		initialRows.setValue(network.getInitialRows());
		panel1.add(initialRows, makeConstraints(1, y));	
		network.addPropertyChangeListener("initialRows", new TextFieldBinder(initialRows));
        addLabel("x", 2, y, panel1);

		initialCols = new JFormattedTextField(formatter);
		initialCols.setName("initialCols");
		initialCols.setColumns(2);
		initialCols.setValue(network.getInitialCols());
		panel1.add(initialCols, makeConstraints(3, y));
		network.addPropertyChangeListener("initialCols", new TextFieldBinder(initialCols));
        
		addLabel("Pattern line", 0, ++y, panel1);
		
		patternLineRows = new JFormattedTextField(formatter);
		patternLineRows.setName("patternLineRows");
		patternLineRows.setColumns(2);
		patternLineRows.setValue(network.getPatternLineRows());
		panel1.add(patternLineRows, makeConstraints(1, y));	
		network.addPropertyChangeListener("patternLineRows", new TextFieldBinder(patternLineRows));

        addLabel("x", 2, y, panel1);

        patternLineCols = new JFormattedTextField(formatter);
        patternLineCols.setName("patternLineCols");
        patternLineCols.setColumns(2);
        patternLineCols.setValue(network.getPatternLineCols());
        panel1.add(patternLineCols, makeConstraints(3, y));
		network.addPropertyChangeListener("patternLineCols", new TextFieldBinder(patternLineCols));
       
        addLabel("Ribbon", 0, ++y, panel1);
        
        ribbonWidth = new JFormattedTextField(formatter);
        ribbonWidth.setName("ribbonWidth");
        ribbonWidth.setColumns(2);
        ribbonWidth.setValue(network.getRibbonWidth());
        panel1.add(ribbonWidth, makeConstraints(1, y));
		network.addPropertyChangeListener("ribbonWidth", new TextFieldBinder(ribbonWidth));
       
        addLabel ("Compression rule", 0, ++y, panel1);
        compressionRule = new JComboBox();
        compressionRule.addItem("Telescope");
        compressionRule.addItem("Digitize");
        compressionRule.setName("compressionRule");
        compressionRule.setSelectedItem(network.isTelescoped() ? "Telescope" : "Digitize");
        panel1.add(compressionRule, makeConstraints(1, y, 3));

        addLabel ("Initial", 0, 0, panel2);
        initialGrid = new WeavingGridControl();
        initialGrid.setName("initialGrid");
        initialGrid.setModel(new NetworkInitialModel(getSession()));
        panel2.add(initialGrid, makeConstraints(0,1));
     
        addLabel ("Key 1", 1, 0, panel2);
        key1 = new WeavingGridControl();
        key1.setName("key1");
        key1.setModel(new NetworkKeyModel(getSession(), new NetworkKeyModel.KeyModelAdapter() {
			public String getKeyProperty() {
				return "key1";
			}
			public void set(int col, int row, boolean value) {
				network.setKey1(col, row, value);
			}
			public boolean get(int col, int row) {
				return network.getKey1().get(col).get(row);
			}
        }));
        panel2.add(key1, makeConstraints(1,1));

        addLabel ("Key 2", 2, 0, panel2);
        key2 = new WeavingGridControl();
        key2.setName("key2");
        key2.setModel(new NetworkKeyModel(getSession(), new NetworkKeyModel.KeyModelAdapter() {
			public String getKeyProperty() {
				return "key2";
			}
			public void set(int col, int row, boolean value) {
				network.setKey2(col, row, value);
			}
			public boolean get(int col, int row) {
				return network.getKey2().get(col).get(row);
			}
        }));
        panel2.add(key2, makeConstraints(2,1));
        
        addLabel ("Pattern line", 0, 3, panel2);
        patternLineGrid = new WeavingGridControl();
        patternLineGrid.setName("patternLineGrid");
        patternLineGrid.setModel(new PatternLineModel(getSession()));
        panel2.add(patternLineGrid, makeConstraints(0, 4, GridBagConstraints.REMAINDER));
        
        pack();
	}
	
	private void addLabel (String text, int x, int y, JPanel panel) {
        JLabel label = new JLabel();
        label.setText(text);
        panel.add(label, makeConstraints(x, y));			
	}
	
	private GridBagConstraints makeConstraints(int x, int y) {
		GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = x;
        gridBagConstraints.gridy = y;
        gridBagConstraints.insets = new Insets(2, 5, 2, 5);
        return gridBagConstraints;
	}
	
	private GridBagConstraints makeConstraints (int x, int y, int width)
	{
		GridBagConstraints c = makeConstraints(x, y);
		c.gridwidth = width;
		return c;
	}
}