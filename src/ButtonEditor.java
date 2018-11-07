import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
  

public class ButtonEditor extends DefaultCellEditor 
{
  protected JButton button;
  private String label;
  private boolean isPushed;
  int rowNumber;
  
  public ButtonEditor(JCheckBox checkBox) 
  {
    super(checkBox);
    button = new JButton();
    button.setOpaque(true);
    button.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent e) 
      {
        fireEditingStopped();
      }
    });
  }
  
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) 
  {
	rowNumber = row;
	if (isSelected) 
	{
	  button.setForeground(table.getSelectionForeground());
	  button.setBackground(table.getSelectionBackground());
	} else
	{
	  button.setForeground(table.getForeground());
	  button.setBackground(table.getBackground());
	}
	label = (value ==null) ? "" : value.toString();
	button.setText( label );
	isPushed = true;
	return button;
  }
  
  public Object getCellEditorValue() 
  {
    if (isPushed)  
    {
    	System.out.println(rowNumber);
    	Object id = Invoices.table.getValueAt(rowNumber, 0);
    	String invoiceNr =(String)id;
    	System.out.println("numer faktury pobrany: " + invoiceNr);
    	String sql = "select * from produkty_faktur where indeks_faktury = " + invoiceNr + ";";
    	//PO NACISNIECIU NA BUTTON POKAZUJE SIE OKNO Z DANYMI NA TEJ FAKTURZE
    	DetailedInvoice DI = new DetailedInvoice(sql);
    	DI.setVisible(true);
    }
    isPushed = false;
    return new String( label ) ;
  }
    
  public boolean stopCellEditing() 
  {
    isPushed = false;
    return super.stopCellEditing();
  }
  
  protected void fireEditingStopped() 
  {
    super.fireEditingStopped();
  }
}