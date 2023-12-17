package Office;
import javax.swing.*;
import javax.swing.text.Document;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Semaphore;

public class MainFrame {

    public int count =0;
    public int sleep =0;
    public int wake =0;
    public int leaveCount =0;
    Semaphore mutex = new Semaphore(1);
    Semaphore AssistantSemaphore = new Semaphore(0);
    Semaphore studentsSemaphore = new Semaphore(0);



    public int num, chairs, desks;
    JPanel panel;
    JFrame frame; 
    JLabel labelin, labelout, label1, label2, label3, label4, label5, label6, label7;
    JTextField textField1, textField2, textField3, textField4, textField5, textField6, textField7;
    JButton button1, button2;
    //AssistantProfessorOffice AssistantProfessorOffice = new AssistantProfessorOffice();
    //Main main = new Main();
    synchronized static void fun(JTextField text, int s){
        text.setText(Integer.toString(s));
    }

    public void init(){
        frame = new JFrame("The Sleeping Teaching Assistant");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        panel = new JPanel();
        panel.setLayout(new GridLayout(9, 2, 0, 5));

        labelin = new JLabel("Inputs");

        label1 = new JLabel("TAS");
        label2 = new JLabel("Chairs");
        label3 = new JLabel("Students");

        textField1 = new JTextField();
        textField2 = new JTextField();
        textField3 = new JTextField();

        labelout = new JLabel("Outputs");
        label4 = new JLabel("TAS Working");
        label5 = new JLabel("TAS Sleeping");
        label6 = new JLabel("Students Waiting on Chairs");
        label7 = new JLabel("Students that Will Come Later");

        textField4 = new JTextField();
        textField5 = new JTextField();
        textField6 = new JTextField();
        textField7 = new JTextField();

        button1 = new JButton("Run");
        button2 = new JButton("Clear");

        
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Button 2 clicked");
            }
        });

        panel.add(labelin);
        panel.add(new JPanel()); 
        panel.add(new JPanel());
        panel.add(new JPanel());
        panel.add(label1);
        panel.add(textField1);
        panel.add(new JPanel());
        panel.add(new JPanel());
        panel.add(label2);
        panel.add(textField2);
        panel.add(new JPanel());
        panel.add(new JPanel());
        panel.add(label3);
        panel.add(textField3);
        panel.add(new JPanel());
        panel.add(labelout);
        panel.add(new JPanel());
        panel.add(new JPanel()); 
        panel.add(label4);
        panel.add(textField4);
        panel.add(new JPanel());   
        panel.add(new JPanel());    
        panel.add(label5);
        panel.add(textField5);
        panel.add(new JPanel());
        panel.add(new JPanel()); 
        panel.add(label6);
        panel.add(textField6);
        panel.add(new JPanel());
        panel.add(new JPanel()); 
        panel.add(label7);
        panel.add(textField7);
        panel.add(new JPanel()); 


        panel.add(button1);
        //panel.add(button2);

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                desks = Integer.parseInt(textField1.getText());
                chairs = Integer.parseInt(textField2.getText());
                num = Integer.parseInt(textField3.getText());

                for(int i = 1; i <= desks; i++){
                    Thread TAThread = new Thread(new Runnable() {
                        @Override
                        public void run(){
                            try {
                                while (true) {
                                    
                                    mutex.acquire();
                                    sleep++;
                                    wake = desks - sleep;
                                    mutex.release(); 

                                    fun(textField5,sleep);
                                    fun(textField4,wake);
                                    //fun(textField4,wake);
                                    
                                    System.out.println("TAs Sleeping: " + sleep);
                                    
                                    //fun(textField5,sleep);
                    
                                    studentsSemaphore.acquire();
                                    AssistantSemaphore.release();
                                    //fun(textField4,wake);

                                    mutex.acquire();
                                    sleep--;
                                    wake = desks - sleep;
                                    mutex.release();
                                    fun(textField5,sleep);
                                    fun(textField4,wake);
                                    System.out.println("TAs working: " + wake);
                                    Thread.sleep(1000);
                                    
                                    
                                    
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    TAThread.start();
                }

                for(int i = 1; i <= num; i++){
                    Thread studentThread = new Thread(new Runnable() {
                        @Override
                        public void run(){
                            try {
                                
                                mutex.acquire();
                                if(count > chairs){
                                    mutex.release();
                                    leaveCount++;
                                    System.out.println("Students that will come later: " + leaveCount);
                                    Thread.sleep(3000);
                                    run( );
                    
                                }else{
                                    
                                    System.out.println("Students Waiting on chairs: "+ count);
                                    count++;
                                    mutex.release();

                                    

                                    studentsSemaphore.release();
                                    AssistantSemaphore.acquire();
                                    Thread.sleep(500);
                    
                                    mutex.acquire();
                                    count--;
                                    mutex.release();

                                    fun(textField7,leaveCount);
                                    fun(textField6, count);
                                    
                                }
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    studentThread.start();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                
            }
        });

    }

    
    public static void main(String[] args){
        MainFrame mainframe = new MainFrame();
        mainframe.init();
    }

}
