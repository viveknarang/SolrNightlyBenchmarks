package org.apache.solr.tests.nightlybenchmarks;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

enum ZookeeperAction {ZOOKEEPER_START, ZOOKEEPER_STOP, ZOOKEEPER_CLEAN }

public class Zookeeper {

	public static String zooCommand;
	public static String zooCleanCommand;

	static {

		zooCommand = System.getProperty("os.name") != null && System.getProperty("os.name").startsWith("Windows")
				? "bin" + File.separator + "zkServer.cmd " : "bin" + File.separator + "zkServer.sh ";

	}

	Zookeeper() throws IOException {
		super();
		this.install();
	}

	private void install() throws IOException {

		Util.postMessage("** Installing Zookeeper Node ...", MessageType.WHITE_TEXT, false);

		File base = new File(Util.ZOOKEEPER_DIR);
		if (!base.exists()) {
			base.mkdir();
			base.setExecutable(true);
		}
	
		File release = new File(Util.TEMP_DIR + "zookeeper-" + Util.ZOOKEEPER_RELEASE + ".tar.gz");
		if (!release.exists()) {

			String fileName = null;
			URL link = null;
			InputStream in = null;
			FileOutputStream fos = null;

			try {

				Util.postMessage("** Attempting to download zookeeper release ..." + " : " + Util.ZOOKEEPER_RELEASE,
						MessageType.WHITE_TEXT, true);
				fileName = "zookeeper-" + Util.ZOOKEEPER_RELEASE + ".tar.gz";
				link = new URL(Util.ZOOKEEPER_DOWNLOAD_URL + "zookeeper-" + Util.ZOOKEEPER_RELEASE + File.separator + fileName);
				Util.postMessage(Util.ZOOKEEPER_DOWNLOAD_URL + "zookeeper-" + Util.ZOOKEEPER_RELEASE + File.separator + fileName,
						MessageType.WHITE_TEXT, true);
				in = new BufferedInputStream(link.openStream());
				fos = new FileOutputStream(Util.TEMP_DIR + fileName);
				byte[] buf = new byte[1024 * 1024]; // 1mb blocks
				int n = 0;
				long size = 0;
				while (-1 != (n = in.read(buf))) {
					size += n;
					Util.postMessageOnLine("\r" + size + " ");
					fos.write(buf, 0, n);
				}
				fos.close();
				in.close();

			} catch (Exception e) {

				Util.postMessage(e.getMessage(), MessageType.RED_TEXT, false);

			}
		} else {
			Util.postMessage("** Release present nothing to download ...",
					MessageType.GREEN_TEXT, false);		
		}

		File urelease = new File(Util.TEMP_DIR + "zookeeper-" + Util.ZOOKEEPER_RELEASE);
		if (!urelease.exists()) {

			
			Util.execute("tar -xf " + Util.TEMP_DIR + "zookeeper-" + Util.ZOOKEEPER_RELEASE + ".tar.gz" + " -C "
							+ Util.ZOOKEEPER_DIR, Util.ZOOKEEPER_DIR);
			
			Util.execute("mv " + Util.ZOOKEEPER_DIR + "zookeeper-" + Util.ZOOKEEPER_RELEASE + File.separator + "conf"
					+ File.separator + "zoo_sample.cfg " + Util.ZOOKEEPER_DIR + "zookeeper-" + Util.ZOOKEEPER_RELEASE
					+ File.separator + "conf" + File.separator + "zoo.cfg", Util.ZOOKEEPER_DIR);

			
		} else {
			Util.postMessage("** Release extracted already nothing to do ..." + " : " + Util.ZOOKEEPER_RELEASE,
					MessageType.GREEN_TEXT, false);		
		}
	}

	
	public int doAction(ZookeeperAction action) {
		
		
		new File(Util.ZOOKEEPER_DIR + "zookeeper-" + Util.ZOOKEEPER_RELEASE + File.separator + zooCommand)
		.setExecutable(true);
		
		if (action == ZookeeperAction.ZOOKEEPER_START) {
			return Util.execute(Util.ZOOKEEPER_DIR + "zookeeper-" + Util.ZOOKEEPER_RELEASE + File.separator + zooCommand + " start", Util.ZOOKEEPER_DIR + "zookeeper-" + Util.ZOOKEEPER_RELEASE + File.separator);
		} else if (action == ZookeeperAction.ZOOKEEPER_STOP) {
			return Util.execute(Util.ZOOKEEPER_DIR + "zookeeper-" + Util.ZOOKEEPER_RELEASE + File.separator + zooCommand + " stop", Util.ZOOKEEPER_DIR + "zookeeper-" + Util.ZOOKEEPER_RELEASE + File.separator);
		} else if (action == ZookeeperAction.ZOOKEEPER_CLEAN) {
			return Util.execute("rm -r -f " + Util.ZOOKEEPER_DIR, Util.ZOOKEEPER_DIR);
		}
		
		return -1;
	}

	public void cleanZooDataDir() {
		try {
				Util.deleteDirectory("/tmp/zookeeper/");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String getZookeeperIp() {
		return Util.ZOOKEEPER_IP;
	}

	public String getZookeeperPort() {
		return Util.ZOOKEEPER_PORT;
	}

}