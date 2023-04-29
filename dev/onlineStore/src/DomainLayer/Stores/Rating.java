package DomainLayer.Stores;

public class Rating {
    double rating;
    String comment;

    public Rating (double rating) {
        if (rating > 5 || rating < 0) {
            throw new IllegalArgumentException("Your Rate must be between 0-5 ");
        }
        this.rating = rating;
    }
    public Rating (double rating, String comment){
        if(comment == null)
            throw new NullPointerException("Comment cant be null");
        if (rating > 5 || rating < 0) {
            throw new IllegalArgumentException("Your Rate must be between 0-5 ");
        }
        this.rating = rating;
        this.comment=comment;
    }

    public double getRating() {
        return rating;
    }

    public void addComment(String comment){
        if(comment == null)
            throw new NullPointerException("Comment cant be null");
        this.comment = comment;
    }
    public void setRating(double rate) {
        if (rate > 5 || rate < 0) {
            throw new IllegalArgumentException("Your rating must be between 0-5 ");
        }
        this.rating = rate;
    }

    public String getComment() {
        return comment;
    }

}
