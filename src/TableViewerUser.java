import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import helper.DBConnection;

public class TableViewerUser extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private String username;

    public TableViewerUser(String username) {
        this.username = username;
        setTitle("Daftar Tanah untuk User");
        setSize(800, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{"ID", "Lokasi", "Luas", "Harga", "Jenis", "Status", "Telepon", "Tanggal"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        loadData();

        // Tambahkan fitur klik untuk memesan tanah
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    String status = model.getValueAt(row, 5).toString();
                    if (status.equalsIgnoreCase("Tersedia")) {
                        int confirm = JOptionPane.showConfirmDialog(
                            TableViewerUser.this,
                            "Apakah Anda yakin ingin memesan tanah ini?",
                            "Konfirmasi Pesan",
                            JOptionPane.YES_NO_OPTION
                        );

                        if (confirm == JOptionPane.YES_OPTION) {
                            String id = model.getValueAt(row, 0).toString();

                            try {
                                MongoDatabase db = DBConnection.getDatabase();
                                MongoCollection<Document> tanahCol = db.getCollection("tanah");

                                tanahCol.updateOne(
                                    new Document("id", id),
                                    new Document("$set", new Document("status", "Dipesan"))
                                );

                                JOptionPane.showMessageDialog(TableViewerUser.this, "Tanah berhasil dipesan!");
                                reloadData(); // refresh tabel
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(TableViewerUser.this, "Gagal memesan: " + ex.getMessage());
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(TableViewerUser.this, "Tanah tidak tersedia untuk dipesan.");
                    }
                }
            }
        });
    }

    private void loadData() {
        model.setRowCount(0); // Kosongkan dulu tabel
        try {
            MongoDatabase db = DBConnection.getDatabase();
            MongoCollection<Document> tanahCol = db.getCollection("tanah");

            for (Document doc : tanahCol.find()) {
                String status = doc.getString("status");
                if (!"Terjual".equalsIgnoreCase(status)) {
                    model.addRow(new Object[]{
                        doc.getString("id"),
                        doc.getString("lokasi"),
                        doc.get("luas"),
                        doc.get("harga"),
                        doc.getString("jenis"),
                        doc.getString("status"),
                        doc.getString("telepon"),
                        doc.getString("tanggal")
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage());
        }
    }

    private void reloadData() {
        loadData(); // sama dengan loadData
    }
}
