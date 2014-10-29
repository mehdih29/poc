package com.arismore.poste;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class JavaReminder {
    Timer timer;

    public JavaReminder(int seconds) {
        timer = new Timer();  //At this line a new Thread will be created
        timer.schedule(new RemindTask(), seconds*1000, 60*1000); //delay in milliseconds
    }

    class RemindTask extends TimerTask {

        @Override
        public void run() {
            System.out.println("ReminderTask is completed by Java timer");
            //timer.cancel(); //Not necessary because we call System.exit
            SimpleDateFormat formater = null;
            formater = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm':00Z'");
            System.out.println(formater.format(new Date()));
            //return new Date();
            //System.out.println(new java.util.Date().getTime());
            //System.exit(0); //Stops the AWT thread (and everything else)
        }
    }

    public static void main(String args[]) {
        System.out.println("Java timer is about to start");
        //while (true){
        JavaReminder reminderBeep = new JavaReminder(5);
        //}
        //System.out.println("Remindertask is scheduled with Java timer.");
    }
}
