<%@ page import="ru.inock.webServletResime.model.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- Resume r = (Resume) request.getAttribute("resume"); --%>
<c:set var="r" scope="page" value="${resume}"/>
<jsp:useBean id="r" type="ru.inock.webServletResime.model.Resume"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="header.jsp" %>

<h3><font color="silver">Текущий пользователь:</font> <%=session.getAttribute("user")%>
</h3>


<h2 align="center">Персональная карточка клиента</h2>
<p>
    <a href="resume?uuid=${r.uuid}&action=delete"><img src="png/RecycleBin_Empty.png " ALT="Удалить резюме"
                                                       title="Удалить резюме"/></a>&nbsp;
    <a href="resume"><img src="png/Home.png" ALT="К списку резюме" title="К списку резюме"/></a>
    <a href="upload?uuid=${r.uuid}"><img src="png/Floppy.png" ALT="Сохранить в файл" title="Сохранить в файл"/></a></p>
</p>

<!-- Full Name -->
<fieldset>
    <h3>ФИО:</h3>
    <c:if test="${r == null}">
        <input type="text" name="fullName" size="60" value="${r.fullName}" disabled>
        <a href="">create</a>
    </c:if>
    <c:if test="${r != null}">
        <input type="text" id="fullName" name="fullName" size="60" value="${r.fullName}" disabled>

        <a href="" id="fullNameEditLink" onclick="enabledElement('fullName');
                hidedElement('fullNameEditLink');
                inlineShowElement('fullNameSaveLink');
                inlineShowElement('fullNameBackLink');
                return false;" title="Редактировать"><img src="png/Pencil.png"></a>


        <a href="" id="fullNameSaveLink" style="display: none" onclick="
                window.location.href='resume?uuid=${r.uuid}&action=save&section=fullName&value=' + document.getElementById('fullName').value; return false;">сохранить</a>


        <a href="#" id="fullNameBackLink" style="display: none"
           onclick="document.getElementById('fullName').value = '${r.fullName}';
                   disabledElement('fullName');
                   hidedElement('fullNameSaveLink');
                   hidedElement('fullNameBackLink');
                   inlineShowElement('fullNameEditLink');
                   return false;">отменить</a>

    </c:if>
</fieldset>
<!-- End Full Name -->

<!-- Контакты -->
<form action="resume" method="post" id="contactsForm">
    <fieldset id="contacts" disabled>
        <legend>Контакты</legend>
        <input type="hidden" name="uuid" value="${r.uuid}">
        <input type="hidden" name="section" value="contacts">

        <c:forEach var="contact" items="<%=ContactType.values()%>">
        <dl>
            <dt>${contact.getTitle()}:</dt>
            <dd><input type="text" name="${contact.name()}" size="60" value="${r.getContact(contact)}"></dd>
            </c:forEach>
        </dl>
    </fieldset>
    <input type="button" value="редактировать секцию" id="editContactsButton" onclick="enabledElement('contacts');
                hidedElement('editContactsButton');
                inlineShowElement('saveContactsButton');
                inlineShowElement('backContactsButton');
                return false">
    <input type="submit" Value="сохранить" style="display: none" id="saveContactsButton">

    <input type="reset" value="отменить" style="display: none" id="backContactsButton"
           onclick="this.form.reset();
           disabledElement('contacts');
                hidedElement('saveContactsButton');
                hidedElement('backContactsButton');
                inlineShowElement('editContactsButton');
                return false">

</form>
<!-- конец контактов -->

