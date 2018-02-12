CREATE SEQUENCE public.device_user_id_seq;

CREATE TABLE public.device_user (
  id         INTEGER     NOT NULL DEFAULT nextval('public.device_user_id_seq'),
  first_name VARCHAR(50) NOT NULL,
  last_name  VARCHAR(50) NOT NULL,
  pin        VARCHAR(10),
  active BOOLEAN NULL,
  CONSTRAINT device_user_pk PRIMARY KEY (id)
);


ALTER SEQUENCE public.device_user_id_seq OWNED BY public.device_user.id;

CREATE SEQUENCE public.device_id_seq;

CREATE TABLE public.device (
  id    INTEGER      NOT NULL DEFAULT nextval('public.device_id_seq'),
  ident VARCHAR(100) NOT NULL,
  device_description VARCHAR(100) NULL,
  active BOOLEAN NULL,
  CONSTRAINT device_pk PRIMARY KEY (id)
);


ALTER SEQUENCE public.device_id_seq OWNED BY public.device.id;

CREATE SEQUENCE public.rental_id_seq;

CREATE TABLE public.rental (
  id          INTEGER   NOT NULL DEFAULT nextval('public.rental_id_seq'),
  user_id     INTEGER,
  device_id   INTEGER   NOT NULL,
  rental_time TIMESTAMP NOT NULL,
  return_time TIMESTAMP,
  is_returned BOOLEAN   NOT NULL,
  returner    INTEGER,
  CONSTRAINT rental_pk PRIMARY KEY (id)
);


ALTER SEQUENCE public.rental_id_seq OWNED BY public.rental.id;

ALTER TABLE public.rental
  ADD CONSTRAINT user_rental_fk
FOREIGN KEY (user_id)
REFERENCES public.device_user (id)
ON DELETE NO ACTION
ON UPDATE CASCADE
  NOT DEFERRABLE;

ALTER TABLE public.rental
  ADD CONSTRAINT device_user_rental_fk
FOREIGN KEY (returner)
REFERENCES public.device_user (id)
ON DELETE NO ACTION
ON UPDATE CASCADE
  DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE public.rental
  ADD CONSTRAINT device_rental_fk
FOREIGN KEY (device_id)
REFERENCES public.device (id)
ON DELETE NO ACTION
ON UPDATE CASCADE
  NOT DEFERRABLE;