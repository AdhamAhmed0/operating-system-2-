package Office;
//(Integer.toString(myInt)
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Main {
    
        
    public static void main(String[] args){
        AssistantProfessorOffice AssistantProfessorOffice = new AssistantProfessorOffice();

        Scanner myObj = new Scanner(System.in);

        System.out.println("Enter Number of Chairs: ");
        int desks = myObj.nextInt();

        System.out.println("Enter Number of Customers: ");
        int num = myObj.nextInt();

        System.out.println("Enter Number of Chairs: ");
        int chairs = myObj.nextInt();

        for(int i = 1; i <= desks; i++){
            final int id = i;
            Thread TAThread = new Thread(()->AssistantProfessorOffice.teachingAssistant(id, desks));
            TAThread.start();
        }
        

        for(int i = 1; i <= num; i++){
            final int id = i;
            Thread studentThread = new Thread(()->AssistantProfessorOffice.students(id, chairs));
            studentThread.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            }
    }

}

