/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso.gui;
import improviso.*;
import java.io.*;
import java.awt.Dimension;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author User
 */
public class Main extends javax.swing.JFrame {
    final private CompositionController controller = new CompositionController();
    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnLoadFile = new javax.swing.JButton();
        btnPlayFile = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        listSections = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        panelSection = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        comboSectionType = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtFixedSectionLengthMin = new javax.swing.JTextField();
        txtFixedSectionLengthMax = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtSectionTempo = new javax.swing.JTextField();
        chkSectionInterruptTracks = new javax.swing.JCheckBox();
        btnSectionApply = new javax.swing.JButton();
        btnSectionPlay = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(900, 600));

        btnLoadFile.setText("File");
        btnLoadFile.setName("btnLoadFile"); // NOI18N
        btnLoadFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadFileActionPerformed(evt);
            }
        });

        btnPlayFile.setText("Play");
        btnPlayFile.setEnabled(false);
        btnPlayFile.setName("btnPlayFile"); // NOI18N
        btnPlayFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayButtonActionPerformed(evt);
            }
        });

        listSections.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listSections.setName("listSections"); // NOI18N
        listSections.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listSectionsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(listSections);

        jLabel1.setText("Sections");

        panelSection.setBorder(javax.swing.BorderFactory.createTitledBorder("Section"));

        jLabel2.setText("Type:");

        comboSectionType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Fixed", "Variable" }));
        comboSectionType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboSectionTypeActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setText("Length:");

        txtFixedSectionLengthMin.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFixedSectionLengthMinFocusLost(evt);
            }
        });

        txtFixedSectionLengthMax.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFixedSectionLengthMaxFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFixedSectionLengthMin, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtFixedSectionLengthMax, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(321, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtFixedSectionLengthMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFixedSectionLengthMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel4.setText("Tempo:");

        chkSectionInterruptTracks.setText("Interrupt tracks?");

        btnSectionApply.setText("Apply");
        btnSectionApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSectionApplyActionPerformed(evt);
            }
        });

        btnSectionPlay.setText("Play");
        btnSectionPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSectionPlayActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelSectionLayout = new javax.swing.GroupLayout(panelSection);
        panelSection.setLayout(panelSectionLayout);
        panelSectionLayout.setHorizontalGroup(
            panelSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSectionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelSectionLayout.createSequentialGroup()
                        .addGroup(panelSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelSectionLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(comboSectionType, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelSectionLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSectionTempo, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(chkSectionInterruptTracks)
                            .addGroup(panelSectionLayout.createSequentialGroup()
                                .addComponent(btnSectionApply)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnSectionPlay)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelSectionLayout.setVerticalGroup(
            panelSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSectionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(comboSectionType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtSectionTempo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkSectionInterruptTracks)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(panelSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSectionApply)
                    .addComponent(btnSectionPlay))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelSection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnLoadFile)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnPlayFile))
                            .addComponent(jLabel1))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLoadFile)
                    .addComponent(btnPlayFile))
                .addGap(52, 52, 52)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelSection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap(232, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoadFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadFileActionPerformed
        final JFileChooser fc = new JFileChooser();
        fc.setPreferredSize(new Dimension(1200, 800));
        fc.setCurrentDirectory(new File("."));
        if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                controller.openComposition(fc.getSelectedFile().getAbsolutePath());
                this.btnPlayFile.setEnabled(true);
                this.listSections.setListData(controller.getSectionList());
            } catch (ImprovisoException ex) {
                JOptionPane.showMessageDialog(rootPane, "Error creating composition: " + ex.getMessage());
            } catch (ParserConfigurationException ex) {
                JOptionPane.showMessageDialog(rootPane, "Configuration error: " + ex.getMessage());
            } catch (SAXException ex) {
                JOptionPane.showMessageDialog(rootPane, "Error parsing XML file: " + ex.getMessage());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, "Error reading file: " + ex.getMessage());
            }
        };
        
    }//GEN-LAST:event_btnLoadFileActionPerformed

    private void btnPlayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayButtonActionPerformed
        if (controller.isCompositionLoaded()) {
            try {
                controller.playComposition();
            } catch (InvalidMidiDataException | ImprovisoException | IOException | MidiUnavailableException ex) {
                JOptionPane.showMessageDialog(rootPane, "Error playing composition: " + ex.getMessage());
            }
        }
    }//GEN-LAST:event_btnPlayButtonActionPerformed

    private void comboSectionTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboSectionTypeActionPerformed
        if (isSectionTypeFixed()) {
            txtFixedSectionLengthMin.setEnabled(true);
            txtFixedSectionLengthMax.setEnabled(true);
        } else {
            txtFixedSectionLengthMin.setText("");
            txtFixedSectionLengthMin.setEnabled(false);
            txtFixedSectionLengthMax.setText("");
            txtFixedSectionLengthMax.setEnabled(false);
        }
    }//GEN-LAST:event_comboSectionTypeActionPerformed

    private void listSectionsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listSectionsValueChanged
        loadSectionAttributes(listSections.getSelectedValue());
    }//GEN-LAST:event_listSectionsValueChanged

    private void btnSectionPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSectionPlayActionPerformed
        if (listSections.getSelectedValue() != null) {
            try {
                this.controller.playSection(listSections.getSelectedValue());
            } catch (InvalidMidiDataException | ImprovisoException | IOException | MidiUnavailableException ex) {
                JOptionPane.showMessageDialog(rootPane, "Error playing section: " + ex.getMessage());
            }
        }
    }//GEN-LAST:event_btnSectionPlayActionPerformed

    private void txtFixedSectionLengthMinFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFixedSectionLengthMinFocusLost
        if (!txtFixedSectionLengthMin.getText().isEmpty() && txtFixedSectionLengthMax.getText().isEmpty()) {
            txtFixedSectionLengthMax.setText(txtFixedSectionLengthMin.getText());
        }
    }//GEN-LAST:event_txtFixedSectionLengthMinFocusLost

    private void txtFixedSectionLengthMaxFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFixedSectionLengthMaxFocusLost
        if (!txtFixedSectionLengthMax.getText().isEmpty() && txtFixedSectionLengthMin.getText().isEmpty()) {
            txtFixedSectionLengthMin.setText(txtFixedSectionLengthMax.getText());
        }
    }//GEN-LAST:event_txtFixedSectionLengthMaxFocusLost

    private void btnSectionApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSectionApplyActionPerformed
        SectionConfiguration config = controller.getSectionConfiguration(listSections.getSelectedValue());
        if (isSectionTypeFixed()) {
            if (txtFixedSectionLengthMin.getText().isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "The section's minimum length must be informed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (txtFixedSectionLengthMax.getText().isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "The section's maximum length must be informed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int sectionLengthMin;
            int sectionLengthMax;
            try {
                sectionLengthMin = Integer.parseInt(txtFixedSectionLengthMin.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(rootPane, "Invalid value for the section's minimum length.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                sectionLengthMax = Integer.parseInt(txtFixedSectionLengthMax.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(rootPane, "Invalid value for the section's maximum length.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (sectionLengthMin > sectionLengthMax) {
                JOptionPane.showMessageDialog(rootPane, "The section's maximum length must be greater or equal to the minimum length.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            config.setLengthMin(sectionLengthMin);
            config.setLengthMax(sectionLengthMax);
        }
        int tempo;
        try {
            tempo = Integer.parseInt(txtSectionTempo.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(rootPane, "Invalid value for the tempo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        config.setTempo(tempo);
        config.setInterruptTracks(chkSectionInterruptTracks.isSelected());
        controller.applyChangesToSection(listSections.getSelectedValue(), config);
        JOptionPane.showMessageDialog(rootPane, "Changes applied", "Ok", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnSectionApplyActionPerformed

    private boolean isSectionTypeFixed() {
        return comboSectionType.getSelectedIndex() == 0;
    }
            
    private void loadSectionAttributes(String selectedValue) {
        SectionConfiguration config = controller.getSectionConfiguration(selectedValue);
        if (config.getType() == SectionConfiguration.TYPE_FIXED) {
            comboSectionType.setSelectedIndex(0);
            txtFixedSectionLengthMin.setEnabled(true);
            txtFixedSectionLengthMin.setText(config.getLengthMin().toString());
            txtFixedSectionLengthMax.setEnabled(true);
            txtFixedSectionLengthMax.setText(config.getLengthMax().toString());
        } else {
            comboSectionType.setSelectedIndex(1);
            txtFixedSectionLengthMin.setEnabled(false);
            txtFixedSectionLengthMin.setText("");
            txtFixedSectionLengthMax.setEnabled(false);
            txtFixedSectionLengthMax.setText("");
        }
        txtSectionTempo.setText(config.getTempo().toString());
        chkSectionInterruptTracks.setSelected(config.getInterruptTracks());
    }
    
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
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLoadFile;
    private javax.swing.JButton btnPlayFile;
    private javax.swing.JButton btnSectionApply;
    private javax.swing.JButton btnSectionPlay;
    private javax.swing.JCheckBox chkSectionInterruptTracks;
    private javax.swing.JComboBox<String> comboSectionType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<String> listSections;
    private javax.swing.JPanel panelSection;
    private javax.swing.JTextField txtFixedSectionLengthMax;
    private javax.swing.JTextField txtFixedSectionLengthMin;
    private javax.swing.JTextField txtSectionTempo;
    // End of variables declaration//GEN-END:variables

}
