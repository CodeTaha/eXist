/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.google.gson.Gson;
import connection.connectivity;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.xmldb.api.base.XMLDBException;

/**
 *
 * @author Taha
 */
public class main_Controller extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
         System.out.println("reached");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
         PrintWriter out = response.getWriter();
        System.out.println("Get f try");
        
        int fn=Integer.parseInt(request.getParameter("fn"));
        
        String jsonString="";
        connectivity conn=new connectivity();
        Gson gson=new Gson();
        try{
        switch(fn)
        {
            case 1:{
                    String search_keyword=request.getParameter("search_keyword");
                    String search_type=request.getParameter("search_type");
                    jsonString=conn.getSearchMovies(search_keyword,search_type);
                    out.println(jsonString);
                    }break;
            case 2:{
                    String search_keyword=request.getParameter("title");
                    jsonString=conn.getAllMovies(search_keyword,"title");
                    out.println("<html><head><title>Movie</title></head><body>");
                    out.println(jsonString);
                    out.println("</body></html>");
                    }break;
            case 3:{
                    conn=new connectivity("/db/apps/demo/data");
                    jsonString=conn.getTitle_Sh();
                    out.println(jsonString);
                    }break;
            case 4:{
                    conn=new connectivity("/db/apps/demo/data");
                    String TITLE=request.getParameter("playTitle");
                    jsonString=conn.getAll_Sh(TITLE);
                    out.println(jsonString);
                    }break;
            case 5:{
                    conn=new connectivity("/db/apps/demo/data");
                    String playTitle=request.getParameter("playTitle");
                    String sceneTitle=request.getParameter("sceneTitle");
                    int selection=Integer.parseInt(request.getParameter("selection"));
                    jsonString=conn.getScene_Sh(playTitle,sceneTitle,selection);
                    out.println(jsonString);
                    }break;
            case 6:{
                    conn=new connectivity("/db/apps/demo/data");
                    String play=request.getParameter("play");
                    String char_name=request.getParameter("char_name");
                    jsonString=conn.getChar_Sh(play,char_name);
                    out.println(jsonString);
                    }break;
        }
        }
        catch (XMLDBException | NumberFormatException e)
        {
            System.out.println("Exception in Get>Switch>fn="+fn+" e="+e);
            out.println("<b>Your search returned no content</b>");
        }
        
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
