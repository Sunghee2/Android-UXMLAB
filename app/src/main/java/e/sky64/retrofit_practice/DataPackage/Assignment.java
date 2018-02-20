package e.sky64.retrofit_practice.DataPackage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by seungyeonlee on 2018. 2. 20..
 */

public class Assignment {
    @SerializedName("courseNo")
    @Expose
    private String courseNo;

    @SerializedName("asName")
    @Expose
    private String asName;

    @SerializedName("asContent")
    @Expose
    private String asContent;

    @SerializedName("asDue")
    @Expose
    private String asDue;
}
