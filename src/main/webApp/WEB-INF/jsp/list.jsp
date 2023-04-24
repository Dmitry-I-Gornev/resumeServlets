<%@ page import="ru.inock.webServletResime.model.ContactType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div align="center">
    <form action="resume" method="post">
        <fieldset style="width: 180px" id="filterForm">
            <legend>Критерии фильтрации</legend>
            <input type="hidden" name="section" value="filterUsers">
            <input type="text" size="150" name="find" id="find" pattern="*"><br>
            <input type="radio" id="findName"
                   name="searchCriteria" value="name" checked>
            <label for="findName">по имени</label>
            <input type="radio" id="findUuid"
                   name="searchCriteria" value="uuid">
            <label for="findUuid">по uuid</label>

            <button type="submit">Отфильтровать</button>
        </fieldset>
    </form>
</div>

<form action="upload" method="post" enctype="multipart/form-data">
    <input type="hidden" name="section" value="uploadResume">
    Создать новое резюме:<a id='addQualification' href="resume?action=addresume" title="Создать резюме"><img src="png/+.png"></a><br>
    Загрузить резюме из файла:<input type="file" name="fileResume" accept=".xml">
    <br>
    <input type="submit" value="Загрузить">
</form>


    <h2>Список резюме</h2>

    <section id="dataTable">
        <TABLE border="1">
            <tr>
                <td>ФИО</td>
                <td>e-mail</td>
            </tr>
            <c:forEach items="${ArrResumes}" var="resume">
                <jsp:useBean id="resume" type="ru.inock.webServletResime.model.Resume"/>
                <tr>
                    <td><a href="resume?uuid=${resume.uuid}">${resume.fullName}
                    </a></td>
                    <td><a href="mailto:${resume.getContact(ContactType.MAIL)}">${resume.getContact(ContactType.MAIL)}
                    </a></td>
                </tr>
            </c:forEach>
        </TABLE>
    </section>

<p>Восстановить базу данных:
    <a href="resume?action=clear"><img src="png/Undo.png" ALT="Восстановить БД" title="Восстановить БД"/></a>
</p>

<p>
    <a href="source.zip">Исходный кодом</a>
</p>

<c:set var="result" scope="page" value="${result}"/>


<c:if test='${result.equals("findFalse")}'>
<p>${result}</p>



    <link rel="stylesheet" href="/Resumes/js/modal.css">
    <script src="/Resumes/js/modal.js"></script>
    <script>
        (function () {
            // создаём модальное окно
            var modal = $modal({
                title: 'К сожалению, ничего не нашлось. Попробуйте еще раз',
                content: '<center><img src="png/Search.png" alt="" border = "0" /></center>'
            });
            modal.show();
        })();
    </script>
</c:if>

<%@ include file="footer.jsp" %>