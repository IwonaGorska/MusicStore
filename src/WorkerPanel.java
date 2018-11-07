 import java.awt.*;
import java.awt.event.*;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class WorkerPanel 
{
	
	public WorkerPanel() 
	   {
	        initComponents();
	   }
	
	public void initComponents()
	{
		  Frame frame= new Frame("Ekran g³ówny");
		  Panel pa = new Panel();
		  Button cp1 = new Button("Zamówienia");
		  cp1.addActionListener(new ActionListener()
          {
          	@Override
          	public void actionPerformed(ActionEvent e) 
          	{
          		Orders ord = new Orders();
        		ord.setVisible(true);
          	}
          	});
		  Button cp2 = new Button("Dostawcy");
		  cp2.addActionListener(new ActionListener()
        {
        	@Override
        	public void actionPerformed(ActionEvent e) 
        	{	
        		Delivers I = new Delivers();
        		I.setVisible(true);
        	}
        	});
		  Button cp3 = new Button("Plyty");
		  cp3.addActionListener(new ActionListener()
        {
        	@Override
        	public void actionPerformed(ActionEvent e) 
        	{	
        		Positions P = new Positions("select * from plyty;", true);
        		P.setVisible(true);
        	}
        	});
		  Button cp5 = new Button("Faktury");
		  cp5.addActionListener(new ActionListener()
        {
        	@Override
        	public void actionPerformed(ActionEvent e) 
        	{
		  		Invoices I = new Invoices();
		  	}
        	});
		  frame.add(pa);
		  pa.setLayout(new BorderLayout());
		  pa.add(cp1, BorderLayout.NORTH);
		  pa.add(cp2, BorderLayout.EAST);
		  pa.add(cp3, BorderLayout.WEST);
		  pa.add(cp5, BorderLayout.SOUTH);
		  
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
        Action action = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	if(text.getText().isEmpty() == false)
            	{
            		String t = text.getText();
            		t = t.toLowerCase();
            		String sql;
            		sql = "SELECT * from egzemplarze where indeks_plyty IN (select indeks from plyty where lower(tytul) like '%" + t + "%');";
            		System.out.println(sql);
            		Products P = new Products(sql, true, -1); // -1 nie bêdzie u¿ywane i tak, bo to nie klient
    				P.setVisible(true);
            	}
            }
        };
        text.addActionListener( action );
        panel.add(text);
        
        panel.add(Box.createVerticalStrut(20));

        JButton all = new JButton("Wszystkie produkty");
		all.addActionListener(new ActionListener()
	      {
	      	@Override
	      	public void actionPerformed(ActionEvent e) 
	      	{
	      		String sql = "SELECT * from egzemplarze;";
		    		Products P = new Products(sql, true, -1); // -1 nie bêdzie u¿ywane i tak, bo to nie klient
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