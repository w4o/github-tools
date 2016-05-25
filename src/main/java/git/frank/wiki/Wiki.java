package git.frank.wiki;

import java.io.IOException;

import git.frank.Repository;

public class Wiki extends Repository {
	
	public Wiki(String login, String password, String repository) throws IOException {
		super(login, password, repository);
	}
	
	public void getWiki(){
		
	}

}
