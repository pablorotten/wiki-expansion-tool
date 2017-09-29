package pablorotten.wiki_expansion_tool;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONException;

public class App 
{
  public static void main( String[] args ) throws JSONException, IOException
  {
    List<String> initialTerms = new LinkedList<String>();

//    initialTerms.add("Water_(classical_element)");
//  	initialTerms.add("Fire_(classical_element)");
//  	initialTerms.add("Earth_(classical_element)");
//    initialTerms.add("Air_(classical_element)");

    initialTerms.add("Wolf");
    initialTerms.add("Dogggy");
//    initialTerms.add("Huelva");
//    initialTerms.add("Granada");
//    initialTerms.add("Seville");
//    initialTerms.add("Cádiz");
//    initialTerms.add("Dos Hermanas");
//    initialTerms.add("Albolote");
//    initialTerms.add("Alhambra");
    
//    initialTerms.add("Poitou_donkey");
//    initialTerms.add("Gray_wolf");
//    initialTerms.add("Hottentotta_tamulus");
//    initialTerms.add("Atlantic_blue_marlin");
    
//    initialTerms.add("Felipe González");
//    initialTerms.add("Vicente Fox");
//    initialTerms.add("Kim_Jong-il");
//    initialTerms.add("Robert_Mugabe");    	
      
    Controller.expand(initialTerms);
      
//      /*
//       * Test de hashset
//       */
//      Set<String> testHashSet = new HashSet<String>();
//      testHashSet.add("a");
//      testHashSet.add("b");
//      testHashSet.add("c");
//      testHashSet.add("a");
//      
//      System.out.println(testHashSet);

//        Set<Notion> notionsSet = new HashSet<Notion>();
//
//        notionsSet.add(new Notion("test 1", NotionType.CATEGORY, 1));
//        notionsSet.add(new Notion("test 2", NotionType.CATEGORY, 2));
//        notionsSet.add(new Notion("test 3", NotionType.CATEGORY, 3));
//        notionsSet.add(new Notion("test 1", NotionType.CATEGORY, 1));
//        
//        System.out.println(notionsSet);

//      /*
//       * Test para ver si haciendo new a partir de una lista copia el elemento de la lista o lo referencia
//       * El resultado es que lo referencia. Dentro del bucle si lo modificamos si que cambia pero luego ya no
//       */
//    	Set<Notion> categoryList = new HashSet<Notion>();
//      ArrayList<TreeNode<Notion>> notionList = new ArrayList<TreeNode<Notion>>();
//      
//      categoryList.add(new Notion("test1", NotionType.CATEGORY, 1));
//      categoryList.add(new Notion("test2", NotionType.CATEGORY, 2));
//      categoryList.add(new Notion("test3", NotionType.CATEGORY, 3));
//      
//      for(Notion commonCategoryNotion : categoryList) {
//        notionList.add(new TreeNode<Notion>(commonCategoryNotion));
//        commonCategoryNotion.setPageid(commonCategoryNotion.getPageid()+1);
//      }
//      
//      for(TreeNode<Notion> notion : notionList) {
//        System.out.println(notion);;
//      }
    }
    public static String getHelloWorld() {  
      return "Hello World";  
    }
  
    public static String getHelloWorld2() {  
      return "Hello World 2";  
    }
}
