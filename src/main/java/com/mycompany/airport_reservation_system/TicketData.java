/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.mycompany.airport_reservation_system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author ADITYA
 */
public class TicketData extends javax.swing.JInternalFrame {

    /**
     * Creates new form TicketData
     */
    public TicketData() {
        initComponents();
        AutoDATA();
    }

    public void AutoDATA()
    {
        try {
            
            String query ="SELECT * FROM ticket";
            Connection con;
            PreparedStatement pre;
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/Airline_Project","root","Ab9797@bhoir");
            pre = con.prepareStatement(query);
            
            ResultSet rs = pre.executeQuery();       
            ResultSetMetaData RSMD = rs.getMetaData();
            int cc = RSMD.getColumnCount();
            TableModel model = table.getModel();
            if (!(model instanceof DefaultTableModel)) {
                JOptionPane.showMessageDialog(this, "Table model is not a DefaultTableModel. Cannot update table.");
                return;
            }
            DefaultTableModel DFT = (DefaultTableModel) model;
            DFT.setRowCount(0);
            
            while(rs.next())
            {
                  Vector v = new Vector();
//                Vector v = new Vector();
          
                    v.add(rs.getString("TicketID"));
                    v.add(rs.getString("FlightID"));
                    v.add(rs.getString("CustomerID"));
                    v.add(rs.getString("Arrival"));
                    v.add(rs.getString("Departure"));
                    v.add(rs.getString("FirstName"));
                    v.add(rs.getString("LastName"));
                    v.add(rs.getString("Contact"));
                    v.add(rs.getString("Gender"));
                    v.add(rs.getString("Status"));
//                    TicketID,FlightID,CustomerID,Arrival,Departure,FirstName,LastName,Contact,Gender,Status
                    DFT.addRow(v);
            }
            
           con.close();
            
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(CustomerData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setClosable(true);

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Ticket ID", "Flight ID", "Customer ID", "Arrival", "Departure", "First Name", "Last Name", "Gender", "Contact", "Status"
            }
        ));
        jScrollPane1.setViewportView(table);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 745, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
