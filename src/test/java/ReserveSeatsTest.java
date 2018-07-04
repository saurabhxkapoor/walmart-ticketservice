import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ReserveSeatsTest {

    private TicketService ticketService;
    private BestSeatAllottment bestSeatAllottment;
    private SeatHoldUtils seatHoldUtils;


    @Before
    public void setVariables() {
        this.bestSeatAllottment = new BestSeatAllottment();
        this.seatHoldUtils = new SeatHoldUtils();
    }


    @Test
    public void should_not_reserve_invalid_seats_for_incorrect_email() {

        Venue venue = new Venue(3, 2);
        this.ticketService = new TicketServiceImp(venue, 70);
        Assert.assertNull(ticketService.reserveSeats(20012, "add.xv.com"));

    }

    @Test
    public void shoud_not_reserve_ticket_if_holdId_is_not_proper() {
        Venue venue = new Venue(3, 2);
        this.ticketService = new TicketServiceImp(venue, 70);
        Assert.assertNull(ticketService.reserveSeats(-1, "abc.xb@gdf.com"));

    }


    @Test
    public void should_be_able_reserve_couple_of_seats() {
        final int seats = 8;
        final int rows = 5;
        final int totalSeats = seats * rows;
        Venue venue = new Venue(seats, rows);
        this.ticketService = new TicketServiceImp(venue, bestSeatAllottment);
        SeatHold hold = ticketService.findAndHoldSeats(2, "fddfg.fdgd@df.com");
        seatHoldUtils.verifySeatHold(hold, "fddfg.fdgd@df.com");
        Assert.assertEquals(totalSeats - 2, ticketService.numSeatsAvailable());
        String ticketConfirmation = ticketService.reserveSeats(hold.getId(), "fddfg.fdgd@df.com");
        Assert.assertNotNull(ticketConfirmation);
        Assert.assertEquals(totalSeats - 2, ticketService.numSeatsAvailable());
    }

    @Test
    public void should_be_able_to_reserve_entire_venue() {
        Venue venue = new Venue(50, 100);
        this.ticketService = new TicketServiceImp(venue, bestSeatAllottment);

        final int blockSize = 2;
        for (int seat = 2; seat < venue.getMaxSeats(); seat += blockSize) {
            SeatHold hold = ticketService.findAndHoldSeats(blockSize, "fddfg.fdgd@df.com");
            //verifySeatHold(hold, "fddfg.fdgd@df.com");
            seatHoldUtils.verifySeatHold(hold, "fddfg.fdgd@df.com");
            Assert.assertEquals(venue.getMaxSeats() - seat, ticketService.numSeatsAvailable());
            String confirmation = ticketService.reserveSeats(hold.getId(), "fddfg.fdgd@df.com");
            Assert.assertNotNull(confirmation);
            Assert.assertEquals(venue.getMaxSeats() - seat, ticketService.numSeatsAvailable());
        }
    }

    @Test
    public void should_not_be_able_to_reserve_seat_without_holding() {

        Venue venue = new Venue(10, 8);
        this.ticketService = new TicketServiceImp(venue, bestSeatAllottment);
        ticketService.reserveSeats(333, "fddfg.fdgd@df.com");
        Assert.assertNull(ticketService.reserveSeats(333, "fddfg.fdgd@df.com"));

    }

    @Test
    public void should_not_be_able_to_reserve_seat_for_different_email_address() {
        Venue venue = new Venue(10, 8);
        this.ticketService = new TicketServiceImp(venue, bestSeatAllottment);
        SeatHold hold = this.ticketService.findAndHoldSeats(5, "fddfg.fdgd@df.com");
        seatHoldUtils.verifySeatHold(hold, "fddfg.fdgd@df.com");
        String confirmation = ticketService.reserveSeats(hold.getId(), "XYZ.ABC@msn.com");
        Assert.assertNull(confirmation);
    }


}
