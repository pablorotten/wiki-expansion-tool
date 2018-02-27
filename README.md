# wiki-expansion-tool

This is a Tool to find common categories between different Terms, and expand in order to obtain new related terms.

## Technologies

Java + JSP → Main languages used

Spring → Framework to implement the MVC pattern

Maven → For dependencies

Tomcat → For deployment

Heroku → Online deployment

Wikimedia RESTfull API → Used for get all the info we need

# BACKEND

## Wikipedia API

A small tutorial with all we need to know about the Wikipedia API

### Manuals

[https://www.mediawiki.org/wiki/API:Main_page](https://www.mediawiki.org/wiki/API:Main_page)

* Queries ([https://www.mediawiki.org/wiki/API:Query](https://www.mediawiki.org/wiki/API:Query)) in order to get information about the page

* Categories ([https://www.mediawiki.org/wiki/API:Categories](https://www.mediawiki.org/wiki/API:Categories))  to list all the categories of a given page

* Generators([https://www.mediawiki.org/wiki/API:Query#Generators](https://www.mediawiki.org/wiki/API:Query#Generators)) to make 2 searches in one. We use as generator de Category search and over that, we make a second search to find the ids

* Redirects ([https://www.mediawiki.org/w/api.php?action=help&modules=query%2Bredirect](https://www.mediawiki.org/w/api.php?action=help&modules=query%2Bredirects)[s](https://www.mediawiki.org/w/api.php?action=help&modules=query%2Bredirects))

### Examples

Some query examples, in **yellow** the ones we use

**Go to page by id**

[http://en.wikipedia.org/?curid=18630637](http://en.wikipedia.org/?curid=18630637)

Don’t redirect

[https://en.wikipedia.org/w/index.php?title=Hitler&redirect=no](https://en.wikipedia.org/w/index.php?title=Hitler&redirect=no)

List categories of Albert Einstein

[https://en.wikipedia.org/w/api.php?action=query&prop=categories&titles=Albert%20Einstein](https://en.wikipedia.org/w/api.php?action=query&prop=categories&titles=Albert%20Einstein)

List categories of Albert Einstein in Json

[https://en.wikipedia.org/w/api.php?action=query&prop=categories&format=json&titles=Albert%20Einstein](https://en.wikipedia.org/w/api.php?action=query&prop=categories&format=json&titles=Albert%20Einstein)

List categories of Dog

[https://en.wikipedia.org/w/api.php?action=query&prop=categories&format=json&titles=Dog](https://en.wikipedia.org/w/api.php?action=query&prop=categories&format=json&titles=Dog)

List categories of Dog with max limit

[https://en.wikipedia.org/w/api.php?action=query&prop=categories&format=json&cllimit=max&titles=Dog](https://en.wikipedia.org/w/api.php?action=query&prop=categories&format=json&cllimit=max&titles=Dog)

**List categories of Dog with max limit and don’t list hidden categories**

[https://en.wikipedia.org/w/api.php?action=query&prop=categories&format=json&cllimit=max&clshow=!hidden&titles=Dog](https://en.wikipedia.org/w/api.php?action=query&prop=categories&format=json&cllimit=max&clshow=!hidden&titles=Dog)

List categories of two different titles, Dog and Spain. Each title is separated by %7C (|

[https://en.wikipedia.org/w/api.php?action=query&generator=categories&prop=info&format=json&gcllimit=max&gclshow=!hidden&titles=Dog%7CSpain](https://en.wikipedia.org/w/api.php?action=query&generator=categories&prop=info&format=json&gcllimit=max&gclshow=!hidden&titles=Dog%7CSpain)

**Get page info Query by title**

[https://en.wikipedia.org/w/api.php?action=query&prop=info&format=json&titles=Dog](https://en.wikipedia.org/w/api.php?action=query&prop=info&format=json&titles=Dog)

**Get page info Query by title and redirects in search. That means that if the given title is not exactly the canonical title of a page, but is contained in his redirections, it will retrieve the page with the canonical title**

[https://en.wikipedia.org/w/api.php?action=query&prop=info%7Credirects&format=json&titles=Catherine%20z-Jones&redirects](https://en.wikipedia.org/w/api.php?action=query&prop=info%7Credirects&format=json&titles=Catherine%20z-Jones&redirects)

In that query, we want the [page of the actress](https://en.wikipedia.org/wiki/Catherine_Zeta-Jones) **Catherine Zeta-Jones**. We have searched with the title "Catherine z-Jones", but the Wikipedia Main Page of the actress is under the title “Catherine Zeta-Jones” so we have a mismatch. If we look at the page’s ids, we realize that are different: “Catherine z-Jones” has the [id:16211435](https://en.wikipedia.org/?curid=16211435&redirect=no) and “Catherine Zeta-Jones” has [id:150996](https://en.wikipedia.org/?curid=150996&redirect=no). If we don’t use &redirect property will get [that useless page](https://en.wikipedia.org/w/api.php?action=query&prop=info%7Credirects&format=json&titles=Catherine%20z-Jones).

But if the title we’re looking for ("Catherine z-Jones") is one of the [redirects](https://en.wikipedia.org/w/api.php?action=query&prop=info%7Credirects&format=json&titles=Catherine%20Zeta%20Jones&redirects) of the Main Page (“Catherine Zeta-Jones”) we can use the &redirect property to fix it. So if we Get page info Query with the main title (Catherine Zeta-Jones) or a title which is included as redirection (Catherine z-Jones, Zeta jones, Zeta-Jones...) with the **&redirects** property, the API will always redirect us to the [Main Page](https://en.wikipedia.org/?curid=150996).

**Get page info Query by pageid**

[https://en.wikipedia.org/w/api.php?action=query&prop=info&format=json&pageids=4269567](https://en.wikipedia.org/w/api.php?action=query&prop=info&format=json&pageids=4269567) 

**List page’s redirects (titles that redirects to the given title page)**

[https://en.wikipedia.org/w/api.php?action=query&prop=redirects&format=json&titles=Dog](https://en.wikipedia.org/w/api.php?action=query&prop=redirects&format=json&titles=Dog)

**List page’s categories by pageid**

[https://en.wikipedia.org/w/api.php?action=query&prop=categories&format=json&clshow=!hidden&pageids=6316](https://en.wikipedia.org/w/api.php?action=query&prop=categories&format=json&clshow=!hidden&pageids=6316) 

**List ****categories by page title: categories of Spain with max limit and !hidden categories**

[https://en.wikipedia.org/w/api.php?action=query&prop=categories&format=json&cllimit=max&clshow=!hidden&titles=Spain](https://en.wikipedia.org/w/api.php?action=query&prop=categories&format=json&cllimit=max&clshow=!hidden&titles=Spain) 

**List pages under a given category title**

[https://en.wikipedia.org/w/api.php?action=query&list=categorymembers&prop=categories&cmlimit=max&format=json&cmtitle=Category:1924_births](https://en.wikipedia.org/w/api.php?action=query&list=categorymembers&prop=categories&cmlimit=max&format=json&cmtitle=Category:1924_births)

**Get page info Query and his redirects by ****title**** and redirections in search**

[https://en.wikipedia.org/w/api.php?action=query&prop=info%7Credirects&format=json&titles=Dog&redirects](https://en.wikipedia.org/w/api.php?action=query&prop=info%7Credirects&format=json&titles=Dog&redirects)  

**List categories and info by ****pageid****: limit and !hidden (note that it’s almost the same, but we need a "g" before each of these properties)**

[https://en.wikipedia.org/w/api.php?action=query&generator=categories&prop=info&format=json&gcllimit=max&gclshow=!hidden&pageids=4269567](https://en.wikipedia.org/w/api.php?action=query&generator=categories&prop=info&format=json&gcllimit=max&gclshow=!hidden&pageids=4269567)

**List Pages and Categories under a given category ****pageid**

[https://en.wikipedia.org/w/api.php?action=query&list=categorymembers&prop=categories&cmlimit=max&format=json&cmpageid=706779](https://en.wikipedia.org/w/api.php?action=query&list=categorymembers&prop=categories&cmlimit=max&format=json&cmpageid=706779) 

## Algorithm

### Phase one

1. Find terms in **Wikipedia API** and check if exists. If exists transform them in **Notions **and tag them under *"Pages"*. That’s we call** Initial Notions**. To accomplish this, we use the **”Get page info Query”**

2. Expand the **Notions **in their **Categories **1 level, the new categories are also **Notions **but despite the I**nitial Notiones**, they’re tagged under *"Categories"*. To accomplish this, we use the **”Category Generator Query****”**

    1. Check if there’s any **Common Category** in all the levels expanded of the different **Inital Notions**

        1. IF there’s one **Common Category**, find others an save all of them. Then go to 3

        2. IF there isn’t any **Common Category**, Repeat 2 until level X. If level X is reached, stop and return "Common categories not found"

3. Create an array of trees for each **Common Category**. In each tree, the root is te **Common Category**, the leafs are the** Initial Notions **and the nodes are the **Category Notions **between the** Initial Notions **and the **Common Category**

4. Return de array of trees

### Problems

#### Wikipedia subcategories → DONE

According with this answer [https://stackoverflow.com/a/5785034](https://stackoverflow.com/a/5785034) Wikipedia's categorization system is not a tree. So we could have the problem that by continually following subcategory links you will eventually wind up back where we started.

To solve that, in the loop where we expand the categories of the initialNotionsTrees, in the expandNotionNode() function we have to check if every categoryNotionExpanded is already in the initalNotionTree. If this happens, the repeated categoryNotionExpanded is not added to the initialNotionTree

#### Repeated Notions expansions

We may expand the same notions different times. For example: two nearby notions that share some categories at the first level, repeated categoryNotions in different expandedNotionTrees, etc. To solve that, we could create the class "NotionCache" with 3 attributes, the notion, the categories expansion and the pages expansion. We create a Map where the key is the notion and the value is the corresponding “NotionCache” object. 

Every time we want to expand a Notion, before launch the query, we search it on the NotionCache Map. If found, we copy what we need, if not, we launch the query.

### Data structure

<table>
  <tr>
    <td>1</td>
    <td>List<String> initialTerms</td>
    <td>The terms introduced by the Users</td>
  </tr>
  <tr>
    <td>2</td>
    <td>List<Notions> initialNotions</td>
    <td>The initial terms processed and converted into Notions</td>
  </tr>
  <tr>
    <td>3</td>
    <td></td>
    <td></td>
  </tr>
  <tr>
    <td>4</td>
    <td></td>
    <td></td>
  </tr>
  <tr>
    <td>5</td>
    <td></td>
    <td></td>
  </tr>
  <tr>
    <td>6</td>
    <td></td>
    <td></td>
  </tr>
</table>


List<String> initialTerms → Initial terms entered by the user

ArrayList <TreeNode<Notion>> notionTrees → Create a List of TreeNodes which root is each initialTerm found in the Wilipedia. Every time that we make an expansion, one nodes level is added to these notionTrees until we found a common category

# FRONTEND

Followed that tutorial to buld the project: [http://crunchify.com/simplest-spring-mvc-hello-world-example-tutorial-spring-model-view-controller-tips/](http://crunchify.com/simplest-spring-mvc-hello-world-example-tutorial-spring-model-view-controller-tips/)

And that for the hotdeploy: [https://www.mkyong.com/eclipse/how-to-configure-hot-deploy-in-eclipse/](https://www.mkyong.com/eclipse/how-to-configure-hot-deploy-in-eclipse/)

## Structure

## Problems

Problem with relative urls: [https://stackoverflow.com/questions/32922716/jsp-form-tag-action-attribute-uri-spring-mvc](https://stackoverflow.com/questions/32922716/jsp-form-tag-action-attribute-uri-spring-mvc) 

# DEPLOYMENT

We can deploy locally with Tomcat or with Jetty; or deploy online on Heroku with Jetty

## Servers

### Tomcat

That option is better to develop because implements hot swap. You need to install and configure Tomcat on Eclipse

Installation

* Follow that tutorial to install Tomcat on Eclipse: [http://crunchify.com/step-by-step-guide-to-setup-and-install-apache-tomcat-server-in-eclipse-development-environment-ide/](http://crunchify.com/step-by-step-guide-to-setup-and-install-apache-tomcat-server-in-eclipse-development-environment-ide/) 

* Right click on Tomcat Server > Properties > General > Switch Location

* Double click on Tomcat Server > Server Locations > Use Tomcat Installation and save

* Start/Restart server 

* Right click on project > run as > run on server

* Insert url in browser: [http://localhost:8081/wiki-expansion-tool/](http://localhost:8081/wiki-expansion-tool/) 

Possible errors:

[http://crunchify.com/how-to-fix-java-lang-classnotfoundexception-org-springframework-web-servlet-dispatcherservlet-exception-spring-mvc-tomcat-and-404-error/](http://crunchify.com/how-to-fix-java-lang-classnotfoundexception-org-springframework-web-servlet-dispatcherservlet-exception-spring-mvc-tomcat-and-404-error/)

### Jetty Webapp Runner with Tomcat

With this options you have to rebuild and deploy every manualle for see changes. But don’t need to install Tomcat. Also needed to deploy on heroku

Follow that tutorial: [https://devcenter.heroku.com/articles/deploy-a-java-web-application-that-launches-with-jetty-runner#create-a-procfile](https://devcenter.heroku.com/articles/deploy-a-java-web-application-that-launches-with-jetty-runner#create-a-procfile) 

Run with:

* mvn package

* java -jar target/dependency/webapp-runner.jar target/*.war

### Heroku

Followed that tutorial: [https://devcenter.heroku.com/articles/java-webapp-runner#deploy-your-application-to-heroku](https://devcenter.heroku.com/articles/java-webapp-runner#deploy-your-application-to-heroku) 

# NOTES

We can use to deploy: 

* [https://manage.openshift.com/](https://manage.openshift.com/)

* [https://www.cloudbees.com/get-started/cloudbees-jenkins-team](https://www.cloudbees.com/get-started/cloudbees-jenkins-team)

