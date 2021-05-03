package mx.ipn.escom.clocks.common.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import mx.ipn.escom.clocks.common.model.Libro;

public interface Libreria extends Remote{
  public Libro solicitarLibro(String fechaPedido) throws RemoteException;
  public Boolean reiniciarSesion() throws RemoteException;

}
