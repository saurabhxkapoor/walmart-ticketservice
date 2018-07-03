
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class SeatHoldTest {

    private TicketService ticketService;
    private BestSeatAllottment points;
    private SeatHoldUtils seatHoldUtils ;


    @Before
    public void set_variables()
    {
        this.points = new BestSeatAllottment();
        this.seatHoldUtils = new SeatHoldUtils();
    }

    @Test
    public void num_seats_available() {
        Venue venue = new Venue(6, 5);
        this.ticketService = new TicketServiceImp(venue, points);
        Assert.assertNotNull(ticketService);
        Assert.assertEquals(venue.getMaxSeats(),ticketService.numSeatsAvailable());
    }


    @Test
    public void should_not_hold_when_seats_not_available() {
        final int seats = 0;
        final int rows = 0;
        Venue venue = new Venue(seats, rows);
        this.ticketService = new TicketServiceImp(venue, points);
        SeatHold hold = ticketService.findAndHoldSeats(5, "zxd.rt@gdo.com");
        Assert.assertNull(hold);
    }


    @Test
    public void should_not_hold_when_seat_demand_is_more_than_availability() {
        final int seats = 2;
        final int rows = 2;
        Venue venue = new Venue(3,3);
        this.ticketService = new TicketServiceImp(venue, points);
        SeatHold hold = ticketService.findAndHoldSeats(5, "dfgdfgdf.rt@fg.com");
        Assert.assertNull(hold);
    }


    @Test
    public  void should_be_able_to_hold_seats_when_available() {
        Venue venue = new Venue(8, 6);
        this.ticketService = new TicketServiceImp(venue, new BestSeatAllottment());
        final int blockSize = 2;
        for (int seat = 2; seat < venue.getMaxSeats(); seat += blockSize) {
            SeatHold hold = ticketService.findAndHoldSeats(blockSize, "sfsfs.ff@fgg.com");
            seatHoldUtils.verifySeatHold(hold, "sfsfs.ff@fgg.com");
            Assert.assertEquals(venue.getMaxSeats() - seat, ticketService.numSeatsAvailable());
        }
    }


    @Test
    public void should_be_able_to_display_available_seats_after_holding() {
        Venue venue = new Venue(10, 8);
        this.ticketService = new TicketServiceImp(venue, this.points, 80);
        SeatHold hold = this.ticketService.findAndHoldSeats(5, "fddfg.fdgd@df.com");
        seatHoldUtils.verifySeatHold(hold, "fddfg.fdgd@df.com");
        Assert.assertEquals(75, this.ticketService.numSeatsAvailable());
        Assert.assertNotEquals(venue.getMaxSeats(), ticketService.numSeatsAvailable());
    }



}
