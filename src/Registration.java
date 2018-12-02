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
 
public class Registration extends JFrame 
{
   JPanel panel = new JPanel();
   JTextField login, name, surname, mail;
   JPasswordField password;
   JPasswordField passwordRepeat;
   Connection conn;
   Statement stmt;
	
   public Registration() 
   {
        setTitle("Registration");
        setSize(400,350);
        setResizable(false);
        setLocationRelativeTo(null);
        initComponents();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(340,150);
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
		  
          JButton  closeButton = new JButton ("Zamknij");
          JLabel  loginLabel= new JLabel("Login: ");
          JLabel  passwordLabel = new JLabel("Has³o: ");
          JLabel  passwordLabelRepeat = new JLabel("Powtórz has³o: ");
          JLabel  nameLabel= new JLabel("Imie: ");
          JLabel  surnameLabel = new JLabel("Nazwisko: ");
          JLabel  mailLabel = new JLabel("E-mail: ");
          login = new JTextField(6);
          password = new JPasswordField(6);
          name = new JTextField(6);
          surname = new JTextField(6);
          mail = new JTextField(6);
          passwordRepeat = new JPasswordField(6);
          closeButton.setSize(90, 30);
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
          closeButton.setLocation(getWidth()-190,getHeight()-90);
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
          login.setToolTipText("Podaj swój login.");
          password.setToolTipText("Podaj swoje has³o.");
          passwordRepeat.setToolTipText("Powtórz swoje has³o.");
          name.setToolTipText("Podaj swoje imiê.");
          surname.setToolTipText("Podaj swoje nazwisko.");
          mail.setToolTipText("Podaj swój adres e-mail.");
          this.getContentPane().add(panel);
          closeButton.addActionListener(new ButtonClose());
          JButton  registrationButton = new JButton ("Zarejestruj");
          registrationButton.setLocation(getWidth()-310,getHeight()-90);
          registrationButton.setSize(90, 30);
          registrationButton.addActionListener(new ButtonReg());

          registrationButton.setToolTipText("Zarejestruj siê.");
          panel.add(registrationButton);
          closeButton.setForeground(new Color(253,4,21));
          registrationButton.setForeground(new Color(248,103,6));
    }
 
    private class ButtonClose implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            System.exit(0);
        }
    }
 
    private class ButtonReg implements ActionListener
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
    			int counter = 0; //inicjalizacja dla picu zeby comilator dal spokoj	        	
				try 
				{
//					ResultSet rs1 = stmt.executeQuery("SELECT * from klienci where login like '" + userString + "' and haslo like '" + passString + "';");
					PreparedStatement stmt = conn.prepareStatement("SELECT * from klienci where login like ? and haslo like ?;");
					stmt.setString(1, userString);
					stmt.setString(2, passString);
					ResultSet rs1 = stmt.executeQuery();
					
					while(rs1.next())
	   			 	{
	   			 		counter++;
	   			 	}
					
				} catch (SQLException e1) 
				{
					e1.printStackTrace();
				}
				if(counter != 0)
				{
					JOptionPane.showMessageDialog(panel, "Uzytkownik o podanym loginie i haœle juz istnieje!", "B³¹d!", JOptionPane.ERROR_MESSAGE);
					login.setText("");
					password.setText("");
					passwordRepeat.setText("");
					name.setText("");
					surname.setText("");
					mail.setText("");
				} else
					try 
					{
						ResultSet nrRes = null;
			    		int nr = -1;
			    		try 
						{
			    			nrRes = stmt.executeQuery("select count(*) as counter from klienci;");
					        while(nrRes.next())
					        {
					        	nr = nrRes.getInt("counter") + 1;       
					        }
						} catch (SQLException f) 
						{
							f.printStackTrace();
						}
						
						
//						String sqlInsert = "INSERT into klienci (indeks, login, haslo, imie, nazwisko, email) values (" + Integer.toString(nr) + ", '" + userString + "', '" + passString + "', '" + nameString + "', '" + surnameString + "' , '" + mailString + "');";
//						int insertInt = stmt.executeUpdate(sqlInsert);
						
						
						PreparedStatement stmtPre = conn.prepareStatement("INSERT into klienci (indeks, login, haslo, imie, nazwisko, email) values (" + Integer.toString(nr) + ", ?, ?, ?, ? , ?);");
    					stmtPre.setString(1, userString);
    					stmtPre.setString(2, passString);
    					stmtPre.setString(3, nameString);
    					stmtPre.setString(4, surnameString);
    					stmtPre.setString(5, mailString);
    					stmtPre.executeUpdate();
						
						
						
						int indeks = -1;
                		ResultSet rsIndeks = null;
        				try 
        				{
//							rsIndeks = stmt.executeQuery("SELECT indeks from klienci where login like '" + userString + "' and haslo like '" + passString + "';");
						
							PreparedStatement stmt = conn.prepareStatement("SELECT * from klienci where login like ? and haslo like ?;");
							stmt.setString(1, userString);
							stmt.setString(2, passString);
							rsIndeks = stmt.executeQuery();
						
							
        				} catch (SQLException e1) 
        				{
							e1.printStackTrace();
						}
        				
    					try 
    					{
							while(rsIndeks.next())
							{
								indeks = rsIndeks.getInt(1);
							}
						} catch (SQLException e1) 
    					{
							e1.printStackTrace();
						}
        				
        				ClientPanel C = new ClientPanel(indeks);
        				
        				dispose();
					} catch (SQLException e1) 
					{
						e1.printStackTrace();
					}
    		}  
        }
       
    }
 
    public static void main(String[] args) 
    {

    }
}