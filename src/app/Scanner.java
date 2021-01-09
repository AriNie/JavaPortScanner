package app;

import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Scanner
 */
public class Scanner {

  public static String portScanner(String ip, int port, int timeOut) {
    try {
      Socket s = new Socket();
      s.connect(new InetSocketAddress(ip, port), timeOut);
      s.close();
      return "Open";
    } catch (Exception e) {
      // TODO: handle exception
      return "Closed";
    }
  }

  public static String portScannerUDP(String ip, int port) {
    // InetAddress addy;
    // try {
    // addy = InetAddress.getByName(ip);
    // } catch (Exception e) {
    // // TODO: handle exception
    // return "Closed";
    // }
    // String udpTex = "Hello World.";
    // byte[] udpMes = udpTex.getBytes();

    // try {
    // DatagramPacket dp = new DatagramPacket(udpMes, udpMes.length, addy, port);
    // DatagramSocket sender = new DatagramSocket();
    // sender.send(dp);
    // dp = new DatagramPacket(udpMes, udpMes.length);
    // sender.receive(dp);
    // String received = String.valueOf(dp.getData());
    // System.out.println("Received: " + received);
    // sender.close();
    // return "Open";
    // } catch (Exception e) {
    // // TODO: handle exception
    // return "Closed";
    // }
    try {
      // the next line will fail and drop into the catch block if
      // there is already a server running on port i
      InetAddress adr = InetAddress.getByName(ip);
      DatagramSocket server = new DatagramSocket(port, adr);
      server.close();
    } catch (SocketException ex) {
      return "Closed";
    } catch (UnknownHostException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return "Closed";
    }
    return "Open";
  }
}
