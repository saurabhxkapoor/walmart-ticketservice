import java.util.*;
import java.util.regex.Pattern;


public class TicketServiceImp implements TicketService {


    private Venue venue;
    private AllottingSeat scorer;
    private long holdTimeout;
    private Thread thread;
    private List<SeatGroup> seatGroups = new ArrayList<SeatGroup>();

    private int holdExpirationTime;
    private static int MAX_EXPIRATION_TIME = 90;
    private Map<Integer, SeatGroup> holdSeats = new HashMap<>();
    private SortedMap<Long, Integer> timeoutToHolds = Collections.synchronizedSortedMap(new TreeMap<Long, Integer>());


    public TicketServiceImp(Venue venue, AllottingSeat scorer) {
        this(venue, scorer, MAX_EXPIRATION_TIME);
    }

    public TicketServiceImp(Venue venue, int holdExpirationTime) {
        this.venue = venue;
        this.holdExpirationTime = holdExpirationTime > 0 && holdExpirationTime < MAX_EXPIRATION_TIME ? holdExpirationTime : MAX_EXPIRATION_TIME;
    }

    public TicketServiceImp(Venue venue, AllottingSeat points, int holdExpirationTime) {
        this.venue = venue;
        this.scorer = points;
        this.holdExpirationTime = holdExpirationTime;

        final int rowSize = venue.getSeatsInOneRow();
        final int rows = venue.getRows();

        for (int row = 0; row < rows; row++) {
            List<Seat> seats = new ArrayList<>(rowSize);

            for (int seat = 0; seat < rowSize; seat++) {
                float score = this.scorer.determinePoints(seat, row, this.venue);
                score = SeatHoldUtils.round(score);
                seats.add(new Seat(seat, row, score));
            }

            this.seatGroups.add(new SeatGroup(seats));
        }

        this.thread = new Thread(() -> {
            while (true) {
                Set<Integer> expiredHolds = new HashSet<>();
                for (Map.Entry<Long, Integer> entry : this.timeoutToHolds.entrySet()) {
                    long expireTime = entry.getKey() + this.holdTimeout;
                    if (System.currentTimeMillis() >= expireTime) {
                        expiredHolds.add(entry.getValue());
                    } else {
                        break;
                    }
                }
                if (expiredHolds.size() > 0) {
                    this.removeHolds(expiredHolds);
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        this.thread.start();
    }


    private synchronized void removeHolds(Set<Integer> holdIds) {
        for (int holdId : holdIds) {
            SeatGroup hold = this.holdSeats.remove(holdId);
            if (hold != null) {
                this.seatGroups.add(hold);
            }
        }
    }

    @Override
    public int numSeatsAvailable() {
        int size = 0;
        for (SeatGroup sb : seatGroups) {
            size += sb.size();
        }
        return size;
    }

    @Override
    public synchronized SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
        int numSeatsAvailable;
        if (this.seatGroups.size() == 0) {
            return null;
        } else if (numSeats > (numSeatsAvailable = numSeatsAvailable())) {
            return null;
        } else {
            SeatGroup result = findBestAvailableSeatGroup(numSeats, false);


            if (result != null) {
                result.hold(customerEmail);
                this.holdSeats.put(result.getId(), result);
                this.timeoutToHolds.put(result.getHoldTime(), result.getId());
            }
            return result;
        }
    }

    private SeatGroup findBestAvailableSeatGroup(int numSeats, boolean seatGroupSize) {
        int seatsRequired = numSeats;
        List<Seat> heldSeats = new ArrayList<>();
        List<SeatGroup> usedSeatGroups = new ArrayList<>();

        for (SeatGroup seatGroup : this.seatGroups) {
            if (seatGroupSize || seatGroup.size() >= numSeats) {
                //
                usedSeatGroups.add(seatGroup);

                if (seatGroup.size() == seatsRequired) {


                    heldSeats.addAll(seatGroup.getSeats());
                    seatsRequired -= seatGroup.size();

                    assert (seatsRequired == 0);

                    break;
                } else if (seatsRequired < seatGroup.size()) {

                    List<SeatGroup> splits = seatGroup.split(seatsRequired);
                    SeatGroup bestAvailableGrp = splits.get(0);
                    heldSeats.addAll(bestAvailableGrp.getSeats());
                    seatsRequired -= bestAvailableGrp.size();

                    for (int i = 1; i < splits.size(); i++) {
                        this.seatGroups.add(splits.get(i));
                    }
                    break;
                } else {
                    heldSeats.addAll(seatGroup.getSeats());
                    seatsRequired -= seatGroup.size();
                    assert (seatsRequired > 0);
                }
            }
        }
        if (usedSeatGroups.size() > 0) {
            for (SeatGroup toDelete : usedSeatGroups) {
                this.seatGroups.remove(toDelete);
            }
        }
        if (heldSeats.size() == 0) {
            assert (seatsRequired == numSeats);
            return null;
        } else {
            return new SeatGroup(heldSeats);
        }
    }

    @Override
    public synchronized String reserveSeats(int seatHoldId, String customerEmail) {

        if (seatHoldId <= 0) {
            return null;
        }

       else if (!isCustomerEmailValid(customerEmail))
            return null;

        else if (!this.holdSeats.containsKey(seatHoldId))
            return null;

        SeatGroup hold = this.holdSeats.remove(seatHoldId);
        if (!hold.getEmail().equals(customerEmail)) {
            return null;

        }

        String result = String.valueOf(hold.getId());
        return result;
    }

    private boolean isCustomerEmailValid(String customerEmail) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (customerEmail == null || customerEmail.equals(""))
            return false;
        return pat.matcher(customerEmail).matches();
    }
}





