/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.jmx.mover.gui;

import com.dhenton9000.jmx.service.BASIC_MESSAGE_PROPS;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class MessagePropertiesPanel extends javax.swing.JPanel {

    private JmxMover moverParent = null;
    private final Logger logger = LoggerFactory.getLogger(MessagePropertiesPanel.class);
    private String selectedQueue = null;
    private String selectedMessageId = null;
    private ListSelectionModel messageSelectionModel = new DefaultListSelectionModel();

    /**
     * Creates new form MessagePropertiesPanel
     */
    public MessagePropertiesPanel() {
        initComponents();
        cmboQueueName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JComboBox b = (JComboBox) e.getSource();
                setSelectedQueue((String) b.getModel().getSelectedItem());
                refreshMessageIds();

            }
        });

        tblMessageIds.setSelectionModel(getNewSelectionModel());



    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cmboQueueName = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        scrollPaneForMessageIds = new javax.swing.JScrollPane();
        tblMessageIds = new javax.swing.JTable();
        tabMessageProperties = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMessageProperties = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        btnRefreshMessageProps = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jLabel1.setText("Queue:");

        jLabel2.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jLabel2.setText("Message Ids:");

        tblMessageIds.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        scrollPaneForMessageIds.setViewportView(tblMessageIds);
        this.tblMessageIds.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tblMessageProperties.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblMessageProperties);

        tabMessageProperties.addTab("Basic", jScrollPane1);

        jLabel3.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jLabel3.setText("Message Properties:");

        btnRefreshMessageProps.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnRefreshMessageProps.setText("Refresh");
        btnRefreshMessageProps.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshMessagePropsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(cmboQueueName, javax.swing.GroupLayout.PREFERRED_SIZE, 617, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnRefreshMessageProps, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(scrollPaneForMessageIds, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tabMessageProperties, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(36, 36, 36))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmboQueueName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(btnRefreshMessageProps))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPaneForMessageIds, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tabMessageProperties, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnRefreshMessagePropsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshMessagePropsActionPerformed
        setSelectedMessageId(null);
        refreshMessageIds();
        refreshMessageProperties();
    }//GEN-LAST:event_btnRefreshMessagePropsActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRefreshMessageProps;
    private javax.swing.JComboBox cmboQueueName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane scrollPaneForMessageIds;
    private javax.swing.JTabbedPane tabMessageProperties;
    private javax.swing.JTable tblMessageIds;
    private javax.swing.JTable tblMessageProperties;
    // End of variables declaration//GEN-END:variables

    public void clear() {
        logger.debug("CLEAR called");
        setSelectedQueue(null);
        setSelectedMessageId(null);
        DefaultComboBoxModel itemModel = new DefaultComboBoxModel();
        cmboQueueName.setModel(itemModel);
        TableModel tModel = new NotEditableTableModel();
        tblMessageIds.setModel(tModel);
        messageSelectionModel = new DefaultListSelectionModel();
        tblMessageIds.setSelectionModel(getNewSelectionModel());
        TableModel tModel2 = new NotEditableTableModel();
        tblMessageProperties.setModel(tModel2);
        this.invalidate();
    }

    public void loadMoverLists(List<String> items) {
        Collections.sort(items);
        setSelectedQueue(null);

        DefaultComboBoxModel itemModel = new DefaultComboBoxModel();
        int i = 0;
        for (String x : items) {
            itemModel.insertElementAt(x, i);
            i++;
        }


        cmboQueueName.setModel(itemModel);
        if (itemModel.getSize() > 0) {
            itemModel.setSelectedItem(itemModel.getElementAt(0));
            setSelectedQueue((String) itemModel.getSelectedItem());
        }

        this.invalidate();
    }

    /**
     * @return the moverParent
     */
    public JmxMover getMoverParent() {
        return moverParent;
    }

    /**
     * @param moverParent the moverParent to set
     */
    public void setMoverParent(JmxMover moverParent) {
        this.moverParent = moverParent;
    }

    /**
     * @return the selectedQueue
     */
    public String getSelectedQueue() {
        return selectedQueue;
    }

    /**
     * @param selectedQueue the selectedQueue to set
     */
    public void setSelectedQueue(String selectedQueue) {
        this.selectedQueue = selectedQueue;
    }

    /**
     * @return the selectedMessageId
     */
    public String getSelectedMessageId() {
        return selectedMessageId;
    }

    /**
     * @param selectedMessageId the selectedMessageId to set
     */
    public void setSelectedMessageId(String selectedMessageId) {
        this.selectedMessageId = selectedMessageId;
    }

    private void refreshMessageIds() {
        logger.debug("message id refresh called " + getSelectedQueue());
        if (getSelectedQueue() == null) {
            return;
        }
        HashMap<String, String> qProps = getMoverParent().getQueueProperties(getSelectedQueue());
        if (qProps == null) {
            return;
        }
        logger.debug("heading toward table ");
        List<String> ids = getMoverParent().getQueueMessageIds(getSelectedQueue());
        logger.debug("id length " + ids.size());
        NotEditableTableModel tModel = new NotEditableTableModel();
        if (ids != null && ids.size() > 0) {
            tblMessageIds.setAutoCreateColumnsFromModel(true);
            messageSelectionModel = new DefaultListSelectionModel();
            tblMessageIds.setSelectionModel(getNewSelectionModel());
            ArrayList<String> messageArray = new ArrayList<String>();
            ArrayList<String> timeStampArray = new ArrayList<String>();
            for (String id : ids) {
                //  logger.debug("adding "+id);
                messageArray.add(id);
                HashMap<String, String> props = getMoverParent().getMessageProperties(getSelectedQueue(), id);
                String tStamp = props.get(BASIC_MESSAGE_PROPS.JMS_Timestamp.getPropertyValue());
                timeStampArray.add(tStamp);
            }



            tModel.addColumn("Message id", messageArray.toArray());
            tModel.addColumn("Time", timeStampArray.toArray());

        }
        this.tblMessageIds.setModel(tModel);
        refreshMessageProperties();
        this.invalidate();
    }

    private void refreshMessageProperties() {
        logger.debug("refresh Message Properties called " + getSelectedMessageId());

        NotEditableTableModel tModel = new NotEditableTableModel();
        HashMap<String, String> props =
                this.getMoverParent().getMessageProperties(getSelectedQueue(), getSelectedMessageId());

        if (props != null && props.keySet().size() > 0) {
            ArrayList<String> valueArray = new ArrayList<String>();
            ArrayList<String> nameArray = new ArrayList<String>();
            for (String k : props.keySet()) {
                nameArray.add(k.trim());
            }

            tModel.addColumn("Property", nameArray.toArray());
            for (String k : props.keySet()) {
                valueArray.add(props.get(k));
            }
            tModel.addColumn("Value", valueArray.toArray());

            
            this.tblMessageProperties.setModel(tModel);
            tblMessageProperties.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
            if (tblMessageProperties.getColumnCount() == 2) {
                int oSize = tblMessageProperties.getWidth() - 120;
                tblMessageProperties.getColumnModel().getColumn(0).setPreferredWidth(120);
                tblMessageProperties.getColumnModel().getColumn(1).setPreferredWidth(oSize);
                tblMessageProperties.doLayout();
            }
        }
        else
        {
              this.tblMessageProperties.setModel(tModel);
        }



    }

    private ListSelectionModel getNewSelectionModel() {
        messageSelectionModel = new DefaultListSelectionModel();
        messageSelectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int row = messageSelectionModel.getMinSelectionIndex();
                    if (row > -1) {
                        String t = (String) tblMessageIds.getModel().getValueAt(row, 0);
                        setSelectedMessageId(t);
                        refreshMessageProperties();
                    }
                }

            }
        });
        return messageSelectionModel;
    }

//    private ListSelectionModel getNewPropertiesSelectionModel() {
//        return new DefaultListSelectionModel();
//    }
    class NotEditableTableModel extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }
}
