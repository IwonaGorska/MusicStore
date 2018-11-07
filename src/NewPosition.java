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
 
class NewPosition extends JFrame 
{
	private static final int MIN_VAL = 1;
    private static final int MAX_VAL = 100;
	
    JPanel panel = new JPanel();
    Connection conn;
    Statement stmt;
    JFormattedTextField number;
	private JSpinner durationField;
	private JTextField studioField;
	private JTextField comeOutField;
	private JTextField genreField;
	private JTextField artistField;
	private JTextField titleField;
   
	
   public NewPosition() 
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
          JLabel  artist = new JLabel("Wykonawca: ");
          JLabel  genre= new JLabel("Gatunek: ");
          JLabel  comeOut= new JLabel("Data_premiery: ");
          JLabel  studio= new JLabel("Wydawnictwo: ");
          JLabel  duration = new JLabel("Dlugosc_w_minutach: ");
          
          titleField = new JTextField(6);
          artistField = new JTextField(6);
          genreField = new JTextField(6);
          comeOutField = new JTextField(6);
          studioField = new JTextField(6);
          durationField = new JSpinner();
          durationField.setModel(new javax.swing.SpinnerNumberModel(MIN_VAL, MIN_VAL, MAX_VAL, 1));
          durationField.setEnabled(false);
          number = ((JSpinner.DefaultEditor)durationField.getEditor()).getTextField();
          number.setEditable(false);

          title.setSize(200,30);
          artist.setSize(200,30);
          genre.setSize(200,30);
          comeOut.setSize(200,30);
          studio.setSize(200,30);
          duration.setSize(200, 30);
          
          titleField.setSize(300,30);
          artistField.setSize(300,30);
          genreField.setSize(300,30);
          comeOutField.setSize(300,30);
          studioField.setSize(300,30);
          durationField.setSize(300,30);
          
          closeButton.setLocation(getWidth()-190,getHeight()-90);
          title.setLocation(getWidth()-550,getHeight()-330);
          artist.setLocation(getWidth()-550,getHeight()-290);
          genre.setLocation(getWidth()-550,getHeight()-250);
          comeOut.setLocation(getWidth()-550,getHeight()-210);
          studio.setLocation(getWidth()-550,getHeight()-170);
          duration.setLocation(getWidth()-550,getHeight()-130);
          
          titleField.setLocation(getWidth()-400,getHeight()-330);
          artistField.setLocation(getWidth()-400,getHeight()-290);
          genreField.setLocation(getWidth()-400,getHeight()-250);
          comeOutField.setLocation(getWidth()-400,getHeight()-210);
          studioField.setLocation(getWidth()-400,getHeight()-170);
          durationField.setLocation(getWidth()-400,getHeight()-130);
          
          panel.setLayout(null);
          panel.add(closeButton);
          panel.add(title);
          panel.add(artist);
          panel.add(genre);
          panel.add(comeOut);
          panel.add(studio);
          panel.add(duration);
          panel.add(titleField);
          panel.add(artistField);
          panel.add(genreField);
          panel.add(comeOutField);
          panel.add(studioField);
          panel.add(durationField);
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

            if(titleField.getText().isEmpty() == true || artistField.getText().isEmpty() == true || genreField.getText().isEmpty() == true
            || comeOutField.getText().isEmpty() == true || studioField.getText().isEmpty() == true)        
    		{
    			JOptionPane.showMessageDialog(panel, "Uzupe³nij wszystkie wymagane pola!", "B³¹d!", JOptionPane.ERROR_MESSAGE);
    		}    
			else
			{
				ResultSet nrRes = null;
	    		int nr = -1;
	    		try 
				{
	    			nrRes = stmt.executeQuery("select count(*) as counter from plyty;");
			        while(nrRes.next())
			        {
			        	nr = nrRes.getInt("counter") + 1;       
			        }
				} catch (SQLException f) 
				{
					f.printStackTrace();
				}
				
				
				String sqlInsert = "INSERT into plyty (indeks, tytul, wykonawca, gatunek, data_premiery, wydawnictwo, dlugosc_w_minutach) values (" + Integer.toString(nr) + ", " +  titleField.getText() + ", " + artistField.getText() + ", " + genreField.getText() + " , " + comeOutField.getText() + " , " +  studioField.getText() + " , " + number.getText() +  ");";
				try 
				{
					int insertInt = stmt.executeUpdate(sqlInsert);
				} catch (SQLException e1) 
				{
					e1.printStackTrace();
				}	
				dispose();

	        	String[] newRow = {Integer.toString(nr), titleField.getText(), artistField.getText(), genreField.getText(), comeOutField.getText(),studioField.getText(), number.getText() };
	        	Products.model.addRow(newRow);
			}
        }
    }
 
    public static void main(String[] args) 
    {

    }
}