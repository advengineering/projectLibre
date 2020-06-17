package com.advengineering.bitriximport;

import java.awt.BorderLayout;
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

import com.projectlibre1.dialog.AbstractDialog;
import com.projectlibre1.dialog.ButtonPanel;
import com.projectlibre1.strings.Messages;

public class ImportBitrixDialog extends AbstractDialog {

	private JButton all;
	private JButton upLevel;
	private JPanel centerPane;
	private CheckboxList workerList;

	private static final long serialVersionUID = 1L;

	public static ImportBitrixDialog getInstance(Frame owner) {
		return new ImportBitrixDialog(owner);
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
		ButtonPanel buttonPanel = new ButtonPanel();
		buttonPanel.addButton(all);
		buttonPanel.addButton(upLevel);
		buttonPanel.addButton(cancel);
		return buttonPanel;
	}

	// implement to load workers from bitrix
	public void updateWorkers() {
		prepareTestCheckboxListPane();
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

	// test var to remove
	private int i = 50;

	// test list to remove
	private void prepareTestCheckboxListPane() {
		Worker worker = new Worker();
		worker.setTitle("Петров В.А." + this.i);
		worker.setPrice(20.5);
		List workers = new ArrayList<Worker>();
		int i = 0;
		while (i < this.i) {
			workers.add(worker);
			i++;
		}
		workerList = new CheckboxList(workers) {
			@Override
			public Object getDataLabel(Object data) {
				Worker a = (Worker) data;
				return a.getTitle();
			}
		};
		centerPane.removeAll();
		centerPane.add(workerList, BorderLayout.CENTER);
		this.i = this.i + 10;
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
				sub.append("<li>" + b.getTitle() + ", " + b.getPrice() + "</li>");
			}
			sub.append("</ul>");
			sub.append("</html>");
			JOptionPane.showMessageDialog(this, sub.toString(), "Информация", JOptionPane.INFORMATION_MESSAGE);
		}
	}

}
