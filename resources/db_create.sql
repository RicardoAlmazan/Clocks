drop database if exists libreria;
create database libreria;
use libreria;

create table libro (
	ISBN bigint primary key,
    nombre nvarchar(1024) not null,
    autor nvarchar(1024) not null,
    editorial nvarchar(1024) not null,
    precio decimal(16, 2) not null,
    portada nvarchar(1024) not null
);

create table usuario (
	idUsuario int auto_increment primary key,
    ip nvarchar(39) not null
);

create table sesion(
	idSesion int auto_increment primary key,
    fechaInicio datetime not null,
    fechaFin datetime
);

create table pedido (
	idLibro bigint not null,
    idUsuario int not null,
    idSesion int not null,
    fecha datetime not null,
    primary key (idLibro, idUsuario, idSesion),
    foreign key (idLibro) references libro(ISBN),
	foreign key (idUsuario) references usuario(idUsuario),
	foreign key (idSesion) references sesion(idSesion)
);

insert into libro values (8437507219, "Rayuela", "Julio Cortazar", "Fondo de Cultura Económica", 120, "rayuela.jpg");
insert into libro values (6073193025, "Harry Potter y el Prisionero de Azkaban", "J.K. Rowling", "Penguin Random House Grupo Editorial", 329, "harry-potter.jfif");
insert into libro values (6073139110, "Ensayo sobre la ceguera", "José Saramago", "Penguin Random House Grupo Editorial SA de CV", 299, "ensayo-sobre-la-ceguera.jpg");
insert into libro values (6070728793, "Cien años de soledad", "Gabriel García Márquez", "Diana México", 204.68, "cien-anios-de-soledad.jpg");

delimiter //
drop procedure if exists spNuevaSesion//
create procedure spNuevaSesion(in inicioDate datetime)
begin
	insert into sesion(fechaInicio) values (inicioDate);
    select MAX(idSesion) from sesion;
end;//

drop procedure if exists spNuevoLibroCliente//
create procedure spNuevoLibroCliente(in ipCliente nvarchar(39), in libroId bigint, in sesionId int, in fechaPedido datetime)
begin
	declare idPersona int;
    set idPersona = (select idUsuario from usuario where ip = ipCliente);
    if idPersona is null then
		insert into usuario(ip) values (ipCliente);
		set idPersona = (select max(idUsuario) from usuario);
	end if;
    insert into pedido values (libroId, idPersona, sesionId, fechaPedido);
    
    select sesionId;
end;//

delimiter ;