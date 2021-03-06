package Client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.KeyStore;
import java.util.Scanner;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.security.cert.X509Certificate;

/*
 * This example shows how to set up a key manager to perform client
 * authentication.
 *
 * This program assumes that the client is not inside a firewall.
 * The application can be modified to connect to a server outside
 * the firewall by following SSLSocketClientWithTunneling.java.
 */
public class client {

	public static void main(String[] args) throws Exception {
		String host = null;
		int port = -1;
		if (args.length < 2) {
			args = new String[2];
			args[0] = "localhost";
			args[1] = "9876";
		}

		for (int i = 0; i < args.length; i++) {
			System.out.println("args[" + i + "] = " + args[i]);
		}
		if (args.length < 2) {
			System.out.println("USAGE: java client host port");
			System.exit(-1);
		}
		try { /* get input parameters */
			host = args[0];
			port = Integer.parseInt(args[1]);
		} catch (IllegalArgumentException e) {
			System.out.println("USAGE: java client host port");
			System.exit(-1);
		}

		try { /* set up a key manager for client authentication */
			SSLSocketFactory factory = null;
			do {
				try {
					Scanner scan = new Scanner(System.in);
					System.out.println("Never give out your login credentials to anyone!");
					System.out.println("Enter your user ID: ");
					String id = scan.next();
					scan.nextLine();
					System.out.println("Enter your password: ");
					String passwd = scan.next();
					scan.nextLine();
					char[] password = "password".toCharArray();
					KeyStore ks = KeyStore.getInstance("JKS");
					KeyStore ts = KeyStore.getInstance("JKS");
					KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
					TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
					SSLContext ctx = SSLContext.getInstance("TLS");
					ks.load(new FileInputStream("certificates/Client/" + id), passwd.toCharArray()); // keystore
																										// password
																										// (storepass)
					ts.load(new FileInputStream("certificates/Client/clientTrustStore"), password); // truststore
																									// password
																									// (storepass);
					kmf.init(ks, passwd.toCharArray()); // user password
														// (keypass)
					tmf.init(ts); // keystore can be used as truststore here
					ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
					factory = ctx.getSocketFactory();
				} catch (Exception e) {
					System.err.println("Something went wrong. Try again");
				}
			} while (factory == null);
			SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
			// System.out.println("\nsocket before handshake:\n" + socket +
			// "\n");

			/*
			 * send http request
			 *
			 * See SSLSocketClient.java for more information about why there is
			 * a forced handshake here when using PrintWriters.
			 */
			socket.startHandshake();

			SSLSession session = socket.getSession();
			X509Certificate cert = (X509Certificate) session.getPeerCertificateChain()[0];
			String subject = cert.getSubjectDN().getName();

			BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String msg, line;

			for (;;) {
				while (!(line = in.readLine()).equals("-end")) {
					System.out.println(line);
				}
				System.out.print(">");
				msg = read.readLine();
				if (msg.equalsIgnoreCase("quit")) {
					break;
				}
				out.println(msg);
				out.flush();
				
			}
			in.close();
			out.close();
			read.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
