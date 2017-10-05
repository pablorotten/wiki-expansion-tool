package pablorotten.wiki_expansion_tool.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;

import pablorotten.Tree.*;
import pablorotten.wiki_expansion_tool.service.ServicesWiki;
/**
 *
 * @author Pablo
 */
public class WikiExpansion {
  private List<String> initialTerms;
  private List<Notion> initialNotions;
  //HashMap where the index is the common category and the TreeNode is a tree where the common category is the root and the initial Notions are the leaves
  private HashMap<Notion, TreeNode<Notion>> expandedNotionTrees;
  private Set<Notion> commonCategoryNotions;
  //An initialNotionTree for each initialTerm
  private List<TreeNode<Notion>> initialNotionTrees;
  
  public WikiExpansion() {
    initialNotions = new ArrayList<Notion>();
    commonCategoryNotions = new HashSet<Notion>();
    initialNotionTrees = new ArrayList<TreeNode<Notion>>();
    expandedNotionTrees = new HashMap<Notion, TreeNode<Notion>>();
  }
  
	/**
   * Search the initial terms in the Wikipedia API and if found, transforms the terms into Notions. Get the notions expanded in trees with
   * Wikipedia Categories and, when common category/ies found, returns a Hashmap of List of Trees with only one branch where the root is 
   * the Initian Notion and the leaf is the common category, each one-branch Tree represent the Expansion of an initial notion to the
   * common category. The Expansions Lists of the HashMap goes with each Common Category found
   * @param initialTerms List of notions to find in Wikipedia
   * @return Expanded notions trees with their Wikipedia categories (if found)
	 * @throws IOException
	 * @throws JSONException
	 */
  public HashMap<Notion, TreeNode<Notion>> expand(List<String> terms) throws IOException, JSONException {
  	//For each initialNotionTree, creates a set with all the categories that are added in order to avoid repeated categories in the initialNotionTree
    HashMap<Notion, HashSet<Notion>> initialNotionTreesCategoryChecker = new HashMap<Notion, HashSet<Notion>>();
    initialTerms = terms;
  	
  	//Transform the initial Terms into initial Notions
  	initialNotions = createNotions(initialTerms);
  	
  	//Transform the initial Notions into TreeNodes
  	for(Notion initialNotion : initialNotions) {
  		initialNotionTrees.add(new TreeNode<Notion>(initialNotion));
  		initialNotionTreesCategoryChecker.put(initialNotion, new HashSet<Notion>());
  	}

  	for(int i=0; i<=5 && commonCategoryNotions.isEmpty(); i++){
  		for(TreeNode <Notion> initialNotionTree : initialNotionTrees){
  			ArrayList <TreeNode<Notion>> notionNodesToExpand = getNotionNodesByLevel(initialNotionTree, i);
  			for (TreeNode <Notion> notionNodeToExpand : notionNodesToExpand) {
  			  ArrayList<Notion> categoryNotionsExpanded = expandCategories(notionNodeToExpand.data);
  				if(!categoryNotionsExpanded.isEmpty()){
  	        for (Notion categoryNotionExpanded : categoryNotionsExpanded ) {
  	          //checks if current categoryNotionExpanded is already in the notionTree to which the notionNodeToExpand (where the category is going to be added) belongs
  	          if(!initialNotionTreesCategoryChecker.get(initialNotionTree.data).contains(categoryNotionExpanded)) {
  	            notionNodeToExpand.addChild(categoryNotionExpanded);
  	            initialNotionTreesCategoryChecker.get(initialNotionTree.data).add(categoryNotionExpanded);
  	          }
  	        }
  	      }    				
  			}
  		}
  		commonCategoryNotions = findCommonAncestors(initialNotionTrees);                 	
  	}
  	
    System.out.println("***************************************** Common Categories *****************************************");
    System.out.println(commonCategoryNotions);
   
    System.out.println("*****************************************************************************************");
    System.out.println("***************************************** Paths *****************************************");
    System.out.println("*****************************************************************************************");
    for(Notion commonCategoryNotion : commonCategoryNotions) {
      TreeNode<Notion> expandedNotionTree = new TreeNode<Notion>(commonCategoryNotion);
      System.out.println("***************************************** CATEGORY: " + commonCategoryNotion.getTitle() + " ***************************************** \n");
      for(TreeNode<Notion> initialNotionTreeExpanded : initialNotionTrees) {
        TreeNode<Notion> notionPath = pathToCategory(initialNotionTreeExpanded, commonCategoryNotion);
//          System.out.println("********** MERGE **********");
//          printTree(expandedNotionTree);
//          System.out.println("********** WITH **********");
//          printTree(notionPath);
        addPathToTree(expandedNotionTree, notionPath);
      }
      printTree(expandedNotionTree);
      expandedNotionTrees.put(commonCategoryNotion, expandedNotionTree); 
    }
    //For each initialNotion in each expandedNotionTree, we expand the last category of each path (the supercategory of each leaf)
    for(Entry<Notion, TreeNode<Notion>> expandedNotionTreeEntry : expandedNotionTrees.entrySet()) {
      TreeNode<Notion> expandedNotionTree = expandedNotionTreeEntry.getValue();
      HashSet<TreeNode<Notion>> categoryNodesToExpand = new HashSet<TreeNode<Notion>>();
      
      for(Notion initialNotion : initialNotions) {
        categoryNodesToExpand.add(expandedNotionTree.findTreeNode(initialNotion).parent);        
      }
      for(TreeNode<Notion> categoryNodeToExpand : categoryNodesToExpand) {
        ArrayList<Notion> newPagesNotions = expandPages(categoryNodeToExpand.data);
        System.out.println("********** NEW PAGES EXPANDED FROM " + categoryNodeToExpand.data.getTitle() + " **********");
        for(Notion newPageNotion : newPagesNotions) {
          System.out.println(newPageNotion.getTitle() + ", ");
        }
      }      
    }    
  	return expandedNotionTrees;
  }
    
