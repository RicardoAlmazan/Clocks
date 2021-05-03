package mx.ipn.escom.clocks.client.gui;

import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JLabel;

public class TimerLabel extends JLabel implements Runnable {

  private Calendar calendar;
  private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
  private float timeFactor = 1;

  public TimerLabel() {
    this.calendar = Calendar.getInstance();
    // setTime(0, 0, second);
    setFont(new Font("Verdana", Font.PLAIN, 60));
    setForeground(Color.WHITE);
    setBackground(new Color(0x333272));
    setOpaque(true);
    setText();
  }

  public void setTime(Integer hour, Integer minute, Integer second) {
    this.calendar = Calendar.getInstance();
    this.calendar.set(Calendar.HOUR_OF_DAY, hour);
    this.calendar.set(Calendar.MINUTE, minute);
    this.calendar.set(Calendar.SECOND, second);
  }

  @Override
  public void run() {
    while (true)
      try {
        this.calendar.add(Calendar.SECOND, 1);
        setText();
        Thread.sleep((long) (this.timeFactor * 1000));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
  }

  private void setText() {
    setText(timeFormat.format(this.calendar.getTime()));
  }

  public String getTime(){
    SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return timeFormat.format(this.calendar.getTime());
  }
}
