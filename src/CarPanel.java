/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package CarRace;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import static javax.swing.JOptionPane.*;



/**
 *
 * @author Sagar
 */
class CarPanel extends JPanel implements KeyListener
{
    int x=600,y=500,opp1x=550,opp2x=300,opp3x=700,opp4x=230,i;
    int opp1y=0,opp2y=0,opp3y=0,opp4y=0;        
    public int step = 25,count=1,totalLife=26,flag=0;
    public long score=0L,tempScore=0L;
    public float speed = 1; 
    boolean bool=true,pause=false,auth=false;    
    
    Thread t;    
    String st=null;
    String url="jdbc:odbc:Driver={SQL Server};server=GIDEON;DATABASE=Distraction";    
    public String name=null;
    
    private BufferedImage opp1;
    private BufferedImage mycar;
    private BufferedImage opp2;
    private BufferedImage opp3;
    private BufferedImage opp4;
    private BufferedImage tree1;
    private BufferedImage tree2;
    private BufferedImage crash;
    
    
    @SuppressWarnings("LeakingThisInConstructor")
    CarPanel()
    {         
       this.addKeyListener(this);       
       this.setFocusable(true);          
    }
    
    private void loadPics()
    {
        try
        {            
            mycar= ImageIO.read(new File("src/CarRace/red_car.png"));
            opp1 = ImageIO.read(new File("src/CarRace/opp1_car.png"));
            opp2 = ImageIO.read(new File("src/CarRace/opp2_car.png"));
            opp3 = ImageIO.read(new File("src/CarRace/opp3_car.png"));
            opp4 = ImageIO.read(new File("src/CarRace/opp4_car.png"));
            tree1= ImageIO.read(new File("src/CarRace/tree1.png"));
            tree2= ImageIO.read(new File("src/CarRace/tree2.png"));
            crash= ImageIO.read(new File("src/CarRace/crash.png"));
        }
        catch(Exception ex)
        {
            System.out.println("Image not found");
        }
    }
 
    public Rectangle opp1rect()
    {
	return new Rectangle(opp1x,opp1y,62,126);
    }
    
    public Rectangle opp2rect()
    {
	return new Rectangle(opp2x,opp2y,62,115);
    }
    
    
    public Rectangle opp3rect()
    {
	return new Rectangle(opp3x,opp3y,62,112);
    }
    
    public Rectangle opp4rect()
    {
	return new Rectangle(opp4x,opp4y,62,133);
    }
    
    public Rectangle myCarRect()
    {
	return new Rectangle(x,500,62,132);
    }
    
    @Override
    public void paintComponent(Graphics g1) 
    {
        super.paintComponent(g1);
        loadPics();       
        Graphics2D g=(Graphics2D) g1;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.green);        
        g.fillRect(0,0,240,720);
        g.fillRect(1040,0,240,720);
        g.setColor(new Color(120,120,120));
        g.fillRect(240, 0, 800, 720);
        g.setColor(Color.white);   

        g.setFont(new Font("TimesRoman", Font.BOLD, 30)); 
        String str="Lives: "+(totalLife-count);
        g.drawString(str, 280, 650);
        str="Score: "+score;
        g.drawString(str, 860, 650);
        
        
        if(!pause)
        {
            if(bool)//TREE AND DIVIDER
            {
                for(int pos=0;pos<15;pos++)
                {
                    g.setColor(Color.white);
                    g.fillRect(600,80+pos*160,20,80);
                    g.drawImage(tree1, 1020, pos*160, this);
                    g.drawImage(tree1, 70, pos*160, this);
                    bool=false;
                }
            }
            else
            {
                for(int pos=0;pos<15;pos++)
                {
                    g.setColor(Color.white);
                    g.fillRect(600,pos*160,20,80);
                    g.drawImage(tree2, 1020, pos*160, this);
                    g.drawImage(tree2, 70, pos*160, this);
                    bool=true;
                }
            }

           //NOW ALL CARS
            g.drawImage(opp1, opp1x, opp1y, this);
            g.drawImage(opp2, opp2x, opp2y, this);
            g.drawImage(opp3, opp3x, opp3y, this);
            g.drawImage(opp4, opp4x, opp4y, this);
            g.drawImage(mycar, x, 500, this);


            //LOGIC BOARD
            if(opp1y>700)
              {
                  opp1y=-150;
                  //randomNum = minimum + (int)(Math.random() * maximum); 
                  opp1x=240+(int)(Math.random()*(980-240));
              }
            else{ opp1y+=50; }

            if(opp2y>700)
              {
                  opp2y=-150;
                  //randomNum = minimum + (int)(Math.random() * maximum); 
                  opp2x=240+(int)(Math.random()*(980-240));
              }
            else{ opp2y+=40; }

            if(opp3y>700)
              {
                  opp3y=-150;
                  opp3x=240+(int)(Math.random()*(980-240));
              }
            else {opp3y+=60;}

            if(opp4y>700)
              {
                  opp4y=-150;
                  opp4x=240+(int)(Math.random()*(980-240));
              }
            else {opp4y+=70;}

        }
        else
        {

        }


