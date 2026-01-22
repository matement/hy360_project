USE university_payroll;
SET SQL_SAFE_UPDATES = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- We use DELETE IGNORE or just standard deletes. 
-- If a table doesn't exist, the script might stop, so ensure Schema is loaded first.

DELETE FROM `Payment`;
DELETE FROM `Contract`;
DELETE FROM `Child`;
DELETE FROM `Allowences`; 
DELETE FROM `Employee`;
DELETE FROM `Salary_rates`;
DELETE FROM `Department`;
DELETE FROM `Role`;
DELETE FROM `Employment_type`;

-- Reset IDs
ALTER TABLE `Employee` AUTO_INCREMENT = 1;
ALTER TABLE `Payment` AUTO_INCREMENT = 1;
ALTER TABLE `Contract` AUTO_INCREMENT = 1;
ALTER TABLE `Department` AUTO_INCREMENT = 1;
ALTER TABLE `Role` AUTO_INCREMENT = 1;
ALTER TABLE `Employment_type` AUTO_INCREMENT = 1;

SET FOREIGN_KEY_CHECKS = 1;
SET SQL_SAFE_UPDATES = 1;