<fieldset>
    <!-- Личные качества -->
    <dl>
        <dt>${SectionType.PERSONAL.getTitle()}:</dt>
        <dd>
            <textarea id="personalInput" name="PERSONAL" cols="60" rows="5"
                      disabled>${r.getSection(SectionType.PERSONAL)}</textarea>

            <a href="" id="personalEditLink" onclick="enabledElement('personalInput');
                hidedElement('personalEditLink');
                inlineShowElement('personalSaveLink');
                inlineShowElement('personalBackLink');
                return false" title="Редактировать"><img src="png/Pencil.png"></a>


            <a href="#" id="personalSaveLink" style="display: none" onclick="
                    window.location.href='resume?uuid=${r.uuid}&action=save&section=personal&value=' + document.getElementById('personalInput').value;">сохранить</a>


            <a href="" id="personalBackLink" style="display: none"
               onclick="document.getElementById('personalInput').value = ${r.getSection(SectionType.PERSONAL)};
                       disabledElement('personalInput');
                       hidedElement('personalSaveLink');
                       hidedElement('personalBackLink');
                       inlineShowElement('personalEditLink');
                       return false">отменить</a>
        </dd>
    </dl>
    <!-- Позиция -->
    <dl>
        <dt>${SectionType.OBJECTIVE.getTitle()}:</dt>
        <dd>
            <textarea id="objectiveInput" name="OBJECTIVE" cols="60" rows="5"
                      disabled>${r.getSection(SectionType.OBJECTIVE)}</textarea>

            <a href="" id="objectiveEditLink" onclick="enabledElement('objectiveInput');
                hidedElement('objectiveEditLink');
                inlineShowElement('objectiveSaveLink');
                inlineShowElement('objectiveBackLink');
                return false" title="Редактировать"><img src="png/Pencil.png"></a>


            <a href="#" id="objectiveSaveLink" style="display: none" onclick="
                    window.location.href='resume?uuid=${r.uuid}&action=save&section=objective&value=' + document.getElementById('objectiveInput').value;">сохранить</a>


            <a href="" id="objectiveBackLink" style="display: none"
               onclick="document.getElementById('objectiveInput').value = ${r.getSection(SectionType.OBJECTIVE)};
                       disabledElement('objectiveInput');
                       hidedElement('objectiveSaveLink');
                       hidedElement('objectiveBackLink');
                       inlineShowElement('objectiveEditLink');
                       return false">отменить</a>
        </dd>
    </dl>
</fieldset>

<!-- квалификация -->
<form action="resume" method="post">
    <input type="hidden" name="uuid" value="${r.uuid}">
    <input type="hidden" name="section" value="qualification">
    <fieldset id="qualification" disabled>
        <legend>${SectionType.QUALIFICATION.getTitle()}</legend>
        <UL id="qualificationUl">
            <c:set scope="page" var="section" value="<%=(r.getSection(SectionType.QUALIFICATION))%>"/>

            <c:if test="${empty section}">
            <li><input type="text" name="qualification" size="60" value="">
                </c:if>

                <c:if test="${not empty section}">
                <c:forEach var="q" items="<%=((ListSection)r.getSection(SectionType.QUALIFICATION)).getItems()%>">

            <li><input type="text" name="qualification" size="60" value="${q}">
                </c:forEach>
                </c:if>
        </UL>

        <a id='addQualification' href="" style="display: none"
           onclick="addListPosition('qualificationUl','qualification'); return false;" title="Добавить позицию"><img
                src="png/+.png"></a>
        <a id='saveQualificationButton' href="" style="display: none"
           onclick="parentElement.parentElement.submit(); return false;" title="Сохранить"><img src="png/Save.png"></a>
        <a id='backQualificationButton' href="" style="display: none"
           onclick="location.reload(); return false;" title="Отменить изменения"><img src="png/Undo.png"></a>
    </fieldset>
    <input type="button" id="editQualificationButton" value="редактировать секцию" onclick="enabledElement('qualification');
inlineShowElement('addQualification');hidedElement('editQualificationButton');
inlineShowElement('backQualificationButton');inlineShowElement('saveQualificationButton');">
</form>

