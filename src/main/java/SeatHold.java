import java.util.List;

public interface SeatHold {
    int getId();
    String getEmail();
    List<Seat> getSeats();
    int size();
}
