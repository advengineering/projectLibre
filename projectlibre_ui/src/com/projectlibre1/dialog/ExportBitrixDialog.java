package com.projectlibre1.dialog;

import java.awt.Frame;

import com.projectlibre1.field.HasExtraFields;
import com.projectlibre1.field.HasExtraFieldsImpl;
import com.projectlibre1.strings.Messages;

/**
 * Диалог экспорта проектов из Битрикс
 * 
 * @author Vadim Konovalov
 *
 */

public class ExportBitrixDialog extends FieldDialog{
	private static final long serialVersionUID = 1L;
	
	public static class Form {
		HasExtraFields extra = new HasExtraFieldsImpl();
	}
	
	private Form form;
	
	private ExportBitrixDialog(Frame owner, Form project) {
		super(owner, Messages.getString("ExportBitrixDialog.Title"), true, false); 
		addDocHelp("/export_to_bitrix");
		if (project != null)
			this.form = project;
		else
			this.form = new Form();
		setObjectClass(HasExtraFieldsImpl.class);
		setObject(form.extra);
	}
	
	public static ExportBitrixDialog getInstance(Frame owner, Form project) {
		return new ExportBitrixDialog(owner, project);
	}
	
	/**
	 * @return Returns the form.
	 */
	public Form getForm() {
		return form;
	}
	public Object getBean(){
		return form;
	}
	protected void onCancel() {
		setVisible(false);
	}
}
