<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>bonjour</title>
<s:url value="/resources/css/bonjour.css" var="coreStyle" />
<s:url value="/resources/css/bootstrap.css" var="bootstrapStyle" />
<link href="${coreStyle}" rel="stylesheet" />
<link href="${bootstrapStyle}" rel="stylesheet" />
</head>
<body>
	
	<div class="container">
	<h2>films</h2>
		<div class="well">
			<form method="post" action="films">
				<div class="form-group">
					<label for="name">Nom du film: </label>
					<input type="text" class="form-control" id="name" name="name" />
				</div>
				<div class="form-group">
					<label for="author">Auteur: </label>
					<input type="text" class="form-control" id="author" name="author" />
				</div>
				<div class="form-group">
					<label for="year">year: </label>
					<input type="text" class="form-control" id="year" name="year" />
				</div>
				<div class="form-group">
					<label for="synopsis">synopsis: </label>
					<input type="text" class="form-control" id="synopsis"  name="synopsis" />
				</div>
				<input type="submit" class="btn btn-primary" value="Enregistrer" />
			</form>
		</div>
		<table border=1 class="table table-striped">
				<thead>
					<tr>
						<th>Nom</th>
						<th>auteur</th>
						<th>annee</th>
						<th>synopsis</th>
						<td>delete</td>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="film" items="${films}">
						<tr>
							<td>${film.name}</td>
							<td>${film.author}</td>
							<td>${film.year}</td>
							<td>${film.synopsis}</td>
							<td>
								<form method="post" action="films/${film.id}" >
									<input type="submit" class="btn btn-danger" value="supprimer" />
								</form>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	</div>
</body>
</html>