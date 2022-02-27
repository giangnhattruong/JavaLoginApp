<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
	<div class="container-fluid">
		<a class="navbar-brand" href="<%= request.getContextPath() + "/app" %>">LoginApp</a>
		<button class="navbar-toggler" type="button"
			data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
			aria-controls="navbarSupportedContent" aria-expanded="false"
			aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse justify-content-end" id="navbarSupportedContent">
			<ul class="navbar-nav mb-2 mb-lg-0">
				<% if (session.getAttribute("email") != null) { %>
				<li class="nav-item">
					<a class="nav-link active" aria-current="page" 
						href="#"><%= session.getAttribute("email") %></a>
				</li>
				<li class="nav-item">
					<a class="nav-link active" aria-current="page" 
						href="<%= request.getContextPath() + "/app?action=logout" %>">Log out</a>
				</li>
				<% } else { %>
				<li class="nav-item">
					<a class="nav-link active" aria-current="page" 
						href="<%= request.getContextPath() + "/app?action=signup" %>">Sign up</a>
				</li>
				<li class="nav-item">
					<a class="nav-link active" aria-current="page" 
						href="<%= request.getContextPath() + "/app?action=login" %>">Log in</a>
				</li>
				<% } %>
				<li class="nav-item">
					<a class="nav-link active" aria-current="page" 
						href="<%= request.getContextPath() + "/app?action=about" %>">About</a>
				</li>
			</ul>
		</div>
	</div>
</nav>