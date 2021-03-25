package mx.ipn.escom.clocks;

import java.util.Calendar;
import java.util.Random;

public class ThreadClock extends Thread {

  Calendar time;

  public ThreadClock(String data) {
    super(data);
    Random aleatorio;
    aleatorio = new Random();

    time = Calendar.getInstance();
    time.set(aleatorio.nextInt(10) + 2014, aleatorio.nextInt(12) + 1, aleatorio.nextInt(30) + 1,
        aleatorio.nextInt(24) + 1, aleatorio.nextInt(60) + 1, aleatorio.nextInt(60) + 1);
  }

  public ThreadClock(String data, Calendar time) {
    super(data);
    this.time = time;
  }

  public void run() {
    while(true){
      System.out.println(time.get(Calendar.HOUR_OF_DAY) + ":" + time.get(Calendar.MINUTE) + ":" + time.get(Calendar.SECOND));
      try {
        sleep(1000);
        this.time.add(Calendar.SECOND, 1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
