package com.advengineering.bitriximport.rest.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import com.advengineering.bitriximport.ImportBitrixDialog;
import com.advengineering.bitriximport.rest.client.RestClient;
import com.projectlibre1.dialog.AbstractDialog;
import com.projectlibre1.dialog.ButtonPanel;
import com.projectlibre1.pm.graphic.IconManager;
import com.projectlibre1.strings.Messages;

public class RestConfigDialog extends AbstractDialog{

	private JButton test;
	private JPanel centerPane;
	private JTextField token;
	private JLabel info;

	private static final long serialVersionUID = 1L;

	public static RestConfigDialog getInstance(Frame owner) {
		return new RestConfigDialog(owner);
	}

	private RestConfigDialog(Frame owner) {
		super(owner, Messages.getString("RestConfigDialog.title"), true);
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
		token.setText(RestClient.getInstance().getToken());
		setVisible(false);
	}

	public JComponent createContentPanel() {
		centerPane = new JPanel();
		centerPane.setLayout(new BoxLayout(centerPane, BoxLayout.Y_AXIS));
		centerPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		info = new JLabel(Messages.getString("RestConfigDialog.info"));
		token = new JTextField();
		token.setText(RestClient.getInstance().getToken());
		ImageIcon icon = IconManager.getIcon("export.config");
		JLabel help = new JLabel(icon);
		help.setBorder(new EmptyBorder(10, 10, 10, 10));
		centerPane.add(help);
		centerPane.add(info);
		centerPane.add(token);
		return centerPane;
	}

	public ButtonPanel createButtonPanel() {
		
		ok = new JButton(Messages.getString("RestConfigDialog.save"));
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RestClient.getInstance().setToken(token.getText());
				ImportBitrixDialog.getInstance().updateWorkers();
				onCancel();
			}
		});
		cancel = new JButton(Messages.getString("ButtonText.Cancel"));
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		});
		test = new JButton(Messages.getString("RestConfigDialog.test"));
		test.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RestClient.getInstance().testConnection(e, token.getText());
			}
		});
		ButtonPanel buttonPanel = new ButtonPanel();
		buttonPanel.addButton(test);
		buttonPanel.addButton(ok);
		buttonPanel.addButton(cancel);
		return buttonPanel;
	}

}
