package controller.access;

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
public class view extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");

		PersistenceManager pm = PMF.get().getPersistenceManager();

		UserService us = UserServiceFactory.getUserService();
		User user = us.getCurrentUser();

		if(user == null){
			resp.sendRedirect(us.createLoginURL(req.getRequestURI()));}
		else if(us.isUserAdmin() || accesoRecurso(user.getEmail(),req.getRequestURI())){
			try {

				Query query = pm.newQuery(Role.class);
				List<Role> arrayRole = (List<Role>)query.execute("select from Role");

				Query query1 = pm.newQuery(Resource.class);
				List<Resource> arrayRecurso = (List<Resource>)query1.execute("select from Recurso");

				String id = req.getParameter("id");
				Long idLong = Long.parseLong(id); 

				Access acceso = pm.getObjectById(Access.class,idLong);

				req.setAttribute("acceso",acceso);
				req.setAttribute("arrayRole",arrayRole);
				req.setAttribute("arrayRecurso",arrayRecurso);



				req.getRequestDispatcher("/WEB-INF/Views/Access/view.jsp").forward(req, resp);

				query.closeAll();
				query1.closeAll();
			} catch (Exception e) {
				System.out.println("Error "+e.getMessage());
			}
			finally{
				pm.close();
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

		//Realizar la persistencia
		String id = req.getParameter("id");
		String idRole = req.getParameter("role");
		String idRecurso = req.getParameter("recurso");
		String estado = req.getParameter("estado");

		PersistenceManager pm = PMF.get().getPersistenceManager();

		try{
			Long idLong = Long.parseLong(id);
			Long idRoleL = Long.parseLong(idRole);
			Long idRecursoL = Long.parseLong(idRecurso);

			boolean estadoBool = Boolean.parseBoolean(estado);

			Access acceso = pm.getObjectById(Access.class,idLong);
			acceso.setIdRecurso(idRecursoL);
			acceso.setIdRole(idRoleL);
			acceso.setEstado(estadoBool);

		}
		catch(Exception e){
			System.out.println("Se produjo un Error");
		}
		finally{
			pm.close();
		}
		resp.sendRedirect("/access");
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

