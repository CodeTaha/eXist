/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

/**
 *
 * @author Taha
 */
import com.google.gson.Gson;

import org.exist.xmldb.XQueryService;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.CompiledExpression;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

public class connectivity {
protected static String DRIVER = "org.exist.xmldb.DatabaseImpl";
protected static String URI = "xmldb:exist://localhost:8080/exist/xmlrpc";
protected String collectionPath = "/db/apps/movies/";
protected static String resourceName = "movies.xml";
protected Collection col ;
Gson gson=new Gson();
    public connectivity()
        {
            try
            {
                Class cl = Class.forName(DRIVER);
                Database database = (Database) cl.newInstance();
                DatabaseManager.registerDatabase(database);
                // get the collection
                col= DatabaseManager.getCollection(URI + collectionPath);
            }
            catch (ClassNotFoundException | InstantiationException | IllegalAccessException | XMLDBException e)
            {
                System.out.println("Error occured in connectivity constructor e="+e);
            }
        }
    public connectivity(String collectionPath)
        {
            try
            {
                Class cl = Class.forName(DRIVER);
                Database database = (Database) cl.newInstance();
                DatabaseManager.registerDatabase(database);
                System.out.println("Collection Path="+collectionPath);
                // get the collection
                col= DatabaseManager.getCollection(URI + collectionPath);
            }
            catch (ClassNotFoundException | InstantiationException | IllegalAccessException | XMLDBException e)
            {
                System.out.println("Error occured in connectivity 2nd constructor e="+e);
            }
        }

    public String getSearchMovies(String search_keyword, String search_type) throws XMLDBException {
        try
        {
            String returnString="";
            /*String xQuery = "let $alls:=doc(\"" + resourceName + "\") "
                    + "for $x in $alls/movies/movie "
                    + "return $x";*/
            String xQuery="let $alls:=doc(\"" + resourceName + "\") " +
                            "for $x in $alls/movies/movie " +
                            "where contains(data($x/"+search_type+"),'"+search_keyword+"') "+                    
                            "return <div><h3><a href='main_Controller?fn=2&amp;title={data($x/title)}'>{data($x/title)}</a></h3><p>{data($x/summary)}</p></div>";
            return executeQuery(xQuery);
            }
        catch (Exception e)
        {
            return "<b>YOUR SEARCH RETURNED NO CONTENT</b>";
        }
    }
    
    public String getAllMovies(String search_keyword, String search_type) throws XMLDBException {
        try
        {
            
            String xQuery="let $alls:=doc(\"" + resourceName + "\") " +
                "let $space := '&#32;'\n" +
                "for $x in $alls/movies/movie\n" +
                "where contains(data($x/"+search_type+"),'"+search_keyword+"') "+   
                "return <div><h3>{data($x/title)}</h3><p>{data($x/summary)}</p><table>\n" +
                "    <tr><td><b>Director</b></td><td>{concat($x/director/first_name,$space,$x/director/last_name)}</td></tr>\n" +
                "    <tr><td><b>Year</b></td><td>{data($x/year)}</td></tr>\n" +
                "    <tr><td><b>Genre</b></td><td>{data($x/genre)}</td></tr>\n" +
                "    <tr><td><b>Country</b></td><td>{data($x/country)}</td></tr>\n" +
                "    {\n" +
                "        for $a in $x/actor\n" +
                "        return <tr><td><b>Actor</b></td><td>{concat($a/first_name,$space,$a/last_name)}</td></tr>\n" +
                "    }\n" +
                "</table></div>";
            return executeQuery(xQuery);
            }
        catch (Exception e)
        {
            return "<b>YOUR SEARCH RETURNED NO CONTENT</b>";
        }
    }

    public String getTitle_Sh() {
        try
        {
            
            /*String xQuery = "let $alls:=doc(\"" + resourceName + "\") "
                    + "for $x in $alls/movies/movie "
                    + "return $x";*/
            String xQuery=
                    "let $alls:=collection('') " +
                    "for $x in $alls/PLAY/TITLE\n" +
                    "return <h4><a onclick=\"getPlayData('{data($x)}')\">{data($x)}</a></h4>";
            
            return executeQuery(xQuery);
            
            }
        catch (Exception e)
        {
            return "<b>YOUR SEARCH RETURNED NO CONTENT</b>";
        }
    }
    public String getAll_Sh(String TITLE) throws XMLDBException {
        String xQuery_arr[]=new String[2];
        xQuery_arr[0]="let $alls:=collection('') " +
                "return <table><tr><th>PERSONA</th><th>Description</th></tr>\n" +
                "    {\n" +
                "    for $x in $alls/PLAY[TITLE=\""+TITLE+"\"]/PERSONAE/PERSONA\n" +
                "    return <tr><td>{data($x)}</td><td></td></tr>\n" +
                "    }\n" +
                "    {\n" +
                "    for $x in $alls/PLAY[TITLE=\"The Tragedy of Hamlet, Prince of Denmark\"]/PERSONAE/PGROUP/PERSONA\n" +
                "    return <tr><td>{data($x)}</td><td style=\"color:grey;\">{data($x/../GRPDESCR)}</td></tr>\n" +
                "    }\n" +
                "</table>";
        xQuery_arr[1]="<table><tr><th>ACT</th><th>Scenes</th></tr>\n" +
                        "    {\n" +
                        "let $alls:=collection('') " +
                        "for $x in $alls/PLAY[TITLE=\""+TITLE+"\"]/ACT\n" +
                        "return <tr><td><a onclick='getScene(\""+TITLE+"\",\"{data($x/TITLE)}\",2)'>{data($x/TITLE)}</a></td><td><table>\n" +
                        "    {\n" +
                        "        for $d in $x/SCENE\n" +
                        "        return <tr><td><a onclick='getScene(\""+TITLE+"\",\"{data($d/TITLE)}\",1)'>{data($d/TITLE)}</a></td></tr>\n" +
                        "        \n" +
                        "    }</table></td></tr>\n" +
                        "}\n" +
                        "</table>";
        
        xQuery_arr[0]=executeQuery(xQuery_arr[0]);
        xQuery_arr[1]=executeQuery(xQuery_arr[1]);
        String returnStr=gson.toJson(xQuery_arr);
        return returnStr;
        
    }
    
