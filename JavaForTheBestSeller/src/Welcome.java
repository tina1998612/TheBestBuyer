import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/4/23.
 */
public class Welcome {
    private final String header = "http://www.price-cream.com/";
    String resultString=null;
    ArrayList<String> DEMO=null;

    public void readWelcome(String url,String parentXML, String priceXML, String nameXML){
        WebClient webClient = disableScript();
        HtmlPage page = getHtmlPage(url, webClient);
        grabData(page,parentXML,priceXML,nameXML);
    }
    private void grabData(HtmlPage page,String parentXML, String priceXML, String nameXML) {
        List<?> fruitList=(List<?>)page.getByXPath(parentXML);
        if(!fruitList.isEmpty()){
            for(Object item:fruitList){
                HtmlDivision div=(HtmlDivision)item;
                HtmlElement nameDiv=div.getFirstByXPath(nameXML);
                HtmlFont priceDiv=div.getFirstByXPath(priceXML);
                if(priceDiv!=null){
                    Item merchadise = getItem(nameDiv, priceDiv.asText());
                    resultString+=jsonStringGeneration(merchadise)+"\n";
                }
            }
        }
    }
    private Item getItem(HtmlElement nameDiv, String price) {
        //TODO: get the item
        Item merchadise=new Item();
        merchadise.setTitle(nameDiv.asText());
        merchadise.setPrice(price);
        return merchadise;
    }
    public void writeToFile(String string){
        FileWriter fileWriter=null;
        try {
            fileWriter=new FileWriter("welcome.csv");
            fileWriter.append(string);
            System.out.println("success");
        } catch (IOException e) {
            System.out.println("error in CSV file");
            e.printStackTrace();
        }finally {
            try{
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String jsonStringGeneration(Item merchadise) {
        //TODO: get it to the JSON string
        ObjectMapper mapper=new ObjectMapper();
        String jsonString=null;
        try {
            jsonString=mapper.writeValueAsString(merchadise);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(jsonString);
        return jsonString;
    }
    private HtmlPage getHtmlPage(String url, WebClient webClient) {
        //TODO: get the page
        String baseURL=url;
        HtmlPage page=null;
        try{
            page=webClient.getPage(baseURL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return page;
    }
    private WebClient disableScript() {
        //TODO: disable JAVASCIRPT AND CSS;
        WebClient webClient=new WebClient();
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        return webClient;
    }

    public ArrayList<String> generateURL(String URL) {
        ArrayList<String> list = new ArrayList<String>();
        WebClient webClient=disableScript();
        HtmlPage mainPage=null;
        try {
            mainPage=webClient.getPage(URL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<?> Div=(List<?>)mainPage.getByXPath("//div[@class='list-group']");
        if(Div.isEmpty()){
            System.out.println("no merchandises!");
        }else{
            for(Object div:Div){
                HtmlDivision divE=(HtmlDivision) div;
                List<?> anchorsList= divE.getByXPath("./a");
                for(Object anchor:anchorsList){
                    list.add(header+((HtmlAnchor)anchor).getHrefAttribute());
                }
            }
        }
        return list;
    }
}
