import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BestSeatPointsTest {

    private AllottingSeat allottingSeat;

    @Before
    public void setup_objects() {
         allottingSeat= new BestSeatAllottment();
    }

    @Test
    public void seat_towards_the_front_left_of_the_stage_has_higher_points(){

        Venue venue = new Venue(100,100);
         Assert.assertEquals(1.0, allottingSeat.determinePoints(0,0, venue),0);

    }

    @Test
    public void seat_towards_back_right_of_the_stage_has_lowest_points(){

        Venue venue = new Venue(100,100);
        Assert.assertEquals(0.5, allottingSeat.determinePoints(99,99,venue),0);
    }
}
