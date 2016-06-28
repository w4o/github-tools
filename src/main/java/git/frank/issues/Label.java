package git.frank.issues;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;
import org.kohsuke.github.GHLabel;

import git.frank.Repository;
import utils.Ex;
import utils.ExUtils;

public class Label extends Repository  {
	
	Logger log = Logger.getLogger(Label.class);
	
	public Label(String login, String password, String repository) throws IOException {
		super(login, password, repository);
	}
	
	/**
	 * 创建 Label
	 * @param name
	 * @param color
	 * @throws IOException
	 */
	public void createLabel(String name, String color ) throws IOException {
		this.ghRepository.createLabel(name, color);
	}
	
	/**
	 * 获取Label
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public GHLabel getLabelByName(String name) throws IOException{
		return this.ghRepository.getLabel(URLEncoder.encode(name,"UTF-8"));
	}
	
	/**
	 * 删除Label
	 * @param label
	 * @throws IOException
	 */
	public void removeLabel(String name) throws IOException {
		GHLabel ghLabel = this.ghRepository.getLabel(URLEncoder.encode(name,"UTF-8"));
		ghLabel.delete();
	}
	
	/**
	 * 判断标签是否存在
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public boolean exist(String name) throws IOException {
		
		try {
			GHLabel ghLabel = this.getLabelByName(name);
			if (ghLabel==null) {
				return false;
			} else { 
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			throw e;
		} catch (IOException e) {
			Ex ex = ExUtils.buildEx(e.getMessage());
			if (ex==null) {
				log.error("检查标签发生异常，异常信息："  + e.getMessage());
				throw e;
			} else { 
				switch (ex.getMessage()) {
				case "Not Found":
					return false;
				default:
					throw e;
				}
			}
		}
	}
	
}
