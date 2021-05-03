package mx.ipn.escom.clocks.server.ldn;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import mx.ipn.escom.clocks.common.model.Libro;
import mx.ipn.escom.clocks.common.model.Pedido;
import mx.ipn.escom.clocks.common.rmi.Libreria;
import mx.ipn.escom.clocks.server.dao.LibroRepository;

public class LibreriaRemote extends UnicastRemoteObject implements Libreria {

  private LibroRepository libroRepository;
  private JLabel portadaContainer;
  private Integer idSesion;

  private static final String basePath = "./resources/libros/";

  public LibreriaRemote(LibroRepository libroRepository, JLabel portadaContainer) throws RemoteException {
    this.libroRepository = libroRepository;
    this.portadaContainer = portadaContainer;
  }

  public Integer getIdSesion() {
    return idSesion;
  }

  public void setIdSesion(Integer idSesion) {
    this.idSesion = idSesion;
  }

  @Override
  public Boolean reiniciarSesion() throws RemoteException {
    System.out.println("REINICAR");
    try {
      System.out.println(getClientHost());
    } catch (ServerNotActiveException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return true;
  }

  @Override
  public Libro solicitarLibro(String fechaPedido) throws RemoteException {
    try {
      Libro libro = libroRepository.getLibroDisponible(idSesion);
      if (libro == null)
        return null;

      Pedido pedido = new Pedido();
      pedido.setIp(getClientHost());
      pedido.setIdLibro(libro.getISBN());
      pedido.setIdSesion(idSesion);
      pedido.setFecha(fechaPedido);
      libroRepository.insertLibroPedido(pedido);

      BufferedImage portada = ImageIO.read(new File(basePath + libro.getPortada()));
      portadaContainer.setIcon(new ImageIcon(portada));

      return libro;
    } catch (SQLException | IOException | ServerNotActiveException e) {
      e.printStackTrace();
    }
    return null;
  }

}
