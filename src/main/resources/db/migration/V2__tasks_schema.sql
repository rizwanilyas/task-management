CREATE TABLE public.tasks (
	id bigserial NOT NULL,
	created_by int8 NULL,
	created_date timestamptz(6) NOT NULL,
	updated_by int8 NULL,
	updated_date timestamptz(6) NULL,
	description varchar(255) NULL,
	due_date timestamp(6) NULL,
	status int4 NULL,
	title varchar(255) NULL,
	assignee_id int8 NULL,
	CONSTRAINT tasks_pkey PRIMARY KEY (id)
);
ALTER TABLE public.tasks ADD CONSTRAINT fkekr1dgiqktpyoip3qmp6lxsit FOREIGN KEY (assignee_id) REFERENCES public.users(id);


CREATE TABLE public.task_comments (
	id bigserial NOT NULL,
	created_by int8 NULL,
	created_date timestamptz(6) NOT NULL,
	updated_by int8 NULL,
	updated_date timestamptz(6) NULL,
	"comments" varchar(255) NULL,
	task_id int8 NULL,
	CONSTRAINT task_comments_pkey PRIMARY KEY (id)
);

ALTER TABLE public.task_comments ADD CONSTRAINT fk9517viwn2geh1gpivj6l9y64u FOREIGN KEY (task_id) REFERENCES public.tasks(id);