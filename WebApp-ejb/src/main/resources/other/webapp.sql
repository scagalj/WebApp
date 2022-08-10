/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Stjepan
 * Created: Aug 8, 2022
 */

create table ACLUser(
	id serial primary key,
	userName text not null 
);


create table salesObject (

	id serial primary key,
	salesObjectName text not null,
	disabled boolean not null default false

);


create table contactuser (
	id serial primary key,
	firstname text,
	lastname text,
	email text,
	uuid text,
	uniqueId text,
	parentcontact_id bigint null,
	disabled boolean not null default false
);

create table representative(
	id serial primary key,
	firstname text null,
	lastname text null,
	ceo boolean null default false,
	contactuser_id bigint null,
	constraint fk_contactuser_id_representative FOREIGN key (contactuser_id) references contactuser(id)
);

create table attachment(
	id serial primary key,
	filename text null,
	internalName text null,
	fileDescription text null,
	dataSize bigint null,
	contentType text null,
	contactuser_id bigint null,
	userorder_id bigint null,
	product_id bigint null,
	constraint fk_contactuser_id_attachment FOREIGN key (contactuser_id) references contactuser(id),
	constraint fk_userorder_id_attachment FOREIGN key (userorder_id) references userorder(id),
	constraint fk_product_id_attachment FOREIGN key (product_id) references product(id)
);


create table acluser(
	id serial primary key,
	username text,
	"password" text,
	issuperuser boolean not null default false,
	disabled boolean not null default false
);


create table product (
	id serial primary key,
	productType integer not null,
	"name" text not null,
	price numeric not null,
	quantity integer not null,
	disabled boolean not null default false,
	constraint fk_salesObject_product FOREIGN key (salesObject_id) references salesObject(id)
);


create table userorder(
	id serial primary key,
	contactuser_id bigint null,
	salesobject_id bigint null,
	disabled boolean null default false,
	constraint fk_contactuser_id_userorder FOREIGN key (contactuser_id) references contactuser(id),
	constraint fk_salesobject_id_userorder FOREIGN key (salesobject_id) references salesobject(id)
);


create table payment(
	id serial primary key,
	paymentdate timestamp null,
	userorder_id bigint null,
	amount bigint null,
	constraint fk_userorder_id_payment FOREIGN key (userorder_id) references userorder(id)
);

create table orderitem(
	id serial primary key,
	userorder_id bigint null,
	product_id bigint null,
	quantity integer null,
	discount numeric null,
	price numeric null,
	constraint fk_userorder_id_orderitem FOREIGN key (userorder_id) references userorder(id),
	constraint fk_product_id_orderitem FOREIGN key (product_id) references product(id)
);

create table orderdiscount(
	id serial primary key,
	userorder_id bigint null,
	discount_id bigint null,
	"name" text null,
	amount numeric null,
	type integer null,
	promocode boolean null,
	promocodevalue text null,
	constraint fk_userorder_id_orderitem FOREIGN key (userorder_id) references userorder(id),
	constraint fk_discount_id_orderitem FOREIGN key (discount_id) references discount(id)
);

create table orderitemdiscount(
	id serial primary key,
	orderitem_id bigint null,
	discount_id bigint null,
	"name" text null,
	amount numeric null,
	type integer null,
	constraint fk_orderitem_id_orderitem FOREIGN key (orderitem_id) references orderitem(id),
	constraint fk_discount_id_orderitem FOREIGN key (discount_id) references discount(id)
);




create table discount(
	id serial primary key,
	"name" text null,
	amount bigint null,
	type integer null,
	validfrom timestamp null,
	validto timestamp null,
	promocode boolean  null default false,
	promocodeValue text null,
	priority bigint null
	disabled boolean null default false;
);

create table discount_contactuser(
	contactUsers_id bigint not null,
	discount_id bigint not null,
	CONSTRAINT discount_contactuser_pkey PRIMARY KEY (discount_id, contactUsers_id),
	CONSTRAINT fk_discount_contactuser_discount_id FOREIGN KEY (discount_id) REFERENCES public.discount(id),
	CONSTRAINT fk_discount_contactuser_contactUsers_id FOREIGN KEY (contactUsers_id) REFERENCES public.contactuser(id)
);


create table discount_product(
	products_id bigint not null,
	discount_id bigint not null,
	CONSTRAINT discount_product_pkey PRIMARY KEY (discount_id, products_id),
	CONSTRAINT fk_discount_products_discount_id FOREIGN KEY (discount_id) REFERENCES public.discount(id),
	CONSTRAINT fk_discount_products_products_id FOREIGN KEY (products_id) REFERENCES public.product(id)
);

create table orderrepresentative(
	id serial primary key,
	representative_id bigint not null,
	constraint fk_representative_id_orderrepresentative FOREIGN key (representative_id) references representative(id)
);