            # Todos schema

            # --- !Ups

            CREATE TABLE todos (
                id bigint(20) NOT NULL AUTO_INCREMENT,
                name varchar(50) NOT NULL,
                completed BIT NOT NULL DEFAULT 0,
                PRIMARY KEY (id)
            );

            # --- !Downs

            DROP TABLE todos;