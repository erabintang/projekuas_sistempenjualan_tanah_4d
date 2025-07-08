
import javax.swing.*;
import java.awt.event.*;

public class DashboardUser extends JFrame {

    private JButton btnLihatTanah, btnLogout;
    private String username;

    public DashboardUser(String username) {
        this.username = username;

        setTitle("Dashboard User - " + username);
        setSize(300, 150);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        btnLihatTanah = new JButton("Lihat Data Tanah");
        btnLogout = new JButton("Logout");

        btnLihatTanah.setBounds(50, 20, 180, 30);
        btnLogout.setBounds(100, 60, 100, 30);

        add(btnLihatTanah);
        add(btnLogout);

        btnLihatTanah.addActionListener(e -> {
         
            new TableViewerUser(username).setVisible(true);
      
        });

 

        btnLogout.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });
    }
}
