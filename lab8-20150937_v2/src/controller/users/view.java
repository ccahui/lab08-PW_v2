package controller.users;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import models.Access;
import models.Resource;
import models.Role;
import models.Users;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import pmf.PMF;

@SuppressWarnings("serial")
public class view extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");


		UserService us = UserServiceFactory.getUserService();
		User user = us.getCurrentUser();

		if(user == null){
			resp.sendRedirect(us.createLoginURL(req.getRequestURI()));}
		else if(us.isUserAdmin() || accesoRecurso(user.getEmail(),req.getRequestURI())){
			PersistenceManager pm = PMF.get().getPersistenceManager();

			try {

				String codigo = req.getParameter("codigo");
				Long codigoLong = Long.parseLong(codigo); 
				Users usuario = pm.getObjectById(Users.class,codigoLong);

				Query query = pm.newQuery(Role.class);
				//query.setOrdering("nombre descending");

				List<Role> array = (List<Role>)query.execute("select from Role");

				req.setAttribute("array", array);


				req.setAttribute("usuario",usuario);
				req.getRequestDispatcher("/WEB-INF/Views/Users/view.jsp").forward(req, resp);

			} catch (Exception e) {
				e.printStackTrace();
			}
			finally{
				pm.close();
			}}else {

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
		String codigo = req.getParameter("codigo");
		String nombre = req.getParameter("nombre");
		String apellido = req.getParameter("apellido");
		String email = req.getParameter("email");
		String estado = req.getParameter("estado");
		Long idRol = Long.parseLong(req.getParameter("idRol"));

		PersistenceManager pm = PMF.get().getPersistenceManager();

		try{
			Long codigoLong = Long.parseLong(codigo);
			boolean estadoBoolean = Boolean.parseBoolean(estado);

			Users usuario = pm.getObjectById(Users.class,codigoLong);

			usuario.setNombre(nombre);
			usuario.setApellido(apellido);
			usuario.setEmail(email);
			usuario.setEstado(estadoBoolean);
			usuario.setIdRol(idRol);
		}
		catch(Exception e){
			System.out.println("Se produjo un Error");
		}
		finally{
			pm.close();
		}
		resp.sendRedirect("/users");
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