<!-- Достижения -->
<form action="resume" method="post">
    <input type="hidden" name="uuid" value="${r.uuid}">
    <input type="hidden" name="section" value="arhievement">
    <fieldset id="arhievement" disabled>
        <legend>${SectionType.ARHIEVEMENT.getTitle()}</legend>
        <UL id="arhievementUl">
            <c:set scope="page" var="section" value="<%=(r.getSection(SectionType.ARHIEVEMENT))%>"/>
            <c:if test="${empty section}">
            <li><input type="text" name="arhievement" size="60" value="">
                </c:if>
                <c:if test="${not empty section}">
                <c:forEach var="q" items="<%=((ListSection)r.getSection(SectionType.ARHIEVEMENT)).getItems()%>">

            <li><input type="text" name="arhievement" size="60" value="${q}">
                </c:forEach>
                </c:if>
        </UL>
        <a id='addArhievement' href="" style="display: none"
           onclick="addListPosition('arhievementUl','arhievement'); return false;" title="Добавить позицию"><img
                src="png/+.png"></a>
        <a id='saveArhievementButton' href="" style="display: none"
           onclick="parentElement.parentElement.submit(); return false;" title="Сохранить"><img src="png/Save.png"></a>
        <a id='backArhievementButton' href="" style="display: none"
           onclick="location.reload(); return false;" title="Отменить изменения"><img src="png/Undo.png"></a>
    </fieldset>
    <input type="button" id="editArhievementButton" value="редактировать секцию" onclick="enabledElement('arhievement');
inlineShowElement('addArhievement');
hidedElement('editArhievementButton');
inlineShowElement('backArhievementButton');
inlineShowElement('saveArhievementButton');">
</form>

<!-- Опыт работы -->

