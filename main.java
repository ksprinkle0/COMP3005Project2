import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;


public class main {
	
	/*
	 * https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
	 */
	
	////////////////////////////////////////////////////////
	// Ask the user to register or login as a member	  //
	// trainer, or administrative staff                   //
	////////////////////////////////////////////////////////
	
	public static void startApp(Connection con) throws SQLException, ParseException {
		
		//welcome and prompt user
		System.out.println("|Health and Fitness Club Management System|");
		System.out.println();
		System.out.println("select an opion (based on the numbers provided)");
		System.out.println("1. Member Login");
		System.out.println("2. Trainer Login");
		System.out.println("3. Administrative Staff Login");
		System.out.println("4. Register!");
		System.out.println();
		
		Scanner keyboard = new Scanner(System.in);
		String input = keyboard.next();
		
		if(input.equals("1")) {
			//System.out.println("im in option 1");
			boolean login = false;
			while (!login) {
				System.out.println();
		        System.out.println("Enter your username:");
		        String username = keyboard.next();
		        System.out.println("Enter your password:");
		        String password = keyboard.next();

		        if (validateMemberLogin(con, username, password)) {
		            System.out.println("Welcome!");
		            login = true;
		            memFunctions(con, username);
		        } else {
		            System.out.println("Incorrect username or password. Please try again.");
		        }
		    }
			
		}else if(input.equals("2")) {
			//System.out.println("im in option 2");
			boolean login = false;
		    while (!login) {
		    	System.out.println();
		        System.out.println("Enter your trainer code:");
		        String trainerCode = keyboard.next();

		        if (validateTrainerLogin(con, trainerCode)) {
		            System.out.println("Welcome!");
		            login = true;
		            trainerFunctions(con, trainerCode);
		        } else {
		            System.out.println("Incorrect trainer code. Please try again.");
		        }
		    }
		    
		}else if(input.equals("3")) {
			//System.out.println("im in option 3");
			boolean login = false;
		    while (!login) {
		    	System.out.println();
		        System.out.println("Enter your admin code:");
		        String adminCode = keyboard.next();

		        if (validateAdminLogin(con, adminCode)) {
		            System.out.println("Welcome!");
		            login = true;
		            adminFunctions(con, adminCode);
		        } else {
		            System.out.println("Incorrect admin code. Please try again.");
		        }
		    }
		}else if(input.equals("4")) {
			//System.out.println("im in option 4");
			System.out.println("Welcome to the member registration page!");
		    System.out.println("*** Get your membership for only $100 today, no tax! ***");

		    boolean registered = false;
		    while (!registered) {
		    	System.out.println();
		        System.out.println("Create a username:");
		        String newUser = keyboard.next();
		        
		        // Check if the username already exists in the Member table else create user
		        if (checkIfUsernameExists(con, newUser)) {
		            System.out.println("This username already exists. Try again.");
		        } else {
		            System.out.println("Create a password:");
		            String newPass = keyboard.next();
		            System.out.println("Enter your name:");
		            String newName = keyboard.next();
		            System.out.println("Enter your email:");
		            String newEmail = keyboard.next();
		            System.out.println("Enter your age:");
		            int newAge = keyboard.nextInt();

		            // Generate a unique 4-digit member ID
		            Random random = new Random();
		            int memberId = 0;
		            while (memberIDExists(con, memberId)){
		            	memberId = random.nextInt(9000) + 1000;
		            }

		            // Insert into Member table
		            String query = "INSERT INTO Member (memberID, username, password, name, email, age) VALUES (?, ?, ?, ?, ?, ?)";
		            PreparedStatement p = con.prepareStatement(query);
	                p.setInt(1, memberId);
	                p.setString(2, newUser);
	                p.setString(3, newPass);
	                p.setString(4, newName);
	                p.setString(5, newEmail);
	                p.setInt(6, newAge);
	                p.executeUpdate();
		            
		            // Insert into MemberBilling table
		            String billingQuery = "INSERT INTO MemberBilling (memberID, member_name, bill_type, cost, paid) VALUES (?, ?, ?, ?, ?)";
		            PreparedStatement p2 = con.prepareStatement(billingQuery);
		            p2.setInt(1, memberId);
	                p2.setString(2, newName);
	                p2.setString(3, "membership fee");
	                p2.setInt(4, 100); 
	                p2.setBoolean(5, false); 
	                p2.executeUpdate();
	                
	                String exerciseRoutine = "Not set";
	                String fitnessGoal = "Not set";
	                int weight = 0;
	                int steps = 0;
	                int caloriesBurnt = 0;
	                int activity = 0;

	                // Insert into MemberDashboard table
	                String query3 = "INSERT INTO MemberDashboard (memberID, exercise_routine, fitness_goal, weight, steps, calories_burnt, activity) VALUES (?, ?, ?, ?, ?, ?, ?)";
	                PreparedStatement p3 = con.prepareStatement(query3);
	                p3.setInt(1, memberId);
	                p3.setString(2, exerciseRoutine);
	                p3.setString(3, fitnessGoal);
	                p3.setInt(4, weight);
	                p3.setInt(5, steps);
	                p3.setInt(6, caloriesBurnt);
	                p3.setInt(7, activity);
	                p3.executeUpdate();

		            System.out.println("Registration successful!");
		            registered = true;
		        }
		    }
		}else {
			System.out.println("invalid input :( restart the system");
		}
			
	}
	
	
	////////////////////////////////////////////////////////
	// function used to see if members login info is      //
    // correct; knowledge of prepared statements comes    //
	// Oracle java documentation                          //
    ////////////////////////////////////////////////////////
	
    public static boolean validateMemberLogin(Connection con, String username, String password) throws SQLException {
        String query = "SELECT * FROM Member WHERE username = ? AND password = ?";
        PreparedStatement p = con.prepareStatement(query);
        p.setString(1, username);
        p.setString(2, password);
        ResultSet rs = p.executeQuery();
        return rs.next(); 
    }
    
	////////////////////////////////////////////////////////
	// function used to see if trainer login info is      //
	// correct                                            //
	////////////////////////////////////////////////////////
    
