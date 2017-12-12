package app;

import java.io.InputStream;
import org.apache.commons.fileupload.FileItemStream;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

public class FileManager {
	
    private static final String ACCESS_TOKEN = "pQXsY7k7n6AAAAAAAAAACCeARGCUI6ZaOF4vho8GFb39O6lv-8wWo2LqYRm1ShIY";
    private DbxClientV2 client;
    
    public FileManager(){
    	DbxRequestConfig config = new DbxRequestConfig("dropbox/thinktech-portal");
        client = new DbxClientV2(config, ACCESS_TOKEN);
    }
    
    public void upload(FileItemStream item) throws Exception {
    	try(InputStream in = item.openStream()) {
    		client.files().uploadBuilder("/sesame/"+item.getName()).uploadAndFinish(in);
    	}
    }
}