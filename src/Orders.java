import java.awt.*;
import java.awt.event.*;
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

public class Orders extends JFrame 
{
   Container mainPanel;
   Connection conn;
   Statement stmt;
   int indeks;
   String sql;
   static DefaultTableModel model;
   JTable table;
   
   public Orders() 
   {
        setTitle("Zamowienia");
        sql = "SELECT * from zamowienia;";
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
			  System.out.println("Nie poszlo dobrze z baza w Orders");
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
    	String[] colHeadings = {"NUMER","INDEKS_PRODUKTU", "DATA_ZLOZENIA", "SZTUKI"};
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
  
        for (int i=0; i<4; i++) 
        {
        	  TableColumn column = table.getColumnModel().getColumn(i);
        	  if (i==0) column.setPreferredWidth(30);
        	  if (i==1) column.setPreferredWidth(100);
        	  if (i==2) column.setPreferredWidth(100);
        	  if (i==3) column.setPreferredWidth(100);
        }
       
        ResultSet res = null;
		try 
		{
			res = stmt.executeQuery(sql);
	        while(res.next())
	        {
	        	int indeks = res.getInt("indeks");
	        	int indeks_egz = res.getInt("indeks_egzemplarza");
	            Date data_zlozenia = res.getDate("data_zlozenia");
	            int pieces = res.getInt("sztuki");
	            
				Format formatter = new SimpleDateFormat("yyyy-MM-dd");
				String stringDate = formatter.format(data_zlozenia);

	            String[] newRow = { Integer.toString(indeks), Integer.toString(indeks_egz), stringDate, Integer.toString(pieces)};
	            model.addRow(newRow);
	            
	            table.setEnabled(false);
	        }
		} catch (SQLException e) 
		{
			e.printStackTrace();
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