<form action="resume" method="post" id="experience">
    <c:set scope="page" var="sectionsExperience" value="<%=(r.getSection(SectionType.EXPERIENCE))%>"/>

    <input type="hidden" name="section" value="experience"/>
    <input type="hidden" name="experienceJson" id="idExperienceJson" value="jsonString"/>
    <input type="hidden" name="uuid" id="experienceUuid" value="${r.uuid}"/>

    <fieldset id="experienceOrganisations">

        <legend>${SectionType.EXPERIENCE.getTitle()}</legend>
        <a href="#" id="addOrganisationButton" alt="Добавить организацию" title="Добавить организацию"
           style="display: none"
           onclick="addOrganisation(parentElement); return false;"><img src="png/+.png"></a>
        <a id='saveExperienceButton' href="" style="display: none"
           onclick="saveOrganisationsToJson(parentElement,'EXPERIENCE'); return false;" title="Сохранить секцию"><img
                src="png/Save.png"></a>
        <c:if test="${not empty sectionsExperience}">
            <c:forEach var="org"
                       items="<%=((OrganisationSection)r.getSection(SectionType.EXPERIENCE)).getOrganizations()%>">
                <fieldset name="organisation">
                    <legend>${org.title}</legend>
                    <input type="text" placeholder="Название организации" title="Название организации" size="60"
                           maxlength="50" name="orgName" value="${org.title}" disabled/>
                    <a href="#" style="display: none" onclick="eraseWorkPeriod(parentElement); return false;"
                       alt="Удалить организацию" title="Удалить организацию"><img src="png/Error.png"></a><br><br>
                    <a href="#" alt="Перейти на сайт" title="Перейти на сайт"
                       onclick="window.open(nextElementSibling.value, '_blank'); return false;"><img
                            src="png/internet24.png"></a>
                    <input type="text" title="URL" placeholder="URL страницы организации в Интернете" size="60"
                           maxlength="50" name="orgUrl" value="${org.homePage.url}" disabled/>
                    <br>
                    <p style="display:inline">Периоды работы:</p>
                    <a href="#" name="addWorkPeriodButton" alt="Добавить период" title="Добавить период"
                       style="display: none" onclick="addWorkPeriod(parentElement); return false;"><img src="png/+.png"></a>
                    <c:forEach var="workPeriod" items="${org.workPeriods}">
                        <jsp:useBean id="workPeriod" type="ru.inock.webServletResime.model.Organisation.WorkPeriod"/>
                        <div name="divWorkPeriod">
                            <input type="date" name="startPeriod" value="${workPeriod.startDate}" disabled>
                            -
                            <input type="date" name="endPeriod" value="${workPeriod.endDate}" disabled>
                            :
                            <input type="text" name="work" size="80" value="${workPeriod.description}" disabled>
                            <a href="#" style="display: none" onclick="eraseWorkPeriod(parentElement); return false;"
                               alt="Удалить период" title="Удалить период"><img src="png/Error.png"></a><br>
                        </div>
                    </c:forEach>
                    <hr>
                </fieldset>

            </c:forEach>
        </c:if>

        <c:if test="${empty sectionsExperience}">
            <fieldset name="organisation">
                <legend>Не внесено ни одной организации</legend>
                <input type="text" placeholder="Название организации" title="Название организации" size="60"
                       maxlength="50" name="orgName" value="" disabled/>
                <a href="#" style="display: none" onclick="eraseWorkPeriod(parentElement); return false;"
                   alt="Удалить организацию" title="Удалить организацию"><img src="png/Error.png"></a><br><br>
                <a href="#" alt="Перейти на сайт" title="Перейти на сайт"
                   onclick="window.open(nextElementSibling.value, '_blank'); return false;"><img
                        src="png/internet24.png"></a>
                <input type="text" title="URL" placeholder="URL страницы организации в Интернете" size="60"
                       maxlength="50" name="orgUrl" value="" disabled/>
                <br>
                <p style="display:inline">Периоды работы:</p>
                <a href="#" name="addWorkPeriodButton" alt="Добавить период" title="Добавить период"
                   style="display: none" onclick="addWorkPeriod(parentElement); return false;"><img src="png/+.png"></a>
                <div name="divWorkPeriod">
                    <input type="date" name="startPeriod" value="" disabled>
                    -
                    <input type="date" name="endPeriod" value="" disabled>
                    :
                    <input type="text" name="work" size="80" value="" disabled>
                    <a href="#" style="display: none" onclick="eraseWorkPeriod(parentElement); return false;"
                       alt="Удалить период" title="Удалить период"><img src="png/Error.png"></a><br>
                </div>

                <hr>
            </fieldset>

        </c:if>

        <input type="button" id="editExperienceButton" value="Редактировать секцию" onclick="enabledFormOrganisation(parentElement);
                            inlineShowElement('saveExperienceButton');
                            inlineShowElement('addOrganisationButton');
                            hidedElement('editExperienceButton');
                            return false;"/>
    </fieldset>
</form>

