# COMP3005Project2

Author: Khushie Singh

Demo Link: https://www.youtube.com/watch?v=bsiY-N4tPHI 

How to run:
make sure you have installed Java and Driver (I used eclipse so the driver in included in my project folder)
create schema on pgAdmin 
open main file and change url, username, and password if differnt on your device
run program 
Notes: This project was created using eclipse 

Functions: All functions uses connection to create a statement, String query specifys what SQL operation is needed. An extra paremeter Connection con is added to all functions to avoid re-writing "Connection con = DriverManager.getConnection(url, user, password);".