    public static boolean validateTrainerLogin(Connection con, String trainerCode) throws SQLException {
        String query = "SELECT * FROM Trainer WHERE code = ?";
        PreparedStatement p = con.prepareStatement(query);
        p.setString(1, trainerCode);
        ResultSet rs = p.executeQuery();
        return rs.next(); 
    }
    
	////////////////////////////////////////////////////////
	// function used to see if admin login info is        //
	// correct                                            //
	////////////////////////////////////////////////////////

    public static boolean validateAdminLogin(Connection con, String adminCode) throws SQLException {
        String query = "SELECT * FROM Admin WHERE code = ?";
        PreparedStatement p = con.prepareStatement(query);
        p.setString(1, adminCode);
        ResultSet rs = p.executeQuery();
        return rs.next(); 
    }
    
	////////////////////////////////////////////////////////
	// function used to see if username exists            //
	////////////////////////////////////////////////////////
    
    public static boolean checkIfUsernameExists(Connection con, String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM Member WHERE username = ?";
        PreparedStatement p = con.prepareStatement(query);
        p.setString(1, username);
        ResultSet rs = p.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        return count > 0;
    }
    
	////////////////////////////////////////////////////////
	// function used to see if membersID exits            //
	////////////////////////////////////////////////////////
    
    public static boolean memberIDExists(Connection con, int memberId) throws SQLException {
        String query = "SELECT COUNT(*) FROM Member WHERE memberID = ?";
        PreparedStatement p = con.prepareStatement(query);
        p.setInt(1, memberId);
        ResultSet rs = p.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        return count > 0;
    }
	
	////////////////////////////////////////////////////////
	// function used to get memberID                      //
	////////////////////////////////////////////////////////
    
    public static int getMemberID(Connection con, String username) throws SQLException {
    	int memberID = 0; 
       
        String query = "SELECT memberID FROM Member WHERE username = ?";
        PreparedStatement p = con.prepareStatement(query);
        p.setString(1, username);
        ResultSet rs = p.executeQuery();
        	if (rs.next()) {
                memberID = rs.getInt("memberID");
            }
        return memberID;
    }
    
	////////////////////////////////////////////////////////
	// function used to get member_name                   //
	////////////////////////////////////////////////////////

    
    public static String getMemberName(Connection con, String username) throws SQLException {
        String memberName = "blank"; 
        
        String query = "SELECT name FROM member WHERE username = ?";
        PreparedStatement p = con.prepareStatement(query);
        p.setString(1, username);
        ResultSet rs = p.executeQuery();
        
        if (rs.next()) {
            memberName = rs.getString("name");
        }
        
        return memberName;
    }
    
	////////////////////////////////////////////////////////
	// function used to allow user to manage thier 		  //
    // profiles, display dashboard, and manage thier      //
	// schedules, or logout                               //
    ////////////////////////////////////////////////////////
	
