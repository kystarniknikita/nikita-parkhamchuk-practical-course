create index idx_users_name on users(name);
create index idx_users_surname on users(surname);
create index idx_users_birth_date on users(birth_date);
create index idx_card_info_user_id on card_info(user_id);
create index idx_card_info_holder on card_info(holder);
create index idx_card_info_expiration_date on card_info(expiration_date);