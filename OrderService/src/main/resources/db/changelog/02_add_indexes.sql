create index idx_items_name on items (name);

create index idx_orders_user_id on orders (user_id);
create index idx_orders_status on orders (status);
create index idx_orders_creation_date on orders (creation_date);

create index idx_order_items_order_id on order_items (order_id);
create index idx_order_items_item_id on order_items (item_id);