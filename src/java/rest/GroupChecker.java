
package rest;



import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.concurrent.Callable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class GroupChecker implements Callable<JsonObject> {

  private final String siteUrl;

  GroupChecker(String currentUrl) {
    this.siteUrl = currentUrl;
  }

  @Override
  public JsonObject call()  {
     Document doc;
    JsonObject groupJson = new JsonObject();
    groupJson.addProperty("siteUrl", siteUrl);
    try {
      doc = Jsoup.connect(siteUrl).get();
    } catch (IOException ex) {
      groupJson.addProperty("error", "Could not connect to: "+siteUrl);
      return groupJson;
    }
    
    Elements newsHeadlines = doc.select(".group");
    Elements authors = doc.select("#authors");
    Elements classs = doc.select("#class");
    Elements group = doc.select("#group");
   if(authors.size() == 0 && classs.size() == 0 && group.size()==0){
      groupJson.addProperty("error", "NO AUTHOR/CLASS-INFO");
      return groupJson;
    }
    groupJson.addProperty("authors", authors.text());
    groupJson.addProperty("class", classs.text());
    groupJson.addProperty("group", group.text());
    return groupJson;
  }
 
}

