import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.Scanner;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class Util {

	static FTPClient ftp = new FTPClient();
	static final private String FTPLOGIN = "anonymous";
	static final private String FTPPASSWORD = "";

	public static boolean connect(String ftpUrl) throws SocketException, IOException {
		try {
			ftp.connect(ftpUrl);

			System.out.println("Connected to " + ftpUrl );
			ftp.login(FTPLOGIN, FTPPASSWORD);
			ftp.enterLocalPassiveMode();
			// ftp.enterLocalActiveMode();

		} catch (Exception e) {
			System.out.println("Message: " + e.getMessage());
			return false;

		}
		return true;

	}

	public static void printFilesName() throws IOException {

        if(ftp.isConnected())
        {
		System.out.println("List of files:");
		System.out.println("**********************");
		FTPFile[] ftpfile = ftp.listFiles();
		for(FTPFile f : ftpfile)
		{
			if(f.isDirectory())
			{
				System.out.println("["+f.getName()+"]");	
			}
			else
			{
				System.out.println(f.getName());
			}
			
			
		}
			
		System.out.println("**********************");
        }
        else
        {
        	System.out.println("Connect error!");
        	
        }

	}
	
	public static void downloadFiles(String nameOfFile) throws IOException 
	{
		
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        
        File downloadFile = new File("C:/Downloads/"+nameOfFile);
        
        OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile));
        boolean success = ftp.retrieveFile(nameOfFile, outputStream1);
        outputStream1.close();

        if (success) {
            System.out.println("File has been downloaded successfully.");
        }
        else
        {
        	System.out.println("File is not found");
        }

		

	}
static public void menu() throws SocketException, IOException {
		
		Scanner sc = new Scanner(System.in);
		boolean b = true;

		System.out.println("Type 'usage' - for more information about command");

		while (b) {

			String choice = sc.nextLine();
			String[] command = choice.split(" ");
			if (command[0].equals("connect") && command.length > 1) {
				if (!(Util.connect(command[1]))) {
					System.out.println("Connection error");

				}
			} else {

				switch (command[0]) {
				//////////////////////////////////////////////////////
				case "usage":

					System.out.println("connect    -        connection to ftp server (con ftp.example.com)");
					System.out.println("cd         -        go into folder (cd examle) ");
					System.out.println("ls         -        to print dir content (print example)");
					System.out.println("back       -        go to parent dir (parent example) ");
					System.out.println("download   -        to download file (download example)  ");
					System.out.println("clear      -        to clear");
					System.out.println("exit       -        to end");
					break;
				//////////////////////////////////////////////////////
				case "ls":
					if (Util.ftp.isConnected()) {
						Util.printFilesName();
					} else {
						System.out.println("For a start you have to connect to ftp!");
					}
					break;

				//////////////////////////////////////////////////////
				case "cd":

					if (command.length > 1) {

						if (Util.ftp.changeWorkingDirectory(command[1])) {
							Util.printFilesName();
						} else {
							System.out.println("Incorrect directory");
						}

					} else {
						System.out.println("Incorrect command");

					}

					break;

				//////////////////////////////////////////////////////
				case "back":
					if (command.length == 1) {

						if (Util.ftp.isConnected()) {
							Util.ftp.changeToParentDirectory();
							Util.printFilesName();
						}
					} else
						System.out.println("Incorrect command!");

					break;

				//////////////////////////////////////////////////////
				case "download":
                    
                     if(command.length>1)
                     {
					Util.downloadFiles(command[1]);
                     }
                     else 
                     {
                    	 System.out.println("Incorrect command!");
                     }

					break;
				//////////////////////////////////////////////////////
				case "exit":
					System.out.println("Good bye");
					b = false;
					break;
				//////////////////////////////////////////////////////
				default:
					System.out.println("Incorrect command!");
					break;

				}

			}
		}

	}
	
	
}
