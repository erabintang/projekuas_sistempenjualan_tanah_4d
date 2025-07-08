import javax.swing.*;
import java.awt.event.*;

public class DashboardAdmin extends JFrame {
    private JButton btnKelolaTanah, btnLogout;

    public DashboardAdmin() {
        setTitle("Dashboard Admin");
        setSize(300, 150);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        btnKelolaTanah = new JButton("Kelola Data Tanah");
        btnLogout = new JButton("Logout");

        btnKelolaTanah.setBounds(50, 20, 180, 30);
        btnLogout.setBounds(100, 60, 100, 30);

        add(btnKelolaTanah);
        add(btnLogout);

        btnKelolaTanah.addActionListener(e -> {
            new FormTanah(true).setVisible(true); 
            dispose(); // agar dashboard tertutup saat form tanah dibuka
        });

        btnLogout.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });
    }
}
