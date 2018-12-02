 import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;

public class ClientPanel 
{
	int indeks;
	
	public ClientPanel(int id) 
	   {
	        initComponents();
	        indeks = id;
	   }
	
	public void initComponents()
	{
		  Frame frame= new Frame("Ekran g³ówny");
		  Panel pa = new Panel();
		  Button cp1 = new Button("Moje faktury");
		  cp1.addActionListener(new ActionListener()
          {
          	@Override
          	public void actionPerformed(ActionEvent e) 
          	{		  					
          		Invoices I = new Invoices(indeks);
          		System.out.println("Invoices jest otwierane");
          	}
          	});
		  Button cp2 = new Button("Mój koszyk");
		  cp2.addActionListener(new ActionListener()
        {
        	@Override
        	public void actionPerformed(ActionEvent e) 
        	{
        		MyCart MC = new MyCart(indeks);
				MC.setVisible(true);
        	}
        	});
		  Button cp3 = new Button("Moje konto");
		  cp3.addActionListener(new ActionListener()
        {
        	@Override
        	public void actionPerformed(ActionEvent e) 
        	{
				MyAccount MA = new MyAccount(indeks);
				MA.setVisible(true);	
        	}
        	});

		  frame.add(pa);
		  pa.setLayout(new BorderLayout());
		  pa.add(cp1, BorderLayout.NORTH);
		  pa.add(cp2, BorderLayout.EAST);
		  pa.add(cp3, BorderLayout.WEST);
		  
		  Box box = Box.createVerticalBox();
		  buildRow(box);
		  pa.add(box, BorderLayout.CENTER);
		 
		  frame.setSize(550,350);
		  frame.setVisible(true);
		  frame.setLocation(400,150);
		  frame.addWindowListener(new WindowAdapter()
		  {
			  public void windowClosing(WindowEvent e)
			  {
				  System.exit(0);
			  }
		  });
		  
	}
	
	private void buildRow(JComponent panel) 
    {
        panel.add(Box.createVerticalStrut(70));

        JLabel search = new JLabel("Szukaj produktu:"); 
        panel.add(search);
        
        panel.add(Box.createVerticalStrut(20));
        
        JTextField text = new JTextField();
        text.setToolTipText("Wpisz szukan¹ frazê i wciœnij enter.");
        panel.add(text);
        
        Action action = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	if(text.getText().isEmpty() == false)
            	{
            		//DOLOZ TUTAJ INJEKCJE ZAPYTAN, SUME CZY TAM COS, ZEBY SZUKALO FRAZY NIE TYLKO W TYTULE, ALE TEZ W ARTYSCIE CZY COS
            		System.out.println("some action");
            		String t = text.getText();
            		t = t.toLowerCase();
            		boolean suspicious = false; // ZMIENNA KTORA POZWALA NA SPRAWDZENIE, CZY Z FRAZY PODANEJ PRZEZ UZYTKOWNIKA MOZNA ZROBIC SQL INJECTION
            		for(int i = 0; i<t.length(); i++)
            		{
            			if((t.charAt(i) < 48 && t.charAt(i) > 32) || (t.charAt(i) < 65 && t.charAt(i) > 57))
            				suspicious = true;
            		}
            		
            		if(suspicious == false)
            		{
            			String sql;
                		sql = "SELECT * from egzemplarze where indeks_plyty IN (select indeks from plyty where lower(tytul) like '%" + t + "%' or lower(wykonawca) like '%" + t + "%') order by indeks;";
                		System.out.println(sql);
                		Products P = new Products(sql, false, indeks);
        				P.setVisible(true);
            		}
            		else
            		{
            			JOptionPane.showMessageDialog(panel, "Niedozwolone znaki!", "B³¹d!", JOptionPane.ERROR_MESSAGE);
            		}
            		
            	}
            }
        };
        text.addActionListener( action );
        
        panel.add(Box.createVerticalStrut(20));

        JButton all = new JButton("Wszystkie produkty");
		all.addActionListener(new ActionListener()
		{
	      	@Override
	      	public void actionPerformed(ActionEvent e) 
	      	{
	      		String sql = "SELECT * from egzemplarze order by indeks;";
	      		Products P = new Products(sql, false, indeks);
				P.setVisible(true);
	      	}
      	});
        panel.add(all);
        
        panel.add(Box.createVerticalStrut(80));
     }

  public static void main(String[] args) 
  {
  }
}