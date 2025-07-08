import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import helper.DBConnection;
import java.awt.LayoutManager;
import java.util.Vector;

public class FormLihatTanah extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public FormLihatTanah() {
        setTitle("Daftar Tanah");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout((LayoutManager) new JScrollPane());

        model = new DefaultTableModel(new String[]{
            "ID", "Lokasi", "Luas (m²)", "Harga (Rp)", "Jenis", "Status"
        }, 0);

        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        add(scroll);

        tampilData();
    }

    private void tampilData() {
        model.setRowCount(0);
        MongoDatabase db = DBConnection.getDatabase();
        MongoCollection<Document> tanahCol = db.getCollection("tanah");

        for (Document doc : tanahCol.find()) {
            Vector<Object> row = new Vector<>();
            row.add(doc.getString("id"));
            row.add(doc.getString("lokasi"));
            row.add(doc.get("luas").toString());
            row.add(doc.get("harga").toString());
            row.add(doc.getString("jenis"));
            row.add(doc.getString("status"));
            model.addRow(row);
        }
    }
}