	public static void memFunctions(Connection con, String username) throws SQLException { 
		boolean login = true;
		while (login) {
		    System.out.println();
		    System.out.println("Would you like to:");
		    System.out.println("1. Manage your profile");
		    System.out.println("2. Display your dashboard");
		    System.out.println("3. Manage your schedule");
		    System.out.println("4. Pay your Bills");
		    System.out.println("5. Logout");
		    System.out.println();

		    Scanner keyboard = new Scanner(System.in);
		    String memOption = keyboard.next();

		    if (memOption.equals("1")) {
		    	 profileLoop:
		    	 while (true) {
	    	        System.out.println();
	    	        System.out.println("Select which profile item you would like to edit");
	    	        System.out.println("1. Age");
	    	        System.out.println("2. Email");
	    	        System.out.println("3. Fitness Goal");
	    	        System.out.println("4. Weight");
	    	        System.out.println("5. Steps");
	    	        System.out.println("6. Calories Burnt");
	    	        System.out.println("7. Minutes of Activity");
	    	        System.out.println("8. <- Back");
	    	        System.out.println();
	    	        int profileOption = keyboard.nextInt();
	    	        keyboard.nextLine();

	    	        switch (profileOption) {
	                case 1:
	                	System.out.println("Enter your new age:");
	                    int newAge = keyboard.nextInt();
	                    String query1 = "UPDATE Member SET age = ? WHERE username = ?";
	                    PreparedStatement p1 = con.prepareStatement(query1);
	                    p1.setInt(1, newAge);
	                    p1.setString(2, username);
	                    p1.executeUpdate();
	                    System.out.println("Update Complete");
	                    break;
	                case 2:
	                	System.out.println("Enter your new email:");
	                    String newEmail = keyboard.next();
	                    String query2 = "UPDATE Member SET email = ? WHERE username = ?";
	                    PreparedStatement p2 = con.prepareStatement(query2);
	                    p2.setString(1, newEmail);
	                    p2.setString(2, username);
	                    p2.executeUpdate();
	                    System.out.println("Update Complete");
	                    break;
	                case 3:
	                	System.out.println("Enter your new fitness goal:");
	                    String newFitnessGoal = keyboard.nextLine().trim();
	                    String query3 = "UPDATE MemberDashboard SET fitness_goal = ? WHERE memberID = ?";
	                    PreparedStatement p3 = con.prepareStatement(query3);
	                    p3.setString(1, newFitnessGoal);
	                    p3.setInt(2, getMemberID(con, username));
	                    p3.executeUpdate();
	                    System.out.println("Update Complete");
	                    break;
	                case 4:
	                	System.out.println("Enter your new weight:");
	                    int newWeight = keyboard.nextInt();
	                    String query4 = "UPDATE MemberDashboard SET weight = ? WHERE memberID = ?";
	                    PreparedStatement p4 = con.prepareStatement(query4);
	                    p4.setInt(1, newWeight);
	                    p4.setInt(2, getMemberID(con, username));
	                    p4.executeUpdate();
	                    System.out.println("Update Complete");
	                    break;
	                case 5:
	                	System.out.println("Enter your new number of steps:");
	                    int newSteps = keyboard.nextInt();
	                    String query5 = "UPDATE MemberDashboard SET steps = ? WHERE memberID = ?";
	                    PreparedStatement p5 = con.prepareStatement(query5);
	                    p5.setInt(1, newSteps);
	                    p5.setInt(2, getMemberID(con, username));
	                    p5.executeUpdate();
	                    System.out.println("Update Complete");
	                    break;
	                case 6:
	                	System.out.println("Enter your new calories burnt:");
	                    int newCalories = keyboard.nextInt();
	                    String query6 = "UPDATE MemberDashboard SET calories_burnt = ? WHERE memberID = ?";
	                    PreparedStatement p6 = con.prepareStatement(query6);
	                    p6.setInt(1, newCalories);
	                    p6.setInt(2, getMemberID(con, username));
	                    p6.executeUpdate();
	                    System.out.println("Update Complete");
	                    break;
	                case 7:
	                	System.out.println("Enter your new minutes of activity:");
	                    int newActivity = keyboard.nextInt();
	                    String query7 = "UPDATE MemberDashboard SET activity = ? WHERE memberID = ?";
	                    PreparedStatement p7 = con.prepareStatement(query7);
	                    p7.setInt(1, newActivity);
	                    p7.setInt(2, getMemberID(con, username));
	                    p7.executeUpdate();
	                    System.out.println("Update Complete");
	                    break;                   
	                case 8:
	                    break profileLoop; // exit :)
	                default: 
	                    System.out.println("Invalid input. Please try again.");
	                    break;
	    	        }
	    	    }
		    	 
		    } else if (memOption.equals("2")) {
		    	//first display dashboard
		    	String query8 = "SELECT * FROM MemberDashboard WHERE memberID = ?";
		        PreparedStatement p8 = con.prepareStatement(query8);
		        p8.setInt(1, getMemberID(con, username));
		        ResultSet rs = p8.executeQuery();
		        
		        while (rs.next()) {
		            String fitnessGoal = rs.getString("fitness_goal");
		            int weight = rs.getInt("weight");
		            int steps = rs.getInt("steps");
		            int caloriesBurnt = rs.getInt("calories_burnt");
		            int activity = rs.getInt("activity");

		            System.out.println("|--- Your Dashboard ---|");
		            System.out.println("Fitness Goal: " + fitnessGoal);
		            System.out.println("Weight: " + weight);
		            System.out.println("Steps: " + steps);
		            System.out.println("Calories Burnt: " + caloriesBurnt);
		            System.out.println("Minutes of Activity: " + activity);
		        }
		        
		        //then display schedule
		        System.out.println("---- Schedule ----");
		    	System.out.println();
		    	
		    	String classQuery = "SELECT * FROM MemberClassSchedule WHERE memberID = ?";
		        PreparedStatement p9 = con.prepareStatement(classQuery);
		        p9.setInt(1, getMemberID(con, username));
		        ResultSet classRs = p9.executeQuery();

		        System.out.println("Class Schedule:");
	            while (classRs.next()) {
	                // Display class schedule details
	                int classID = classRs.getInt("classID");
	                String className = classRs.getString("class_name");
	                Date classDay = classRs.getDate("class_day");
	                Time classTime = classRs.getTime("class_time");

	                System.out.println("Class ID: " + classID);
	                System.out.println("Class Name: " + className);
	                System.out.println("Class Day: " + classDay);
	                System.out.println("Class Time: " + classTime);
	                System.out.println();
	            }
		        
		        
		        String sessionQuery = "SELECT * FROM MemberSessionSchedule WHERE memberID = ?";
		        PreparedStatement p10 = con.prepareStatement(sessionQuery);
		        p10.setInt(1, getMemberID(con, username));
		        ResultSet sessionRs = p10.executeQuery();

	            System.out.println("Session Schedule:");
	            while (sessionRs.next()) {
	                // Display session schedule details
	                int sessionID = sessionRs.getInt("sessionID");
	                String trainerName = sessionRs.getString("trainer_name");
	                Date sessionDay = sessionRs.getDate("session_day");
	                Time sessionTime = sessionRs.getTime("session_time");

	                System.out.println("Session ID: " + sessionID);
	                System.out.println("Trainer Name: " + trainerName);
	                System.out.println("Session Day: " + sessionDay);
	                System.out.println("Session Time: " + sessionTime);
	                System.out.println();
	            }
		       
		    } else if (memOption.equals("3")) {
		    	System.out.println("|Member Schedule Management|");
                managementLoop:
		        while (true) {
		            System.out.println();
		            System.out.println("What would you like to do?");
		            System.out.println("1. View available personal training sessions");
		            System.out.println("2. View available fitness classes");
		            System.out.println("3. Register for a fitness class");
		            System.out.println("4. Schedule a personal training session");
		            System.out.println("5. Cancel a personal training session");
		            System.out.println("6. <- Back");
		            String scheduleOption = keyboard.next();

		            switch (scheduleOption) {
		                case "1":
		                	System.out.println();
		                	String query1 = "SELECT * FROM ManageSessionSchedule WHERE session_availability = TRUE";
		                	PreparedStatement p1 = con.prepareStatement(query1);
		                	ResultSet rs1 = p1.executeQuery();
	                	    while (rs1.next()) {
		                	    System.out.println("Session ID: " + rs1.getInt("sessionID"));
		                	    System.out.println("Session Day: " + rs1.getDate("session_day"));
		                	    System.out.println("Session Time: " + rs1.getTime("session_time"));
		                	    System.out.println("Trainer ID: " + rs1.getInt("trainerID"));
		                	    System.out.println("Trainer Name: " + rs1.getString("trainer_name"));
		                	    System.out.println();
	                	    }
		                	break;
		                	
		                case "2":
		                	System.out.println();
		                	String query2 = "SELECT * FROM ManageClassSchedule";
		                    PreparedStatement p2 = con.prepareStatement(query2);
		                    ResultSet rs2 = p2.executeQuery();
	                        while (rs2.next()) {
	                            System.out.println("Class ID: " + rs2.getInt("classID"));
	                            System.out.println("Class Name: " + rs2.getString("class_name"));
	                            System.out.println("Class Day: " + rs2.getDate("class_day"));
	                            System.out.println("Class Time: " + rs2.getTime("class_time"));
	                            System.out.println();
	                        }
		                    break;
		                    
		                case "3":
		                    System.out.println();
		                    System.out.println("What fitness class would you like to register for? (Enter class ID)");
		                    int classID = keyboard.nextInt();
		               
		                    String query3 = "SELECT * FROM ManageClassSchedule WHERE classID = ?";
		                    PreparedStatement p3 = con.prepareStatement(query3);
		                    p3.setInt(1, classID);
		                    ResultSet classRs = p3.executeQuery();
		                    
		                    if (classRs.next()) {
	                            // Class exists, register the member for the class
	                            String className = classRs.getString("class_name");
	                            Date classDay = classRs.getDate("class_day");
	                            Time classTime = classRs.getTime("class_time");

	                            // Register the member for the class
	                            String query4 = "INSERT INTO MemberClassSchedule (memberID, classID, class_day, class_time, class_name) VALUES (?, ?, ?, ?, ?)";
	                            PreparedStatement p4 = con.prepareStatement(query4);
                                p4.setInt(1, getMemberID(con, username));
                                p4.setInt(2, classID);
                                p4.setDate(3, (java.sql.Date) classDay);
                                p4.setTime(4, classTime);
                                p4.setString(5, className);
                                p4.executeUpdate();
	                            
                                //make an entry to bill member $50
                                String query5 = "INSERT INTO MemberBilling (memberID, member_name, bill_type, cost, paid) VALUES (?, ?, ?, ?, ?)";
                                PreparedStatement p5 = con.prepareStatement(query5);
                                p5.setInt(1, getMemberID(con, username));
                                p5.setString(2, getMemberName(con, username));
                                p5.setString(3, "fitness class");
                                p5.setInt(4, 50);
                                p5.setBoolean(5, false); 
                                p5.executeUpdate();
                                
                                System.out.println("Registration successful");
	                        } else {
	                            System.out.println("Invalid Class ID");
	                        }
		                    break;
		                    
		                case "4":
		                    System.out.println();
		                    System.out.println("What training would you like to add? (Enter session ID)");
		                    int sessionID = keyboard.nextInt();

		                    String query6 = "SELECT * FROM ManageSessionSchedule WHERE sessionID = ?";
		                    PreparedStatement p6 = con.prepareStatement(query6);
		                    p6.setInt(1, sessionID);
		                    ResultSet sessionRs = p6.executeQuery();

		                    if (sessionRs.next()) {
		                        // Session exists, schedule the session for the member
		                        Date sessionDay = sessionRs.getDate("session_day");
		                        Time sessionTime = sessionRs.getTime("session_time");
		                        int trainerID = sessionRs.getInt("trainerID");
		                        String trainerName = sessionRs.getString("trainer_name");

		                        // Schedule the session for the member
		                        String query7 = "INSERT INTO MemberSessionSchedule (memberID, sessionID, session_day, session_time, trainerID, trainer_name) VALUES (?, ?, ?, ?, ?, ?)";
		                        PreparedStatement p7 = con.prepareStatement(query7);
		                        p7.setInt(1, getMemberID(con, username));
		                        p7.setInt(2, sessionID);
		                        p7.setDate(3, (java.sql.Date) sessionDay);
		                        p7.setTime(4, sessionTime);
		                        p7.setInt(5, trainerID);
		                        p7.setString(6, trainerName);
		                        p7.executeUpdate();
		             
		                        // make session not available to others
		                        String updateQuery = "UPDATE ManageSessionSchedule SET session_availability = FALSE WHERE sessionID = ?";
		                        PreparedStatement update = con.prepareStatement(updateQuery);
		                        update.setInt(1, sessionID);
		                        update.executeUpdate();
	                        
	                            // add entry for bill of $30
	                            String billingQuery = "INSERT INTO MemberBilling (memberID, member_name, bill_type, cost, paid) VALUES (?, ?, ?, ?, ?)";
	                            PreparedStatement billing = con.prepareStatement(billingQuery);
	                            billing.setInt(1, getMemberID(con, username));
	                            billing.setString(2, getMemberName(con, username));
	                            billing.setString(3, "training session");
	                            billing.setInt(4, 30);
	                            billing.setBoolean(5, false); 
	                            billing.executeUpdate();
	                            
	                            // add entry into TrainerSchedule
	                            String trainerScheduleQuery = "INSERT INTO TrainerSchedule (session_day, session_time, trainerID, memberID, member_name) VALUES (?, ?, ?, ?, ?)";
	                            PreparedStatement trainerScheduleStatement = con.prepareStatement(trainerScheduleQuery);
	                            trainerScheduleStatement.setDate(1, (java.sql.Date) sessionDay);
	                            trainerScheduleStatement.setTime(2, sessionTime);
	                            trainerScheduleStatement.setInt(3, trainerID);
	                            trainerScheduleStatement.setInt(4, getMemberID(con, username));
	                            trainerScheduleStatement.setString(5, getMemberName(con, username));
	                            trainerScheduleStatement.executeUpdate();
	                            
	                            //pay trainer
	                            String insertTrainerPaymentQuery = "INSERT INTO TrainerPayment (trainerID, sessionID, paycheck, paid) VALUES (?, ?, ?, ?)";
	                            PreparedStatement insertTrainerPaymentStatement = con.prepareStatement(insertTrainerPaymentQuery);
	                            insertTrainerPaymentStatement.setInt(1, trainerID);
	                            insertTrainerPaymentStatement.setInt(2, sessionID);
	                            insertTrainerPaymentStatement.setInt(3, 20); 
	                            insertTrainerPaymentStatement.setBoolean(4, false); 
	                            insertTrainerPaymentStatement.executeUpdate();
	                            
	                            System.out.println("Registration successful");

		                    } else {
		                        System.out.println("Invalid Session ID");
		                    }
		                    break;
		                    
		                case "5":
		                	System.out.println();
		                    System.out.println("What training would you like to cancel? (Enter session ID)");
		                    int removeTraining = keyboard.nextInt();
		                    
		                    String query8 = "SELECT * FROM MemberSessionSchedule WHERE sessionID = ? AND memberID = ?";
		                    PreparedStatement p8 = con.prepareStatement(query8);
		                    p8.setInt(1, removeTraining);
		                    p8.setInt(2, getMemberID(con, username));
		                    ResultSet checkResult = p8.executeQuery();

		                    if (checkResult.next()) {
			                    String query9 = "DELETE FROM MemberSessionSchedule WHERE sessionID = ? AND memberID = ?";
			                    PreparedStatement p9 = con.prepareStatement(query9);
			                    p9.setInt(1, removeTraining);
			                    p9.setInt(2, getMemberID(con, username));
			                    p9.executeUpdate();
			                   
		                        // make session available again
		                        String query10 = "UPDATE ManageSessionSchedule SET session_availability = TRUE WHERE sessionID = ?";
		                        PreparedStatement p10 = con.prepareStatement(query10);
		                        p10.setInt(1, removeTraining);
		                        p10.executeUpdate();
	
		                        // remove bill
		                        String deleteBillingQuery = "DELETE FROM MemberBilling WHERE memberID = ? AND bill_type = ? AND sessionID = ?";
		                        PreparedStatement deleteBilling = con.prepareStatement(deleteBillingQuery);
		                        deleteBilling.setInt(1, getMemberID(con, username));
		                        deleteBilling.setString(2, "training session");
		                        deleteBilling.setInt(3, removeTraining);
		                        
		                        // remove entry from TrainerSchedule
		                        String deleteTrainerScheduleQuery = "DELETE FROM TrainerSchedule WHERE sessionID = ?";
		                        PreparedStatement deleteTrainerScheduleStatement = con.prepareStatement(deleteTrainerScheduleQuery);
		                        deleteTrainerScheduleStatement.setInt(1, removeTraining);
		                        deleteTrainerScheduleStatement.executeUpdate();
		                        
		                        //remove trainer payment
		                        String deleteTrainerPaymentQuery = "DELETE FROM TrainerPayment WHERE sessionID = ?";
		                        PreparedStatement deleteTrainerPaymentStatement = con.prepareStatement(deleteTrainerPaymentQuery);
		                        deleteTrainerPaymentStatement.setInt(1, removeTraining);
		                        deleteTrainerPaymentStatement.executeUpdate();
		                        
		                        System.out.println("Session canceled.");
		                        
		                    } else {
		                        System.out.println("Invalid Session ID");
		                    }
	                        
		                    break;
		                case "6":
		                    // Exit this loop
		                    break managementLoop;
		                default:
		                    System.out.println("Invalid input");
		                    break;
		            }
		        }
		    	
		    } else if (memOption.equals("4")) {
		    	// Get unpaid bills for the member
		        String query = "SELECT SUM(cost) AS total_cost FROM MemberBilling WHERE memberID = ? AND paid = FALSE";
		        PreparedStatement p = con.prepareStatement(query);
		        p.setInt(1, getMemberID(con, username));
		        ResultSet unpaidBillsResult = p.executeQuery();

		        if (unpaidBillsResult.next()) {
		            int totalCost = unpaidBillsResult.getInt("total_cost");

		            if (totalCost > 0) {
		                // Print total amount to pay
		                System.out.println("Your amount to pay is: $" + totalCost);
		                System.out.println("Would you like to proceed with the payment?");
		                System.out.println("1. Yes");
		                System.out.println("2. No");
		                int paymentOption = keyboard.nextInt();

		                if (paymentOption == 1) {
		                    // Update bills to mark as paid
		                    String query2 = "UPDATE MemberBilling SET paid = TRUE WHERE memberID = ? AND paid = FALSE";
		                    PreparedStatement p2 = con.prepareStatement(query2);
		                    p2.setInt(1, getMemberID(con, username));
		                    p2.executeUpdate();
		                    System.out.println("Payment successful. Thank you!");
		         
		                } else if (paymentOption == 2) {
		                    System.out.println("Payment canceled.");
		                } else {
		                    System.out.println("Invalid input");
		                }
		            } else {
		                System.out.println("You have no unpaid bills.");
		            }
		        } 
		        
		        
		    } else if (memOption.equals("5")) {
		        login = false;
		        System.out.println("Bye Bye!.");
		    } else {
		        System.out.println("Invalid input");
		    }

		}
	}
	
