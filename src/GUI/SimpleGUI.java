package GUI;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import Indexing.hashDatabase;
import parser.*;
import minidbms.*;

import java.io.ByteArrayInputStream;

import minidbms.Database;

import parser.SQL_parser;


public class SimpleGUI extends javax.swing.JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Database d=new Database();
	public static hashDatabase d_hash=null;
	static WindowListener exitListener;
	public static int indexingMode=0;//default criteria first then join


	
	public SimpleGUI() throws Exception {
        initComponents();
        setSize(550, 600);
        setVisible(true);
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() throws Exception {

        jScrollPane1 = new javax.swing.JScrollPane();
        sqlText = new javax.swing.JTextArea();
        proceedButton = new javax.swing.JButton();
        sqlLabel = new javax.swing.JLabel();
        openFileButton = new javax.swing.JButton();
        fileDir = new javax.swing.JTextField();
        messageText = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        
        jTabbedPane_result = new javax.swing.JTabbedPane();
        
        jScrollPane2 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        result= new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        tabError = new javax.swing.JTextArea();
        clearButton = new javax.swing.JButton();
        joinMode = new javax.swing.JButton();
        tablesText = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablesList = new javax.swing.JList();
         jScrollPane6 = new javax.swing.JScrollPane();
        
        tablesList.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {		
		     	javax.swing.JList theList = (javax.swing.JList) e.getSource();
		     	 jTabbedPane1.setSelectedIndex(0);
                if (e.getClickCount() == 2) {
                  int index = theList.locationToIndex(e.getPoint());
                  if (index >= 0) {
                    Object o = theList.getModel().getElementAt(index);
                    //System.out.println("Double-clicked on: " + o.toString());
                    
                    //update the scheme table display
                    try {
						jTable2.setModel(new javax.swing.table.DefaultTableModel(
								GUIhelper.constrc_schema_table(d,o.toString()),
						        new String [] {
						            "Name", "Type", "Primary Key", "Foreign Key", "Null"
						        }
						    ) {
						        Class[] types = new Class [] {
						            java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
						        };

						        public Class getColumnClass(int columnIndex) {
						            return types [columnIndex];
						        }
						    });
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                    
                    //update the entry table display
                    try {
						table.setModel(new javax.swing.table.DefaultTableModel(
								GUIhelper.constrc_data_table( d, o.toString()),
						        GUIhelper.get_table_arr_name_list( d,o.toString())
						    ));
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                  }
                }
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
         });
        

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Database GUI");

        sqlText.setColumns(20);
        sqlText.setRows(5);
        jScrollPane1.setViewportView(sqlText);

        proceedButton.setText("Execute");
        proceedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                proceedButtonActionPerformed(evt);
            }
        });

        sqlLabel.setText("SQL");

        openFileButton.setText("Open File");
        openFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openFileButtonActionPerformed(evt);
            }
        });

       // messageText.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
      //  messageText.setText("message");

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(table);

        jTabbedPane1.addTab("Data", jScrollPane2);
        
 
        
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
        	GUIhelper.constrc_schema_table(d,""),
            new String [] {
                "Name", "Type", "Primary Key", "Foreign Key", "Null"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        
        jScrollPane4.setViewportView(jTable2);

        jTabbedPane1.addTab("Schema", jScrollPane4);
        
        //Result tab      
      /* 
        jTabbedPane1.addTab("Result", jScrollPane6);
        result.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
         jScrollPane6.setViewportView(result);   */
        
        jTabbedPane1.addTab("Result",jTabbedPane_result);
        
        
        //message tab
        tabError.setColumns(20);
        tabError.setRows(5);
        tabError.setText("Message");
        jScrollPane5.setViewportView(tabError);
        jTabbedPane1.addTab("Message", jScrollPane5);
        clearButton.setText("Clear");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });
        
        joinMode.setText("Criteria First || Condition 2 first (if not join case)");
        joinMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(indexingMode==0){
                	indexingMode=1;
                    joinMode.setText("Join(=) First || Condition 1 first (if not join case)");
                }else{
                	indexingMode=0;
                    joinMode.setText("Criteria First || Condition 2 first (if not join case)");
                }
            	
                
            }
        });

        tablesText.setText("Tables:");

        tablesList.setModel(new javax.swing.AbstractListModel() {
            String[] strings =  GUIhelper.get_table_name_arr(d);
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(tablesList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(proceedButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(joinMode)
                        //.addGap(0, 0, Short.MAX_VALUE))
                        // .addComponent(joinMode)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane1)
                                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(openFileButton)
                                            .addComponent(sqlLabel))
                                        .addGap(155, 155, 155)
                                        .addComponent(tablesText))
                                    .addComponent(fileDir, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(messageText)
                .addGap(19, 19, 19))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(openFileButton)
                            .addComponent(tablesText))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(sqlLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(proceedButton)
                    //.addComponent(joinMode))
                    .addComponent(joinMode))
                    
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(messageText)
                .addContainerGap())
        );

        messageText.getAccessibleContext().setAccessibleName("");
        jTabbedPane1.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void proceedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_proceedButtonActionPerformed
        String text; 
        jTabbedPane1.setSelectedIndex(3);
        Return result=new Return();
        try {
            text = this.sqlText.getText();
           
            if(!text.equals("")){
            	 text=text+"#";
            	 ByteArrayInputStream inputStream = new  ByteArrayInputStream(text.getBytes());
            	  
            	 long startTime = System.nanoTime();
            	 jTabbedPane_result.removeAll();
            	   result=GUIhelper.eval_GUI(d,inputStream);
            	   
            	 long endTime = System.nanoTime();
                   
            	 double duration = (((double)endTime) - ((double)startTime))/1000000000;
            	 //   System.out.println(duration);
            	        
            	   
            	 tabError.setText(result.message+" in ["+duration+"] s");
                  
                  
                  //this.sqlText.setText("");
                  //refresh the table list model
                  tablesList.setModel(new javax.swing.AbstractListModel() {
                      String[] strings = GUIhelper.get_table_name_arr(d);
                      public int getSize() { return strings.length; }
                      public Object getElementAt(int i) { return strings[i]; }
                  });
                  if ( result.message.equalsIgnoreCase("Success: Select Query executed sucessfully!")){
 	            	 jTabbedPane1.setSelectedIndex(2);
 	              }
            }
            else
            	 tabError.setText("No SQL statement given!");
           
            
        } catch (Exception e) {
          	e.printStackTrace();
            this.tabError.setText("Exception: "+e.getMessage());
        }catch (Throwable t){
        	tabError.setText("Throwable err: "+t.getMessage());
        }
    }//GEN-LAST:event_proceedButtonActionPerformed

    private void openFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openFileButtonActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        Return result=new Return();

        try {
            
            File f = chooser.getSelectedFile();
            String fileName = f.getAbsolutePath();
            this.fileDir.setText(fileName);
            jTabbedPane1.setSelectedIndex(3);
            if(fileName.endsWith(".sql")){ 	
	            InputStream in= new FileInputStream(fileName);
	            
	            
	            long startTime = System.nanoTime();
	            jTabbedPane_result.removeAll();
	            
	            result=GUIhelper.eval_GUI(d,in);
	            
	            long endTime = System.nanoTime();//count time 
                
           	 double duration = (((double)endTime) - ((double)startTime))/1000000000;
           	 
             tabError.setText(result.message+" in ["+duration+"] s");
        
	            //refresh the table list model
	            tablesList.setModel(new javax.swing.AbstractListModel() {
	                String[] strings = GUIhelper.get_table_name_arr(d);
	                public int getSize() { return strings.length; }
	                public Object getElementAt(int i) { return strings[i]; }
	            });
	            in.close();
	            if ( result.message.equalsIgnoreCase("Success: Select Query executed sucessfully!")){
	            	 jTabbedPane1.setSelectedIndex(2);
	            }
            }
            else{
            	// messageText.setText("File type is not correct! Only .sql file will be supported! Please reselect!");
            	 tabError.setText("File type is not correct! Only .sql file will be supported! Please reselect!");
            }
           
            
        } catch (Exception e) {
        	//System.out.println("exception");
        	tabError.setText("Exception: "+e.getMessage()+"\n");
        	e.printStackTrace();
        }catch (Throwable t){
        	tabError.setText("Throwable err: "+t.getMessage());
        }
    }//GEN-LAST:event_openFileButtonActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        messageText.setText(" ");
        sqlText.setText(" ");
        tabError.setText("");
        
    }//GEN-LAST:event_clearButtonActionPerformed
    
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
       /* try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SimpleGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SimpleGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SimpleGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SimpleGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }*/
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                	File f = new File("dbRawData.dat");
                	if(f.exists()) { 
	                	FileInputStream fin = new FileInputStream("dbRawData.dat");
	                	ObjectInputStream ois = new ObjectInputStream(fin);
	                	d= (Database) ois.readObject();
	                	
	                	//do indexing
	                	d_hash=new hashDatabase();
	              
                	}
                	
					new SimpleGUI().addWindowListener(exitListener);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        
       exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
            	
            	//do object out to file
            	FileOutputStream fout = null;
				try {
					fout = new FileOutputStream("dbRawData.dat");
				} catch (FileNotFoundException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
            	ObjectOutputStream oos = null;
				try {
					oos = new ObjectOutputStream(fout);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
            	try {
					oos.writeObject(d);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	
            	
                int confirm = JOptionPane.showOptionDialog(null, "Are You Sure to Close Application?", "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == 0) {
                   System.exit(0);
                }
            }
        };
    
       
        
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clearButton;
    private javax.swing.JButton joinMode;
    
    private javax.swing.JTextField fileDir;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel messageText;
    private javax.swing.JButton openFileButton;
    private javax.swing.JButton proceedButton;
    private javax.swing.JLabel sqlLabel;
    private javax.swing.JTextArea sqlText;
    private javax.swing.JTextArea tabError;
    public static javax.swing.JTable table,result;
    private javax.swing.JList tablesList;
    private javax.swing.JLabel tablesText;
    
    public static javax.swing.JTabbedPane jTabbedPane_result;
    // End of variables declaration//GEN-END:variables
}
