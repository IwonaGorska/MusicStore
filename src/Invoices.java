import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.EventObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class Invoices extends JFrame 
{
   Container mainPanel;
   Connection conn;
   Statement stmt;
   int indeks;
   boolean addEnabled;
   String sql;
   static DefaultTableModel model;
   static JTable table;
	
   public Invoices(int id) 
   {
        setTitle("Moje faktury");       
        indeks = id;
        addEnabled = false;        
        sql = "SELECT * from faktury where id_klienta_dostawcy = " + Integer.toString(indeks) + " order by indeks;";
        System.out.println(sql);
        initComponents();
   }
   
   public Invoices() 
   {
        setTitle("Faktury");
        addEnabled = true;
        sql = "SELECT * from faktury order by indeks;";
        System.out.println(sql);
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
			  System.out.println("Nie poszlo dobrze z baza w Invoices");
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
    	String[] colHeadings = {"NUMER","DATA SPRZEDAZY", "WARTOSC_NETTO", "WARTOSC_BRUTTO", "WARTOSC_VAT", "RODZAJ_DOKUMENTU", "ID_KLIENTA/DOSTAWCY", "WIÊCEJ"};
        int numRows = 0;
        model = new DefaultTableModel(numRows, colHeadings.length) ;
        model.setColumnIdentifiers(colHeadings);
        //USTAWIAM KOLUMNE OSTATNIA JAKO EDYTOWALNA - W NIEJ SA BUTTONY I BEZ TEGO NIE DZIALALOBY KLIKANIE W NIE
        table = new JTable(model) 
        {
        	public boolean isCellEditable(int row,int column)
		    {
		    	switch(column)
		    	{
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
        
//        setRowHeight(int row, int rowHeight); // ustawia indywidualnie wysokosc dla wiersza o indeksie row
        table.setRowHeight(30);
  
        for (int i=0; i<8; i++) 
        {
        	  TableColumn column = table.getColumnModel().getColumn(i);
        	  if (i==0) column.setPreferredWidth(30);
        	  if (i==1) column.setPreferredWidth(100);
        	  if (i==2) column.setPreferredWidth(100);
        	  if (i==3) column.setPreferredWidth(100);
        	  if (i==4) column.setPreferredWidth(100);
        	  if (i==5) column.setPreferredWidth(100);
        	  if (i==6) column.setPreferredWidth(100);
        	  if (i==7) column.setPreferredWidth(100);
        	}
       
        ResultSet res = null;
		try 
		{
			res = stmt.executeQuery(sql);
	        while(res.next())
	        {
	        	int indeks = res.getInt("indeks");
	            Date data_sprzedazy = res.getDate("data_sprzedazy");
	            BigDecimal wartosc_netto = res.getBigDecimal("wartosc_netto");
	            BigDecimal wartosc_brutto = res.getBigDecimal("wartosc_brutto");
	            BigDecimal wartosc_vat = res.getBigDecimal("wartosc_vat");
	            short rodz_dok = res.getShort("rodz_dok");
	            int id_klienta = res.getInt("id_klienta_dostawcy");
	            
				Format formatter = new SimpleDateFormat("yyyy-MM-dd");
				String stringDate = formatter.format(data_sprzedazy);
				int w_n = Integer.valueOf(wartosc_netto.intValue());
				int w_b = Integer.valueOf(wartosc_brutto.intValue());
				int w_v = Integer.valueOf(wartosc_vat.intValue());
				int r_d = (int)rodz_dok;

	            String[] newRow = { Integer.toString(indeks), stringDate, Integer.toString(w_n), Integer.toString(w_b), Integer.toString(w_v), Integer.toString(r_d), Integer.toString(id_klienta), "Zobacz produkty"};
	            model.addRow(newRow);
	        }
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		table.getColumn("WIÊCEJ").setCellRenderer(new ButtonRenderer());
	    table.getColumn("WIÊCEJ").setCellEditor(new ButtonEditor(new JCheckBox()));
   
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
        
        JButton buttonAdd = new JButton ("Dodaj");
        buttonAdd.setToolTipText("Dodaj now¹ fakturê.");
        setPrefferedMaxAndMinSize(buttonAdd, 90, 30);
        panel.add(buttonAdd);
        buttonAdd.addActionListener(new ButtonAdd());
        panel.add(Box.createHorizontalStrut(30));
        if(!addEnabled)
        	buttonAdd.setVisible(false);
    }

    private class ButtonAdd implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            NewInvoice I = new NewInvoice();
            I.setVisible(true);
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