        //COLLISION FOR OPPONENT
        if(collision1() && flag1==1)
        {
            //System.out.print(collision1());
            if(count<totalLife)//count<5
            {
                flag1=0; 
                try{
                g.drawImage(crash, opp1x-123, 380, this);
                //Thread.sleep(500);
                }
                catch(Exception e){}
                count++;
            }
            else
            {
                pause=true;
                tempScore=score;
                PreparedStatement pst ;
                try {

                   Connection con=DriverManager.getConnection(url); 
                   try {
                        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Archery.Panel.class.getName()).log(Level.SEVERE, null, ex);
                    }

                   pst=con.prepareStatement("Select * from SinglePlayer where Player_Name= ? and Game_Name = ?");
                   pst.setString(1, name);
                   pst.setString(2, "CarRace");
                   ResultSet rs=pst.executeQuery();
                   if(rs.next())
                   {
                        rs.close();
                        pst.close();
                        pst = con.prepareStatement("update SinglePlayer set Score= ? where Player_Name= ? and Game_Name = ?");                    
                        pst.setString(1, String.valueOf(score));
                        pst.setString(2,name);
                        pst.setString(3,"CarRace");
                        pst.executeUpdate();
                   }
                   else
                   {
                        rs.close();
                        pst.close();
                        pst = con.prepareStatement("insert into SinglePlayer values( ?,?,? )");
                        pst.setString(1,"CarRace");
                        pst.setString(2,name);
                        pst.setString(3, String.valueOf(score));
                        pst.executeUpdate();
                   }
                   System.out.println(name);
               } catch (SQLException ex) {
                   Logger.getLogger(Archery.Panel.class.getName()).log(Level.SEVERE, null, ex);
                   JOptionPane.showMessageDialog(this,"Not able to Connect to Database" +ex.getMessage(), "Shadow Archer", WARNING_MESSAGE); 
               }

                JOptionPane.showMessageDialog(this, "Your game is Over\n Your Score is "+score, "Wars of the Cars", WARNING_MESSAGE); 

            }
        }


        if(collision2() && flag2==1)
        {
            //System.out.print(collision2());
            if(count<totalLife)//count<5
            {
                flag2=0; try{
                g.drawImage(crash, opp2x-123, 380, this);
               // Thread.sleep(500);
                }
                catch(Exception e){}
                count++;
            }
            else
            {

                pause=true;
                tempScore=score;
                PreparedStatement pst ;
               try {

                   Connection con=DriverManager.getConnection(url); 
                   try {
                        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Archery.Panel.class.getName()).log(Level.SEVERE, null, ex);
                    }

                   pst=con.prepareStatement("Select * from SinglePlayer where Player_Name= ? and Game_Name = ?");
                   pst.setString(1, name);
                   pst.setString(2, "CarRace");
                   ResultSet rs=pst.executeQuery();
                   if(rs.next())
                   {
                        rs.close();
                        pst.close();
                        pst = con.prepareStatement("update SinglePlayer set Score= ? where Player_Name= ? and Game_Name = ?");                    
                        pst.setString(1, String.valueOf(score));
                        pst.setString(2,name);
                        pst.setString(3,"CarRace");
                        pst.executeUpdate();
                   }
                   else
                   {
                        rs.close();
                        pst.close();
                        pst = con.prepareStatement("insert into SinglePlayer values( ?,?,? )");
                        pst.setString(1,"CarRace");
                        pst.setString(2,name);
                        pst.setString(3, String.valueOf(score));
                        pst.executeUpdate();
                   }
                   System.out.println(name);
               } catch (SQLException ex) {
                   Logger.getLogger(Archery.Panel.class.getName()).log(Level.SEVERE, null, ex);
                   JOptionPane.showMessageDialog(this,"Not able to Connect to Database" +ex.getMessage(), "Shadow Archer", WARNING_MESSAGE); 
               }

                JOptionPane.showMessageDialog(this, "Your game is Over\n Your Score is "+score, "Wars of the Cars", WARNING_MESSAGE); 

            }
        }

        if(collision3() && flag3==1)
        {
            //System.out.print(collision3());
            if(count<totalLife)//count<5
            {
                flag3=0; 
                count++;
                try{
                g.drawImage(crash, opp3x-123, 380, this);
                //Thread.sleep(500);
                }
                catch(Exception e){}
            }            
            else
            {

                pause=true;
                tempScore=score;
                PreparedStatement pst ;
               try {

                   Connection con=DriverManager.getConnection(url); 
                   try {
                        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Archery.Panel.class.getName()).log(Level.SEVERE, null, ex);
                    }

                   pst=con.prepareStatement("Select * from SinglePlayer where Player_Name= ? and Game_Name = ?");
                   pst.setString(1, name);
                   pst.setString(2, "CarRace");
                   ResultSet rs=pst.executeQuery();
                   if(rs.next())
                   {
                        rs.close();
                        pst.close();
                        pst = con.prepareStatement("update SinglePlayer set Score= ? where Player_Name= ? and Game_Name = ?");                    
                        pst.setString(1, String.valueOf(score));
                        pst.setString(2,name);
                        pst.setString(3,"CarRace");
                        pst.executeUpdate();
                   }
                   else
                   {
                        rs.close();
                        pst.close();
                        pst = con.prepareStatement("insert into SinglePlayer values( ?,?,? )");
                        pst.setString(1,"CarRace");
                        pst.setString(2,name);
                        pst.setString(3, String.valueOf(score));
                        pst.executeUpdate();
                   }
                   System.out.println(name);
               } catch (SQLException ex) {
                   Logger.getLogger(Archery.Panel.class.getName()).log(Level.SEVERE, null, ex);
                   JOptionPane.showMessageDialog(this,"Not able to Connect to Database" +ex.getMessage(), "Shadow Archer", WARNING_MESSAGE); 
               }

                JOptionPane.showMessageDialog(this, "Your game is Over\n Your Score is "+score, "Wars of the Cars", WARNING_MESSAGE); 

            }
        }

        if(collision4() && flag4==1)
        {
            //System.out.print(collision4());
            if(count<totalLife)//count<5
            {
                flag4=0; 
                count++;
                try
                {
                    g.drawImage(crash, opp4x-123, 380, this);                    
                }catch(Exception e){}
            }
            else
            {

                pause=true;
                tempScore=score;
                PreparedStatement pst ;
               try {

                   Connection con=DriverManager.getConnection(url); 
                   try {
                        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Archery.Panel.class.getName()).log(Level.SEVERE, null, ex);
                    }

                   pst=con.prepareStatement("Select * from SinglePlayer where Player_Name= ? and Game_Name = ?");
                   pst.setString(1, name);
                   pst.setString(2, "CarRace");
                   ResultSet rs=pst.executeQuery();
                   if(rs.next())
                   {
                        rs.close();
                        pst.close();
                        pst = con.prepareStatement("update SinglePlayer set Score= ? where Player_Name= ? and Game_Name = ?");                    
                        pst.setString(1, String.valueOf(score));
                        pst.setString(2,name);
                        pst.setString(3,"CarRace");
                        pst.executeUpdate();
                   }
                   else
                   {
                        rs.close();
                        pst.close();
                        pst = con.prepareStatement("insert into SinglePlayer values( ?,?,? )");
                        pst.setString(1,"CarRace");
                        pst.setString(2,name);
                        pst.setString(3, String.valueOf(score));
                        pst.executeUpdate();
                   }
                   System.out.println(name);
               } catch (SQLException ex) {
                   Logger.getLogger(Archery.Panel.class.getName()).log(Level.SEVERE, null, ex);
                   JOptionPane.showMessageDialog(this,"Not able to Connect to Database" +ex.getMessage(), "Shadow Archer", WARNING_MESSAGE); 
               }

                JOptionPane.showMessageDialog(this, "Your game is Over\n Your Score is "+score, "Wars of the Cars", WARNING_MESSAGE); 

            }
        }
        
        
        
        
        
        
        try
        {
            if(tempScore==0L)
                score++;            
            
            Thread.sleep((int)(80/speed));
            repaint();
        }
        catch(Exception e){}       
    }  //END OF PAINT METHOD  
    
    int flag1=0,flag2=0,flag3=0,flag4=0;
    
    public boolean collision1()//    COLLISION DETECT
    {
        if(opp1rect().intersects(myCarRect()))
        {            
            opp1y=701;            
            flag1=1;
            return true;
        }
        else
            return false;
    }
    
    public boolean collision2()//    COLLISION DETECT
    {
        if(opp2rect().intersects(myCarRect()))
        {
            opp2y=701;
            flag2=1;
            return true;
        }
        else
            return false;
    }
    
    public boolean collision3()//    COLLISION DETECT
    {
        if(opp3rect().intersects(myCarRect()))
        {
            opp3y=701;
            flag3=1;
            return true;
        }
        else
            return false;
    }
    
    public boolean collision4()//    COLLISION DETECT
    {
        if(opp4rect().intersects(myCarRect()))
        {
            opp4y=701;         
            flag4=1;
            return true;
        }
        else
            return false;
    }
    
    
    
    @Override
    public void keyPressed(KeyEvent e) 
    {	
        int k=e.getKeyCode();        
        switch(k)
        {
            case KeyEvent.VK_LEFT  :if(x>240) x=x-step;
                                    break;

            case KeyEvent.VK_RIGHT :if(x<980) x=x+step;
                                    break;
                
            case KeyEvent.VK_UP    :if(speed<5)speed++;    
                                    break;
                
            case KeyEvent.VK_DOWN  :if(speed>1) speed--;                                   
                                    break;
                
            case KeyEvent.VK_F5     :repaint();
                                    break;
        }        
	
    }
    
    @Override
    public void keyReleased(KeyEvent e){}	
    @Override
    public void keyTyped(KeyEvent e){}   
 }