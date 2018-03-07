package pablorotten.wiki_expansion_tool.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import pablorotten.Tree.TreeNode;
import pablorotten.wiki_expansion_tool.model.Notion;
import pablorotten.wiki_expansion_tool.model.WikiExpansion;
import pablorotten.wiki_expansion_tool.rest_template_domain.Quote;
 
@Controller
public class WikiExpansionController {
  
  @RequestMapping("/welcome")
  public ModelAndView helloWorld() {
 
    String message = "<br><div style='text-align:center;'><h3>WIKIPEDIA EXPANSION TOOL <3</h3></div><br><br>";
    return new ModelAndView("welcome", "message", message);
  } 

  @RequestMapping("/test1")
  public ModelAndView test() {
    RestTemplate restTemplate = new RestTemplate();
    Quote quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
    System.out.println(quote);
    return new ModelAndView("test1", "quote", quote);
  } 
  
  @RequestMapping("/test-rest-template")
  public ModelAndView testRestTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    Quote quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
    System.out.println(quote);
    return new ModelAndView("test-rest-template", "quote", quote);
  } 
  
  @RequestMapping("/test2")
  public ModelAndView tests(@RequestParam String notion1, @RequestParam String notion2) {
    String notions = notion1 + " " + notion2;
    return new ModelAndView("test2", "notions", notions);
  }

  @RequestMapping("/expand")
  public ModelAndView expand(
      @RequestParam String notion1, 
      @RequestParam String notion2, 
      @RequestParam String notion3,
      @RequestParam String notion4, 
      @RequestParam String notion5
      ) throws JSONException, IOException {
    String expansionResult = "";
    WikiExpansion expansion = new WikiExpansion();
    HashMap<Notion, TreeNode<Notion>> expandedNotionTrees = new HashMap<>();
    
    List<String> initialTerms = new LinkedList<String>();
//  initialTerms.add("Poitou_donkey");
//  initialTerms.add("Gray_wolf");
//  initialTerms.add("Hottentotta_tamulus");
//  initialTerms.add("Atlantic_blue_marlin");

    initialTerms.add(notion1);
    initialTerms.add(notion2);
    initialTerms.add(notion3);
    initialTerms.add(notion4);
    initialTerms.add(notion5);

    if(initialTerms.size()<=1)
      return new ModelAndView("expand", "expansionResult", "You must insert at least 2 notions");      

    expandedNotionTrees = expansion.expand(initialTerms);
    
    for(Entry<Notion, TreeNode<Notion>> expandedNotionTreeEntry : expandedNotionTrees.entrySet()) {
      TreeNode<Notion> expandedNotionTree = expandedNotionTreeEntry.getValue();
      Notion commonCategory = expandedNotionTreeEntry.getKey();
      expansionResult += "******************* COMMON CATEGORY: " + commonCategory.getTitle() + " *******************<br>";
      
      for (TreeNode<Notion> node : expandedNotionTree) {
        String indent = createIndent(node.getLevel());
        expansionResult += indent;
        if(node.isLeaf())
            expansionResult += "<b>" + node.data.getTitle() + "</b><br>";
        else
            expansionResult += node.data.getTitle() + "<br>";      
      }
    }  
    
    return new ModelAndView("expand", "expansionResult", expansionResult);
  }
  
  public static String createIndent(int depth) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < depth; i++) {
      sb.append("&nbsp;&nbsp;&nbsp;&nbsp;");
    }
    return sb.toString();
  }

}