CREATE TABLE domain (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  ip_address varchar(255)  NOT NULL ,
  redirection_url varchar(128) DEFAULT NULL,
  hostname varchar(256)  NOT NULL,
  PRIMARY KEY (id),
  INDEX domain_ind (id)
) AUTO_INCREMENT=1;

CREATE TABLE scan_details (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  domain_id bigint(20) NOT NULL,
  website_title varchar(255)  NOT NULL,
  website_body LONGTEXT,
  img_count bigint(100),
  links_count bigint(100),
  status varchar(15) NOT NULL,
  submitted_on datetime NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_domain FOREIGN KEY (domain_id)
  REFERENCES domain(id),
  INDEX sc_ind (id),
  INDEX sc_domain_id (domain_id)
);
