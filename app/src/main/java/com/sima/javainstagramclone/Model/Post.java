package com.sima.javainstagramclone.Model;

public class Post {

   public  String Mail;
   public String DownloadUrl;
    public String Comment;

    public Post(String Mail,String DownloadUrl,String Comment){
        this.Comment=Comment;
        this.DownloadUrl=DownloadUrl;
        this.Mail=Mail;
    }
}
