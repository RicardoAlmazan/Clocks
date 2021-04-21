package mx.ipn.escom.clocks.server.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TimerPanel extends JPanel implements Runnable {

  private static final long serialVersionUID = 1L;
  private Calendar calendar;
  private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");;
  private float timeFactor = 1;

  private JLabel timeLabel;
  private JButton modifyButton;
  private JButton increaseFrequencyButton;
  private JButton reduceFrequencyButton;

  // private Socket cliente;

  private InetAddress host;
  private Integer port;
  private DatagramSocket socket;
  private DataOutputStream dataOutputStream;

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
    this.modifyButton = new JButton();
    this.increaseFrequencyButton = new JButton();
    this.reduceFrequencyButton = new JButton();

    timeLabel.setFont(new Font("Verdana", Font.PLAIN, 50));
    timeLabel.setForeground(Color.WHITE);
    timeLabel.setBackground(new Color(0x333272));
    timeLabel.setOpaque(true);
    setText();

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
                sendData();
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
    try {
      BufferedImage image;
      BufferedImage imageHover;
      image = ImageIO.read(new File("./resources/edit.png"));
      imageHover = ImageIO.read(new File("./resources/edit-hover.png"));
      modifyButton.setIcon(
          new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
      modifyButton.setPressedIcon(
          new ImageIcon(new ImageIcon(imageHover).getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
    } catch (IOException e) {
      e.printStackTrace();
    }

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
    try {
      BufferedImage image;
      BufferedImage imageHover;
      image = ImageIO.read(new File("./resources/increase.png"));
      imageHover = ImageIO.read(new File("./resources/increase-hover.png"));
      increaseFrequencyButton.setIcon(
          new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
      increaseFrequencyButton.setPressedIcon(
          new ImageIcon(new ImageIcon(imageHover).getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
    } catch (IOException e) {
      e.printStackTrace();
    }

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
    try {
      BufferedImage image;
      BufferedImage imageHover;
      image = ImageIO.read(new File("./resources/decrease.png"));
      imageHover = ImageIO.read(new File("./resources/decrease-hover.png"));
      reduceFrequencyButton.setIcon(
          new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
      reduceFrequencyButton.setPressedIcon(
          new ImageIcon(new ImageIcon(imageHover).getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
    } catch (IOException e) {
      e.printStackTrace();
    }

    add(timeLabel);
    add(modifyButton);
    add(reduceFrequencyButton);
    add(increaseFrequencyButton);
    if (addSendButon) {
      JButton sendDataButton = new JButton("Enviar");
      sendDataButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          System.out.println(sendData());
        }
      });
      add(sendDataButton);
    }
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

  // public void setCliente(Socket cliente) {
  // this.cliente = cliente;
  // try {
  // this.dataOutputStream = new DataOutputStream(cliente.getOutputStream());
  // sendData();
  // } catch (IOException e) {
  // e.printStackTrace();
  // Logger.getLogger(TimerPanel.class.getName()).log(Level.SEVERE, null, e);
  // }
  // }

  public Boolean sendData() {
    try {
      String hora = timeFormat.format(this.calendar.getTime());
      byte[] horaByte = hora.getBytes();
      System.out.println(hora);
      System.out.println(horaByte);
      DatagramPacket respuesta = new DatagramPacket(horaByte, horaByte.length, host, port);
      socket.send(respuesta);
      return true;
    } catch (IOException e) {
      Logger.getLogger(TimerPanel.class.getName()).log(Level.SEVERE, null, e);
      return false;
    }
  }

  public void setCliente(DatagramSocket socket, InetAddress host, Integer port) {
    this.host = host;
    this.port = port;
    this.socket = socket;
  }

}
