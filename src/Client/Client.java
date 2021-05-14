package Client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;

public class Client extends JFrame implements ActionListener {
    private JButton send, delete, exit, log_in, log_out;
    private JPanel jPanel_login, jPanel_chat;
    private JTextField nick_1, nick_2, message, inputIP;
    private JTextArea msg, online;
    private ImageIcon imageIcon;

    private Socket client;
    private DataStream dataStream;
    private DataOutputStream dos;
    private DataInputStream dis;

    public Client() {
        super("Client");
        imageIcon = new ImageIcon("src\\background.jpg");
        JLabel myLabel = new JLabel(imageIcon);
        myLabel.setSize(600,400);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
        setSize(600, 400);
        addItem();
        setVisible(true);
    }

    private void addItem() {
        setLayout(new BorderLayout());

        exit = new JButton("Exit");
        exit.addActionListener(this);
        send = new JButton("Send");
        send.addActionListener(this);
        delete = new JButton("Delete");
        delete.addActionListener(this);
        log_in = new JButton("Log in");
        log_in.addActionListener(this);
        log_out = new JButton("Exit");
        log_out.addActionListener(this);

        jPanel_chat = new JPanel();
        jPanel_chat.setLayout(new BorderLayout());

        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout(FlowLayout.LEFT));
        nick_1 = new JTextField(20);
        nick_1.setFont(new Font("Serif", Font.PLAIN, 15));
        JLabel account_0 = new JLabel("Account");
        account_0.setForeground(Color.BLUE);
        account_0.setFont(new Font("Serif", Font.PLAIN, 18));
        p1.add(account_0);
        p1.add(nick_1);
        p1.add(exit);

        JPanel p2 = new JPanel();
        p2.setLayout(new BorderLayout());

        JPanel p22 = new JPanel();
        p22.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel _onl = new JLabel("Online Listings");
        _onl.setForeground(Color.BLUE);
        _onl.setFont(new Font("Serif", Font.PLAIN, 18));
        p22.add(_onl);
        p2.add(p22, BorderLayout.NORTH);

        online = new JTextArea(10, 10);
        online.setEditable(false);
        online.setFont(new Font("Serif", Font.PLAIN, 15));
        p2.add(new JScrollPane(online), BorderLayout.CENTER);
        p2.add(new JLabel("     "), BorderLayout.SOUTH);
        p2.add(new JLabel("     "), BorderLayout.EAST);
        p2.add(new JLabel("     "), BorderLayout.WEST);

        msg = new JTextArea(10, 20);
        msg.setFont(new Font("Serif", Font.PLAIN, 20));
        msg.setEditable(false);

        JPanel p3 = new JPanel();
        p3.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel _msg = new JLabel("Message");
        _msg.setForeground(Color.BLUE);
        _msg.setFont(new Font("Serif", Font.PLAIN, 18));
        p3.add(_msg);
        message = new JTextField(25);
        message.setFont(new Font("Serif", Font.PLAIN, 18));
        p3.add(message);
        p3.add(send);
        p3.add(delete);

        jPanel_chat.add(new JScrollPane(msg), BorderLayout.CENTER);
        jPanel_chat.add(p1, BorderLayout.NORTH);
        jPanel_chat.add(p2, BorderLayout.EAST);
        jPanel_chat.add(p3, BorderLayout.SOUTH);
        jPanel_chat.add(new JLabel("     "), BorderLayout.WEST);

        jPanel_chat.setVisible(false);
        add(jPanel_chat, BorderLayout.CENTER);
        //-------------------------
        jPanel_login = new JPanel();
        jPanel_login.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel account_1 = new JLabel("Account");
        account_1.setForeground(Color.BLUE);
        account_1.setFont(new Font("Serif", Font.PLAIN, 20));
        jPanel_login.add(account_1);
        nick_2 = new JTextField(10);
        nick_2.setFont(new Font("Serif", Font.PLAIN, 20));
        JLabel input_ip = new JLabel("IP");
        input_ip.setForeground(Color.BLUE);
        input_ip.setFont(new Font("Serif", Font.PLAIN, 20));
        inputIP = new JTextField("localhost", 10);
        inputIP.setFont(new Font("Serif", Font.PLAIN, 20));
        jPanel_login.add(nick_2);
        jPanel_login.add(input_ip);
        jPanel_login.add(inputIP);
        jPanel_login.add(log_in);
        jPanel_login.add(log_out);

        add(jPanel_login, BorderLayout.NORTH);
    }

    private void go() {
        try {
                client = new Socket("localhost", 20);
                dos = new DataOutputStream(client.getOutputStream());
                dis = new DataInputStream(client.getInputStream());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this
                    , "Connection error, review the network cable or room is not open."
                    , "Warning!!!", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new Client().go();
    }

    private void sendMSG(String data) {
        try {
            dos.writeUTF(data);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getMSG() {
        String data = null;
        try {
            data = dis.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void getMSG(String msg1, String msg2) {
        int stt = Integer.parseInt(msg1);
        switch (stt) {
            case 3:
                this.msg.append(msg2);
                break;
            case 4:
                this.online.setText(msg2);
                break;
            case 5:
                dataStream.stopThread();
                exit();
                break;
            default:
                break;
        }
    }

    private void checkSend(String msg) {
        if (msg.compareTo("\n") != 0) {

            this.msg.append("Me: " + msg);
            sendMSG("1");
            sendMSG(msg);
        }
    }

    private boolean checkLogin(String nick) {
        if (nick.compareTo("") == 0)
            return false;
        else if (nick.compareTo("0") == 0) {
            return false;
        } else {
            sendMSG(nick);
            int sst = Integer.parseInt(getMSG());
            if (sst == 0)
                return false;
            else return true;
        }
    }

    private void exit() {
        try {
            sendMSG("0");
            dos.close();
            dis.close();
            client.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        System.exit(0);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exit) {
            dataStream.stopThread();
            exit();
        } else if (e.getSource() == delete) {
            message.setText("");
        } else if (e.getSource() == send) {
            checkSend(message.getText() + "\n");
            message.setText("");
        } else if (e.getSource() == log_in) {
            if (checkLogin(nick_2.getText())) {
                jPanel_chat.setVisible(true);
                jPanel_login.setVisible(false);
                nick_1.setText(nick_2.getText());
                nick_1.setEditable(false);
                this.setTitle(nick_2.getText());
                msg.append("Login successfully.\n");
                dataStream = new DataStream(this, this.dis);
            } else {
                JOptionPane.showMessageDialog(this, "This nick already exists.", "Warning!!!", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == log_out) {
            exit();
        }
    }


}
