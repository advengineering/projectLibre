package com.advengineering.bitriximport;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class ColumnColorTableCellRenderer extends DefaultTableCellRenderer{

    private Color colorOne = Color.white;
    private Color colorTwo = new Color(206,231,255);

    private Color textColor = Color.BLACK;
    private Color numberColor = Color.RED;
    private Color dateColor = Color.BLUE;

    public ColumnColorTableCellRenderer(Color one, Color two) {
        if(one != null)
            colorOne = one;
        if(two != null)
            colorTwo = two;
    }

    public ColumnColorTableCellRenderer() {
    }

    public Component getTableCellRendererComponent(JTable table,
                                                   Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {

        if(value instanceof String)
        {
            String str = (String)value;
            this.setForeground(textColor);

        }else if(value instanceof Integer || value instanceof Double || value instanceof Long)
        {
            this.setForeground(numberColor);
            this.setHorizontalAlignment(SwingConstants.RIGHT);
        }
        if(row % 2 == 0){
            setBackground(colorOne);
        }
        else if(row % 2 == 1)
        {
            setBackground(colorTwo);
        }
        return super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);
    }
}
