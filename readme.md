# HOTEL MANAGEMENT SYSTEM - README

**AUTHOR:** Kacper Lipiec  
**DATE:** November 17, 2025

---

## BUILDING THE PROJECT

**Basic build:**
```bash
mvn clean install
````

**Build with SonarQube analysis:**
**NOTE:** Before running SonarQube analysis, start the SonarQube server first.

1. Start the SonarQube server:

   * Windows: `StartSonar.bat` (in SonarQube `bin` directory)
   * Linux/Mac: `./sonar.sh start`

2. Wait until the server starts (check [http://localhost:9000](http://localhost:9000))

3. Run the build with analysis:

```bash
mvn clean install sonar:sonar
```

If the SonarQube server is not running, use the basic build.

---

## RUNNING THE APPLICATION

```bash
java -jar hotel-main/target/hotel-main-1.0-SNAPSHOT.jar
```

Available commands in the application:
`checkin`, `checkout`, `view`, `list`, `prices`, `save`, `load`, `exit`

---

## CSV FILE FORMAT

The CSV file contains 12 columns:

| Column                | Description                 |
| --------------------- | --------------------------- |
| RoomNumber            | Room number                 |
| Description           | Room type                   |
| Price                 | Price per night             |
| Capacity              | Number of guests            |
| Occupied              | true/false                  |
| MainGuestFirstName    | First name of main guest    |
| MainGuestLastName     | Last name of main guest     |
| CheckInDate           | Format: YYYY-MM-DD          |
| CheckOutDate          | Format: YYYY-MM-DD          |
| StayDuration          | Number of days              |
| AdditionalInfo        | Extra information           |
| AdditionalGuestsCount | Number of additional guests |

**Example (occupied room):**

```
101,Single,150.0,1,true,Kacper,Lipiec,2025-11-01,2025-11-04,3,VIP,0
```

**Example (vacant room):**

```
102,Double,250.0,2,false,,,,,,,0
```

---

## SONARQUBE CONFIGURATION

1. Start SonarQube.

2. Set the token:

**Windows PowerShell:**

```powershell
$env:SONAR_TOKEN="generated-token"
```

Or edit `pom.xml` and replace:

```xml
<sonar.token>${env.SONAR_TOKEN}</sonar.token>
```

with

```xml
<sonar.token>generated-token</sonar.token>
```

3. Run analysis:

```bash
mvn clean install sonar:sonar
```

4. View results:
   [http://localhost:9000/dashboard?id=hotel-management](http://localhost:9000/dashboard?id=hotel-management)

---

## GENERATING DOCUMENTATION

**Javadoc:**

```bash
mvn javadoc:aggregate
```

Location: `target/site/apidocs/index.html`

---

## PROJECT STRUCTURE

```
hotel-management/
├── pom.xml                          # Parent POM
├── README.md                        # This file
├── hotel-utils/                     # Utilities module (MyMap)
├── hotel-main/                      # Main application module
├── hotel_states/                    # Sample CSV hotel state files
├── javadoc/target/site/apidocs/     # Javadoc documentation
└── sonar-qube/                      # SonarQube PDF reports
```
