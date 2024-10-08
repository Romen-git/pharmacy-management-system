/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import model.MySQL;
import java.util.Date;
import javax.swing.ImageIcon;

/**
 *
 * @author acer
 */
public class CustomerRegistration extends javax.swing.JFrame {

    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {

            if (invoicevisible == 0) {
                Home h = new Home();
                h.setVisible(true);
            }
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
    private void dateTextField() {
        JTextFieldDateEditor editor = (JTextFieldDateEditor) jDateChooser1.getDateEditor();
        editor.setEditable(false);
        editor.addPropertyChangeListener("foreground", event -> {
            if (Color.BLACK.equals(event.getNewValue())) {
                editor.setForeground(new Color(204, 204, 204));

            }
        });
        //editor.getComponent().

    }

    private void loadCustomers() {
        try {
            ResultSet search = MySQL.search("SELECT `customer`.`id`, `customer`.`fname`, `customer`.`lname`, `customer`.`contact_no`"
                    + ", `customer`.`address`, `user_status`.`name` AS `customer_status`,`gender`.`name` AS `gender`,`customer`.`email`,`customer`.`dob` FROM `customer` INNER JOIN `user_status` "
                    + "INNER JOIN `gender` ON `customer`.`user_status_id` = `user_status`.`id` AND `customer`.`gender_id` = `gender`.`id` ORDER BY `customer`.`id` ASC");

            //DefaultTableModel model1 = (DefaultTableModel) jTable1.getModel();
            ((DefaultTableModel) jTable1.getModel()).setRowCount(0);

            while (search.next()) {
                if (Integer.parseInt(search.getString("id")) != 1) {
                    Vector v = new Vector();
                    v.add(search.getString("id"));
                    v.add(search.getString("fname") + " " + search.getString("lname"));
                    v.add(search.getString("contact_no"));
                    v.add(search.getString("gender"));
                    v.add(search.getString("dob"));
                    v.add(search.getString("address"));
                    v.add(search.getString("email"));
                    v.add(search.getString("customer_status"));

                    ((DefaultTableModel) jTable1.getModel()).addRow(v);
                }

            }
            jTable1.setModel(((DefaultTableModel) jTable1.getModel()));
            //table cell render

        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(jTable1.getRowCount()); 
    }

    private void loadCustomers(String text) {
        try {
            ResultSet search = MySQL.search("SELECT `customer`.`id`, `customer`.`fname`, `customer`.`lname`, `customer`.`contact_no`"
                    + ", `customer`.`address`, `user_status`.`name` AS `customer_status`,`gender`.`name` AS `gender`,`customer`.`email`,`customer`.`dob` FROM `customer` INNER JOIN `user_status` "
                    + "INNER JOIN `gender` ON `customer`.`user_status_id` = `user_status`.`id` AND `customer`.`gender_id` = `gender`.`id` WHERE `customer`.`fname` LIKE '" + text + "%'"
                    + "OR `customer`.`lname` LIKE '" + text + "%' OR `customer`.`contact_no` LIKE '" + text + "%' ORDER BY `customer`.`id` ASC");

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (search.next()) {
                if (search.getString("id") != "1") {
                    Vector v = new Vector();
                    v.add(search.getString("id"));
                    v.add(search.getString("fname") + " " + search.getString("lname"));
                    v.add(search.getString("contact_no"));
                    v.add(search.getString("gender"));
                    v.add(search.getString("dob"));
                    v.add(search.getString("address"));
                    v.add(search.getString("email"));
                    v.add(search.getString("customer_status"));
                    model.addRow(v);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadGenderTypes() {

        Vector v = new Vector();

        v.add("Select");
        v.add("Male");
        v.add("Female");

        jComboBox2.setModel(new DefaultComboBoxModel(v));

    }

    private void clearFields() {

        jTextField1.setText("");
        txt_lname.setText("");
        jTextField4.setText("");
        txt_address.setText("");
        txt_email.setText("");
        jComboBox2.setSelectedIndex(0);
        jDateChooser1.setDate(null);
        jTextField1.grabFocus();
    }

    private void setListner() {

        ListSelectionListener lsl = (ListSelectionEvent e) -> {

            int selectedRow = jTable1.getSelectedRow();

            if (selectedRow != -1) {

                String id = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();

                try {
                    ResultSet rs = MySQL.search("SELECT `customer`.`fname`, `customer`.`lname`, `customer`.`contact_no`, `customer`.`dob` "
                            + ", `customer`.`address`,`gender`.`name` AS `gender`,`customer`.`email` FROM `customer` INNER JOIN `gender` ON `customer`.`gender_id`=`gender`.`id` WHERE `customer`.`id`='" + Integer.parseInt(id) + "'");
                    rs.next();
                    jTextField1.setText(rs.getString("fname"));
                    txt_lname.setText(rs.getString("lname"));

                    jTextField4.setText(rs.getString("contact_no"));

                    jDateChooser1.setDate(rs.getDate("dob"));
                    txt_address.setText(rs.getString("address"));
                    jComboBox2.setSelectedItem(rs.getString("gender"));
                    txt_email.setText(rs.getString("email"));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                jButton1.setEnabled(false);

                //jTable1.clearSelection();
            }
        };

        jTable1.getSelectionModel().addListSelectionListener(lsl);

    }

    /**
     * Creates new form UserRegistration
     */
    public CustomerRegistration() {
        initComponents();

        loadCustomers();
        loadGenderTypes();
        dateTextField();
        setListner();
        Image ic = new ImageIcon(this.getClass().getResource("/resources/medicare.png")).getImage();
        this.setIconImage(ic);

    }

    Invoice invoice;
    int invoicevisible = 0;

    public CustomerRegistration(Invoice invoice) {

        this.invoice = invoice;
        initComponents();

        loadCustomers();
        loadGenderTypes();
        dateTextField();
        setListner();
        invoicevisible = 1;
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
        jLabel4 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txt_lname = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txt_address = new javax.swing.JTextField();
        txt_email = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1280, 356));

        jLabel1.setText("First name");

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

        jLabel7.setText("Date of Birth");

        jDateChooser1.setForeground(new java.awt.Color(204, 255, 255));

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton4.setText("Update Account");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel1)
                            .addComponent(jLabel10)
                            .addComponent(jLabel8)
                            .addComponent(jLabel4)
                            .addComponent(jLabel11)
                            .addComponent(jLabel7))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_lname)
                            .addComponent(txt_address)
                            .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField1)
                            .addComponent(jTextField4)
                            .addComponent(txt_email)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton1, jButton2, jButton4});

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
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_address, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Contact No.", "Gender", "Date of Birth", "Address", "Email", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

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
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(30);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(130);
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(60);
            jTable1.getColumnModel().getColumn(7).setPreferredWidth(40);
        }

        jLabel9.setText("Search Customer");

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
                .addGap(7, 7, 7)
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

        loadCustomers(text);
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            int r = jTable1.getSelectedRow();

            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Please select a customer", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {

                if (invoice != null) {
                    String sid = jTable1.getValueAt(r, 0).toString();
                    String sname = jTable1.getValueAt(r, 1).toString();
                    String smobile = jTable1.getValueAt(r, 2).toString();
                    try {
                        LocalDate curdate = LocalDate.now();
                        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate dob = LocalDate.parse((CharSequence) jTable1.getValueAt(r, 4));
                        System.out.println(dob);
                        int age = Period.between(dob, curdate).getYears();
                        invoice.jLabel8.setText(Integer.toString(age));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    invoice.jLabel6.setText(sid);
                    invoice.jLabel7.setText(sname);
                    invoice.jLabel5.setText(smobile);

                    this.dispose();
                }
            }
        }
        if (evt.getClickCount() == 1) {

            String id = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();

            try {
                ResultSet rs = MySQL.search("SELECT `customer`.`fname`, `customer`.`lname`, `customer`.`contact_no`, `customer`.`dob` "
                        + ", `customer`.`address`,`gender`.`name` AS `gender`,`customer`.`email` FROM `customer` INNER JOIN `gender` ON `customer`.`gender_id`=`gender`.`id` WHERE `customer`.`id`='" + Integer.parseInt(id) + "'");
                rs.next();
                jTextField1.setText(rs.getString("fname"));
                txt_lname.setText(rs.getString("lname"));

                jTextField4.setText(rs.getString("contact_no"));

                jDateChooser1.setDate(rs.getDate("dob"));
                txt_address.setText(rs.getString("address"));
                jComboBox2.setSelectedItem(rs.getString("gender"));
                txt_email.setText(rs.getString("email"));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        String fname = jTextField1.getText().trim();
        String lname = txt_lname.getText().trim();
        String mobile = jTextField4.getText().trim();
        String address = txt_address.getText();
        String gender = jComboBox2.getSelectedItem().toString();
        String email = txt_email.getText().trim();
        Date dob = jDateChooser1.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        if (fname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter first name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (lname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter last name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (mobile.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter contact number", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("07[01245678][0-9]{7}").matcher(mobile).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid contact number", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (gender.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select gender", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (dob == null) {
            JOptionPane.showMessageDialog(this, "Please select date of birth", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter address", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter email", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$").matcher(email).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid email", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            try {

                ResultSet rs1 = MySQL.search("SELECT `id` FROM `gender` WHERE `name` = '" + gender + "'");
                rs1.next();

                String gender_id = rs1.getString("id");

                MySQL.iud("INSERT INTO `customer`(`fname`, `lname`,  `contact_no`, `gender_id`,`dob`,`address`,`email`) VALUES('" + fname + "', '" + lname + "', '" + mobile + "', " + gender_id + ",'" + sdf.format(dob) + "', '" + address + "', '" + email + "')");

                JOptionPane.showMessageDialog(this, "New customer account created!", "Success", JOptionPane.INFORMATION_MESSAGE);

                loadCustomers();

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

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:

        int selecteRow = jTable1.getSelectedRow();

        if (selecteRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer", "Warning", JOptionPane.WARNING_MESSAGE);

        } else {

            String id = jTable1.getValueAt(selecteRow, 0).toString();
            String currentStatus = jTable1.getValueAt(selecteRow, 7).toString();

            int status;

            if (currentStatus.equals("Active")) {
                status = 2;
            } else {
                status = 1;
            }

            MySQL.iud("UPDATE `customer` SET `user_status_id` = " + status + " WHERE `id` = " + Integer.parseInt(id) + "");

            loadCustomers();

            JOptionPane.showMessageDialog(this, "Customer status updated", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        if (!jTable1.getSelectionModel().isSelectionEmpty()) {
            String id = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();
            String fname = jTextField1.getText().trim();
            String lname = txt_lname.getText().trim();

            String mobile = jTextField4.getText().trim();

            String address = txt_address.getText();
            String gender = jComboBox2.getSelectedItem().toString();
            String email = txt_email.getText().trim();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dob = jDateChooser1.getDate();

            String genderid = null;

            if (!Pattern.compile("07[01245678][0-9]{7}").matcher(mobile).matches()) {
                JOptionPane.showMessageDialog(this, "Invalid contact number", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (gender.equals("Select")) {
                JOptionPane.showMessageDialog(this, "Please select gender", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (dob == null) {
                JOptionPane.showMessageDialog(this, "Please select date of birth", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (!Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$").matcher(email).matches()) {
                JOptionPane.showMessageDialog(this, "Invalid email", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                try {

                    ResultSet rs1 = MySQL.search("SELECT * FROM `gender` WHERE `name`='" + gender + "'");
                    rs1.next();
                    genderid = rs1.getString("id");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!fname.isEmpty()) {
                    MySQL.iud("UPDATE `customer` SET `fname`='" + fname + "' WHERE `id`='" + Integer.parseInt(id) + "'");
                }
                if (!lname.isEmpty()) {
                    MySQL.iud("UPDATE `customer` SET `lname`='" + lname + "' WHERE `id`='" + Integer.parseInt(id) + "'");
                }

                if (!mobile.isEmpty()) {
                    MySQL.iud("UPDATE `customer` SET `contact_no`='" + mobile + "' WHERE `id`='" + Integer.parseInt(id) + "'");
                }

                if (!address.isEmpty()) {
                    MySQL.iud("UPDATE `customer` SET `address`='" + address + "' WHERE `id`='" + Integer.parseInt(id) + "'");
                }
                if (!gender.isEmpty()) {
                    MySQL.iud("UPDATE `customer` SET `gender_id`='" + genderid + "' WHERE `id`='" + Integer.parseInt(id) + "'");
                }
                if (!email.isEmpty()) {
                    MySQL.iud("UPDATE `customer` SET `email`='" + email + "' WHERE `id`='" + Integer.parseInt(id) + "'");
                }
                if (!sdf.format(jDateChooser1.getDate()).isEmpty()) {
                    MySQL.iud("UPDATE `customer` SET `dob`='" + sdf.format(jDateChooser1.getDate()) + "' WHERE `id`='" + Integer.parseInt(id) + "'");
                }
                clearFields();
                jTable1.getSelectionModel().clearSelection();
                jButton1.setEnabled(true);
                loadCustomers();
            }

        } else {
            JOptionPane.showMessageDialog(this, "Please select a customer", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

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
                new CustomerRegistration().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox2;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
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
