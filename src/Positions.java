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


public class Positions extends JFrame 
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
    
    
	public Positions(String sqlStatement, boolean addE)
	{
		super("P³yty");
		sql = sqlStatement;
		addEnabled = addE; // oznacza, ze oglada to pracownik - moze dodawac, nie moze checkboxow zaznaczac,, sa not enable
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
			  System.out.println("Nie poszlo dobrze z baza w Positions");
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
    	String[] colHeadings = {"NUMER","TYTUL", "WYKONAWCA", "GATUNEK", "DATA_PREMIERY", "WYDAWNICTWO", "DLUGOSC_W_MINUTACH"};
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
        
        table.setRowHeight(30);
  
        for (int i=0; i<7; i++) 
        {
        	  TableColumn column = table.getColumnModel().getColumn(i);
        	  if (i==0) column.setPreferredWidth(20);
        	  if (i==1) column.setPreferredWidth(90);
        	  if (i==2) column.setPreferredWidth(90);
        	  if (i==3) column.setPreferredWidth(90);
        	  if (i==4) column.setPreferredWidth(90);
        	  if (i==5) column.setPreferredWidth(90);
        	  if (i==6) column.setPreferredWidth(90);
        	}
 
        ResultSet res = null;
		try 
		{
			res = stmt.executeQuery(sql);
	        while(res.next())
	        {
	        	int indeks = res.getInt("indeks");
	        	String title = res.getString("tytul");
	        	String artist = res.getString("wykonawca");
	        	String genre = res.getString("gatunek");
	        	Date comeOut = res.getDate("data_premiery");
	        	String studio = res.getString("wydawnictwo");
	        	int duration = res.getInt("dlugosc_w_minutach");
	        	
	        	Format formatter = new SimpleDateFormat("yyyy-MM-dd");
				String stringDate = formatter.format(comeOut);
	        	
	            String[] newRow = { Integer.toString(indeks), title, artist, genre, stringDate, studio, Integer.toString(duration)};
	            model.addRow(newRow);            
	        }
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
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
        buttonAdd.setToolTipText("Dodaj now¹ pozycjê.");
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
            NewPosition NP = new NewPosition();
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