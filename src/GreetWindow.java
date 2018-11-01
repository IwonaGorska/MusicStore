import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
 
public class GreetWindow extends JFrame 
{
 
   JPanel panel = new JPanel();
   JTextField login;
   JPasswordField password;
   JRadioButton worker;
   boolean isWorker;
   Connection conn;
   Statement stmt;
	
   public GreetWindow() 
   {
        setTitle("Witaj w MusicStore");
        setSize(400,300);
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
		  
		
    	  worker = new JRadioButton("Zaloguj sie jako pracownik");
    	  worker.setBounds(200,50,200,30);      
          JButton  closeButton = new JButton ("Zamknij");
          JButton  okButton = new JButton ("OK");
          JLabel  loginLabel= new JLabel("Login: ");
          JLabel  passwordLabel = new JLabel("Has³o: ");
          login = new JTextField(6);
          password = new JPasswordField(6);
          closeButton.setSize(90, 30);
          okButton.setSize(90, 30);
          login.setSize(100,30);
          password.setSize(100,30);
          loginLabel.setSize(100,30);
          passwordLabel.setSize(100,30);
          closeButton.setLocation(getWidth()-190,getHeight()-150);
          okButton.setLocation(getWidth()-310,getHeight()-150);
          loginLabel.setLocation(getWidth()-300,getHeight()-260);
          passwordLabel.setLocation(getWidth()-300,getHeight()-220);
          worker.setLocation(getWidth()-300,getHeight()-180);
          login.setLocation(getWidth()-250,getHeight()-260);
          password.setLocation(getWidth()-250,getHeight()-220);
          panel.setLayout(null);
          panel.add(closeButton);
          panel.add(okButton);
          panel.add(worker);
          panel.add(login);
          panel.add(password);
          panel.add(loginLabel);
          panel.add(passwordLabel);
          closeButton.setToolTipText("Zamknij Program.");
          okButton.setToolTipText("Zaloguj siê.");
          login.setToolTipText("Podaj swój login.");
          password.setToolTipText("Podaj swoje has³o.");
          this.getContentPane().add(panel);
          closeButton.addActionListener(new ButtonZamknij());
          okButton.addActionListener(new ButtonOK());
          JButton  registrationButton = new JButton ("Register");
          registrationButton.setLocation(getWidth()-190,getHeight()-65);
          registrationButton.setSize(90, 20);
          registrationButton.addActionListener(new ActionListener()
                {
                	@Override
                	public void actionPerformed(ActionEvent e) 
                	{
                		Registration R = new Registration();
                		R.setVisible(true);	
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
            String user = login.getText();
            String pass = password.getText();
            
            
            if(  (login.getText().isEmpty() == true) ||   (password.getText().isEmpty() == true) )
    		{
    			//wyœwietl okno z komunikatem, ¿e zosta³y takie pola i je zamknij i na czerwono podkreœl t³o tych pól
    			JOptionPane.showMessageDialog(panel, "Uzupe³nij wszystkie wymagane pola!", "B³¹d!", JOptionPane.ERROR_MESSAGE);
    		}
    		else
    		{
//    			this.setVisible(false);
    			//, zweryfikuj dane w bazie, sprawdz czy zaznaczone, zepracownik i czy to jest pracownik, przejdz do kolejnego panelu
    			if(worker.isSelected())
    			{
    				isWorker = true;
    				int counter = 0;
    				ResultSet rs = null;
        			try 
        			{
						rs = stmt.executeQuery("SELECT * from pracownicy where login like '" + user + "' and haslo like '" + pass + "';");
						while(rs.next())
           			 	{
           			 		counter++;
           			 	}
						System.out.println(user);
						System.out.println(pass);
					} catch (SQLException e1) 
        			{
						e1.printStackTrace();
					}
        			if(counter == 0)
        			{
        				JOptionPane.showMessageDialog(panel, "Pracownik o podanym loginie i haœle nie istnieje!", "B³¹d!", JOptionPane.ERROR_MESSAGE);
        				login.setText("");
        				password.setText("");
        			}
        			else
        			{
        				//przeczodzisz do kolejnego panelu
        				WorkerPanel W = new WorkerPanel();
//	            		W.setVisible(true);
        			}
        				
    			}
                	
                else
                {
                	isWorker = false;
                	int counter = 0;
                	ResultSet rs = null;
        			try 
        			{
        				rs = stmt.executeQuery("SELECT * from klienci where login like '" + user + "' and haslo like '" + pass + "';");
        				while(rs.next())
           			 	{
           			 		counter++;
           			 	}
        			} catch (SQLException e1) 
        			{
						e1.printStackTrace();
					}
        			if(counter == 0)
        			{
        				JOptionPane.showMessageDialog(panel, "Uzytkownik o podanym loginie i haœle nie istnieje!", "B³¹d!", JOptionPane.ERROR_MESSAGE);
        				login.setText("");
        				password.setText("");
        			}
        			else
        			{
        				//przeczodzisz do kolejnego panelu
        				setVisible(false);//NO WIEM, ZE NIE ZBYT OSZCZEDNE PAMIECIOWO, ALE TO TYLKO JEDEN RAZ SIE TAK UKRYWA, WIEC NIE JEST GROZNE
        				ClientPanel C = new ClientPanel();
//        				C.setVisible(true);
        				System.out.println("Otwieram panel klienta z poziomu greetWindow");
        			}
        				
                }
                	
                System.out.println(isWorker);
    		}  
        }
       
    }
 
    public static void main(String[] args) {
 
       EventQueue.invokeLater(new Runnable() 
       {
         public void run() 
         {
            GreetWindow EkranLogowania = new GreetWindow();
            EkranLogowania.setVisible(true);
            Toolkit t = Toolkit.getDefaultToolkit();
            Dimension d = t.getScreenSize();
            EkranLogowania.setLocation((d.width/4), (d.height/4));
         } 
        });
    }
}