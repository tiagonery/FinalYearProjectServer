package server.model;

import java.io.Serializable;

/**
 * Created by Tiago on 22/03/2016.
 */
public class UserWish implements Serializable{

    private String userId;
    private int wishId;
//    private UserWishState state;

//    public enum UserWishState {
//        IN(1),
//        OUT(2),
//        OWNER(2);
//
//
//        private final int num;
//
//        private UserWishState(int num)
//        {
//            this.num = num;
//        }
//
//        public int getNumber()
//        {
//            return num;
//        }
//    }

    /**
     * @param userId
     * @param wishId
     */
    public UserWish(String userId, int wishId) {
        this.userId = userId;
        this.wishId = wishId;
    }

    public UserWish() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getWishId() {
        return wishId;
    }

    public void setWishId(int wishId) {
        this.wishId = wishId;
    }

//    public UserWishState getState() {
//        return state;
//    }
//
//    public void setState(UserWishState state) {
//        this.state = state;
//    }


}
