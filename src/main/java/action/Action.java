package action;

public abstract class Action {
	
	protected String login;
	protected String password;
	protected String repository;
	
	public Action(String login, String password, String repository) {
		this.login = login;
		this.password = password;
		this.repository = repository;
	}
	
	public void run(){}

}
