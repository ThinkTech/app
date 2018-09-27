package app;

import java.util.Map;
import javax.sql.DataSource;
import groovy.lang.Writable;
import groovy.sql.Sql;
import groovy.text.markup.MarkupTemplateEngine;

@SuppressWarnings("serial")
public class ActionSupport extends org.metamorphosis.core.ActionSupport {
	
	public Object getConnection()  {
		 return new Sql((DataSource) getDataSource());	
    }
	
	public void sendSupportMail(String object,String content){
		sendMail("ThinkTech Support","support@thinktech.sn",object,content);
	}
	
	@SuppressWarnings("rawtypes")
	public String parseTemplate(String template, Map map) throws Exception {
		MarkupTemplateEngine engine = new MarkupTemplateEngine();
		Writable writable = engine.createTemplate(readFile("templates/"+template)).make(map);
		return writable.toString();
	}

}
