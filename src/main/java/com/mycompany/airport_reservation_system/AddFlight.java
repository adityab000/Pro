/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.mycompany.airport_reservation_system;

import static java.awt.Color.blue;
import java.awt.Graphics;
import java.awt.Image;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author ADITYA
 */
public class AddFlight extends javax.swing.JInternalFrame {
    
    class BackgroundPanel extends javax.swing.JPanel {
        public Image backgroundImage;

        public BackgroundPanel() {
            backgroundImage = new ImageIcon(
                    "C:\\Users\\ADITYA\\OneDrive\\Desktop\\images\\customer.jpg"
            ).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /**
     * Creates new form AddCustomer
     */
    public AddFlight() {
        setContentPane(new AddFlight.BackgroundPanel());
        initComponents();
        
        jPanel1 = AppTheme.replaceWithGlassPanel(this, jPanel1, 80, 20);
        jPanel2 = AppTheme.replaceWithGlassPanel(this, jPanel2, 80, 20);
    
        AppTheme.styleTitle(jLabel1);
        AppTheme.styleLabels(jLabel2, jLabel3, jLabel4, jLabel5,
                             jLabel6, jLabel8, jLabel9, jLabel11);
        AppTheme.styleTextFields(flightid, flightName, arrival,
                                 departure, duration, seats, fare);
        AppTheme.stylePrimaryButton(jButton1);  // Add    → blue
        AppTheme.styleDangerButton(jButton2);
        this.getContentPane().setBackground(blue);
        AutoID();
    }
    
    private void AutoID() {
    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/Airline_Project", "root", "Ab9797@bhoir");
         PreparedStatement pre = con.prepareStatement("SELECT MAX(FlightID) FROM flight")) {
        Class.forName("com.mysql.cj.jdbc.Driver");
        ResultSet rs = pre.executeQuery();
        
        if (rs.next()) {
            String maxId = rs.getString("MAX(FlightID)");
            if (maxId != null) {
                
                long id = Long.parseLong(maxId.substring(2));  // Extract digits after "FL"
                id++;
                flightid.setText("FL" + String.format("%03d", id));
            } else {
                // No flights in DB, start with FL001
                flightid.setText("FL001");
            }
        } else {
            
            flightid.setText("FL001");
        }
        
    } catch (ClassNotFoundException | SQLException | NumberFormatException ex) {
        Logger.getLogger(AddFlight.class.getName()).log(Level.SEVERE, null, ex);  // Fixed class name
        JOptionPane.showMessageDialog(null, "Error generating Flight ID: " + ex.getMessage());
        // Fallback: Set a default ID if generation fails
        flightid.setText("FL001");
    }
}
     // Validation and Verification Methods
    private boolean validateInputs() {
       
        if (flightid.getText().trim().isEmpty() || flightName.getText().trim().isEmpty() ||
            arrival.getText().trim().isEmpty() || departure.getText().trim().isEmpty() ||
            duration.getText().trim().isEmpty() || seats.getText().trim().isEmpty() ||
            fare.getText().trim().isEmpty() || date.getDate() == null) {
            JOptionPane.showMessageDialog(null, "All fields must be filled!");
            return false;
        }
        
    
        try {
            int seatCount = Integer.parseInt(seats.getText().trim());
            double fareAmount = Double.parseDouble(fare.getText().trim());
            if (seatCount <= 0 || fareAmount <= 0) {
                JOptionPane.showMessageDialog(null, "Seats and Fare must be positive numbers!");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Seats and Fare must be valid numbers!");
            return false;
        }
        
        String arrivalText = arrival.getText().trim();
        String departureText = departure.getText().trim();
        String flightnamecheck = flightName.getText().trim();
        if (!arrivalText.matches("^[A-Z][a-z]*$")) {
            JOptionPane.showMessageDialog(null, "Arrival must start with a capital letter and contain only lowercase letters after (e.g., 'Delhi'). No numbers or special characters!");
            return false;
        }
        if (!departureText.matches("^[A-Z][a-z]*$")) {
            JOptionPane.showMessageDialog(null, "Departure must start with a capital letter and contain only lowercase letters after (e.g., 'Mumbai'). No numbers or special characters!");
            return false;
        }
        if (!flightnamecheck.matches("^[A-Z][a-z]*$")) {
            JOptionPane.showMessageDialog(null, "Flight Name must start with a capital letter and contain only lowercase letters after (e.g., 'Airindia'). No numbers or special characters!");
            return false;
        }
        
        String durationText = duration.getText().trim();
        if (durationText.matches("^\\d{2}$")) {
            // Exactly two digits: Accept as-is (e.g., "12")
            if ("00".equals(durationText)) {
            JOptionPane.showMessageDialog(null, "Duration cannot be zero (e.g., '0', '00', or '00:00'). Please enter a valid duration.");
            return false;
        } else{
            duration.setText(durationText +":00");
            }
        } else if (durationText.matches("^\\d{2}:\\d{2}$")) {
            // "HH:MM" format: Accept as-is (e.g., "12:30")
            if ("00:00".equals(durationText)) {
            JOptionPane.showMessageDialog(null, "Duration cannot be zero (e.g., '0', '00', or '00:00'). Please enter a valid duration.");
            return false;
        }
            
        } else if (durationText.matches("^\\d{1}$")) {
            // Single digit: Pad with leading zero (e.g., "2" -> "02")
            if ("0".equals(durationText)) {
            JOptionPane.showMessageDialog(null, "Duration cannot be zero (e.g., '0', '00', or '00:00'). Please enter a valid duration.");
            return false;
        } else{
            duration.setText("0" + durationText +":00");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Duration must be exactly two digits (e.g., '12') or in 'HH:MM' format (e.g., '12:30'). Single digits will be padded (e.g., '2' becomes '02'). No letters or extra characters!");
            return false;
        }
    
        if (arrival.getText().trim().equalsIgnoreCase(departure.getText().trim())) {
            JOptionPane.showMessageDialog(null, "Arrival and Departure locations cannot be the same!");
            return false;
        }
    
     // Verify date is in the future
        Date selectedDate = date.getDate();
        if (selectedDate.before(new Date())) {
            JOptionPane.showMessageDialog(null, "Flight date must be in the future!");
            return false;
        }

        return true;
    }
    
//    private boolean verifyFlightUniqueness(String arrival, String departure, String dateStr) {
//        // Optional: Check for duplicate flights (e.g., same route and date)
//        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/Airline_Project", "root", "Ab9797@bhoir");
//             PreparedStatement pre = con.prepareStatement("SELECT COUNT(*) FROM flight WHERE Arrival = ? AND Departure = ? AND Date = ?")) {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            pre.setString(1, arrival);
//            pre.setString(2, departure);
//            pre.setString(3, dateStr);
//            ResultSet rs = pre.executeQuery();
//            if (rs.next() && rs.getInt(1) > 0) {
//                JOptionPane.showMessageDialog(null, "A flight with the same route and date already exists!");
//                return false;
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(AddFlight.class.getName()).log(Level.SEVERE, null, ex);
//            JOptionPane.showMessageDialog(null, "Error verifying flight uniqueness: " + ex.getMessage());
//            return false;
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(AddFlight.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return true;
//    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField5 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        flightid = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        flightName = new javax.swing.JTextField();
        arrival = new javax.swing.JTextField();
        departure = new javax.swing.JTextField();
        duration = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        seats = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        date = new com.toedter.calendar.JDateChooser();
        fare = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel10.setText("jLabel10");

        setClosable(true);

        jLabel1.setFont(new java.awt.Font("Leelawadee UI", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Welcome to the Flight Panel");

        jPanel1.setBackground(new java.awt.Color(0, 51, 204));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setOpaque(false);

        jLabel2.setFont(new java.awt.Font("Leelawadee UI", 1, 12)); // NOI18N
        jLabel2.setText("Flight ID");

        flightid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                flightidActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Leelawadee UI", 1, 12)); // NOI18N
        jLabel3.setText("Flight Name");

        jLabel4.setFont(new java.awt.Font("Leelawadee UI", 1, 12)); // NOI18N
        jLabel4.setText("Arrival");

        jLabel5.setFont(new java.awt.Font("Leelawadee UI", 1, 12)); // NOI18N
        jLabel5.setText("Departure");

        jLabel6.setFont(new java.awt.Font("Leelawadee UI", 1, 12)); // NOI18N
        jLabel6.setText("Duration");

        jLabel7.setFont(new java.awt.Font("Leelawadee UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));

        flightName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                flightNameActionPerformed(evt);
            }
        });

        arrival.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arrivalActionPerformed(evt);
            }
        });

        departure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                departureActionPerformed(evt);
            }
        });

        duration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                durationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(duration, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(flightid, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(flightName, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(arrival, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(departure, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(flightid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(flightName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(arrival, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(departure, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(duration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(jLabel7)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(0, 51, 204));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.setOpaque(false);

        jLabel8.setFont(new java.awt.Font("Leelawadee UI", 1, 12)); // NOI18N
        jLabel8.setText("Seats");

        seats.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatsActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Leelawadee UI", 1, 12)); // NOI18N
        jLabel9.setText("Fare");

        jLabel11.setFont(new java.awt.Font("Leelawadee UI", 1, 12)); // NOI18N
        jLabel11.setText("Date of Flight");

        fare.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fareActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(seats, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fare, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(seats, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(fare, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("cancel");
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
                .addContainerGap(120, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(94, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(85, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE)
                        .addComponent(jButton2)))
                .addContainerGap(85, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 110, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1)
                            .addComponent(jButton2)))))
        );

        jPanel1.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void seatsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seatsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_seatsActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
            // Validate inputs first
        if (!validateInputs()) {
            return;
        }
        String flightID = flightid.getText().trim();
        String FlightName = flightName.getText().trim();
        String arrivalLoc = arrival.getText().trim();
        String departureLoc = departure.getText().trim();
        String durationVal = duration.getText().trim();
        String seatVal = seats.getText().trim();
        String fareVal = fare.getText().trim();
        String status = "Scheduled";
            
        DateFormat da = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = da.format(date.getDate());
        
        // Verify uniqueness
//        if (!verifyFlightUniqueness(arrivalLoc, departureLoc, dateStr)) {
//            return;
//        }
        
  
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/Airline_Project", "root", "Ab9797@bhoir");
             PreparedStatement pre = con.prepareStatement("INSERT INTO flight (FlightID, FlightName, Arrival, Departure, Duration, Seats, Fare, Date, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            pre.setString(1, flightID);
            pre.setString(2, FlightName);
            pre.setString(3, arrivalLoc);
            pre.setString(4, departureLoc);
            pre.setString(5, durationVal);
            pre.setString(6, seatVal);
            pre.setString(7, fareVal);
            pre.setString(8, dateStr);
            pre.setString(9, status);

            pre.executeUpdate();
            JOptionPane.showMessageDialog(null, "Flight Added Successfully");

            // Clear fields after successful add
            clearFields();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AddFlight.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error adding flight: " + ex.getMessage());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void clearFields() {
        flightid.setText("");
        flightName.setText("");
        arrival.setText("");
        departure.setText("");
        duration.setText("");
        seats.setText("");
        fare.setText("");
        date.setDate(null);
        AutoID(); // Regenerate ID
    }
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int choice = JOptionPane.showConfirmDialog(this,
        "Are you sure you want to cancel flight page?",
        "Confirm Cancel", JOptionPane.YES_NO_OPTION);

    if (choice == JOptionPane.YES_OPTION) {
        this.dispose();                    // ← close SignupFrame
        
    }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void durationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_durationActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_durationActionPerformed

    private void departureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_departureActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_departureActionPerformed

    private void arrivalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrivalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_arrivalActionPerformed

    private void flightNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_flightNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_flightNameActionPerformed

    private void flightidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_flightidActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_flightidActionPerformed

    private void fareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fareActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fareActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField arrival;
    private com.toedter.calendar.JDateChooser date;
    private javax.swing.JTextField departure;
    private javax.swing.JTextField duration;
    private javax.swing.JTextField fare;
    private javax.swing.JTextField flightName;
    private javax.swing.JTextField flightid;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField seats;
    // End of variables declaration//GEN-END:variables
}
