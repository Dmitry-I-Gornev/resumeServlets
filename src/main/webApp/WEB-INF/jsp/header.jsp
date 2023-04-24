<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML lang="ru">
<HEAD>
    <TITLE><%= (String) request.getAttribute("title") %>
    </TITLE>
    <META content="text/html; charset=utf-8" http-equiv=Content-Type>
    <meta name="Other.Language" content="Russian">
    <meta name="distribution" content="global">
    <meta name="rating" content="General">
    <meta name="ROBOTS" content="INDEX,FOLLOW">
    <META http-equiv="Content-Script-Type" content="text/javascript">

    <style>
        input[type="text"].link:hover {
            border-bottom: 1px solid #ff000000;
            cursor: pointer;
            padding-bottom: 2px;
        }

        input[type="text"].link {
            border: unset;
            background: unset;
            border-bottom: 1px solid #969696;
            outline: unset;
            padding: unset;
            margin: unset;
            text-align: center;
            padding-bottom: 2px;

        }
    </style>
    <script>
        "use strict";

        function enabledElement(idElement) {
            document.getElementById(idElement).disabled = false;
            return false;
        }

        function disabledElement(idElement) {
            document.getElementById(idElement).disabled = true;
            return false;
        }

        function hidedElement(idElement) {
            document.getElementById(idElement).style.display = 'none';
            return false;
        }

        function inlineShowElement(idElement) {
            document.getElementById(idElement).style.display = 'inline';
            return false;
        }

        function addListPosition(idBlok, nameInput) {
            // Создаем элемент ДИВ
            var div = document.createElement("div");
            // Добавляем HTML-контент с пом. свойства innerHTML
            div.innerHTML = "<li><input type='text' name=" + nameInput + " size='60' value=''>";
            // Добавляем новый узел в конец списка полей
            document.getElementById(idBlok).appendChild(div);
            // Возвращаем false, чтобы не было перехода по сслыке
            return false;
        }

        function addWorkPeriod(parrentObject) {
            // Создаем элемент ДИВ
            var div = document.createElement("div");
            div.setAttribute('name', 'divWorkPeriod');
            // Добавляем HTML-контент с пом. свойства innerHTML
            let divTpl = `
            <div name="divWorkPeriod">
                            <input type="date" name="startPeriod" value="${workPeriod.startDate}" />
                        -
                            <input type="date" name="endPeriod" value="${workPeriod.endDate}"  />
                        :
                        <input type="text" name="work" size="80" value="${workPeriod.description}" />
                        <a href = "#" onclick="eraseWorkPeriod(parentElement); return false;" alt="Удалить период" title="Удалить период"><img src="png/Error.png"></a><br>
                        </div>
            `;
            div.innerHTML = divTpl;

            // Добавляем новый узел
            parrentObject.lastElementChild.before(div);
            // Возвращаем false, чтобы не было перехода по сслыке
            return false;
        }

        function addOrganisation(parrentObject) {
            var fieldset = document.createElement("fieldset");
            fieldset.setAttribute('name', 'organisation');
            // Добавляем HTML-контент с пом. свойства innerHTML
            let divTpl = `
                <legend></legend>
                <input type="text" placeholder="Название организации" title="Название организации" size="60" maxlength="50" name="orgName" value="" />
                <a href="" onclick="eraseWorkPeriod(parentElement); return false;" alt="Удалить организацию" title="Удалить организацию">
                <img src="png/Error.png"></a><br><br>
                <a href="#" alt="Перейти на сайт" title="Перейти на сайт" onclick="window.open(nextElementSibling.value, '_blank'); return false;"><img src="png/internet24.png"></a>
                <input type="text"  title="URL" placeholder="URL страницы организации в Интернете" size="60" maxlength="50" name="orgUrl" value="" />
                <br>
                <p style="display:inline">Периоды:</p>
                <a href="#" name = "addWorkPeriodButton" alt="Добавить период" title="Добавить период" onclick="addWorkPeriod(parentElement); return false;"><img src="png/+.png"></a>
                        <div name="divWorkPeriod">
                            <input type="date" name="startPeriod" value="" />
                        -
                            <input type="date" name="endPeriod" value="" />
                        :
                        <input type="text" name="work" size="80" value="" />
                        <a href = "#" onclick="eraseWorkPeriod(parentElement); return false;" alt="Удалить период" title="Удалить период"><img src="png/Error.png"></a><br>
                        </div>
                    <hr>
            `;
            fieldset.innerHTML = divTpl;
            let ogranisations = parrentObject.querySelectorAll('fieldset[name="organisation"]');
            if (ogranisations.length == 0){
                parrentObject.append(fieldset);
            }else{
            let lastOrganisation = ogranisations[ogranisations.length-1];
            lastOrganisation.after(fieldset);
            }
            return false;
        }

        function eraseWorkPeriod(parrentForm) {
            parrentForm.remove();
            return false;
        }

        function enabledFormOrganisation(parrentForm) {
            parrentForm.style.backgroundColor ='beige';
            for (let fieldset of parrentForm.children) {
                fieldset.style.display = "";
                if (fieldset.type === "fieldset" && fieldset.name === "organisation") {
                    for (let input of fieldset.children) {
                        input.style.display = "";
                        //console.log('input: ' + input + '; name: ' + input.getAttribute("name"));
                        if (input.disabled == true) {
                            input.disabled = false
                        }
                        if (input.getAttribute('name') === "divWorkPeriod") {

                            for (let wp of input.children) {
                                wp.style.display = "";
                                wp.disabled = false
                            }
                        }
                    }
                }
            }
            return false;
        }

        function saveOrganisationsToJson(patternObject, organosationsType) {
            let organizations = new Array();
            console.log('1');
            for (let fieldset of patternObject.children) { // patternObject - <fieldset id="organisations">
                if (fieldset.type === "fieldset" && fieldset.getAttribute('name') === "organisation") {
                    let org = new Object();
                    let workPeriods = new Array();
                    let workPeriod = new Object();
                    let start_Date = new Object();
                    let end_Date = new Object();
                    let arrStart = new Array();
                    let arrEnd = new Array();

                    for (let orgField of fieldset.children) {

                        switch (orgField.getAttribute('name')) {
                            case "orgName":
                                org.title = orgField.value;
                                break;
                            case "orgUrl":
                                org.homePage = {"url": orgField.value,};
                                break;
                            case "divWorkPeriod":
                                for (let wpField of orgField.children) {
                                    workPeriod = new Object();
                                    arrStart = new Array();
                                    arrEnd = new Array();

                                    switch (wpField.getAttribute('name')) {
                                        case "startPeriod":
                                            arrStart = wpField.value.toString().split('-');
                                            start_Date = new Object();
                                            start_Date.day = arrStart[2];
                                            start_Date.month = arrStart[1];
                                            start_Date.year = arrStart[0];
                                            //  console.log(start_Date);
                                            break;
                                        case "endPeriod":
                                            arrEnd = wpField.value.toString().split('-');
                                            end_Date = new Object();
                                            end_Date.day = arrEnd[2];
                                            end_Date.month = arrEnd[1];
                                            end_Date.year = arrEnd[0];
                                            //   console.log(end_Date);
                                            break;
                                        case "work":
                                            workPeriod.startDate = start_Date;
                                            workPeriod.endDate = end_Date;
                                            workPeriod.description = wpField.value;
                                            workPeriods.push(workPeriod);
                                            break;
                                    }
                                }
                        }
                    }
                    org.workPeriods = workPeriods
                    organizations.push(org);
                }
            }
            let jsonString;
            switch (organosationsType) {
                case "EXPERIENCE":
                    let EXPERIENCE = new Object;
                    EXPERIENCE.organizations = organizations;
                    jsonString = JSON.stringify(EXPERIENCE);
                    document.getElementById("idExperienceJson").value = jsonString;
                    document.getElementById("experience").submit();
                    //document.write(document.getElementById("idExperienceJson").value);
                    break;
                case "EDUCATION":
                    let EDUCATION = new Object;
                    EDUCATION.organizations = organizations;
                    jsonString = JSON.stringify(EDUCATION);
                    document.getElementById("idEducationJson").value = jsonString;
                    document.getElementById("education").submit();
                    //document.write(document.getElementById("idEducationJson").value);
                    break;
            }
        }

    </script>
</HEAD>

<BODY>

