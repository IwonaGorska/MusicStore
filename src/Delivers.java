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


public class Delivers extends JFrame 
{ 
   Container mainPanel;
   Connection conn;
   Statement stmt;
   int indeks;
   String sql;
   static DefaultTableModel model;
   JTable table;
   JButton buttonSave;
    
    
	public Delivers()
	{
		super("Dostawcy");
		sql = "select * from dostawcy;";
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
			  System.out.println("Nie poszlo dobrze z baza w Delivers");
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
    	String[] colHeadings = {"NUMER","NAZWA", "NIP", "NR_TELEFONU"};
        int numRows = 0;
        model = new DefaultTableModel(numRows, colHeadings.length) ;
        model.setColumnIdentifiers(colHeadings);
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        tablePanel.add(scroll);
        
        tablePanel.add(table, BorderLayout.CENTER);
        tablePanel.add(table.getTableHeader(), BorderLayout.NORTH);
        panel.add(tablePanel);

        setPrefferedMaxAndMinSize(table, 400, 400);
        
//        setRowHeight(int row, int rowHeight); // ustawia indywidualnie wysokosc dla wiersza o indeksie row
        table.setRowHeight(30);
  
        for (int i=0; i<4; i++) 
        {
        	  TableColumn column = table.getColumnModel().getColumn(i);
        	  if (i==0) column.setPreferredWidth(20);
        	  if (i==1) column.setPreferredWidth(90);
        	  if (i==2) column.setPreferredWidth(90);
        	  if (i==3) column.setPreferredWidth(90);
        	}
 
        ResultSet res = null;
		try 
		{
			res = stmt.executeQuery(sql);
	        while(res.next())
	        {
	        	int indeks = res.getInt("indeks");
	        	String nazwa = res.getString("nazwa");
	        	String nip = res.getString("nip");
	        	String nr_telefonu = res.getString("nr_telefonu");
	        	
	            String[] newRow = { Integer.toString(indeks), nazwa, nip, nr_telefonu};
	            model.addRow(newRow);            
	        }
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		// CALA TABELA BEDZIE NIEINTERAKTYWNA
		table.setEnabled(false);
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
        buttonAdd.setToolTipText("Dodaj nowego dostawcê.");
        setPrefferedMaxAndMinSize(buttonAdd, 90, 30);
        panel.add(buttonAdd);
        buttonAdd.addActionListener(new ButtonAdd());
        panel.add(Box.createHorizontalStrut(30));
    }
    
 
    private class ButtonAdd implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            NewDeliver ND = new NewDeliver();
            ND.setVisible(true);
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