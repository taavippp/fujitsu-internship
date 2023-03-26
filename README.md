# Fujitsu Internship application project

This project is supposed to emulate a transportation app's fee generator such as Bolt's.

There is a Cronjob that runs every hour on the 15th minute.
However, for ease of use, a 30 second interval cron expression is commented out right above.

This project implements a REST API that is queryable through port 8080 on the endpoint "/api".
The REST API takes three parameters:
- city
  - Tallinn
  - Tartu
  - Pärnu
- vehicle
  - car
  - scooter
  - bike
- timestamp (OPTIONAL)
  - unix epoch time (seconds since 01/01/1970)