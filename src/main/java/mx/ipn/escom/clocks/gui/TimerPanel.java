package mx.ipn.escom.clocks.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TimerPanel extends JPanel implements Runnable {

  private static final long serialVersionUID = 1L;
  Calendar calendar;
  SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");;
  float timeFactor = 1;

  JLabel timeLabel;
  JButton modifyButton;
  JButton increaseFrequencyButton;
  JButton reduceFrequencyButton;

  private volatile boolean shutdown;

  public TimerPanel() {
    this.calendar = Calendar.getInstance();
    setProperties();
  }

  public TimerPanel(Integer hour, Integer minute, Integer second) {
    setTime(hour, minute, second);
    setProperties();
  }

  private void setTime(Integer hour, Integer minute, Integer second) {
    this.calendar = Calendar.getInstance();
    this.calendar.set(Calendar.HOUR_OF_DAY, hour);
    this.calendar.set(Calendar.MINUTE, minute);
    this.calendar.set(Calendar.SECOND, second);
  }

  private void setProperties() {
    this.timeLabel = new JLabel();
    this.modifyButton = new JButton();
    this.increaseFrequencyButton = new JButton();
    this.reduceFrequencyButton = new JButton();

    timeLabel.setFont(new Font("Verdana", Font.PLAIN, 50));
    timeLabel.setForeground(Color.WHITE);
    timeLabel.setBackground(new Color(0x333272));
    timeLabel.setOpaque(true);
    setText();

    modifyButton.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/resources/edit.png")).getImage()
        .getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
    modifyButton.setPressedIcon(new ImageIcon(new ImageIcon(getClass().getResource("/resources/edit-hover.png"))
        .getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
    modifyButton.setBorder(BorderFactory.createEmptyBorder());
    modifyButton.setContentAreaFilled(false);
    modifyButton.setBounds(0, 0, 30, 30);
    modifyButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        pause();
        modifyButton.setEnabled(false);
        boolean valid = false;
        while (!valid) {
          String timeString = JOptionPane.showInputDialog(null, "Ingrese la nueva hora.",
              timeFormat.format(calendar.getTime()));
          String[] aux = timeString.split(":");
          if (aux.length != 3)
            JOptionPane.showMessageDialog(null, "El formato ingresado no es válido");
          else if (Integer.parseInt(aux[0]) >= 0 && Integer.parseInt(aux[0]) < 24)
            if (Integer.parseInt(aux[1]) >= 0 && Integer.parseInt(aux[1]) < 60)
              if (Integer.parseInt(aux[2]) >= 0 && Integer.parseInt(aux[2]) < 60) {
                valid = true;
                setTime(Integer.parseInt(aux[0]), Integer.parseInt(aux[1]), Integer.parseInt(aux[2]));
                resume();
                modifyButton.setEnabled(true);
              } else
                JOptionPane.showMessageDialog(null, "Los segundos tienen que estar en un rango entre 00 y 59.");
            else
              JOptionPane.showMessageDialog(null, "Los minutos tienen que estar en un rango entre 00 y 59.");
          else
            JOptionPane.showMessageDialog(null, "La hora tiene que estar en un rango entre 00 y 23.");
        }
      }
    });

    increaseFrequencyButton.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/resources/increase.png"))
        .getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
    increaseFrequencyButton
        .setPressedIcon(new ImageIcon(new ImageIcon(getClass().getResource("/resources/increase-hover.png")).getImage()
            .getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
    increaseFrequencyButton.setBorder(BorderFactory.createEmptyBorder());
    increaseFrequencyButton.setContentAreaFilled(false);

    increaseFrequencyButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        increaseFrequencyButton.setEnabled(false);
        timeFactor = timeFactor / 2;
        try {
          Thread.sleep((long) (timeFactor * 1000));
          increaseFrequencyButton.setEnabled(true);
        } catch (InterruptedException e1) {
          e1.printStackTrace();
        }
      }
    });

    reduceFrequencyButton.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/resources/decrease.png"))
        .getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
    reduceFrequencyButton
        .setPressedIcon(new ImageIcon(new ImageIcon(getClass().getResource("/resources/decrease-hover.png")).getImage()
            .getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
    reduceFrequencyButton.setBorder(BorderFactory.createEmptyBorder());
    reduceFrequencyButton.setContentAreaFilled(false);

    reduceFrequencyButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        reduceFrequencyButton.setEnabled(false);
        timeFactor = timeFactor * 2;
        try {
          Thread.sleep((long) (timeFactor * 1000));
          reduceFrequencyButton.setEnabled(true);
        } catch (InterruptedException e1) {
          e1.printStackTrace();
        }
      }
    });

    add(timeLabel);
    add(modifyButton);
    add(reduceFrequencyButton);
    add(increaseFrequencyButton);
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

  public void pause() {
    shutdown = true;
  }

  public void resume() {
    shutdown = false;
  }
}