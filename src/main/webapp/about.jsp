<%@include file="boilerplate.jsp" %>
<div class="container">
	<h1 class="text-center my-5">About</h1>
	<ul>
		<li>
			<p>Database: JNDI, MSSQL-JDBC.</p>
		</li>
		<li>
			<p>Data required: a table "tblUser" with 3 fields: "id", "email", "password"</p>
<code><pre class="bg-light p-3 rounded">CREATE TABLE tblUser (
	id IDENTITY(1, 1) PRIMARY KEY,
	email varchar(60) UNIQUE,
	password nvarchar(60)
)</pre></code>
		</li>
	</ul>
	
</div>
<%@include file="boilerplateExtended.jsp" %>