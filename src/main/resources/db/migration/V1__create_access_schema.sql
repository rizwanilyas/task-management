CREATE TABLE public.roles (
	id bigserial NOT NULL,
	code varchar(150) NULL,
	description varchar(150) NULL,
	"name" varchar(150) NULL,
	CONSTRAINT roles_pkey PRIMARY KEY (id)
);

CREATE TABLE public.users (
	id bigserial NOT NULL,
	created_by int8 NULL,
	created_date timestamptz(6) NOT NULL,
	updated_by int8 NULL,
	updated_date timestamptz(6) NULL,
	disabled bool NULL,
	email varchar(255) NULL,
	first_name varchar(255) NULL,
	last_name varchar(255) NULL,
	"password" varchar(255) NULL,
	username varchar(255) NULL,
	CONSTRAINT uk_6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email),
	CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6 UNIQUE (username),
	CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE public.user_roles (
	user_id int8 NOT NULL,
	role_id int8 NOT NULL,
	CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id)
);

ALTER TABLE public.user_roles ADD CONSTRAINT fkh8ciramu9cc9q3qcqiv4ue8a6 FOREIGN KEY (role_id) REFERENCES public.roles(id);
ALTER TABLE public.user_roles ADD CONSTRAINT fkhfh9dx7w3ubf1co1vdev94g3f FOREIGN KEY (user_id) REFERENCES public.users(id);


CREATE TABLE public.sessions (
	id bigserial NOT NULL,
	created_by int8 NULL,
	created_date timestamptz(6) NOT NULL,
	updated_by int8 NULL,
	updated_date timestamptz(6) NULL,
	access_token varchar(255) NULL,
	expired_at timestamptz(6) NULL,
	refresh_token varchar(255) NULL,
	revoked bool DEFAULT false NOT NULL,
	user_id int8 NULL,
	CONSTRAINT sessions_pkey PRIMARY KEY (id),
	CONSTRAINT uk_5c0wuuupmbdyshagbed9llly1 UNIQUE (access_token),
	CONSTRAINT uk_5tv2ais75eg4bct8kl44w0ktx UNIQUE (refresh_token)
);

ALTER TABLE public.sessions ADD CONSTRAINT fkruie73rneumyyd1bgo6qw8vjt FOREIGN KEY (user_id) REFERENCES public.users(id);

CREATE TABLE public.tokens (
	id bigserial NOT NULL,
	created_by int8 NULL,
	created_date timestamptz(6) NOT NULL,
	updated_by int8 NULL,
	updated_date timestamptz(6) NULL,
	expired_at timestamptz(6) NULL,
	"token" varchar(255) NULL,
	user_id int8 NULL,
	CONSTRAINT tokens_pkey PRIMARY KEY (id),
	CONSTRAINT uk_na3v9f8s7ucnj16tylrs822qj UNIQUE (token)
);


ALTER TABLE public.tokens ADD CONSTRAINT fk2dylsfo39lgjyqml2tbe0b0ss FOREIGN KEY (user_id) REFERENCES public.users(id);


INSERT INTO public.roles
(id, code, description, "name")
VALUES(1, 'ADMIN', NULL, 'Admin');
INSERT INTO public.roles
(id, code, description, "name")
VALUES(2, 'REGULAR', NULL, 'Regular');