	////////////////////////////////////////////////////////
	//function used to get trainerID                     //
	////////////////////////////////////////////////////////
	
	public static int getTrainerID(Connection con, String trainerCode) throws SQLException {
		int trainerID = 0; 
		
		String query = "SELECT trainerID FROM Trainer WHERE code = ?";
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setString(1, trainerCode);
		
		ResultSet rs = pstmt.executeQuery();
		
		if (rs.next()) {
			trainerID = rs.getInt("trainerID");
		}
		
		return trainerID;
	}
	
	////////////////////////////////////////////////////////
	//function used to get trainer name                   //
	////////////////////////////////////////////////////////
	
	public static String getTrainerName(Connection con, String trainerCode) throws SQLException {
	    String trainerName = "blank"; 
	    
	    String query = "SELECT name FROM Trainer WHERE code = ?";
	    PreparedStatement p = con.prepareStatement(query);
	    p.setString(1, trainerCode);
	    ResultSet rs = p.executeQuery();
	    
	    if (rs.next()) {
	        trainerName = rs.getString("name");
	    }
	    
	    return trainerName;
	}
	
	////////////////////////////////////////////////////////
	// function allows trainers to manage thier			  //
    // schedules, view member profiles, or logout          //
    ////////////////////////////////////////////////////////
	
