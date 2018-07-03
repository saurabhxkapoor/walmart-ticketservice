import org.junit.Assert;

import java.util.List;

public class SeatHoldUtils {


    public static float round(float f) {

        return (float)Math.round(f * 100.0f) / 100.0f;
    }

    public void verifySeatHold(SeatHold hold, String email, Integer startingSeat, Integer endingSeat) {

        final List<Seat> seats = hold.getSeats();
        if (startingSeat != null) {
            Assert.assertEquals(startingSeat.intValue(),seats.get(0).getSeat());
        }
        if (endingSeat != null) {
            Assert.assertEquals(endingSeat.intValue(), seats.get(seats.size() - 1).getSeat());
        }
    }

    public void verifySeatHold(SeatHold hold, String email) {
        this.verifySeatHold(hold, email, null, null);
    }

}
