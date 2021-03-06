`Ticket Service QA Challenge`
==
Developing & Testing solution for a simple ticket service that facilitates the discovery, temporary hold, and final reservation of seats within a high-demand
performance venue.  


## Build & Test

Run the following command to execute tests:
```mvn clean test```

#### Optional:To generate maven-surefire report
##### Execute the following commands in order:
1. ```mvn clean test```
2. ```mvn surefire-report:report-only```
3. ```mvn site -DgenerateReports=false```

Once executed, open the following html file:
```~target/site/surefire-report.html```
#### Sample Screenshot
![image](https://user-images.githubusercontent.com/8683732/42201220-d57897ca-7e64-11e8-8680-724486527152.png)

Following Tests have been implemented for SeatHold, SeatReserve & BestSeatAvailable in `SeatHoldTest`, `ReserveSeatsTest` & `BestSeatPointsTest`
1. `num_seats_available`
2. `shoud_not_reserve_ticket_if_holdId_is_not_proper`
3. `should_not_hold_when_seats_not_available`
4. `should_not_hold_when_seat_demand_is_more_than_availability`
5. `should_be_able_to_hold_seats_when_available`
6. `should_be_able_to_display_available_seats_after_holding`
7. `should_not_reserve_invalid_seats_for_incorrect_email`
8. `shoud_not_reserve_ticket_if_holdId_is_not_proper`
9. `should_be_able_reserve_couple_of_seats`
10. `should_be_able_to_reserve_entire_venue`
11. `should_not_be_able_to_reserve_seat_without_holding`
12. `should_not_be_able_to_reserve_seat_for_different_email_address`
13. `seat_towards_the_front_left_of_the_stage_has_higher_points`
14. `seat_towards_back_right_of_the_stage_has_lowest_points`

Following Load (High user volume) tests have not been implemented:
1. `users_should_be_able_to_hold_different_seats_concurrently` for multiple users trying to block seats concurrently.
2. `users_should_be_able_reserve_held_seats_concurrently` for multiple users trying to reserve blocked seats concurrently.

## Assumptions

1. Hold on seats expire after given time.
2. Once the hold on seats expire, the seats become available to the user.
3. Once Seats have been reserved, it can't be reversed.
4. User is not restricted on the number of the seats he can book. 

## Design

1. ```TicketServiceImp``` class implements the methods declared in the interface `TicketService`.
2. `Best Available Seats` are allotted from front to the back of the stage. The closer a user is seated to the stage, the better the seat it is. The `BestSeatAllotment` provides a way to assign points to the best seating arrangement.The more the seat is towards the right/ or the back of the stage, the lesser will be the points associated with the seat.
3. ```findAndHoldSeats``` and ```reserveSeats``` need to be thread safe and hence these methods are `Synchronized`.
4. Once a user selects seat/seats, we call it as `SeatGroup` to identify it as a group of seats belonging to a user.
5. HoldId is generated once the user successfully generates a hold on the seat.
6. Once the seat/seats are reserved, a reserveId is generated based on holdId.
7. Email Address at Hold And Reservation should match. If they are different, user won't be able to reserve the seats.

