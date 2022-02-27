<%@include file="boilerplate.jsp" %>
<div class="container">
	<h1 class="text-center my-5">Sign Up</h1>

	<div class="row justify-content-center">
		<div class="col-6 border border-0 rounded border-primary px-5 py-3 bg-info p-2 text-white">
			<form method="post"
				action="<%=response.encodeURL(request.getContextPath() + "/app")%>">
				<% if (request.getAttribute("validatingMessage") != null) { %>
				<div class="row">
					<p class="text-white"><%= request.getAttribute("validatingMessage") %></p>
				</div>
				<% } %>
				<div class="my-3 row">
					<label for="email" class="col-sm-2 col-form-label">Email</label> 
					<div class="col-sm-10">
					<input
						id="email" type="text" name="email" value="<%%>"
						class=" col-sm-10 form-control" />
					</div>
				</div>
				<div class="mb-3 row">
					<label for="password1" class="col-sm-2 col-form-label">Password</label>
					<div class="col-sm-10">
						<input id="password1" type="password" name="password1"
							class="col-sm-10 form-control" />
					</div>
				</div>
				<div class="mb-3 row">
					<label for="password2" class="col-sm-2 col-form-label"></label>
					<div class="col-sm-10">
						<input id="password2" type="password" name="password2"
							class="col-sm-10 form-control" placeholder="Re-enter password" />
					</div>
				</div>
				<div class="row justify-content-center">
					<input type="hidden" name="formAction" value="signup"/>
					<button class="col-sm-3 btn btn-success">Sign Up</button>
				</div>
			</form>
		</div>
	</div>

</div>
<%@include file="boilerplateExtended.jsp" %>