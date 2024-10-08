/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.formdev.flatlaf.FlatDarculaLaf;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import static javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import model.MySQL;

/**
 *
 * @author acer
 */
public class UserRegistration extends javax.swing.JFrame {

    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {

            new Home().setVisible(true);

        }
    }

    /*public void focusGained(FocusEvent arg0) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                jTextField4.setCaretPosition(jTextField4.getText().length());
            }
        });
    }*/
    private void loadUsers() {
        try {
            ResultSet search = MySQL.search("SELECT `user`.`id`, `user`.`fname`, `user`.`lname`, `user`.`password`, `user`.`contact_no`, `user_type`.`name` "
                    + "AS `user_type`, `user`.`address`, `user_status`.`name` AS `user_status`,`gender`.`name` AS `gender`,`user`.`email` FROM `user` INNER JOIN `user_type` INNER JOIN `user_status` "
                    + "INNER JOIN `gender` ON `user`.`user_type_id` = `user_type`.`id` AND `user`.`user_status_id` = `user_status`.`id` AND `user`.`gender_id` = `gender`.`id` ORDER BY `user`.`id` ASC");

            //DefaultTableModel model1 = (DefaultTableModel) jTable1.getModel();
            ((DefaultTableModel) jTable1.getModel()).setRowCount(0);

            while (search.next()) {

                Vector v = new Vector();
                v.add(search.getString("id"));
                v.add(search.getString("fname") + " " + search.getString("lname"));
                v.add(search.getString("password"));
                v.add(search.getString("contact_no"));
                v.add(search.getString("user_type"));
                v.add(search.getString("address"));
                v.add(search.getString("user_status"));
                v.add(search.getString("gender"));
                v.add(search.getString("email"));
                ((DefaultTableModel) jTable1.getModel()).addRow(v);
            }
            jTable1.setModel(((DefaultTableModel) jTable1.getModel()));
            //table cell render

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUsers(String text) {
        try {
            ResultSet search = MySQL.search("SELECT `user`.`id`, `user`.`fname`, `user`.`lname`, `user`.`password`, `user`.`contact_no`, `user_type`.`name` "
                    + "AS `user_type`, `user`.`address`, `user_status`.`name` AS `user_status`,`gender`.`name` AS `gender`,`user`.`email` FROM `user` INNER JOIN `user_type` INNER JOIN `user_status` "
                    + "INNER JOIN `gender` ON `user`.`user_type_id` = `user_type`.`id` AND `user`.`user_status_id` = `user_status`.`id` AND `user`.`gender_id` = `gender`.`id` WHERE user.`fname` LIKE '" + text + "%'"
                    + "OR `user`.`lname` LIKE '" + text + "%' OR `user`.`contact_no` LIKE '" + text + "%' ORDER BY `user`.`id` ASC");

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (search.next()) {

                Vector v = new Vector();
                v.add(search.getString("id"));
                v.add(search.getString("fname") + " " + search.getString("lname"));
                v.add(search.getString("password"));
                v.add(search.getString("contact_no"));
                v.add(search.getString("user_type"));
                v.add(search.getString("address"));
                v.add(search.getString("user_status"));
                v.add(search.getString("gender"));
                v.add(search.getString("email"));
                model.addRow(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUserTypes() {

        try {

            ResultSet rs = MySQL.search("SELECT * FROM `user_type`");

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

    private void loadGenderTypes() {
        try {

            Vector v = new Vector();

            v.add("Select");
            v.add("Male");
            v.add("Female");

            jComboBox2.setModel(new DefaultComboBoxModel(v));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {

        jTextField1.setText("");
        txt_lname.setText("");
        jTextField4.setText("");
        txt_address.setText("");
        txt_email.setText("");
        jPasswordField1.setText("");
        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
        jTextField1.grabFocus();
    }

    private void setStatusButtonListner() {

        ListSelectionListener lsl = (ListSelectionEvent e) -> {

            int selectedRow = jTable1.getSelectedRow();

            if (selectedRow != -1) {

                String id = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();

                try {
                    ResultSet rs = MySQL.search("SELECT `user`.`fname`, `user`.`lname`, `user`.`password`, `user`.`contact_no`, `user_type`.`name` "
                            + "AS `user_type`, `user`.`address`,`gender`.`name` AS `gender`,`user`.`email` FROM `user` INNER JOIN `user_type` INNER JOIN `user_status` "
                            + "INNER JOIN `gender` ON `user`.`user_type_id` = `user_type`.`id` AND `user`.`user_status_id` = `user_status`.`id` AND `user`.`gender_id` = `gender`.`id` WHERE `user`.`id`='" + Integer.parseInt(id) + "'");
                    rs.next();
                    jTextField1.setText(rs.getString("fname"));
                    txt_lname.setText(rs.getString("lname"));
                    jPasswordField1.setText(rs.getString("password"));
                    jTextField4.setText(rs.getString("contact_no"));
                    jComboBox1.setSelectedItem(rs.getString("user_type"));
                    txt_address.setText(rs.getString("address"));
                    jComboBox2.setSelectedItem(rs.getString("gender"));
                    txt_email.setText(rs.getString("email"));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                jButton1.setEnabled(false);
                String type = jTable1.getValueAt(selectedRow, 4).toString();
                int sacount = 0;
                try {
                    ResultSet search = MySQL.search("SELECT COUNT(*) AS `count` FROM `user` WHERE `user_type_id`='1' AND `user_status_id`='1'");
                    search.next();
                    sacount = Integer.parseInt(search.getString("count"));

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (type.equals("Admin")) {

                    jButton2.setEnabled(true);

                } else if ((type.equals("Super admin") && sacount == 1) || Integer.parseInt(id) == SignIn.userId) {
                    jButton2.setEnabled(false);
                } else if (Integer.parseInt(jTable1.getValueAt(selectedRow, 0).toString()) == SignIn.userId) {
                    jButton2.setEnabled(true);
                } else {
                    jButton2.setEnabled(false);
                }

                //jTable1.clearSelection();
            }
        };

        jTable1.getSelectionModel().addListSelectionListener(lsl);

    }

    /**
     * Creates new form UserRegistration
     */
    public UserRegistration() {
        initComponents();
        setStatusButtonListner();
        loadUsers();
        loadUserTypes();
        loadGenderTypes();

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
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel8 = new javax.swing.JLabel();
        txt_lname = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txt_address = new javax.swing.JTextField();
        txt_email = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1280, 356));

        jLabel1.setText("First name");

        jLabel2.setText("Password");

        jLabel4.setText("Contact Number");

        jTextField4.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTextField4CaretUpdate(evt);
            }
        });
        jTextField4.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jTextField4MouseMoved(evt);
            }
        });
        jTextField4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField4FocusGained(evt);
            }
        });
        jTextField4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField4MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTextField4MousePressed(evt);
            }
        });
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField4KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField4KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField4KeyTyped(evt);
            }
        });

        jLabel5.setText("Type");

        jLabel6.setText("Gender");

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton1.setText("Create Account");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton2.setText("Change Status");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel8.setText("Last name");

        jLabel10.setText("Address");

        jLabel11.setText("Email");

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton3.setText("Update Account");
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
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel1)
                            .addComponent(jLabel10)
                            .addComponent(jLabel8)
                            .addComponent(jLabel4)
                            .addComponent(jLabel11)
                            .addComponent(jLabel2))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_lname, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_address, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(7, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_lname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_address, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Password", "Contact No.", "Type", "Address", "Status", "Gender", "Email"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTable1FocusLost(evt);
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(30);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(130);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(60);
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(60);
            jTable1.getColumnModel().getColumn(6).setPreferredWidth(40);
        }

        jLabel9.setText("Search User");

        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 907, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    boolean click = false;

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        // TODO add your handling code here:

        String text = jTextField3.getText();

        loadUsers(text);
    }//GEN-LAST:event_jTextField3KeyReleased


    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 1) {

            String id = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();

            try {
                ResultSet rs = MySQL.search("SELECT `user`.`fname`, `user`.`lname`, `user`.`password`, `user`.`contact_no`, `user_type`.`name` "
                        + "AS `user_type`, `user`.`address`,`gender`.`name` AS `gender`,`user`.`email` FROM `user` INNER JOIN `user_type` INNER JOIN `user_status` "
                        + "INNER JOIN `gender` ON `user`.`user_type_id` = `user_type`.`id` AND `user`.`user_status_id` = `user_status`.`id` AND `user`.`gender_id` = `gender`.`id` WHERE `user`.`id`='" + Integer.parseInt(id) + "'");
                rs.next();
                jTextField1.setText(rs.getString("fname"));
                txt_lname.setText(rs.getString("lname"));
                jPasswordField1.setText(rs.getString("password"));
                jTextField4.setText(rs.getString("contact_no"));
                jComboBox1.setSelectedItem(rs.getString("user_type"));
                txt_address.setText(rs.getString("address"));
                jComboBox2.setSelectedItem(rs.getString("gender"));
                txt_email.setText(rs.getString("email"));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:

        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user", "Warning", JOptionPane.WARNING_MESSAGE);

        } else {

            String id = jTable1.getValueAt(selectedRow, 0).toString();
            String currentStatus = jTable1.getValueAt(selectedRow, 6).toString();

            int status;

            if (currentStatus.equals("Active")) {
                status = 2;
            } else {
                status = 1;
            }

            MySQL.iud("UPDATE `user` SET `user_status_id` = " + status + " WHERE `id` = " + Integer.parseInt(id) + "");

            loadUsers();

            JOptionPane.showMessageDialog(this, "User status updated", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        String fname = jTextField1.getText().trim();
        String lname = txt_lname.getText().trim();
        String password = new String(jPasswordField1.getPassword());
        String mobile = jTextField4.getText().trim();
        String type = jComboBox1.getSelectedItem().toString();
        String address = txt_address.getText();
        String gender = jComboBox2.getSelectedItem().toString();
        String email = txt_email.getText().trim();

        if (fname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter first name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (lname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter last name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter password", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (mobile.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter contact number", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("07[01245678][0-9]{7}").matcher(mobile).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid mobile number", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (type.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select type", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter address", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (gender.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select gender", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter email", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$").matcher(email).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid email", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            try {

                ResultSet rs1 = MySQL.search("SELECT `id` FROM `gender` WHERE `name` = '" + gender + "'");
                rs1.next();

                ResultSet rs2 = MySQL.search("SELECT `id` FROM `user_type` WHERE `name` = '" + type + "'");
                rs2.next();

                String gender_id = rs1.getString("id");
                String type_id = rs2.getString("id");

                MySQL.iud("INSERT INTO `user`(`fname`, `lname`, `password`, `contact_no`, `user_type_id`, `gender_id`,`address`,`email`) VALUES('" + fname + "', '" + lname + "', '" + password + "', '" + mobile + "', " + type_id + ", " + gender_id + ",' " + address + "',' " + email + "')");

                JOptionPane.showMessageDialog(this, "New user account created!", "Success", JOptionPane.INFORMATION_MESSAGE);

                loadUsers();

                clearFields();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyTyped
        // TODO add your handling code here:
        String text = jTextField4.getText().trim();
        String fullText = text + evt.getKeyChar();
        //System.out.println((text + evt.getKeyChar()).length());
        //System.out.println(fullText);

        if (fullText.length() == 1) {
            if (!fullText.equals("0")) {
                evt.consume();
            }
        } else if (fullText.length() == 2) {
            if (!fullText.equals("07")) {
                evt.consume();
            }
        } else if (fullText.length() == 3) {
            if (!Pattern.compile("07[01245678]").matcher(fullText).matches()) {
                evt.consume();
            }
        } else if (fullText.length() <= 10) {
            if (!Pattern.compile("07[01245678][0-9]+").matcher(fullText).matches()) {
                evt.consume();
            }
        } else {
            evt.consume();
        }


    }//GEN-LAST:event_jTextField4KeyTyped

    private void jTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyReleased
        // TODO add your handling code here:

        /*if (jTextField4.getText().length() == 10) {
             jTextField4.setEditable(false);
           //jTextField4.setDocument(null);
        }*/

    }//GEN-LAST:event_jTextField4KeyReleased

    private void jTextField4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField4MouseClicked
        // TODO add your handling code here:

        if (evt.getClickCount() == 1) {
            //jTextField4.setEditable(true);
            //jTextField4.setText("");
            //jTextField4.grabFocus();
            jTextField4.setCaretPosition(jTextField4.getText().length());
            if (jTextField4.getCaretPosition() != jTextField4.getText().length()) {
                jTextField4.setCaretColor(new Color(0x46494B));

            } else {
                jTextField4.setCaretColor(Color.white);
            }
            click = false;

        }
        if (evt.getClickCount() == 2) {
            click = true;
        }

        /*jTextField4.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent arg0) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        jTextField4.setCaretPosition(jTextField4.getText().length());
                    }
                });
            }
        });*/
    }//GEN-LAST:event_jTextField4MouseClicked

    private void jTextField4FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField4FocusGained
        // TODO add your handling code here:
        /*jTextField4.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent arg0) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        jTextField4.setCaretPosition(jTextField4.getText().length());
                    }
                });
            }
        });*/
    }//GEN-LAST:event_jTextField4FocusGained

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
        /*if(jTextField4.getCaretPosition()!=10){
        jTextField4.setCaretColor(new Color(0x46494B));
        }else{
         jTextField4.setCaretColor(Color.white);
        }*/

    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField4CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTextField4CaretUpdate
        // TODO add your handling code here:
        /*if (jTextField4.getCaretPosition() != 10) {
            jTextField4.setCaretColor(new Color(0x46494B));
        } else {
            jTextField4.setCaretColor(Color.white);
        }*/
    }//GEN-LAST:event_jTextField4CaretUpdate

    private void jTextField4MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField4MouseMoved
        // TODO add your handling code here:

    }//GEN-LAST:event_jTextField4MouseMoved

    private void jTextField4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 8) {
            jTextField4.setCaretPosition(jTextField4.getText().length());
            jTextField4.setCaretColor(Color.white);
            if (click == true) {
                jTextField4.setText("");
                click = false;
            }
        } else {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField4KeyPressed

    private void jTextField4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField4MousePressed
        // TODO add your handling code here:

        if (jTextField4.getCaretPosition() != jTextField4.getText().length()) {
            jTextField4.setCaretColor(new Color(0x46494B));

        } else {
            jTextField4.setCaretColor(Color.white);
        }


    }//GEN-LAST:event_jTextField4MousePressed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if (!jTable1.getSelectionModel().isSelectionEmpty()) {
            String id = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();
            String fname = jTextField1.getText().trim();
            String lname = txt_lname.getText().trim();
            String password = new String(jPasswordField1.getPassword());
            String mobile = jTextField4.getText().trim();
            String type = jComboBox1.getSelectedItem().toString();
            String address = txt_address.getText();
            String gender = jComboBox2.getSelectedItem().toString();
            String email = txt_email.getText().trim();
            String typeid = null;
            String genderid = null;

            if (!Pattern.compile("07[01245678][0-9]{7}").matcher(mobile).matches()) {
                JOptionPane.showMessageDialog(this, "Invalid mobile number", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (type.equals("Select")) {
                JOptionPane.showMessageDialog(this, "Please select type", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (type.equals("Admin")) {
                JOptionPane.showMessageDialog(this, "Please select type as Super admin", "Warning", JOptionPane.WARNING_MESSAGE);
            }  else if (gender.equals("Select")) {
                JOptionPane.showMessageDialog(this, "Please select gender", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (!Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$").matcher(email).matches()) {
                JOptionPane.showMessageDialog(this, "Invalid email", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    ResultSet rs = MySQL.search("SELECT * FROM `user_type` WHERE `name`='" + type + "'");
                    rs.next();
                    typeid = rs.getString("id");
                    ResultSet rs1 = MySQL.search("SELECT * FROM `gender` WHERE `name`='" + gender + "'");
                    rs1.next();
                    genderid = rs1.getString("id");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!fname.isEmpty()) {
                    MySQL.iud("UPDATE `user` SET `fname`='" + fname + "' WHERE `id`='" + Integer.parseInt(id) + "'");
                }
                if (!lname.isEmpty()) {
                    MySQL.iud("UPDATE `user` SET `lname`='" + lname + "' WHERE `id`='" + Integer.parseInt(id) + "'");
                }
                if (!password.isEmpty()) {
                    MySQL.iud("UPDATE `user` SET `password`='" + password + "' WHERE `id`='" + Integer.parseInt(id) + "'");
                }
                if (!mobile.isEmpty()) {
                    MySQL.iud("UPDATE `user` SET `contact_no`='" + mobile + "' WHERE `id`='" + Integer.parseInt(id) + "'");
                }
                if (!type.isEmpty()) {

                    MySQL.iud("UPDATE `user` SET `user_type_id`='" + typeid + "' WHERE `id`='" + Integer.parseInt(id) + "'");
                    /*if (id == "1") {
                        MySQL.iud("UPDATE `user` SET `user_type_id`='1' WHERE `id`='" + Integer.parseInt(id) + "'");
                    }*/
                }
                if (!address.isEmpty()) {
                    MySQL.iud("UPDATE `user` SET `address`='" + address + "' WHERE `id`='" + Integer.parseInt(id) + "'");
                }
                if (!gender.isEmpty()) {
                    MySQL.iud("UPDATE `user` SET `gender_id`='" + genderid + "' WHERE `id`='" + Integer.parseInt(id) + "'");
                }
                if (!email.isEmpty()) {
                    MySQL.iud("UPDATE `user` SET `email`='" + email + "' WHERE `id`='" + Integer.parseInt(id) + "'");
                }
                clearFields();
                jTable1.getSelectionModel().clearSelection();
                jButton1.setEnabled(true);
                loadUsers();
            }

        } else {
            JOptionPane.showMessageDialog(this, "Please select a user", "Warning", JOptionPane.WARNING_MESSAGE);
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTable1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTable1FocusLost
        // TODO add your handling code here:


    }//GEN-LAST:event_jTable1FocusLost

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
                new UserRegistration().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField txt_address;
    private javax.swing.JTextField txt_email;
    private javax.swing.JTextField txt_lname;
    // End of variables declaration//GEN-END:variables

}
