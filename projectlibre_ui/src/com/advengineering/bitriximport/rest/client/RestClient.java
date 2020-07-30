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
import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import com.advengineering.bitriximport.rest.config.RestConfigDialog;
import com.advengineering.bitriximport.rest.model.ResultOne;
import com.advengineering.bitriximport.rest.model.Task;
import com.advengineering.bitriximport.rest.model.Worker;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.projectlibre1.pm.graphic.frames.GraphicManager;
import com.projectlibre1.util.ClassLoaderUtils;

public class RestClient {

	private static RestClient instance;
	
	private final String CONFIG_FILE = "com/advengineering/bitriximport/rest/config/bitrix.ini";
	private final Properties properties = new Properties();
	private final ClassLoader classLoader = ClassLoaderUtils.getLocalClassLoader();
	
	private RestConfigDialog restConfigDialog = null;

	private String token = getToken();
	
	private CloseableHttpClient httpClient = HttpClients.createDefault();
	
	public static RestClient getInstance() {
		if(instance==null)
			instance = new RestClient();
		return instance;
	}

	public String getToken() {
		if(properties.getProperty("TOKEN", null) == null)
			try {	
				InputStream in = new FileInputStream(getFileProperties());
				properties.load(in);
				in.close();
			} catch(IOException e) {	
				e.printStackTrace();
			}
		return properties.getProperty("TOKEN", null);
	}

	public void setToken(String token) {
		this.token = token.trim().replace("profile", "");
		try {
			properties.put("TOKEN", this.token);
			OutputStream out = new FileOutputStream(getFileProperties());
			properties.store(out, "");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private File getFileProperties() {
		return new File(classLoader.getResource(CONFIG_FILE).getFile());
	}

	public void doNewRestConfigDialog() {
		if (restConfigDialog == null) {
			restConfigDialog = RestConfigDialog.getInstance(GraphicManager.getInstance().getFrame());
			restConfigDialog.pack();
			restConfigDialog.setModal(true);
		}
		restConfigDialog.setLocationRelativeTo(GraphicManager.getInstance().getFrame());
		restConfigDialog.setVisible(true);
	}

	public List<Worker> getWorkers() {
		if(token==null)
			return null;
		ArrayList<Worker> workers;
		try {
			HttpURLConnection con = (HttpURLConnection) new URL(token + "user.get.json").openConnection();
			con.setRequestMethod("GET");

			int responseCode = con.getResponseCode();
			if (responseCode != 200) {
				return null;
			}

			String response = IOUtils.toString(con.getInputStream(), "UTF-8").toLowerCase();
			
			String totalStr = "0";
			int index = 8;
			while(totalStr.matches("[-+]?\\d+")) {
				totalStr = response.substring(response.indexOf("total") + 7, response.indexOf("total") + index);
				index++;
			}
			int totalInt = Integer.parseInt(totalStr.substring(0, totalStr.length()-1));	
			con.getInputStream().close();
			Gson gson = new GsonBuilder()
				    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
				    .create();
			workers = ((ResultOne<ArrayList<Worker>>) gson.fromJson(response,
					new TypeToken<ResultOne<ArrayList<Worker>>>() {}.getType())).result;
			index = 50;
			while(totalInt > index) {
				con = (HttpURLConnection) new URL(token + "user.get.json?start=" + index).openConnection();
				con.setRequestMethod("GET");

				responseCode = con.getResponseCode();
				if (responseCode != 200) {
					return workers;
				}

				response = IOUtils.toString(con.getInputStream(), "UTF-8");
				con.getInputStream().close();
				workers.addAll(((ResultOne<ArrayList<Worker>>) new Gson().fromJson(response,
						new TypeToken<ResultOne<ArrayList<Worker>>>() {}.getType())).result);
				index += 50;
			}
			return workers;
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public List<Task> getTasks(int id){
		 /*List<NameValuePair> urlParameters = new ArrayList();
		 NameValuePair select = new 
	     urlParameters.add(new BasicNameValuePair("username", "abc"));
	     urlParameters.add(new BasicNameValuePair("password", "123"));
	     urlParameters.add(new BasicNameValuePair("custom", "secret"));*/
		return null;
	}

	public void testConnection(java.awt.event.ActionEvent evt, String token) {
		token = token.trim().replace("profile", "");
		if(!token.endsWith("/")) {
			testButActionPerformed(evt, null);
			return;
		}
		try {
			HttpURLConnection con = (HttpURLConnection) new URL(token + "/profile").openConnection();
			con.setRequestMethod("GET");
			
			if (con.getResponseCode() != 200) {
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
	
	private void testButActionPerformed(java.awt.event.ActionEvent evt, Worker worker) {
		if (worker==null) {
			JOptionPane.showMessageDialog(restConfigDialog, "Ошибка соединения!", "Тест соединения", JOptionPane.INFORMATION_MESSAGE);
		} else {
			StringBuffer sub = new StringBuffer();
			sub.append("<html>");
			sub.append("<b>Соединение успешно:</b><br>");
			sub.append("<p>" + worker.getLastName() + " " + worker.getName() +  " " + worker.getSecondName() + "</p>");
			sub.append("</html>");
			JOptionPane.showMessageDialog(restConfigDialog, sub.toString(), "Тест соединения", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
