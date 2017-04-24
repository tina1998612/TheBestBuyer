import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lenovo on 2017/4/22.
 */
public class snapCheck {
    public void scrapingWeb() {

        //TODO: unable the CSS and JAVASCRIPT to be faster
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        //TODO: get the HTML page
        String baseUrl1="https://newyork.craigslist.org/search/sss?sort=rel&query=";
        String searchQuery = "Iphone 6s";
        HtmlPage page = null;
        try {
            String searchUrl =  baseUrl1+ URLEncoder.encode(searchQuery, "UTF-8");
            page = client.getPage(searchUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //TODO: get the Tags we need for price
        List<?> items = (List<?>) page.getByXPath("//p[@class='result-info']");
        if (items.isEmpty()) {
            System.out.println("No items found !");
        } else {
            for (Object itemObject : items) {
                HtmlElement item = (HtmlElement) itemObject;
                //TODO: get the information of the label
                HtmlAnchor itemAnchor = ((HtmlAnchor) item.getFirstByXPath(".//a"));
                HtmlElement spanPrice = ((HtmlElement) item.getFirstByXPath(".//span[@class='result-price']"));
                String itemPrice = spanPrice == null ? "0" : spanPrice.toString();
                //String itemName = itemAnchor.asText();
                //String itemUrl = itemAnchor.getHrefAttribute();

                // It is possible that an item doesn't have any price
                //String itemPrice = spanPrice == null ? "no price" : spanPrice.asText();

                //TODO: define an item
                Item merchandise = new Item();
                merchandise.setTitle(itemAnchor.asText());
                //merchandise.setUrl(baseUrl1 + itemAnchor.getHrefAttribute());
                merchandise.setPrice(itemPrice);
                //TODO: change an item to JSON
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonString = null;
                try {
                    jsonString = objectMapper.writeValueAsString(merchandise);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                System.out.println(jsonString);
            }
        }


    }
    public static void main(String[] args) throws IOException {
        readingURLConnection();
        //System.out.println("https://www.zifangsky.cn/".matches("(https?://)?(\\w+)?\\..*"));

    }

    private static void javaRegularExpression() {
        System.out.println("a".matches("a?"));
        System.out.println("".matches("a?"));
        System.out.println( "192.168.0.aaa".matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}.*"));
        System.out.println("C".matches("[A-Z&&[ABS]]"));//false
        System.out.println("C".matches("[A-Z&[ABS]]"));//true
        System.out.println("A".matches("[A-Z&&[ABS]]"));//true
        System.out.println("A".matches("[A-Z&[ABS]]"));//true
        System.out.println("C".matches("[A-Z|[ABS]]"));//true
        System.out.println("C".matches("[A-Z||[ABS]]"));//true
        System.out.println(" \n\t\r".matches("\\s{4}"));//true
        System.out.println(" ".matches("\\s"));//true
        System.out.println(" ".matches("\\S"));//false
        System.out.println("abc888&^%".matches("[a-z]{1,3}\\d+[0-9|!~$&*^#%()]+"));//false
        System.out.println("helloworld".matches("^h[a-z]{1,3}o\\b.*"));//false
        System.out.println("www.zifangsky.cn".matches("[^/bd\\s]*"));//true,
        System.out.println("www.zifangsky.cn".matches(".*?"));//true,
        System.out.println("ab54564654sbg48746bshj".replaceAll("[^0-9]|//d", "-"));

        Pattern p = Pattern.compile("(\\d{3,5})([a-z]{2})");
        String s = "123aaa-7878bb-646dd00";
        Matcher m = p.matcher(s);
        while(m.find()) {
            System.out.println(m.group());
            System.out.println(m.group(1));//输出每对符合的 数字
            System.out.println(m.group(2));//输出每对符合的 字母
        }
    }

    private static String readingURLConnection() throws IOException {
        URL url=new URL("https://www.oracle.com");
        HttpURLConnection conn= (HttpURLConnection) url.openConnection();
        System.out.println("response code: "+conn.getResponseCode());
        BufferedReader bf=new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder line=new StringBuilder();
        String current;
        while((current=bf.readLine())!=null){
            line.append(current);
            System.out.println(current);
        }
        bf.close();
        return line.toString();
    }

    private static void readingDirectlyFromURL() throws IOException {
        URL url=new URL("https://docs.oracle.com/javase/tutorial/networking/urls/readingWriting.html");
        BufferedReader bf=new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        while((line=bf.readLine())!=null){
            System.out.println(line);
        }
        bf.close();
    }

    private static void readingFromURL() {
        try
        {
            URL url = new URL("http://www.google.com");
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection connection = null;
            if(urlConnection instanceof HttpURLConnection)
            {
                connection = (HttpURLConnection) urlConnection;
            }
            else
            {
                System.out.println("请输入 URL 地址");
                return;
            }
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String urlString = "";
            String current;
            while((current = in.readLine()) != null)
            {
                urlString += current+'\n';
            }
            System.out.println(urlString);
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void printWelcome() {
        Welcome p=new Welcome();
        String firstURL="http://www.price-cream.com/category";
        ArrayList<String> initialPage=p.generateURL(firstURL);
        String parentDiv="//div[@class='col-xs-12 col-sm-9 col-md-9']";
        String nameXML="./h4[@class='list-group-item-heading']";
        String priceXML="./p[@class='list-group-item-text']/font[1]";
        for(String string:initialPage){
            p.readWelcome(string,parentDiv,priceXML,nameXML);
        }
        p.writeToFile(p.resultString);
    }

    public static void printParkn() {
        parknShop p=new parknShop();
        ArrayList<String> initialPage=new ArrayList<>(Arrays.asList("http://www.parknshop.com/%E6%96%B0%E9%AE%AE%E9%A3%9F%E5%93%81/lc/070000",
                "http://www.parknshop.com/%E6%97%A9%E9%A4%90%E5%8F%8A%E9%BA%B5%E5%8C%85%E7%B3%95%E9%BB%9E/lc/010000",
                "http://www.parknshop.com/%E7%B3%A7%E6%B2%B9%E9%9B%9C%E8%B2%A8/lc/020000",
                "http://www.parknshop.com/%E9%A4%85%E4%B9%BE%E3%80%81%E9%9B%B6%E9%A3%9F%E5%8F%8A%E7%B3%96%E6%9E%9C/lc/030000",
                "http://www.parknshop.com/%E9%A3%B2%E5%93%81%E5%8F%8A%E9%85%92%E9%A1%9E/lc/040000",
                "http://www.parknshop.com/%E4%B9%B3%E8%A3%BD%E5%93%81%E3%80%81%E5%86%B7%E5%87%8D%E9%A3%9F%E5%93%81%E5%8F%8A%E9%9B%9E%E8%9B%8B/lc/050000",
                "http://www.parknshop.com/%E6%80%A5%E5%87%8D%E9%A3%9F%E5%93%81/lc/060000",
                "http://www.parknshop.com/%E5%AC%B0%E5%85%92%E8%AD%B7%E7%90%86/lc/080000",
                "http://www.parknshop.com/%E4%BF%9D%E5%81%A5%E5%8F%8A%E5%80%8B%E4%BA%BA%E8%AD%B7%E7%90%86/lc/090000",
                "http://www.parknshop.com/%E7%B4%99%E5%B7%BE%E3%80%81%E6%B4%97%E8%A1%A3%E5%8F%8A%E5%AE%B6%E5%B1%85%E6%B8%85%E6%BD%94%E7%94%A8%E5%93%81/lc/100000",
                "http://www.parknshop.com/%E5%AF%B5%E7%89%A9%E8%AD%B7%E7%90%86/lc/110000",
                "http://www.parknshop.com/%E5%AE%B6%E9%9B%BB%E5%8F%8A%E6%95%B8%E7%A2%BC%E7%94%A2%E5%93%81/lc/120000",
                "http://www.parknshop.com/%E5%AF%A2%E5%AE%A4%E5%8F%8A%E6%B5%B4%E5%AE%A4%E7%94%A8%E5%93%81/lc/130000",
                "http://www.parknshop.com/%E6%9C%8D%E8%A3%9D%E3%80%81%E9%9E%8B%E9%A1%9E%E5%8F%8A%E9%85%8D%E4%BB%B6/lc/150000",
                "http://www.parknshop.com/%E5%82%A2%E4%BF%B1%E3%80%81%E7%87%88%E9%A3%BE%E5%8F%8A%E5%AE%B6%E5%B1%85%E8%A3%9D%E9%A3%BE/lc/160000",
                "http://www.parknshop.com/%E5%BB%9A%E6%88%BF%E5%8F%8A%E9%A3%AF%E5%BB%B3%E7%94%A8%E5%93%81/lc/170000",
                "http://www.parknshop.com/%E9%AB%94%E8%82%B2%E3%80%81%E6%88%B7%E5%A4%96%E5%8F%8A%E6%97%85%E9%81%8A%E7%94%A8%E5%93%81/lc/180000",
                "http://www.parknshop.com/%E9%9B%BB%E6%B1%A0%E3%80%81%E6%8F%92%E5%BA%A7%E5%8F%8A%E5%84%B2%E7%89%A9%E7%94%A8%E5%93%81/lc/190000",
                "http://www.parknshop.com/%E7%8E%A9%E5%85%B7%E5%8F%8A%E6%96%87%E5%85%B7/lc/200000"
                ));
        String parentDiv="//div[@class='padding']";
        String nameXML="./div[@class='name']/a";
        String priceXML="./div[@class='price-container']/div[@class='display-price']/div[@class='price discount']";
        for(String string:initialPage){
            p.readParkn(string,parentDiv,priceXML,nameXML);
        }

        p.writeToFile(p.resultString);
    }

    private void Swing(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame frame=new MainFrame();
                /*JFrame frame=new JFrame("Snap Check");
                frame.setSize(600,500);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                */
            }
        });
    }
    private void testingBuffer() throws IOException {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        String line;
        while((line=br.readLine())!=null){
            System.out.println(line);
        }
    }
}
