public class BestSeatAllottment implements AllottingSeat {

    @Override
    public float determinePoints(int seatIndex, int rowIndex, Venue venue) {
        float seatPoints = (float) venue.getSeatsInOneRow() / (float) (venue.getSeatsInOneRow() + seatIndex);
        float rowPoints = (float) venue.getRows() / (float) (venue.getRows() + rowIndex);
        return getApproxVal((seatPoints + rowPoints) / 2);
    }

    private float getApproxVal(float f) {

        return (float) Math.round(f * 100.0f) / 100.0f;
    }


}