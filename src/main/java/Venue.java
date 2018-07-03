public class Venue {

    private int rows;
    private int seatsInOneRow;


    public Venue(int rows, int seatsInOneRow) {
        this.rows = rows;
        this.seatsInOneRow = seatsInOneRow;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getSeatsInOneRow() {
        return seatsInOneRow;
    }

    public void setSeatsInOneRow(int seatsInOneRow) {
        this.seatsInOneRow = seatsInOneRow;
    }

    public int totalSeatsInVenue() {
        return this.rows * seatsInOneRow;
    }

    public int getMaxSeats() {
        return this.seatsInOneRow * rows;
    }

}
