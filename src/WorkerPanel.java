 import java.awt.*;
import java.awt.event.*;

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
//		  cp1.addActionListener(new ActionListener()
//          {
//          	@Override
//          	public void actionPerformed(ActionEvent e) 
//          	{
//		  					
//          				otworz okno do przegladania i dodawania zamowien zaslaniajace to obecne okno
//          	}
//          	});
		  Button cp2 = new Button("Dostawcy");
//		  cp2.addActionListener(new ActionListener()
//        {
//        	@Override
//        	public void actionPerformed(ActionEvent e) 
//        	{
//        		otworz okno do przegladania, dodawania i modyfikowania dostawcow zaslaniajace to obecne okno	
//        	}
//        	});
		  Button cp3 = new Button("Plyty");
//		  cp3.addActionListener(new ActionListener()
//        {
//        	@Override
//        	public void actionPerformed(ActionEvent e) 
//        	{
//        		otworz okno do przegladania, dodawania i usuwania plyt zaslaniajace to obecne okno	
//        	}
//        	});
		  Button cp4 = new Button("Egzemplarze");
//		  cp4.addActionListener(new ActionListener()
//        {
//        	@Override
//        	public void actionPerformed(ActionEvent e) 
//        	{
//        		otworz okno do przegladania, dodawania i usuwania egzemplarzy zaslaniajace to obecne okno
//        	}
//        	});
		  Button cp5 = new Button("Faktury");
//		  cp5.addActionListener(new ActionListener()
//        {
//        	@Override
//        	public void actionPerformed(ActionEvent e) 
//        	{
//        		otworz okno do przegladania i dodawania faktur zaslaniajace to obecne okno
//		  	}
//        	});
		  frame.add(pa);
		  pa.setLayout(new BorderLayout());
		  pa.add(cp1, BorderLayout.NORTH);
		  pa.add(cp2, BorderLayout.EAST);
		  pa.add(cp3, BorderLayout.WEST);
		  pa.add(cp4, BorderLayout.CENTER);
		  pa.add(cp5, BorderLayout.SOUTH);
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

  public static void main(String[] args) 
  {
  }
}