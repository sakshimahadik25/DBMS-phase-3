import constants.AppealStatus;
import constants.PaymentStatus;
import constants.SpaceType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReportOperations {

    public static void ReportOperationChoice(int reportChoice) {

        switch (reportChoice) {
            case 0:
                return;
            case 1:
                citationReport();
                break;
            case 2:
                monthlyCitationReport();
                break;
            case 3:
                annualCitationReport();
                break;
            case 4:
                rangeBasedCitationReport();
                break;
            case 5:
                listZonesForEachLot();
                break;
            case 6:
                numberOfCarsInViolation();
                break;
            case 7:
                numberOfEmployeesHavingPermitsForGivenZone();
                break;
            case 8:
                permitInformation();
                break;
            case 9:
                spaceAvailability();
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }
    }


    /**
     * It displays all the citations issued till date.
     */
    public static void citationReport() {
        String query = "SELECT * FROM Citations";
        Statement stmt = null;
        ResultSet result = null;
        try {
            Connection DB = DatabaseConnection.getDBInstance();
            stmt = DB.createStatement();
            result = stmt.executeQuery(query);
            if(result.first()){
                System.out.println("\n--------------------- Citation Report ---------------------\n");
                System.out.printf("%15s %15s %15s %20s %10s %15s %15s %20s %15s %15s %10s", "CitationNumber", "CitationDate", "CitationTime", "Category", "Fee", "PaymentStatus", "AppealStatus", "CarLicenseNumber", "ParkingLot", "DriverID", "StaffID");
                do {
                    System.out.printf("\n%15d %15s %15s %20s %10.2f %15s %15s %20s %15s %15s %10d", result.getInt("CitationNo"), result.getString("CitationDate"), result.getString("CitationTime"), result.getString("Category"), result.getFloat("Fee"), result.getString("PaymentStatus"), result.getString("AppealStatus"), result.getString("CarLicenseNumber"), result.getString("ParkingLotName"), result.getString("DriverID"), result.getInt("StaffID"));
                } while (result.next());
            } else {
                System.out.println("\nNo citations issued till date");
            }
            System.out.println("\n\n");
        } catch (SQLException err) {
            System.out.println("Error while generating citations - " + err.getMessage());
        } finally {
            DatabaseConnection.close(result);
            DatabaseConnection.close(stmt);
        }
    }


    /**
     * It gives the annual report of citations.
     */
    public static void monthlyCitationReport() {
        String query = "SELECT ParkingLotName, MONTHNAME(CitationDate) AS Month, COUNT(*) AS TotalCitations FROM Citations GROUP BY ParkingLotName , MONTHNAME(CitationDate);";
        Statement stmt = null;
        ResultSet result = null;
        try {
            Connection DB = DatabaseConnection.getDBInstance();
            stmt = DB.createStatement();
            result = stmt.executeQuery(query);

            System.out.println("\n-------------------------------- Monthly Citation Report --------------------------------\n");

            if(result.next()){
                System.out.printf("%20s %20s %20s", "Parking Lot", "Month", "Total Citations");
                do{
                    System.out.format("\n%20s %20s %20d", result.getString("ParkingLotName"), result.getString("Month"), result.getInt("TotalCitations"));
                } while(result.next());
            } else {
                System.out.println("\nNo citations issued till date");
            }
            System.out.println("\n\n");

        } catch (SQLException err) {
            System.out.println("Error while generating monthly citation report - " + err.getMessage());
        } finally {
            DatabaseConnection.close(result);
            DatabaseConnection.close(stmt);
        }
    }


    /**
     * It gives the annual report of citations.
     */
    public static void annualCitationReport() {
        String query = "SELECT ParkingLotName, YEAR(CitationDate) AS Year, COUNT(*) AS TotalCitations FROM Citations GROUP BY ParkingLotName , YEAR(CitationDate);";
        Statement stmt = null;
        ResultSet result = null;
        try {
            Connection DB = DatabaseConnection.getDBInstance();
            stmt = DB.createStatement();
            result = stmt.executeQuery(query);

            System.out.println("\n-------------------------------- Annual Citation Report --------------------------------\n");

            if(result.next()){
                System.out.printf("%20s %20s %20s", "Parking Lot", "Year", "Total Citations");
                do{
                    System.out.format("\n%20s %20s %20d", result.getString("ParkingLotName"), result.getString("Year"), result.getInt("TotalCitations"));
                } while(result.next());
            } else {
                System.out.println("\nNo citations issued till date");
            }
            System.out.println("\n\n");

        } catch (SQLException err) {
            System.out.println("Error while generating annual citation report - " + err.getMessage());
        } finally {
            DatabaseConnection.close(result);
            DatabaseConnection.close(stmt);
        }
    }


    /**
     * It gives the report of citations issued within a specified time duration
     */
    public static void rangeBasedCitationReport() {
        String startDate = UserInput.getString("Enter start date");
        String endDate = UserInput.getString("Enter end date");
        String query = String.format("SELECT ParkingLotName, COUNT(*) AS TotalCitations FROM Citations WHERE CitationDate BETWEEN '%s' AND '%s' GROUP BY ParkingLotName;", startDate, endDate);
        Statement stmt = null;
        ResultSet result = null;
        try {
            Connection DB = DatabaseConnection.getDBInstance();
            stmt = DB.createStatement();
            result = stmt.executeQuery(query);

            System.out.println(String.format("\n------------------ Citation Report from %s to %s ------------------\n", startDate, endDate));

            if(result.next()){
                System.out.printf("%20s %20s", "Parking Lot", "Total Citations");
                do{
                    System.out.format("\n%20s %20d", result.getString("ParkingLotName"), result.getInt("TotalCitations"));
                } while(result.next());
            } else {
                System.out.println("\nThere are no citations issued in the specified duration");
            }
            System.out.println("\n\n");

        } catch (SQLException err) {
            System.out.println("Error while generating citation report - " + err.getMessage());
        } finally {
            DatabaseConnection.close(result);
            DatabaseConnection.close(stmt);
        }
    }


    /**
     * It gives all the pairs of parking lot and zone in the Wolf Parking Management System
     */
    public static void listZonesForEachLot() {
        String query = "SELECT ParkingLotName, ZoneID FROM Zones ORDER BY ParkingLotName , ZoneID;";
        Statement stmt = null;
        ResultSet result = null;
        try {
            Connection DB = DatabaseConnection.getDBInstance();
            stmt = DB.createStatement();
            result = stmt.executeQuery(query);

            System.out.println(String.format("\n------------------ List of zones per lot ------------------\n"));

            if(result.next()){
                System.out.printf("%40s %20s", "Parking Lot", "Zone");
                do{
                    System.out.format("\n%40s %20s", result.getString("ParkingLotName"), result.getString("ZoneID"));
                } while(result.next());

            } else {
                System.out.println("\nZones are not assigned to any parking lot");
            }
            System.out.println("\n\n");

        } catch (SQLException err) {
            System.out.println("Error while listing zones for each lot - " + err.getMessage());
        } finally {
            DatabaseConnection.close(result);
            DatabaseConnection.close(stmt);
        }
    }


    /**
     * It gives the total number of cars currently in violation.
     */
    public static void numberOfCarsInViolation() {
        String query = String.format("SELECT COUNT(DISTINCT CarLicenseNumber) AS ViolationCount FROM Citations WHERE PaymentStatus NOT IN ('%s','%s') AND AppealStatus != '%s';", PaymentStatus.PAID, PaymentStatus.WAIVED, AppealStatus.ACCEPT);
        Statement stmt = null;
        ResultSet result = null;
        try {
            Connection DB = DatabaseConnection.getDBInstance();
            stmt = DB.createStatement();
            result = stmt.executeQuery(query);

            System.out.println(String.format("\n------------------ Cars in violation ------------------\n"));
            if(result.next()){
                System.out.printf("%30s", "Number of cars in violation");
                System.out.format("\n%30d", result.getInt("ViolationCount"));
            } else {
                System.out.println("\n There are no cars under violation");
            }

            System.out.println("\n\n");

        } catch (SQLException err) {
            System.out.println("Error while computing cars in violation - " + err.getMessage());
        }  finally {
            DatabaseConnection.close(result);
            DatabaseConnection.close(stmt);
        }
    }


    /**
     * It gives the total number of employees having valid permits in all parking lots for a given zone.
     */
    public static void numberOfEmployeesHavingPermitsForGivenZone() {
        String zone = UserInput.getString("Enter zone").toUpperCase();
        String query = String.format("SELECT COUNT(*) AS EmployeeCount FROM Permits WHERE DriverStatus IN ('E') AND ZoneID = '%s' AND DATEDIFF(NOW(), StartDate) > 0 AND (DATEDIFF(ExpirationDate, NOW()) > 0 OR (DATEDIFF(ExpirationDate, NOW()) = 0 AND time_to_sec(TIMEDIFF(CONCAT_WS(' ', ExpirationDate, ExpirationTime), NOW()) > 0))) > 0;", zone);
        Statement stmt = null;
        ResultSet result = null;
        try {
            Connection DB = DatabaseConnection.getDBInstance();
            stmt = DB.createStatement();
            result = stmt.executeQuery(query);
            System.out.println(String.format("\n------------------ Number of employees having permits in Zone %s ------------------\n", zone));
            if(result.next()){
                System.out.printf("%30s", "Number of employees");
                do {
                    System.out.format("\n%30d", result.getInt("EmployeeCount"));
                } while(result.next());
            } else {
                System.out.println(String.format("\nEmployee permit information not available"));
            }
            System.out.println("\n\n");

        } catch (SQLException err) {
            System.out.println("Error while computing employee count - " + err.getMessage());
        }  finally {
            DatabaseConnection.close(result);
            DatabaseConnection.close(stmt);
        }
    }


    /**
     * It gives the permit information of a driver.
     */
    public static void permitInformation() {
        String driverID = UserInput.getString("Enter driver ID");
        String query = String.format("SELECT * FROM Permits WHERE DriverID = '%s';", driverID);
        Statement stmt = null;
        ResultSet result = null;
        try {
            Connection DB = DatabaseConnection.getDBInstance();
            stmt = DB.createStatement();
            result = stmt.executeQuery(query);


            System.out.println(String.format("\n------------------ Permit information ------------------\n"));
            if(result.next()){
                System.out.printf("%10s %15s %15s %20s %20s %15s %20s %8s %30s %15s", "Permit ID", "Permit type", "Start date", "Expiration date", "Expiration time", "Space type", "Car license number", "Zone", "Parking lot", "Driver status");
                do {
                    System.out.format("\n%10d %15s %15s %20s %20s %15s %20s %8s %30s %15s", result.getInt("PermitID"), result.getString("PermitType"), result.getString("StartDate"), result.getString("ExpirationDate"), result.getString("ExpirationTime"), result.getString("SpaceType"), result.getString("CarLicenseNumber"), result.getString("ZoneID"), result.getString("ParkingLotName"), result.getString("DriverStatus"));
                } while(result.next());
            } else {
                System.out.println(String.format("\nPermit information not available for Driver(%s)",driverID));
            }
            System.out.println("\n\n");

        } catch (SQLException err) {
            System.out.println("Error while fetching permit information - " + err.getMessage());
        }   finally {
            DatabaseConnection.close(result);
            DatabaseConnection.close(stmt);
        }
    }


    /**
     * It checks for the availability of space in given space type and parking lot. It lists all the available space IDs.
     */
    public static void spaceAvailability() {
        String parkingLot = UserInput.getString("Enter parking lot");
        System.out.print("Select space type: ");
        String spaceTypes[] = SpaceType.getSpaceTypes();
        String spaceType = displayOptions(spaceTypes);
        String query = String.format("SELECT SpaceID, ParkingLotName FROM Spaces WHERE SpaceType = '%s' AND AvailabilityStatus = 'Available' AND ParkingLotName = '%s';", spaceType, parkingLot);

        Statement stmt = null;
        ResultSet result = null;
        try {
            Connection DB = DatabaseConnection.getDBInstance();
            stmt = DB.createStatement();
            result = stmt.executeQuery(query);

            System.out.println(String.format("\n------------------ Available spaces in %s for %s space ------------------\n", parkingLot, spaceType));
            if (result.next()) {
                System.out.printf("%10s", "Space ID");
                do {
                    System.out.format("\n%10d", result.getInt("SpaceID"));
                } while (result.next());
            } else {
                System.out.println(String.format("No space available for %s in %s", spaceType, parkingLot));
            }

            System.out.println("\n\n");

        } catch (SQLException err) {
            System.out.println("Error while fetching space availability - " + err.getMessage());
        }   finally {
            DatabaseConnection.close(result);
            DatabaseConnection.close(stmt);
        }
    }


    /**
     * This method prints the menu list from which user selects one option.
     * @param list - It accepts a string array that contains the options given to user.
     * @return = A string representing the selected option by the user.
     */
    private static String displayOptions(String[] list) {
        for (int j = 0; j < list.length; j++) {
            System.out.print((j + 1) + ". " + list[j] + "  ");
        }
        System.out.println();
        return list[UserInput.getInt("Enter choice") - 1];
    }
}
