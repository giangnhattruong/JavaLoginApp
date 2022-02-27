package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 * Servlet implementation class DataSource
 */
@WebServlet("/Controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataSource ds;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Controller() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		try {
			InitialContext initContext = new InitialContext();
			Context env = (Context) initContext.lookup("java:comp/env");
			ds = (DataSource) env.lookup("jdbc/RTTO");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String basePath = request.getContextPath();
		HttpSession session = request.getSession();
		
		String action = request.getParameter("action");
		Map<String, String> routes = new HashMap<>();
		routes.put("signup", "/signup.jsp");
		routes.put("login", "/login.jsp");
		routes.put("about", "/about.jsp");
		routes.put("signup", "/signup.jsp");
		routes.put(null, "/index.jsp");
		String page = routes.get(action) == null? "/error.jsp": routes.get(action);
		
		if (action != null && action.equals("logout")) {
			session.setAttribute("email", null);
			page = "/index.jsp";
		}
		
		if (session.getAttribute("email") != null &&
				(page == "/login.jsp" || page == "/signup.jsp"))
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		else
			request.getRequestDispatcher(page).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String basePath = request.getContextPath();
		HttpSession session = request.getSession();
		String formAction = request.getParameter("formAction");
		
		if (formAction.equals("login"))
			logIn(request, response, basePath, session);
		else if (formAction.equals("signup"))
			signUp(request, response, basePath, session);
	}

	private void logIn(HttpServletRequest request, HttpServletResponse response, String basePath, HttpSession session) 
			throws IOException, ServletException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		User user = new User(email, password);
		user.validate();
		String validatingMessage = user.getMessage();
		
		try (Connection con = ds.getConnection();) {
			Account account = new Account(con);
			boolean userExists = account.verify(email, password);
			
			if (validatingMessage.equals("Successful") &&
					userExists == true) {
				loginForward(request, response, session, email);
				return;
			}

			if (validatingMessage.equals("Successful") &&
					userExists == false)
				validatingMessage = "User doesn't exist.";
				
			forward(request, response, "/login.jsp", validatingMessage);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			forward(request, response, "/login.jsp", "Sorry, our server is temporary down, please try again later.");
			e.printStackTrace();
		}
	}
	
	private void signUp(HttpServletRequest request, HttpServletResponse response, String basePath, HttpSession session) 
					throws IOException, ServletException {
		String email = request.getParameter("email");
		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");
		
		if (!password1.equals(password2)) {
			forward(request, response, "/signup.jsp", "Password must be matched.");
			return;
		}		

		String password = password1;
		
		User user = new User(email, password);
		user.validate();
		String validatingMessage = user.getMessage();
		
		try (Connection con = ds.getConnection();) {
			Account account = new Account(con);
			boolean userExists = account.getUser(email) != null;
			
			if (validatingMessage.equals("Successful") &&
					userExists == false) {
				account.addUser(email, password);
				loginForward(request, response, session, email);
				return;
			}

			if (validatingMessage.equals("Successful") &&
					userExists == true)
				validatingMessage = "User exists.";
				
			forward(request, response, "/signup.jsp", validatingMessage);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			forward(request, response, "/signup.jsp", "Sorry, our server is temporary down, please try again later.");
			e.printStackTrace();
		}
	}

	private void forward(HttpServletRequest request, HttpServletResponse response, String page, String message)
			throws ServletException, IOException {
		request.setAttribute("validatingMessage", message);
		request.getRequestDispatcher(page).forward(request, response);
	}

	private void loginForward(HttpServletRequest request, HttpServletResponse response, HttpSession session, String email)
			throws IOException, ServletException {
		session.setAttribute("email", email);
		session.setMaxInactiveInterval(30);
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}

}
