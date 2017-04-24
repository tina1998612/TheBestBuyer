import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by lenovo on 2017/4/22.
 */
public class parknShop {
    String resultString=null;
    public void readParkn(String url,String parentXML, String priceXML, String nameXML){
        WebClient webClient = disableScript();
        HtmlPage page = getHtmlPage(url, webClient);
        grabData(page,parentXML,priceXML,nameXML);
    }
    //priceXML:"./div[@class='name']/a"
    //parent for parknshop:"//div[@class='padding']"
    //name:"./div[@class='price-container']/div[@class='display-price']/div[@class='price discount']"
    private void grabData(HtmlPage page,String parentXML, String priceXML, String nameXML) {
        List<?> fruitList=(List<?>)page.getByXPath(parentXML);
        if(fruitList.isEmpty()){
            System.out.println("no merchandise");
        }else{
            for(Object item:fruitList){
                HtmlDivision  div=(HtmlDivision)item;
                HtmlAnchor a=div.getFirstByXPath(nameXML);
                HtmlDivision priceDiv=div.getFirstByXPath(priceXML);
                if(priceDiv!=null){
                    Item merchadise = getItem(a, priceDiv.asText());
                    resultString+=jsonStringGeneration(merchadise)+"\n";
                }

            }
        }
    }
    private Item getItem(HtmlAnchor a, String price) {
        //TODO: get the item
        Item merchadise=new Item();
        merchadise.setTitle(a.asText());
        merchadise.setPrice(price);
        return merchadise;
    }
    public void writeToFile(String string){
        FileWriter fileWriter=null;
        try {
            fileWriter=new FileWriter("parknShop.csv");
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




    public void readCream(){
        //TODO: disable JAVASCIRPT AND CSS;
        WebClient webClient = disableScript();
        //TODO: get the page
        HtmlPage page = getHtmlPage("http://www.price-cream.com/search?q=%E6%96%B0%E9%AE%AE%E6%B0%B4%E6%9E%9C", webClient);
        //TODO:get the TAGS
        List<?> fruitList=(List<?>)page.getByXPath("//div[@class='col-xs-12 col-sm-9 col-md-9']");
        if(fruitList.isEmpty()){
            System.out.println("no merchandise");
        }else{
            for(Object item:fruitList){
                HtmlDivision  div=(HtmlDivision)item;
                HtmlElement a=div.getFirstByXPath("./h4[@class='list-group-item-heading']");
                HtmlFont priceDiv=div.getFirstByXPath("./p[@class='list-group-item-text']/font[1]");
                String price=priceDiv==null?"N/A":priceDiv.asText();
                //TODO: get the item
                Item merchadise=new Item();
                merchadise.setTitle(a.asText());
                //merchadise.setUrl(a.getHrefAttribute());
                merchadise.setPrice(price);
                //TODO: get it to the JSON string
                jsonStringGeneration(merchadise);
            }
        }
    }
}
