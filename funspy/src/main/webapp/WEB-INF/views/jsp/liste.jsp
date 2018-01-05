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
	<h2>Drone en folie</h2>
		<div class="well">
			<!-- ajout url complete car create et edition ds la mm page / des le debut veut dire a la base du serveur
			et plus de lendroit ou onse trouve 
			-->
			<form method="post" action="/funspy/articles">
				<input type="hidden" name="id" id="id" value="${articleId}" />
				<div class="form-group">
					<label for="nom">Nom de l'article: </label>
					<input type="text" class="form-control" id="nom" name="nom" value="${nom}"/>
				</div>
				<div class="form-group">
					<label for="description">Description: </label>
					<input type="text" class="form-control" id="description" name="description" value="${description}"/>
				</div>
				<div class="form-group">
					<label for="prix">prix: </label>
					<input type="text" class="form-control" id="prix" name="prix" value="${prix}" />
				</div>
				<div class="form-group">
					<label for="poid">poid: </label>
					<input type="text" class="form-control" id="poid"  name="poid" value="${poid}" />
				</div>
				<!--  <div class="form-group">
					<label for="date">date: </label>
					<input type="text" class="form-control" id="date"  name="date" />
				</div>-->
				<input type="submit" class="btn btn-primary" value="Enregistrer" />
			</form>
		</div>
		<div class="well">
			<form action="/funspy/articles" method="get">
				<div class="form-group">
					<label for="searchTerm">Champ de recherhce</label>
					<input type="text" name="searchTerm" id="searchTerm" class="form-control"/>
				</div>
			</form>
		</div>
		<table border=1 class="table table-striped">
				<thead>
					<tr>
						<th>Nom</th>
						<th>description</th>
						<th>prix</th>
						<th>poid</th>
						<th>date</th>
						<td>delete</td>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="film" items="${articles}">
						<tr>
							<td>${film.nom}</td>
							<td>${film.description}</td>
							<td>${film.prix}</td>
							<td>${film.poid}</td>
							<td>${film.dateSortie}</td>
							<td>
								<a href="editArticle/${film.id}" class="btn btn-success"> Editer </a>
								<form method="post" action="delete/${film.id}" >
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