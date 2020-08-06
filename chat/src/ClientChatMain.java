import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;

public class ClientChatMain extends JFrame implements ActionListener, KeyListener {
    public static void main(String[] args) {
        new ClientChatMain();
    }

    private JTextArea jta;
    private JScrollPane jsp;
    private JPanel jp;
    private JTextField jtf;
    private JButton jb;

    private BufferedWriter bw = null;

    public ClientChatMain() {
        jta = new JTextArea();
        jta.setEditable(false);
        jsp = new JScrollPane(jta);
        jp = new JPanel();
        jtf = new JTextField(10);
        jb = new JButton("发送");
        jp.add(jtf);
        jp.add(jb);

        this.add(jsp, BorderLayout.CENTER);
        this.add(jp, BorderLayout.SOUTH);

        this.setTitle("聊天客户端");
        this.setSize(300, 300);
        this.setLocation(600, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        /*******tcp客户端********/
        jb.addActionListener(this);
        //回车发送
        jtf.addKeyListener(this);
        try{
            //创建一个客户端套接字，在创建客户端套接字的时候就在尝试连接
            Socket socket = new Socket("127.0.0.1", 8888);
            //获取socket通道的输入流
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //获取socket通道的输出流
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //循环读取，以防死循环
            String line = null;
            while ((line = br.readLine()) != null) {
                jta.append(line + System.lineSeparator());
            }

            //关闭socket通道
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        /*******tcp服务端end********/
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        sendDataToSocket();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            sendDataToSocket();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void sendDataToSocket() {
        String text = jtf.getText();
        text = "客户端对服务端说：" + text;
        jta.append(text + System.lineSeparator());
        try {
            bw.write(text);
            bw.newLine();
            bw.flush();
            jtf.setText("");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
