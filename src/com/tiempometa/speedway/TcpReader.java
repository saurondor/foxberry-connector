/**
 * 
 */
package com.tiempometa.speedway;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * @author Gerardo Tasistro
 * 
 */
public class TcpReader implements Runnable {

	private static final Logger logger = Logger.getLogger(TcpReader.class);

	private int port = 14150; // use default port
	private String hostname = "";
	private Socket readerSocket = null;
	private InputStream dataInputStream = null;
	private OutputStream dataOutputStream = null;

	private void openSocket() throws UnknownHostException, IOException {
		readerSocket = new Socket(hostname, port);
	}

	public void connect() throws UnknownHostException, IOException {
		openSocket();
	}

	public void connect(String hostname) throws UnknownHostException,
			IOException {
		this.hostname = hostname;
		openSocket();
	}

	public void connect(String hostname, Integer port)
			throws UnknownHostException, IOException {
		this.hostname = hostname;
		this.port = port;
		openSocket();
	}

	public void disconnect() throws IOException {
		readerSocket.close();
	}

	@Override
	public void run() {
		try {
			dataInputStream = readerSocket.getInputStream();
			dataOutputStream = readerSocket.getOutputStream();
			while (!readerSocket.isClosed()) {
				logger.info("Socket is open");
				while (readerSocket.isConnected()) {
					if (readerSocket.isClosed()) {
						logger.warn("Socket is closed!");
					}
					logger.info("Socket is connected");

					int dataInStream;
					try {
						dataInStream = dataInputStream.available();
						if (dataInStream < 0) {
							logger.warn("Less than 0 bytes available, disconnect?");
						}
						logger.info("pinging server");
						dataOutputStream.write("ping".getBytes());
						dataOutputStream.flush();
						logger.info("pinged server!");
						if (dataInStream > 0) {
							byte[] b = new byte[dataInStream];
							dataInputStream.read(b);
							String dataString = new String(b);
							if (logger.isDebugEnabled()) {
								logger.debug("DATA>\n" + dataString + "\nLEN:"
										+ dataString.length());
							}
							StringBuffer buffer = new StringBuffer((new Date())
									+ " " + dataString);
							String[] dataRows = dataString.split("\\n");
							for (String string : dataRows) {
								logger.debug(string);
							}
						} else {
							logger.info("No data");
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						try {
							disconnect();
							connect();
							dataInputStream = readerSocket.getInputStream();
							dataOutputStream = readerSocket.getOutputStream();
						} catch (IOException e2) {
							e2.printStackTrace();
						}
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

	}

}