    /**
     * Starting from a list of Initial Terms, search those Terms in the Wiki API as Titles, and if Pages found, creates Notions with all of them
     * @param initialTerms Array of String with the initial terms to search
     * @return An array of Notions with the info found in the Wiki API
     * @throws IOException
     */
  public static List<Notion> createNotions(List<String> initialTerms) throws IOException {
    List<Notion> initialNotions = new ArrayList<Notion>();
    
    for(String title : initialTerms) {
      Notion initialNotion = ServicesWiki.createNotion(title);
      if(initialNotion != null)
        initialNotions.add(initialNotion);
    }
    return initialNotions;
  } 
    
    /**
     * Find and returns the categories of a given Notion in the Wiki API
     * @param notion NotionNode which Notion is goun
     * @throws IOException
     */
  public static ArrayList<Notion> expandCategories(Notion notion) throws IOException {
    ArrayList<Notion> categoryNotionsExpanded = new ArrayList<Notion>();
    categoryNotionsExpanded = ServicesWiki.expandNotionCategories(notion);
    return categoryNotionsExpanded;
  }

    /**
     * Expands a given CategoryNotion TreeNode. Search in wikipedia all the pages under that Category, transform them into PageNotion TreeNodes
     * and adds them as childs of the given CategoryNotion Treenode if already isn't
     * @param categoryNotion
     * @throws JSONException
     * @throws IOException
     */
  public static ArrayList<Notion> expandPages(Notion categoryNotion) throws JSONException, IOException{
    ArrayList<Notion> newNotions = new ArrayList<Notion>();
    
    if(categoryNotion.getType() == NotionType.CATEGORY) {
      newNotions = ServicesWiki.getNotionPagesInCategory(categoryNotion);              
//          if(!newNotions.isEmpty()) {
//            List <TreeNode<Notion>> categoryTreenodeNotionChildrens = categoryNotion.children;
//            for(Notion newNotion : newNotions) {
//              if(!categoryNotion.children.contains(newNotion)) {
//               categoryNotion.addChild(newNotion); 
//              }           
//            }
//          }
    }
    else {
      System.err.println(categoryNotion.getTitle() + " Must be a Category Notion");        
    }
    return newNotions;  
  }
    
