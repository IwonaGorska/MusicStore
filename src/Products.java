import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Format;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;


public class Products extends JFrame 
{ 
   Container mainPanel;
   Connection conn;
   Statement stmt;
   int indeks;
   boolean addEnabled;
   String sql;
   static DefaultTableModel model;
   JTable table;
   JButton buttonSave;
   boolean isPriceEditable;
   int indeksClient;
    
    
	public Products(String sqlStatement, boolean addE, int indC)
	{
		super("Produkty");
		sql = sqlStatement;
		indeksClient = indC;
		//ADDENABLED TRUE OZNACZA, ZE EKRAN WIDOCZNY WYSWIETLANY JEST DLA PRACOWNIKA Z JEGO FUNKCJONALNOSCIAMI PRACOWNICZYMI
		addEnabled = addE; 
		initComponents();
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
			  System.out.println("Nie poszlo dobrze z baza w Products");
			e1.printStackTrace();
		  }
    	
	    setLocation(100,20);	
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
		setResizable(false);
		mainPanel = getContentPane();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(getRow1());
        mainPanel.add(getRow2());
        mainPanel.add(getRow3());
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(getRow4());
        mainPanel.add(getRow5());
        setSize(1100,600);//width, height       
    }
    
    void setDebugBorder(JComponent c)
    {
        //Odkomentowac, zeby widziec granice
//        c.setBorder(BorderFactory.createLineBorder(Color.BLUE));
    }
	
	void setPrefferedMaxAndMinSize(Component c, int width, int height)
    {
        Dimension rozmiar = new Dimension(width, height);
        c.setPreferredSize(rozmiar);
        c.setMaximumSize(rozmiar);
        c.setMinimumSize(rozmiar);
    }
    
    private Box getBox(int height)
    {
        Box box = Box.createHorizontalBox();
        setPrefferedMaxAndMinSize(box, 4096, height);
        setDebugBorder(box);
        return box;
    }


	private Component  getRow1()
    {
        return Box.createVerticalStrut(10);
    }

	private Component  getRow2()
    {
        Box box = getBox(500);
        buildRow2(box);
        return box;
    }
	
	private Component  getRow3()
    {
       return  Box.createVerticalStrut(10);
    }

    private Component  getRow4()
    {
        Box box = getBox(30);
        buildRow4(box);
        return box;
    }

    private Component  getRow5()
    {
       return  Box.createVerticalStrut(10);
    }

    private void buildRow2(JComponent panel) 
    {
    	JPanel tablePanel = new JPanel(new BorderLayout());
    	panel.add(Box.createHorizontalStrut(30));
    	String[] colHeadings = {"INDEKS","PLYTA", "NOSNIK", "CENA", "KOSZT_DOSTAWY", "RODZAJ", "STAN", "¯¥DANA LICZBA"};
        int numRows = 0;
        model = new DefaultTableModel(numRows, colHeadings.length) ;
        model.setColumnIdentifiers(colHeadings);
        
        
        table = new JTable(model) 
        {
        	public boolean isCellEditable(int row,int column)
		    {
		    	switch(column)
		    	{
		    	case 3:
		    	{
		    		if(isPriceEditable)
		    			return true;
		    		else
		    			return false;
		    	}
		    	case 4:
		    	{
		    		if(isPriceEditable)
		    			return true;
		    		else
		    			return false;
		    	}
		        case 7:
		          return true;
		        default: return false;
		       }
		    }
        	};

        JScrollPane scroll = new JScrollPane(table);
        tablePanel.add(scroll);
        
        tablePanel.add(table, BorderLayout.CENTER);
        tablePanel.add(table.getTableHeader(), BorderLayout.NORTH);
        panel.add(tablePanel);
        setPrefferedMaxAndMinSize(table, 400, 400);
        
        table.setRowHeight(30);
  
        for (int i=0; i<8; i++) 
        {
        	  TableColumn column = table.getColumnModel().getColumn(i);
        	  if (i==0) column.setPreferredWidth(20);
        	  if (i==1) column.setPreferredWidth(90);
        	  if (i==2) column.setPreferredWidth(90);
        	  if (i==3) column.setPreferredWidth(90);
        	  if (i==4) column.setPreferredWidth(90);
        	  if (i==5) column.setPreferredWidth(90);
        	  if (i==6) column.setPreferredWidth(90);
        	  if (i==7) column.setPreferredWidth(90);
        	}

        ResultSet res = null;
		try 
		{
			res = stmt.executeQuery(sql);
	        while(res.next())
	        {
	        	int indeks = res.getInt("indeks");
	        	int indeksAlbum = res.getInt("indeks_plyty");
	        	short nosnik = res.getShort("nosnik");
	        	BigDecimal cena = res.getBigDecimal("cena");
	        	BigDecimal koszt_dostawy = res.getBigDecimal("koszt_dostawy");
	        	short rodzaj = res.getShort("rodzaj");
	        	int stan = res.getInt("stan");
				int c = Integer.valueOf(cena.intValue());
				int k_d = Integer.valueOf(koszt_dostawy.intValue());
				String n = "", r = "";
				if(nosnik == 0)
					n = "winyl";
				else
					n = "CD";
				if(rodzaj == 0)
					r = "singiel";
				else
					r = "album";				
				
	            String[] newRow = { Integer.toString(indeks), Integer.toString(indeksAlbum), n, Integer.toString(c), Integer.toString(k_d), r, Integer.toString(stan), ""};
	            model.addRow(newRow);            
	        }
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		String titleAlbum = "";
		ResultSet resTitle = null;
		String id = "";
		for(int i =0; i<model.getRowCount(); i++)
    	{
	        try 
			{
	        	id = (String)table.getValueAt(i, 1);
	        	System.out.println(id);
	        	resTitle = stmt.executeQuery("select tytul from plyty where indeks = " + id + ";");
		        while(resTitle.next())
		        {
		        	titleAlbum = resTitle.getString("tytul");       
		        }
			} catch (SQLException e) 
			{
				e.printStackTrace();
			}
	        table.setValueAt(titleAlbum, i, 1);
    	}

        panel.add(Box.createHorizontalStrut(30));      
    }

    private void buildRow4(JComponent panel) 
    {
    	panel.add(Box.createHorizontalStrut(30));

    	
    	JButton buttonToInvoice = new JButton ("Umieœæ na fakturze");
        setPrefferedMaxAndMinSize(buttonToInvoice, 200, 30);
        panel.add(buttonToInvoice);
        buttonToInvoice.addActionListener(new ButtonToInvoice());
        panel.add(Box.createHorizontalStrut(30));
        buttonToInvoice.setVisible(false);
    	
        JButton buttonClose = new JButton ("Zamknij");
        buttonClose.setToolTipText("Zamknij okno.");
        setPrefferedMaxAndMinSize(buttonClose, 90, 30);
        panel.add(buttonClose);
        panel.add(Box.createHorizontalStrut(30));
        buttonClose.addActionListener(new ButtonClose());
       
        JButton buttonAdd = new JButton ("Dodaj");
        buttonAdd.setToolTipText("Dodaj nowy egzemplarz.");
        setPrefferedMaxAndMinSize(buttonAdd, 90, 30);
        panel.add(buttonAdd);
        buttonAdd.addActionListener(new ButtonAdd());
        panel.add(Box.createHorizontalStrut(30));
        if(!addEnabled)
        	buttonAdd.setVisible(false);
        
        JButton buttonOrder = new JButton ("Zamów");
        setPrefferedMaxAndMinSize(buttonOrder, 90, 30);
        panel.add(buttonOrder);
        buttonOrder.addActionListener(new ButtonOrder());
        panel.add(Box.createHorizontalStrut(30));
        if(!addEnabled)
        	buttonOrder.setVisible(false);
        
        JButton buttonEdit = new JButton ("Edytuj ceny i zatwierdz enterem");
        setPrefferedMaxAndMinSize(buttonEdit, 215, 30);
        panel.add(buttonEdit);
        buttonEdit.addActionListener(new ButtonEdit());       
        if(!addEnabled)
        {
        	panel.add(Box.createHorizontalStrut(30));
        	buttonEdit.setVisible(false);
        }        	
        
        buttonSave = new JButton ("Zapisz ceny");
        setPrefferedMaxAndMinSize(buttonSave, 110, 30);
        panel.add(buttonSave);
        buttonSave.addActionListener(new ButtonSave());
        if(!addEnabled)
        {
        	panel.add(Box.createHorizontalStrut(30));
        }
        buttonSave.setVisible(false);
        
        JButton buttonBuy = new JButton ("Kup");
        setPrefferedMaxAndMinSize(buttonBuy, 110, 30);
        panel.add(buttonBuy);
        buttonBuy.addActionListener(new ButtonBuy());
        if(addEnabled)
        {
        	panel.add(Box.createHorizontalStrut(30));
        	buttonBuy.setVisible(false);
        }
        
        JButton buttonCart = new JButton ("Dodaj do koszyka");
        setPrefferedMaxAndMinSize(buttonCart, 200, 30);
        panel.add(buttonCart);
        buttonCart.addActionListener(new ButtonCart());
        if(addEnabled)
        {
        	panel.add(Box.createHorizontalStrut(30));
        	buttonCart.setVisible(false);
        }
        
        if(indeksClient == -10 || indeksClient == -20)
        {
        	buttonToInvoice.setVisible(true);
        	buttonAdd.setVisible(false);
        	buttonOrder.setVisible(false);
        	buttonEdit.setVisible(false);
        }
    }
    
    private class ButtonEdit implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
        	buttonSave.setVisible(true);
        	isPriceEditable = true;	
        }
    }
    
    //WIDOCZNY TYLKO Z POZIMIOMU PRACOWNIKA W SYTUACJI GDY WPROWADZA FAKTURE, UMOZLIWIA DODANIE NA NIA ARTYKULOW
    private class ButtonToInvoice implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
        	String insertSql;
        	String valueString1;
        	Object valueObject1;
        	String valueString2 = "";
        	Object valueObject2;
        	double valueInt3;
        	Object valueObject3;
        	double valueInt4;
        	Object valueObject4;
        	String valueString6 = ""; // stan
        	Object valueObject6;
        	
        	double amount = 0;
        	ResultSet invoiceNrRes = null;
    		int invoiceNr = -1;
    		try 
			{
    			invoiceNrRes = stmt.executeQuery("select count(*) as counter from faktury;");
		        while(invoiceNrRes.next())
		        {
		        	invoiceNr = invoiceNrRes.getInt("counter") + 1;       
		        }
			} catch (SQLException f) 
			{
				f.printStackTrace();
			}
    		
    		//newEmptyInvoice //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    		try 
			{
    			stmt.executeUpdate("insert into faktury (indeks, data_sprzedazy, wartosc_netto, wartosc_brutto, wartosc_vat, rodz_dok, id_klienta_dostawcy) "
    					+ "values (" + Integer.toString(invoiceNr) + ", '2000-01-01', 0, 0, 0, 1, 1);");
			} catch (SQLException f) 
			{
				f.printStackTrace();
			}
    		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    		
    		for(int i = 0; i<model.getRowCount(); i++)
        	{
        		valueObject1 = table.getValueAt(i, 0);
        		valueString1 = (String)valueObject1;
        		System.out.println("value1String: " + valueString1);
        		valueObject2 = table.getValueAt(i, 7);
        		valueString2 = (String)valueObject2;
        		System.out.println("value2String: " + valueString2);
        		int mnoznikCen;
        		if(valueString2.equals(""))
        			mnoznikCen = 0;        			
        		else
        			mnoznikCen = Integer.parseInt(valueString2);
        		System.out.println("MNOZNIK CEN = " + mnoznikCen);
        		valueObject3 = table.getValueAt(i, 3);
        		valueInt3 = Double.parseDouble((String)valueObject3);
        		valueObject4 = table.getValueAt(i, 4);
        		valueInt4 = Double.parseDouble((String)valueObject4);
        		valueObject6 = table.getValueAt(i, 6);
        		valueString6 = (String)valueObject6;
        		
        		if(!valueString2.equals("") )
        		{
	        		ResultSet nrRes = null;
		    		int nr = -1;
		    		try 
					{
		    			nrRes = stmt.executeQuery("select count(*) as counter from produkty_faktur;");
				        while(nrRes.next())
				        {
				        	nr = nrRes.getInt("counter") + 1;       
				        }
					} catch (SQLException f) 
					{
						f.printStackTrace();
					}
	        		
	        		insertSql = "INSERT into produkty_faktur (indeks, indeks_faktury, indeks_egzemplarza, sztuki) values (" + Integer.toString(nr) + "," +  Integer.toString(invoiceNr) + "," + valueString1 +  ", " + valueString2 + ");";
	        		System.out.println("-----" + insertSql);
	        		try 
	        		{
						stmt.executeUpdate(insertSql);
					} catch (SQLException e1) 
	        		{
						e1.printStackTrace();
					}
	        		
	        		String updSql = "";
	        		if(indeksClient == -10)
	        			updSql = "update egzemplarze set stan = stan - " + valueString2 + " where indeks = " + valueString1 + ";";
	        		if(indeksClient == -20)
	        			updSql = "update egzemplarze set stan = stan + " + valueString2 + " where indeks = " + valueString1 + ";";
	        		
	        		try 
	        		{
						stmt.executeUpdate(updSql);
					} catch (SQLException e1) 
	        		{
						e1.printStackTrace();
					}
	        		
	        		amount = amount + valueInt3*mnoznikCen + valueInt4*mnoznikCen; 
	        		System.out.println("amount->" + amount);
        		}
        	}
    		NewInvoice.saveButton.setVisible(true);
    		NewInvoice.amount  = amount;
    		System.out.println("amount = " + amount);
    		dispose();
        }
    }
    
    private class ButtonBuy implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
        	String insertSql;
        	String valueString1;
        	Object valueObject1;
        	String valueString2 = "";
        	Object valueObject2;
        	double valueInt3;
        	Object valueObject3;
        	double valueInt4;
        	Object valueObject4;
        	String valueString6 = ""; // stan
        	Object valueObject6;
        	
        	double amount = 0;
        	ResultSet invoiceNrRes = null;
    		int invoiceNr = -1;
    		try 
			{
    			invoiceNrRes = stmt.executeQuery("select count(*) as counter from faktury;");
		        while(invoiceNrRes.next())
		        {
		        	invoiceNr = invoiceNrRes.getInt("counter") + 1;       
		        }
			} catch (SQLException f) 
			{
				f.printStackTrace();
			}
    		
    		//newEmptyInvoice //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    		try 
			{
    			stmt.executeUpdate("insert into faktury (indeks, data_sprzedazy, wartosc_netto, wartosc_brutto, wartosc_vat, rodz_dok, id_klienta_dostawcy) "
    					+ "values (" + Integer.toString(invoiceNr) + ", '2000-01-01', 0, 0, 0, 0, 1);");
			} catch (SQLException f) 
			{
				f.printStackTrace();
			}
    		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    		
    		for(int i = 0; i<model.getRowCount(); i++)
        	{
        		valueObject1 = table.getValueAt(i, 0);
        		valueString1 = (String)valueObject1;
        		valueObject2 = table.getValueAt(i, 7);
        		valueString2 = (String)valueObject2;
        		int mnoznikCen;
        		if(valueString2.equals(""))
        			mnoznikCen = 0;        			
        		else
        			mnoznikCen = Integer.parseInt(valueString2);
        		valueObject3 = table.getValueAt(i, 3);
        		valueInt3 = Double.parseDouble((String)valueObject3);
        		valueObject4 = table.getValueAt(i, 4);
        		valueInt4 = Double.parseDouble((String)valueObject4);
        		valueObject6 = table.getValueAt(i, 6);
        		valueString6 = (String)valueObject6;
        		
        		if(!valueString2.equals("") && (!(valueString6.equals("0"))) && Integer.parseInt(valueString2) <= Integer.parseInt(valueString6)  )
        		{
	        		ResultSet nrRes = null;
		    		int nr = -1;
		    		try 
					{
		    			nrRes = stmt.executeQuery("select count(*) as counter from produkty_faktur;");
				        while(nrRes.next())
				        {
				        	nr = nrRes.getInt("counter") + 1;       
				        }
					} catch (SQLException f) 
					{
						f.printStackTrace();
					}
        		
	        		insertSql = "INSERT into produkty_faktur (indeks, indeks_faktury, indeks_egzemplarza, sztuki) values (" + Integer.toString(nr) + "," +  Integer.toString(invoiceNr) + "," + valueString1 +  ", " + valueString2 + ");";
	        		try 
	        		{
						stmt.executeUpdate(insertSql);
					} catch (SQLException e1) 
	        		{
						e1.printStackTrace();
					}
	        		
	        		String updSql = "update egzemplarze set stan = stan - " + valueString2 + " where indeks = " + valueString1 + ";";
	        		try 
	        		{
						stmt.executeUpdate(updSql);
					} catch (SQLException e1) 
	        		{
						e1.printStackTrace();
					}
	        		
	        		amount = amount + valueInt3*mnoznikCen + valueInt4*mnoznikCen; 
        		}
        	}
    	   
    		if(amount > 0)
    		{
//    			ResultSet nrRes = null;
//        		int nr = -1;
//        		try 
//    			{
//        			nrRes = stmt.executeQuery("select count(*) as counter from faktury;");
//    		        while(nrRes.next())
//    		        {
//    		        	nr = nrRes.getInt("counter") + 1;       
//    		        }
//    			} catch (SQLException f) 
//    			{
//    				f.printStackTrace();
//    			}
        		try 
    			{
//        			stmt.executeUpdate("insert into faktury (indeks, data_sprzedazy, wartosc_netto, wartosc_brutto, wartosc_vat, rodz_dok, id_klienta_dostawcy) values (" + Integer.toString(nr) + ", current_date, " + String.valueOf(0.77 * amount) + "," + String.valueOf(amount) + "," + String.valueOf(0.23 * amount) +  ", 1, " + Integer.toString(indeksClient)+ ");");				
//        			stmt.executeUpdate("update faktury set (data_sprzedazy, wartosc_netto, wartosc_brutto, wartosc_vat, rodz_dok, id_klienta_dostawcy) values (current_date, " + String.valueOf(0.77 * amount) + "," + String.valueOf(amount) + "," + String.valueOf(0.23 * amount) +  ", 1, " + Integer.toString(indeksClient)+ ") where indeks = " + Integer.toString(invoiceNr) + ";");	
        			String s = "update faktury set data_sprzedazy = current_date, wartosc_netto = " + String.valueOf(0.77 * amount) + ", wartosc_brutto = " + String.valueOf(amount) + ", wartosc_vat = " + String.valueOf(0.23 * amount) + ", rodz_dok = 1, id_klienta_dostawcy = " + Integer.toString(indeksClient) + "where indeks = " + Integer.toString(invoiceNr) + ";" ;
        			System.out.println(s);
        			stmt.executeUpdate(s);

    			} catch (SQLException f) 
    			{
    				f.printStackTrace();
    			}		
    		}
    		else
    		{
    			try 
    			{
					stmt.executeUpdate("delete from faktury where indeks = " + Integer.toString(invoiceNr) + ";");
				} catch (SQLException e1) 
    			{
					e1.printStackTrace();
				}
    		}
        }
    }
    
    private class ButtonCart implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
        {
    		String insertSql;
        	String valueString1;
        	Object valueObject1;
        	String valueString2;
        	Object valueObject2;
        	for(int i = 0; i<model.getRowCount(); i++)
        	{
        		valueObject1 = table.getValueAt(i, 0);
        		valueString1 = (String)valueObject1;
        		valueObject2 = table.getValueAt(i, 7);
        		valueString2 = (String)valueObject2;
        		String pieces = (String)model.getValueAt(i, 6);
        		
        		if(!valueString2.equals("") && !pieces.equals("0") && Integer.parseInt(valueString2) <= Integer.parseInt(pieces)  )
        		{
	        		ResultSet nrRes = null;
	        		int nr = 1;
	        		try 
	    			{
	        			nrRes = stmt.executeQuery("select * from koszyk;");
	    		        while(nrRes.next())
	    		        {
	    		        	nr = nrRes.getInt("indeks") + 1;       
	    		        }
	    			} catch (SQLException f) 
	    			{
	    				f.printStackTrace();
	    			}
	        		
//	        		nr = nr + 1;
        			insertSql = "INSERT into koszyk (indeks, indeks_egzemplarza, indeks_klienta, sztuki) values (" + Integer.toString(nr) + ", " + valueString1 + ", " + Integer.toString(indeksClient) + ", " + valueString2 + ");";
            		System.out.println(insertSql);
            		try 
            		{
    					stmt.executeUpdate(insertSql);
    				} catch (SQLException e1) 
            		{
    					e1.printStackTrace();
    				}
        		}
        		
        	}
        	dispose();
        }
    }
    
    private class ButtonOrder implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
        	String insertSql;
        	String valueString1;
        	Object valueObject1;
        	String valueString2;
        	Object valueObject2;
        	for(int i = 0; i<model.getRowCount(); i++)
        	{
        		valueObject1 = table.getValueAt(i, 0);
        		valueString1 = (String)valueObject1;
        		valueObject2 = table.getValueAt(i, 7);
        		valueString2 = (String)valueObject2;
        		
        		ResultSet nrRes = null;
        		int nr = -1;
        		try 
    			{
        			nrRes = stmt.executeQuery("select count(*) as counter from zamowienia;");
    		        while(nrRes.next())
    		        {
    		        	nr = nrRes.getInt("counter") + 1;       
    		        }
    			} catch (SQLException f) 
    			{
    				f.printStackTrace();
    			}
        		
        		if(!valueString2.equals(""))
        		{
        			insertSql = "INSERT into zamowienia (indeks, indeks_egzemplarza, data_zlozenia, sztuki) values (" + Integer.toString(nr) + ", " + valueString1 + ", " +  "current_date, " + valueString2 + ");";
            		System.out.println(insertSql);
            		try 
            		{
    					stmt.executeUpdate(insertSql);
    				} catch (SQLException e1) 
            		{
    					e1.printStackTrace();
    				}
        		}
        		
        	}
        }
    }
    
    private class ButtonSave implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
        	String updateSql;
        	String cenaString;
        	Object cenaObject;
        	String updateSql2;
        	String cenaString2;
        	Object cenaObject2;
        	int update;
        	for(int i =0; i<model.getRowCount(); i++)
        	{
        		cenaObject = table.getValueAt(i, 3);
        		cenaString = (String)cenaObject;
        		cenaObject2 = table.getValueAt(i, 4);
        		cenaString2 = (String)cenaObject2;
        		updateSql = "update egzemplarze set cena = " + cenaString + " where indeks = "+ Integer.toString(i+1) + ";";
        		updateSql2 = "update egzemplarze set koszt_dostawy = " + cenaString2 + " where indeks = "+ Integer.toString(i+1) + ";";
        		System.out.println(updateSql);
        		try 
        		{
					update = stmt.executeUpdate(updateSql);
					update = stmt.executeUpdate(updateSql2);
				} catch (SQLException e1) 
        		{
					e1.printStackTrace();
				}
        	}
        	
        	isPriceEditable = false;
        	buttonSave.setVisible(false);
        }
    }

    private class ButtonAdd implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            NewProduct NP = new NewProduct();
            NP.setVisible(true);
        }
    }
 
    private class ButtonClose implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            dispose();
        }
    }
 
    public static void main(String[] args) 
    {

    }
}