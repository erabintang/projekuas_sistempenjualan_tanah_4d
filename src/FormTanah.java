
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import helper.DBConnection;

public class FormTanah extends JFrame {

    private JTextField txtId, txtLokasi, txtLuas, txtHarga, txtTelepon;
    private JComboBox<String> cmbJenis, cmbStatus;
    private JButton btnSimpan, btnHapus, btnEdit;
    private JTable tabelTanah;
    private DefaultTableModel tableModel;
    private JLabel lblJam;
    private boolean isAdmin;

    public FormTanah(boolean isAdmin) {
        this.isAdmin = isAdmin;
        setTitle("Form Input Tanah");
        setSize(950, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        // Label dan Input
        JLabel lblId = new JLabel("ID:");
        JLabel lblLokasi = new JLabel("Lokasi:");
        JLabel lblLuas = new JLabel("Luas:");
        JLabel lblHarga = new JLabel("Harga:");
        JLabel lblJenis = new JLabel("Jenis:");
        JLabel lblStatus = new JLabel("Status:");
        JLabel lblTelp = new JLabel("Telepon:");

        txtId = new JTextField();
        txtLokasi = new JTextField();
        txtLuas = new JTextField();
        txtHarga = new JTextField();
        txtTelepon = new JTextField();
        cmbJenis = new JComboBox<>(new String[]{"Perumahan", "Komersial", "Pertanian"});
        cmbStatus = new JComboBox<>(new String[]{"Tersedia", "Terjual"});

        btnSimpan = new JButton("Simpan");
        btnEdit = new JButton("Edit");
        btnHapus = new JButton("Hapus");

        lblId.setBounds(20, 20, 100, 25);
        txtId.setBounds(120, 20, 200, 25);
        lblLokasi.setBounds(20, 50, 100, 25);
        txtLokasi.setBounds(120, 50, 200, 25);
        lblLuas.setBounds(20, 80, 100, 25);
        txtLuas.setBounds(120, 80, 200, 25);
        lblHarga.setBounds(20, 110, 100, 25);
        txtHarga.setBounds(120, 110, 200, 25);
        lblJenis.setBounds(20, 140, 100, 25);
        cmbJenis.setBounds(120, 140, 200, 25);
        lblStatus.setBounds(20, 170, 100, 25);
        cmbStatus.setBounds(120, 170, 200, 25);
        lblTelp.setBounds(20, 200, 100, 25);
        txtTelepon.setBounds(120, 200, 200, 25);
        btnSimpan.setBounds(20, 240, 90, 30);
        btnEdit.setBounds(120, 240, 90, 30);
        btnHapus.setBounds(230, 240, 90, 30);

        add(lblId);
        add(txtId);
        add(lblLokasi);
        add(txtLokasi);
        add(lblLuas);
        add(txtLuas);
        add(lblHarga);
        add(txtHarga);
        add(lblJenis);
        add(cmbJenis);
        add(lblStatus);
        add(cmbStatus);
        add(lblTelp);
        add(txtTelepon);
        add(btnSimpan);
        add(btnEdit);
        add(btnHapus);

        lblJam = new JLabel();
        lblJam.setFont(new Font("Monospaced", Font.BOLD, 16));
        lblJam.setBounds(20, 280, 200, 30);
        add(lblJam);
        Timer timer = new Timer(1000, e -> updateJam());
        timer.start();

        tableModel = new DefaultTableModel(new String[]{"ID", "Lokasi", "Luas", "Harga", "Jenis", "Status", "Telepon", "Tanggal"}, 0);
        tabelTanah = new JTable(tableModel);

        JScrollPane scroll = new JScrollPane(tabelTanah);
        scroll.setBounds(350, 20, 560, 470);
        add(scroll);

        // Events
        btnSimpan.addActionListener(e -> simpanData());
        btnHapus.addActionListener(e -> hapusData());
        btnEdit.addActionListener(e -> editData());

        tabelTanah.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tabelTanah.getSelectedRow();
                if (row >= 0) {
                    txtId.setText(tableModel.getValueAt(row, 0).toString());
                    txtLokasi.setText(tableModel.getValueAt(row, 1).toString());
                    txtLuas.setText(tableModel.getValueAt(row, 2).toString());
                    txtHarga.setText(tableModel.getValueAt(row, 3).toString().replace("Rp", "").replace(".", "").replace(",", "").trim());
                    cmbJenis.setSelectedItem(tableModel.getValueAt(row, 4).toString());
                    cmbStatus.setSelectedItem(tableModel.getValueAt(row, 5).toString());
                    txtTelepon.setText(tableModel.getValueAt(row, 6).toString());

                    if (!isAdmin) {
                        handlePesanTanah(row);
                    }
                }
            }
        });

        tampilData();
    }

    private void simpanData() {
        try {
            String id = txtId.getText();
            String lokasi = txtLokasi.getText();
            double luas = Double.parseDouble(txtLuas.getText());
            double harga = Double.parseDouble(txtHarga.getText());
            String jenis = cmbJenis.getSelectedItem().toString();
            String status = cmbStatus.getSelectedItem().toString();
            String telepon = txtTelepon.getText();
            String tanggal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            MongoDatabase db = DBConnection.getDatabase();
            MongoCollection<Document> tanahCol = db.getCollection("tanah");

            Document doc = new Document("id", id)
                    .append("lokasi", lokasi)
                    .append("luas", luas)
                    .append("harga", harga)
                    .append("jenis", jenis)
                    .append("status", status)
                    .append("telepon", telepon)
                    .append("tanggal", tanggal);

            tanahCol.insertOne(doc);
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
            tampilData();
            clearForm();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan: " + ex.getMessage());
        }
    }

    private void hapusData() {
        int row = tabelTanah.getSelectedRow();
        if (row >= 0) {
            String id = tableModel.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ID: " + id + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                MongoDatabase db = DBConnection.getDatabase();
                MongoCollection<Document> tanahCol = db.getCollection("tanah");
                tanahCol.deleteOne(new Document("id", id));
                tampilData();
                clearForm();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih data dulu.");
        }
    }

    private void editData() {
        int row = tabelTanah.getSelectedRow();
        if (row >= 0) {
            try {
                String id = txtId.getText();
                String lokasi = txtLokasi.getText();
                double luas = Double.parseDouble(txtLuas.getText());
                double harga = Double.parseDouble(txtHarga.getText());
                String jenis = cmbJenis.getSelectedItem().toString();
                String status = cmbStatus.getSelectedItem().toString();
                String telepon = txtTelepon.getText();

                MongoDatabase db = DBConnection.getDatabase();
                MongoCollection<Document> tanahCol = db.getCollection("tanah");

                tanahCol.updateOne(new Document("id", id),
                        new Document("$set", new Document("lokasi", lokasi)
                                .append("luas", luas)
                                .append("harga", harga)
                                .append("jenis", jenis)
                                .append("status", status)
                                .append("telepon", telepon)
                        ));

                JOptionPane.showMessageDialog(this, "Data berhasil diupdate!");
                tampilData();
                clearForm();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal mengedit: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit.");
        }
    }

    private void tampilData() {
        try {
            tableModel.setRowCount(0);
            MongoDatabase db = DBConnection.getDatabase();
            MongoCollection<Document> tanahCol = db.getCollection("tanah");

            for (Document doc : tanahCol.find()) {
                String id = doc.getString("id");
                String lokasi = doc.getString("lokasi");
                Object luas = doc.get("luas");
                Object hargaObj = doc.get("harga");
                String hargaFormat;

                if (hargaObj != null) {
                    double harga = ((Number) hargaObj).doubleValue();
                    hargaFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(harga);
                } else {
                    hargaFormat = "Rp 0";
                }

                tableModel.addRow(new Object[]{
                    id,
                    lokasi,
                    luas,
                    hargaFormat,
                    doc.getString("jenis"),
                    doc.getString("status"),
                    doc.getString("telepon"),
                    doc.getString("tanggal")
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menampilkan data: " + ex.getMessage());
        }
    }

    private void handlePesanTanah(int row) {
        String status = tableModel.getValueAt(row, 5).toString();
        if (status.equalsIgnoreCase("Tersedia")) {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin memesan tanah ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String id = tableModel.getValueAt(row, 0).toString();
                MongoDatabase db = DBConnection.getDatabase();
                MongoCollection<Document> tanahCol = db.getCollection("tanah");
                tanahCol.updateOne(new Document("id", id), new Document("$set", new Document("status", "Dipesan")));
                JOptionPane.showMessageDialog(this, "Tanah berhasil dipesan!");
                tampilData();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Tanah tidak tersedia.");
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtLokasi.setText("");
        txtLuas.setText("");
        txtHarga.setText("");
        txtTelepon.setText("");
        cmbJenis.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
    }

    private void updateJam() {
        String jam = new SimpleDateFormat("HH:mm:ss").format(new Date());
        lblJam.setText("🕒 " + jam);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormTanah(true).setVisible(true)); // true = admin
    }
}
