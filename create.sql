CREATE TABLE produtos (
	id bigserial primary key,
	hash uuid,
	nome varchar(255) not null,
	descricao text,
	ean13 varchar(13),
	preco  numeric(13,2) not null,
	quantidade numeric(13,2) not null,
	estoque_min  numeric(13,2) not null,
	dtcreate timestamp,
	dtupdate timestamp,
	lativo boolean
);