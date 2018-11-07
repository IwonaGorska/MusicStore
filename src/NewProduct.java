import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.*;
 
public class NewProduct extends JFrame 
{
    JPanel panel = new JPanel();
    Connection conn;
    Statement stmt;
	private JComboBox titleCombo;
	private JComboBox kindCombo;
	private JTextField deliveryField;
	private JTextField amountField;
	private JComboBox deviceCombo;
	
   public NewProduct() 
   {
        setTitle("Nowy produkt");
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
			  System.out.println("Nie poszlo dobrze z baza w NewProduct");
			e1.printStackTrace();
		  }
		  
          JButton  closeButton = new JButton ("Zamknij");
          JLabel  title = new JLabel("Tytul: "); 
          JLabel  device = new JLabel("No�nik: ");
          JLabel  amount= new JLabel("Cena: ");
          JLabel  delivery= new JLabel("Koszt_dostawy: ");
          JLabel  kind= new JLabel("Rodzaj: ");
          
          titleCombo = new JComboBox(); 
          deviceCombo = new JComboBox(new Object[]{"CD", "vinyl"}); 
          amountField = new JTextField(6); 
          deliveryField = new JTextField(6);
          kindCombo = new JComboBox(new Object[]{"album", "singiel"}); 

          title.setSize(200,30);
          device.setSize(200,30);
          amount.setSize(200,30);
          delivery.setSize(200,30);
          kind.setSize(200,30);
          titleCombo.setSize(300, 30);
          deviceCombo.setSize(300, 30);
          amountField.setSize(200,30);                   
          deliveryField.setSize(200,30);
          kindCombo.setSize(300, 30);                  
          
          try 
			{
				ResultSet rs1 = stmt.executeQuery("SELECT * from plyty;");
				int k = 0;
				while(rs1.next())
 			 	{
					String titleString = rs1.getString("tytul");
					titleCombo.addItem(titleString);
					k++;
 			 	}
				if(k == 0)
					titleCombo.addItem("Brak plyt w bazie");
			} catch (SQLException e1) 
			{
				e1.printStackTrace();
			}
          
          closeButton.setLocation(getWidth()-190,getHeight()-90);
          title.setLocation(getWidth()-550,getHeight()-330);
          device.setLocation(getWidth()-550,getHeight()-290);
          amount.setLocation(getWidth()-550,getHeight()-250);
          delivery.setLocation(getWidth()-550,getHeight()-210);
          kind.setLocation(getWidth()-550,getHeight()-170);
          
          titleCombo.setLocation(getWidth()-400,getHeight()-330);
          deviceCombo.setLocation(getWidth()-400,getHeight()-290);
          amountField.setLocation(getWidth()-400,getHeight()-250);
          deliveryField.setLocation(getWidth()-400,getHeight()-210);
          kindCombo.setLocation(getWidth()-400,getHeight()-170);
          
          panel.setLayout(null);
          panel.add(closeButton);
          panel.add(title);
          panel.add(device);
          panel.add(amount);
          panel.add(delivery);
          panel.add(kind);
          panel.add(titleCombo);
          panel.add(deviceCombo);
          panel.add(amountField);
          panel.add(deliveryField);
          panel.add(kindCombo);
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
            if(amountField.getText().isEmpty() == true)        
    		{
    			JOptionPane.showMessageDialog(panel, "Uzupe�nij wszystkie wymagane pola!", "B��d!", JOptionPane.ERROR_MESSAGE);
    		}    
    		else
    		{
    			if(deliveryField.getText().isEmpty() == true)        
        		{
        			JOptionPane.showMessageDialog(panel, "Uzupe�nij wszystkie wymagane pola!", "B��d!", JOptionPane.ERROR_MESSAGE);
        		}
    			else
    			{
    				if((String)titleCombo.getSelectedItem() == "Brak plyt w bazie")
        			{
        				JOptionPane.showMessageDialog(panel, "Brak plyt w bazie!", "B��d!", JOptionPane.ERROR_MESSAGE);
        			}
    				else
    				{
    					int ind = -1;
    					try 
    					{
    						ResultSet rs1 = stmt.executeQuery("SELECT indeks from plyty where tytul = " + (String)titleCombo.getSelectedItem() + ";");
    						while(rs1.next())
    		 			 	{
    							ind= rs1.getInt("indeks");
    		 			 	}
    					} catch (SQLException e1) 
    					{
    						e1.printStackTrace();
    					}

    					
    					ResultSet nrRes = null;
    		    		int nr = -1;
    		    		try 
    					{
    		    			nrRes = stmt.executeQuery("select count(*) as counter from egzemplarze;");
    				        while(nrRes.next())
    				        {
    				        	nr = nrRes.getInt("counter") + 1;       
    				        }
    					} catch (SQLException f) 
    					{
    						f.printStackTrace();
    					}
    					
    					String sqlInsert = "INSERT into egzemplarze (indeks, indeks_plyty, nosnik, cena, koszt_dostawy, rodzaj, stan) values (" + Integer.toString(nr) + ", " + Integer.toString(ind) + ", " + (String)deviceCombo.getSelectedItem() + ", " + amountField.getText() + " , " + deliveryField.getText() +  (String)kindCombo.getSelectedItem() + ", 0);";
    					try 
    					{
							int insertInt = stmt.executeUpdate(sqlInsert);
						} catch (SQLException e1) 
    					{
							e1.printStackTrace();
						}	
    		
    		        	String[] newRow = {Integer.toString(nr), (String)deviceCombo.getSelectedItem(), amountField.getText(), deliveryField.getText(), (String)kindCombo.getSelectedItem(), "0" };
    		        	Products.model.addRow(newRow);
    		        	
    		        	dispose();
    				}
    			}								
    		}
        }
    }
 
    public static void main(String[] args) 
    {

    }
}