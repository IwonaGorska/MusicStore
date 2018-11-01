import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
 
public class Registration extends JFrame 
{
 
	// dodaj badanie czy dobrze powtorzono haslo i dodaj inne pola do uzupelnienia - imie, nazwisko, mail itp
	// badanie dlugosci hasla i innych danych i weryfikacja adresu email czy dobra struktura
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
        setLocation(300,20);
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
			// TODO Auto-generated catch block
			  System.out.println("Nie poszlo dobrze z baza w GreetWindow");
			e1.printStackTrace();
		  }
		  
          JButton  closeButton = new JButton ("Zamknij");
          JButton  okButton = new JButton ("OK");
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
          okButton.setSize(90, 30);
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
          okButton.setLocation(getWidth()-310,getHeight()-90);
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
          panel.add(okButton);
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
          closeButton.setToolTipText("Zamknij Program.");
          okButton.setToolTipText("Zarejestruj siê.");
          login.setToolTipText("Podaj swój login.");
          password.setToolTipText("Podaj swoje has³o.");
          passwordRepeat.setToolTipText("Powtórz swoje has³o.");
          name.setToolTipText("Podaj swoje imiê.");
          surname.setToolTipText("Podaj swoje nazwisko.");
          mail.setToolTipText("Podaj swój adres e-mail.");
          this.getContentPane().add(panel);
          closeButton.addActionListener(new ButtonZamknij());
          okButton.addActionListener(new ButtonOK());
          JButton  registrationButton = new JButton ("Register");
          registrationButton.setLocation(getWidth()-190,getHeight()-25);
          registrationButton.setSize(90, 20);
          registrationButton.addActionListener(new ActionListener()
                {
                	@Override
                	public void actionPerformed(ActionEvent e) 
                	{
                		ClientPanel C = new ClientPanel();		
                	}
                	});
          panel.add(registrationButton);
          okButton.setForeground(new Color(9,210,19));
          closeButton.setForeground(new Color(253,4,21));
          registrationButton.setForeground(new Color(248,103,6));
    }
 
    private class ButtonZamknij implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            System.exit(0);
        }
    }
 
    private class ButtonOK implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            String userString = login.getText();
            String passString = password.getText();
            String passRepeatString = passwordRepeat.getText();
            String nameString = name.getText();
            String surnameString = surname.getText();
            String mailString = mail.getText();
            
            if(  (login.getText().isEmpty() == true) ||  (password.getText().isEmpty() == true) ||  (passwordRepeat.getText().isEmpty() == true) 
            ||  (name.getText().isEmpty() == true)	||  (surname.getText().isEmpty() == true) ||  (mail.getText().isEmpty() == true)	)
    		{
    			//wyœwietl okno z komunikatem, ¿e zosta³y takie pola i je zamknij i na czerwono podkreœl t³o tych pól
    			JOptionPane.showMessageDialog(panel, "Uzupe³nij wszystkie wymagane pola!", "B³¹d!", JOptionPane.ERROR_MESSAGE);
    		}
            
    		else
    		{
    			int counter = 0; //inicjalizacja dla picu zeby comilator dal spokoj
	        	
				try 
				{
					ResultSet rs1 = stmt.executeQuery("SELECT * from klienci where login like '" + userString + "' and haslo like '" + passString + "';");
					while(rs1.next())
	   			 	{
	   			 		counter++;
	   			 		System.out.println(counter + "jest next");
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
						String sqlInsert = "INSERT into klienci (login, haslo, imie, nazwisko, email) values ('" + userString + "', '" + passString + "', '" + nameString + "', '" + surnameString + "' , '" + mailString + "');";
						int insertInt = stmt.executeUpdate(sqlInsert);
//						System.out.println(insertInt);
						ClientPanel C = new ClientPanel();
//            			C.setVisible(true);
					} catch (SQLException e1) 
					{
						e1.printStackTrace();
					}
					;
               
    		}  
        }
       
    }
 
    public static void main(String[] args) 
    {
//       EventQueue.invokeLater(new Runnable() 
//       {
//         public void run() 
//         {
//            GreetWindow EkranLogowania = new GreetWindow();
//            EkranLogowania.setVisible(true);
//            Toolkit t = Toolkit.getDefaultToolkit();
//            Dimension d = t.getScreenSize();
//            EkranLogowania.setLocation((d.width/4), (d.height/4));
//         } 
//        });
    }
}