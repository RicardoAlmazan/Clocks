package mx.ipn.escom.clocks.server.gui;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import mx.ipn.escom.clocks.common.model.Libro;
import mx.ipn.escom.clocks.server.dao.LibroRepository;
import mx.ipn.escom.clocks.server.ldn.LibreriaRemote;

public class Container extends JFrame {
  /**
   *
   */
  private static final long serialVersionUID = 3864514692691142425L;
  private TimerPanel timer1;
  private JLabel portadaContainer;
  private BufferedImage portada;

  private Integer idSesion;

  private LibroRepository libroRepository;
  private LibreriaRemote libreriaRemote;

  private DatagramSocket server;

  public Container() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setTitle("PRÁCTICA 2");
    this.setLayout(new BorderLayout());
    this.setSize(600, 600);
    this.setResizable(false);
    this.setVisible(true);

    this.libroRepository = new LibroRepository();

    init();
  }

  private void init() {
    // Random rand = new Random();

    this.timer1 = new TimerPanel();
    Thread t1 = new Thread(timer1);
    this.add(timer1, BorderLayout.NORTH);

    try {
      this.idSesion = this.libroRepository.getNuevaSesion(timer1.getTime());
    } catch (SQLException e1) {
      e1.printStackTrace();
    }

    try {
      portada = ImageIO.read(new File("./resources/libros/books.jpg"));
      this.portadaContainer = new JLabel(new ImageIcon(portada));
      add(portadaContainer, BorderLayout.CENTER);
    } catch (IOException e) {
      e.printStackTrace();
    }

    
    try {
      libreriaRemote = new LibreriaRemote(this.libroRepository, this.portadaContainer);
      Registry reg = LocateRegistry.createRegistry(2405);
      reg.bind("libreria", libreriaRemote);
      System.out.println("SERVIDOR INICIADO");
    } catch (RemoteException | AlreadyBoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    t1.start();
    startServer();
  }

  private void startServer() {
    libreriaRemote.setIdSesion(idSesion);
    try {
      server = new DatagramSocket(2405);
      DatagramPacket dato = new DatagramPacket(new byte[100], 100);
      while (true) {
        System.out.println("Esperando cliente... ");
        server.receive(dato);
        // Libro libro = libroRepository.getLibroDisponible();
        // portada = ImageIO.read(new File(basePath + libro.getPortada()));
        // portadaContainer.setIcon(new ImageIcon(portada));
        System.out.println(
            "Recibido dato de " + dato.getAddress() + ":" + dato.getPort() + " : " + new String(dato.getData()));
        if (new String(dato.getData()).trim().equals("LIBRO")) {
          // System.out.println(libro);
          System.out.println("REGISTRAR PEDIDO");
        }
        // Socket cliente = server.accept();
        // System.out.println("Conexion establecida desde " + cliente.getInetAddress() +
        // ":" + cliente.getPort() + "No. Cliente: " + count);
        // if (count == 1) {
        // timer2.setCliente(cliente);
        // } else if (count == 2) {
        // timer3.setCliente(cliente);
        // } else if (count == 3) {
        // timer4.setCliente(cliente);
        // } else {
        // break;
        // }

        // if (count == 1) {
        // timer2.setCliente(server, dato.getAddress(), dato.getPort());
        // } else if (count == 2) {
        // timer3.setCliente(server, dato.getAddress(), dato.getPort());
        // } else if (count == 3) {
        // timer4.setCliente(server, dato.getAddress(), dato.getPort());
        // } else {
        // break;
        // }
      }
      // } catch (IOException | SQLException e) {
    } catch (IOException e) {
      e.printStackTrace();
      Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, e);
    }
  }
}
