package app;

import javax.sql.DataSource;
import groovy.sql.Sql;

@SuppressWarnings("serial")
public class ActionSupport extends org.metamorphosis.core.ActionSupport {
	
	public Object getConnection()  {
		 return new Sql((DataSource) getDataSource());	
    }
	
	public void sendSupportMail(String object,String content){
		sendMail("ThinkTech Support","support@thinktech.sn",object,content);
	}

}
