CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `transaction` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `earning` decimal(19,2) DEFAULT NULL,
  `field1` varchar(255) DEFAULT NULL,
  `field2` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_transaction_user_id FOREIGN KEY (`user_id`) REFERENCES user(`id`)
);

INSERT INTO `user` (`address`,`date`,`name`) VALUES ('Address 1','2021-08-24 12:01:00','Ana');
INSERT INTO `user` (`address`,`date`,`name`) VALUES ('Address 2','2021-08-24 12:02:00','Peter');
INSERT INTO `transaction` (`date`,`earning`,`field1`,`field2`,`user_id`) VALUES ('2021-07-01 12:01:00',11.1,'Field 1','Field 2',1);
INSERT INTO `transaction` (`date`,`earning`,`field1`,`field2`,`user_id`) VALUES ('2021-07-01 12:02:00',12.1,'Field 1','Field 2',1);
INSERT INTO `transaction` (`date`,`earning`,`field1`,`field2`,`user_id`) VALUES ('2021-07-03 12:03:00',13.1,'Field 1','Field 2',1);
INSERT INTO `transaction` (`date`,`earning`,`field1`,`field2`,`user_id`) VALUES ('2021-07-03 12:04:00',14.1,'Field 1','Field 2',1);
INSERT INTO `transaction` (`date`,`earning`,`field1`,`field2`,`user_id`) VALUES ('2021-07-10 12:05:00',15.1,'Field 1','Field 2',1);
INSERT INTO `transaction` (`date`,`earning`,`field1`,`field2`,`user_id`) VALUES ('2021-07-11 12:06:00',16.1,'Field 1','Field 2',1);
INSERT INTO `transaction` (`date`,`earning`,`field1`,`field2`,`user_id`) VALUES ('2021-07-11 12:07:00',17.1,'Field 1','Field 2',1);
INSERT INTO `transaction` (`date`,`earning`,`field1`,`field2`,`user_id`) VALUES ('2021-07-15 12:08:00',18.1,'Field 1','Field 2',1);
INSERT INTO `transaction` (`date`,`earning`,`field1`,`field2`,`user_id`) VALUES ('2021-07-20 12:09:00',19.1,'Field 1','Field 2',1);
INSERT INTO `transaction` (`date`,`earning`,`field1`,`field2`,`user_id`) VALUES ('2021-07-21 12:10:00',20.1,'Field 1','Field 2',1);
INSERT INTO `transaction` (`date`,`earning`,`field1`,`field2`,`user_id`) VALUES ('2021-07-22 12:11:00',21.1,'Field 1','Field 2',1);
INSERT INTO `transaction` (`date`,`earning`,`field1`,`field2`,`user_id`) VALUES ('2021-07-25 12:12:00',22.1,'Field 1','Field 2',1);
INSERT INTO `transaction` (`date`,`earning`,`field1`,`field2`,`user_id`) VALUES ('2021-08-02 12:13:00',23.1,'Field 1','Field 2',1);
INSERT INTO `transaction` (`date`,`earning`,`field1`,`field2`,`user_id`) VALUES ('2021-08-03 12:14:00',24.1,'Field 1','Field 2',1);
INSERT INTO `transaction` (`date`,`earning`,`field1`,`field2`,`user_id`) VALUES ('2021-08-04 12:15:00',25.1,'Field 1','Field 2',1);
INSERT INTO `transaction` (`date`,`earning`,`field1`,`field2`,`user_id`) VALUES ('2021-08-10 12:16:00',26.1,'Field 1','Field 2',1);
INSERT INTO `transaction` (`date`,`earning`,`field1`,`field2`,`user_id`) VALUES ('2021-08-15 12:17:00',27.1,'Field 1','Field 2',1);
INSERT INTO `transaction` (`date`,`earning`,`field1`,`field2`,`user_id`) VALUES ('2021-08-15 12:18:00',28.1,'Field 1','Field 2',1);
INSERT INTO `transaction` (`date`,`earning`,`field1`,`field2`,`user_id`) VALUES ('2021-08-15 12:19:00',29.1,'Field 1','Field 2',1);
INSERT INTO `transaction` (`date`,`earning`,`field1`,`field2`,`user_id`) VALUES ('2021-08-20 12:20:00',30.1,'Field 1','Field 2',1);
INSERT INTO `transaction` (`date`,`earning`,`field1`,`field2`,`user_id`) VALUES ('2021-06-20 12:21:00',31.1,'Field 1','Field 2',1);
INSERT INTO `transaction` (`date`,`earning`,`field1`,`field2`,`user_id`) VALUES ('2021-06-10 12:22:00',32.1,'Field 1','Field 2',1);
INSERT INTO `transaction` (`date`,`earning`,`field1`,`field2`,`user_id`) VALUES ('2021-06-05 12:23:00',33.1,'Field 1','Field 2',1);
INSERT INTO `transaction` (`date`,`earning`,`field1`,`field2`,`user_id`) VALUES ('2021-06-05 12:24:00',34.1,'Field 1','Field 2',2);
