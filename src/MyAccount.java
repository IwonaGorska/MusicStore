import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
 
public class MyAccount extends JFrame 
{
   JPanel panel = new JPanel();
   JButton  saveButton;
   JButton  editButton;
   JTextField login, name, surname, mail;
   JPasswordField password;
   JPasswordField passwordRepeat;
   JLabel  passwordLabelRepeat;
   Connection conn;
   Statement stmt;
   int indeks;
	
   public MyAccount(int id) 
   {
        setTitle("Moje konto");
        setSize(400,350);
        setResizable(false);
        setLocation(500,150);
        indeks = id;
        initComponents();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);       
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
			  System.out.println("Nie poszlo dobrze z baza w GreetWindow");
			e1.printStackTrace();
		  }
		  
		  String sql = "";
          JButton  closeButton = new JButton ("Zamknij");
          saveButton = new JButton ("Zapisz");
          saveButton.setVisible(false);
          editButton = new JButton ("Edytuj");
          JLabel  loginLabel= new JLabel("Login: ");
          JLabel  passwordLabel = new JLabel("Has³o: ");
          passwordLabelRepeat = new JLabel("Powtórz has³o: ");
          passwordLabelRepeat.setVisible(false);
          JLabel  nameLabel= new JLabel("Imie: ");
          JLabel  surnameLabel = new JLabel("Nazwisko: ");
          JLabel  mailLabel = new JLabel("E-mail: ");
          login = new JTextField(6);
          sql = "SELECT login from klienci where indeks = " +  Integer.toString(indeks) + ";";
          System.out.println(sql);
          login.setText(readFromDB(sql));
          login.setEditable(false);
          password = new JPasswordField(6);
          sql = "SELECT haslo from klienci where indeks = " +  Integer.toString(indeks) + ";";
          password.setText(readFromDB(sql));
          password.setEditable(false);
          name = new JTextField(6);
          sql = "SELECT imie from klienci where indeks = " +  Integer.toString(indeks) + ";";
          name.setText(readFromDB(sql));
          name.setEditable(false);
          surname = new JTextField(6);
          sql = "SELECT nazwisko from klienci where indeks = " +  Integer.toString(indeks) + ";";
          surname.setText(readFromDB(sql));
          surname.setEditable(false);
          mail = new JTextField(6);
          sql = "SELECT email from klienci where indeks = " +  Integer.toString(indeks) + ";";
          mail.setText(readFromDB(sql));
          mail.setEditable(false);
          passwordRepeat = new JPasswordField(6);
          passwordRepeat.setVisible(false);
          closeButton.setSize(90, 30);
          saveButton.setSize(90, 30);
          editButton.setSize(90, 30);
          login.setSize(100,30);
          password.setSize(100,30);
          name.setSize(100,30);
          surname.setSize(100,30);
          mail.setSize(100,30);
          passwordRepeat.setSize(100,30);
          loginLabel.setSize(100,30);
          passwordLabel.setSize(100,30);
          passwordLabelRepeat.setSize(100,30);
          nameLabel.setSize(100,30);
          surnameLabel.setSize(100,30);
          mailLabel.setSize(100,30);
          closeButton.setLocation(getWidth()-100,getHeight()-90);
          saveButton.setLocation(getWidth()-220,getHeight()-90);
          editButton.setLocation(getWidth()-340,getHeight()-90);
          loginLabel.setLocation(getWidth()-340,getHeight()-330);
          passwordLabel.setLocation(getWidth()-340,getHeight()-290);
          passwordLabelRepeat.setLocation(getWidth()-340,getHeight()-250);
          nameLabel.setLocation(getWidth()-340,getHeight()-210);
          surnameLabel.setLocation(getWidth()-340,getHeight()-170);
          mailLabel.setLocation(getWidth()-340,getHeight()-130);
          login.setLocation(getWidth()-250,getHeight()-330);
          password.setLocation(getWidth()-250,getHeight()-290);
          passwordRepeat.setLocation(getWidth()-250,getHeight()-250);
          name.setLocation(getWidth()-250,getHeight()-210);
          surname.setLocation(getWidth()-250,getHeight()-170);
          mail.setLocation(getWidth()-250,getHeight()-130);
          panel.setLayout(null);
          panel.add(closeButton);
          panel.add(saveButton);
          panel.add(editButton);
          panel.add(login);
          panel.add(password);
          panel.add(passwordRepeat);
          panel.add(name);
          panel.add(surname);
          panel.add(mail);
          panel.add(loginLabel);
          panel.add(passwordLabel);
          panel.add(passwordLabelRepeat);
          panel.add(nameLabel);
          panel.add(surnameLabel);
          panel.add(mailLabel);
          closeButton.setToolTipText("Zamknij okno.");
          saveButton.setToolTipText("Zapisz nowe dane.");
          editButton.setToolTipText("Edytuj swoje dane.");
          login.setToolTipText("Podaj swój login.");
          password.setToolTipText("Podaj swoje has³o.");
          passwordRepeat.setToolTipText("Powtórz swoje has³o.");
          name.setToolTipText("Podaj swoje imiê.");
          surname.setToolTipText("Podaj swoje nazwisko.");
          mail.setToolTipText("Podaj swój adres e-mail.");
          this.getContentPane().add(panel);
          closeButton.addActionListener(new ButtonClose());
          saveButton.addActionListener(new ButtonSave());
          editButton.addActionListener(new ButtonEdit());
          saveButton.setForeground(new Color(9,210,19));
          closeButton.setForeground(new Color(253,4,21));
    }
   
    private String readFromDB(String sql)
    {
    	String data = "";
    	ResultSet rs = null;
		try 
		{
			rs = stmt.executeQuery(sql);
		} catch (SQLException e1) 
		{
			e1.printStackTrace();
		}
		
		try 
		{
			while(rs.next())
			{
				data = rs.getString(1);
			}
		} catch (SQLException e1) 
		{
			e1.printStackTrace();
		}
        return data;
    }
    
    private class ButtonEdit implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
        	saveButton.setVisible(true);
        	editButton.setEnabled(false);
        	passwordLabelRepeat.setVisible(true);
        	passwordRepeat.setVisible(true);
        	login.setEditable(true);
        	password.setEditable(true);
        	name.setEditable(true);
        	surname.setEditable(true);
        	mail.setEditable(true);
        }
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
            String userString = login.getText();
            String passString = password.getText();
            String passRepeatString = passwordRepeat.getText();
            String nameString = name.getText();
            String surnameString = surname.getText();
            String mailString = mail.getText();
            
            boolean isAt = false;
            for(int i = 0; i<mailString.length(); i++)
            {
            	if(mailString.charAt(i) == '@')
            		isAt = true;
            }
            
            if(  (isAt == false || login.getText().isEmpty() == true) ||  (password.getText().isEmpty() == true) ||  (passwordRepeat.getText().isEmpty() == true) 
            ||  (name.getText().isEmpty() == true)	||  (surname.getText().isEmpty() == true) ||  (mail.getText().isEmpty() == true)	)
    		{
    			JOptionPane.showMessageDialog(panel, "Zostawiles puste pola lub adres email jest bledny!", "B³¹d!", JOptionPane.ERROR_MESSAGE);
    		}
            
            else
            {
            	if(!password.getText().equals(passwordRepeat.getText()))
            	{
            		JOptionPane.showMessageDialog(panel, "Powtorzone haslo rozni sie od wpisanego jako pierwsze!", "B³¹d!", JOptionPane.ERROR_MESSAGE);
            	}
            	else
        		{
//    				String sqlUpdate = "UPDATE klienci SET login='" + userString + "', haslo='" + passString + "', imie='" + nameString + "', nazwisko='" + surnameString + "', email='" + mailString + "' WHERE indeks=" + Integer.toString(indeks) + ";";
//    				try 
//    				{
//    					stmt.executeUpdate(sqlUpdate);
//    					System.out.println(sqlUpdate);
//    				} catch (SQLException e1) 
//    				{
//    					e1.printStackTrace();
//    				}
    				
    				
    				try
    	    		{
    					PreparedStatement stmtPre = conn.prepareStatement("UPDATE klienci SET login=?, haslo=?, imie=?, nazwisko=?, email=? WHERE indeks=" + Integer.toString(indeks) + ";");
    					stmtPre.setString(1, userString);
    					stmtPre.setString(2, passString);
    					stmtPre.setString(3, nameString);
    					stmtPre.setString(4, surnameString);
    					stmtPre.setString(5, mailString);
    					stmtPre.executeUpdate();
    	    		}
    	    		catch (SQLException e1)
    	    		{
    	    			e1.printStackTrace();
    	    		}
    				
    				
    				
    				
    				dispose();
        		} 
            }
            
    		 
        }
    }
 
    public static void main(String[] args) 
    {

    }
}