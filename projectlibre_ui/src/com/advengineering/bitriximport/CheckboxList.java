package com.advengineering.bitriximport;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class CheckboxList extends JPanel{

	private List<BooleanWrap> dataModel = new ArrayList<BooleanWrap>();
    private javax.swing.JTextField colText;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTable table;

    public CheckboxList(List list) {
        if(list == null || list.isEmpty())
            throw new java.lang.IllegalArgumentException("list is null or empty!");
        for(Object o : list)
        {
            BooleanWrap bw = new BooleanWrap(o);
            dataModel.add(bw);
        }
        initComponents();
    }

    public ArrayList getSelectedData(){
        ArrayList i = new ArrayList();

        for(BooleanWrap bw : dataModel){
            if(bw.getChecked()){
                i.add(bw.getData());
            }
        }
        return i;
    }

    private TableModel getTableModel() {
        return new DataModel();
    }

    private void initComponents() {
        colText = new javax.swing.JTextField();
        scrollPane = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());
        add(colText, java.awt.BorderLayout.PAGE_START);

        table.setModel(getTableModel());
        setTableParam();
        scrollPane.setViewportView(table);
        add(scrollPane, java.awt.BorderLayout.CENTER);
    }

    private void actionPerformOfButton(int type){
        if(!dataModel.isEmpty()){
            for(BooleanWrap bw : dataModel)
            {
                if(type == 1){
                    bw.setChecked(Boolean.TRUE);
                }else if(type == 2)
                {
                    bw.setChecked(Boolean.FALSE);
                }else if(type == 3){
                    bw.setChecked(!bw.getChecked());
                }
            }
            ((AbstractTableModel)table.getModel()).fireTableDataChanged();
        }
    }

    private void setTableParam() {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(false);
        table.getTableHeader().setEnabled(false);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setPreferredSize(new Dimension(0, 0));
        table.getTableHeader().setDefaultRenderer(renderer);
        
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                ListSelectionModel sm = (ListSelectionModel) e.getSource();
                int row = sm.getLeadSelectionIndex();
                if(row >=0){
                    BooleanWrap bw = dataModel.get(row);
                    colText.setText(bw.getLabel());
                }
            }
        });

        DefaultTableColumnModel dtm = (DefaultTableColumnModel)table.getColumnModel();
        
        TableColumn tc = dtm.getColumn(0);
        tc.setResizable(false);
        tc.setMaxWidth(25);
        ColumnColorTableCellRenderer ctlr = new ColumnColorTableCellRenderer();
        table.getColumnModel().getColumn(1).setCellRenderer(ctlr);
    }


    class DataModel extends AbstractTableModel {
        private String[] columnNames = {"...", "..."};

        public Class getColumnClass(int columnIndex) {
            if(columnIndex == 0)
                return Boolean.class;
            return String.class;
        }

        public String getColumnName(int column) {
            return columnNames[column];
        }
        public int getRowCount() {
            return dataModel.size();
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public Object getValueAt(int rowIndex, int colIndex) {
            BooleanWrap bw = dataModel.get(rowIndex);
            if(colIndex == 1)
                return getDataLabel(bw.getData());
            else{

                return bw.getChecked();
            }
        }

        @Override
        public void setValueAt(Object value, int rowIndex, int colIndex) {
            if(colIndex == 0){
                Boolean flag = (Boolean)value;
                BooleanWrap bw = dataModel.get(rowIndex);
                bw.setChecked(flag);

            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if(columnIndex == 0)
                return true;
            return false;
        }
    }

    public Object getDataLabel(Object data){
        return data.toString();
    }

    class BooleanWrap{
        private Boolean checked = Boolean.FALSE;
        private Object data = null;

        public BooleanWrap(Object obj)
        {
            this.data = obj;
        }

        public Boolean getChecked() {
            return checked;
        }

        public void setChecked(Boolean checked) {
            this.checked = checked;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public String getLabel(){
            return (String)getDataLabel(this.getData());
        }
    }
}
