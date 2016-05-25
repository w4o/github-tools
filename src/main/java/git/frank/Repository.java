package git.frank;

import java.io.IOException;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

public class Repository {

	protected GitHub gitHub;
	protected GHRepository ghRepository;
	
	
	public Repository(String login, String password, String repository) throws IOException {
		//实例化 Github
		gitHub = GitHub.connectUsingPassword(login, password);
		//选择仓库
		ghRepository = gitHub.getRepository(repository);
	}
	
}
