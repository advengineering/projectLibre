package com.projectlibre1.dialog;

import java.awt.Frame;

import com.projectlibre1.field.HasExtraFields;
import com.projectlibre1.field.HasExtraFieldsImpl;
import com.projectlibre1.strings.Messages;

/**
 * Диалог импорта проектов из Битрикс
 * 
 * @author Vadim Konovalov
 *
 */

public class ImportBitrixDialog extends FieldDialog{
	private static final long serialVersionUID = 1L;
	
	public static class Form {
		HasExtraFields extra = new HasExtraFieldsImpl();
	}
	
	private Form form;
	
	private ImportBitrixDialog(Frame owner, Form project) {
		super(owner, Messages.getString("ImportBitrixDialog.Title"), true, false); 
		addDocHelp("/import_from_bitrix");
		if (project != null)
			this.form = project;
		else
			this.form = new Form();
		setObjectClass(HasExtraFieldsImpl.class);
		setObject(form.extra);
	}
	
	public static ImportBitrixDialog getInstance(Frame owner, Form project) {
		return new ImportBitrixDialog(owner, project);
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