	public static void trainerFunctions(Connection con, String code) throws SQLException {
		Scanner keyboard = new Scanner(System.in);
	    boolean login = false;

	    while (!login) {
	        System.out.println();
	        System.out.println("Would you like to:");
	        System.out.println("1. Manage your schedule");
	        System.out.println("2. View member profile");
	        System.out.println("3. Logout");
	        System.out.println();

	        String trainerOption = keyboard.next();

	        if (trainerOption.equals("1")) {
	            while (true) {
	                System.out.println();
	                System.out.println("What would you like to do:");
	                System.out.println("1. View your schedule");
	                System.out.println("2. Add personal training availability");
	                System.out.println("3. <- Back");

	                String scheduleOption = keyboard.next();
	                
	                if (scheduleOption.equals("1")) {
	                	String query = "SELECT * FROM TrainerSchedule WHERE trainerID = ?";
	                    PreparedStatement p = con.prepareStatement(query);
	                    p.setInt(1, getTrainerID(con, code));
	                    ResultSet sessionRs = p.executeQuery();

	                    System.out.println("Session Schedule:");
	                    while (sessionRs.next()) {
	                        // Display session schedule details
	                    	int sessionID = sessionRs.getInt("sessionID");
	                        String memberName = sessionRs.getString("member_name");
	                        Date sessionDay = sessionRs.getDate("session_day");
	                        Time sessionTime = sessionRs.getTime("session_time");

	                        System.out.println("Session ID: " + sessionID);
	                        System.out.println("Member Name: " + memberName);
	                        System.out.println("Session Day: " + sessionDay);
	                        System.out.println("Session Time: " + sessionTime);
	                        System.out.println();
	                    	
	                	}
	                } else if (scheduleOption.equals("2")) {
	                    System.out.println();
	                    System.out.println("What time are you available?");
	                    String stringAvailability = keyboard.next() + ":00:00";
	                    
	                    Time availability = Time.valueOf(stringAvailability);

	                    String query2 = "SELECT * FROM TrainerAvailability WHERE trainerID = ? AND trainer_name = ? AND hour = ?";
	                    PreparedStatement p2 = con.prepareStatement(query2);
	                    p2.setInt(1, getTrainerID(con, code));
	                    p2.setString(2, getTrainerName(con, code));
	                    p2.setTime(3, availability);
	                    ResultSet checkResult = p2.executeQuery();

	                    if (!checkResult.next()) {
	                        // Add to trainers availibilityy
	                    	String query3 = "INSERT INTO TrainerAvailability (trainerID, trainer_name, hour) VALUES (?, ?, ?)";
	                        PreparedStatement p3 = con.prepareStatement(query3);
	                        p3.setInt(1, getTrainerID(con, code));
	                        p3.setString(2, getTrainerName(con, code));
	                        p3.setTime(3, availability);
	                        p3.executeUpdate();
	                        
	                        System.out.println("Availability added successfully.");
	                    } else {
	                        System.out.println("Availability already exists in your schedule.");
	                    }
	                } else if (scheduleOption.equals("3")) {
	                    break;
	                } else {
	                    System.out.println("Invalid input.");
	                }
	            }
	        } else if (trainerOption.equals("2")) {
	        	System.out.println();
	            System.out.println("Which member's profile would you like to view?");
	            String name = keyboard.next();

	            // see if member exists
	            String query4 = "SELECT * FROM Member WHERE name = ?";
	            PreparedStatement p4 = con.prepareStatement(query4);
	            p4.setString(1, name);
	            ResultSet memberResult = p4.executeQuery();

	            if (memberResult.next()) {
	                
	            	String memberUsername = memberResult.getString("username");
	                String memberEmail = memberResult.getString("email");
	                
	                System.out.println("Member Profile:");
	                System.out.println("Name: " + name);
	                System.out.println("Username: " + memberUsername); 
	                System.out.println("Email: " + memberEmail);
	                
	                System.out.println("Would you like to edit the member's exercise routine? (1. yes or 2. no)");
		            String editOption = keyboard.next();
		            if (editOption.equals("1")) {
		            	System.out.println("Enter the new exercise routine:");
		            	keyboard.nextLine();
			            String newExerciseRoutine = keyboard.nextLine().trim();

			            String query = "UPDATE MemberDashboard SET exercise_routine = ? WHERE memberID = ?";
			            PreparedStatement p = con.prepareStatement(query);
			            p.setString(1, newExerciseRoutine);
			            p.setInt(2, getMemberID(con, memberUsername)); 
			            p.executeUpdate();
			            
			            System.out.println("Update Complete");
		            }
	                
	            } else {
	                System.out.println("Member not found.");
	            }
	            
	       
	        } else if (trainerOption.equals("3")) {
	            System.out.println("Bye Bye!");
	            login = true;
	        } else {
	            System.out.println("Invalid input.");
	        }
	    }
    }
		
	

