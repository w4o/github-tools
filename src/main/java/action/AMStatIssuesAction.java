package action;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;

import git.frank.issues.Issues;
import utils.FileUtils;

public class AMStatIssuesAction extends Action {
	
	private Logger log = Logger.getLogger(AMStatIssuesAction.class);
	
	public AMStatIssuesAction(String login, String password, String repository) {
		super(login, password, repository);
	}
	
	public void run() {
		
		try {
			// 实例化 Issues
			Issues issues = new Issues(this.login, this.password, this.repository);
			
			// 早报统计
			String result = issues.aMStatIssues();
	
			log.info(result);
			
			String title = "Issues早报--" + DateFormatUtils.format(new Date(), "yyyy-mm-dd");
			// issues Number
			int issuesNum = issues.createIssues(title,result);
			
			// 文件写入路径
			String path = FileUtils.getPath() + File.separatorChar + repository.replace("/", "_") + "AMIssuesNum";
			
			// Issues编号写入文件
			FileUtils.writeLine(path, issuesNum + "");
			
			log.info("issues编号：#" + issuesNum);
			
		} catch (IOException e) {
			log.error("创建早报issues失败！异常信息：" + e.getMessage());
			return;
		}
	}
	
}
