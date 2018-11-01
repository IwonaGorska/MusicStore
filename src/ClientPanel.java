 import java.awt.*;
import java.awt.event.*;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

public class ClientPanel 
{
	
	public ClientPanel() 
	   {
	        initComponents();
	   }
	
	public void initComponents()
	{
		  Frame frame= new Frame("Ekran g³ówny");
		  Panel pa = new Panel();
		  Button cp1 = new Button("Moje faktury");
//		  cp1.addActionListener(new ActionListener()
//          {
//          	@Override
//          	public void actionPerformed(ActionEvent e) 
//          	{
//		  					
//          				otworz okno do tylko przegladania faktur zaslaniajace to obecne okno
//          	}
//          	});
		  Button cp2 = new Button("Mój koszyk");
//		  cp2.addActionListener(new ActionListener()
//        {
//        	@Override
//        	public void actionPerformed(ActionEvent e) 
//        	{
//        		otworz okno do przegladania i usuwania zawartosci koszyka zaslaniajace to obecne okno	
//        	}
//        	});
		  Button cp3 = new Button("Moje konto");
//		  cp3.addActionListener(new ActionListener()
//        {
//        	@Override
//        	public void actionPerformed(ActionEvent e) 
//        	{
//        		otworz okno do przegladania i edycji danych wlasnych zaslaniajace to obecne okno	
//        	}
//        	});
		  Button cp4 = new Button("Szukaj produktu");
		  
//		  cp4.addActionListener(new ActionListener()
//        {
//        	@Override
//        	public void actionPerformed(ActionEvent e) 
//        	{
//        		
//		  		dodaj szukajke w tym komponencie od razu i przycisk do obejrzenia wszystkich produktow zaslaniajace to obecne okno
//        	}
//        	});
		  frame.add(pa);
		  pa.setLayout(new BorderLayout());
		  pa.add(cp1, BorderLayout.NORTH);
		  pa.add(cp2, BorderLayout.EAST);
		  pa.add(cp3, BorderLayout.WEST);
//		  pa.add(cp4, BorderLayout.CENTER);
		  
		  Box box = Box.createHorizontalBox();
		  buildRow(box);
//		  box.add(cp4);
////		  box.add(Box.createVerticalStrut(6));
//		  box.add(cp3);
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
        
        panel.add(Box.createHorizontalStrut(30));//pozioma rozpórka - szerokoœæ wiersza jakby

        JLabel search = new JLabel("Szukaj produktu:"); 
        search.setEnabled(false);
        panel.add(search);
        
        JTextField text = new JTextField();
        Dimension size = new Dimension(40, 23);
        text.setPreferredSize(size);
        panel.add(text);
        
        panel.add(Box.createVerticalStrut(6));
        
        JButton all = new JButton("Wszystkie produkty");
        panel.add(all);
        
//        TextB =new JTextField();
//        setPrefferedMaxAndMinSize(TextB, 40, 23);
//        TextB.setEnabled(false);
//        panel.add(TextB);
        
     }

  public static void main(String[] args) 
  {
  }
}