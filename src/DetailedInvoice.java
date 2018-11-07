import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Format;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class DetailedInvoice extends JFrame 
{
   Container mainPanel;
   Connection conn;
   Statement stmt;
   int indeks;
   String sql;
   DefaultTableModel model;
   JTable table;
   
   public DetailedInvoice(String sqlStatement) 
   {
        setTitle("Produkty na wybranej fakturze");
        sql = sqlStatement;
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
			  System.out.println("Nie poszlo dobrze z baza w DetailedInvoice");
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
    	String[] colHeadings = {"INDEKS","PLYTA", "NOSNIK", "RODZAJ", "STAN", "CENA", "KOSZT_DOSTAWY",  "SZTUK"};
        int numRows = 0;
        model = new DefaultTableModel(numRows, colHeadings.length) ;
        model.setColumnIdentifiers(colHeadings);       
        
        table = new JTable(model);
        table.setEnabled(false);
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
			//WPROWADZAM DO TABELI DANE DOSTEPNE Z TEGO RESULTSETA, Z RELACJI PRODUKTY_FAKTUR MAM TYLE INFORMACJI
			res = stmt.executeQuery(sql);
	        while(res.next())
	        {
	        	int indeks_egzemplarza = res.getInt("indeks_egzemplarza");
	        	int sztuki = res.getInt("sztuki");
	            String[] newRow = { Integer.toString(indeks_egzemplarza), "", "", "", "", "", "", Integer.toString(sztuki)};
	            model.addRow(newRow);            
	        }
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}

		//DOKLADAM DO TABELI INFORMACJE BARZIEJ CZYTELNE DLA UZYTKOWNIKA, DANE Z RELACJI EGZEMPLARZE 
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
		
		//UZUPELNIAM KOLUMNE 'PLYTA' TABELI O TYTUL WZIETY Z RELACJI PLYTY
		String titleAlbum = "";
		ResultSet resTitle = null;
		String id2 = "";
		for(int i =0; i<model.getRowCount(); i++)
    	{
	        try 
			{
	        	id2 = (String)table.getValueAt(i, 1);
	        	System.out.println(id);
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