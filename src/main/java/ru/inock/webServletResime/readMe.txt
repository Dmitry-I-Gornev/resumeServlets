Для корректной работы приложения, после компиляции артефактов, war-файл должен быть переименован
windows: ren servletResume-1.0-SNAPSHOT.war Resumes.war
*nix: mv servletResume-1.0-SNAPSHOT.war Resumes.war

После этого Resumes.war можно деплоить в tomCat