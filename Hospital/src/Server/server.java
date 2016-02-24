package Server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.security.KeyStore;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;
import javax.security.cert.X509Certificate;

import Util.Hospital;
import Util.Person;

public class server implements Runnable {
	private ServerSocket serverSocket = null;
	private static int numConnectedClients = 0;
	private Hospital hospital;

	public server(ServerSocket ss) throws IOException {
		hospital = new Hospital();
		serverSocket = ss;
		newListener();
	}

	public void run() {
		try {
			SSLSocket socket = (SSLSocket) serverSocket.accept();
			newListener();
			SSLSession session = socket.getSession();
			System.out.println(session.getCipherSuite());
			X509Certificate cert = (X509Certificate) session.getPeerCertificateChain()[0];
			String subject = cert.getSubjectDN().getName();
			// numConnectedClients++;
			System.out.println("client connected");
			// System.out.println("client name (cert subject DN field): " +
			// subject);
			// System.out.println("issuer name: " +
			// cert.getIssuerDN().getName());
			// System.out.println("serial number: " + cert.getSerialNumber());
			// System.out.println(numConnectedClients + " concurrent
			// connection(s)\n");

			// CN=dic14jan, OU=Doctor, O=Head, L=Jan, ST=Unknown, C=Unknown
			System.out.println("Subject = " + subject);
			String[] id = new String[4];
			for (int i = 0; i < 3; i++) {
				id[i] = subject.substring(subject.indexOf('=') + 1, subject.indexOf(','));
				subject = subject.substring(subject.indexOf(',') + 1);
			}
			int index = subject.indexOf(',');
			if(index == -1) {
				id[3] = subject.substring(subject.indexOf('=') + 1, subject.length());
			} else {
				id[3] = subject.substring(subject.indexOf('=') + 1, index);
			}
			
			
			PrintWriter out = null;
			BufferedReader in = null;
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			System.out.println(cert.getSerialNumber());
			Person p = hospital.login(cert.getSerialNumber(), id);
			
//			StringBuilder sb = new StringBuilder();
//			sb.append("What do you want to do? Choose a number\n");
//			sb.append("1. Read a record\n");
//			if(id[1].toLowerCase().equals("doctor")) {
//				sb.append("2. Make a new record\n");
//			}
//			if(id[1].toLowerCase().equals("agent")) {
//				sb.append("2. Remove a record\n");
//			}
//			
//			sb.append("-end");
//			out.println(sb.toString());
			
			StringBuilder sb = new StringBuilder();
			
			sb.append("Enter a command\n");
			sb.append("-end");
			
			out.println(sb.toString());
			
			String clientMsg = null;
			while ((clientMsg = in.readLine()) != null) {
//				String rev = new StringBuilder(clientMsg).reverse().toString();
//				System.out.println("received '" + clientMsg + "' from client");
//				System.out.print("sending '" + rev + "' to client...");
//				out.println(rev);
				System.out.println(clientMsg);
				hospital.readInput(clientMsg, p, in, out);
				out.println("-end");
				out.flush();
				System.out.println("done\n");
			}
			in.close();
			out.close();
			socket.close();
			numConnectedClients--;
			System.out.println("client disconnected");
			System.out.println(numConnectedClients + " concurrent connection(s)\n");
		} catch (IOException e) {
			System.out.println("Client died: " + e.getMessage());
			e.printStackTrace();
			return;
		}
	}

	private void newListener() {
		(new Thread(this)).start();
	} // calls run()

	public static void main(String args[]) {
		System.out.println("\nServer Started\n");
		int port = 9876;
		if (args.length >= 1) {
			port = Integer.parseInt(args[0]);
		}
		String type = "TLS";
		try {
			ServerSocketFactory ssf = getServerSocketFactory(type);
			ServerSocket ss = ssf.createServerSocket(port);
			((SSLServerSocket) ss).setNeedClientAuth(true); // enables client
															// authentication
			new server(ss);
		} catch (IOException e) {
			System.out.println("Unable to start Server: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static ServerSocketFactory getServerSocketFactory(String type) {
		if (type.equals("TLS")) {
			SSLServerSocketFactory ssf = null;
			try { // set up key manager to perform server authentication
				SSLContext ctx = SSLContext.getInstance("TLS");
				KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
				TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
				KeyStore ks = KeyStore.getInstance("JKS");
				KeyStore ts = KeyStore.getInstance("JKS");
				char[] password = "password".toCharArray();

				ks.load(new FileInputStream("certificates/Server/serverKeyStore"), password); // keystore
																								// password
																								// (storepass)
				ts.load(new FileInputStream("certificates/Server/serverTrustStore"), password); // truststore
																								// password
																								// (storepass)
				kmf.init(ks, password); // certificate password (keypass)
				tmf.init(ts); // possible to use keystore as truststore here
				ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
				ssf = ctx.getServerSocketFactory();
				return ssf;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return ServerSocketFactory.getDefault();
		}
		return null;
	}
}
