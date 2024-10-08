/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.formdev.flatlaf.FlatDarculaLaf;
import java.awt.Image;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import model.MySQL;

/**
 *
 * @author acer
 */
public class Home extends javax.swing.JFrame {

    /**
     * Creates new form Home
     */
    public void stockcheck() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date today = new Date();
            ResultSet rs = MySQL.search("SELECT * FROM `stock` WHERE `exd`<='" + sdf.format(today) + "'");
            
            while (rs.next()) {
                //System.out.println(rs.getString("id"));
                MySQL.iud("UPDATE `stock` SET `stock_status`='Inactive' WHERE `id`='" + Integer.parseInt(rs.getString("id")) + "'");
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadEXDAlert() {
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //long millis = System.currentTimeMillis();
            Date today = new Date();
            Calendar cal = new GregorianCalendar();
            cal.setTime(today);
            cal.add(Calendar.DAY_OF_MONTH, +31);
            Date today31 = cal.getTime();
            ResultSet search = MySQL.search("SELECT * FROM `stock` INNER JOIN `grn_item` ON `grn_item`.`stock_id` = `stock`.`id` INNER JOIN `product` ON `stock`.`product_id` = `product`.`id` INNER JOIN `brand` ON `product`.`brand_id` = `brand`.`id` INNER JOIN `category` ON `product`.`category_id` = `category`.`id` INNER JOIN `grn` ON `grn_item`.`grn_id` = `grn`.`id` WHERE `stock`.`stock_status`='Active' AND `exd`<'" + sdf.format(today31) + "' ORDER BY `stock`.`id`");

            //DefaultTableModel model1 = (DefaultTableModel) jTable1.getModel();
            ((DefaultTableModel) jTable2.getModel()).setRowCount(0);
            
            while (search.next()) {
                //SimpleDateFormat sdf1 = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",Locale.ENGLISH);
                long millis = System.currentTimeMillis();
                Date today1 = new Date(millis);
                Date firstDate = today1;
                Date secondDate = sdf.parse(search.getString("stock.exd"));
                
                long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
                int diff = (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                
                Vector v = new Vector();
                v.add(search.getString("stock.id"));
                v.add(search.getString("product.name"));
                v.add(search.getString("grn.supplier_id"));
                v.add(search.getString("stock.qty"));
                v.add(search.getString("stock.exd"));
                v.add(diff);
                
                ((DefaultTableModel) jTable2.getModel()).addRow(v);
            }
            jTable2.setModel(((DefaultTableModel) jTable2.getModel()));
            //table cell render

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadQuantityAlert() {
        try {
            ResultSet search = MySQL.search("SELECT * FROM `stock` INNER JOIN `product` ON `stock`.`product_id` = `product`.`id` INNER JOIN `brand` ON `product`.`brand_id` = `brand`.`id` INNER JOIN `category` ON `product`.`category_id` = `category`.`id` WHERE `stock`.`stock_status`='Active' ORDER BY `stock`.`id`");

            //DefaultTableModel model1 = (DefaultTableModel) jTable1.getModel();
            ((DefaultTableModel) jTable1.getModel()).setRowCount(0);
            
            while (search.next()) {
                int row = -1;
                for (int i = 0; i < jTable1.getRowCount(); i++) {
                    //String qty =search.getString("stock.qty");
                    if (search.getString("product.name").equals(jTable1.getValueAt(i, 2)) && search.getString("brand.name").equals(jTable1.getValueAt(i, 1)) && search.getString("category.name").equals(jTable1.getValueAt(i, 0))) {
                        row = i;
                        
                    }
                    
                }
                if (row == -1) {
                    if (Integer.parseInt(search.getString("stock.qty")) < 31) {
                        Vector v = new Vector();
                        v.add(search.getString("category.name"));
                        v.add(search.getString("brand.name"));
                        v.add(search.getString("product.name"));
                        v.add(search.getString("stock.qty"));
                        
                        ((DefaultTableModel) jTable1.getModel()).addRow(v);
                    }
                    
                } else {
                    jTable1.setValueAt(Integer.parseInt(jTable1.getValueAt(row, 3).toString()) + Integer.parseInt(search.getString("stock.qty")), row, 3);
                    if (Integer.parseInt(jTable1.getValueAt(row, 3).toString()) > 30) {
                        ((DefaultTableModel) jTable1.getModel()).removeRow(row);
                    }
                }
                
            }
            jTable1.setModel(((DefaultTableModel) jTable1.getModel()));
            //table cell render

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void access() {
        try {
            ResultSet search = MySQL.search("SELECT `user`.`user_type_id` FROM `user` WHERE `user`.`id`='" + SignIn.userId + "' ");
            search.next();
            if (search.getString("user_type_id").equals("1")) {
                jButton1.setEnabled(true);
            } else {
                jButton1.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public Home() {
        initComponents();
        access();
        loadQuantityAlert();
        loadEXDAlert();
        stockcheck();
        Image ic = new ImageIcon(this.getClass().getResource("/resources/medicare.png")).getImage();
        this.setIconImage(ic);
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
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton1.setText("User Registration");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton2.setText("Product Registration");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton3.setText("Supplier Registration");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton4.setText("GRN");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton5.setText("Stock");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton6.setText("Customer Registration");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton7.setText("Invoice");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jTable1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Category", "Brand", "Product", "Quantity"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setText("Stock Quantity Alert :");

        jTable2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Stock Id", "Product", "Supplier Id", "Quantity", "EXD", "Days Left"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTable2);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setText("Stock Expiration Alert :");

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/reload.png"))); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1236, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton8)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        new UserRegistration().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:

        new ProductRegistration().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:

        new SupplierRegistration().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:

        new GRN().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:

        new Stock().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        new CustomerRegistration().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        new Invoice().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        loadQuantityAlert();
        loadEXDAlert();
        stockcheck();
    }//GEN-LAST:event_jButton8ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables
}
