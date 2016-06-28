package action;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import git.frank.issues.Label;
import utils.FileUtils;

public class CreateLabelAction {
	
	private static Logger log = Logger.getLogger(CreateLabelAction.class);
	
	public static void run(String login, String password, String repository) {
		try {
			// 实例化 Label 对象
			Label label = new Label(login, password, repository);
			
			// labels 文件
			String path = FileUtils.getPath() + File.separatorChar + "labels.txt";
			File file = new File(path);
			if (!file.exists()) {
				log.error("标签文件不存在，无法正常创建issues标签");
				return;
			}
			
			List<String> labels = FileUtils.readLines(path);
			boolean labelExist = false;
			for (String str : labels) {
				String[] arr = str.split("#");
				
				// 是否存在
				labelExist = label.exist(arr[0]);
				
				// 如果数组长度等于3，表示有特殊操作
				if (arr.length==3) {
					// d 表示删除改标签
					switch (arr[2]) {
					case "del":
						if (!labelExist) {
							log.warn("标签［"+ arr[0] +"］不存在，无法删除。[自动忽略]");
							continue;
						}
						label.removeLabel(arr[0]);
						log.info("标签［"+ arr[0]+"］删除成功！");
						break;

					default:
						log.warn("发现意外的选项值["+arr[3]+"]，请仔细检查。[自动忽略]" );
						break;
					}
				} else {
					
					log.info("正在创建：" + arr[0]);
					
					if (labelExist) {
						log.warn("标签［"+arr[0]+"］已存在，无法创建。[自动忽略]");
						continue;
					}
					try {
						label.createLabel(arr[0], arr[1]);
					} catch (IOException e) {
						log.error("创建label失败！异常信息：" + e.getMessage());
					}
				}
			}
			log.info("标签批量设置完成！");
		} catch (IOException e) {
			log.error("创建label失败！异常信息：" + e.getMessage());
		}
	}

}
