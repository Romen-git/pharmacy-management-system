/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import model.MySQL;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author acer
 */
public class Stock extends javax.swing.JFrame {

    /**
     * Creates new form Stock
     */
    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            Home h = new Home();
            h.setVisible(true);

        }
    }

    /*private void dateTextField() {
        JTextFieldDateEditor editor = (JTextFieldDateEditor) jDateChooser1.getDateEditor();
        editor.setEditable(false);
        editor.addPropertyChangeListener("foreground", event -> {
            if (Color.BLACK.equals(event.getNewValue())) {
                loadStock();
                editor.setForeground(new Color(204, 204, 204));

            }
        });
        JTextFieldDateEditor editor1 = (JTextFieldDateEditor) jDateChooser2.getDateEditor();
        editor1.setEditable(false);
        editor1.addPropertyChangeListener("foreground", event -> {
            if (Color.BLACK.equals(event.getNewValue())) {
                loadStock();
                editor1.setForeground(new Color(204, 204, 204));

            }
        });
      
        //editor.getComponent().

    }*/
    private void loadStock() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String category = jComboBox1.getSelectedItem().toString();
            String brand = jComboBox2.getSelectedItem().toString();
            String name = jTextField2.getText();
            String minprice = jTextField3.getText();
            String maxprice = jTextField5.getText();

            String sort = jComboBox4.getSelectedItem().toString();

            String orderBy = "";

            switch (sort) {
                case "Name ASC":
                    orderBy = " `product`.`name` ASC";
                    break;
                case "Name DESC":
                    orderBy = " `product`.`name` DESC";
                    break;
                case "Selling Price ASC":
                    orderBy = " `stock`.`selling_price` ASC";
                    break;
                case "Selling Price DESC":
                    orderBy = " `stock`.`selling_price` DESC";
                    break;
                case "Buying Price ASC":
                    orderBy = " `grn_item`.`buying_price` ASC";
                    break;
                case "Buying Price DESC":
                    orderBy = " `grn_item`.`buying_price` DESC";
                    break;
                case "Quantity ASC":
                    orderBy = " `stock`.`qty` ASC";
                    break;
                case "Quantity DESC":
                    orderBy = " `stock`.`qty` DESC";
                    break;
                case "EXD ASC":
                    orderBy = " `stock`.`exd` ASC";
                    break;
                case "EXD DESC":
                    orderBy = " `stock`.`exd` DESC";
                    break;
                case "MFD ASC":
                    orderBy = " `stock`.`mfd` ASC";
                    break;
                case "MFD DESC":
                    orderBy = " `stock`.`mfd` DESC";
                    break;
                default:
                    orderBy = " `product`.`name` ASC";
                    break;
            }

            String mainQuery = "";
            if (category == "Select" && brand == "Select" && name.isEmpty() && minprice.isEmpty() && maxprice.isEmpty()) {
                mainQuery = "SELECT DISTINCT `stock`.`id`, `product`.`id`, `category`.`name`, `brand`.`name`, `product`.`name`, `stock`.`qty`, `grn_item`.`buying_price`, `stock`.`selling_price`, `stock`.`mfd`, `stock`.`exd` FROM `stock` INNER JOIN `grn_item` ON `grn_item`.`stock_id` = `stock`.`id` INNER JOIN `product` ON `stock`.`product_id` = `product`.`id` INNER JOIN `brand` ON `product`.`brand_id` = `brand`.`id` INNER JOIN `category` ON `product`.`category_id` = `category`.`id` WHERE `stock`.`stock_status`='Active' ORDER BY " + orderBy;
            } else {
                String query = "";

                if (category != "Select") {
                    query += "AND `category`.`name`='" + category + "' ";
                }
                if (brand != "Select") {
                    query += "AND `brand`.`name`='" + brand + "' ";
                }
                if (!name.isEmpty()) {
                    query += "AND `product`.`name` LIKE '" + name + "%' ";
                }
                if (!minprice.isEmpty() && !maxprice.isEmpty()) {
                    query += "AND `stock`.`selling_price` >= " + Integer.parseInt(minprice) + " AND `stock`.`selling_price` <= " + Integer.parseInt(maxprice) + " ";
                } else if (!minprice.isEmpty()) {
                    query += "AND `stock`.`selling_price` >= " + Integer.parseInt(minprice) + " ";
                } else if (!maxprice.isEmpty()) {
                    query += "AND `stock`.`selling_price` <= " + Integer.parseInt(maxprice) + " ";
                }

                mainQuery = "SELECT DISTINCT `stock`.`id`, `product`.`id`, `category`.`name`, `brand`.`name`, `product`.`name`, `stock`.`qty`, `grn_item`.`buying_price`, `stock`.`selling_price`, `stock`.`mfd`, `stock`.`exd` FROM `stock` INNER JOIN `grn_item` ON `grn_item`.`stock_id` = `stock`.`id` INNER JOIN `product` ON `stock`.`product_id` = `product`.`id` INNER JOIN `brand` ON `product`.`brand_id` = `brand`.`id` INNER JOIN `category` ON `product`.`category_id` = `category`.`id` WHERE `stock`.`stock_status`='Active' " + query + " ORDER BY " + orderBy;
            }

            //System.out.println(query);
            //String mainQuery = "SELECT DISTINCT `stock`.`id`, `product`.`id`, `category`.`name`, `brand`.`name`, `product`.`name`, `stock`.`qty`, `grn_item`.`buying_price`, `stock`.`selling_price`, `stock`.`mfd`, `stock`.`exd` FROM `stock` INNER JOIN `grn_item` ON `grn_item`.`stock_id` = `stock`.`id` INNER JOIN `product` ON `stock`.`product_id` = `product`.`id` INNER JOIN `brand` ON `product`.`brand_id` = `brand`.`id` INNER JOIN `category` ON `product`.`category_id` = `category`.`id` ORDER BY " + sortquery;
            ResultSet search = MySQL.search(mainQuery);

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (search.next()) {

                Vector v = new Vector();
                v.add(search.getString("stock.id"));
                v.add(search.getString("product.id"));
                v.add(search.getString("category.name"));
                v.add(search.getString("brand.name"));
                v.add(search.getString("product.name"));
                v.add(search.getString("stock.qty"));
                v.add(search.getString("grn_item.buying_price"));
                v.add(search.getString("stock.selling_price"));
                v.add(search.getString("stock.mfd"));
                v.add(search.getString("stock.exd"));
                model.addRow(v);
            }
            jTable1.setModel(model);
            //table cell render

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCategory() {

        try {
            ResultSet rs = MySQL.search("SELECT `name` from `category` ORDER BY `name` ASC");

            Vector v = new Vector();
            v.add("Select");

            while (rs.next()) {
                v.add(rs.getString("name"));
            }

            jComboBox1.setModel(new DefaultComboBoxModel(v));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadBrands() {

        try {
            ResultSet rs = MySQL.search("SELECT `name` from `brand`");

            Vector v = new Vector();
            v.add("Select");

            while (rs.next()) {
                v.add(rs.getString("name"));
            }

            jComboBox2.setModel(new DefaultComboBoxModel(v));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void resetFields() {

        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
        jComboBox4.setSelectedIndex(0);
        jLabel2.setText("0.00");
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField5.setText("");

        jTable1.clearSelection();

        loadStock();
    }

    public Stock() {
        initComponents();
        loadCategory();
        loadBrands();
        loadStock();

        //dateSelected();
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
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel4.setText("Category");

        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel5.setText("Brand");

        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jLabel7.setText("Name");

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        jLabel8.setText("Selling Price");

        jLabel9.setText("Min");

        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField3KeyTyped(evt);
            }
        });

        jLabel15.setText("Sort Product By");

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Name ASC", "Name DESC", "Selling Price ASC", "Selling Price DESC", "Buying Price ASC", "Buying Price DESC", "Quantity ASC", "Quantity DESC", "EXD ASC", "EXD DESC", "MFD ASC", "MFD DESC" }));
        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });

        jLabel16.setText("Max");

        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField5KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField5KeyTyped(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/reset_1.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/report.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(19, 19, 19)
                                .addComponent(jLabel9))
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(25, 25, 25)))
                .addGap(12, 12, 12)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                .addGap(3, 3, 3))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton2, jButton3});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(jLabel9)
                        .addComponent(jLabel16)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton2, jButton3});

        jLabel1.setText("Buying Price");

        jLabel2.setText("0.00");

        jLabel3.setText("New Price");

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton1.setText("Update Stock");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Stock id", "Product id", "Category", "Brand", "Name", "Qty", "Buying Price", "Selling Price", "MFD", "EXD"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1385, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
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

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
        loadStock();
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyTyped
        // TODO add your handling code here:

        String price = jTextField3.getText();
        String txt = price + evt.getKeyChar();

        if (!Pattern.compile("(0|0[.]|0[.][0-9]*)|[1-9]|[1-9][0-9]*|[1-9][0-9]*[.]|[1-9][0-9]*[.][0-9]*").matcher(txt).matches()) {
            evt.consume();
        }

    }//GEN-LAST:event_jTextField3KeyTyped

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        // TODO add your handling code here:
        loadStock();
    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void jTextField5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyTyped
        // TODO add your handling code here:

        String price = jTextField5.getText();
        String txt = price + evt.getKeyChar();

        if (!Pattern.compile("(0|0[.]|0[.][0-9]*)|[1-9]|[1-9][0-9]*|[1-9][0-9]*[.]|[1-9][0-9]*[.][0-9]*").matcher(txt).matches()) {
            evt.consume();
        }

    }//GEN-LAST:event_jTextField5KeyTyped

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        resetFields();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
        // TODO add your handling code here:

        String price = jTextField1.getText();
        String txt = price + evt.getKeyChar();

        if (!Pattern.compile("(0|0[.]|0[.][0-9]*)|[1-9]|[1-9][0-9]*|[1-9][0-9]*[.]|[1-9][0-9]*[.][0-9]*").matcher(txt).matches()) {
            evt.consume();
        }

    }//GEN-LAST:event_jTextField1KeyTyped

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        String bprice = jLabel2.getText();
        String newPrice = jTextField1.getText();
        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow == 1) {
            JOptionPane.showMessageDialog(this, "Please select a stock", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("(0|[1-9][0-9]*)|([0][.]([0]*[1-9][0-9]*))|([1-9][0-9]*[.]([0]*[1-9][0-9]*))").matcher(newPrice).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid selling price", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            String stockId = jTable1.getValueAt(selectedRow, 0).toString();

            if (Double.parseDouble(newPrice) > Double.parseDouble(bprice)) {
                MySQL.iud("Update `stock` SET `selling_price` = '" + newPrice + "' WHERE `id` = '" + stockId + "'");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid New price", "Warning", JOptionPane.WARNING_MESSAGE);
            }

            resetFields();

        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:

        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow > -1) {

            String bprice = jTable1.getValueAt(selectedRow, 6).toString();
            jLabel2.setText(bprice);

        }

    }//GEN-LAST:event_jTable1MouseClicked

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        //loadStock();
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
        loadStock();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        // TODO add your handling code here:
        loadStock();
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        // TODO add your handling code here:
        loadStock();
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jTextField5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyReleased
        // TODO add your handling code here:
        loadStock();
    }//GEN-LAST:event_jTextField5KeyReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        try {
            ResultSet rs3 = MySQL.search("SELECT * FROM `pharmacy`");
            rs3.next();

            InputStream path = Invoice.class.getResourceAsStream("/reports/Stock.jrxml");
            JasperReport jr = JasperCompileManager.compileReport(path);
            //InputStream logo = Invoice.class.getResourceAsStream("../reports/logo.png");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            HashMap parameters = new HashMap();
            parameters.put("Parameter1", rs3.getString("address"));
            parameters.put("Parameter2", rs3.getString("contact_no"));
            parameters.put("Parameter3", rs3.getString("email"));

            parameters.put("Parameter5", sdf.format(new Date()));

            //JREmptyDataSource datasource=new JREmptyDataSource();
            JRTableModelDataSource datasource = new JRTableModelDataSource(jTable1.getModel());
            JasperPrint jp = JasperFillManager.fillReport(jr, parameters, datasource);
            JasperViewer.viewReport(jp, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Stock().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
