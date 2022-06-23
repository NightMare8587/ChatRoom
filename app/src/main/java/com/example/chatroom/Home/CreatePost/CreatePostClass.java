package com.example.chatroom.Home.CreatePost;

public class CreatePostClass {
    public String title,userName,userEmail,upVotes,downVotes,creatorID;

    public CreatePostClass(String title, String userName, String userEmail, String upVotes, String downVotes, String creatorID) {
        this.title = title;
        this.userName = userName;
        this.userEmail = userEmail;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
        this.creatorID = creatorID;
    }
}
