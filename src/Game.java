/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package CarRace;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.JFrame;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;

/**
 *
 * @author Sagar
 */
public class Game extends JFrame{    
    Game()
    {
        this.setSize(1280,720);
        this.setTitle("Wars of the Cars");
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);       
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        final CarPanel cpanel=new CarPanel();   
                            
        this.add(cpanel);       
        
        cpanel.name=JOptionPane.showInputDialog(this,"Enter your name", "Enter Username:", QUESTION_MESSAGE);          
        
        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent we)
            { 
                String ObjButtons[] = {"Yes","No"};

                cpanel.pause=true;
                int PromptResult = JOptionPane.showOptionDialog(null,""+cpanel.name+",Are you sure you want to exit?","Wars of the Cars",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,ObjButtons,ObjButtons[1]);

                if(PromptResult==JOptionPane.YES_OPTION)
                { 
                    dispose();
                }
                else
                {
                    cpanel.pause=false;
                    cpanel.speed=1; 
                }
            }
        });
        
    }
       
    public static void main(String args[])
    {
        new Game();
    }
}



