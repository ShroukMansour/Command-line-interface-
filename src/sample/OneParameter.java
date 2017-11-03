
package sample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OneParameter extends Command {
	private String firstParam;
	private String realCmd, fileName;
	private boolean appendToFile = false;
	private boolean writeToFile = false;

	@Override
	public void executeCommand(String cmd) { 
		if (verifyCommand(cmd)) {
			if ("cd".equals(realCmd)) {
				String[] dirs = firstParam.split("\\\\");
				String newPath = "";
				if (dirs.length > 1) {
					for (int i = 0; i < dirs.length; i++) {
						if ("..".equals(dirs[i + 1])) {
							i += 2;
						} else {
							newPath += dirs[i];
						}
					System.setProperty("user.dir", System.getProperty("user.dir") + "\\" + newPath);
					}
				} else 
					System.setProperty("user.dir", System.getProperty("user.dir") + "\\" + firstParam);
				
			} else if ("mkdir".equals(realCmd)) {
				new File(System.getProperty("user.dir") + "\\" + firstParam).mkdir();
			} else if ("rmdir".equals(realCmd))
				new File(System.getProperty("user.dir") + firstParam).delete();
			else if ("rm".equals(realCmd)) {
				File file_remove = new File(System.getProperty("user.dir") + firstParam);
				file_remove.delete();
			} else if ("cat".equals(realCmd)) {
				FileInputStream fis = null;
				try {
					File file = new File(System.getProperty("user.dir") + firstParam);
					fis = new FileInputStream(file);
					byte[] data = new byte[(int) file.length()];
					fis.read(data);
					fis.close();
					String text = new String(data, "UTF-8");
					checkWhereToWrite(text);
				} catch (FileNotFoundException ex) {
					Logger.getLogger(OneParameter.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IOException ex) {
					Logger.getLogger(OneParameter.class.getName()).log(Level.SEVERE, null, ex);
				} finally {
					try {
						fis.close();
					} catch (IOException ex) {
						Logger.getLogger(OneParameter.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			} else if ("more".equals(realCmd)) {

				String line, cont;
				BufferedReader br = null;
				try {
					br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\" + firstParam));
					while (true) {
						for (int i = 0; i < 10; i++) {
							line = br.readLine();
							if (line == null) {
								System.out.println("sorry the file end");
								break;
							}
							System.out.println(line);
						}
						System.out.println("Do you want to read more?y/n");
						Scanner input = new Scanner(System.in);
						cont = input.next();
						if ("y".equals(cont))
							continue;
						else
							break;
					}

				} catch (IOException ex) {
					Logger.getLogger(OneParameter.class.getName()).log(Level.SEVERE, null, ex);
				}

			} else if ("args".equals(realCmd)) {
				if ("cd".equals(firstParam))
					checkWhereToWrite("Take the distination file as an argument\n");
				if ("mkdir".equals(firstParam))
					checkWhereToWrite("Take the directory name to be created as an argument\n");
				if ("rmdir".equals(firstParam))
					checkWhereToWrite("Take the directory name to be deleted as an argument\n");
				if ("cat".equals(firstParam))
					checkWhereToWrite("Take the file name to be all listed as an argument\n");
				if ("more".equals(firstParam))
					checkWhereToWrite("Take the file name to be listed sperately as an argument\n");
				if ("?".equals(firstParam))
					checkWhereToWrite("Take the command you want help to as an argument\n");
				if ("cp".equals(firstParam))
					checkWhereToWrite("Take the you want to copy and the file to copy to as an argument\n");
				if ("mv".equals(firstParam))
					checkWhereToWrite(
							"Take the file you want to rename and the new name or takes the file you want to move and the destination folder name as an argument\n");
				if ("ls".equals(firstParam) || "clear".equals(firstParam) || "pwd".equals(firstParam)
						|| "date".equals(firstParam) || "help".equals(firstParam))
					checkWhereToWrite("This commands have no argument\n");

			} else if ("?".equals(realCmd)) {
				if ("cd".equals(firstParam))
					checkWhereToWrite("This command change the working director\ny");
				if ("mkdir".equals(firstParam))
					checkWhereToWrite("This command makes a directory\n");
				if ("rmdir".equals(firstParam))
					checkWhereToWrite("This command delets a directory\n");
				if ("cat".equals(firstParam))
					checkWhereToWrite("This command prints all the content of a file\n");
				if ("more".equals(firstParam))
					checkWhereToWrite("This command prints some of content of a file when you ask more\n");
				if ("args".equals(firstParam))
					checkWhereToWrite("This command tells you the arguments of any command\n");
				if ("cp".equals(firstParam))
					checkWhereToWrite("This command copy a file to another file\n");
				if ("mv".equals(firstParam))
					checkWhereToWrite("This command rename a file or move to another directory\n");
				else if ("ls".equals(firstParam))
					checkWhereToWrite("This command lists all content of the current directory\n");
			}

		} else {
			System.out.println("Please enter a valid command!");
		}
	}

	@Override
	public boolean verifyCommand(String cmd) {
		if (cmd.indexOf(">>") > -1) {
			appendToFile = true;
			writeToFile = false;
			String[] params = cmd.split(">>");
			cmd = params[0];
			fileName = params[1];
			System.out.println(cmd + " " + fileName);
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

		splitCommand(cmd);
		if ("cd".equals(realCmd) || "rmdir".equals(realCmd)) {
			File file = new File(System.getProperty("user.dir") + "\\" + firstParam);
			if (file.isDirectory()) {
				return true;
			}
		} else if ("cat".equals(realCmd) || "more".equals(realCmd)) {
			File file = new File(System.getProperty("user.dir") + firstParam);
			if (file.exists())
				return true;
		} else if ("args".equals(realCmd))
			return true;
		else if ("?".equals(realCmd))
			return true;
		else if ("mkdir".equals(realCmd))
			return true;
		else if ("rm".equals(realCmd)) {
			File file = new File(System.getProperty("user.dir") + firstParam);
			if (file.exists())
				return true;
		}

		return false;
	}

	public void checkWhereToWrite(String text) {
		if (appendToFile) {
			BufferedWriter bw = null;
			try {
				System.out.println(System.getProperty("user.dir") + fileName);
				bw = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + fileName, true));
				bw.write(text);
				bw.newLine();
				bw.flush();
				System.out.println("Cone");
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} finally { // always close the file
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

	@Override
	public void splitCommand(String cmd) {
		String[] params = cmd.split(" ");
		realCmd = params[0];
		firstParam = params[1];
	}

}
