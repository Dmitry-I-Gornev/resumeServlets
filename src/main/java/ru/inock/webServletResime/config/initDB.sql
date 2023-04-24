CREATE ROLE inock WITH
	LOGIN
	SUPERUSER
	CREATEDB
	CREATEROLE
	INHERIT
	NOREPLICATION
	CONNECTION LIMIT -1
	PASSWORD 'xxxxxx';
============================

CREATE TABLE webapp.public.resume
(
uuid CHAR(36) PRIMARY KEY NOT NULL,
full_name TEXT NOT NULL
);

CREATE TABLE webapp.public.contact
(
id SERIAL PRIMARY KEY,
resume_uuid CHAR(36) NOT NULL
REFERENCES public.resume(uuid) ON DELETE CASCADE ON UPDATE CASCADE,
type TEXT NOT NULL,
value TEXT NOT NULL
);

CREATE UNIQUE INDEX index_uuid_type
    ON public.contact USING btree
    (resume_uuid ASC NULLS LAST, type ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE TABLE workPeriod
(
    id SERIAL PRIMARY KEY,
    organisation_uuid integer NOT NULL
        REFERENCES public.organisation(id) ON DELETE CASCADE ON UPDATE CASCADE,
    start_date TEXT NOT NULL,
    end_date TEXT NOT NULL,
    description TEXT NOT NULL
);

CREATE TABLE section
(
    id SERIAL PRIMARY KEY,
    resume_uuid CHAR(36) NOT NULL
        REFERENCES public.resume(uuid) ON DELETE CASCADE ON UPDATE CASCADE,

    type TEXT NOT NULL,
    content TEXT NOT NULL
);

CREATE UNIQUE INDEX index_section
    ON public.section USING btree
    (resume_uuid ASC NULLS LAST, type ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE TABLE webapp.public.organisation
(
    id SERIAL PRIMARY KEY,
    resume_uuid CHAR(36) NOT NULL
        REFERENCES public.resume(uuid) ON DELETE CASCADE ON UPDATE CASCADE,
    type TEXT NOT NULL,
    title TEXT NOT NULL,
    description TEXT,
    link_url TEXT,
    link_name TEXT
);

CREATE TABLE webapp.public.users
(
    id SERIAL PRIMARY KEY,
    user_login TEXT NOT NULL,
    user_password  TEXT NOT NULL,
    user_role TEXT NOT NULL
);



INSERT INTO contact (resume_uuid,"type","value")
VALUES ('d745a2fa-c370-403a-a5b5-02aa7c381e8a','HOME_PHONE','123456');

INSERT INTO contact (resume_uuid,"type","value")
VALUES ('d745a2fa-c370-403a-a5b5-02aa7c381e8a','MAIL','r1@mail.ru');

SELECT * FROM resume r LEFT JOIN contact c ON c.resume_uuid = r.uuid
WHERE r.uuid = '179d9be4-ec5c-416e-a306-c14d25f4bbe7'
ORDER BY r.uuid
