import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


//使用网络编程完成数据传输tcp udp
public class ServerChatMain extends JFrame implements ActionListener, KeyListener {
    public static void main(String[] args) {
        new ServerChatMain();
    }

    private JTextArea jta;
    private JScrollPane jsp;
    private JPanel jp;
    private JTextField jtf;
    private JButton jb;
    //输出流
    private BufferedWriter bw = null;

    public ServerChatMain() {
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

        this.setTitle("聊天服务器");
        this.setSize(300, 300);
        this.setLocation(300, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        /*******tcp服务端********/
        jb.addActionListener(this);
        //回车发送
        jtf.addKeyListener(this);
        try{

            //创建一个服务端套接字
            ServerSocket serverSocket = new ServerSocket(8888);
            //等待客户端连接
            Socket socket = serverSocket.accept();
            //获取socket通道的输入流(如何读取数据？？一行一行读) BufferedReader->readline();
            //InputStream in = socket.getInputStream();
            //在读取发过来的内容
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //获取socket通道的输出流（实现写出数据，也是写一行换一行）BufferedWriter->newLine();
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //循环读取，以防死循环
            String line = null;
            while((line = br.readLine()) != null) {
                //将读取的数据拼接到文本域中显示
                //System.lineSeparator()是换行的意思
                jta.append(line + System.lineSeparator());
            }
            //关闭socket通道
            serverSocket.close();


        }catch (IOException e) {
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
        //System.out.println(e);
        if(e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            sendDataToSocket();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    //将数据发送到通道中
    private void sendDataToSocket() {
        //System.out.println("点击了");
        String text = jtf.getText();
        text = "服务端对客户端说：" + text;
        jta.append(text + System.lineSeparator());
        try {
            //发送
            bw.write(text);
            //换行
            //newline和System.lineSeparator()两个换行有冲突
            bw.newLine();
            //刷新
            bw.flush();
            jtf.setText("");
        }catch (Exception e1){
            e1.printStackTrace();
        }
    }
}
