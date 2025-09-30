/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Tampilan;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import koneksi.koneksi;

/**
 *
 * @author dennis adzua firdaus
 */
public class Diagnosa extends javax.swing.JFrame {
private Connection conn = new koneksi().connect();
private DefaultTableModel tabmode;
private Statement stat;
private ResultSet rs;
    /**
     * Creates new form Diagnosa
     */
    public Diagnosa() {
        initComponents();
        setLocationRelativeTo(null);
        autonumber();
        tampilPasien();
        tampilGejala();
    }
    
protected void autonumber() {
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery("SELECT MAX(id_diagnosa) FROM diagnosa");
            if (rs.next()) {
                String id = rs.getString(1);
                if (id == null) {
                    txtIdDiagnosa.setText("D001");
                } else {
                    int urutan = Integer.parseInt(id.substring(1)) + 1;
                    txtIdDiagnosa.setText(String.format("D%03d", urutan));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Auto Number Error: " + e);
        }
    }
 
    protected void tampilPasien() {
        try {
            String sql = "SELECT NamaPasien FROM data_pasien";
            stat = conn.createStatement();
            rs = stat.executeQuery(sql);
            while (rs.next()) {
                txtnamapasien.addItem(rs.getString("NamaPasien"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal tampil pasien: " + e);
        }
    }
    
   
    protected void tampilGejala() {
        tabmode = new DefaultTableModel(
                new Object[]{"Daftar Gejala", "Pilih"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 1 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };
        tblPilihGejala.setModel(tabmode);

        try {
            String sql = "SELECT Nama_Gejala FROM data_gejala";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String pertanyaan = "Apakah Pasien Mengalami " + rs.getString("Nama_Gejala") + "?";
                tabmode.addRow(new Object[]{pertanyaan, false});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Load Gejala Error: " + e);
        }
    }

    
    private void prosesDiagnosa() {
        try {
            String idDiagnosa = txtIdDiagnosa.getText();
            String namaPasien = txtnamapasien.getSelectedItem().toString();

            // Ambil gejala yang dipilih
            List<String> kodeGejalaDipilih = new ArrayList<>();
            for (int i = 0; i < tblPilihGejala.getRowCount(); i++) {
                Boolean isSelected = (Boolean) tblPilihGejala.getValueAt(i, 1);
                if (Boolean.TRUE.equals(isSelected)) {
                    // Ambil teks pertanyaan dari kolom tabel
                    String gejalaTeks = tblPilihGejala.getValueAt(i, 0).toString();

                    // Hapus awalan "Apakah anjing mengalami " dan tanda tanya
                   String namaGejala = gejalaTeks.replace("Apakah Pasien Mengalami ", "").replace("?", "");

                    // Ambil kode gejala berdasarkan nama_gejala
                    String sql = "SELECT Id_Gejala FROM data_gejala WHERE Nama_Gejala = ?";
                    PreparedStatement pstg = conn.prepareStatement(sql);
                    pstg.setString(1, namaGejala);
                    ResultSet rsg = pstg.executeQuery();
                    if (rsg.next()) {
                        kodeGejalaDipilih.add(rsg.getString("Id_Gejala"));
                    }
                }
            }

            // Mencari penyakit berdasarkan rule forward chaining
            Map<String, Integer> penyakitCounter = new HashMap<>();
            for (String kodeGejala : kodeGejalaDipilih) {
                String sql = "SELECT idpenyakit FROM data_rules WHERE Id_Gejala = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, kodeGejala);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    String kodePenyakit = rs.getString("idpenyakit");
                    penyakitCounter.put(kodePenyakit, penyakitCounter.getOrDefault(kodePenyakit, 0) + 1);
                }
            }

            // Menentukan hasil diagnosa
            String hasilDiagnosa = "";
            String solusi = "";
            if (!penyakitCounter.isEmpty()) {
                // Ambil penyakit dengan jumlah gejala terbanyak yang cocok
                String kodeTerbanyak = Collections.max(penyakitCounter.entrySet(), Map.Entry.comparingByValue()).getKey();

                // Ambil data penyakit
                String sql = "SELECT Nama_Penyakit, Solusi FROM data_penyakit WHERE Id_Penyakit = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, kodeTerbanyak);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    hasilDiagnosa = rs.getString("Nama_Penyakit");
                    solusi = rs.getString("Solusi");
                }

                // Simpan ke tabel diagnosa
                String insert = "INSERT INTO diagnosa (Id_Diagnosa, Nama_pasien, kode_penyakit, hasil_diagnosa, solusi) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(insert);
                ps.setString(1, idDiagnosa);
                ps.setString(2, namaPasien);
                ps.setString(3, kodeTerbanyak);
                ps.setString(4, hasilDiagnosa);
                ps.setString(5, solusi);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Diagnosa berhasil disimpan:\n" + hasilDiagnosa + "\nsolusi: " + solusi);
                autonumber();
                tampilGejala();
            } else {
                JOptionPane.showMessageDialog(this, "Tidak ditemukan penyakit dari gejala yang dipilih.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Proses diagnosa gagal: " + e);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jbSelesai = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtIdDiagnosa = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jbAnalisis = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPilihGejala = new javax.swing.JTable();
        txtnamapasien = new javax.swing.JComboBox<>();
        jbSelesai1 = new javax.swing.JButton();

        jbSelesai.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jbSelesai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit-door.png"))); // NOI18N
        jbSelesai.setText("Kembali");
        jbSelesai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSelesaiActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 0, 51));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Diagnosa");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(443, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(427, 427, 427))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(102, 0, 102));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/mask.png"))); // NOI18N
        jLabel5.setText("Id Diagnosa");

        txtIdDiagnosa.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        txtIdDiagnosa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdDiagnosaActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/mask.png"))); // NOI18N
        jLabel6.setText("Nama Pasien");

        jbAnalisis.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jbAnalisis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/diagnose.png"))); // NOI18N
        jbAnalisis.setText("Analisis");
        jbAnalisis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAnalisisActionPerformed(evt);
            }
        });

        tblPilihGejala.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        tblPilihGejala.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Pilih", "Nama Gejala"
            }
        ));
        jScrollPane1.setViewportView(tblPilihGejala);

        txtnamapasien.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));

        jbSelesai1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jbSelesai1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit-door.png"))); // NOI18N
        jbSelesai1.setText("Kembali");
        jbSelesai1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSelesai1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1001, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6))
                                .addGap(53, 53, 53)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtnamapasien, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtIdDiagnosa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jbAnalisis, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(140, 140, 140)
                        .addComponent(jbSelesai1)
                        .addGap(41, 41, 41))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtIdDiagnosa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtnamapasien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(65, 65, 65)
                .addComponent(jbSelesai1)
                .addGap(121, 121, 121)
                .addComponent(jbAnalisis)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbAnalisisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAnalisisActionPerformed
       prosesDiagnosa();
    
    }//GEN-LAST:event_jbAnalisisActionPerformed

    private void txtIdDiagnosaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdDiagnosaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdDiagnosaActionPerformed

    private void jbSelesaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSelesaiActionPerformed
        this.dispose();
        new MenuUtama().setVisible(true);
    }//GEN-LAST:event_jbSelesaiActionPerformed

    private void jbSelesai1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSelesai1ActionPerformed
        this.dispose();
        new MenuUtama().setVisible(true);
    }//GEN-LAST:event_jbSelesai1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Diagnosa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Diagnosa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Diagnosa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Diagnosa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Diagnosa().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbAnalisis;
    private javax.swing.JButton jbSelesai;
    private javax.swing.JButton jbSelesai1;
    private javax.swing.JTable tblPilihGejala;
    private javax.swing.JTextField txtIdDiagnosa;
    private javax.swing.JComboBox<String> txtnamapasien;
    // End of variables declaration//GEN-END:variables
}
