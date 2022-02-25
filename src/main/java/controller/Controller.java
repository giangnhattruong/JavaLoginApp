package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

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
		String page = "";
		
		switch (action) {
		case "signup":
			page = "/signup.jsp";
			break;
		case "login":
			page = "/login.jsp";
			break;
		case "logout":
			session.setAttribute("email", null);
			page = "/index.jsp";
			break;
		case "about":
			page = "/about.jsp";
			break;
		default:
			page = "/error.jsp";
		}
		
		if (session.getAttribute("email") != null &&
				(page == "/login.jsp" || page == "/signup.jsp"))
			response.sendRedirect(response.encodeURL(basePath));
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
				loginRedirect(response, basePath, session, email);
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
				loginRedirect(response, basePath, session, email);
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

	private void loginRedirect(HttpServletResponse response, String basePath, HttpSession session, String email)
			throws IOException {
		session.setAttribute("email", email);
		session.setMaxInactiveInterval(30);
		response.sendRedirect(response.encodeURL(basePath));
	}

}
