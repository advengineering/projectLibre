package com.advengineering.bitriximport;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import com.advengineering.bitriximport.rest.client.RestClient;
import com.advengineering.bitriximport.rest.model.Worker;
import com.projectlibre1.dialog.AbstractDialog;
import com.projectlibre1.dialog.ButtonPanel;
import com.projectlibre1.strings.Messages;

public class ImportBitrixDialog extends AbstractDialog {

	private JButton all;
	private JButton upLevel;
	private JButton options;
	private JPanel centerPane;
	private CheckboxList workerList;

	private static final long serialVersionUID = 1L;
	
	private static ImportBitrixDialog instance;

	private ImportBitrixDialog() {}
	
	public static ImportBitrixDialog getInstance(Frame owner) {
		if(instance==null)
			instance = new ImportBitrixDialog(owner);
		return instance;
	}
	
	public static ImportBitrixDialog getInstance() {
		return instance;
	}

	private ImportBitrixDialog(Frame owner) {
		super(owner, Messages.getString("ImportBitrixDialog.Title"), true);
	}

	protected JRootPane createRootPane() {
		ActionListener escapeListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				onCancel();
			}
		};
		JRootPane rootPane = new JRootPane();
		KeyStroke escapeStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		rootPane.registerKeyboardAction(escapeListener, escapeStroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
		return rootPane;
	}

	protected void onCancel() {
		setVisible(false);
	}

	public JComponent createContentPanel() {
		centerPane = new JPanel();
		updateWorkers();
		return centerPane;
	}

	public ButtonPanel createButtonPanel() {
		all = new JButton(Messages.getString("ImportBitrixDialog.all"));
		all.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				loadAll(e);

			}
		});
		upLevel = new JButton(Messages.getString("ImportBitrixDialog.upLevel"));
		upLevel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				loadLevelUp(e);

			}
		});
		cancel = new JButton(Messages.getString("ButtonText.Cancel"));
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		});
		options = new JButton(Messages.getString("ImportBitrixDialog.options"));
		options.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RestClient.doNewRestConfigDialog();
			}
		});
		ButtonPanel buttonPanel = new ButtonPanel();
		buttonPanel.addButton(all);
		buttonPanel.addButton(upLevel);
		buttonPanel.addButton(options);
		buttonPanel.addButton(cancel);
		return buttonPanel;
	}

	// implement to load workers from bitrix
	public void updateWorkers() {
		List<Worker> workers = RestClient.getWorkers();
		if (workers == null) {
			centerPane.removeAll();
			setSize(new Dimension(500, 80));
			return;
		} else {
			workerList = new CheckboxList(workers) {
				@Override
				public Object getDataLabel(Object data) {
					Worker a = (Worker) data;
					return a.getLAST_NAME() + " " + a.getNAME() + " " + a.getSECOND_NAME();
				}
			};
			centerPane.removeAll();
			centerPane.add(workerList, BorderLayout.CENTER);
		}
		setSize(new Dimension(500, 500));
		centerPane.setVisible(false);
		centerPane.setVisible(true);
	}

	// implement load project from bitrix
	public void loadAll(ActionEvent e) {
		// only for test
		testButActionPerformed(e);
	}

	// implement load project from bitrix
	public void loadLevelUp(ActionEvent e) {
		// only for test
		testButActionPerformed(e);
	}

	// test listener to remove
	private void testButActionPerformed(java.awt.event.ActionEvent evt) {
		ArrayList as = workerList.getSelectedData();
		if (as.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Ничего не выбрано!", "Информация", JOptionPane.INFORMATION_MESSAGE);
		} else {
			StringBuffer sub = new StringBuffer();
			sub.append("<html>");
			sub.append("<b>Выбрано:</b><br>");
			sub.append("<ul>");
			for (Object o : as) {
				Worker b = (Worker) o;
				sub.append("<li>" + b.getSECOND_NAME() + ", " + b.getID() + "</li>");
			}
			sub.append("</ul>");
			sub.append("</html>");
			JOptionPane.showMessageDialog(this, sub.toString(), "Информация", JOptionPane.INFORMATION_MESSAGE);
		}
	}

}
