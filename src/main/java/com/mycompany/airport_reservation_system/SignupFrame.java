/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.airport_reservation_system;

import java.awt.Graphics;
import java.awt.Image;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author ADITYA
 */
public class SignupFrame extends javax.swing.JFrame {
    
    private static final int MIN_USERNAME_LENGTH = 3;
    private static final int MAX_USERNAME_LENGTH = 20;
    private static final int MIN_PASSWORD_LENGTH = 4;
    private static final int MAX_PASSWORD_LENGTH = 20;
    
    private static final String DB_URL = "jdbc:mysql://localhost/Airline_Project";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Ab9797@bhoir";
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    

     class BackgroundPanel extends javax.swing.JPanel {
        public Image backgroundImage;

        public BackgroundPanel() {
            backgroundImage = new ImageIcon(
                    "C:\\Users\\ADITYA\\OneDrive\\Desktop\\images\\sign.jpg"
            ).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    /**
     * Creates new form SignupFrame
     */
    public SignupFrame() {
        
        setContentPane(new SignupFrame.BackgroundPanel());
        initComponents();
        
        jPanel1 = AppTheme.replaceWithGlassPanel(this, jPanel1, 80, 20);
        
        AppTheme.styleTitle(jLabel1);
        AppTheme.styleLabels(jLabel2, jLabel3);
        AppTheme.styleTextField(username);
        AppTheme.stylePasswordField(password);
        AppTheme.stylePrimaryButton(jButton1);  // Sign Up → blue
        AppTheme.styleDangerButton(jButton2);
        
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setFont(AppTheme.FONT_TITLE);
        jLabel1.setForeground(java.awt.Color.WHITE);
    
        javax.swing.JPanel wrapper = new javax.swing.JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new java.awt.BorderLayout(0, 20));
        wrapper.add(jLabel1, java.awt.BorderLayout.NORTH);
        wrapper.add(jPanel1, java.awt.BorderLayout.CENTER);
    
        javax.swing.JPanel btnPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 30, 10));
        btnPanel.setOpaque(false);
        btnPanel.add(jButton1);
        btnPanel.add(jButton2);
        wrapper.add(btnPanel, java.awt.BorderLayout.SOUTH);
        
        javax.swing.JPanel contentPane = (javax.swing.JPanel) getContentPane();
        contentPane.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        contentPane.add(wrapper, gbc);   
        
