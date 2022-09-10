
-- insert test data if not exist

CREATE OR REPLACE FUNCTION insert_data() RETURNS void AS
    '
    BEGIN
    INSERT INTO public.users (username, password, name, surname, description, enabled)
    VALUES
           (''ivan'', ''$2a$12$Vo5LEE/uPtRs6lbVViZtPeRAUB5S3JguW1R5s8k6phGg9tNMy.fN2'', ''Иван'', ''Иванов'', ''Админ'', true),
           (''kirill'', ''$2a$12$Vo5LEE/uPtRs6lbVViZtPeRAUB5S3JguW1R5s8k6phGg9tNMy.fN2'', ''Кирилл'', ''Воронцов'', ''Простой юзер'', true),
           (''fedya'', ''$2a$12$Vo5LEE/uPtRs6lbVViZtPeRAUB5S3JguW1R5s8k6phGg9tNMy.fN2'', ''Федор'', ''Кукушкин'', ''простой юзер'', true);

    INSERT INTO public.authorities (username, authority)
    VALUES
           (''ivan'', ''ADMIN''),
           (''kirill'', ''USER''),
           (''fedya'', ''USER'');
    END;
    ' LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION select_data() RETURNS integer AS
    '
    BEGIN
    RETURN (SELECT COUNT(*) FROM users);
    END ;
    ' LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE new_start() AS
    '
    BEGIN
        IF  select_data() = 0 THEN
            PERFORM insert_data();
        end if;
    end;
    ' LANGUAGE plpgsql;

CALL new_start();
