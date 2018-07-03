public class Seat  {
    private int seat;
    private int row;
    private float score;
    private String code;

    public Seat(int seat, int row, float score) {
        this.seat = seat;
        this.row = row;
        this.score = score;
        this.code = String.format("%d-%d", this.seat, this.row);
    }


    public int getSeat() {
        return seat;
    }

    public int getRow() {
        return row;
    }

    public float getScore() {
        return score;
    }


}