        setLocationRelativeTo(null);
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
    }
    
   private String autoID() {
        String query = "SELECT MAX(ID) FROM login";
        try (Connection con = getConnection();
             PreparedStatement pre = con.prepareStatement(query);
             ResultSet rs = pre.executeQuery()) {

            rs.next();
            String maxId = rs.getString("MAX(ID)");
            if (maxId == null) {
                return "ID001";
            }
            long id = Long.parseLong(maxId.substring(2)) + 1;
            return "ID" + String.format("%03d", id);

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SignupFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
   
   private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DB_DRIVER);
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
   
//   private String hashPassword(String plainText) {
//        try {
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            byte[] hash = md.digest(plainText.getBytes());
//            StringBuilder sb = new StringBuilder();
//            for (byte b : hash) {
//                sb.append(String.format("%02x", b));
//            }
//            return sb.toString();
//        } catch (NoSuchAlgorithmException ex) {
//            Logger.getLogger(SignupFrame.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return plainText; // fallback (should not happen)
//    }
   
   private boolean isUsernameExists(String userName) {
        String query = "SELECT COUNT(*) FROM login WHERE Username = ?";
        try (Connection con = getConnection();
             PreparedStatement pre = con.prepareStatement(query)) {

            pre.setString(1, userName);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SignupFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
   
   private boolean validateCredentialsFormat(String userName, String password) {

        // Username length
        if (userName.length() < MIN_USERNAME_LENGTH || userName.length() > MAX_USERNAME_LENGTH) {
            JOptionPane.showMessageDialog(this,
                "Username must be between " + MIN_USERNAME_LENGTH +
                " and " + MAX_USERNAME_LENGTH + " characters!",
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            username.requestFocus();
            return false;
        }
        
        
        
        if (!userName.matches("^[a-zA-Z0-9_]+$")) {
            JOptionPane.showMessageDialog(this,
                "Username can only contain letters, numbers, and underscores!",
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            username.requestFocus();
            return false;
        }

        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            JOptionPane.showMessageDialog(this,
                "Password must be between " + MIN_PASSWORD_LENGTH +
                " and " + MAX_PASSWORD_LENGTH + " characters!",
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            this.password.requestFocus();
            return false;
        }

        // Password strength: at least one digit, one letter
        if (!password.matches(".*[a-zA-Z].*") || !password.matches(".*[0-9].*")) {
            JOptionPane.showMessageDialog(this,
                "Password must contain at least one letter and one number!",
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            this.password.requestFocus();
            return false;
        }

        return true;
    }
   
   private void clearFields() {
        username.setText("");
        password.setText("");
        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        password = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel2.setText("USERNAME");

        jLabel3.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel3.setText("PASSWORD");

        username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(username, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                    .addComponent(password, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(username, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(password, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        jLabel1.setText("Welcome to the Sign up page");
        jLabel1.setAlignmentX(0.5F);

        jButton1.setText("Sign Up");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("CANCLE");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(57, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(53, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton1, jButton2});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton1, jButton2});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private void confirmPasswordActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        // Trigger signup when Enter is pressed in confirm password field
        jButton1ActionPerformed(evt);
    }   
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        if (!validateInput()) {
            return;
        }
        
        String userName = username.getText().trim();
        String pwd  = new String(password.getPassword());
        
       
        
        if (!validateCredentialsFormat(userName, pwd)) {
            return;
        }
        
        if (isUsernameExists(userName)) {
            JOptionPane.showMessageDialog(this,
                "Username '" + userName + "' is already taken!\nPlease choose a different username.",
                "Username Exists", JOptionPane.ERROR_MESSAGE);
            username.setText("");
            username.requestFocus();
            return;
        }

        
        
            // TODO add your handling code here:

            String nid = autoID();
            if (nid == null) {
                JOptionPane.showMessageDialog(this, 
                    "Failed to generate user ID. Please try again.", 
                    "ID Generation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
//        String hashedPassword = hashPassword(pwd);
        String role = "Customer";
        String insertQuery = "INSERT INTO login(ID, Username, Password, Role) VALUES (?, ?, ?, ?)";                   

        try (Connection con = getConnection();
             PreparedStatement pre = con.prepareStatement(insertQuery)) {

            pre.setString(1, nid);
            pre.setString(2, userName);
//            pre.setString(3, hashedPassword);  // ← hashed, not plain text
            pre.setString(3, pwd);
            pre.setString(4, role);

            int rows = pre.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this,
                    "Sign Up Successful!\n" +
                    "Your User ID : " + nid + "\n" +
                    "Username     : " + userName + "\n" +
                    "Role         : " + role,
                    "Registration Complete", JOptionPane.INFORMATION_MESSAGE);

                clearFields();
                AppTheme.transition(this, () -> new LoginFrame(), AppTheme.TransitionType.SLIDE_RIGHT);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Sign Up Failed! Please try again.",
                    "Registration Failed", JOptionPane.ERROR_MESSAGE);
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SignupFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                "Database error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void usernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameActionPerformed
        // TODO add your handling code here:
        password.requestFocus();
    }//GEN-LAST:event_usernameActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int choice = JOptionPane.showConfirmDialog(this,
        "Are you sure you want to cancel sign up?",
        "Confirm Cancel", JOptionPane.YES_NO_OPTION);

    if (choice == JOptionPane.YES_OPTION) {
        AppTheme.transition(this, () -> new LoginFrame(), AppTheme.TransitionType.SLIDE_RIGHT);
    }
    }//GEN-LAST:event_jButton2ActionPerformed

    private boolean validateInput() {
        String userName   = username.getText().trim();
        String pwd        = new String(password.getPassword());
        
        
        if (userName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty!",
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            username.requestFocus();
            return false;
        }
        if (pwd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty!",
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            password.requestFocus();
            return false;
        }
        
        return true;
        
        
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info :
                    javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(SignupFrame.class.getName()).log(Level.SEVERE, null, ex);
        }//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
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
            java.util.logging.Logger.getLogger(SignupFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SignupFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SignupFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SignupFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new SignupFrame().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField password;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}
