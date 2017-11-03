package sample;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author de
 */
public class NoParameter extends Command {

	public boolean writeToFile = false, appendToFile = false;
	String fileName;

	@Override
	public void executeCommand(String cmd) {
		if (verifyCommand(cmd)) {
			if ("clear".equals(cmd)) {
				for (int i = 0; i < 10; i++)
					System.out.println("\n");
			} else if ("exit".equals(cmd)) {
				System.exit(0);
			}

			else if ("pwd".equals(cmd)) {
				String current_directory = System.getProperty("user.dir");
				Current_dir = current_directory;
				checkWhereToWrite("Your Current Directory " + current_directory);

			} else if ("ls".equals(cmd)) {
				String current_directory = System.getProperty("user.dir");
				File Folder = new File(current_directory);
				File[] ListOfFiles = Folder.listFiles();
				for (int i = 0; i < ListOfFiles.length; i++) {
					if (ListOfFiles[i].isFile()) {
						checkWhereToWrite("File " + ListOfFiles[i].getName());
					} else if (ListOfFiles[i].isDirectory()) {
						checkWhereToWrite("Directory " + ListOfFiles[i].getName());
					} else {
						System.out.println("There are no files or directories");
					}
				}
			} else if ("date".equals(cmd)) {
				Date date = new Date();

				// display time and date
				String str = String.format("Current Date/Time : %tc ", date);

				checkWhereToWrite(str);

			} else {
				System.out.println("your Entered wrong Command");
			}
		} else {
			System.out.println("Your Enter invalid Command");
		}

	}

	@Override
	public void splitCommand(String Cmd) {

	}

	@Override
	public boolean verifyCommand(String cmd) {
		if (cmd.indexOf(">>") > -1) {
			appendToFile = true;
			writeToFile = false;
			String[] params = cmd.split(">>");
			cmd = params[0];
			fileName = params[1];
		} else if (cmd.indexOf(">") > -1) {
			writeToFile = true;
			appendToFile = false;
			String[] params = cmd.split(">");
			cmd = params[0];
			fileName = params[1];
			if (fileName.charAt(0) == ' ') {
				StringBuilder sb = new StringBuilder(fileName);
				sb.deleteCharAt(0);
				fileName = sb.toString();
			}
		}

		if (("clear").equals(cmd) || ("pwd").equals(cmd) || ("exit").equals(cmd) || ("ls").equals(cmd)
				|| ("date").equals(cmd)) {
			return true;
		}
		return false;
	}

	public void checkWhereToWrite(String text) {
		if (appendToFile) {
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + fileName, true));
				bw.write(text);
				bw.newLine();
				bw.flush();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} finally {
				if (bw != null)
					try {
						bw.close();
					} catch (IOException ioe2) {

					}
			}
			appendToFile = false;
		} else if (writeToFile) {
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + fileName, false));
				bw.write(text);
				bw.newLine();
				bw.flush();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} finally { // always close the file
				if (bw != null)
					try {
						bw.close();
					} catch (IOException ioe2) {

					}
			}
			writeToFile = false;
		} else {
			System.out.println(text);
		}
	}

}
