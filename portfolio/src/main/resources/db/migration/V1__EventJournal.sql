CREATE TABLE tbl_portfoliodata (
  id				VARCHAR(128)	PRIMARY KEY,
  owner				VARCHAR(256),
  s_type 			VARCHAR(256),
  s_type_version 		VARCHAR(256),
  s_data 			VARCHAR(256),
  s_data_version 		VARCHAR(256),
  s_metadata_value 		VARCHAR(256),
  s_metadata_value_version 	VARCHAR(256),
  s_metadata_op 		VARCHAR(256),
  s_id 				VARCHAR(256)
);