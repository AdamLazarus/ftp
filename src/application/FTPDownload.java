package application;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FTPDownload {

	private FTPClient ftpClient;
	private String currentUserPath;

	public FTPDownload(String server, int port, String username, String pass){
		System.out.println(System.getProperty("user.home"));
		currentUserPath = System.getProperty("user.home");
		ftpClient = new FTPClient();
		try{
			ftpClient.connect(server, port);
			ftpClient.login(username, pass);
		}
		catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
		}
	}

	/*public static void main(String[] args) {
		FTPDownload download = new FTPDownload("31.170.161.56", 21, "a6152286", "peggy123");
		download.Download();
	}*/

	 public void Download(String selectedItem) {

		 try {
	            ftpClient.enterLocalPassiveMode();
	            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);


	            String remoteFile = "selectedItem";
	            File downloadFile = new File("/home/adam/Music/index.html");
	            OutputStream outputStream2 = new BufferedOutputStream(new FileOutputStream(downloadFile));
	            InputStream inputStream = ftpClient.retrieveFileStream(remoteFile);
	            byte[] bytesArray = new byte[4096];
	            int bytesRead = -1;
	            while ((bytesRead = inputStream.read(bytesArray)) != -1) {
	                outputStream2.write(bytesArray, 0, bytesRead);
	            }


	            boolean success = ftpClient.completePendingCommand();
	            if (success) {
	                System.out.println(selectedItem + " has been downloaded successfully.");
	            }
	            outputStream2.close();
	            inputStream.close();

	        } catch (IOException ex) {
	            System.out.println("Error: " + ex.getMessage());
	            ex.printStackTrace();
	        } finally {
	            try {
	                if (ftpClient.isConnected()) {
	                    ftpClient.logout();
	                    ftpClient.disconnect();
	                }
	            } catch (IOException ex) {
	                ex.printStackTrace();
	            }
	        }
	    }

	 public void Upload(String selectedItem){

		 try{
			 File secondLocalFile = new File(currentUserPath + "/" + selectedItem);
	         String secondRemoteFile = selectedItem;
	         FileInputStream inputStream = new FileInputStream(secondLocalFile);

	         System.out.println("Start uploading second file");
	         OutputStream outputStream = ftpClient.storeFileStream(secondRemoteFile);
	         byte[] bytesIn = new byte[4096];
	         int read = 0;

	         while ((read = inputStream.read(bytesIn)) != -1) {
	             outputStream.write(bytesIn, 0, read);
	         }
	         inputStream.close();
	         outputStream.close();

	         boolean completed = ftpClient.completePendingCommand();
	         if (completed) {
	             System.out.println("The second file is uploaded successfully.");
	         }
     } catch (IOException ex) {
         System.out.println("Error: " + ex.getMessage());
         ex.printStackTrace();
     	}
	 }

	 public ObservableList<String> getFTPItems(boolean hidden){
		// lists files and directories in the current working directory
         FTPFile[] files;
         ObservableList<String> fileList = FXCollections.observableArrayList();
		try {
			files = ftpClient.listFiles();
			// iterates over the files and prints details for each
	         DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	         for (FTPFile file : files) {
	        	 String name = file.getName();
	        	 if(hidden || !name.startsWith(".")){
	        		 if (file.isDirectory()){
	        			 name = "[" + name + "]";
	        		 }
	        	 fileList.add(name);
	        	 }

	             /*String details = file.getName();
	             if (file.isDirectory()) {
	                 details = "[" + details + "]";
	             }
	             details += "\t\t" + file.getSize();
	             details += "\t\t" + dateFormater.format(file.getTimestamp().getTime());
	             System.out.println(details);*/
	         	}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fileList;
	 	}

	 public ObservableList<String> getUserItems(boolean hidden){
		 ObservableList<String> fileList = FXCollections.observableArrayList();
		 File directory = new File(currentUserPath);
		 File[] listOfFiles = directory.listFiles();
		 for(File f:listOfFiles){
			 if(hidden || !f.isHidden()){
				String name = f.getName();
		        if (f.isDirectory()){
		        	name = "[" + name + "]";
		        }
		        fileList.add(name);
			 }
		 }
		 java.util.Collections.sort(fileList);
		 fileList.add(0, "[.]");
		 fileList.add(1, "[..]");
		return fileList;

	 }

	 	public void changeDirectory(String newDirectory){
	 		try {
				ftpClient.changeWorkingDirectory(newDirectory);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 	}

	 	public void changeUserDirectory(String newDirectory){
	 		if(newDirectory == "."){
	 			currentUserPath = "/";
	 		}
	 		else if (newDirectory == ".."){
	 			int endIndex = currentUserPath.lastIndexOf("/");
	 		    if (endIndex != -1)
	 		    {
	 		        currentUserPath = currentUserPath.substring(0, endIndex);
	 		    }
	 		    else{
	 		    	currentUserPath = System.getProperty("user.root");
	 		    }
	 		}
	 		else{
	 			currentUserPath += "/" + newDirectory;
	 		}

	 		System.out.println(currentUserPath);
	 	}
	 }


