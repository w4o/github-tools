package git.frank.issues;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueBuilder;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHUser;

import git.frank.Repository;

public class Issues extends Repository {
	
	private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private final static String UNKNOWN = "unknown";
	
	public Issues(String login, String password, String repository) throws IOException {
		super(login, password, repository);
	}
	
	/**
	 * 早报 Issues 统计
	 * @param username
	 * @param password
	 * @param repository
	 * @return
	 * @throws IOException
	 */
	public String aMStatIssues() throws IOException{
		
		//获取仓库中用户
		Set < String > userNames = ghRepository.getCollaboratorNames();
		
		//封装 issues Map
		Map < String , List < GHIssue >> issuesMap = new LinkedHashMap < String , List < GHIssue >>();
		for (String userName : userNames) 
			issuesMap.put(userName, new ArrayList< GHIssue >());
		issuesMap.put(UNKNOWN, new ArrayList< GHIssue >());
		
		
		//获取仓库中 open issues 总数
		int issueCount = ghRepository.getOpenIssueCount();
		
		//构造早报 issues Markdown 文本
		StringBuffer buffer = new StringBuffer();
		buffer.append("# ISSUES 早报 ").append(simpleDateFormat.format(new Date())).append("\n\n");	//# ISSUES 早报 2016-05-12
		buffer.append("## 当前共有 Open Issues `").append(issueCount).append("` 条").append("\n\n");	//## 当前共有 Open Issues `21` 条
		
		//获取仓库中所有打开的issues
		List < GHIssue > issuesList = ghRepository.getIssues(GHIssueState.OPEN);
		
		//遍历 issues 列表，并封装 issuesMap
		for (GHIssue issue : issuesList ) {
			
			GHUser user = issue.getAssignee();
			
			String _uname = "";
			
			if (user != null) {
				_uname = user.getLogin();
				
			} else { 
				_uname = UNKNOWN;
			}
			
			List < GHIssue > _list = issuesMap.get(_uname);
			_list.add(issue);
			 
		}
		
		StringBuffer headBuffer = new StringBuffer();
		StringBuffer detailBuffer = new StringBuffer();
		//遍历 issuesMap ，继续构造 issues Markdown 文本
		for(Map.Entry< String ,  List < GHIssue > > entry : issuesMap.entrySet()) {
			String _name = entry.getKey();
			List < GHIssue > _list = entry.getValue();
			headBuffer.append("> ").append(_name).append(" ").append(_list.size()).append(" 条").append("\n\n");					//> Inchris 21 条
			
			if (_list.size() > 0 ) { 
				detailBuffer.append("## ").append(entry.getKey()).append("\n\n");												//## Inchris
				for (GHIssue issue : _list) {
					detailBuffer.append("#").append(issue.getNumber()).append(" : ").append(issue.getTitle()).append("\n\n");	//#41 : fkdlsjfldsfljslfjdsl
				}
			}
		}
		
		buffer.append(headBuffer);
		buffer.append("--").append("\n\n");
		buffer.append(detailBuffer);
		return buffer.toString();
	}
	
	/**
	 * 创建 issues
	 * @param username
	 * @param password
	 * @param repository
	 * @param content
	 * @return
	 * @throws IOException
	 */
	public int createIssues(String title,String content) throws IOException {
		
		GHIssueBuilder ghIssueBuilder = ghRepository.createIssue(title);
		ghIssueBuilder.body(content);
		GHIssue issue = ghIssueBuilder.create();
		return issue.getNumber();
	}

	/**
	 * 晚报 Issues 统计
	 * @param username
	 * @param password
	 * @param repository
	 * @return
	 * @throws IOException 
	 */
	public String pMStatIssues(int amIssuesNumber) throws IOException {
		
		//早报 issues 内容
		GHIssue ghIssue = ghRepository.getIssue(amIssuesNumber);
		String amIssues = ghIssue.getBody();
		
		//当前打开的issues
		List < GHIssue > issuesList = ghRepository.getIssues(GHIssueState.OPEN);
		//封装issuesMap
		Map < Integer , GHIssue > issuesMap = new HashMap<Integer, GHIssue>();
		for (GHIssue issues : issuesList )
			issuesMap.put(issues.getNumber(), issues);
		//删除掉当前早报issues
		issuesMap.remove(amIssuesNumber);
		
		//解析早报 issues，并标记已完成的issues
		String[] issuesArr = amIssues.split("\n");
		Pattern pIssuesNumber = Pattern.compile("^#(\\d+) : ");
		Pattern pUsername = Pattern.compile("^## (\\w+)$");
		Matcher m;
		boolean mark = false;
		Map < String , List <String >> markedIssuesMap = new LinkedHashMap<String , List <String>>();
		String username = "";
		for (String str : issuesArr) {
			if (str.equals("--")) 
				mark = true;
			
			if (mark) {
				m = pUsername.matcher(str);
				if (m.find()) { //匹配到username
					username = m.group(1);
					markedIssuesMap.put(username, new ArrayList< String >());
				}
			}
			
			m = pIssuesNumber.matcher(str);
			if (m.find()){	//匹配到issues编号
				int _issuesNumber =Integer.parseInt(m.group(1)); 
				if (issuesMap.get(_issuesNumber) == null) 	//issuesMap 中不存在，说明该issues已经关闭
					str = "~~" + str + "~~";				//标记删除线
				else
					issuesMap.remove(_issuesNumber);			//重复issues，在map中删除掉
				
				List < String > _list = markedIssuesMap.get(username);
				_list.add(str);
			}
			
		}
		
		//重新封装issues Map，将key变成 username
		Map < String , GHIssue > map  = new HashMap<>(); 
		for (Map.Entry< Integer,  GHIssue> entry : issuesMap.entrySet()) {
			GHIssue _issues = entry.getValue();
			GHUser user = _issues.getAssignee();
			
			String _uname = "";
			
			if (user != null) {
				_uname = user.getLogin();
				
			} else { 
				_uname = UNKNOWN;
			}
			map.put(_uname, entry.getValue());
		}
		
		//创建晚报issues统计
		StringBuffer buffer = new StringBuffer();
		for (Map.Entry< String , List<String> > entry: markedIssuesMap.entrySet()) { 
			String _username = entry.getKey();
			buffer.append("## ").append(_username).append("\n\n");
			for (String str : entry.getValue())
				buffer.append(str).append("\n\n");
			GHIssue  _issues = map.get(_username);
			if (_issues != null)
				buffer.append("#").append(_issues.getNumber()).append(" : ").append(_issues.getTitle()).append("\n\n");
			
		}
		
		return buffer.toString();
	}

	/**
	 * 根据issues编号创建评论
	 * @param issuesNumber
	 * @param message
	 * @throws IOException
	 */
	public void createCommentByIssuesNumber(int issuesNumber, String message) throws IOException {
		GHIssue issue = ghRepository.getIssue(issuesNumber);
		issue.comment(message);
	}
	
}
