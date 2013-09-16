package com.ascent.filter;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class SignonFilter implements Filter {

       
	    public void init(javax.servlet.FilterConfig config){  }

        public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)throws IOException,ServletException
        {
          System.out.println("do filter begin");
          HttpServletRequest hreq = (HttpServletRequest)req;
          HttpServletResponse hres = (HttpServletResponse)res;

          HttpSession session = hreq.getSession(false);
          try{
            if (session != null) {
                String isLogin = (String) session.getAttribute("isLogin");
                System.out.println("islogin: ?" + isLogin);
                
                if ( (isLogin != null) && (isLogin.equals("true"))) {  // ok
                    System.out.println("Filter passed");
                    chain.doFilter(req, res);  //go to resource or next filter
                }
                else { // user is illegal
                    System.out.println("Filter rejected, please go to the sign on page");
                     hres.sendRedirect(hreq.getContextPath()+"/login.jsp"); // go to the sign on page
                }
            }else{  //no session
               System.out.println("session is null, Filter rejected, please go to the sign on page");
               hres.sendRedirect(hreq.getContextPath()+"/login.jsp");
            }
          }catch(Exception e){
                 e.printStackTrace();
          }
        }
               
       public void destroy(){   }
}
       