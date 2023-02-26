package view;

import javax.swing.*;

/**
 * hwq:无作用
 */
public class StructureDlg extends JDialog{

	public StructureDlg(java.awt.Frame parent, boolean modal){
	     super(parent, modal);
	     initComponents();
	     setTitle("框架元素设定");
	}
	
	void initComponents(){
		
	}
	
	private JLabel setJbl;
	private JButton okBtn;
	private JButton cancelBtn; 
}
class PaintPanel extends JPanel{
	
}