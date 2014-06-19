import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class Calculator extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	public static final int WINX=400,WINY = 110;
	
	private JTextField expField,resField;
	private JButton evalBtn;
	private JCheckBox radBox;
	

	public static void main(String[] args) {
		Calculator calc = new Calculator();
		calc.setVisible(true);
	}
	
	public Calculator(){
		expField = new JTextField(25);
		resField = new JTextField(20);
		evalBtn = new JButton("eval");
		radBox = new JCheckBox("use radian");
		
		this.setSize(WINX,WINY);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("CalcLight");
		this.setLayout(new FlowLayout());
		
		evalBtn.addActionListener(this);
		resField.setEditable(false);
		
		this.add(expField);
		this.add(evalBtn);
		this.add(radBox);
		this.add(resField);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		double d;
		try{
			d = ExpressionEval.solve(expField.getText(),!radBox.isSelected());
			resField.setText(Double.toString(d));
		}catch(Exception e){
			resField.setText("error : " + e.getMessage());
		}		
	}

}
