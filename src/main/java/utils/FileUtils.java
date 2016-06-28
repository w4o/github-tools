package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import git.frank.Main;

public class FileUtils {

	private final static Logger log = Logger.getLogger(FileUtils.class);

	public static final String PATH;

	static {

		String _path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();

		PATH = _path.substring(0, _path.lastIndexOf("/"));

	}
	
	/**
	 * 获取jar文件绝对路径
	 * @return
	 */
	public static String getPath(){
		return PATH;
	}
	
	/**
	 * 向文件写入一行纪录
	 * @param path
	 * @param text
	 */
	public static void writeLine(String path, String text) {
		List < String > textList = new ArrayList<>();
		textList.add(text);
		FileUtils.writeLines(path, textList);
	}
	
	/**
	 * 向文件写入纪录
	 * @param path
	 * @param textList
	 */
	public static void writeLines (String path, List < String > textList) {
		File file = new File(path);
		if (!file.exists()) { 
			try {
				file.createNewFile();
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
		
		FileOutputStream out =null;
		try { 
			out= new FileOutputStream(file);
			
			for (String text : textList) 
				out.write((text).getBytes());
		
		} catch ( FileNotFoundException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			if (out != null ) {
				try {
					out.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		}
	}

	/**
	 * 读取文件一行纪录
	 * 
	 * @param name
	 * @return
	 */
	public static String readLine(String path) {
		List<String> list = FileUtils.readLines(path);
		return list.size() > 0 ? list.get(0) : "";
	}

	/**
	 * 读取文件多行纪录
	 * 
	 * @param name
	 * @return
	 */
	public static List<String> readLines(String path) {
		File file = new File(path);
		List<String> result = new ArrayList<>();
		InputStream in = null;
		InputStreamReader read = null;
		BufferedReader bufferedReader = null;
		try {
			in = new FileInputStream(file);
			read = new InputStreamReader(in);
			bufferedReader = new BufferedReader(read);
			String lineText = null;
			while ((lineText = bufferedReader.readLine()) != null) {
				result.add(lineText);
			}
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}

			if (read != null) {
				try {
					read.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}

			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		}

		return result;
	}

}
