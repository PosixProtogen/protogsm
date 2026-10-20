package com.protogsm.gameservercore.service.impl.steamcmd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;
import org.springframework.context.ApplicationEventPublisher;

import com.protogsm.architecture.util.Platform;
import com.pty4j.PtyProcessBuilder;

public class SteamCmdBuilder {
	
	private String localDir;
	
	private ApplicationEventPublisher publisher;
	
	public SteamCmdBuilder setLocalDir(String localDir) {
		this.localDir = localDir;
		
		return this;
	}
	
	public SteamCmdBuilder addPublisher(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
		
		return this;
	}
	
	public SteamCmd build() {
		if (this.localDir == null) {
			this.localDir = System.getProperty("java.io.tmpdir");
		}
		
		this.localDir = this.localDir + File.separator + "steamcmd" + File.separator;
		
		String steamCmdLocal = localDir;
		String steamcmdURL = "";
		String zipSteam = "";
		
		if (Platform.isLinux()) {
			steamCmdLocal = steamCmdLocal + "steamcmd.sh";
			steamcmdURL = "https://steamcdn-a.akamaihd.net/client/installer/steamcmd_linux.tar.gz";
			zipSteam = "steamcmd_linux.tar.gz";
			
		} else if (Platform.isWindows()) {
			steamCmdLocal = steamCmdLocal + "steamcmd.exe";
			steamcmdURL = "https://steamcdn-a.akamaihd.net/client/installer/steamcmd.zip";
			zipSteam = "steamcmd.zip";
		}
		
		File shellSteamcmd = new File(steamCmdLocal);

		if (!shellSteamcmd.exists()) {
			installSteamCMD(zipSteam, steamcmdURL);
		}
		
		PtyProcessBuilder processBuilder = createProcessBuilder(steamCmdLocal);
		
		return new SteamCmd(processBuilder, publisher);
	}
	
	private PtyProcessBuilder createProcessBuilder(String steamCmdLocal) {
		PtyProcessBuilder builder = null;
		
		if (Platform.isLinux()) {
			String[] commandLines = { steamCmdLocal };
			
    		builder = new PtyProcessBuilder(commandLines);
    		
    	} else if (Platform.isWindows()) {
    		String[] commandLines = { steamCmdLocal };
    		
    		builder = new PtyProcessBuilder(commandLines)
    		        .setConsole(false)
    		        .setUseWinConPty(true);
    	}
		return builder;
	}

	private void installSteamCMD(String zipSteam, String steamcmdURL) {
		File localFile = new File(localDir);
		File zipFile = new File(localDir + zipSteam);

		localFile.mkdir();

		zipFile = downloadSteamCMD(steamcmdURL, zipFile);

		String[] splitStringZipSteam = zipSteam.split("\\.");

		if  (splitStringZipSteam[splitStringZipSteam.length -1].equals("zip")) {
			Archiver archiver = ArchiverFactory.createArchiver("zip");
			
			extractFile(localFile, zipFile, archiver);
			
		} else if (splitStringZipSteam[splitStringZipSteam.length -1].equals("gz")) {
			Archiver archiver = ArchiverFactory.createArchiver("tar", "gz");
			
			extractFile(localFile, zipFile, archiver);
		}
	}
	
	private File downloadSteamCMD(String steamcmdURL, File zipFile) {
		URL url;
		try {
			url = new URI(steamcmdURL).toURL();
			ReadableByteChannel rbc = Channels.newChannel(url.openStream());
			FileOutputStream fos = new FileOutputStream(zipFile);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
			rbc.close();
			
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e);
		}
		return zipFile;
	}
	
	private void extractFile(File localFile, File zipFile, Archiver archiver) {
		try {
			archiver.extract(zipFile, localFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
