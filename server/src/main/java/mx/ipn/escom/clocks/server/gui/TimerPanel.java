package mx.ipn.escom.clocks.server.gui;

import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class TimerPanel extends JPanel implements Runnable {

  private static final long serialVersionUID = 1L;
  private Calendar calendar;
  private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
  private float timeFactor = 1;

  private JLabel timeLabel;

  private volatile boolean shutdown;

  public TimerPanel() {
    this.calendar = Calendar.getInstance();
    setProperties(false);
  }

  public TimerPanel(Integer hour, Integer minute, Integer second) {
    setTime(hour, minute, second);
    setProperties(true);
  }

  private void setTime(Integer hour, Integer minute, Integer second) {
    this.calendar = Calendar.getInstance();
    this.calendar.set(Calendar.HOUR_OF_DAY, hour);
    this.calendar.set(Calendar.MINUTE, minute);
    this.calendar.set(Calendar.SECOND, second);
  }

  private void setProperties(Boolean addSendButon) {
    this.timeLabel = new JLabel();

    timeLabel.setFont(new Font("Verdana", Font.PLAIN, 50));
    timeLabel.setForeground(Color.WHITE);
    timeLabel.setBackground(new Color(0x333272));
    timeLabel.setOpaque(true);
    setText();
    add(timeLabel);
  }

  private void setText() {
    timeLabel.setText(timeFormat.format(this.calendar.getTime()));
  }

  @Override
  public void run() {
    while (true)
      while (!shutdown) {
        try {
          this.calendar.add(Calendar.SECOND, 1);
          setText();
          Thread.sleep((long) (this.timeFactor * 1000));
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
  }

  public String getTime() {
    SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return timeFormat.format(this.calendar.getTime());
  }
}
