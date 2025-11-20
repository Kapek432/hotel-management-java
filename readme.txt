================================================================================
                    HOTEL MANAGEMENT SYSTEM - README
================================================================================

AUTOR: Kacper Lipiec
DATA: 17 Listopada 2025

================================================================================
                          BUDOWANIE PROJEKTU
================================================================================

Podstawowy build:
    mvn clean install

Build z analizą SonarQube:
    UWAGA: Przed uruchomieniem analizy SonarQube należy najpierw uruchomić
    serwer SonarQube

    1. Uruchom serwer SonarQube:
       - Windows: StartSonar.bat (w katalogu bin SonarQube)
       - Linux/Mac: ./sonar.sh start

    2. Poczekaj, aż serwer się uruchomi (sprawdź http://localhost:9000)

    3. Uruchom build z analizą:
       mvn clean install sonar:sonar

    Jeśli serwer SonarQube nie jest uruchomiony, użyj podstawowego buildu.

================================================================================
                       URUCHAMIANIE APLIKACJI
================================================================================

    java -jar hotel-main/target/hotel-main-1.0-SNAPSHOT.jar

Dostępne komendy w aplikacji:
    checkin, checkout, view, list, prices, save, load, exit

================================================================================
                          FORMAT PLIKU CSV
================================================================================

Plik CSV zawiera 12 kolumn:
1.  RoomNumber              - Numer pokoju
2.  Description             - Typ pokoju
3.  Price                   - Cena za noc
4.  Capacity                - Liczba gości
5.  Occupied                - true/false
6.  MainGuestFirstName      - Imię
7.  MainGuestLastName       - Nazwisko
8.  CheckInDate             - Format: YYYY-MM-DD
9.  CheckOutDate            - Format: YYYY-MM-DD
10. StayDuration            - Liczba dni
11. AdditionalInfo          - Dodatkowe informacje
12. AdditionalGuestsCount   - Liczba dodatkowych gości

Przykład (pokój zajęty):
    101,Single,150.0,1,true,Kacper,Lipic,2025-11-01,2025-11-04,3,VIP,0

Przykład (pokój wolny):
    102,Double,250.0,2,false,,,,,,,0

================================================================================
                    KONFIGURACJA SONARQUBE
================================================================================

1. Uruchom SonarQube.

2. Ustaw token:

    Windows PowerShell:
        $env:SONAR_TOKEN="wygenerowany-token"

    Lub edytuj pom.xml i zamień:
        <sonar.token>${env.SONAR_TOKEN}</sonar.token>
    na:
        <sonar.token>wygenerowany-token</sonar.token>

4. Uruchom analizę:
    mvn clean install sonar:sonar

5. Zobacz wyniki:
    http://localhost:9000/dashboard?id=hotel-management

================================================================================
                      GENEROWANIE DOKUMENTACJI
================================================================================

Javadoc:
    mvn javadoc:aggregate
    Lokalizacja: target/site/apidocs/index.html

================================================================================
                        STRUKTURA PROJEKTU
================================================================================

hotel-management/
├── pom.xml                          # Parent POM
├── readme.txt                       # Ten plik
├── hotel-utils/                     # Moduł utilities (MyMap)
├── hotel-main/                      # Moduł główny aplikacji
├── hotel_states/                    # Przykładowe pliki CSV stanów hoteli
├── javadoc/target/site/apidocs/index.html     # Dokumentacja Javadoc
└── sonar-qube/                      # Raport SonarQube PDF

================================================================================