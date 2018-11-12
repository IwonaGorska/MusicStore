import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
 
public class NewInvoice extends JFrame 
{
   JPanel panel = new JPanel();
   JComboBox type, ID_klienta, ID_dostawcy;
   Connection conn;
   Statement stmt;
   static JButton  saveButton;
   JButton  addButton;
   static double amount;
	
   public NewInvoice() 
   {
        setTitle("Nowa faktura");
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
			  System.out.println("Nie poszlo dobrze z baza w NewInvoice");
			e1.printStackTrace();
		  }
		  
          JButton  closeButton = new JButton ("Zamknij");
          addButton = new JButton ("Dodaj produkty na fakturê");
          addButton.setSize(200, 30);
          addButton.addActionListener(new ButtonAdd());
          panel.add(addButton);
          JLabel  typeLabel = new JLabel("Rodzaj dokumentu: ");
          JLabel  IDKLabel= new JLabel("ID Klienta: ");
          JLabel  IDDLabel= new JLabel("ID Dostawcy: ");
          closeButton.setSize(90, 30); 
           
          addButton.setLocation(getWidth()-400,getHeight()-90);
          type = new JComboBox(new Object[]{"Faktura wychodz¹ca (0)", "Faktura przychodz¹ca (1)"});
          ID_klienta = new JComboBox();
          ID_dostawcy = new JComboBox();
          ID_dostawcy.setEnabled(false);
          type.setSize(300, 30);
          typeLabel.setSize(200,30);
          ID_klienta.setSize(300, 30);
          ID_dostawcy.setSize(300, 30);
          IDKLabel.setSize(200,30);
          IDDLabel.setSize(200,30);         
          
          type.addActionListener (new ActionListener () 
          {
        	    public void actionPerformed(ActionEvent e) 
        	    {
        	    	if(((String)type.getSelectedItem()).equals("Faktura wychodz¹ca (0)"))
        	    	{
        	    		ID_klienta.setEnabled(true);
        	    		ID_dostawcy.setEnabled(false);
        	    	}
        	    	else
        	    	{
        	    		ID_klienta.setEnabled(false);
        	    		ID_dostawcy.setEnabled(true);
        	    	}	
        	    		
        	    }
        	});
          
          try 
			{
				ResultSet rs1 = stmt.executeQuery("SELECT * from klienci;");
				int k = 0;
				while(rs1.next())
 			 	{
					int indeks = rs1.getInt(1);
					System.out.println("indeks do combobox klienta " + indeks);
					String indeksString = Integer.toString(indeks);
					System.out.println("indeksString do combobox klienta " + indeksString);
					ID_klienta.addItem(Integer.toString(indeks));
					k++;
 			 	}
				if(k == 0)
					ID_klienta.addItem("Brak klientów w bazie");

			} catch (SQLException e1) 
			{
				e1.printStackTrace();
			}
          try 
			{
				ResultSet rs1 = stmt.executeQuery("SELECT * from dostawcy;");
				int k = 0;
				while(rs1.next())
			 	{
					int indeks = rs1.getInt(1);
					System.out.println("indeks do combobox dostawcy " + indeks);
					String indeksString = Integer.toString(indeks);
					System.out.println("indeksString do combobox dostawcy " + indeksString);
					ID_dostawcy.addItem(Integer.toString(indeks));
					System.out.println("weszlam do srodka combo dostawcy dodaje elementy");
					k++;
			 	}
				if(k == 0)
					ID_dostawcy.addItem("Brak dostawców w bazie");
				
				
			} catch (SQLException e1) 
			{
				e1.printStackTrace();
			}
         
          closeButton.setLocation(getWidth()-190,getHeight()-90);
          typeLabel.setLocation(getWidth()-550,getHeight()-290);
          IDKLabel.setLocation(getWidth()-550,getHeight()-250);
          IDDLabel.setLocation(getWidth()-550,getHeight()-210);
          type.setLocation(getWidth()-400,getHeight()-290);
          ID_klienta.setLocation(getWidth()-400,getHeight()-250);
          ID_dostawcy.setLocation(getWidth()-400,getHeight()-210);
          
