# Comment tone

This part of project receives feedbacks for stocktrader application and send it to account project.

###Data structure

    public class CommentTone {
        public String id;
        public String message;
        public String *sentiment;
    }

*_In this case the sentiment would be generated from random numbers from 0 to 10 (0-3 is negative, 4-7 is neutral, 8-10 is positive)_   

