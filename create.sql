CREATE TABLE produtos (
	id bigserial primary key,
	hash uuid,
	descricao text,
	nome varchar(255) not null,
	ean13 varchar(13),
	preco  numeric(13,2) not null,
	quantidade numeric(13,2) not null,
	estoque_min  numeric(13,2) not null,
	dtcreate timestamp,
	dtupdate timestamp,
	lativo boolean
);