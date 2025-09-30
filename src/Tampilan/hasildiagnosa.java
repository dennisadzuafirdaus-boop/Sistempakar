/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Tampilan;
import java.awt.event.KeyEvent;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import koneksi.koneksi;

/**
 *
 * @author dennis adzua firdaus
 */
public class hasildiagnosa extends javax.swing.JFrame {
private Connection conn = new koneksi().connect();
private PreparedStatement pst;
private ResultSet rs;
private DefaultTableModel tabmode;

    /**
     * Creates new form hasildiagnosa
     */
    public hasildiagnosa() {
        initComponents();
         setLocationRelativeTo(null);
        tampilDiagnosa();
        tampilTable();
        
    cbiddiagnosa.addActionListener(e -> {
            String IdDiagnosa = (String) cbiddiagnosa.getSelectedItem();
            if (IdDiagnosa != null && !IdDiagnosa.isEmpty()){ 
                tampilkanDiagnosa(IdDiagnosa);
            }
        });
    }
      
   private void tampilDiagnosa() {
    try {
        String sql = "SELECT Id_Diagnosa FROM diagnosa ORDER BY Id_Diagnosa";
        pst = conn.prepareStatement(sql);
        rs  = pst.executeQuery();
        cbiddiagnosa.removeAllItems();
        while (rs.next()) {
            cbiddiagnosa.addItem(rs.getString("Id_Diagnosa"));
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null,"Gagal ambil Id Diagnosa\n" + e);
    }
}
    