          panel.setLayout(null);
          panel.add(closeButton);
          panel.add(type);
          panel.add(ID_klienta);
          panel.add(ID_dostawcy);
          panel.add(typeLabel);
          panel.add(IDKLabel);
          panel.add(IDDLabel);
          closeButton.setToolTipText("Zamknij okno.");
          
          this.getContentPane().add(panel);
          closeButton.addActionListener(new ButtonClose());
          saveButton = new JButton ("Zapisz");
          saveButton.setVisible(false);
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
        	ResultSet invoiceNrRes = null;
    		int invoiceNr = -1;
    		try 
			{
    			invoiceNrRes = stmt.executeQuery("select count(*) as counter from faktury;");
		        while(invoiceNrRes.next())
		        {
		        	invoiceNr = invoiceNrRes.getInt("counter") ;       
		        }
			} catch (SQLException f) 
			{
				f.printStackTrace();
			}

    		//JESLI OKNO JEST ZAMYKANE BEZ ZAPISU, TO TRZEBA COFNAC WSZYSTKIE ZMIANY NA STANACH, KTORE WPROWADZILO OKNO DODAWANIA PRODUKTU NA FAKTURE
    		ResultSet backUpdateRes = null;  		
    		try 
			{
    			backUpdateRes = stmt.executeQuery("select * from produkty_faktur where indeks_faktury = " + Integer.toString(invoiceNr) + ";");
		        while(backUpdateRes.next())
		        {
		        	int productId = backUpdateRes.getInt("indeks_egzemplarza"); 
		        	int pieces = backUpdateRes.getInt("sztuki");
		        	
		        	//cofam zmiany na stanie
		        	String updSql = "";
		        	if(((String)type.getSelectedItem()).equals("Faktura wychodz¹ca (0)"))		        	
		        		updSql = "update egzemplarze set stan = stan + " + Integer.toString(pieces) + " where indeks = " + Integer.toString(productId) + ";";
		        	else
	        			updSql = "update egzemplarze set stan = stan - " + Integer.toString(pieces) + " where indeks = " + Integer.toString(productId) + ";";
	        		
		        	try 
	        		{
						stmt.executeUpdate(updSql);
					} catch (SQLException e1) 
	        		{
						e1.printStackTrace();
					}
		        	
		        }
			} catch (SQLException f) 
			{
				f.printStackTrace();
			}
    		
    		
        	// I TRZEBA USUNAC DODANE W OKNIE DODAWANIA PRODUKTU NA FAKTURE WIERSZE Z TABELI PRODUKTY_FAKTUR I FAKTURE TEZ
        	String sqlDelete1 = "DELETE from produkty_faktur where indeks_faktury = " + Integer.toString(invoiceNr) + ";";
        	String sqlDelete2 = "DELETE from faktury where indeks = " + Integer.toString(invoiceNr) + ";";
				try 
				{
					stmt.executeUpdate(sqlDelete1);
					stmt.executeUpdate(sqlDelete2);
				} catch (SQLException e1) 
				{
					e1.printStackTrace();
				}	
        	
