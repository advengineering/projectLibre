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
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

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
	private final Gson usersGson = new GsonBuilder()
		    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
		    .create();
	private final Gson commonGson = new Gson();
	
	
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
		List<Worker> workers = new ArrayList<>();
		getInstanses(workers, Worker[].class, token.concat("user.get.json"), "GET", null, null, usersGson, 0, 0);
		return workers.size() == 0 ? null : workers;
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
			String response = IOUtils.toString(con.getInputStream(), "UTF-8").toLowerCase();
			con.getInputStream().close();
			Worker worker = ((ResultOne<Worker>) usersGson
					.fromJson(response, new TypeToken<ResultOne<Worker>>() {}.getType())).result;
			testButActionPerformed(evt, worker);
			return;
		} catch (Exception ex) {}
		testButActionPerformed(evt, null);
	}
	
	private void testButActionPerformed(java.awt.event.ActionEvent evt, Worker worker) {
		if (worker==null) {
			JOptionPane.showMessageDialog(restConfigDialog, "Ошибка соединения!",
					"Тест соединения", JOptionPane.INFORMATION_MESSAGE);
		} else {
			StringBuffer sub = new StringBuffer();
			sub.append("<html>");
			sub.append("<b>Соединение успешно:</b><br>");
			sub.append("<p>" + worker.getName() + " " + worker.getLastName() + "</p>");
			sub.append("</html>");
			JOptionPane.showMessageDialog(restConfigDialog, sub.toString(), "Тест соединения", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	

	public <T> void getInstanses (List<T> list,  Class<T[]> clazz, String url, String method, 
			Object body, HttpHeaders headers, Gson gson, int index, int total) {
		if(method.equals("GET")) {
			HttpGet request = new HttpGet(url);
			try (CloseableHttpResponse response = httpClient.execute(request)) {
				if(response.getStatusLine().getStatusCode() != 200)
					return;
				String entity = EntityUtils.toString(response.getEntity()).toLowerCase();
				if(total == 0)
					total = findTotalCounts(entity);
				entity = entity.substring(entity.indexOf("["), entity.lastIndexOf("]") + 1);
				list.addAll(Arrays.asList(gson.fromJson(entity, clazz)));
				index += 50;
				if(total > index) {
					if(url.indexOf("?start=") != -1)
						url = url.substring(0, url.lastIndexOf(String.valueOf(index - 50)));
					else
						url = url + "?start=";
					getInstanses(list, clazz, url + index, method, body, headers, gson, index, total);
				}
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
		}
	}
	
	private int findTotalCounts(String entity) {
		String totalStr = "0";
		int index = 8;
		while(totalStr.matches("[-+]?\\d+")) {
			totalStr = entity.substring(entity.indexOf("total") + 7, entity.indexOf("total") + index);
			index++;
		}
		return Integer.parseInt(totalStr.substring(0, totalStr.length()-1));
	}
}
