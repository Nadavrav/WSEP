package DomainLayer.Stores;

import DomainLayer.Response;

import java.util.Map;

public class Rating {
    int rate;
    String comment;

    public Rating (int rate) {
        if (rate > 5 || rate < 0) {
            throw new IllegalArgumentException("Your Rate must be between 0-5 ");
        }
        this.rate = rate;
    }
    public Rating (int rate,String comment){
        if(comment == null)
            throw new NullPointerException("Comment cant be null");
        if (rate > 5 || rate < 0) {
            throw new IllegalArgumentException("Your Rate must be between 0-5 ");
        }
        this.rate = rate;
        this.comment=comment;
    }

    public int getRate() {
        return rate;
    }

    public void addComment(String comment){
        if(comment == null)
            throw new NullPointerException("Comment cant be null");
        this.comment = comment;
    }
    public void addRate(int rate) {
        if (rate > 5 || rate < 0) {
            throw new IllegalArgumentException("Your Rate must be between 0-5 ");
        }
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

}