	////////////////////////////////////////////////////////
	// function allows admins to manage bookings and	  //
    // class schedules, monitor equipment, and process    //
	// payments, or logout                                //
    ////////////////////////////////////////////////////////
	
	public static void adminFunctions(Connection con, String code) throws SQLException, ParseException {
		Scanner keyboard = new Scanner(System.in);
	    boolean login = true;

	    while (login) {
	        System.out.println();
	        System.out.println("Admin Panel:");
	        System.out.println("1. Manage Bookings");
	        System.out.println("2. Monitor Equipment");
	        System.out.println("3. Manage Schedules");
	        System.out.println("4. View Billings and Payments");
	        System.out.println("5. Logout");
	        System.out.println();

	        String adminOption = keyboard.next();

	        if (adminOption.equals("1")) {
	        	boolean booking = true;

	            while (booking) {
	                System.out.println();
	                System.out.println("1. Book a room");
	                System.out.println("2. Cancel a booking");
	                System.out.println("3. <- Back");

	                String bookingOption = keyboard.next();

	                if (bookingOption.equals("1")) {
	                	System.out.println();
	                    System.out.println("What's the name for the booking?");
	                    String bookName = keyboard.next();
	                    System.out.println();
	                    System.out.println("What time would you like to book the room?");
	                    String bookTime = keyboard.next();
	                    Time bookingTime = Time.valueOf(bookTime);
	                    System.out.println();
	                    System.out.println("What day would you like to book the room?");
	                    String stringDate = keyboard.next();
	                    java.util.Date bookDate = null;
	                    bookDate = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);

	                    String query = "SELECT * FROM Bookings WHERE member_name = ? AND time = ? AND day = ?";
	                    PreparedStatement p = con.prepareStatement(query);
	                    p.setString(1, bookName);
	                    p.setTime(2, bookingTime);
	                    p.setDate(3, new java.sql.Date(bookDate.getTime()));
	                    ResultSet availabilityResult = p.executeQuery();

	                    if (!availabilityResult.next()) {
	                        String query2 = "INSERT INTO Bookings (member_name, time, day) VALUES (?, ?, ?)";
	                        PreparedStatement p2 = con.prepareStatement(query2);
	                        p2.setString(1, bookName);
	                        p2.setTime(2, bookingTime);
	                        p2.setDate(3, new java.sql.Date(bookDate.getTime()));
	                        p2.executeUpdate();

	                        System.out.println("Booking added");
	                    } else {
	                        System.out.println("Invalid Input");
	                    }
	                    
	                } else if (bookingOption.equals("2")) {
	                	 System.out.println();
                	     System.out.println("What's the name for the booking?");
                	     String bookName = keyboard.next();
                	     System.out.println();
                	     System.out.println("What time is the booking? (HH:mm:ss)");
                	     String bookTime = keyboard.next();
                	     Time bookingTime = Time.valueOf(bookTime);
                	     System.out.println();
                	     System.out.println("What day is the booking? (yyyy-MM-dd)");
                	     String stringDate = keyboard.next();
                	     java.util.Date bookDate = null;
                	     bookDate = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);

                	     String query3 = "SELECT * FROM Bookings WHERE member_name = ? AND time = ? AND day = ?";
                	     PreparedStatement p3 = con.prepareStatement(query3);
                	     p3.setString(1, bookName);
                	     p3.setTime(2, bookingTime);
                	     p3.setDate(3, new java.sql.Date(bookDate.getTime()));
                	     ResultSet availabilityResult = p3.executeQuery();
 
                	     if (availabilityResult.next()) {
                	        String query4 = "DELETE FROM Bookings WHERE member_name = ? AND time = ? AND day = ?";
                	        PreparedStatement p4 = con.prepareStatement(query4);
                	        p4.setString(1, bookName);
                	        p4.setTime(2, bookingTime);
                	        p4.setDate(3, new java.sql.Date(bookDate.getTime()));
                	        p4.executeUpdate();

                	        System.out.println("Booking canceled");
                	        
                	    } else {
                	        System.out.println("Invalid Input");
                	    }
	                	    
	                } else if (bookingOption.equals("3")) {
		                    booking = false;
	                } else {
	                    System.out.println("Invalid input.");
	                }
	            }
	        } else if (adminOption.equals("2")) {
	        	System.out.println();
	        	System.out.println("Equipment Monitoring:");
	        	String query = "SELECT * FROM Equipment";
	        	PreparedStatement p = con.prepareStatement(query);
	        	ResultSet equipmentResultSet = p.executeQuery();

	        	System.out.println("Equipment ID | Name | Status");
	        	while (equipmentResultSet.next()) {
	        	    String equipmentID = equipmentResultSet.getString("name");
	        	    String name = equipmentResultSet.getString("name");
	        	    String status = equipmentResultSet.getString("status");
	        	    System.out.println(equipmentID + " | " + name + " | " + status);
	        	}

	        	System.out.println();
	        	System.out.println("Would you like to:");
	        	System.out.println("1. Change equipment status");
	        	System.out.println("2. Add maintenance history");
	        	String monitorOption = keyboard.next();

	        	if (monitorOption.equals("1")) {
	        	    System.out.println();
	        	    System.out.println("Which equipment's status would you like to change? (Enter equipment ID)");
	        	    String equipmentID = keyboard.next();
	        	    
	        	    String query2 = "SELECT * FROM Equipment WHERE name = ?";
	        	    PreparedStatement p2 = con.prepareStatement(query2);
	        	    p2.setString(1, equipmentID);
	        	    ResultSet checkResult = p2.executeQuery();
	        	    
	        	    if (checkResult.next()) {
	        	        System.out.println();
	        	        System.out.println("Enter the new status:");
	        	        String newStatus = keyboard.next();
	        	        
	        	        String query3 = "UPDATE Equipment SET status = ? WHERE name = ?";
	        	        PreparedStatement p3 = con.prepareStatement(query3);
	        	        p3.setString(1, newStatus);
	        	        p3.setString(2, equipmentID);
	        	        p3.executeUpdate();
	        	        
	        	        System.out.println("Equipment status updated successfully.");
	        	    } else {
	        	        System.out.println("Equipment not found.");
	        	    }
	        	} else if (monitorOption.equals("2")) {
	        	    System.out.println();
	        	    System.out.println("Enter maintenance history for the equipment:");
	        	    keyboard.nextLine(); 
	        	    String maintenanceHistory = keyboard.nextLine();
	        	    
	        	    System.out.println();
	        	    System.out.println("Enter equipment ID:");
	        	    String equipmentID = keyboard.next();
	        	    
	        	    String query4 = "UPDATE Equipment SET maintenance_history = array_append(maintenance_history, ?) WHERE name = ?";
	        	    PreparedStatement p4 = con.prepareStatement(query4);
	        	    p4.setString(1, maintenanceHistory);
	        	    p4.setString(2, equipmentID);
	        	    p4.executeUpdate();
	        	    
	        	    System.out.println("Maintenance history added successfully.");
	        	} else {
	        	    System.out.println("Invalid option.");
	        	}
	        } else if (adminOption.equals("3")) {
	        	System.out.println();
	            System.out.println("Would you like to:");
	            System.out.println("1. Add a fitness class");
	            System.out.println("2. Add a training session");
	            System.out.println("3. <- Back");

	            String scheduleOption = keyboard.next();

	            if (scheduleOption.equals("1")) {
	                System.out.println();
	                System.out.println("What time would you like to add the class?");
	                String addTime = keyboard.next();
	                Time bookingTime = Time.valueOf(addTime);
	                System.out.println("What day would you like to add the class?");
	                String stringDay = keyboard.next();
	                java.util.Date addDay = null;
                    addDay = new SimpleDateFormat("yyyy-MM-dd").parse(stringDay);
	                System.out.println("What is the class name?");
	                String className = keyboard.next();

	                // Generate a unique class ID
	                Random random = new Random();
	                int classId = random.nextInt(900) + 100;

	                String query = "INSERT INTO ManageClassSchedule (classID, class_name, class_time, class_day) VALUES (?, ?, ?, ?)";
	                PreparedStatement p = con.prepareStatement(query);
	                p.setInt(1, classId); 
	                p.setString(2, className); 
	                p.setTime(3, bookingTime); 
	                p.setDate(4, new java.sql.Date(addDay.getTime())); 
	                p.executeUpdate();
	                
	            } else if (scheduleOption.equals("2")) {
	                System.out.println();
	                System.out.println("Here are the trainers' availability:");

	                String query2 = "SELECT * FROM TrainerAvailability";
	                PreparedStatement p2 = con.prepareStatement(query2);
	                ResultSet resultSet = p2.executeQuery();

	                // Print the results for all trainers
	                System.out.println("Trainers' Availability:");
	                while (resultSet.next()) {
	                    String trainerName = resultSet.getString("trainer_name");
	                    Time availability = resultSet.getTime("hour");
	                    System.out.println("Trainer: " + trainerName + ", Availability: " + availability);
	                }
	                
	                System.out.println();
	                System.out.println("Which trainer? (Enter name)");
	                String addTrainerName = keyboard.next();
	                System.out.println("Enter thier ID");
	                int addTrainerId = keyboard.nextInt();
	                System.out.println("What time would you like to add the session for them?");
	                String addTime2 = keyboard.next();
	                Time bookingTime = Time.valueOf(addTime2);
	                System.out.println("What day would you like to add the session for them?");
	                String stringDay2 = keyboard.next();
	                java.util.Date addDay2 = null;
                    addDay2 = new SimpleDateFormat("yyyy-MM-dd").parse(stringDay2);

	                // Generate a unique session ID
	                Random random = new Random();
	                int sessionId = random.nextInt(900) + 100;

	                String query3 = "INSERT INTO ManageSessionSchedule (sessionID, session_time, session_day, trainerID, trainer_name) VALUES (?, ?, ?, ?, ?)";
	                PreparedStatement p3 = con.prepareStatement(query3);
	                p3.setInt(1, sessionId);
	                p3.setTime(2, bookingTime);
	                p3.setDate(3, new java.sql.Date(addDay2.getTime()));
	                p3.setInt(4, addTrainerId); 
	                p3.setString(5, addTrainerName); 
	                p3.executeUpdate();
	                
	                
	            } else if (scheduleOption.equals("3")) {
	                break;
	            } else {
	                System.out.println("Invalid input.");
	            }
	        } else if (adminOption.equals("4")) {
	        	System.out.println();
	        	System.out.println("|MEMBER BILLING|");
	        	String query = "SELECT * FROM MemberBilling";
	        	PreparedStatement p = con.prepareStatement(query);
	        	ResultSet memberBillingResultSet = p.executeQuery();

	        	while (memberBillingResultSet.next()) {
	        	    int memberID = memberBillingResultSet.getInt("memberID");
	        	    String memberName = memberBillingResultSet.getString("member_name");
	        	    String billType = memberBillingResultSet.getString("bill_type");
	        	    int cost = memberBillingResultSet.getInt("cost");
	        	    boolean paid = memberBillingResultSet.getBoolean("paid");
	        	    
	        	    System.out.println("MemberID: " + memberID + ", Member Name: " + memberName + ", Bill Type: " + billType + ", Cost: " + cost + ", Paid: " + paid);
	        	}
	        	
	        	System.out.println();
	        	System.out.println("|TRAINER PAYMENT|");
	        	String query1 = "SELECT * FROM TrainerPayment";
	        	PreparedStatement p1 = con.prepareStatement(query1);
	        	ResultSet trainerPaymentResultSet = p1.executeQuery();

	        	while (trainerPaymentResultSet.next()) {
	        	    int trainerID = trainerPaymentResultSet.getInt("trainerID");
	        	    int sessionID = trainerPaymentResultSet.getInt("sessionID");
	        	    int paycheck = trainerPaymentResultSet.getInt("paycheck");
	        	    boolean paid = trainerPaymentResultSet.getBoolean("paid");
	        	    
	        	    System.out.println("TrainerID: " + trainerID + ", SessionID: " + sessionID + ", Paycheck: " + paycheck + ", Paid: " + paid);
	        	}
	        	
	        	System.out.println("Do you want to send a paycheck to a trainer? (1. yes or 2. no)");
	        	String payTrainer = keyboard.next();

	        	// If the user wants to pay the trainer
	        	if (payTrainer.equals("1")) {
	        	    System.out.println("Enter the trainer's ID:");
	        	    int trainerID = keyboard.nextInt();

	        	    System.out.println("Enter the session ID:");
	        	    int sessionID = keyboard.nextInt();

	        	    // Make paid true
	        	    String query3 = "UPDATE TrainerPayment SET paid = true WHERE trainerID = ? AND sessionID = ?";
	        	    PreparedStatement p3 = con.prepareStatement(query3);
	        	    p3.setInt(1, trainerID);
	        	    p3.setInt(2, sessionID);
	        	    p3.executeUpdate();
	        	    
	        	    System.out.println("trainer paid");
	        	}
	        	
	        } else if (adminOption.equals("5")) {
	            System.out.println("Logging out of admin panel.");
	            login = false;
	        } else {
	            System.out.println("Invalid input.");
	        }
	    }
	}
		
		
	
	////////////////////////////////////////////////////////
	// function given in lecture 11						  //
    // used help from example video in week 11            //
	// main function to execute code                      //
    ////////////////////////////////////////////////////////

    public static void main(String[] args) throws ParseException {
        // JDBC & Database credentials
        String url = "jdbc:postgresql://localhost:5432/proj";
        String user = "postgres";  
        String password = "postgres";  
         
        try {
            Class.forName("org.postgresql.Driver");
            // Connect to the database
            Connection con = DriverManager.getConnection(url, user, password);
            if (con != null) {
                System.out.println("Connected to PostgreSQL successfully!");
                startApp(con);
            } else {
                System.out.println("Failed to establish connection.");
            }
        } 
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
