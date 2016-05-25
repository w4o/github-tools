package git.frank;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import git.frank.issues.Issues;

public class Main {
	
	
	private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	private final static String PATH;
	
	private final static Logger log = Logger.getLogger(Main.class);
	
	static{

		String _path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		
		System.out.println(_path);
		
		PATH = _path.substring(0,_path.lastIndexOf("/"));
		
		//PropertyConfigurator.configure("/log4j.properties");
	}
	
//	public static void main(String[] args) {
//		log.info("info");
//		log.error("error");
//	}
	
	public static void main(String[] args) {
		
		if (args.length != 8 || args[0] == "-help") {
			log.info("未指定参数或参数不足！");
			help();
			System.exit(0);
		}
		
		String login 		= args[1];
		String password 	= args[3];
		String repository 	= args[5];
		String option 		= args[7];
		
		log.info("login=" + login + "|password=" + password + "|repository=" + repository + "|option=" + option);
		
		//jar 包所在路径
		String path = PATH;
		
		switch (option) {
		case "AMStatIssues":
			try {
				Issues issues = new Issues(login, password, repository);
				String result = issues.aMStatIssues();

				log.info(result);
				
				String title = "Issues早报--" + simpleDateFormat.format(new Date());
				int issuesNum = issues.createIssues(title,result);
				
				path = path + File.separatorChar + repository.replace("/", "_") + "AMIssuesNum";
				File file = new File(path);
				if (!file.exists()) {
					log.info("文件不存在，创建文件：" + path);
					file.createNewFile();
				}
				
				FileOutputStream out = new FileOutputStream(file);
				out.write((issuesNum+"").getBytes());
				out.close();
				
				log.info("issues编号：#" + issuesNum);
				
				System.exit(0);
			} catch (IOException e) {
				log.error("创建早报issues失败！异常信息：" + e.getMessage());
				System.exit(-1);
			}
			break;
		case "PMStatIssues":
			try {
				Issues issues = new Issues(login, password, repository);
				path = path + File.separatorChar + repository.replace("/", "_") + "AMIssuesNum";
				File file = new File(path);
				if (!file.exists()) {
					log.error("早报Issues Num文件丢失，无法正常读取！");
					System.exit(-1);
				}
				
				//读取早报Issues Num保存文件
				InputStream in = new FileInputStream(file);
				InputStreamReader read = new InputStreamReader(in);
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineText = bufferedReader.readLine();
				bufferedReader.close();
				read.close();
				if (lineText == null || lineText == "") {
					log.error("早报Issues Num文件读取失败，无法正常读取Issues Num！");
					System.exit(-1);
				}
				
				int issuesNumber = Integer.parseInt(lineText);
				//晚报 issues 统计
				String  result = issues.pMStatIssues(issuesNumber);
				//创建issues评论
				issues.createCommentByIssuesNumber(issuesNumber, result);
				log.info(result);
				System.exit(0);
			} catch (IOException e) {
				log.error("创建晚报issues失败！异常信息：" + e.getMessage());
				System.exit(-1);
			}
			System.exit(0);
			break;
		case "buildWiki":
			
			break;
		default:
			System.out.println("没有找到对应的操作，请使用 -help 选项查看帮助信息");
			break;
		}
		
	}
	
	
	public static void help(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("                   _ooOoo_").append("\n");
		buffer.append("                  o8888888o").append("\n");
		buffer.append("                  88\" . \"88").append("\n");
		buffer.append("                  (| -_- |)").append("\n");
		buffer.append("                  O\\  =  /O").append("\n");
		buffer.append("               ____/`---'\\____").append("\n");
		buffer.append("             .'  \\|     |//  `.").append("\n");
		buffer.append("            /  \\\\|||  :  |||//  \\").append("\n");
		buffer.append("           /  _||||| -:- |||||-  \\").append("\n");
		buffer.append("           |   | \\\\\\  -  /// |   |").append("\n");
		buffer.append("           | \\_|  ''\\---/''  |   |").append("\n");
		buffer.append("           \\  .-\\__  `-`  ___/-. /").append("\n");
		buffer.append("         ___`. .'  /--.--\\  `. . __").append("\n");
		buffer.append("      .\"\" '<  `.___\\_<|>_/___.'  >'\"\".").append("\n");
		buffer.append("     | | :  `- \\`.;`\\ _ /`;.`/ - ` : | |").append("\n");
		buffer.append("     \\  \\ `-.   \\_ __\\ /__ _/   .-` /  /").append("\n");
		buffer.append("======`-.____`-.___\\_____/___.-`____.-'======").append("\n");
		buffer.append("").append("\n");
		buffer.append("----========= 佛祖保佑  用无Bug =========----").append("\n");
		buffer.append("----                Frank                ----").append("\n");
		buffer.append("---------------------------------------------").append("\n");
		buffer.append("=============================================\n");
		buffer.append("\t\t帮助\n");
		buffer.append("=============================================\n");
		buffer.append("必须参数：\n");
		buffer.append("\t-u\t用户名\n");
		buffer.append("\t-p\t密码\n");
		buffer.append("\t-r\t仓库路径（如：lincoln56/2048）\n");
		buffer.append("\t-o\t选项\n");
		buffer.append("---------------------------------------------\n");
		buffer.append("[选项]可选值：\n");
		buffer.append("\tAMStatIssues\n");
		buffer.append("\tPMStatIssues\n");
		//buffer.append("\tbuildWiki\n");
		buffer.append("=============================================\n");
		
		System.out.println(buffer);
	}
	
	
}
