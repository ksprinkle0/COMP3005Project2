# COMP3005Project2

Author: Khushie Singh

Description: a health and fitness club management system

Functions: 
- public static void startApp(Connection con) throws SQLException, ParseException
- public static boolean validateMemberLogin(Connection con, String username, String password) throws SQLException
- public static boolean validateTrainerLogin(Connection con, String trainerCode) throws SQLException
- public static boolean validateAdminLogin(Connection con, String adminCode) throws SQLException
- public static boolean checkIfUsernameExists(Connection con, String username) throws SQLException
- public static boolean memberIDExists(Connection con, int memberId) throws SQLException
- public static int getMemberID(Connection con, String username) throws SQLException
- public static String getMemberName(Connection con, String username) throws SQLException
- public static void memFunctions(Connection con, String username) throws SQLException
- public static int getTrainerID(Connection con, String trainerCode) throws SQLException
- public static String getTrainerName(Connection con, String trainerCode) throws SQLException
- public static void trainerFunctions(Connection con, String code) throws SQLException
- public static void adminFunctions(Connection con, String code) throws SQLException, ParseException
- public static void main(String[] args) throws ParseException
