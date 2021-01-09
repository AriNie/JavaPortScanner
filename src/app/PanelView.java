package app;

// import javax.swing.JButton;
// import javax.swing.JFrame;
// import javax.swing.JLabel;
// import javax.swing.JOptionPane;
// import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.Container;
import javax.swing.*;
import java.awt.GridLayout;
import java.util.regex.Pattern;
import java.util.ArrayList;

// import jdk.internal.joptsimple.internal.OptionNameMap;

/**
 * PannelView
 */
public class PanelView extends JFrame implements ActionListener {

  Timer timer;
  JLabel label;
  JProgressBar bar;
  String ip_add = "localhost";
  int timeout = 100;
  String result;
  Container c;
  JTextArea infoTextArea;

  public static void main(String[] args) {
    // JFrame frame = new JFrame("ポートスキャナー");
    PanelView frame = new PanelView("ポートスキャナー");
    frame.setSize(700, 600);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  PanelView(String title) {
    setTitle(title);
    setBounds(10, 10, 300, 200);
    // setLayout(new GridLayout(2, 1));

    label = new JLabel("");
    label.setHorizontalAlignment(JLabel.CENTER);

    JPanel mp = new JPanel();
    mp.setLayout(new GridLayout(2, 1));
    JTabbedPane t = new JTabbedPane();
    JPanel info = new JPanel();
    info.setLayout(new GridLayout(1, 2));
    JPanel TCPp = new JPanel();
    TCPp.setLayout(new GridLayout(5, 1));
    JPanel UDPp = new JPanel();
    UDPp.setLayout(new GridLayout(5, 1));
    JPanel Settingp = new JPanel();
    Settingp.setLayout(new GridLayout(4, 1));
    c = getContentPane();

    JButton portScanTCP_btn = new JButton("全ポートTCPスキャン");
    portScanTCP_btn.addActionListener(this);
    portScanTCP_btn.setVisible(true);
    portScanTCP_btn.setActionCommand("all_portScan_TCP");

    JButton portScanUDP_btn = new JButton("全ポートUDPスキャン");
    portScanUDP_btn.addActionListener(this);
    portScanUDP_btn.setVisible(true);
    portScanUDP_btn.setActionCommand("all_portScan_UDP");

    JButton rngScanTCP_btn = new JButton("範囲を指定してTCPスキャン");
    rngScanTCP_btn.addActionListener(this);
    rngScanTCP_btn.setVisible(true);
    rngScanTCP_btn.setActionCommand("rng_portScan_TCP");

    JButton rngScanUDP_btn = new JButton("範囲を指定してUDPスキャン");
    rngScanUDP_btn.addActionListener(this);
    rngScanUDP_btn.setVisible(true);
    rngScanUDP_btn.setActionCommand("rng_portScan_UDP");

    JButton port_selectedTCP = new JButton("ポート番号を指定してTCPスキャン");
    port_selectedTCP.addActionListener(this);
    port_selectedTCP.setVisible(true);
    port_selectedTCP.setActionCommand("selected_portScan_TCP");

    JButton port_selectedUDP = new JButton("ポート番号を指定してUDPスキャン");
    port_selectedUDP.addActionListener(this);
    port_selectedUDP.setVisible(true);
    port_selectedUDP.setActionCommand("selected_portScan_UDP");

    JButton ip_setting = new JButton("IPアドレスを設定する");
    ip_setting.addActionListener(this);
    ip_setting.setVisible(true);
    ip_setting.setActionCommand("set_ip");

    JButton timeo_setting = new JButton("タイムアウトする時間を設定する");
    timeo_setting.addActionListener(this);
    timeo_setting.setVisible(true);
    timeo_setting.setActionCommand("set_timeout");

    JButton reviewsetting = new JButton("設定状況を確認する");
    reviewsetting.addActionListener(this);
    reviewsetting.setVisible(true);
    reviewsetting.setActionCommand("rev_setting");

    infoTextArea = new JTextArea(30, 40);
    JScrollPane scrollpane = new JScrollPane(infoTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    scrollpane.setVisible(true);

    bar = new JProgressBar();
    bar.setValue(0);

    TCPp.add(portScanTCP_btn);
    UDPp.add(portScanUDP_btn);
    TCPp.add(port_selectedTCP);
    UDPp.add(port_selectedUDP);
    TCPp.add(rngScanTCP_btn);
    UDPp.add(rngScanUDP_btn);
    Settingp.add(ip_setting);
    Settingp.add(timeo_setting);
    Settingp.add(reviewsetting);
    t.addTab("TCP Scan", TCPp);
    t.addTab("UDP Scan", UDPp);
    t.addTab("Setting", Settingp);
    c.add(t);
    mp.add(t);
    mp.add(scrollpane);

    getContentPane().add(mp, BorderLayout.CENTER);
    // getContentPane().add(label, BorderLayout.PAGE_END);
    getContentPane().add(bar, BorderLayout.PAGE_END);
  }

  public void actionPerformed(ActionEvent e) {

    String cmd = e.getActionCommand();
    int option;
    int j = 1;
    timer = new Timer(65535, this);
    timer.setActionCommand("timer");
    ArrayList<Integer> openList = new ArrayList<Integer>();

    if (cmd.equals("all_portScan_TCP")) {
      option = JOptionPane.showConfirmDialog(this, "全ポートをTCPスキャンします。よろしいですか？");
      if (option == JOptionPane.YES_OPTION) {
        timer.start();
        for (int i = 0; i < 65535; i++) {
          result = Scanner.portScanner(ip_add, i, timeout);
          if (result.equals("Open")) {
            openList.add(i);
          }
          System.out.println("Port " + i + " is " + result);
          infoTextArea.append(ip_add + " - Port " + i + " is " + result + " [TCP全ポートスキャン]\n");
        }
        infoTextArea.append("[" + ip_add + " TCP全ポートスキャン]" + " Open Port:\n");
        for (int i = 0; i < openList.size(); i++) {
          infoTextArea.append(openList.get(i) + "\n");
        }
        infoTextArea.append("-------------------------------------------------------------------------------------\n");
      }

    } else if (cmd.equals("selected_portScan_TCP")) {
      String string_j = JOptionPane.showInputDialog("スキャンするポート番号を入力して下さい");

      try {
        while (!(isNumber(string_j))) {
          string_j = JOptionPane.showInputDialog("入力が不正です。\n再度スキャンするポート番号を入力して下さい");
        }
        j = Integer.valueOf(string_j);
        result = Scanner.portScanner(ip_add, j, timeout);
        System.out.println("Port " + j + " is " + result);
        infoTextArea.append(ip_add + " - Port " + j + " is " + result + " [TCPポート指定スキャン]\n");
        infoTextArea.append("-------------------------------------------------------------------------------------\n");
      } catch (Exception ex) {
        // TODO: handle exception
      }

    } else if (cmd.equals("all_portScan_UDP")) {
      option = JOptionPane.showConfirmDialog(this, "全ポートをUDPスキャンします。よろしいですか？");
      if (option == JOptionPane.YES_OPTION) {
        for (int i = 0; i < 65535; i++) {
          result = Scanner.portScannerUDP(ip_add, i);
          if (result.equals("Closed")) {
            openList.add(i);
          }
          System.out.println("Port " + i + " is " + result);
          infoTextArea.append(ip_add + " - Port " + i + " is " + result + " [UDP全ポートスキャン]\n");
        }
        infoTextArea.append("[" + ip_add + " TCP全ポートスキャン]" + " Closed Port:\n");
        for (int i = 0; i < openList.size(); i++) {
          infoTextArea.append(openList.get(i) + "\n");
        }
        infoTextArea.append("-------------------------------------------------------------------------------------\n");
      }

    } else if (cmd.equals("selected_portScan_UDP")) {
      String string_j = JOptionPane.showInputDialog("スキャンするポート番号を入力してください");
      try {
        while (!(isNumber(string_j))) {
          string_j = JOptionPane.showInputDialog("入力が不正です。\n再度スキャンするポート番号を入力してください");
        }
        j = Integer.valueOf(string_j);
        result = Scanner.portScannerUDP(ip_add, j);
        System.out.println("Port " + j + " is " + result);
        infoTextArea.append(ip_add + " - Port " + j + " is " + result + " [UDPポート指定スキャン]\n");
        infoTextArea.append("-------------------------------------------------------------------------------------\n");
      } catch (Exception ex) {
        // TODO: handle exception
      }

    } else if (cmd.equals("rng_portScan_TCP")) {
      String innum;
      String endnum;

      try {
        innum = JOptionPane.showInputDialog("スキャンを開始するポート番号を入力してください");
        while (!(isNumber(innum))) {
          innum = JOptionPane.showInputDialog("入力が不正です。\n再度スキャンを開始するポート番号を入力してください");
        }
        endnum = JOptionPane.showInputDialog("スキャンを終了するポート番号を入力してください");
        while (!(isNumber(endnum))) {
          endnum = JOptionPane.showInputDialog("入力が不正です。\n再度スキャンを終了するポート番号を入力してください");
        }
        for (int i = Integer.valueOf(innum); i <= Integer.valueOf(endnum); i++) {
          result = Scanner.portScanner(ip_add, i, timeout);
          if (result.equals("Open")) {
            openList.add(i);
          }
          System.out.println("Port " + i + " is " + result);
          infoTextArea.append(ip_add + " - Port " + i + " is " + result + " [TCP範囲指定スキャン]\n");
        }
        infoTextArea.append("[" + ip_add + " TCP全ポートスキャン]" + " Open Port:\n");
        for (int i = 0; i < openList.size(); i++) {
          infoTextArea.append(openList.get(i) + "\n");
        }
        infoTextArea.append("-------------------------------------------------------------------------------------\n");
      } catch (Exception ex) {
        // TODO: handle exception
      }

    } else if (cmd.equals("rng_portScan_UDP")) {
      String innum;
      String endnum;

      try {
        innum = JOptionPane.showInputDialog("スキャンを開始するポート番号を入力してください");
        while (!(isNumber(innum))) {
          innum = JOptionPane.showInputDialog("入力が不正です。\n再度スキャンを開始するポート番号を入力してください");
        }
        endnum = JOptionPane.showInputDialog("スキャンを終了するポート番号を入力してください");
        while (!(isNumber(endnum))) {
          endnum = JOptionPane.showInputDialog("入力が不正です。\n再度スキャンを終了するポート番号を入力してください");
        }
        for (int i = Integer.valueOf(innum); i <= Integer.valueOf(endnum); i++) {
          result = Scanner.portScannerUDP(ip_add, i);
          if (result.equals("Closed")) {
            openList.add(i);
          }
          System.out.println("Port " + i + " is " + result);
          infoTextArea.append(ip_add + " - Port " + i + " is " + result + " [UDP範囲指定スキャン]\n");
        }
        infoTextArea.append("[" + ip_add + " TCP全ポートスキャン]" + " Closed Port:\n");
        for (int i = 0; i < openList.size(); i++) {
          infoTextArea.append(openList.get(i) + "\n");
        }
        infoTextArea.append("-------------------------------------------------------------------------------------\n");
      } catch (Exception ex) {
        // TODO: handle exception
      }

    } else if (cmd.equals("set_ip")) {
      String costom_ip;

      try {
        costom_ip = JOptionPane.showInputDialog("スキャンするIPアドレスを入力してください");
        while (!(isIPv4(costom_ip))) {
          costom_ip = JOptionPane.showInputDialog("IPアドレスが不正です。\n再度、スキャンするIPアドレスを入力してください");
        }
        ip_add = costom_ip;

      } catch (Exception ex) {
        // TODO: handle exception
      }
    } else if (cmd.equals("set_timeout")) {
      String costom_timeo;

      try {
        costom_timeo = JOptionPane.showInputDialog("スキャンする際のタイムアウト時間を入力してください。");
        while ((!(isNumber(costom_timeo))) || (Integer.parseInt(costom_timeo) > 100000000)
            || (Integer.parseInt(costom_timeo) < 0)) {
          costom_timeo = JOptionPane
              .showInputDialog("入力が不正です。\n再度タイムアウト時間を入力してください。\n\n設定されるタイムアウト時間を100000000以下かつ正の数にしてください。");
        }
        timeout = Integer.parseInt(costom_timeo);
      } catch (Exception ex) {
        // TODO: handle exception
      }
    } else if (cmd.equals("rev_setting")) {
      JLabel mes = new JLabel("IPアドレス: " + ip_add + ", タイムアウト時間: " + timeout);
      JOptionPane.showMessageDialog(this, mes);
    }
  }

  public boolean isNumber(String num) {
    if (num.equals(null)) {
      return true;
    }
    try {
      Integer.parseInt(num);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public static boolean isIPv4(String str) {
    if (str.equals("localhost")) {
      return true;
    }
    return Pattern.matches("((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])([.](?!$)|$)){4}", str);
  }
}
