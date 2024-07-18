INSERT INTO public.users
(id, created_by, created_date, updated_by, updated_date, disabled, email, first_name, last_name, "password", username)
VALUES(1, NULL, '2024-07-19 01:37:50.954', NULL, '2024-07-19 01:37:50.954', false, 'rzwnilyas@gmail.com', 'Rizwan', 'Ilyas', '$2a$10$.R3cnbNjz8rxZZtzidwCHuzyKfAj.otA7SY1TqnxnrMZgE0nI8RMK', 'admin');


INSERT INTO public.user_roles
(user_id, role_id)
VALUES(1, 1);