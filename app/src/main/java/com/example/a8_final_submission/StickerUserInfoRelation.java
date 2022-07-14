package com.example.a8_final_submission;

import java.io.Serializable;

public class StickerUserInfoRelation implements Serializable {

    String stickerId;
    String senderUsername;
    String receiverUserName;
    String stickerRecivedDate;

    // to firebase getValue to map it into a object
    public StickerUserInfoRelation(){

    }

    public StickerUserInfoRelation(String stickerId, String senderUsername, String receiverUserName, String stickerRecivedDate) {
        this.stickerId = stickerId;
        this.senderUsername = senderUsername;
        this.receiverUserName = receiverUserName;
        this.stickerRecivedDate = stickerRecivedDate;
    }

    public String getStickerId() {
        return stickerId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getReceiverUserName() {
        return receiverUserName;
    }

    public String getStickerRecivedDate() {
        return stickerRecivedDate;
    }

    public void setStickerId(String stickerId) {
        this.stickerId = stickerId;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public void setReceiverUserName(String receiverUserName) {
        this.receiverUserName = receiverUserName;
    }

    public void setStickerRecivedDate(String stickerRecivedDate) {
        this.stickerRecivedDate = stickerRecivedDate;
    }

    @Override
    public String toString() {
        return "StickerUserInfoRelation{" +
                "stickerId='" + stickerId + '\'' +
                ", senderUsername='" + senderUsername + '\'' +
                ", receiverUserName='" + receiverUserName + '\'' +
                ", stickerRecivedDate='" + stickerRecivedDate + '\'' +
                '}';
    }
}