            dispose();
        }
    }
    
    private class ButtonAdd implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
        	boolean canOpen = true;
        	if(((String)type.getSelectedItem()).equals("Faktura wychodz¹ca (0)"))
            {
            	if(((String)ID_dostawcy.getSelectedItem()).equals("Brak dostawców w bazie"))
            		canOpen = false;
            }
        	if(((String)type.getSelectedItem()).equals("Faktura przychodz¹ca (1)"))
            {
            	if(((String)ID_dostawcy.getSelectedItem()).equals("Brak klientów w bazie"))
            		canOpen = false;
            }
        	
        	if(canOpen == true)
        	{
        		String sql = "select * from egzemplarze;";
            	int contrahentType;
            	if(((String)type.getSelectedItem()).equals("Faktura wychodz¹ca (0)"))
            		contrahentType = -10;
            	else
            		contrahentType = -20;
            	Products P = new Products(sql, true, contrahentType);
            	addButton.setVisible(false); // zeby nie wciskali juz wiecej, bo znow owstanie "pusta" faktura
            	type.setEnabled(false);
            	ID_klienta.setEnabled(false);
            	ID_dostawcy.setEnabled(false);
            	// i nie usune jej, bo usuwa j¹ dopiero przycisk zamknij
//            	saveButton.setVisible(true);
    			P.setVisible(true);
        	}      	
        }
    }
 
    private class ButtonSave implements ActionListener
    { 
    	
        public void actionPerformed(ActionEvent e)
        {
        	System.out.println("AMOUNT = " + amount);
        	if(amount > 0)
        	{
	            short typeShort;
	            int IDint = -1;//zeby kompilator sie nie czepial, a i tak jesli nie ma kntrahentow to nie zostanie wykorzystane nigdzie
	            
	            ResultSet invoiceNrRes = null;
	    		int invoiceNr = -1;
	    		try 
				{
	    			invoiceNrRes = stmt.executeQuery("select count(*) as counter from faktury;");
			        while(invoiceNrRes.next())
			        {
			        	invoiceNr = invoiceNrRes.getInt("counter");       
			        }
				} catch (SQLException f) 
				{
					f.printStackTrace();
				}
            
	    		if(((String)type.getSelectedItem()).equals("Faktura wychodz¹ca (0)"))
	            {
	            	typeShort = 0;
	            	if(!((String)ID_dostawcy.getSelectedItem()).equals("Brak dostawców w bazie"))
	            		IDint = Integer.parseInt((String)ID_dostawcy.getSelectedItem());
	            }	
	            else
	            {
	            	typeShort = 1;
	            	if(!((String)ID_klienta.getSelectedItem()).equals("Brak klientów w bazie"))
	            		IDint = Integer.parseInt((String)ID_klienta.getSelectedItem());
	            }
            	
    			double vatD = 0.23 * amount;	
                double nettoD = amount - vatD;
    			
    			if(((String)ID_klienta.getSelectedItem()).equals("Brak klientów w bazie") && typeShort == 1 )
    			{
    				JOptionPane.showMessageDialog(panel, "Brak klientów w bazie!", "B³¹d!", JOptionPane.ERROR_MESSAGE);
    			}
	
    			else
    			{
    				if(((String)ID_dostawcy.getSelectedItem()).equals("Brak dostawców w bazie") && typeShort == 0 )
        			{
        				JOptionPane.showMessageDialog(panel, "Brak dostawców w bazie!", "B³¹d!", JOptionPane.ERROR_MESSAGE);
        			}
    				else
    				{
    					
            			String sqlInsert = "update faktury set data_sprzedazy = current_date, wartosc_netto = " + String.valueOf(nettoD) + ", wartosc_brutto = " + String.valueOf(amount) + ", wartosc_vat = " + String.valueOf(vatD) + ", rodz_dok = " + typeShort + "," + "id_klienta_dostawcy = " + Integer.toString(IDint) + " where indeks = " + Integer.toString(invoiceNr) + ";" ;
            				try 
        					{
    							stmt.executeUpdate(sqlInsert);
    						} catch (SQLException e1) 
        					{
    							e1.printStackTrace();
    						}	
        					dispose(); //! juz do przycisku close nie dojdzie i dobrze
        					
        					String currDate;
    					    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    					    java.util.Date date = new java.util.Date();
    					    currDate = dateFormat.format(date);
        					
        		        	int nr = Invoices.model.getRowCount() + 1;
        		        	String[] newRow = {Integer.toString(nr), currDate, String.valueOf(nettoD), String.valueOf(amount), String.valueOf(vatD), String.valueOf(typeShort), Integer.toString(IDint), "Zobacz produkty"};
        		        	Invoices.model.addRow(newRow);
    				}			
    			}										
        	}
        }
    }
 
    public static void main(String[] args) 
    {

    }
}