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
          JLabel  device = new JLabel("Noœnik: ");
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
        	boolean suspicious1 = false;
    		for(int v = 0; v<amountField.getText().length(); v++)
    		{
    			if(((amountField.getText()).charAt(v) < 48 ||  (amountField.getText()).charAt(v) > 57) && (amountField.getText()).charAt(v) != '.') // CZYLI WG ASCII NIE JEST CYFRA
    			{
    				suspicious1 = true;
    				
    			}	
    		}
    		
    		boolean suspicious2 = false;
    		for(int v = 0; v<deliveryField.getText().length(); v++)
    		{
    			if(((deliveryField.getText()).charAt(v) < 48 ||  (deliveryField.getText()).charAt(v) > 57) && (deliveryField.getText()).charAt(v) != '.') // CZYLI WG ASCII NIE JEST CYFRA
    			{
    				suspicious2 = true;
    				
    			}	
    		}
        	
        	
            if(suspicious1 == true || suspicious2 == true)        
            {
    			JOptionPane.showMessageDialog(panel, "B³êdne dane!", "B³¹d!", JOptionPane.ERROR_MESSAGE);
    		}    
    		else
    		{
    			if(amountField.getText().isEmpty() == true || Double.parseDouble(amountField.getText() ) <=0 || deliveryField.getText().isEmpty() == true || Double.parseDouble(deliveryField.getText() ) <=0)          
        		{
        			JOptionPane.showMessageDialog(panel, "B³êdne dane!", "B³¹d!", JOptionPane.ERROR_MESSAGE);
        		}
    			else
    			{
    				if((String)titleCombo.getSelectedItem() == "Brak plyt w bazie")
        			{
        				JOptionPane.showMessageDialog(panel, "Brak plyt w bazie!", "B³¹d!", JOptionPane.ERROR_MESSAGE);
        			}
    				else
    				{
    					int ind = -1;
    					try 
    					{
    						ResultSet rs1 = stmt.executeQuery("SELECT indeks from plyty where tytul = '" + (String)titleCombo.getSelectedItem() + "';");
    						while(rs1.next())
    		 			 	{
    							ind= rs1.getInt("indeks");
    		 			 	}
    					} catch (SQLException e1) 
    					{
    						e1.printStackTrace();
    					}

    					ResultSet titleRes = null;
    					String titleToRow = "";
    		    		try 
    					{
    		    		titleRes = stmt.executeQuery("select tytul from plyty where indeks = " + Integer.toString(ind) + ";");
    				        while(titleRes.next())
    				        {
    				        	titleToRow = titleRes.getString("tytul");       
    				        }
    					} catch (SQLException f)  
    					{
    						f.printStackTrace();
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
    		    		int kindComboInt = 0, deviceComboInt = 0;
    		    		if(((String)kindCombo.getSelectedItem()).equals("album"))
    		    			kindComboInt = 1;
    		    		if(((String)deviceCombo.getSelectedItem()).equals("CD"))
    		    			deviceComboInt = 1;
    					String sqlInsert = "INSERT into egzemplarze (indeks, indeks_plyty, nosnik, cena, koszt_dostawy, rodzaj, stan) values (" + Integer.toString(nr) + ", " + Integer.toString(ind) + ", " + deviceComboInt + ", " + amountField.getText() + " , " + deliveryField.getText() + "," + kindComboInt + ", 0);";
    					System.out.println(sqlInsert);
    					try 
    					{
							int insertInt = stmt.executeUpdate(sqlInsert);
						} catch (SQLException e1) 
    					{
							e1.printStackTrace();
						}	
    		
    					
    					
    		        	String[] newRow = {Integer.toString(nr), titleToRow, (String)deviceCombo.getSelectedItem(), amountField.getText(), deliveryField.getText(), (String)kindCombo.getSelectedItem(), "0" };
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