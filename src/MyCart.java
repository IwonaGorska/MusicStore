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

public class MyCart extends JFrame 
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
    
    
	public MyCart(int indC)
	{
		super("Mój koszyk");
		indeksClient = indC;
		sql = "select * from koszyk where indeks_klienta = " + indeksClient + ";";		
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
			  System.out.println("Nie poszlo dobrze z baza w MyCart");
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
    	String[] colHeadings = {"IND_EGZEMPLARZA", "P£YTA", "NOSNIK", "RODZAJ", "STAN", "CENA", "KOSZT_DOSTAWY",  "SZTUK", "¯¥DANA_LICZBA"};
        int numRows = 0;
        model = new DefaultTableModel(numRows, colHeadings.length) ;
        model.setColumnIdentifiers(colHeadings);
        //USTAWIAM KOLUMNE OSTATNIA JAKO EDYTOWALNA, ZEBY UZYTKOWNIK MOGL WPROWADZAC ZADANA LICZBE I PO NACISNIECIU ENTER DOPIERO PROGRAM JA WIDZI
        table = new JTable(model) 
        {
        	public boolean isCellEditable(int row,int column)
		    {
		    	switch(column)
		    	{
		        case 8:
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
  
        for (int i=0; i<9; i++) 
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
        	  if (i==8) column.setPreferredWidth(90);
        	}
        
        ResultSet res = null;
		try 
		{
			//DODAJE DO TABELI INFORMACJE ZACZERPNIETE Z RELACJI PRODUKTY_FAKTUR
			res = stmt.executeQuery(sql);
	        while(res.next())
	        {
	        	int indeks_egzemplarza = res.getInt("indeks_egzemplarza");
	        	int sztuki = res.getInt("sztuki");
	            String[] newRow = { Integer.toString(indeks_egzemplarza), "", "", "", "", "", "", Integer.toString(sztuki), ""};
	            model.addRow(newRow);            
	        }
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		//DODAJE DO TABELI INFORMACJE ZACZERPNIETE Z RELACJI EGZEMPLARZE
        ResultSet res2 = null;
        String id = "";
		for(int i =0; i<model.getRowCount(); i++)
		{
			try 
			{
				res2 = stmt.executeQuery("select * from egzemplarze where indeks = " + id + ";");
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
					
					table.setValueAt(Integer.toString(indeksAlbum), i, 1);
					table.setValueAt(n, i, 2);
					table.setValueAt(r, i, 3);
					table.setValueAt(Integer.toString(stan), i, 4);
					table.setValueAt(Integer.toString(c), i, 5);
					table.setValueAt(Integer.toString(k_d), i, 6);		                       
		        }
			} catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		
		//UZUPELNIAM KOLUMNE 'PLYTA' TABELI O INFORMACJE Z RELACJI PLYTY 
		String titleAlbum = "";
		ResultSet resTitle = null;
		String id2 = "";
		for(int i =0; i<model.getRowCount(); i++)
    	{
	        try 
			{
	        	id2 = (String)table.getValueAt(i, 1);
	        	resTitle = stmt.executeQuery("select tytul from plyty where indeks = " + id2 + ";");
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

        JButton buttonClose = new JButton ("Zamknij");
        buttonClose.setToolTipText("Zamknij okno.");
        setPrefferedMaxAndMinSize(buttonClose, 90, 30);
        panel.add(buttonClose);
        panel.add(Box.createHorizontalStrut(30));
        buttonClose.addActionListener(new ButtonClose());
        
        panel.add(Box.createHorizontalStrut(30));
        
        JButton buttonBuy = new JButton ("Kup");
        setPrefferedMaxAndMinSize(buttonBuy, 110, 30);
        panel.add(buttonBuy);
        buttonBuy.addActionListener(new ButtonBuy());
        panel.add(Box.createHorizontalStrut(30));
        
        JButton buttonDelete = new JButton ("Usun");
        setPrefferedMaxAndMinSize(buttonDelete, 110, 30);
        panel.add(buttonDelete);
        buttonDelete.addActionListener(new ButtonDelete());
        panel.add(Box.createHorizontalStrut(30));
        	
    }
    
    private class ButtonDelete implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
        {
        	String insertSql;
        	String valueString1;
        	Object valueObject1;
        	String valueString2;
        	Object valueObject2;
        	double valueInt3;
        	Object valueObject3;
        	double valueInt4;
        	Object valueObject4;
    		
    		for(int i = 0; i<model.getRowCount(); i++)
        	{
        		valueObject1 = table.getValueAt(i, 0);
        		valueString1 = (String)valueObject1;
        		valueObject2 = table.getValueAt(i, 7);
        		valueString2 = (String)valueObject2;
        		valueObject3 = table.getValueAt(i, 3);
        		valueInt3 = Double.parseDouble((String)valueObject3);
        		valueObject4 = table.getValueAt(i, 4);
        		valueInt4 = Double.parseDouble((String)valueObject4);
        		String updSql = "update koszyk set sztuki = sztuki - " + valueString2 + " where indeks = " + valueString1 + ";";
        		try 
        		{
					stmt.executeUpdate(updSql);
				} catch (SQLException e1) 
        		{
					e1.printStackTrace();
				}
        		String deleteSql = "delete from koszyk where sztuki = 0;";
        		try 
        		{
					stmt.executeUpdate(deleteSql);
				} catch (SQLException e1) 
        		{
					e1.printStackTrace();
				}
        		dispose();
        	}
        }
    }

    private class ButtonClose implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
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
        	String valueString2;
        	Object valueObject2;
        	double valueInt3;
        	Object valueObject3;
        	double valueInt4;
        	Object valueObject4;
        	
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
    		
    		for(int i = 0; i<model.getRowCount(); i++)
        	{
        		valueObject1 = table.getValueAt(i, 0);
        		valueString1 = (String)valueObject1;
        		valueObject2 = table.getValueAt(i, 7);
        		valueString2 = (String)valueObject2;
        		valueObject3 = table.getValueAt(i, 3);
        		valueInt3 = Double.parseDouble((String)valueObject3);
        		valueObject4 = table.getValueAt(i, 4);
        		valueInt4 = Double.parseDouble((String)valueObject4);
        		
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
        		
        		amount = amount + valueInt3 + valueInt4;         		
        	}
    	   
    		
    		ResultSet nrRes = null;
    		int nr = -1;
    		try 
			{
    			nrRes = stmt.executeQuery("select count(*) as counter from faktury;");
		        while(nrRes.next())
		        {
		        	nr = nrRes.getInt("counter") + 1;       
		        }
			} catch (SQLException f) 
			{
				f.printStackTrace();
			}
    		try 
			{
    			stmt.executeUpdate("insert into faktury (indeks, data_sprzedazy, wartosc_netto, wartosc_brutto, wartosc_vat, rodz_dok, id_klienta_dostawcy) values (" + Integer.toString(nr) + ", current_date, " + String.valueOf(0.77 * amount) + "," + String.valueOf(amount) + "," + String.valueOf(0.23 * amount) +  ", 1, " + Integer.toString(indeksClient)+ ");");				
			} catch (SQLException f) 
			{
				f.printStackTrace();
			}
        	
        }
    }
    
    
    public static void main(String[] args) 
    {

    }
    
}
