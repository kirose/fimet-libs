package com.fimet.ftp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import com.fimet.FimetLogger;
import com.fimet.IEventManager;
import com.fimet.Manager;
import com.fimet.event.EnviromentEvent;
import com.fimet.net.IConnectionListener;
import com.fimet.utils.Args;
import com.fimet.utils.FileUtils;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class Ftp implements IFtp {
	
	private static final long TIME_TO_ATTEMP_RECONNECT = Manager.getPropertyLong("ftp.reconnectTime", 1000L * 45);// 45 Seconds
	public static final int TIMEOUT = Manager.getPropertyInteger("ftp.timeout", 4000);
	private Date timeCheckConnection;
	private String name;
    private Session session = null;
    private ChannelSftp channel = null;
    private Status status = Status.DISCONNECTED;
	private IConnectionListener listener;
	public Ftp(IEFtp entity, IConnectionListener listener) {
		Args.notNull("Ftp Entity", entity);
		Args.notNull("Ftp Entity Name", entity.getName());
		Args.notNull("Ftp Entity User", entity.getUser());
		Args.notNull("Ftp Entity Password", entity.getPassword());
		Args.notNull("Ftp Entity Address", entity.getAddress());
		Args.notNull("Ftp Connection Listener", listener);
		this.listener = listener;
		name = entity.getName();
		try {
	        JSch jsch = new JSch();
	        if (entity.getPort() != null) {
				session = jsch.getSession(entity.getUser(), entity.getAddress(), entity.getPort());
	    	} else {
	    		session = jsch.getSession(entity.getUser(), entity.getAddress());
	    	}
		} catch (NumberFormatException e) {
			throw new FtpException(e);
		} catch (JSchException e) {
			throw new FtpException(e);
		}
		session.setPassword(entity.getPassword());
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        //config.put("PreferredAuthentications","publickey,gssapi-with-mic,keyboard-interactive,password");
        //config.put("PreferredAuthentications","publickey,keyboard-interactive,password");
        config.put("PreferredAuthentications","publickey,password");
        session.setConfig(config);
	}
	public void connect() {
		if (status == Status.DISCONNECTED) {
	        try {
	        	status = Status.CONNECTING;
	        	listener.onConnecting(this);
	        	Manager.get(IEventManager.class).fireEvent(EnviromentEvent.FTP_CONNECTING, this, this);
	            FimetLogger.debug(Ftp.class,"Connecting to "+session.getHost()+" with "+session.getUserName());
	            session.connect(TIMEOUT);
	            FimetLogger.debug(Ftp.class,"Connected to "+session.getHost()+" with "+session.getUserName());
	            channel = (ChannelSftp)session.openChannel("sftp");
	            
		        channel.connect();
		        status = Status.CONNECTED;
		        listener.onConnected(this);
		        Manager.get(IEventManager.class).fireEvent(EnviromentEvent.FTP_CONNECTED, this, this);
		        //channelSftp.cd(SFTPWORKINGDIR); // Change Directory on SFTP Server
		    } catch (Exception ex) {
		        disconnect();
		        throw new FtpException(ex);
		    }
		}
	}
	public void disconnect() {
		if (status != Status.DISCONNECTED) {
	        if (channel != null)
	        	try { 
	        		channel.disconnect();
	        		channel = null;
	        	} catch (Exception ex) {}
	        if (session != null) {
	        	try {
	        		session.disconnect();
	        	} catch (Exception ex) {}
	        }
	        status = Status.DISCONNECTED;
	        listener.onDisconnected(this);
	        Manager.get(IEventManager.class).fireEvent(EnviromentEvent.FTP_DISCONNECTED, this, this);
	        FimetLogger.debug(Ftp.class,"Disconnected from "+name+" ("+session.getHost() + " " + session.getPort()+")");
		}
	}
	@Override
	public String execute(String command) throws FtpException {
		if (channel != null) {
			ChannelExec channel = null;
			try {
				checkConnection();
				channel = (ChannelExec)session.openChannel("exec");
				channel.setCommand(command);
		        InputStream output = channel.getInputStream();
		        channel.connect();
		        String result = FileUtils.readString(output);
		        return result;
			} catch (JSchException e) {
				throw new FtpException(e);
			} catch (IOException e) {
				throw new FtpException(e);
			} finally {
				disconnect(channel);
			}
		}
		return null;
	}

	@Override
	public long download(String command, File out) throws FtpException {
		long downloadSize = 0;
		if (channel != null) {
			InputStream in = null;
			Channel channel = null;
			try {
				checkConnection();
		        channel = session.openChannel("exec");
		        ((ChannelExec) channel).setCommand(command);
		        in = channel.getInputStream();
		        channel.connect();
		        FileUtils.write(in, out, true, false);
		        if (out.exists()) {
		        	downloadSize = out.length();
		        }
		        channel.disconnect();

			} catch (JSchException e) {
				throw new FtpException(e);
			} catch (IOException e) {
				throw new FtpException(e);
			} finally {
				FileUtils.close(in);
				disconnect(channel);
			}
		}
		return downloadSize;
	}
	private void disconnect(Channel channel) {
		if (channel != null) {
			try {
				channel.disconnect();
	        } catch (Exception e) {}
		}
	}
	@Override
	public void download(String command, File out, IFtpDownloadListener listener) throws FtpException {
		if (channel != null) {
			long downloadSize = 0;
			InputStream in = null;
			Channel channel = null;
			try {
				checkConnection();
		        channel = session.openChannel("exec");
		        ((ChannelExec) channel).setCommand(command);
		        in = channel.getInputStream();
		        channel.connect();
		        long numOfLines = FileUtils.writeAndCountLines(in, out, false);
		        if (out.exists()) {
		        	downloadSize = out.length();
		        }
		        channel.disconnect();
		        listener.onFtpDonwloadFinish(out, numOfLines, downloadSize);
			} catch (JSchException e) {
				throw new FtpException(e);
			} catch (IOException e) {
				throw new FtpException(e);
			} finally {
				FileUtils.close(in);
				disconnect(channel);
			}
		}
		
	}
	private void checkConnection() {
		if (channel != null && timeCheckConnection != null && new Date().after(timeCheckConnection)) {
			timeCheckConnection = new Date(new Date().getTime() + TIME_TO_ATTEMP_RECONNECT);
			try {
				channel.pwd();
				if (!session.isConnected()) {
					reconnect();
				}
			} catch (SftpException e) {
				reconnect();
			}
		}
	}
	private void reconnect() {
		disconnect();
		connect();
	}
	@Override
	public boolean isDisconnected() {
		return status == Status.DISCONNECTED;
	}
	@Override
	public boolean isConnected() {
		return status == Status.CONNECTED;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public Integer getPort() {
		return session.getPort();
	}
	@Override
	public String getAddress() {
		return session.getHost();
	}
	@Override
	public String getUser() {
		return session.getUserName();
	}
	@Override
	public IConnectionListener getConnectionListener() {
		return listener;
	}
	@Override
	public void setConnectionListener(IConnectionListener listener) {
		this.listener = listener;
	}
	@Override
	public boolean isConnecting() {
		return status == Status.CONNECTING;
	}
}
