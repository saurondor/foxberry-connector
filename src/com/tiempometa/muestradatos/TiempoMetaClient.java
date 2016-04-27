/**
 * 
 */
package com.tiempometa.muestradatos;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tiempometa.timing.models.ChipReadRaw;

/**
 * Handles communication with the tiempometa.com server
 * 
 * @author Gerardo Tasistro
 * 
 */
public class TiempoMetaClient {
	private static final Logger logger = Logger
			.getLogger(TiempoMetaClient.class);
	// static String host = "192.168.2.32";
	// static String port = "3000";
	static String host = "www.tiempometa.com";
	static String port = null;
	static String getPath = "/api/live_timing/chip_reads";
	static String putPath = "/api/live_timing/chip_reads";
	Gson gson = new Gson();
	HttpClient httpclient = new DefaultHttpClient();
	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");
	String apiKey = null;
	String checkPoint = null;
	Date latestCreateTime = null;
	Integer pageSize = 1000;
	private Map<String, TagReadingLog> logMap = new HashMap<String, TagReadingLog>();

	public List<ChipReadRaw> downloadReadings() throws ClientProtocolException,
			IOException {
		logger.debug("Uploading readings");
		StringBuffer uploadUrl = new StringBuffer();
		uploadUrl.append("https://");
		uploadUrl.append(host);
		if (port != null) {
			uploadUrl.append(":");
			uploadUrl.append(port);
		}
		uploadUrl.append(getPath);
		// uploadUrl.append("?limit=");
		// uploadUrl.append(pageSize);

		uploadUrl.append("?from=");
		if (latestCreateTime != null) {
			uploadUrl.append(dateFormat.format(latestCreateTime));
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Download GET url : " + uploadUrl.toString());
		}
		HttpClient httpclient = new DefaultHttpClient();
		String response = null;
		HttpGet httpGet = new HttpGet(uploadUrl.toString());
		httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Token token=\"" + apiKey
				+ "\"");
		httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

		ResponseHandler<String> handler = new BasicResponseHandler();
		logger.debug("Issuing get");
		response = httpclient.execute(httpGet, handler);

		List<TiempoMetaReading> readings = (List<TiempoMetaReading>) gson
				.fromJson(response, new TypeToken<List<TiempoMetaReading>>() {
				}.getType());
		logger.info("List size = " + readings.size());
		List<ChipReadRaw> chipReadList = new ArrayList<ChipReadRaw>();
		for (TiempoMetaReading tiempoMetaReading : readings) {
			chipReadList.add(tiempoMetaReading.toChipReadRaw());
			TagReadingLog log = logMap.get(tiempoMetaReading.getCheckpoint());
			if (log == null) {
				log = new TagReadingLog();
				log.setCheckpoint(tiempoMetaReading.getCheckpoint());
				log.setCount(0);
				logMap.put(tiempoMetaReading.getCheckpoint(), log);
			}
			log.setCount(log.getCount() + 1);
			log.setLastDownload(tiempoMetaReading.getCreated_at());
			try {
				if ((latestCreateTime == null)
						|| (latestCreateTime.compareTo(dateFormat
								.parse(tiempoMetaReading.getCreated_at())) <= 0)) {
					latestCreateTime = new Date(dateFormat.parse(
							tiempoMetaReading.getCreated_at()).getTime() + 1000);
				}
			} catch (ParseException e) {
			}
		}
		return chipReadList;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;

	}

	/**
	 * @return the checkPoint
	 */
	public String getCheckPoint() {
		return checkPoint;
	}

	/**
	 * @param checkPoint
	 *            the checkPoint to set
	 */
	public void setCheckPoint(String checkPoint) {
		this.checkPoint = checkPoint;
	}

	/**
	 * @return the latestCreateTime
	 */
	public Date getLatestCreateTime() {
		return latestCreateTime;
	}

	/**
	 * @param latestCreateTime
	 *            the latestCreateTime to set
	 */
	public void setLatestCreateTime(Date latestCreateTime) {
		this.latestCreateTime = latestCreateTime;
	}

	/**
	 * @return the pageSize
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public List<TagReadingLog> getReadLog() {
		List<TagReadingLog> readLog = new ArrayList<TagReadingLog>();
		for (String key : logMap.keySet()) {
			readLog.add(logMap.get(key));
		}
		return readLog;
	}

}