     private void tampilkanDiagnosa(String Id_Diagnosa) {
        try {
            String sql = "SELECT * FROM diagnosa WHERE Id_Diagnosa = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1,Id_Diagnosa);
            rs = pst.executeQuery();

            if (rs.next()) {
                txtnamapasien1.setText(rs.getString("Nama_pasien"));
                txtidpenyakit1.setText(rs.getString("kode_penyakit"));
                jthasildiagnosa.setText(rs.getString("hasil_diagnosa"));
                jtsolusi.setText(rs.getString("solusi"));

            } else {
                // Reset semua jika data tidak ditemukan
                txtnamapasien1.setText("");
                txtidpenyakit1.setText("");
                jthasildiagnosa.setText("");
                jtsolusi.setText("");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menampilkan data diagnosa: " + e.getMessage());
        
        }       
     }
     
     private void tampilPasien(String namaPasien){
         
        try{
            String query ="SELECT Nama_pasien FROM diagnosa WHERE Id_Diagnosa=?";
            pst = conn.prepareStatement(query);
            String IdDianosa = null;
            pst.setString(1,IdDianosa);
            rs = pst.executeQuery();
            
            if(rs.next()){
                txtnamapasien1.setText(rs.getString("Nama_pasien"));
                tampilPasien(txtnamapasien1.getText());
            }
            }catch (SQLException e){
                JOptionPane.showMessageDialog(null,"Gagal Mengambil Nama Pasien:"+e);
        }
     }
     
      private void ambilKodePenyakit(String nama) {
    try {
        String query = "SELECT kode_penyakit FROM diagnosa WHERE Nama_pasien = ? ORDER BY Id_Diagnosa DESC LIMIT 1";
        pst = conn.prepareStatement(query);
        pst.setString(1, nama);
        rs = pst.executeQuery();
        if (rs.next()) {
            txtidpenyakit1.setText(rs.getString("kode_penyakit"));
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Gagal mengambil kode penyakit: " + e);
    }
      } 

    private void tampilTable(){
        String[]kolom = {"Id Diagnosa","Nama pasien","Kode penyakit","Hasil Diagnosa","Solusi"};
        tabmode = new DefaultTableModel(null, kolom);
        tblhasildiagnosa2.setModel(tabmode);
        
        
        String keyword ="%"+txtcari.getText().trim()+"%";
        String sql = txtcari.getText().trim().isEmpty()?"SELECT* FROM hasil_diagnosa":"SELECT* FROM hasil_diagnosa WHERE Id_Diagnosa LIKE ? OR Nama_pasien LIKE? OR idpenyakit LIKE? OR hasildiagnosa LIKE? OR solusi LIKE?";
        
        try{
            pst=conn.prepareStatement(sql);
            if(!txtcari.getText().trim().isEmpty()){
                for(int i = 1;i<=4;i++)pst.setString(i,keyword);
            }
            
            rs=pst.executeQuery();
            while (rs.next()){
                tabmode.addRow(new String[]{
                rs.getString("Id_Diagnosa"),
                rs.getString("Nama_pasien"),
                rs.getString("idpenyakit"),
                rs.getString("hasildiagnosa"),
                rs.getString("solusi")
            });
            }
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null,"Gagal Tampil Data:"+e);
        }
}
    
    private void resetForm(){
        cbiddiagnosa.setSelectedIndex(0);
        txtnamapasien1.setText("");
        txtidpenyakit1.setText("");
        jthasildiagnosa.setText("");
        jtsolusi.setText("");
        txtcari.setText("");
    }

    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jbSelesai1 = new javax.swing.JButton();
        jbhapus1 = new javax.swing.JButton();
        cbiddiagnosa = new javax.swing.JComboBox<>();
        txtnamapasien1 = new javax.swing.JTextField();
        txtidpenyakit1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jthasildiagnosa = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtsolusi = new javax.swing.JTextArea();
        jbsimpan = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblhasildiagnosa2 = new javax.swing.JTable();
        jbcari = new javax.swing.JButton();
        txtcari = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 204));

        jPanel1.setBackground(new java.awt.Color(51, 0, 51));

        jLabel1.setBackground(new java.awt.Color(102, 0, 102));
        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Hasil Diagnosa");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(467, 467, 467))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel1)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(102, 0, 102));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/symptoms.png"))); // NOI18N
        jLabel2.setText("Id Diagnosa");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/employee.png"))); // NOI18N
        jLabel3.setText("Nama Pasien");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/virus.png"))); // NOI18N
        jLabel4.setText("Hasil Diagnosa");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/mask.png"))); // NOI18N
        jLabel5.setText("Id Penyakit");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/idea.png"))); // NOI18N
        jLabel6.setText("Solusi");

        jbSelesai1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jbSelesai1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit-door.png"))); // NOI18N
        jbSelesai1.setText("Kembali");
        jbSelesai1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSelesai1ActionPerformed(evt);
            }
        });

        jbhapus1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jbhapus1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/trash.png"))); // NOI18N
        jbhapus1.setText("Hapus");
        jbhapus1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbhapus1ActionPerformed(evt);
            }
        });

        cbiddiagnosa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbiddiagnosaActionPerformed(evt);
            }
        });

        txtnamapasien1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        txtidpenyakit1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        txtidpenyakit1.setToolTipText("");

        jthasildiagnosa.setColumns(20);
        jthasildiagnosa.setRows(5);
        jScrollPane1.setViewportView(jthasildiagnosa);

        jtsolusi.setColumns(20);
        jtsolusi.setRows(5);
        jScrollPane2.setViewportView(jtsolusi);

        jbsimpan.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jbsimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/save.png"))); // NOI18N
        jbsimpan.setText("Simpan");
        jbsimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbsimpanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(159, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5))
                        .addGap(29, 29, 29))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jbsimpan)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jbhapus1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jbSelesai1))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtnamapasien1)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtidpenyakit1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbiddiagnosa, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(145, 145, 145))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbiddiagnosa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtnamapasien1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtidpenyakit1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(67, 67, 67)
                        .addComponent(jLabel6))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbhapus1)
                    .addComponent(jbSelesai1)
                    .addComponent(jbsimpan))
                .addGap(45, 45, 45))
        );

        jPanel3.setBackground(new java.awt.Color(102, 0, 102));

        tblhasildiagnosa2.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        tblhasildiagnosa2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Id Diangnosa", "Nama Pasien", "Kode Penyakit", "Hasil Diagnosa", "Solusi"
            }
        ));
        tblhasildiagnosa2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblhasildiagnosa2MouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblhasildiagnosa2);

        jbcari.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jbcari.setText("Cari");
        jbcari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbcariActionPerformed(evt);
            }
        });
        jbcari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jbcariKeyPressed(evt);
            }
        });

        txtcari.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbcari)
                .addGap(18, 18, 18)
                .addComponent(txtcari, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(265, Short.MAX_VALUE))
            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbcari)
                    .addComponent(txtcari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbiddiagnosaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbiddiagnosaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbiddiagnosaActionPerformed

    private void tblhasildiagnosa2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblhasildiagnosa2MouseClicked
         int bar = tblhasildiagnosa2.getSelectedRow();
         if(bar >=0){
        String a = tabmode.getValueAt(bar, 0).toString();
        String b = tabmode.getValueAt(bar, 1).toString();
        String c = tabmode.getValueAt(bar, 2).toString();
        String d = tabmode.getValueAt(bar, 3).toString();
        String e = tabmode.getValueAt(bar, 4).toString();
        
        cbiddiagnosa.setSelectedItem(a);
        txtnamapasien1.setText(b);
        txtidpenyakit1.setText(c);
        jthasildiagnosa.setText(d);
        jtsolusi.setText(e);
         }
    }//GEN-LAST:event_tblhasildiagnosa2MouseClicked

    private void jbcariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbcariActionPerformed
         Object[] kolom = {"Id Diagnosa", "Nama Pasien", "Kode Penyakit", "Hasil Diagnosa", "Solusi"};
        tabmode = new DefaultTableModel(null, kolom);
        tblhasildiagnosa2.setModel(tabmode);
        String sql = "SELECT * FROM hasil_diagnosa WHERE Nama_pasien LIKE ?";
        try {
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, "%" + txtcari.getText() + "%");
            ResultSet hasil = stat.executeQuery();
            while (hasil.next()) {
                String idpasien = hasil.getString("Id_Diagnosa");
                String namapasien = hasil.getString("Nama_pasien");
                String idpenyakit = hasil.getString("idpenyakit");
                String hasildiagnosa = hasil.getString("hasildiagnosa");
                String solusi = hasil.getString("solusi");

                String[] data = {idpasien, namapasien, idpenyakit, hasildiagnosa, solusi};
                tabmode.addRow(data);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data gagal dicari: " + e);
        }
    }//GEN-LAST:event_jbcariActionPerformed

    private void jbhapus1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbhapus1ActionPerformed
                                        
    int ok = JOptionPane.showConfirmDialog(null, "Hapus Data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
    if (ok == 0) {
        try {
            String idDiagnosa = cbiddiagnosa.getSelectedItem().toString();
            String sql = "DELETE FROM hasil_diagnosa WHERE Id_Diagnosa = ?";
            PreparedStatement stat  = conn.prepareStatement(sql);
            stat.setString(1, idDiagnosa);

            int hasil = stat.executeUpdate();
            if (hasil > 0) {
                JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
                tampilTable();       // refresh tabel
                resetForm();         // bersihkan form
                txtcari.setText(""); // reset filter pencarian
            } else {
                JOptionPane.showMessageDialog(null, "Data tidak ditemukan atau gagal dihapus");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data gagal dihapus: " + e);
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Silakan klik data di tabel terlebih dahulu!");
        }
    }
    }//GEN-LAST:event_jbhapus1ActionPerformed

    private void jbSelesai1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSelesai1ActionPerformed
        this.dispose();
        new MenuUtama().setVisible(true);
    }//GEN-LAST:event_jbSelesai1ActionPerformed

    private void jbsimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbsimpanActionPerformed
     String sql = "insert into hasil_diagnosa (Id_Diagnosa, Nama_Pasien, idpenyakit, hasildiagnosa,solusi) Values (?,?,?,?,?)";
        try {
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, cbiddiagnosa.getSelectedItem().toString());
            stat.setString(2, txtnamapasien1.getText());
            stat.setString(3, txtidpenyakit1.getText());
            stat.setString(4,  jthasildiagnosa.getText());
            stat.setString(5,   jtsolusi.getText());

            stat.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil disimpan");
            txtnamapasien1.requestFocus();
            tampilTable();
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Data gagal disimpan"+e);
        }
    }//GEN-LAST:event_jbsimpanActionPerformed

    private void jbcariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jbcariKeyPressed
        if (evt.getKeyCode()== KeyEvent.VK_ENTER){ 
            tampilTable();
      }
    }//GEN-LAST:event_jbcariKeyPressed

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
            java.util.logging.Logger.getLogger(hasildiagnosa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(hasildiagnosa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(hasildiagnosa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(hasildiagnosa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new hasildiagnosa().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbiddiagnosa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JButton jbSelesai1;
    private javax.swing.JButton jbcari;
    private javax.swing.JButton jbhapus1;
    private javax.swing.JButton jbsimpan;
    private javax.swing.JTextArea jthasildiagnosa;
    private javax.swing.JTextArea jtsolusi;
    private javax.swing.JTable tblhasildiagnosa2;
    private javax.swing.JTextField txtcari;
    private javax.swing.JTextField txtidpenyakit1;
    private javax.swing.JTextField txtnamapasien1;
    // End of variables declaration//GEN-END:variables
}