

import javax.swing.*;
import java.awt.event.*;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import helper.DBConnection;
import helper.HashUtil;

public class LoginForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister;

    public LoginForm() {
        setTitle("Login");
        setSize(350, 200);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel lblUsername = new JLabel("Username:");
        JLabel lblPassword = new JLabel("Password:");

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();

        btnLogin = new JButton("Login");
        btnRegister = new JButton("Register");

        lblUsername.setBounds(30, 20, 100, 25);
        txtUsername.setBounds(120, 20, 170, 25);

        lblPassword.setBounds(30, 55, 100, 25);
        txtPassword.setBounds(120, 55, 170, 25);

        btnLogin.setBounds(120, 90, 80, 30);
        btnRegister.setBounds(210, 90, 100, 30);

        add(lblUsername); add(txtUsername);
        add(lblPassword); add(txtPassword);
        add(btnLogin); add(btnRegister);

        // Aksi tombol login
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        // Aksi tombol register
        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new RegisterForm().setVisible(true);
                dispose();
            }
        });
    }

    private void login() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        try {
            if (username.equals("admin") && password.equals("admin123")) {
                JOptionPane.showMessageDialog(this, "Login Admin berhasil!");
                new DashboardAdmin().setVisible(true);
                dispose();
                return;
            }

            MongoDatabase db = DBConnection.getDatabase();
            MongoCollection<Document> users = db.getCollection("users");

            Document user = users.find(new Document("username", username)).first();
            if (user != null) {
                String storedHash = user.getString("password");
                if (HashUtil.checkPassword(password, storedHash)) {
                    JOptionPane.showMessageDialog(this, "Login berhasil!");
                    new DashboardUser(username).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Password salah!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "User tidak ditemukan!");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}
