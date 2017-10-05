package pablorotten.wiki_expansion_tool.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pablorotten.wiki_expansion_tool.model.Notion;
import pablorotten.wiki_expansion_tool.model.NotionType;

public class ServicesWiki {
    
  private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
    InputStream is = new URL(url).openStream();
    try {
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
      String jsonText = readAll(rd);
      JSONObject json = new JSONObject(jsonText);
      return json;
    } finally {
      is.close();
    }
  }

  /**
   * Search a term in Wikipedia as a title and if found, creates a Notion with the retrieved info:title, pageid and redirects (titles
   * that also redirects to the main title)
   * @param title Title to search
   * @return Notion with the info found in Wikipedia
   * @throws IOException
   */
  public static Notion createNotion(String title) throws IOException{
    Notion notion = null;
    title = title.replaceAll(" ", "_");
    String url = "https://en.wikipedia.org/w/api.php?action=query&prop=info%7Credirects&format=json&titles=" + title + "&redirects";
    System.out.println(url);
    
    try{
      JSONObject json = readJsonFromUrl(url);
      Integer pageid = Integer.parseInt(json.getJSONObject("query").getJSONObject("pages").names().get(0).toString());
      if(pageid != -1) {
        title = (String) json.getJSONObject("query").getJSONObject("pages").getJSONObject(pageid.toString()).get("title");
        JSONArray redirects = (JSONArray)json.getJSONObject("query").getJSONObject("pages").getJSONObject(pageid.toString()).get("redirects");
        ArrayList<String> redirectTitles = new ArrayList<String>();
        
        for(int i=0; i<redirects.length(); i++){   
          String redirectTitle = (String) ((JSONObject)redirects.get(i)).get("title");
          redirectTitles.add(redirectTitle);            
        }     
          
        notion = new Notion(title, NotionType.PAGE, pageid, redirectTitles);
      }
    }catch(org.json.JSONException exception){
      System.err.println("Json Error with" + title +": "+exception.getMessage());
    }  
    return notion;
  }

 /**
   * Returns categories of a given notion 
   * @param notion notion which title is used to find categories   
   * @return A list of Notions with the founded categories
   * @throws IOException
   */
  public static ArrayList<Notion> expandNotionCategories(Notion notion) throws IOException{
    ArrayList<Notion> categoryNotions = new ArrayList<Notion>();
    
    String url = "https://en.wikipedia.org/w/api.php?action=query&generator=categories&prop=info&format=json&gcllimit=max&gclshow=!hidden&pageids="+notion.getPageid();
//    System.out.println(url);
    
    try{
      JSONObject json = readJsonFromUrl(url);
//            JSONArray categoryPages = json.getJSONObject("query").getJSONObject("pages").names();
      JSONObject categoryPages = json.getJSONObject("query").getJSONObject("pages");
      Iterator<String> keys = categoryPages.keys();
      while(keys.hasNext()) {
        String key = (String)keys.next();
        if(categoryPages.get(key) instanceof JSONObject){
          JSONObject categoryPage = (JSONObject) categoryPages.get(key);
//          System.out.println(categoryPage);
          categoryNotions.add(new Notion(((String) categoryPage.get("title")).replaceFirst("([^:]*:){1}", ""), NotionType.CATEGORY,(Integer) categoryPage.get("pageid")));
        }
      }         
    }catch(org.json.JSONException exception){
      System.err.println("Json Error with " + notion.getTitle() + ": "+exception.getMessage());
    }  
    return categoryNotions;
  }

  /**
   * Returns a list of Page Notions under a given Category in the Wiki
   * @param categoryNotion the Category Notion to get the Pages are under
   * @return List of PageNotions that are under the given Category Notion in the Wiki
   * @throws JSONException
   * @throws IOException
   */
  public static ArrayList <Notion> getNotionPagesInCategory(Notion categoryNotion) throws JSONException, IOException{
    ArrayList<Notion> pageNotions = new ArrayList<Notion>();
    String url = "https://en.wikipedia.org/w/api.php?action=query&list=categorymembers&prop=categories&cmlimit=max&format=json&cmpageid=" + categoryNotion.getPageid();
      try{
        JSONObject json = readJsonFromUrl(url);
        JSONArray pagesArray = (JSONArray)json.getJSONObject("query").getJSONArray("categorymembers");
        for(int i=0; i<pagesArray.length(); i++){
          String title = (String) ((JSONObject)pagesArray.get(i)).get("title");
          Integer pageid = (Integer) ((JSONObject)pagesArray.get(i)).get("pageid");
          if(!title.contains("Category:"))
            pageNotions.add(new Notion(title, NotionType.PAGE, pageid));
        }   
      }catch(org.json.JSONException exception){
        System.err.println("Json Error: "+exception.getMessage());
      }  
      return pageNotions;   
  }

}