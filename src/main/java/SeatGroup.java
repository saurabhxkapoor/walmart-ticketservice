import java.util.*;

public class SeatGroup implements SeatHold {

    private static int ID = 0;
    private int id = ++ID;
    private long holdTime;
    private String email;
    private float seatPoints;
    private List<Seat> seats = new ArrayList<>();

    //Customer Holding block of seat/seats
    public SeatGroup(String email, List<Seat> seats) {
        this.email = email;

        float scoreSum = 0.0f;
        for (Seat s : seats) {
            scoreSum += s.getScore();
            this.seats.add(s);
        }
        this.seatPoints = SeatHoldUtils.round(scoreSum / this.seats.size());
    }

    public SeatGroup(List<Seat> seats) {
        this(null, seats);
    }

    public int getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public void hold(String email) {
        this.email = email;
        this.holdTime = System.currentTimeMillis();
    }

    public long getHoldTime() {
        return this.holdTime;
    }

    public List<Seat> getSeats() {
        return Arrays.asList(this.seats.toArray(new Seat[this.seats.size()]));
    }

    public int size() {
        return this.seats.size();
    }


    public List<SeatGroup> split(int size) {
        if (size > this.seats.size()) {
            throw new ArrayIndexOutOfBoundsException("Partitioned size > the number of seats: " + size);
        }
        List<SeatGroup> results = new ArrayList<>();

        int bestStartingIndex = bestAvailableIndex(size);

        if (bestStartingIndex == 0) {

            List<Seat> left = this.seats.subList(0, size);
            List<Seat> right = this.seats.subList(size, this.seats.size());

            results.add(new SeatGroup(left));
            results.add(new SeatGroup(right));
        } else if (bestStartingIndex + size == this.seats.size()) {

            List<Seat> left = this.seats.subList(0, bestStartingIndex);
            List<Seat> right = this.seats.subList(bestStartingIndex, this.seats.size());

            results.add(new SeatGroup(right));
            results.add(new SeatGroup(left));
        } else if (bestStartingIndex > 0) {

            List<Seat> left = this.seats.subList(0, bestStartingIndex);
            List<Seat> middle = this.seats.subList(bestStartingIndex, bestStartingIndex + size);
            List<Seat> right;
            if (bestStartingIndex + size < this.seats.size()) {
                right = this.seats.subList(bestStartingIndex + size, this.seats.size());
            } else {
                right = new ArrayList<>();
            }

            results.add(new SeatGroup(middle));
            results.add(new SeatGroup(left));
            if (right.size() > 0) {
                results.add(new SeatGroup(right));
            }
        }
        return results;
    }

    private int bestAvailableIndex(int size) {
        int bestIndexToStart = 0;
        float maxAverage = 0.0f;
        for (int i = 0; i <= this.seats.size() - size; ++i) {

            float sum = 0.0f;
            for (int j = i; j < i + size; j++) {
                sum += this.seats.get(j).getScore();
            }
            float average = sum / size;

            if (average > maxAverage) {
                maxAverage = average;
                bestIndexToStart = i;
            }
        }
        return bestIndexToStart;
    }


}
