create table products (
  id bigint not null auto_increment,
  number varchar(8) not null unique,
  name varchar(255) not null,
  description text,
  primary key(id)
);

create table items (
  id bigint not null auto_increment,
  number varchar(8) not null unique,
  product_id bigint not null,
  price decimal(10,2) not null,
  primary key(id),
  constraint item_product foreign key(product_id) references products(id)
);

create table payments (
  id bigint not null auto_increment,
  card_type varchar(255) default null,
  card_number varchar(255) default null,
  card_expiry_date varchar(255) default null,
  payment_type varchar(64) not null,
  primary key(id)
);

create table orders (
  id bigint not null auto_increment,
  number varchar(8) not null unique,
  payment_id bigint default null,
  primary key(id),
  constraint order_payment foreign key(payment_id) references payments(id)
);

create table line_items (
  id bigint not null auto_increment,
  item_number varchar(8) not null unique,
  item_unit_price decimal(10,2) not null,
  quantity smallint default null,
  order_id bigint not null,
  order_line smallint default null,
  primary key(id),
  constraint line_item_order foreign key(order_id) references orders(id)
)