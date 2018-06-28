package controller.resources;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import pmf.PMF;
import models.Access;
import models.Resource;
import models.Role;
import models.Users;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

@SuppressWarnings("serial")
public class add extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");


		UserService us = UserServiceFactory.getUserService();
		User user = us.getCurrentUser();

		if(user == null){
			resp.sendRedirect(us.createLoginURL(req.getRequestURI()));}
		else if(us.isUserAdmin() || accesoRecurso(user.getEmail(),req.getRequestURI())){

			try {
				req.getRequestDispatcher("/WEB-INF/Views/Resource/add.jsp").forward(req, resp);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}
		else {

			try {
				req.getRequestDispatcher("/WEB-INF/Views/AccesoDenegado.jsp").forward(req, resp);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		String url = req.getParameter("url");
	

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			Query query = pm.newQuery(Resource.class);
			query.setFilter("url == idParam");
			query.declareParameters("String idParam");

			List<Resource> arrayRecurso = (List<Resource>)query.execute(url);


			if(arrayRecurso.size() > 0 ){
				resp.getWriter().print("2");
			}
			else {
				Date fecha = new Date();
				Resource rec = new Resource(url,fecha,true);
				pm.makePersistent(rec);
				resp.getWriter().print("1");
			}
			query.closeAll();
		}
		finally{
			pm.close();
		}
	}
	private boolean accesoRecurso(String gmail,String url){

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(User.class);
		query.setFilter("email == idParam");
		query.declareParameters("String idParam");

		List<Users> array = (List<Users>) query.execute(gmail);

		if(array.size()> 0 ){

			Users usuario = array.get(0);
			Query query1 = pm.newQuery(Resource.class);
			query1.setFilter("url == idParam");
			query1.declareParameters("String idParam");

			List<Resource> arrayRecurso = (List<Resource>)query1.execute(url);


			if(arrayRecurso.size() > 0 ){

				Resource recurso = arrayRecurso.get(0);

				Long idRol = usuario.getIdRol();
				Long idRecurso = recurso.getId();

				Query query2 = pm.newQuery(Access.class);
				query2.setFilter("idRole == idParam && idRecurso == idParam2");
				query2.declareParameters("Long idParam , Long idParam2");
				System.out.println("das");

				List<Access> arrayAcceso = (List<Access>)query2.execute(idRol,idRecurso);

				if(arrayAcceso.size()>0){
					Access acceso = arrayAcceso.get(0);
					if(acceso.isEstado()){
						pm.close();
						return true;
					}
				}
			}

		}
		pm.close();
		return false;
	}
}

