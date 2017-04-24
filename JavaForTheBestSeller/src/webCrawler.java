import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lenovo on 2017/4/24.
 */
public class webCrawler {
    String baseURL;
    public webCrawler(String url){
        Pattern pattern=Pattern.compile("(https?://)?((\\w)+\\.)+.*");
        Matcher matcher=pattern.matcher(url);
        if(matcher.find()){
            this.baseURL=matcher.group();
        }else{
            System.out.println("invalid URL");
        }
    }
    public void Crawl(){
        HashMap<String,Boolean> oldMap=new HashMap<String, Boolean>();
        if(baseURL==null){
            return;
        }else{
            oldMap.put(baseURL,false);
            oldMap=this.crawlLink(oldMap);
            for(Map.Entry<String,Boolean> entry:oldMap.entrySet()){
                System.out.println(entry.getKey());
            }
        }
    }
    public HashMap<String,Boolean> crawlLink(HashMap<String,Boolean> oldMap){
        Pattern pattern=Pattern.compile("<a\\s.*(href=)");
        Matcher matcher=null;
        String oldLinkHost;
        for(Map.Entry<String,Boolean> entry:oldMap.entrySet()){
            //TODO: step through all of the false links;
            if(!entry.getValue()){
                System.out.println("link: "+entry.getKey());
                oldLinkHost=entry.getKey();

                try {
                    URL url=new URL(oldLinkHost);
                    HttpURLConnection conn=(HttpURLConnection) url.openConnection();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