    public String getScene_Sh(String playTitle, String sceneTitle,int selection) {
        try
        {
            
            /*String xQuery = "let $alls:=doc(\"" + resourceName + "\") "
                    + "for $x in $alls/movies/movie "
                    + "return $x";*/
            String xQuery;
            if(selection == 1)//for select on scene
            {
             xQuery=
                    "let $alls:=collection('') " +
                    "for $x in $alls/PLAY[TITLE=\""+playTitle+"\"]/ACT/SCENE[TITLE=\""+sceneTitle+"\"]\n" +
                    "return <div><center><h3>{data($x/../TITLE)}</h3><h4>{data($x/TITLE)}</h4></center>\n" +
                    "    {\n" +
                    "        for $y in $x/STAGEDIR\n" +
                    "        return <div><p>StageDir:{data($y)}</p><ul>\n" +
                    "        {\n" +
                    "            for $z in $y/following-sibling::SPEECH\n" +
                    "        return <li>{data($z/SPEAKER)}:{data($z/LINE)}</li>\n" +
                    "        }</ul></div>\n" +
                    "    }\n" +
                    "</div>";
            }
            else //for select on act
            {
                xQuery=
                         "let $alls:=collection('') " +
                        "for $x in $alls/PLAY[TITLE=\""+playTitle+"\"]/ACT[TITLE=\""+sceneTitle+"\"]\n" +
                        "return <div><center><h3>{data($x/TITLE)}</h3></center>\n" +
                        "    {\n" +
                        "        for $y in $x/SCENE\n" +
                        "        return <div><center><h4>{data($y/TITLE)}</h4></center>\n" +
                        "        {\n" +
                        "            for $z in $y/STAGEDIR\n" +
                        "            return <div><p>StageDir:{data($z)}</p><ul>\n" +
                        "                {\n" +
                        "                    for $a in $z/following-sibling::SPEECH\n" +
                        "                    return <li>{data($a/SPEAKER)}:{data($a/LINE)}</li>\n" +
                        "                }\n" +
                        "            </ul></div>\n" +
                        "        }\n" +
                        "        </div>\n" +
                        "            \n" +
                        "    }\n" +
                        "</div>";
            }
            return executeQuery(xQuery);
            
            }
        catch (Exception e)
        {
            return "<b>YOUR SEARCH RETURNED NO CONTENT</b>";
        }
    }
    private String executeQuery(String xQuery) throws XMLDBException
    {
            String returnString="";
            System.out.println("Execute xQuery = " + xQuery);
            // Instantiate a XQuery service
            XQueryService service = (XQueryService) col.getService("XQueryService",
            "1.0");
            service.setProperty("indent", "yes");
            // Execute the query, print the result
            ResourceSet result = service.query(xQuery);
            ResourceIterator i = result.getIterator();
            while (i.hasMoreResources()) 
            {
                Resource r = i.nextResource();
                //System.out.println((String) r.getContent());
                returnString=returnString+(String) r.getContent();
            }
            System.out.println("result="+returnString);
            if(returnString.equals(""))
            {
                returnString="<b>YOUR SEARCH RETURNED NO CONTENT</b>";
            }
            return returnString;
    }

    public String getChar_Sh(String play, String char_name) {
        try
        {
            String xQuery=
                    "let $alls:=collection('') " +
                    "let $speaker:='"+char_name+"' " +
                    "for $x in $alls/PLAY[TITLE=\""+play+"\"]/ACT[SCENE/SPEECH/SPEAKER=$speaker]\n" +
                    "return <div><center><h3>{data($x/TITLE)}</h3></center>\n" +
                    "    {\n" +
                    "        for $y in $x/SCENE[SPEECH/SPEAKER=$speaker]\n" +
                    "        return <div><center><h4>{data($y/TITLE)}</h4></center><ul>\n" +
                    "            {\n" +
                    "                for $z in $y/SPEECH[SPEAKER=$speaker]\n" +
                    "                return <li>{data($z/SPEAKER)}:{data($z/LINE)}</li>\n" +
                    "            }\n" +
                    "        </ul></div>\n" +
                    "    }\n" +
                    "</div>";
            
            return executeQuery(xQuery);
            
            }
        catch (Exception e)
        {
            return "<b>YOUR SEARCH RETURNED NO CONTENT</b>";
        }
    }

    
    
    
}
