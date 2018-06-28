package controlador.usuario;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import modelo.usuario.Usuario;
import modelo.role.*;
import modelo.recurso.*;
import modelo.acceso.*;

@SuppressWarnings("serial")
public class usuarioAcceso extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");


			try {
				req.getRequestDispatcher("/WEB-INF/Vistas/Usuario/usuarioAcceso.jsp").forward(req, resp);

			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}



}