  /**
   * Merges a path with the Main Tree. They must have the same root (Common category)  
   * @param notionTree Main Tree
   * @param pathTree Path to merge
   */
  public static void addPathToTree(TreeNode<Notion> notionTree, TreeNode<Notion> pathTree) {  
	  //check if both tree and path has the same root
	  if(pathTree.data.equals(notionTree.data)) {
	    //get a pathNode
      for(TreeNode<Notion> pathNode : pathTree) {
        //checks if has children
        if(!pathNode.children.isEmpty()) {
          //get his children, that's the Node we're going to insert in the tree
          TreeNode<Notion> pathInsertNode = pathNode.children.get(0);
          //checks if the children is already in the tree, if not, inserts it
          if(notionTree.findTreeNode(pathInsertNode.data) == null) {
            TreeNode<Notion> notionTreePointer = notionTree.findTreeNode(pathNode.data);
            if(notionTreePointer != null) {
              notionTreePointer.addChild(pathInsertNode.data);
            }
            else {
              System.out.println("Unable to find category");          
            }
          }
        }
      }
	  }
	  else {
	    System.out.println("Different root, unable to add the path");
	  }
  }

	/**
	 * Iterates a list of Trees to find common CategoryNodes
	 * @param notionTrees trees to iterate
	 * @return list of commonCategories found
	 */
  public static Set<Notion> findCommonAncestors(List<TreeNode<Notion>> notionTrees){
    TreeNode<Notion> firstNotionTree = notionTrees.get(0);
    Set<Notion> commonCategories = new HashSet<Notion>();
      
    for(TreeNode<Notion> node : firstNotionTree) {
      Notion notion = node.data;              
      boolean found = true;  
      for(TreeNode <Notion> notionTree : notionTrees){
      	if(notionTree != firstNotionTree) {
        	TreeNode<Notion> nodeFound = notionTree.findTreeNode(notion);
          if(nodeFound == null){
          	found = false;
            break;
          }
      	}
      }
      if(found)
      	commonCategories.add(notion);
    }
    return commonCategories;
  }
    
  /**
   * Given a Notion Tree and a level, returns all the inodes of that level
   * @param notionTree 
   * @param level
   * @return
   * @throws IOException
   */
  public static ArrayList<TreeNode<Notion>> getNotionNodesByLevel(TreeNode <Notion> notionTree, int level) throws IOException {        
    ArrayList <TreeNode<Notion>> notionNodesByLevel = new ArrayList<TreeNode<Notion>>();
    for (TreeNode<Notion> notionNode : notionTree) {
      if (level == notionNode.getLevel()){
      	notionNodesByLevel.add(notionNode);
      }
    }
    return notionNodesByLevel;
  }
 
  /**
   * Returns a Path with the Common Category as Root, the Initial Notion as Leaf, and the inodes are the intermediate
   * categories that connects the Initial Notion with the Root
   * @param tree
   * @param commonCategoryNotion
   * @return
   */
  public TreeNode<Notion> pathToCategory(TreeNode<Notion> expandedNotionTree, Notion commonCategoryNotion) {
    TreeNode<Notion> expandedNotionTreeNode = expandedNotionTree.findTreeNode(commonCategoryNotion);
    TreeNode<Notion> pathTreeNode = new TreeNode<Notion>(expandedNotionTreeNode.data);
    TreeNode<Notion> pathTreeNodeIterator = pathTreeNode;
    
    while (!expandedNotionTreeNode.isRoot()) {
      expandedNotionTreeNode = expandedNotionTreeNode.parent;
      pathTreeNodeIterator.addChild(expandedNotionTreeNode.data);
      pathTreeNodeIterator = pathTreeNodeIterator.children.get(0);
    }        
    return pathTreeNode;
  } 

  /**
   * Prints a tree
   * @param tree
   */
  public static void printTree(TreeNode<Notion> tree) {
    for (TreeNode<Notion> node : tree) {
        String indent = createIndent(node.getLevel());
        System.out.println(indent + node.data.getTitle());
    }
  }
   
  public static String createIndent(int depth) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < depth; i++) {
      sb.append("    ");
    }
    return sb.toString();
  }

  public HashMap<Notion, TreeNode<Notion>> getExpandedNotionTrees() {
    return expandedNotionTrees;
  }

  public List<String> getInitialTerms() {
    return initialTerms;
  }

  public Set<Notion> getCommonCategoryNotions() {
    return commonCategoryNotions;
  }

  public List<Notion> getInitialNotions() {
    return initialNotions;
  }

  public List<TreeNode<Notion>> getInitialNotionTrees() {
    return initialNotionTrees;
  }
}
