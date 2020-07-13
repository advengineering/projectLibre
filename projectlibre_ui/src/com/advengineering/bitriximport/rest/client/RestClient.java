package com.advengineering.bitriximport.rest.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.apache.commons.io.IOUtils;

import com.advengineering.bitriximport.rest.config.RestConfigDialog;
import com.advengineering.bitriximport.rest.model.ResultOne;
import com.advengineering.bitriximport.rest.model.Worker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.projectlibre1.pm.graphic.frames.GraphicManager;
import com.projectlibre1.util.ClassLoaderUtils;

public class RestClient {

	private static String CONFIG_FILE;
	protected static ClassLoader classLoader;
	private static Properties properties;
	public static String TOKEN;
	private static RestConfigDialog restConfigDialog;

	private static void init() {
		CONFIG_FILE = "com/advengineering/bitriximport/rest/config/bitrix.ini";
		classLoader = ClassLoaderUtils.getLocalClassLoader();
		properties = new Properties();
		TOKEN = getProperties();
	}

	public static String getProperties() {
		try {	
			InputStream in = new FileInputStream(getFileProperties());
			properties.load(in);
			in.close();
			return properties.getProperty("TOKEN","");
		} catch(Exception e) {	
			e.printStackTrace();
		}
		return "";
	}

	public static void setProperties(String token) {
		TOKEN = token.trim().replace("profile", "");
		try {
			properties.put("TOKEN", TOKEN);
			OutputStream out = new FileOutputStream(getFileProperties());
			properties.store(out, "");
			out.close();
		} catch (IOException e) {}
	}

	private static File getFileProperties() {
		return new File(classLoader.getResource(CONFIG_FILE).getFile());
	}

	public static void doNewRestConfigDialog() {
		if (restConfigDialog == null) {
			restConfigDialog = RestConfigDialog.getInstance(GraphicManager.getInstance().getFrame());
			restConfigDialog.pack();
			restConfigDialog.setModal(true);
		}
		restConfigDialog.setLocationRelativeTo(GraphicManager.getInstance().getFrame());
		restConfigDialog.setVisible(true);
	}

	public static ArrayList<Worker> getWorkers() {
		if(TOKEN==null)
			init();
		ArrayList<Worker> workers;
		try {
			HttpURLConnection con = (HttpURLConnection) new URL(TOKEN + "user.get.json").openConnection();
			con.setRequestMethod("GET");

			int responseCode = con.getResponseCode();
			if (responseCode != 200) {
				return null;
			}

			String response = IOUtils.toString(con.getInputStream(), "UTF-8");
			
			String totalStr = "0";
			int index = 8;
			while(totalStr.matches("[-+]?\\d+")) {
				totalStr = response.substring(response.indexOf("total") + 7, response.indexOf("total") + index);
				index++;
			}
			int totalInt = Integer.parseInt(totalStr.substring(0, totalStr.length()-1));	
			con.getInputStream().close();
			workers = ((ResultOne<ArrayList<Worker>>) new Gson().fromJson(response,
					new TypeToken<ResultOne<ArrayList<Worker>>>() {}.getType())).result;
			index = 50;
			while(totalInt > index) {
				con = (HttpURLConnection) new URL(TOKEN + "user.get.json?start=" + index).openConnection();
				con.setRequestMethod("GET");

				responseCode = con.getResponseCode();
				if (responseCode != 200) {
					return null;
				}

				response = IOUtils.toString(con.getInputStream(), "UTF-8");
				con.getInputStream().close();
				workers.addAll(((ResultOne<ArrayList<Worker>>) new Gson().fromJson(response,
						new TypeToken<ResultOne<ArrayList<Worker>>>() {}.getType())).result);
				index += 50;
			}
			return workers;
			
		} catch (IOException ex) {}
		return null;
	}
	
	/*public static ArrayList<Worker> getTasks() {
		
	}*/
	

	public static void testConnection(java.awt.event.ActionEvent evt, String token) {
		token = token.trim().replace("profile", "");
		try {
			HttpURLConnection con = (HttpURLConnection) new URL(token + "/profile").openConnection();
			con.setRequestMethod("GET");

			int responseCode = con.getResponseCode();
			if (responseCode != 200) {
				testButActionPerformed(evt, null);
				return;
			}
			String response = IOUtils.toString(con.getInputStream(), "UTF-8");
			con.getInputStream().close();
			Worker worker = ((ResultOne<Worker>) new Gson().fromJson(response, new TypeToken<ResultOne<Worker>>() 
			{}.getType())).result;
			testButActionPerformed(evt, worker);
			return;
		} catch (Exception ex) {}
		testButActionPerformed(evt, null);
	}
	
	private static void testButActionPerformed(java.awt.event.ActionEvent evt, Worker worker) {
		if (worker==null) {
			JOptionPane.showMessageDialog(restConfigDialog, "Ошибка соединения!", "Тест соединения", JOptionPane.INFORMATION_MESSAGE);
		} else {
			StringBuffer sub = new StringBuffer();
			sub.append("<html>");
			sub.append("<b>Соединение успешно:</b><br>");
			sub.append("<p>" + worker.getLAST_NAME() + " " + worker.getNAME() +  " " + worker.getSECOND_NAME() + "</p>");
			sub.append("</html>");
			JOptionPane.showMessageDialog(restConfigDialog, sub.toString(), "Тест соединения", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
