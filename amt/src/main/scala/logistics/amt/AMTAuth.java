package logistics.amt;

public class AMTAuth {
  public final String login;
  public final String passwd;

  public AMTAuth(String login, String passwd) {
    this.login = login;
    this.passwd = passwd;
  }

  public static AMTAuth fromEnv() {
    return new AMTAuth(System.getenv("AMT_LOGIN"), System.getenv("AMT_PASSWD"));
  }

  public String[] credentials() {
    return new String[] { login, passwd };
  }
}