<!-- Образование -->
<form action="resume" method="post" id="education">
    <c:set scope="page" var="sectionEducation" value="<%=(r.getSection(SectionType.EDUCATION))%>"/>

    <input type="hidden" name="section" value="education"/>
    <input type="hidden" name="educationJson" id="idEducationJson" value="jsonString"/>
    <input type="hidden" name="uuid" id="educationUuid" value="${r.uuid}"/>

    <fieldset id="educationOrganisations">
        <legend>${SectionType.EDUCATION.getTitle()}</legend>
        <a href="" id="addOrganisationButton" alt="Добавить организацию" title="Добавить организацию"
           style="display: none"
           onclick="addOrganisation(parentElement); return false;"><img src="png/+.png"></a>
        <a id='saveEducationButton' href="" style="display: none"
           onclick="saveOrganisationsToJson(parentElement,'EDUCATION'); return false;" title="Сохранить секцию"><img
                src="png/Save.png"></a>
        <c:if test="${not empty sectionEducation}">
            <c:forEach var="org"
                       items="<%=((OrganisationSection)r.getSection(SectionType.EDUCATION)).getOrganizations()%>">
                <fieldset name="organisation">
                    <legend>${org.title}</legend>
                    <input type="text" placeholder="Название организации" title="Название организации" size="60"
                           maxlength="50" name="orgName" value="${org.title}" disabled/>
                    <a href="#" style="display: none" onclick="eraseWorkPeriod(parentElement); return false;"
                       alt="Удалить организацию" title="Удалить организацию"><img src="png/Error.png"></a><br><br>
                    <a href="#" alt="Перейти на сайт" title="Перейти на сайт"
                       onclick="window.open(nextElementSibling.value, '_blank'); return false;"><img
                            src="png/internet24.png"></a>
                    <input type="text" title="URL" placeholder="URL страницы организации в Интернете" size="60"
                           maxlength="50" name="orgUrl" value="${org.homePage.url}" disabled/>
                    <br>
                    <p style="display:inline">Периоды обучения:</p>
                    <a href="#" name="addWorkPeriodButton" alt="Добавить период" title="Добавить период"
                       style="display: none" onclick="addWorkPeriod(parentElement); return false;"><img src="png/+.png"></a>
                    <c:forEach var="educationPeriod" items="${org.workPeriods}">
                        <jsp:useBean id="educationPeriod" type="ru.inock.webServletResime.model.Organisation.WorkPeriod"/>
                        <div name="divWorkPeriod">
                            <input type="date" name="startPeriod" value="${educationPeriod.startDate}" disabled>
                            -
                            <input type="date" name="endPeriod" value="${educationPeriod.endDate}" disabled>
                            :
                            <input type="text" name="work" size="80" value="${educationPeriod.description}" disabled>
                            <a href="#" style="display: none" onclick="eraseWorkPeriod(parentElement); return false;"
                               alt="Удалить период" title="Удалить период"><img src="png/Error.png"></a><br>
                        </div>
                    </c:forEach>
                    <hr>
                </fieldset>

            </c:forEach>
        </c:if>

        <c:if test="${empty sectionEducation}">
            <fieldset name="organisation">
                <legend>Не внесено ни одной организации</legend>
                <input type="text" placeholder="Название организации" title="Название организации" size="60"
                       maxlength="50" name="orgName" value="" disabled/>
                <a href="#" style="display: none" onclick="eraseWorkPeriod(parentElement); return false;"
                   alt="Удалить организацию" title="Удалить организацию"><img src="png/Error.png"></a><br><br>
                <a href="#" alt="Перейти на сайт" title="Перейти на сайт"
                   onclick="window.open(nextElementSibling.value, '_blank'); return false;"><img
                        src="png/internet24.png"></a>
                <input type="text" title="URL" placeholder="URL страницы организации в Интернете" size="60"
                       maxlength="50" name="orgUrl" value="" disabled/>
                <br>
                <p style="display:inline">Периоды обучения:</p>
                <a href="#" name="addWorkPeriodButton" alt="Добавить период" title="Добавить период"
                   style="display: none" onclick="addWorkPeriod(parentElement); return false;"><img src="png/+.png"></a>
                <div name="divWorkPeriod">
                    <input type="date" name="startPeriod" value="" disabled>
                    -
                    <input type="date" name="endPeriod" value="" disabled>
                    :
                    <input type="text" name="work" size="80" value="${educationPeriod.description}" disabled>
                    <a href="#" style="display: none" onclick="eraseWorkPeriod(parentElement); return false;"
                       alt="Удалить период" title="Удалить период"><img src="png/Error.png"></a><br>
                </div>
                <hr>
            </fieldset>
        </c:if>

        <input type="button" id="editEducationButton" value="Редактировать секцию" onclick="enabledFormOrganisation(parentElement);
                            inlineShowElement('saveEducationButton');
                            inlineShowElement('addOrganisationButton');
                            hidedElement('editEducationButton');
                            return false;"/>
    </fieldset>
</form>

<%@ include file="footer.jsp" %>
