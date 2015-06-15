package SSH;

import java.util.LinkedList;
import java.util.List;

public abstract class CustomTask
{
  protected static String DELIMETER = ";";
  protected List err_sysout_keyword_list;
  protected String[] err_sysout_keywords;
  
  public CustomTask()
  {
    this.err_sysout_keyword_list = new LinkedList();
    this.err_sysout_keywords = new String[] {"Usage", "usage", "not found", "fail", "Fail", "error", "Error", "exception", "Exception", "not a valid" };
    resetErrSysoutKeyword(this.err_sysout_keywords);
  }
  
  public void resetErrSysoutKeyword(String[] str)
  {
    this.err_sysout_keyword_list.clear();
    for (int i = 0; i < str.length; i++) {
      this.err_sysout_keyword_list.add(str[i]);
    }
  }
  
  public Boolean isSuccess(String stdout, int exitCode)
  {
    if ((checkStdOut(stdout).booleanValue()) && (checkExitCode(exitCode).booleanValue())) {
      return Boolean.valueOf(true);
    }
    return Boolean.valueOf(false);
  }
  
  protected abstract Boolean checkStdOut(String paramString);
  
  protected abstract Boolean checkExitCode(int paramInt);
  
  public abstract String getCommand();
  
  public abstract String getInfo();
  
  protected String cat(String... args)
  {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < args.length; i++)
    {
      sb.append(args[i]);
      sb.append(" ");
    }
    return sb.toString();
  }
}
