package e.sky64.retrofit_practice.DataPackage;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hh960 on 2018-02-20.
 */

public class Board {


    @SerializedName("board_title")
    private String board_title;
    @SerializedName("board_content")
    private String board_content;
    @SerializedName("author")
    private String author;
    @SerializedName("board_date")
    private String board_date;

    @SerializedName("course_no")
    private int course_no;


    public Board(String board_title, String board_content, String author, String board_date, int course_no) {
        this.board_title = board_title;
        this.board_content = board_content;
        this.author = author;
        this.board_date=board_date;
        this.course_no=course_no;
    }


    public void setBoard_title(String board_title) {
        this.board_title = board_title;
    }

    public void setBoard_content(String board_content) {
        this.board_content = board_content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBoard_title() {
        return board_title;
    }

    public String getBoard_content() {
        return board_content;
    }

    public String getAuthor() {
        return author;
    }
    public void setBoard_date(String board_date) {
        this.board_date = board_date;
    }

    public String getBoard_date() {
        return board_date;
    }


    public int getCourse_no() {
        return course_no;
    }

    public void setCourse_no(int course_no) {
        this.course_no = course_no;
    }

}
