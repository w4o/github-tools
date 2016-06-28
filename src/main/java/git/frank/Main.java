package git.frank;

import org.apache.log4j.Logger;

import action.AMStatIssuesAction;
import action.CreateLabelAction;
import action.PMStatIssuesAction;

public class Main {
	
	// Log日志
	private final static Logger log = Logger.getLogger(Main.class);
	
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
		
		switch (option) {
		case "AMStatIssues":
			AMStatIssuesAction.run(login, password, repository);
			break;
		case "PMStatIssues":
			PMStatIssuesAction.run(login, password, repository);
			break;
		case "CreateLabel":
			CreateLabelAction.run(login, password, repository);
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
		buffer.append("\tCreateLabel\n");
		buffer.append("[选项]说明：\n");
		buffer.append("\tAMStatIssues：（无）\n");
		buffer.append("\tPMStatIssues：（无）\n");
		buffer.append("\tCreateLabel：在根目录创建“lables.txt”文件，内容为标签描述信息，每一行为一个标签，格式为“[标签名]#[颜色16进制值]#[选项]”\n");
		buffer.append("\t\t选项可选值\n");
		buffer.append("\t\t1.del\t“del：删除当前标签”，例：“已完成#00ff00”或“已完成#00ff00#del”\n");
		buffer.append("=============================================\n");
		
		System.out.println(buffer);
	}
	
	
}
