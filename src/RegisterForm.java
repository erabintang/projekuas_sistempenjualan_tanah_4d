

import javax.swing.*;
import java.awt.event.*;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import helper.DBConnection;
import helper.HashUtil;

public class RegisterForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnRegister, btnBack;

    public RegisterForm() {
        setTitle("Register");
        setSize(350, 200);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel lblUsername = new JLabel("Username:");
        JLabel lblPassword = new JLabel("Password:");

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();

        btnRegister = new JButton("Register");
        btnBack = new JButton("Kembali");

        lblUsername.setBounds(30, 20, 100, 25);
        txtUsername.setBounds(120, 20, 170, 25);

        lblPassword.setBounds(30, 55, 100, 25);
        txtPassword.setBounds(120, 55, 170, 25);

        btnRegister.setBounds(120, 90, 80, 30);
        btnBack.setBounds(210, 90, 90, 30);

        add(lblUsername); add(txtUsername);
        add(lblPassword); add(txtPassword);
        add(btnRegister); add(btnBack);

        btnRegister.addActionListener(e -> registerUser());
        btnBack.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });
    }

    private void registerUser() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }

        try {
            MongoDatabase db = DBConnection.getDatabase();
            MongoCollection<Document> users = db.getCollection("users");

            // Cek apakah username sudah dipakai
            Document existing = users.find(new Document("username", username)).first();
            if (existing != null) {
                JOptionPane.showMessageDialog(this, "Username sudah digunakan!");
                return;
            }

            // Hash password
            String hashed = HashUtil.hashPassword(password);

            Document newUser = new Document("username", username)
                    .append("password", hashed);

            users.insertOne(newUser);
            JOptionPane.showMessageDialog(this, "Registrasi berhasil!");

            new LoginForm().setVisible(true);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal daftar: " + ex.getMessage());
        }
    }
}
