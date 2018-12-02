import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
 
class NewDeliver extends JFrame 
{	
   JPanel panel = new JPanel();
   Connection conn;
   Statement stmt;
private JTextField nameField;
private JTextField nipField;
private JTextField nr_telField;
   	
   public NewDeliver() 
   {
        setTitle("Nowy dostawca");
        setSize(600,350);
        setResizable(false);
        setLocationRelativeTo(null);
        initComponents();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocation(250,150);
   }
 
    public void initComponents()
    {
		  try 
		  {
			DriverManager.registerDriver(new org.postgresql.Driver());
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/musicstore", "postgres", "admin");
			stmt = conn.createStatement();
		  } catch (SQLException e1) 
		  {
			  System.out.println("Nie poszlo dobrze z baza w NewDeliver");
			e1.printStackTrace();
		  }
		  
          JButton  closeButton = new JButton ("Zamknij");
          JLabel  name = new JLabel("Nazwa: "); 
          JLabel  nip = new JLabel("NIP: ");
          JLabel  nr_tel= new JLabel("Nr_Telefonu: ");
          
          nameField = new JTextField(6);
          nipField = new JTextField(6);
          nr_telField = new JTextField(6);

          name.setSize(200,30);
          nip.setSize(200,30);
          nr_tel.setSize(200,30);        
          nameField.setSize(200,30);
          nipField.setSize(200,30);
          nr_telField.setSize(200,30);

          closeButton.setLocation(getWidth()-190,getHeight()-90);
          name.setLocation(getWidth()-550,getHeight()-330);
          nip.setLocation(getWidth()-550,getHeight()-290);
          nr_tel.setLocation(getWidth()-550,getHeight()-250);
          
          nameField.setLocation(getWidth()-400,getHeight()-330);
          nipField.setLocation(getWidth()-400,getHeight()-290);
          nr_telField.setLocation(getWidth()-400,getHeight()-250);
          
          panel.setLayout(null);
          panel.add(closeButton);
          panel.add(name);
          panel.add(nip);
          panel.add(nr_tel);
          panel.add(nameField);
          panel.add(nipField);
          panel.add(nr_telField);
          closeButton.setToolTipText("Zamknij okno.");
          
          this.getContentPane().add(panel);
          closeButton.addActionListener(new ButtonClose());
          JButton  saveButton = new JButton ("Zapisz");
          saveButton.setLocation(getWidth()-310,getHeight()-90);
          saveButton.setSize(90, 30);
          saveButton.addActionListener(new ButtonSave());
          panel.add(saveButton);
          closeButton.setForeground(new Color(253,4,21));
    }
 
    private class ButtonClose implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            dispose();
        }
    }
 
    private class ButtonSave implements ActionListener
    { 
        public void actionPerformed(ActionEvent e)
        {
        	String nipString = nipField.getText();
        	String telString = nr_telField.getText();
        	boolean isTelAndNipOk = true;
        	if(nipString.length() != 10)// chyba juz wtedy nipField.getText().isEmpty() == true nie trzeba sprawdzac
        		isTelAndNipOk = false;
        	if(telString.length() != 9)//chyba juz wtedy nr_telField.getText().isEmpty() == true nie trzeba sprawdzac
        		isTelAndNipOk = false;
        	for(int i = 0; i<nipString.length(); i++)
            {
            	if(nipString.charAt(i) != '0' && nipString.charAt(i) != '1' && nipString.charAt(i) != '2' && nipString.charAt(i) != '3' 
            			&& nipString.charAt(i) != '4' && nipString.charAt(i) != '5' && nipString.charAt(i) != '6'
            			&& nipString.charAt(i) != '7' && nipString.charAt(i) != '8' && nipString.charAt(i) != '9' )
            		isTelAndNipOk = false;
            }
        	for(int i = 0; i<telString.length(); i++)
            {
            	if(telString.charAt(i) != '0' && telString.charAt(i) != '1' && telString.charAt(i) != '2' && telString.charAt(i) != '3' 
            			&& telString.charAt(i) != '4' && telString.charAt(i) != '5' && telString.charAt(i) != '6'
            			&& telString.charAt(i) != '7' && telString.charAt(i) != '8' && telString.charAt(i) != '9' )
            		isTelAndNipOk = false;
            }
        	

            if(nameField.getText().isEmpty() == true || !isTelAndNipOk)        
    		{
    			JOptionPane.showMessageDialog(panel, "Niepoprawne dane!", "B³¹d!", JOptionPane.ERROR_MESSAGE);
    		}    
			else
			{
				ResultSet nrRes = null;
	    		int nr = -1;
	    		try 
				{
	    			nrRes = stmt.executeQuery("select count(*) as counter from dostawcy;");
			        while(nrRes.next())
			        {
			        	nr = nrRes.getInt("counter") + 1;       
			        }
				} catch (SQLException f) 
				{
					f.printStackTrace();
				}
							
//				String sqlInsert = "INSERT into dostawcy (indeks, nazwa, nip, nr_telefonu) values (" + Integer.toString(nr) + ", '" + nameField.getText() + "', '" + nipField.getText() + "', '" + nr_telField.getText() + "');";
//				try 
//				{
//					int insertInt = stmt.executeUpdate(sqlInsert);
//				} catch (SQLException e1) 
//				{
//					e1.printStackTrace();
//				}	
				
				//
	    		try
	    		{
					PreparedStatement stmtPre = conn.prepareStatement("INSERT into dostawcy (indeks, nazwa, nip, nr_telefonu) values (" + Integer.toString(nr) + ", ?,?,?);");
					stmtPre.setString(1, nameField.getText());
					stmtPre.setString(2, nipField.getText());
					stmtPre.setString(3, nr_telField.getText());
					stmtPre.executeUpdate();
	    		}
	    		catch (SQLException e1)
	    		{
	    			e1.printStackTrace();
	    		}
				//

	        	String[] newRow = {Integer.toString(nr), nameField.getText(), nipField.getText(), nr_telField.getText()};
	        	Delivers.model.addRow(newRow);
	        	
	        	dispose();
			}
 
        }
    }
 
    public static void main(String[] args) 
    {

    }
}