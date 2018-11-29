create database MusicStore with owner postgres;

create table Plyty (indeks serial Primary Key, tytul varchar (20) not null, wykonawca varchar(30) not null, gatunek varchar(30), 
data_premiery date, wydawnictwo varchar(30), dlugosc Integer);

create table Egzemplarze (indeks serial Primary Key, indeks_plyty Integer references Plyty (indeks), nosnik smallint default 1 not null, 
cena numeric not null, koszt_dostawy numeric not null, liczba_ocen integer not null, srednia_ocena integer not null, rodzaj smallint default 1 not null,
stan Integer not null);

-- create table Wykonawcy (indeks Integer Primary Key, nazwa varchar (30) not null, gatunek varchar(30) not null); -- czy wgl potrzebna ta tabela?

create table Dostawcy (indeks serial Primary Key, nazwa varchar (30) not null, nip varchar(10) not null, nr_telefonu varchar (9) not null);

create table Klienci (indeks serial Primary Key, login varchar (15) not null, haslo varchar(9) not null, imie varchar (20) not null, 
nazwisko varchar(30) not null, email varchar(30));

create table Koszyk (indeks serial Primary Key, indeks_plyty Integer references Plyty (indeks), indeks_klienta Integer references Klienci (indeks));

create table Zamowienia (indeks serial Primary Key, indeks_egzemplarza Integer references Egzemplarze (indeks), 
data_zlozenia date, data_realizacji date);

create table Faktury (indeks serial Primary Key, data_sprzedazy date, wartosc_netto numeric, wartosc_brutto numeric, wartosc_vat numeric,
rodz_dok smallint);

create table Pracownicy (indeks serial Primary Key, login varchar (15) not null, haslo varchar(9) not null);

create table Produkty_Faktur(indeks serial Primary Key, indeks_faktury Integer references faktury (indeks), indeks_egzemplarza Integer references egzemplarze (indeks), sztuk integer not null);


----//////////////////////////////////////
create table test (indeks serial Primary Key, login varchar (15) not null, haslo varchar(9) not null);
alter table klienci alter column indeks set default nextval(1);

select indeks from plyty where lower(tytul) like '%black%';

update faktury set (data_sprzedazy = current_date, wartosc_netto = String.valueOf(0.77 * amount), wartosc_brutto = String.valueOf(amount), wartosc_vat = String.valueOf(0.23 * amount), rodz_dok = 1, id_klienta_dostawcy = Integer.toString(indeksClient) where indeks = " + Integer.toString(invoiceNr)

alter table koszyk add foreign key (indeks_egzemplarza) references egzemplarze (indeks);






















