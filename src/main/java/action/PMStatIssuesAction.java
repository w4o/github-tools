package action;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import git.frank.issues.Issues;
import utils.FileUtils;

public class PMStatIssuesAction extends Action {

	private Logger log = Logger.getLogger(PMStatIssuesAction.class);

	public PMStatIssuesAction(String login, String password, String repository) {
		super(login, password, repository);
	}

	public void run() {
		try {
			// 实例化 Issues
			Issues issues = new Issues(login, password, repository);
			// 获取 issuesNum 存储绝对路径
			String path = FileUtils.getPath() + File.separatorChar + repository.replace("/", "_") + "AMIssuesNum";
			// 判断文件是否存在
			File file = new File(path);
			if (!file.exists()) {
				log.error("早报Issues Num文件丢失，无法正常读取！");
				return;
			}
			// 读取 issues Number
			String issuesNumberStr = FileUtils.readLine(path);
			if (issuesNumberStr == null || issuesNumberStr == "") {
				log.error("早报Issues Num文件读取失败，无法正常读取Issues Num！");
				return;
			}
			int issuesNumber = Integer.parseInt(issuesNumberStr);

			// 晚报 issues 统计
			String result = issues.pMStatIssues(issuesNumber);
			// 创建issues评论
			issues.createCommentByIssuesNumber(issuesNumber, result);
			log.info(result);

		} catch (IOException e) {
			log.error("创建晚报issues失败！异常信息：" + e.getMessage());
		}
	}

}
