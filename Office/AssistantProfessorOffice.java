package Office;

import java.util.concurrent.Semaphore;
import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;


public class AssistantProfessorOffice  {
    public int count;
    public int sleep;
    public int wake;
    public int leaveCount;
    Semaphore mutex;
    Semaphore AssistantSemaphore;
    Semaphore studentsSemaphore;


    public AssistantProfessorOffice(){
        count = 0;
        sleep = 0;
        wake = 0;
        leaveCount = 0;
        mutex = new Semaphore(1);
        AssistantSemaphore = new Semaphore(0);
        studentsSemaphore = new Semaphore(0);
    }

    
    
    public void teachingAssistant(int id, int desks){
        try {
            while (true) {
                mutex.acquire();
                sleep++;
                wake = desks - sleep;
                mutex.release();                
                System.out.println("TA "+ id +" is sleeping..." );

                studentsSemaphore.acquire();
                AssistantSemaphore.release();
                //F.helpStudent(id);          
                mutex.acquire();
                sleep--;
                wake = desks - sleep;
                mutex.release();

                System.out.println("The TA "+ id +" is helping the student." );
                Thread.sleep(1000);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void students(int id, int chairs){
        try {
            //F.enter(id);
            System.out.println("Student " + id + " arrives");

            mutex.acquire();

            if(count > chairs){
                mutex.release();
                leaveCount++;
                System.out.println("Student " + id + " leaves because no chairs is avaliable.");
                Thread.sleep(3000);
                students(id, chairs);

            }else{
                count++;
                mutex.release();

                studentsSemaphore.release();
                AssistantSemaphore.acquire();
        
                System.out.println("Student " + id + " is getting a help.");
                Thread.sleep(1000);
                //F.getHelp(id);

                mutex.acquire();
                count--;
                mutex.release();

                System.out.println("Student " + id + " leaves office.");
                //F.go(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public int getSleep(){
        return sleep;
    }


    

    
}
